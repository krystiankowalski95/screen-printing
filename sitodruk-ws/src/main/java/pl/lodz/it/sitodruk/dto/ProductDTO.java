package pl.lodz.it.sitodruk.dto;

import lombok.Data;
import pl.lodz.it.sitodruk.model.CategoryEntity;

@Data
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private Boolean isActive;
    private String categoryName;
    private String dtoVersion;
}
