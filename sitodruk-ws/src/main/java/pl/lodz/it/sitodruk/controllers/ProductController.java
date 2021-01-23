package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.ApplicationOptimisticLockException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.ProductAlreadyExistsException;
import pl.lodz.it.sitodruk.exceptions.ProductNotFoundException;
import pl.lodz.it.sitodruk.service.ProductCategoryService;
import pl.lodz.it.sitodruk.service.ProductService;

import javax.annotation.security.PermitAll;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER, transactionManager = "mopTransactionManager")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private Properties exceptionProperties;

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<List<CategoryDTO>> getAllProductCategories() {
        try {
            return new ResponseEntity(productCategoryService.getAllProductCategories(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> editProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.modifyProduct(productDTO);
            return ResponseEntity.ok("");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @GetMapping("/findAllActive")
    @PermitAll
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        try {
            return new ResponseEntity(productService.findAllActiveProducts(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            return new ResponseEntity(productService.findAllProducts(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @GetMapping("/findByName/{name}")
    @PermitAll
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        try {
            return new ResponseEntity(productService.findProductByName(name), HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product.not.found");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }


    @PostMapping("/addNew")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> addNewProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok("product.added");
        } catch (ProductAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product.already.exists");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/activateProduct")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> activateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.activateProduct(productDTO);
            return ResponseEntity.ok("product.activated");
        } catch (ApplicationOptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock");
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product.not.found");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/deactivateProduct")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> deactivateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.deactivateProduct(productDTO);
            return ResponseEntity.ok("product.deactivated");
        } catch (ApplicationOptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock");
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "product.not.found");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }
}
