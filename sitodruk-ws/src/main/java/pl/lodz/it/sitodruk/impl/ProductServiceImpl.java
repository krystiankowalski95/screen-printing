package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapper;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.ProductAlreadyExistsException;
import pl.lodz.it.sitodruk.exceptions.ProductNotFoundException;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;
import pl.lodz.it.sitodruk.repositories.mop.ProductRepository;
import pl.lodz.it.sitodruk.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProduct(ProductDTO productDTO) throws BaseException {
//        if(productRepository.existsByNameAndCategoryName(productDTO.getName(),productDTO.getCategoryName())){
//            throw new ProductAlreadyExistsException();
//        }else {
            productRepository.saveAndFlush(ProductMapper.INSTANCE.createNewProduct(productDTO));
//        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void modifyProduct(ProductDTO productDTO) throws BaseException {
        productRepository.findByNameAndCategoryName(productDTO.getName(),productDTO.getCategoryName());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeProductByName(String name) throws BaseException {
        Optional<ProductEntity> productEntity = productRepository.findByName(name);
        if(productEntity.isPresent()){
            productRepository.delete(productEntity.get());
            productRepository.flush();
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProductDTO findProductByName(String productName) throws BaseException {
        Optional<ProductEntity> product = productRepository.findByName(productName);
        if(product.isPresent()){
            return ProductMapper.INSTANCE.toProductDTO(product.get());
        }else{
            throw new ProductNotFoundException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ProductDTO> findAllProducts() throws BaseException {
        List<ProductDTO> products = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findAll();
        for (ProductEntity prod: productEntities) {
            ProductDTO productDTO = ProductMapper.INSTANCE.toProductDTO(prod);
            products.add(productDTO);
        }
        return products;
    }
}
