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
import pl.lodz.it.sitodruk.SecurityConsts;
import pl.lodz.it.sitodruk.dto.CategoryDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.ApplicationOptimisticLockException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.ProductAlreadyExistsException;
import pl.lodz.it.sitodruk.exceptions.ProductNotFoundException;
import pl.lodz.it.sitodruk.service.ProductCategoryService;
import pl.lodz.it.sitodruk.service.ProductService;

import javax.annotation.security.PermitAll;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/app/products")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER, transactionManager = "mopTransactionManager")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;


    @GetMapping("/categories")
    @PermitAll
    public ResponseEntity<List<CategoryDTO>> getAllProductCategories() {
        try {
            return new ResponseEntity(productCategoryService.getAllProductCategories(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.EMPLOYEE + "')")
    public ResponseEntity<?> editProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.modifyProduct(productDTO);
            return ResponseEntity.ok("");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.EMPLOYEE + "')")
    public ResponseEntity<List<ProductDTO>> getAllEmployeeProducts() {
        try {
            return new ResponseEntity(productService.findAllProducts(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @GetMapping("/findAllActive")
    @PermitAll
    public ResponseEntity<List<ProductDTO>> getAllActiveProducts() {
        try {
            return new ResponseEntity(productService.findAllActiveProducts(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @GetMapping("/findByName/{name}")
    @PermitAll
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        try {
            return new ResponseEntity(productService.findProductByName(name), HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


    @PostMapping("/addNew")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.EMPLOYEE + "')")
    public ResponseEntity<?> addNewProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok("product.added");
        } catch (ProductAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/activateProduct")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.EMPLOYEE + "')")
    public ResponseEntity<?> activateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.activateProduct(productDTO);
            return ResponseEntity.ok("product.activated");
        } catch (ApplicationOptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/deactivateProduct")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.EMPLOYEE + "')")
    public ResponseEntity<?> deactivateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.deactivateProduct(productDTO);
            return ResponseEntity.ok("product.deactivated");
        } catch (ApplicationOptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }
}
