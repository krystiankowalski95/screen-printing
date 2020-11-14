package pl.lodz.it.sitodruk.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayMethodPayU implements Serializable {
 private String type;
 private String authorizationCode;
 private BlikData blikData;
}
