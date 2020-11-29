import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    product: {},
    shoppingList: []
  },
  modules: {
    auth
  },
  getters: {
    shoppingListSize: state => {
      return state.shoppingList.length;
    }
  },
  mutations: {
    addProduct(state, product){
      let changed = false;
      console.log(product);
      for(let i = 0; i < state.shoppingList.length; i ++){
        if(state.shoppingList[i].product.name == product.name){
          state.shoppingList[i].product.quantity= Number(state.shoppingList[i].product.quantity) + Number(product.quantity);
          changed = true;
        }
      }
      if(changed == false){ 
        state.shoppingList.push({product: product});
      }
    }
  }
});
