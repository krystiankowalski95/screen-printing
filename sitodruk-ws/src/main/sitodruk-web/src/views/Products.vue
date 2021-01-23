<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t("productList") }}</h3>
      <div v-if="isManagerInRole" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/addProduct" class="nav-link"> <font-awesome-icon icon="plus-square" />{{ $t("addProduct") }} </router-link>
        </li>
      </div>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t("productName") }}</b-col>
        <b-col>{{ $t("categoryName") }}</b-col>
        <b-col>{{ $t("quantity") }}</b-col>
        <b-col>{{ $t("goToDetails") }}</b-col>
        <b-col v-if="isManagerInRole">{{ $t('deactivateSelectedProduct') }}</b-col>
        <b-col v-if="isManagerInRole">{{ $t('activateSelectedProduct') }}</b-col>
        <b-col v-if="isManagerInRole">{{ $t('editSelectedProduct') }}</b-col>
      </b-row>
    </b-container>
    <b-container class="bv-example-row" v-for="(product, index) in productList" :key="index">
      <b-row  v-if="isManagerInRole == true" style="padding: 5px">
        <b-col>{{ product.name }}</b-col>
        <b-col>{{ $t(product.categoryName) }}</b-col>
        <b-col>{{ product.stock }}</b-col>
        <b-col 
        ><b-button pill variant="primary" @click="getDetails(index)">{{ $t("details") }}</b-button></b-col
        >
         <b-col  v-if="(product.isActive == true)"><b-button pill variant="danger" @click="deactivate(index)">{{ $t('deactivate') }}</b-button></b-col>
         <b-col  v-if="(product.isActive == false)"><b-button pill disabled variant="danger">{{ $t('deactivate') }}</b-button></b-col>
         <b-col  v-if="(product.isActive == false)"><b-button pill variant="success" @click="activate(index)">{{ $t('activate') }}</b-button></b-col>
         <b-col  v-if="(product.isActive == true)"><b-button pill disabled variant="success">{{ $t('activate') }}</b-button></b-col>
          <b-col v-if="isManagerInRole"><b-button pill variant="secondary" @click="edit(index)">{{ $t('edit') }}</b-button></b-col>
      </b-row>
      <b-row  v-if="(isManagerInRole == false) && (product.isActive == true)" style="padding: 5px">
        <b-col>{{ product.name }}</b-col>
        <b-col>{{ $t(product.categoryName) }}</b-col>
        <b-col>{{ product.stock }}</b-col>
        <b-col 
        ><b-button pill variant="primary" @click="getDetails(index)">{{ $t("details") }}</b-button></b-col
        >
      </b-row>
    </b-container>
    <div
      v-if="message"
      class="alert"
      :class="successful ? 'alert-success' : 'alert-danger'"
    >
      {{ $t(message.message) }}
    </div>
  </div>
</template>

<script>
import ProductService from "../services/product.service";
import Product from "../models/product";

export default {
  name: "Products",
  data() {
    return {
      message: "",
      responseList: [],
      productList: [],
      successful: false
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdminInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_ADMIN");
      }

      return false;
    },
    isManagerInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_MANAGER");
      }

      return false;
    },
    isClientInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_CLIENT");
      }

      return false;
    },
  },
  mounted() {
    ProductService.getAllProducts().then(
      (data) => {
        this.responseList = data.data;
        this.responseList.map((productDTO) => {
          this.productList.push(new Product(productDTO.id, productDTO.name, productDTO.categoryName, productDTO.price, productDTO.dtoVersion,productDTO.quantity, productDTO.stock,productDTO.isActive));
        });
      },
      (error) => {
        this.message = (error.response && error.response.data);
        if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$router.push({
                  path: '/login',
                });
              }
      }
    );
  },
  methods: {
    edit(index) {
      this.$store.productName = this.productList[index].name
      this.$router.push({
        path: '/editProduct',
        params: { productName: this.productList[index].name },
      });
    },
    removeProduct(index) {
      this.$confirm(this.$t("areyousure"), this.$t("deletingmsg") + this.productList[index].name, "warning")
        .then(() => {
          ProductService.removeProduct(this.productList[index]).then(
            (data) => {
              this.responseList = data.data;
            
            },
            (error) => {
              this.message = (error.response && error.response.data);
              if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$router.push({
                  path: '/login',
                });
              }
            }
          );
        })
    },
    activate(index) {
          ProductService.activateProduct(this.productList[index]).then(
            (data) => {
              this.responseList = data.data;
               this.$router.go();
            },
            (error) => {
              this.message = (error.response && error.response.data);
              if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$router.push({
                  path: '/login',
                });
              }
            }
          );
    },

    deactivate(index) {
          ProductService.deactivateProduct(this.productList[index]).then(
            (data) => {
              this.responseList = data.data;
              this.$router.go();
            },
            (error) => {
              this.message = (error.response && error.response.data);
              if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$router.push({
                  path: '/login',
                });
              }
            }
          );
    },
    
    getDetails(index) {
      this.$store.product = this.productList[index];
      this.$router.push({
        path: "/productDetails",
        params: { productName: this.productList[index].name },
      });
    },
  },
};
</script>
