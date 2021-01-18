import axios from 'axios';
import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/';

class UserService {
  confirmAccount(token) {
    return axios.post(API_URL + 'confirmAccount', {
      token: token,
    });
  }

  changePassword(user) {
    return axios.post(API_URL + 'changeOwnPassword', {
      username: user.username,
      password: user.password,
      confirmPassword: user.confirmPassword,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  editOwnAccount(user){
    return axios.post(API_URL + 'modifyOwnAccount', {
      username: user.username,
      firstname: user.firstname,
      lastname: user.lastname,
      phoneNumber: user.phoneNumber,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
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

  getUserProfile(user) {
    return axios.post(API_URL + 'findAccountByUsername', {
      username: user.username
    }, { headers: authHeader() }
    );
  }
}

export default new UserService();
