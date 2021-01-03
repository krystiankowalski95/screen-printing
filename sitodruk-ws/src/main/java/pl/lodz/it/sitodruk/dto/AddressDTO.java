package pl.lodz.it.sitodruk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String country;
    private String voivodeship;
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
}
