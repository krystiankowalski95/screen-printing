package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.OrderStatusDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

public interface OrderStatusService {
    OrderStatusDTO findOrderStatusByName(String orderStatus) throws BaseException;
}
