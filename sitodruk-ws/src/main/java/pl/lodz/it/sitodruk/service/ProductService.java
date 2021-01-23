package pl.lodz.it.sitodruk.service;


import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws BaseException;
    void modifyProduct(ProductDTO productDTO) throws BaseException;
    void deactivateProduct(ProductDTO productDTO) throws BaseException;
    void activateProduct(ProductDTO productDTO) throws BaseException;
    ProductDTO findProductByName(String productName) throws BaseException;
    List<ProductDTO> findAllProducts() throws  BaseException;
    List<ProductDTO> findAllActiveProducts() throws  BaseException;
}
