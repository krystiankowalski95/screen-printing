package pl.lodz.it.sitodruk.dto.payu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyerPayU implements Serializable {
    private String extCustomerId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String language;

    @JsonProperty("extCustomerId")
    private String getExtCustomerId(){
        return firstName+lastName;
    }
}
