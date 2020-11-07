package pl.lodz.it.sitodruk.service;


import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws BaseException;
    void modifyProduct(ProductDTO productDTO) throws BaseException;
    void removeProductByName(String name) throws BaseException;
    ProductDTO findProductByName(String productName) throws BaseException;
    List<ProductDTO> findAllProducts() throws  BaseException;
}
