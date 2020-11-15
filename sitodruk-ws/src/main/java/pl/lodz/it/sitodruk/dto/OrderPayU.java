package pl.lodz.it.sitodruk.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderPayU implements Serializable {
    private String notifyUrl;
    private String customerIp;
    private String merchantPosId;
    private String description;
    private String currencyCode;
    private String totalAmount;
    private String extOrderId;
    private BuyerPayU buyer;
    private String continueUrl;
    private List<ProductPayU> products = new ArrayList<>();
    private PayMethodsPayU payMethods;
}
