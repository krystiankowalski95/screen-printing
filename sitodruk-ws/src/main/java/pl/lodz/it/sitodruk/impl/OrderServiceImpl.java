package pl.lodz.it.sitodruk.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.controllers.PayUController;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.payu.BuyerPayU;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.UserNotFoundException;
import pl.lodz.it.sitodruk.model.moz.UserEntity;
import pl.lodz.it.sitodruk.repositories.moz.OrderRepository;
import pl.lodz.it.sitodruk.repositories.moz.UserRepositoryMoz;
import pl.lodz.it.sitodruk.service.OrderService;

import java.util.Optional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW , transactionManager = "mozTransactionManager")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepositoryMoz userRepository;

    @Autowired
    private PayUController payUController;

    @Override
    public void createOrder(OrderDTO orderDTO) throws BaseException {
        Optional<UserEntity> userEntity = userRepository.findByUsername(orderDTO.getUsername());
        if(userEntity.isPresent()){
           String orderId =  payUController.payuTransaction(userEntity.get(),orderDTO);
           log.error(orderId);
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
