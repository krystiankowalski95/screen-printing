package pl.lodz.it.sitodruk.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.payload.MessageResponse;
import pl.lodz.it.sitodruk.service.ProductService;
import pl.lodz.it.sitodruk.utils.ResourceBundles;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
@Transactional(propagation = Propagation.NEVER, transactionManager = "mopTransactionManager")
public class ProductController {

    @Autowired
    private ProductService productService;

    private Properties exceptionProperties;

    @PostConstruct
    private void init() {
        try {
            exceptionProperties = ResourceBundles.loadProperties("exception.properties");
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
