import AuthService from '../services/auth.service';

const user = JSON.parse(localStorage.getItem('user'));
const currentAccessLevel =  JSON.parse(localStorage.getItem('accessLevel'));
const initialState = user
  ? { status: { loggedIn: true }, user, currentAccessLevel }
  : { status: { loggedIn: false }, user: null, currentAccessLevel: null};

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }, user) {
      return AuthService.login(user).then(
        user => {
          commit('loginSuccess', user);
          return Promise.resolve(user);
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    changeAccessLevel({commit}, accessLevelName){
      commit('changeAccessLevel', accessLevelName);
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    register({ commit }, user) {
      return AuthService.register(user).then(
        response => {
          commit('registerSuccess');
          return Promise.resolve(response.data);
        },
        error => {
          commit('registerFailure');
          return Promise.reject(error);
        }
      );
    }
  },
  mutations: {
    loginSuccess(state, user) {
      state.status.loggedIn = true;
      state.user = user;
      if(user.roles.includes('ROLE_CLIENT')){
        state.currentAccessLevel = 'CLIENT';
        localStorage.setItem('accessLevel', JSON.stringify('CLIENT'));
      }else if((user.roles.includes('ROLE_MANAGER') && (user.roles.includes('ROLE_ADMIN') && (!user.roles.includes('ROLE_CLIENT'))))){
        state.currentAccessLevel = 'MANAGER';
        localStorage.setItem('accessLevel', JSON.stringify('MANAGER'));
      }else if((!user.roles.includes('ROLE_CLIENT') && (!user.roles.includes('ROLE_ADMIN') && (user.roles.includes('ROLE_MANAGER'))))){
        state.currentAccessLevel = 'MANAGER';
        localStorage.setItem('accessLevel', JSON.stringify('MANAGER'));
      }else if((!user.roles.includes('ROLE_CLIENT') && (!user.roles.includes('ROLE_MANAGER') && (user.roles.includes('ROLE_ADMIN'))))){
        state.currentAccessLevel = 'ADMIN';
        localStorage.setItem('accessLevel', JSON.stringify('ADMIN'));
      }else{
        state.currentAccessLevel = '';
        localStorage.setItem('accessLevel', JSON.stringify(''));
      }
    },
    changeAccessLevel(state,accessLevelName){
      if(accessLevelName == 'CLIENT'){
        state.currentAccessLevel = 'CLIENT';
        localStorage.setItem('accessLevel', JSON.stringify('CLIENT'));
      }else if(accessLevelName == 'MANAGER'){
        state.currentAccessLevel = 'MANAGER';
        localStorage.setItem('accessLevel', JSON.stringify('MANAGER'));
      }else if(accessLevelName == 'ADMIN'){
        state.currentAccessLevel == 'ADMIN';
        localStorage.setItem('accessLevel', JSON.stringify('ADMIN'));
      }
    },
    loginFailure(state) {
      state.status.loggedIn = false;
      state.user = null;
    
    },
    logout(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state) {
      state.status.loggedIn = false;
    },
    registerFailure(state) {
      state.status.loggedIn = false;
    }
  }
};
