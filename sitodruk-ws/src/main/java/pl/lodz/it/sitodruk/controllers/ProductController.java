package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.ApplicationOptimisticLockException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.service.ProductCategoryService;
import pl.lodz.it.sitodruk.service.ProductService;

import javax.annotation.security.PermitAll;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
@Transactional(propagation = Propagation.NEVER, transactionManager = "mopTransactionManager")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private Properties exceptionProperties;

    @GetMapping("/categories")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<List<CategoryDTO>> getAllProductCategories() {
        try {
            return new ResponseEntity(productCategoryService.getAllProductCategories(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @PostMapping("/edit")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<?> editProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.modifyProduct(productDTO);
            return ResponseEntity.ok("");
        }catch(ApplicationOptimisticLockException ex){
            throw new ResponseStatusException(HttpStatus.LOCKED,"optimistic.lock",ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @GetMapping("/findAll")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            return new ResponseEntity(productService.findAllProducts(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @GetMapping("/findByName/{name}")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        try {
            return new ResponseEntity(productService.findProductByName(name), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }


    @PostMapping("/addNew")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<?> addNewProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok("");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

    @PostMapping("/removeProduct")
//    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    @PermitAll
    public ResponseEntity<?> removeProductByName(@RequestBody ProductDTO productDTO) {
        try {
            productService.removeProductByName(productDTO.getName());
            return ResponseEntity.ok("");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }
}
