<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('userOrders') }}</h3>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t('id') }}</b-col>
        <b-col>{{ $t('totalcost') }}</b-col>
        <b-col>{{ $t('status') }}</b-col>
        <b-col></b-col>
        <b-col></b-col>
      </b-row>
    </b-container>
    <b-container
      class="bv-example-row"
      v-for="(order, index) in orderList"
      :key="index"
    >
      <b-row style="padding: 5px">
        <b-col draggable="true">{{ index + 1 }}</b-col>
        <b-col>{{ order.totalValue }}</b-col>
        <b-col>{{ $t(order.orderStatus) }}</b-col>
        <b-col
          ><b-button pill variant="primary" @click="getDetails(index)">{{
            $t('details')
          }}</b-button></b-col
        >
        <b-col
          ><b-button
            v-if="order.orderStatus == 'created'"
            pill
            variant="danger"
            @click="cancelOrder(index)"
            >{{ $t('cancelOrder') }}</b-button
          ></b-col
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
import OrderService from '../services/order.service';
import Order from '../models/order';
import Product from '../models/product';
import Address from '../models/product';

export default {
  name: 'UserOrders',
  data() {
    return {
      responseList: [],
      orderList: [],
      message: '',
      content: '',
      successful: false,
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isManagerInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_MANAGER');
      }

      return false;
    },
    isClientInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_CLIENT');
      }

      return false;
    },
  },
  mounted() {
    OrderService.getAllUserOrders(this.currentUser).then(
      (data) => {
        this.responseList = data.data;
        this.responseList.map((orderDto) => {
          let temp = new Order(
            orderDto.payUOrderId,
            null,
            orderDto.totalValue,
            null,
            null,
            new Address(
              orderDto.addressDTO.country,
              orderDto.addressDTO.voivodeship,
              orderDto.addressDTO.city,
              orderDto.addressDTO.postalCode,
              orderDto.addressDTO.street,
              orderDto.addressDTO.streetNumber
            ),
            orderDto.orderStatus,
            orderDto.dtoVersion
          );
          let productList = [];
          orderDto.products.map((productDTO) => {
            productList.push(
              new Product(
                productDTO.id,
                productDTO.name,
                productDTO.categoryName,
                productDTO.price,
                productDTO.dtoVersion,
                productDTO.stock,
                productDTO.quantity
              )
            );
          });
          temp.products = productList;
          this.orderList.push(temp);
        });
      },
      (error) => {
        this.message = error.response && error.response.data;
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
    getDetails(index) {
      this.$store.payUOrderId = this.orderList[index].payUOrderId;
      this.$router.push({
        path: '/orderDetails',
        params: { payUOrderId: this.orderList[index].payUOrderId },
      });
    },

    cancelOrder(index) {
      this.$confirm(this.$t("areyousure"), this.$t("cancelorder.msg"), "warning")
        .then(() => {
          OrderService.cancelOrder(this.orderList[index]).then(
        (data) => {
          this.responseList = data.data;
          this.successful = true;
          this.$router.push({
          path: '/userOrders',
        });
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
                this.$store.dispatch('auth/logout');
                this.$router.push({
                  path: '/login',
                });
              }
          this.successful = false;
        }
      )
        })
    },
  },
};
</script>
