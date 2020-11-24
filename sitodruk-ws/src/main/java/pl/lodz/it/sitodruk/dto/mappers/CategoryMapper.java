package pl.lodz.it.sitodruk.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.model.mop.CategoryEntity;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryDTO toCategoryDto(CategoryEntity categoryEntity);
    Collection<CategoryDTO> toCategoryDTOCollection(Collection<CategoryEntity> categoryEntities);

}
