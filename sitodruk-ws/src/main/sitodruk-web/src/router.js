import Vue from 'vue';
import Router from 'vue-router';
import Home from './views/Home.vue';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import ConfirmUser from './views/ConfirmUser.vue';
import ProductDetails from './views/ProductDetails.vue';
import AddProduct from './views/AddProduct.vue';
import ChangePassword from './views/ChangePassword.vue';
import ResetPassword from './views/ResetPassword.vue';
import ResetPasswordForm from './views/ResetPasswordForm.vue';
import EditProduct from './views/EditProduct.vue';
import Cart from './views/Cart.vue';
import SumUp from './views/SumUp.vue';
import EditUser from './views/EditUser.vue';
import UserOrders from './views/UserOrders.vue';
import OrderDetails from './views/OrderDetails.vue';
import ManagerOrderBoard from './views/ManagerOrderBoard.vue';
import UserList from './views/UserList.vue';
import UserDetails from './views/UserDetails.vue';
import UserEdit from './views/UserEdit.vue';
import CreateAccount from './views/CreateAccount.vue';
import ChangeUsersPassword from './views/ChangeUsersPassword.vue';

Vue.use(Router);

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/sumup',
      component: SumUp
    },
    {
      path: '/createUser',
      component: CreateAccount
    },
    {
      path: '/changeUsersPassword',
      component: ChangeUsersPassword
    },
    {
      path: "/userOrders",
      component: UserOrders
    },
    {
      path: '/changePassword',
      component: ChangePassword
    },
    {
      path: '/editUser',
      component: UserEdit
    },
    {
      path: '/userEdit',
      component: EditUser
    },
    {
      path: '/users',
      component: UserList
    },
    {
      path: '/userDetails',
      component: UserDetails
    },
    {
      path: '/home',
      component: Home
    },
    {
      path: '/setNewPassword',
      component: ResetPasswordForm
    },
    {
      path: '/resetPassword',
      component: ResetPassword
    },
    {
      path: '/orders',
      component: ManagerOrderBoard
    },
    {
      path: '/confirmAccount',
      component: ConfirmUser
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/addProduct',
      component: AddProduct
    },
    {
      path: '/profile',
      name: 'profile',
      // lazy-loaded
      component: () => import('./views/Profile.vue')
    },
    {
      path: '/products',
      name: 'products',
      component: () => import('./views/Products.vue')
    },
    {
      path: '/orderDetails',
      name: 'orderDetails',
      component: OrderDetails,
      props: true
    },
    {
      path: '/productDetails',
      name: 'productDetails',
      component: ProductDetails,
      props: true
    },
    {
      path: '/cart',
      name: 'cart',
      component: Cart,
    },
    {
      path: '/editProduct',
      name: 'editProduct',
      // lazy-loaded
      component: EditProduct,
      props: true
    },
  ]
});

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register', '/home','/products','/productDetails','/confirmAccount','/resetPassword','/setNewPassword'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user');

  // trying to access a restricted page + not logged in
  // redirect to login page
  if (authRequired && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});
