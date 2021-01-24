import axios from 'axios';
import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/products';

class ProductService {
  getAllProducts() {
    return axios.get(API_URL + '/findAllActive');
  }

  getAllProductsManager() {
    return axios.get(API_URL + '/findAll'
      , { headers: authHeader() }
    );
  }

  getAllProductCategories() {
    return axios.get(API_URL + '/categories', {}
      , { headers: authHeader() }
    );
  }

  editProduct(product) {
    return axios.post(API_URL + '/edit', {
      name: product.name,
      categoryName: product.categoryName,
      price: product.price,
      quantity: product.quantity,
      stock: product.stock,
      dtoVersion: product.dtoVersion
    }, { headers: authHeader() }
    );
  }

  addProduct(product) {
    return axios.post(API_URL + '/addNew', {
      name: product.name,
      categoryName: product.categoryName,
      price: product.price,
      quantity: product.quantity,
      stock: product.stock,
    }, { headers: authHeader() }
    );
  }

  activateProduct(product) {
    return axios.post(API_URL + '/activateProduct', {
      name: product.name,
      categoryName: product.categoryName,
      dtoVersion: product.dtoVersion,
      isActive: product.isActive
    }
      , { headers: authHeader() }
    );
  }


  deactivateProduct(product) {
    return axios.post(API_URL + '/deactivateProduct', {
      name: product.name,
      categoryName: product.categoryName,
      dtoVersion: product.dtoVersion,
      isActive: product.isActive
    }
      , { headers: authHeader() }
    );
  }

  findProductByName(name) {
    return axios.get(API_URL + '/findByName/' + name
      , { headers: authHeader() }
    );
  }
}

export default new ProductService();
