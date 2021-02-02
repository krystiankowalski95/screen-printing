package pl.lodz.it.sitodruk.controllers;


import lombok.extern.slf4j.Slf4j;
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
import pl.lodz.it.sitodruk.config.annotations.ClientAuthenticatedAndUsernameFromOrderDto;
import pl.lodz.it.sitodruk.config.annotations.ClientAuthenticatedAndUsernameFromUserDto;
import pl.lodz.it.sitodruk.config.annotations.UserOrManagerAuthenticated;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.exceptions.*;
import pl.lodz.it.sitodruk.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/app/orders")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.NEVER, transactionManager = "mozTransactionManager")
public class OrderController {


    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.CLIENT + "')")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        try {
            orderDTO.setIpAddress(request.getRemoteAddr());
            orderService.createOrder(orderDTO);
            return ResponseEntity.ok("order.placed");
        }  catch (InsufficientStockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/cancelClient")
    @ClientAuthenticatedAndUsernameFromOrderDto
    public ResponseEntity<?> cancelOrderByUser(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.cancelOrder(orderDTO);
            return ResponseEntity.ok("order.cancelled");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.MANAGER + "')")
    public ResponseEntity<?> cancelOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.cancelOrder(orderDTO);
            return ResponseEntity.ok("order.cancelled");
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/complete")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.MANAGER + "')")
    public ResponseEntity<?> completeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.markOrderAsCompleted(orderDTO);
            return ResponseEntity.ok("order.completed");
        }catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/repeatPayment")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.CLIENT + "')")
    public ResponseEntity<?> repeatPayment(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        try {
            orderDTO.setIpAddress(request.getRemoteAddr());
            orderService.repeatPayment(orderDTO);
            return ResponseEntity.ok("order.completed");
        } catch (InvalidOrderStatusException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (ApplicationOptimisticLockException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }  catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/findUsersOrders")
    @ClientAuthenticatedAndUsernameFromUserDto
    public ResponseEntity<List<OrderDTO>> getAllUsersOrders(@RequestBody UserDTO userDTO) {
        try {
            List<OrderDTO> orderDTOS = orderService.findUsersOrders(userDTO.getUsername());
            return new ResponseEntity(orderDTOS, HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @PostMapping("/findByPayUOrderId")
    @UserOrManagerAuthenticated
    public ResponseEntity<OrderDTO> findOrderByPayUOrderId(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO dto = orderService.findOrderByPayuOrderId(orderDTO);
            return new ResponseEntity(dto, HttpStatus.OK);
        }catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }

    @GetMapping("/findAllOrders")
    @PreAuthorize("hasAnyRole('" + SecurityConsts.MANAGER + "')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            return new ResponseEntity(orderService.findAllOrders(), HttpStatus.OK);
        } catch (BaseException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "database.error");
        }
    }


}
