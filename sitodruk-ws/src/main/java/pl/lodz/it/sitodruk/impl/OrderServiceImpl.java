package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.moz.OrderRepository;
import pl.lodz.it.sitodruk.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createOrder(OrderDTO orderDTO) throws BaseException {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void modifyOrder(OrderDTO orderDTO) throws BaseException {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderDTO findOrderBy(String username) throws BaseException {
        return null;
    }
}
