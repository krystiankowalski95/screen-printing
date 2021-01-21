package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.util.List;

public interface OrderService {
    void createOrder(OrderDTO orderDTO) throws BaseException;
    void cancelOrder(OrderDTO orderDTO) throws BaseException;
    void markOrderAsCompleted(OrderDTO orderDTO) throws BaseException;
    void repeatPayment(OrderDTO orderDTO) throws BaseException;
    List<OrderDTO> findUsersOrders(String username) throws BaseException;
    List<OrderDTO> findAllOrders() throws BaseException;
    OrderDTO findOrderByPayuOrderId(OrderDTO orderDTO) throws BaseException;
}
