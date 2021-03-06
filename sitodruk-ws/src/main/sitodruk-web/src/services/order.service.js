import axios from 'axios';
import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/app/orders';

class OrderService {
  getAllUserOrders(user) {
    return axios.post(API_URL + '/findUsersOrders',
    {
      username: user.username
    } , { headers: authHeader() });
  }

  getAllOrders() {
    return axios.get(API_URL + '/findAllOrders', { headers: authHeader() });
  }


  findOrderByPayUOrderId(payUOrderId) {
    return axios.post(API_URL + '/findByPayUOrderId',
    {
      payUOrderId: payUOrderId
    } , { headers: authHeader() });
  }

  completeOrder(order) {
    return axios.post(API_URL + '/complete',
    {
      payUOrderId: order.payUOrderId,
      products: order.products,
      totalValue: order.totalValue,
      username: order.username,
      addressDTO: order.addressDTO,
      orderStatus: order.orderStatus,
      dtoVersion: order.dtoVersion
    } , { headers: authHeader() });
  }

  cancelOrder(order) {
    return axios.post(API_URL + '/cancel',
    {
      payUOrderId: order.payUOrderId,
      products: order.products,
      totalValue: order.totalValue,
      username: order.username,
      addressDTO: order.addressDTO,
      orderStatus: order.orderStatus,
      dtoVersion: order.dtoVersion
    } , { headers: authHeader() });
  }

  cancelClientOrder(order,user) {
    return axios.post(API_URL + '/cancelClient',
    {
      payUOrderId: order.payUOrderId,
      products: order.products,
      totalValue: order.totalValue,
      username: user.username,
      addressDTO: order.addressDTO,
      orderStatus: order.orderStatus,
      dtoVersion: order.dtoVersion
    } , { headers: authHeader() });
  }
  
  repeatPayment(order) {
    return axios.post(API_URL + '/repeatPayment',
    {
      payUOrderId: order.payUOrderId,
      products: order.products,
      totalValue: order.totalValue,
      username: order.username,
      blikCode: order.blikCode,
      addressDTO: order.addressDTO,
      orderStatus: order.orderStatus,
      dtoVersion: order.dtoVersion
    } , { headers: authHeader() });
  }




  // getAllOrderStatuses() {
  //   return axios.get(API_URL + '/categories',{},
  //   // , { headers: authHeader() }
  //   );
  // }

  // editOrderStatus(product) {
  //   return axios.post(API_URL + '/edit' ,{
  //     name: product.name,
  //     categoryName: product.categoryName,
  //     price: product.price,
  //     active: true,
  //     quantity: product.quantity,
  //     dtoVersion: product.dtoVersion
  //   },
  //   // , { headers: authHeader() }
  //   );
  // }

  placeOrder(order) {
    return axios.post(API_URL + '/placeOrder', {
      payUOrderId: order.payUOrderId,
      products: order.products,
      totalValue: order.totalValue,
      username: order.username,
      blikCode: order.blikCode,
      addressDTO: order.address
    }
    , { headers: authHeader() }
    );
  }

}

export default new OrderService();
