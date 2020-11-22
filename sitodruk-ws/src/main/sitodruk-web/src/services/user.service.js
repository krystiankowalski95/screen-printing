import axios from 'axios';
import authHeader from './auth-header';

const test_api_url = process.env.VUE_APP_BASE_API_URL + '/api/test/';
const API_URL = process.env.VUE_APP_BASE_API_URL + '/';

class UserService {
  getPublicContent() {
    return axios.get(test_api_url + 'all');
  }

  getUserBoard() {
    return axios.get(test_api_url + 'user', { headers: authHeader() });
  }

  getModeratorBoard() {
    return axios.get(test_api_url + 'mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(test_api_url + 'admin', { headers: authHeader() });
  }

  confirmAccount(token) {
    return axios.post(API_URL + 'confirmAccount', {
      token: token,
    });
  }

  changePassword(user) {
    return axios.post(API_URL + 'changePassword', {
      username: user.username,
      password: user.password,
      confirmPassword: user.confirmPassword
    });
  }

  resetPassword(user) {
    return axios.post(API_URL + 'resetPassword', {
      email: user.email,
    });
  }

  setNewPassword(user) {
    return axios.post(API_URL + 'setNewPassword', {
      token: user.token,
      password: user.password,
      confirmPassword: user.password
    });
  }
}

export default new UserService();
