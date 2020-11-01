package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CategoryDTO findCategoryByCategoryName(String category) {
        return null;
    }
}
