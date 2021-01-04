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
    },
    shoppingList: state => {
      return state.shoppingList;
    }
  },
  mutations: {
    addProduct(state, productDTO){
      let changed = false;
      console.log(productDTO);
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
      console.log(state.shoppingList.length);
      state.shoppingList.splice(state.shoppingList[index],1);
      console.log(state.shoppingList.length);
    }
  }
});
