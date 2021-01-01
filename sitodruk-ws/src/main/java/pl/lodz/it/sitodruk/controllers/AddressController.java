package pl.lodz.it.sitodruk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.service.AddressService;
import pl.lodz.it.sitodruk.service.ProductCategoryService;
import pl.lodz.it.sitodruk.service.ProductService;
import pl.lodz.it.sitodruk.utils.ResourceBundles;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.Properties;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/address")
@Transactional(propagation = Propagation.NEVER, transactionManager = "mokTransactionManager")
public class AddressController {

    @Autowired
    private AddressService addressService;

    private Properties exceptionProperties;

    @PostConstruct
    private void init() {
        try {
            exceptionProperties = ResourceBundles.loadProperties("exception.properties");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }


    @PostMapping("/addNew")
//    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PermitAll
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDTO addressDTO) {
        try {
            addressService.createAddress(addressDTO);
            return ResponseEntity.ok("");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
        }
    }

//    @PostMapping("/remove")
////    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
//    @PermitAll
//    public ResponseEntity<?> removeAddress(@RequestBody AddressDTO addressDTO) {
//        try {
//            addressService.removeAddress(addressDTO);
//            return ResponseEntity.ok("");
//        } catch (BaseException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
//        }
//    }
//
//    @GetMapping("/addresses")
////    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
//    @PermitAll
//    public ResponseEntity<List<AddressDTO>> getAllUserAddresses(@RequestBody String username) {
//        try {
//            return new ResponseEntity(addressService.findAddressByUsername(username), HttpStatus.OK);
//        } catch (BaseException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exceptionProperties.getProperty("unexpected.error"));
//        }
//    }


}
