package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.CategoryDTO;

public interface CategoryService {
    CategoryDTO findCategoryByCategoryName(String category);
}
