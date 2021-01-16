package pl.lodz.it.sitodruk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String payUOrderId;
    private Collection<ProductDTO> products;
    private Double totalValue;
    private String username;
    private Long blikCode;
    private AddressDTO addressDTO;
    private String orderStatus;
    private String ipAddress;
    private String dtoVersion;
}
