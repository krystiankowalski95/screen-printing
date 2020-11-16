package pl.lodz.it.sitodruk.dto.payu;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class BlikData implements Serializable {
    private boolean register;
}
