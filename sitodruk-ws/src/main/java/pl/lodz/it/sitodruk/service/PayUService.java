package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.OrderDTO;

public interface PayUService {
    void getAuthorizationToken();
    void createNewTransaction(OrderDTO orderDTO);
    String getTransactionDetails(OrderDTO orderDTO);
}
