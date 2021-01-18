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
        <b-col>{{ $t("id") }}</b-col>
        <b-col>{{ $t("payUId") }}</b-col>
        <b-col>{{ $t("totalValue") }}</b-col>
        <!-- <b-col>{{ $t("Cancel") }}</b-col> -->
      </b-row>
    </b-container>
    <b-container class="bv-example-row" v-for="(order, index) in orderList" :key="index">
      <b-row style="padding: 5px">
        <b-col draggable="true">{{ index + 1 }}</b-col>
        <b-col>{{ order.payUOrderId }}</b-col>
        <b-col>{{ order.totalValue }}</b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import OrderService from "../services/order.service";
import Order from "../models/order";
import Product from "../models/product";
import Address from "../models/product";

export default {
  name: "UserOrders",
  data() {
    return {
      responseList: [],
      orderList: [],
      message: ""
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
    OrderService.getAllUserOrders(this.currentUser).then(
      (data) => {
        this.responseList = data.data;

        console.log(this.responseList);
        this.responseList.map((orderDto) => {
          let temp = new Order(orderDto.payUOrderId, null, orderDto.totalValue, new Address(orderDto.addressDTO.country, orderDto.addressDTO.voivodeship, orderDto.addressDTO.city, orderDto.addressDTO.postalCode, orderDto.addressDTO.street, orderDto.addressDTO.streetNumber), orderDto.dtoVersion);
          let productList = [];
          orderDto.products.map((productDTO) => {
          productList.push(new Product(productDTO.id, productDTO.name, productDTO.categoryName, productDTO.price, productDTO.dtoVersion,productDTO.quantity, productDTO.stock));
        });
          temp.products = productList;
          this.orderList.push(temp)

        });
      },
      (error) => {
        this.content = (error.response && error.response.data)
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
    // removeProduct(index) {
    //   this.$confirm(this.$t("areyousure"), this.$t("deletingmsg") + this.productList[index].name, "warning")
    //     .then(() => {
    //       ProductService.removeProduct(this.productList[index]).then(
    //         (data) => {
    //           this.responseList = data.data;
    //         },
    //         (error) => {
    //           this.content = (error.response && error.response.data);
    //         }
    //       );
    //     })
    // },
    
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
