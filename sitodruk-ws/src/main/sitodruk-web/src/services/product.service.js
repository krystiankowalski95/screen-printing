import axios from 'axios';
// import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/products';

class ProductService {
  getAllProducts() {
    console.log(API_URL + '/findAll');
    return axios.get(API_URL + '/findAll');
  }

  addProduct(product) {
    return axios.post(API_URL + '/addNew', {
      name: product.name,
      categoryName: product.categoryName,
      price: product.price,
      active: true,
    }
    // , { headers: authHeader() }
    );
  }

  removeProduct(product) {
    return axios.post(API_URL + '/removeProduct', {
      name: product.name,
    }
    // , { headers: authHeader() }
    );
  }

  findProductByName(name) {
    return axios.post(API_URL + '/findByName/' + name, {}
    // , { headers: authHeader() }
    );
  }
}

export default new ProductService();
