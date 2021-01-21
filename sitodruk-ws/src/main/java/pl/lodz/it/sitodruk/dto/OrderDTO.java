package pl.lodz.it.sitodruk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String payUOrderId;
    private Collection<ProductDTO> products = new ArrayList<>();
    private Double totalValue;
    private String username;
    private Long blikCode;
    private AddressDTO addressDTO;
    private String ipAddress;
    private String orderStatus;
    private String dtoVersion;
}
