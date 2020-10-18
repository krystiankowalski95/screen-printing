package pl.lodz.it.sitodruk.service;


import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws BaseException;
    void modifyProduct(ProductDTO productDTO) throws BaseException;
    ProductDTO findProductByName(String productName) throws BaseException;
}
