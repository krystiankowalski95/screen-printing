<template>
  <div class="container">
    <header class="jumbotron" style="height:100px">
      <h3>Product list</h3>
      <!-- <div v-if="showModeratorBoard" class="navbar-nav ml-auto"> -->
       <div class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/addProduct" class="nav-link">
            <font-awesome-icon icon="plus-square" />Sign Up
          </router-link>
        </li>
      </div>
    </header>
      <b-container>
        <b-row >
          <b-col >Id</b-col>
          <b-col>Product name</b-col>
          <b-col>Product Category</b-col>
          <b-col>Go to details page</b-col>
        </b-row>
      </b-container>
      <b-container class="bv-example-row" v-for="(product,index) in productList" :key="index">
        <b-row style="padding: 5px">
          <b-col draggable="true">{{product.id }}</b-col>
          <b-col>{{product.name }}</b-col>
          <b-col>{{product.categoryName }}</b-col>
          <b-col><b-button pill variant="primary" @click="getDetails(index)">Details</b-button></b-col>
        </b-row>
      </b-container>
    </div>
</template>

<script>
import ProductService from '../services/product.service';
import Product from '../models/product';


export default {
  name: 'Products',
  data() {
    return {
      responseList: [],
      productList: [],
    };
  },
  mounted() {
    ProductService.getAllProducts().then(
      (data) => {
        this.responseList = data.data;

        this.responseList.map((product,index) => {
          this.productList.push(
            new Product(
              product.id,
              product.name,
              product.categoryName,
              product.price,
              product.dtoVersion
            ),
          );
          console.log(this.productList[index]);
        });
      },
      (error) => {
        this.content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();
      }
    );
  },
  methods: {
    getDetails(index) {
      (this.productList[index]);
      this.$router.push({path: '/productDetails', params: {productName: this.productList[index].name}});
    }
  }
};
</script>
