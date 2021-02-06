package pl.lodz.it.sitodruk.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.mappers.ProductMapper;
import pl.lodz.it.sitodruk.exceptions.ApplicationOptimisticLockException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.ProductAlreadyExistsException;
import pl.lodz.it.sitodruk.exceptions.ProductNotFoundException;
import pl.lodz.it.sitodruk.model.mop.ProductEntity;
import pl.lodz.it.sitodruk.repositories.mop.ProductRepository;
import pl.lodz.it.sitodruk.service.ProductService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Retryable(maxAttempts = 5, include = {SQLException.class})
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW, transactionManager = "mopTransactionManager",rollbackFor = BaseException.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${thesis.app.dtoSecret}")
    private String dtoSecret;

    @Override
    public void createProduct(ProductDTO productDTO) throws BaseException, SQLException {
        if (productRepository.existsByNameAndCategoryName(productDTO.getName(), productDTO.getCategoryName())) {
            throw new ProductAlreadyExistsException();
        } else {
            ProductEntity productEntity = ProductMapper.INSTANCE.createNewProduct(productDTO);
            productEntity.setPrice(productDTO.getPrice());
            productEntity.setStock(productDTO.getStock());
            productRepository.saveAndFlush(productEntity);
        }
    }

    @Override
    public void modifyProduct(ProductDTO productDTO) throws BaseException, SQLException  {
        Optional<ProductEntity> productEntity = productRepository.findByNameAndCategoryName(productDTO.getName(), productDTO.getCategoryName());
        if (productEntity.isPresent()) {
            if (String.valueOf(productDTO.getDtoVersion()).equals(getVersionHash(productEntity.get()))) {
                productEntity.get().setPrice(productDTO.getPrice());
                productEntity.get().setStock(productDTO.getStock());
                productRepository.save(productEntity.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public void deactivateProduct(ProductDTO productDTO) throws BaseException, SQLException  {
        Optional<ProductEntity> productEntity = productRepository.findByNameAndCategoryName(productDTO.getName(), productDTO.getCategoryName());
        if (productEntity.isPresent()) {
            if (String.valueOf(productDTO.getDtoVersion()).equals(getVersionHash(productEntity.get()))) {
                productEntity.get().setActive(false);
                productRepository.saveAndFlush(productEntity.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public void activateProduct(ProductDTO productDTO) throws BaseException, SQLException  {
        Optional<ProductEntity> productEntity = productRepository.findByNameAndCategoryName(productDTO.getName(), productDTO.getCategoryName());
        if (productEntity.isPresent()) {
            if (String.valueOf(productDTO.getDtoVersion()).equals(getVersionHash(productEntity.get()))) {
                productEntity.get().setActive(true);
                productRepository.saveAndFlush(productEntity.get());
            } else {
                throw new ApplicationOptimisticLockException();
            }
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public ProductDTO findProductByName(String productName) throws BaseException, SQLException {
        Optional<ProductEntity> product = productRepository.findByName(productName);
        if (product.isPresent()) {
            ProductDTO productDTO = ProductMapper.INSTANCE.toProductDTO(product.get());
            productDTO.setIsActive(product.get().isActive());
            productDTO.setDtoVersion(getVersionHash(product.get()));
            return productDTO;
        } else {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public List<ProductDTO> findAllActiveProducts() throws BaseException, SQLException  {
        List<ProductDTO> products = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findAllByIsActiveIsTrue();
        for (ProductEntity prod : productEntities) {
            ProductDTO productDTO = ProductMapper.INSTANCE.toProductDTO(prod);
            productDTO.setIsActive(prod.isActive());
            productDTO.setDtoVersion(getVersionHash(prod));
            products.add(productDTO);
        }
        return products;
    }

    @Override
    public List<ProductDTO> findAllProducts() throws BaseException, SQLException  {
        List<ProductDTO> products = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findAll();
        for (ProductEntity prod : productEntities) {
            ProductDTO productDTO = ProductMapper.INSTANCE.toProductDTO(prod);
            productDTO.setIsActive(prod.isActive());
            productDTO.setDtoVersion(getVersionHash(prod));
            products.add(productDTO);
        }
        return products;
    }

    public String getVersionHash(ProductEntity productEntity) {
        return DigestUtils.sha256Hex(productEntity.getId() + dtoSecret+ productEntity.getVersion());
    }
}
