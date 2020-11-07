import axios from 'axios';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/products';

class ProductService {
  getAllProducts() {
    console.log(API_URL + '/findAll');
    return axios.get(API_URL + '/findAll');
  }
}

export default new ProductService();
