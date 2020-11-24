package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.CategoryMapper;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapper;
import pl.lodz.it.sitodruk.model.mop.CategoryEntity;
import pl.lodz.it.sitodruk.repositories.mop.ProductCategoryRepository;
import pl.lodz.it.sitodruk.service.ProductCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "mopTransactionManager")
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<CategoryDTO> getAllProductCategories() {
        List<CategoryDTO> categories = new ArrayList<>();
        for(CategoryEntity category : productCategoryRepository.findAll()){
            categories.add(new CategoryDTO(category.getCategoryName()));
        }
      return categories;
    }
}
