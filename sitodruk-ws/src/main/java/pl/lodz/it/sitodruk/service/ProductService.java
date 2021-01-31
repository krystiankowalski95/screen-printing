package pl.lodz.it.sitodruk.service;


import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    void createProduct(ProductDTO productDTO) throws BaseException, SQLException;
    void modifyProduct(ProductDTO productDTO) throws BaseException, SQLException;
    void deactivateProduct(ProductDTO productDTO) throws BaseException, SQLException;
    void activateProduct(ProductDTO productDTO) throws BaseException, SQLException;
    ProductDTO findProductByName(String productName) throws BaseException, SQLException;
    List<ProductDTO> findAllProducts() throws  BaseException, SQLException;
    List<ProductDTO> findAllActiveProducts() throws  BaseException, SQLException;
}
