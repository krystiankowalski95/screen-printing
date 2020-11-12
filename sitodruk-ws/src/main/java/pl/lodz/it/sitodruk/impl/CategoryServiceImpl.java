package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.service.CategoryService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW , transactionManager = "mopTransactionManager")
public class CategoryServiceImpl implements CategoryService {

    @Override
    public CategoryDTO findCategoryByCategoryName(String category) {
        return null;
    }
}
