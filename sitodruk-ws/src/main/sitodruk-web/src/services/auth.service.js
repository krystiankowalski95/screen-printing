import axios from 'axios';

const API_URL = process.env.VUE_APP_BASE_API_URL + '/api/auth/'

class AuthService {
  login(user) {
    return axios
      .post(API_URL + 'signin', {
        username: user.username,
        password: user.password
      })
      .then(response => {
        if (response.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(response.data));
          if(response.data.roles.includes('ROLE_CLIENT')){
            localStorage.setItem('accessLevel', JSON.stringify('CLIENT'));
          }else if((response.data.roles.includes('ROLE_MANAGER') && (response.data.roles.includes('ROLE_ADMIN') && (!response.data.roles.includes('ROLE_CLIENT'))))){
            localStorage.setItem('accessLevel', JSON.stringify('MANAGER'));
          }else if((!response.data.roles.includes('ROLE_CLIENT') && (!response.data.roles.includes('ROLE_ADMIN') && (response.data.roles.includes('ROLE_MANAGER'))))){
            localStorage.setItem('accessLevel', JSON.stringify('MANAGER'));
          }else if((!response.data.roles.includes('ROLE_CLIENT') && (!response.data.roles.includes('ROLE_MANAGER') && (response.data.roles.includes('ROLE_ADMIN'))))){
            localStorage.setItem('accessLevel', JSON.stringify('ADMIN'));
          }else{
            localStorage.setItem('accessLevel', JSON.stringify(''));
          }
          
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('accessLevel');
    localStorage.removeItem('shoppingList');
  }

  register(user) {
    return axios.post(API_URL + 'signup', {
      username: user.username,
      email: user.email,
      password: user.password,
      firstname: user.firstname,
      lastname: user.lastname,
      phoneNumber: user.phoneNumber,
      confirmPassword: user.confirmPassword
    });
  }
}

export default new AuthService();
