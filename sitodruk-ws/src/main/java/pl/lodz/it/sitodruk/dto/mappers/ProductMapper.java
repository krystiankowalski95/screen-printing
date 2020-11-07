package pl.lodz.it.sitodruk.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.model.mop.CategoryEntity;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductDTO toProductDTO(ProductEntity productEntity);
    ProductEntity createNewProduct(ProductDTO productDTO);
    Collection<ProductDTO> toProductDTOCollection(Collection<ProductEntity> productEntities);
    default Collection<String> toCategoryStringCollection(Collection<CategoryEntity> categoryEntities) {
        return categoryEntities.stream()
                .map(CategoryEntity::getCategoryName)
                .collect(Collectors.toList());
    }
    default Collection<ProductEntity> toCategoryCollection(Collection<String> productsCollection) {
        return new ArrayList<>();
    }
}
