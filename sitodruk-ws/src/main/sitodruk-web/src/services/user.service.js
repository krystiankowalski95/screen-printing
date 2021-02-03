import axios from 'axios';
import authHeader from './auth-header';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/app/users';

class UserService {
  confirmAccount(token) {
    return axios.post(API_URL + '/confirmAccount', {
      token: token,
    });
  }

  changePassword(user) {
    return axios.post(API_URL + '/changeOwnPassword', {
      username: user.username,
      password: user.password,
      confirmPassword: user.confirmPassword,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  changeOtherUserPassword(user) {
    return axios.post(API_URL + '/changePassword', {
      username: user.username,
      password: user.password,
      confirmPassword: user.confirmPassword,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }
  editOwnAccount(user){
    return axios.post(API_URL + '/modifyOwnAccount', {
      username: user.username,
      firstname: user.firstname,
      lastname: user.lastname,
      phoneNumber: user.phoneNumber,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  editSelectedAccount(user){
    return axios.post(API_URL + '/modifyAccount', {
      username: user.username,
      firstname: user.firstname,
      lastname: user.lastname,
      phoneNumber: user.phoneNumber,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  sendActivationLink(user){
    return axios.post(API_URL + '/resendActivationLink', {
      username: user.username,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  

  
  activateAccount(user){
    return axios.post(API_URL + '/activateAccount', {
      username: user.username,
      active: true,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  activateRole(user,role){
    return axios.post(API_URL + '/activateAccessLevel/' + role, {
      username: user.username,
      dtoVersion: user.dtoVersion,
      accessLevelDtoVersion: user.accessLevelDtoVersion
    }, { headers: authHeader() });
}

  deactivateRole(user,role){
      return axios.post(API_URL + '/deactivateAccessLevel/' + role, {
        username: user.username,
        dtoVersion: user.dtoVersion,
        accessLevelDtoVersion: user.accessLevelDtoVersion
      }, { headers: authHeader() });
  }

  deactivateAccount(user){
    return axios.post(API_URL + '/deactivateAccount', {
      username: user.username,
      active: false,
      dtoVersion: user.dtoVersion
    }, { headers: authHeader() });
  }

  resetPassword(user) {
    return axios.post(API_URL + '/resetPassword', {
      email: user.email,
    });
  }

  setNewPassword(user) {
    return axios.post(API_URL + '/setNewPassword', {
      token: user.token,
      password: user.password,
      confirmPassword: user.password
    });
  }

  getUserProfile(user) {
    return axios.post(API_URL + '/findAccountByUsername', {
      username: user.username
    }, { headers: authHeader() }
    );
  }

  findAccount(user) {
    return axios.post(API_URL + '/findAccount', {
      username: user.username
    }, { headers: authHeader() }
    );
  }
  createUser(user,selectedRoles) {
    return axios.post(API_URL + '/createUser', {
      username: user.username,
      email: user.email,
      password: user.password,
      firstname: user.firstname,
      lastname: user.lastname,
      phoneNumber: user.phoneNumber,
      confirmPassword: user.confirmPassword,
      roles: selectedRoles
    }, { headers: authHeader() });
  }

  getAllUsers() {
    return axios.get(API_URL + '/findAllAccounts', { headers: authHeader() }
    );
  }
}

export default new UserService();
