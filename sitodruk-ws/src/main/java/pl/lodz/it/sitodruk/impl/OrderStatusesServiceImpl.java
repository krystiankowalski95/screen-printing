package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.OrderStatusDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.moz.OrderStatusRepository;
import pl.lodz.it.sitodruk.service.OrderStatusService;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW , transactionManager = "mozTransactionManager")
public class OrderStatusesServiceImpl implements OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatusDTO findOrderStatusByName(String orderStatus) throws BaseException {
        return null;
    }
}

