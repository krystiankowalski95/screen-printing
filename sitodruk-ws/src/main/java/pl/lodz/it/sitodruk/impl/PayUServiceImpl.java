package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.service.PayUService;

@Service
public class PayUServiceImpl implements PayUService {

    @Value("${thesis.app.payu.client.id}")
    private String client_id;
    @Value("${thesis.app.payu.client.secret}")
    private String client_secret;
    @Value("${thesis.app.payu.url}")
    private String OauthUrl;
    @Value("${thesis.app.payu.api.url}")
    private String payuApiUrl;

    @Override
    public void getAuthorizationToken() {

    }

    @Override
    public void createNewTransaction(OrderDTO orderDTO) {

    }

    @Override
    public String getTransactionDetails(OrderDTO orderDTO) {
        return null;
    }
}
