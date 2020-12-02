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
      path: '/changePassword',
      component: ChangePassword
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
      // lazy-loaded
      component: () => import('./views/Products.vue')
    },
    {
      path: '/productDetails',
      name: 'productDetails',
      // lazy-loaded
      component: ProductDetails,
      props: true
    },
    {
      path: '/editProduct',
      name: 'editProduct',
      // lazy-loaded
      component: EditProduct,
      props: true
    },
    {
      path: '/admin',
      name: 'admin',
      // lazy-loaded
      component: () => import('./views/BoardAdmin.vue')
    },
    {
      path: '/mod',
      name: 'moderator',
      // lazy-loaded
      component: () => import('./views/BoardModerator.vue')
    },
    {
      path: '/user',
      name: 'user',
      // lazy-loaded
      component: () => import('./views/BoardUser.vue')
    }
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
