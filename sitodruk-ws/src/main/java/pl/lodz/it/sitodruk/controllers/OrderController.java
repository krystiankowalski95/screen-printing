package pl.lodz.it.sitodruk.controllers;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.service.OrderService;
import pl.lodz.it.sitodruk.service.ProductCategoryService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/orders")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER, transactionManager = "mozTransactionManager")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        try {
            orderDTO.setIpAddress(request.getRemoteAddr());
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("order.placed");
        } catch (PaymentException ex) {
            return ResponseEntity.ok("order.created");
        } catch (InsufficientStockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "insufficient.stock", ex);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_MANAGER')")
    public ResponseEntity<?> cancelOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.cancelOrder(orderDTO);
            return ResponseEntity.ok("order.cancelled");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "order.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<?> completeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.markOrderAsCompleted(orderDTO);
            return ResponseEntity.ok("order.completed");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "order.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/repeatPayment")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<?> repeatPayment(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        try {
            orderDTO.setIpAddress(request.getRemoteAddr());
            orderService.repeatPayment(orderDTO);
            return ResponseEntity.ok("order.completed");
        } catch (InvalidOrderStatusException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "invalid.order.status", ex);
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "optimistic.lock", ex);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "order.not.found", ex);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/findUsersOrders")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_MANAGER')")
    public ResponseEntity<List<OrderDTO>> getAllUsersOrders(@RequestBody OrderDTO orderDTO) {
        try {
            List<OrderDTO> orderDTOS = orderService.findUsersOrders(orderDTO.getUsername());
            return new ResponseEntity(orderDTOS, HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @PostMapping("/findByPayUOrderId")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_MANAGER')")
    public ResponseEntity<OrderDTO> findOrderByPayUOrderId(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO dto = orderService.findOrderByPayuOrderId(orderDTO);
            return new ResponseEntity(dto, HttpStatus.OK);
        }catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "order.not.found");
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }

    @GetMapping("/findAllOrders")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            return new ResponseEntity(orderService.findAllOrders(), HttpStatus.OK);
        }catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error");
        }
    }


}
