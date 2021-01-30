import Vue from 'vue';
import Vuex from 'vuex';

import { auth } from './auth.module';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    product: {},
    order: {},
    shoppingList: []
  },
  modules: {
    auth
  },
  getters: {
    shoppingListSize: state => {
      return state.shoppingList.length;
    },
    shoppingList: state => {
      return state.shoppingList;
    },
    order: state => {
      return state.order;
    },
  },
  mutations: {
    addProduct(state, productDTO){
      let changed = false;
      for(let i = 0; i < state.shoppingList.length; i ++){
        if(state.shoppingList[i].name == productDTO.name){
          state.shoppingList[i].quantity= Number(state.shoppingList[i].quantity) + Number(productDTO.quantity);
          changed = true;
        }
      }
      if(changed == false){ 
        state.shoppingList.push({
          id : productDTO.id,
          name : productDTO.name,
          price : productDTO.price,
          categoryName : productDTO.categoryName,
          dtoVersion : productDTO.dtoVersion,
          quantity :  productDTO.quantity,
          stock : productDTO.stock
        });
      }
    },
    removeProduct(state, index){
      state.shoppingList.splice(state.shoppingList[index],1);
    },
    clearShoppingList(state){
      state.shoppingList = [];
    }
  }
});
