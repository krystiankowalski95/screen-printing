package pl.lodz.it.sitodruk.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private boolean isActive;
    private String categoryName;
    private String dtoVersion;
    private Long quantity;
    private Long stock;
}
