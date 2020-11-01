package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapper;
import pl.lodz.it.sitodruk.dto.mappers.UserMapper;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.ProductNotFoundException;
import pl.lodz.it.sitodruk.model.ProductEntity;
import pl.lodz.it.sitodruk.repositories.ProductRepository;
import pl.lodz.it.sitodruk.service.ProductService;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProduct(ProductDTO productDTO) throws BaseException {
        productRepository.saveAndFlush(ProductMapper.INSTANCE.createNewProduct(productDTO));

    }

    @Override
    public void modifyProduct(ProductDTO productDTO) throws BaseException {
        productRepository.findByNameAndCategoryName(productDTO.getName(),productDTO.getCategoryName());
    }

    @Override
    public ProductDTO findProductByName(String productName) throws BaseException {
        Optional<ProductEntity> product = productRepository.findByName(productName);
        ProductDTO productDTO;
        if(product.isPresent()){
            productDTO = ProductMapper.INSTANCE.toProductDTO(product.get());
            productDTO.setDtoVersion(String.valueOf(product.get().getVersion()*product.get().hashCode()));
        }else{
            throw new ProductNotFoundException();
        }
        return productDTO;
    }
}
