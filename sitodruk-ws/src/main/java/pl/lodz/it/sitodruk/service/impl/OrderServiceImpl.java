package pl.lodz.it.sitodruk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.controllers.PayUController;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.AddressMapper;
import pl.lodz.it.sitodruk.dto.mappers.OrderMapper;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapperMoz;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.model.moz.*;
import pl.lodz.it.sitodruk.repositories.moz.OrderRepository;
import pl.lodz.it.sitodruk.repositories.moz.OrderStatusRepository;
import pl.lodz.it.sitodruk.repositories.moz.ProductRepositoryMoz;
import pl.lodz.it.sitodruk.repositories.moz.UserRepositoryMoz;
import pl.lodz.it.sitodruk.service.OrderService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Retryable(maxAttempts = 5, include = {SQLException.class})
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, transactionManager = "mozTransactionManager",rollbackFor = BaseException.class)
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

    @Autowired
    private Environment env;

    @Override
    public void createOrder(OrderDTO orderDTO) throws BaseException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(orderDTO.getUsername());
        if (userEntity.isPresent()) {
            OrderEntity orderEntity = new OrderEntity();
            for (ProductDTO product : orderDTO.getProducts()) {
                Optional<ProductEntity> prdEnt = productRepository.findByNameAndStockGreaterThanEqual(product.getName(), product.getQuantity());
                if (prdEnt.isPresent()) {
                    if(prdEnt.get().getIsActive()){
                        prdEnt.get().setStock(prdEnt.get().getStock() - product.getQuantity());
                        OrderProductEntity orderProductEntity = new OrderProductEntity();
                        orderProductEntity.setOrderEntity(orderEntity);
                        orderProductEntity.setProductEntity(prdEnt.get());
                        orderProductEntity.setAmount(product.getQuantity().intValue());
                        orderEntity.getOrderProductEntities().add(orderProductEntity);
                    }else{
                        throw new ProductNotAvailableException();
                    }

                } else {
                    throw new InsufficientStockException();
                }
            }
            orderEntity.setTotalValue(orderDTO.getTotalValue());
            orderEntity.setAddressByAddressId(new AddressEntity());
            orderEntity.getAddressByAddressId().setCountry(orderDTO.getAddressDTO().getCountry());
            orderEntity.getAddressByAddressId().setCity(orderDTO.getAddressDTO().getCity());
            orderEntity.getAddressByAddressId().setVoivodeship(orderDTO.getAddressDTO().getVoivodeship());
            orderEntity.getAddressByAddressId().setPostalCode(orderDTO.getAddressDTO().getPostalCode());
            orderEntity.getAddressByAddressId().setStreet(orderDTO.getAddressDTO().getStreet());
            orderEntity.getAddressByAddressId().setStreetNumber(orderDTO.getAddressDTO().getStreetNumber());
            orderEntity.setPayUOrderId(payUController.payuTransaction(userEntity.get(), orderDTO));
            orderEntity.setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.getPayUOrderId())));
            orderEntity.setUsername(userEntity.get().getUsername());
            orderRepository.saveAndFlush(orderEntity);
        } else throw new UserNotFoundException();
    }

    @Override
    public void cancelOrder(OrderDTO orderDTO) throws BaseException {
        Optional<OrderEntity> orderEntity = orderRepository.findByPayUOrderId(orderDTO.getPayUOrderId());
        OrderStatusEntity cancelled = orderStatusRepository.findByStatusName("cancelled");

        if (orderEntity.isPresent()) {
            if (String.valueOf(orderDTO.getDtoVersion()).equals(getOrderVersionHash(orderEntity.get()))) {
                for (ProductDTO product : orderDTO.getProducts()) {
                    ProductEntity prdEnt = productRepository.findByNameAndCategoryName(product.getName(),product.getCategoryName());
                    if (String.valueOf(product.getDtoVersion()).equals(getProductVersionHash(prdEnt))) {
                        for (OrderProductEntity orderProductEntity: orderEntity.get().getOrderProductEntities()){
                            if(orderProductEntity.getProductEntity().equals(prdEnt)){
                                orderProductEntity.getProductEntity().setStock(prdEnt.getStock() + orderProductEntity.getAmount());
                            }
                        }
                        productRepository.saveAndFlush(prdEnt);
                    }else{
                        throw new ApplicationOptimisticLockException();
                    }
                }
                orderEntity.get().setOrderStatus(cancelled);
                orderRepository.saveAndFlush(orderEntity.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else {
            throw new OrderNotFoundException();
        }
    }

    @Override
    public void markOrderAsCompleted(OrderDTO orderDTO) throws BaseException {
        Optional<OrderEntity> orderEntity = orderRepository.findByPayUOrderId(orderDTO.getPayUOrderId());
        OrderStatusEntity completed = orderStatusRepository.findByStatusName("completed");
        if (orderEntity.isPresent()) {
            if (String.valueOf(orderDTO.getDtoVersion()).equals(getOrderVersionHash(orderEntity.get()))) {
                orderEntity.get().setOrderStatus(completed);
                orderRepository.saveAndFlush(orderEntity.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else {
            throw new OrderNotFoundException();

        }
    }

    @Override
    public void repeatPayment(OrderDTO orderDTO) throws BaseException {
        Optional<OrderEntity> orderEntity = orderRepository.findByPayUOrderId(orderDTO.getPayUOrderId());
        Optional<UserEntity> userEntity = userRepository.findByUsername(orderDTO.getUsername());
        OrderStatusEntity created = orderStatusRepository.findByStatusName("created");
        if(orderEntity.isPresent()){
            if(orderEntity.get().getOrderStatus().equals(created)){
                orderEntity.get().setPayUOrderId(payUController.payuTransaction(userEntity.get(), orderDTO));
                orderEntity.get().setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.get().getPayUOrderId())));
                orderRepository.saveAndFlush(orderEntity.get());
            }else{
                throw new InvalidOrderStatusException();
            }
        } else throw new OrderNotFoundException();
    }

    @Override
    public List<OrderDTO> findUsersOrders(String username) throws BaseException, SQLException {
        List<OrderDTO> orderDtos = new ArrayList<>();
        OrderStatusEntity pending = orderStatusRepository.findByStatusName("pending");
        List<OrderEntity> orderEntities = orderRepository.findAllByUsername(username);
        for (OrderEntity orderEntity : orderEntities) {
            if (orderEntity.getOrderStatus().getStatusName().equalsIgnoreCase(pending.getStatusName())) {
                orderEntity.setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.getPayUOrderId())));
                orderRepository.saveAndFlush(orderEntity);
            }
            OrderDTO orderDTO = OrderMapper.INSTANCE.toOrderDTO(orderEntity);
            for(OrderProductEntity ope : orderEntity.getOrderProductEntities()){
                ProductDTO productDTO = ProductMapperMoz.INSTANCE.toProductDTO(ope.getProductEntity());
                productDTO.setDtoVersion(getProductVersionHash(ope.getProductEntity()));
                productDTO.setStock(ope.getAmount().longValue());
                productDTO.setQuantity(ope.getAmount().longValue());
                orderDTO.getProducts().add(productDTO);
            }
            orderDTO.setAddressDTO(AddressMapper.INSTANCE.toAddressDTO(orderEntity.getAddressByAddressId()));
            orderDTO.setDtoVersion(getOrderVersionHash(orderEntity));
            orderDtos.add(orderDTO);
        }
        return orderDtos;
    }


    @Override
    public List<OrderDTO> findAllOrders() throws BaseException, SQLException {
        List<OrderDTO> orderDtos = new ArrayList<>();
        OrderStatusEntity pending = orderStatusRepository.findByStatusName("pending");
        for (OrderEntity orderEntity : orderRepository.findAll()) {
            if (orderEntity.getOrderStatus().getStatusName().equalsIgnoreCase(pending.getStatusName())) {
                orderEntity.setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.getPayUOrderId())));
                orderRepository.saveAndFlush(orderEntity);
            }
            OrderDTO orderDTO = OrderMapper.INSTANCE.toOrderDTO(orderEntity);
            for(OrderProductEntity ope : orderEntity.getOrderProductEntities()){
                ProductDTO productDTO = ProductMapperMoz.INSTANCE.toProductDTO(ope.getProductEntity());
                productDTO.setDtoVersion(getProductVersionHash(ope.getProductEntity()));
                productDTO.setStock(ope.getAmount().longValue());
                productDTO.setQuantity(ope.getAmount().longValue());
                orderDTO.getProducts().add(productDTO);
            }
            orderDTO.setAddressDTO(AddressMapper.INSTANCE.toAddressDTO(orderEntity.getAddressByAddressId()));
            orderDTO.setDtoVersion(getOrderVersionHash(orderEntity));
            orderDtos.add(orderDTO);
        }
        return orderDtos;
    }

    @Override
    public OrderDTO findOrderByPayuOrderId(OrderDTO orderDTO) throws BaseException, SQLException  {
        OrderStatusEntity pending = orderStatusRepository.findByStatusName("pending");
        Optional<OrderEntity> orderEntity = orderRepository.findByPayUOrderId(orderDTO.getPayUOrderId());
        if(orderEntity.isPresent()){
            if (orderEntity.get().getOrderStatus().getStatusName().equalsIgnoreCase(pending.getStatusName())) {
                orderEntity.get().setOrderStatus(orderStatusRepository.findByStatusName(payUController.getPaymentStatus(orderEntity.get().getPayUOrderId())));
                orderRepository.saveAndFlush(orderEntity.get());
            }
            OrderDTO dto = OrderMapper.INSTANCE.toOrderDTO(orderEntity.get());
            for(OrderProductEntity ope : orderEntity.get().getOrderProductEntities()){
                ProductDTO productDTO = ProductMapperMoz.INSTANCE.toProductDTO(ope.getProductEntity());
                productDTO.setDtoVersion(getProductVersionHash(ope.getProductEntity()));
                productDTO.setStock(ope.getAmount().longValue());
                productDTO.setQuantity(ope.getAmount().longValue());
                dto.getProducts().add(productDTO);
            }
            dto.setAddressDTO(AddressMapper.INSTANCE.toAddressDTO(orderEntity.get().getAddressByAddressId()));
            dto.setDtoVersion(getOrderVersionHash(orderEntity.get()));
            return dto;
        }else throw new OrderNotFoundException();
    }

    public String getOrderVersionHash(OrderEntity orderEntity) {
        String dtoSecret=env.getProperty("thesis.app.dtoSecret");
        return DigestUtils.sha256Hex(orderEntity.getId() + dtoSecret + orderEntity.getVersion());
    }

    public String getProductVersionHash(ProductEntity productEntity) {
        String dtoSecret=env.getProperty("thesis.app.dtoSecret");
        return DigestUtils.sha256Hex(productEntity.getId() + dtoSecret + productEntity.getVersion());
    }
}
