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
        }

        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('user');
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

  confirmAccount(token) {
    return axios.get(API_URL + 'activateUser?'+ token, {
    });
  }
}

export default new AuthService();
