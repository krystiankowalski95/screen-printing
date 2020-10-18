package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

public interface OrderService {
    void createOrder(OrderDTO orderDTO) throws BaseException;
    void modifyOrder(OrderDTO orderDTO) throws BaseException;
    OrderDTO findOrderBy(String username) throws BaseException;
}
