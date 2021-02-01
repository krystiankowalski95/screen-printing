const shoppingList = JSON.parse(localStorage.getItem('shoppingList'));
const initialShoppingListState = shoppingList
    ? { shoppingList }
    : { shoppingList: null };

export const cart = {
    namespaced: true,
    state: initialShoppingListState,
    actions: {
        addProduct({ commit }, product) {
            commit('addProduct', product);
            return Promise.resolve(product);
        },
        removeProduct({ commit }, index) {
            commit('removeProduct', index);
            return Promise.resolve(index);
        },
        clearShoppingList({ commit }) {
            commit('clearShoppingList');
            return Promise.resolve();
        },
        changeQuantity({ commit }, index, product) {
            commit('clearShoppingList', index, product);
            return Promise.resolve();
        },
    },
    getters: {
        shoppingListSize: state => {
            if (state.shoppingList != null) {
                return state.shoppingList.length;
            } else {
                return 0;
            }
        },
        shoppingList: state => {
            return state.shoppingList;
        },
    },
    mutations: {
        addProduct(state, productDTO) {
            let changed = false;
            let productList = state.shoppingList;
            console.log(shoppingList);
            if (productList != null) {
                for (let i = 0; i < productList.length; i++) {
                    if (productList[i].name == productDTO.name) {
                        let quant = Number(productList[i].quantity) + Number(productDTO.quantity);
                        if (quant <= productList[i].stock) {
                            productList[i].quantity = quant;
                        }
                        changed = true;
                    }
                }
            }
            if (productList == null) {
                productList = [];
            }
            if (changed == false) {
                productList.push({
                    id: productDTO.id,
                    name: productDTO.name,
                    price: productDTO.price,
                    categoryName: productDTO.categoryName,
                    dtoVersion: productDTO.dtoVersion,
                    quantity: productDTO.quantity,
                    stock: productDTO.stock
                });
            }
            localStorage.setItem('shoppingList', JSON.stringify(productList));
        },
        changeQuantity(state,index,product) {
            let products = [];
            if(state.shoppingList.length > 0){
                for (let i = 0; i < state.shoppingList.length; i++) {
                    if(i == index){
                        product.quantity = product;
                    }
                    products.push(state.shoppingList[i]);
                }
            }
            localStorage.removeItem('shoppingList');
            localStorage.setItem('shoppingList', JSON.stringify(products));
        },
        removeProduct(state, index) {
            state.shoppingList.splice(index, 1)
            let products = [];
            if(state.shoppingList.length > 0){
                for (let i = 0; i < state.shoppingList.length; i++) {
                    products.push(state.shoppingList[i]);
                }
            }
            localStorage.removeItem('shoppingList');
            localStorage.setItem('shoppingList', JSON.stringify(products));
        },
        clearShoppingList() {
            localStorage.removeItem('shoppingList');
        }
    }
};
