package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.it.sitodruk.dto.OrderStatusDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.OrderStatusRepository;
import pl.lodz.it.sitodruk.service.OrderStatusService;

@Service
public class OrderStatusesServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatusDTO findOrderStatusByName(String orderStatus) throws BaseException {
        return null;
    }
}

