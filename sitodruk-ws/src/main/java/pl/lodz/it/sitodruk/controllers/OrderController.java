package pl.lodz.it.sitodruk.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.exceptions.ApplicationOptimisticLockException;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.exceptions.InsufficientStockException;
import pl.lodz.it.sitodruk.exceptions.UserNotFoundException;
import pl.lodz.it.sitodruk.service.OrderService;
import pl.lodz.it.sitodruk.service.ProductCategoryService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/orders")
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.NEVER, transactionManager = "mozTransactionManager")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private Properties exceptionProperties;

    @PostMapping("/placeOrder")
    @PermitAll
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        try {
            orderDTO.setIpAddress(request.getRemoteAddr());
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("order.placed");
        } catch (InsufficientStockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "insufficient.stock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        }        catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }



}
