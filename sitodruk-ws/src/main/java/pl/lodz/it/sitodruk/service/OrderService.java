package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    void createOrder(OrderDTO orderDTO) throws BaseException, SQLException;
    void cancelOrder(OrderDTO orderDTO) throws BaseException, SQLException;
    void markOrderAsCompleted(OrderDTO orderDTO) throws BaseException, SQLException;
    void repeatPayment(OrderDTO orderDTO) throws BaseException, SQLException;
    List<OrderDTO> findUsersOrders(String username) throws BaseException, SQLException;
    List<OrderDTO> findAllOrders() throws BaseException, SQLException;
    OrderDTO findOrderByPayuOrderId(OrderDTO orderDTO) throws BaseException, SQLException;
}
