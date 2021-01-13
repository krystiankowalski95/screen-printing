package pl.lodz.it.sitodruk.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.controllers.PayUController;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapper;
import pl.lodz.it.sitodruk.dto.payu.BuyerPayU;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.InsufficientStockException;
import pl.lodz.it.sitodruk.exceptions.UserNotFoundException;
import pl.lodz.it.sitodruk.model.moz.*;
import pl.lodz.it.sitodruk.repositories.moz.OrderRepository;
import pl.lodz.it.sitodruk.repositories.moz.OrderStatusRepository;
import pl.lodz.it.sitodruk.repositories.moz.ProductRepositoryMoz;
import pl.lodz.it.sitodruk.repositories.moz.UserRepositoryMoz;
import pl.lodz.it.sitodruk.service.OrderService;

import java.util.Optional;

@Service
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW , transactionManager = "mozTransactionManager")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepositoryMoz productRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private UserRepositoryMoz userRepository;

    @Autowired
    private PayUController payUController;

    @Override
    public void createOrder(OrderDTO orderDTO) throws BaseException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(orderDTO.getUsername());
        if(userEntity.isPresent()){
           OrderEntity orderEntity = new OrderEntity();
           for(ProductDTO product : orderDTO.getProducts()){
               Optional<ProductEntity> prdEnt = productRepository.findByNameAndStockGreaterThanEqual(product.getName(),product.getQuantity());
               if(prdEnt.isPresent()){
                   prdEnt.get().setStock(prdEnt.get().getStock() - product.getQuantity());
                   orderEntity.getProductEntityList().add(prdEnt.get());
               }else{
                   throw new InsufficientStockException();
               }
           }
           orderEntity.setAddressByAddressId(new AddressEntity());
           orderEntity.getAddressByAddressId().setCountry(orderDTO.getAddressDTO().getCountry());
           orderEntity.getAddressByAddressId().setCity(orderDTO.getAddressDTO().getCity());
           orderEntity.getAddressByAddressId().setVoivodeship(orderDTO.getAddressDTO().getVoivodeship());
           orderEntity.getAddressByAddressId().setPostalCode(orderDTO.getAddressDTO().getPostalCode());
           orderEntity.getAddressByAddressId().setStreet(orderDTO.getAddressDTO().getStreet());
           orderEntity.getAddressByAddressId().setStreetNumber(orderDTO.getAddressDTO().getStreetNumber());
           orderEntity.setPayuOrderId(payUController.payuTransaction(userEntity.get(),orderDTO));
           orderEntity.setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.getPayuOrderId())));
           orderEntity.setUsername(userEntity.get().getUsername());
           orderRepository.saveAndFlush(orderEntity);
        }else throw new UserNotFoundException();
    }

    @Override
    public void modifyOrder(OrderDTO orderDTO) throws BaseException {

    }

    @Override
    public OrderDTO findOrderBy(String username) throws BaseException {
        return null;
    }
}
