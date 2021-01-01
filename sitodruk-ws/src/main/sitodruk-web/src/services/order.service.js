import axios from 'axios';
// import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/orders';

class OrderService {
  getAllOrders() {
    return axios.get(API_URL + '/findAll');
  }

  getAllOrderStatuses() {
    return axios.get(API_URL + '/categories',{},
    // , { headers: authHeader() }
    );
  }

  editOrderStatus(product) {
    return axios.post(API_URL + '/edit' ,{
      name: product.name,
      categoryName: product.categoryName,
      price: product.price,
      active: true,
      quantity: product.quantity,
      dtoVersion: product.dtoVersion
    },
    // , { headers: authHeader() }
    );
  }

  placeOrder(productList) {
    return axios.post(API_URL + '/addNew', {
      productList: productList,
    }
    // , { headers: authHeader() }
    );
  }

}

export default new OrderService();
