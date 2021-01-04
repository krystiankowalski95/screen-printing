<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t("productList") }}</h3>
      <!-- <div v-if="isManagerInRole" class="navbar-nav ml-auto"> -->
      <div class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/addProduct" class="nav-link"> <font-awesome-icon icon="plus-square" />{{ $t("addProduct") }} </router-link>
        </li>
      </div>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t("productId") }}</b-col>
        <b-col>{{ $t("productName") }}</b-col>
        <b-col>{{ $t("categoryName") }}</b-col>
        <b-col>{{ $t("quantity") }}</b-col>
        <b-col>{{ $t("goToDetails") }}</b-col>
        <b-col>{{ $t("removeSelectedProduct") }}</b-col>

        <!-- <b-col v-if="isManagerInRole">{{ $t('removeSelectedProduct') }}</b-col> -->
      </b-row>
    </b-container>
    <b-container class="bv-example-row" v-for="(product, index) in productList" :key="index">
      <b-row style="padding: 5px">
        <b-col draggable="true">{{ index + 1 }}</b-col>
        <b-col>{{ product.name }}</b-col>
        <b-col>{{ product.categoryName }}</b-col>
        <b-col>{{ product.stock }}</b-col>
        <b-col v-if="product.stock > 0"
          ><b-button pill variant="primary" @click="getDetails(index)">{{ $t("details") }}</b-button></b-col
        >
         <b-col v-if="product.stock === 0"
          ><b-button pill disabled variant="primary" @click="getDetails(index)">{{ $t("details") }}</b-button></b-col
        >
        <!--  <b-col v-if="isManagerInRole"><b-button pill variant="danger" @click="removeProduct(index)">{{ $t('removeButton') }}</b-button></b-col> -->
        <b-col
          ><b-button pill variant="danger" @click="removeProduct(index)">{{ $t("removeButton") }}</b-button></b-col
        >
      </b-row>
    </b-container>
  </div>
</template>

<script>
import ProductService from "../services/product.service";
import Product from "../models/product";

export default {
  name: "Products",
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

        this.responseList.map((productDTO) => {
          this.productList.push(new Product(productDTO.id, productDTO.name, productDTO.categoryName, productDTO.price, productDTO.dtoVersion,productDTO.quantity, productDTO.stock));
        });
        console.log(this.productList);
      },
      (error) => {
        this.content = (error.response && error.response.data) || error.message || error.toString();
      }
    );
  },
  methods: {
    removeProduct(index) {
      this.$confirm(this.$t("areyousure"), this.$t("deletingmsg") + this.productList[index].name, "warning")
        .then(() => {
          ProductService.removeProduct(this.productList[index]).then(
            (data) => {
              this.responseList = data.data;
            },
            (error) => {
              this.content = (error.response && error.response.data) || error.message || error.toString();
            }
          );
        })
        .catch(() => console.log());
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
