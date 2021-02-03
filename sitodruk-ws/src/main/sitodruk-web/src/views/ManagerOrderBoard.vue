<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('orders') }}</h3>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t('id') }}</b-col>
        <b-col>{{ $t('username') }}</b-col>
        <b-col>{{ $t('totalcost') }}</b-col>
        <b-col>{{ $t('status') }}</b-col>
        <b-col></b-col>
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
        <b-col>{{ order.username }}</b-col>
        <b-col>{{ order.totalValue }}</b-col>
        <b-col>{{ $t(order.orderStatus) }}</b-col>
        <b-col
          ><b-button pill variant="primary" @click="getDetails(index)">{{
            $t('details')
          }}</b-button></b-col
        >
        <b-col>
          <b-button-group>
            <b-button v-if="order.orderStatus == 'paid'" @click="completeOrder(index)" variant="success">{{
              $t('completeOrder')
            }}</b-button>
             <b-button v-if="order.orderStatus != 'paid'" disabled variant="success">{{
              $t('completeOrder')
            }}</b-button>
            <b-button
              disabled
              v-if="order.orderStatus != 'created'"
              variant="danger"
              >{{ $t('cancelOrder') }}
            </b-button>
            <b-button v-if="order.orderStatus == 'created'" variant="danger"
              @click="cancelOrder(index)"
              >{{ $t('cancelOrder') }}
            </b-button>
          </b-button-group>
         </b-col>
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
import Address from '../models/address';

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
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isManagerInRole() {
    if (this.currentUserAccessLevel == 'MANAGER') {
        return true;
      }
      return false;
    },
  },
  mounted() {
    if (this.isManagerInRole) {
      OrderService.getAllOrders().then(
        (data) => {
          this.responseList = data.data;
          this.responseList.map((orderDto) => {
            let temp = new Order(
              orderDto.payUOrderId,
              null,
              orderDto.totalValue,
              orderDto.username,
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
                  productDTO.quantity,
                  productDTO.isActive
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
    } else {
      this.$router.push({
        path: '/home',
      });
    }
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
      this.$confirm(
        this.$t('areyousure'),
        this.$t('cancelorder.msg'),
        'warning'
      ).then(() => {
        OrderService.cancelOrder(this.orderList[index]).then(
          (data) => {
            this.responseList = data.data;
            this.successful = true;
            this.$router.go();
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
        );
      });
    },

    completeOrder(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('complete.msg'),
        'warning'
      ).then(() => {
        OrderService.completeOrder(this.orderList[index]).then(
          (data) => {
            this.responseList = data.data;
            this.successful = true;
            this.$router.go();
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
        );
      });
    },
  },
};
</script>
