package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.model.mop.CategoryEntity;

import java.util.List;

public interface ProductCategoryService {
    List<CategoryDTO> getAllProductCategories() throws BaseException;
}
