package pl.lodz.it.sitodruk.dto.payu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPayU implements Serializable {
    private String unitPrice;
    private String quantity;
    private String name;
}
