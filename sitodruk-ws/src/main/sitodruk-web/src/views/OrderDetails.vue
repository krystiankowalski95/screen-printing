<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('orderDetails') }}</h3>
    </header>
    <form>
    <b-container>
      <b-row>
        <b-col>{{ $t('productName') }}</b-col>
        <b-col>{{ $t('categoryName') }}</b-col>
        <b-col>{{ $t('numberOf') }}</b-col>
      </b-row>
    </b-container>
    <b-container
      class="bv-example-row"
      v-for="(product, index) in order.products"
      :key="index"
    >
      <b-row style="padding: 5px">
        <b-col>{{ product.name }}</b-col>
        <b-col>{{ $t(product.categoryName) }}</b-col>
        <b-col>
          <number-input
            size="small"
            name="quantity"
            v-model="product.quantity"
            disabled
          ></number-input
        ></b-col>
      </b-row>
    </b-container>
    <b-container>
      <b-row>
        <b-col style="font-weight: bold">{{ $t('totalcost') }}</b-col>
        <b-col
          ><money
            disabled
            v-model="order.totalValue"
            v-bind="money"
            class="form-input"
            name="price"
        /></b-col>
      </b-row>
      <br/>
       <b-row>
        <b-col style="font-weight: bold">{{ $t('status') }}</b-col>
        <b-col
          >{{$t(order.orderStatus) }}</b-col>
      </b-row>
    </b-container>
    <br />
    <b-container>
      <b-row>
        <b-col
          ><label for="country">{{ $t('country') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <input
              v-model="order.addressDTO.country"
              type="text"
              disabled
              name="country"
            /></div
        ></b-col>
        <b-col
          ><label for="voivodeship">{{ $t('voivodeship') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <input
              v-model="order.addressDTO.voivodeship"
              disabled
              type="text"
              name="voivodeship"
            />
            <div
              v-if="submitted && errors.has('voivodeship')"
              class="alert-danger"
            >
              {{ errors.first('voivodeship') }}
            </div>
          </div></b-col
        >
      </b-row>
      <b-row>
        <b-col
          ><label for="city">{{ $t('city') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <input
              v-model="order.addressDTO.city"
              disabled
              type="text"
              name="city"
            /></div
        ></b-col>
        <b-col
          ><label for="postalCode">{{ $t('postalCode') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <the-mask
              name="postalCode"
              :mask="['##-###']"
              disabled
              v-model="order.addressDTO.postalCode"
            /></div
        ></b-col>
      </b-row>
      <b-row>
        <b-col
          ><label for="street">{{ $t('street') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <input
              v-model="order.addressDTO.street"
              type="text"
              disabled
              name="street"
            /></div
        ></b-col>
        <b-col
          ><label for="streetNumber">{{ $t('streetNumber') }}</label></b-col
        >
        <b-col>
          <div class="form-group">
            <input
              v-model="order.addressDTO.streetNumber"
              disabled
              type="text"
              name="streetNumber"
            /></div
        ></b-col>
      </b-row>
    </b-container>
    <br />
    <br />
    <b-container>
            <b-row>
              <b-col></b-col>
              <b-col></b-col>
              <b-col
                ><label v-if="order.orderStatus=='created'" for="blikCode">{{ $t('blikCode') }}</label></b-col
              >
              <b-col>
                <div v-if="order.orderStatus=='created'" class="form-group" >
                  <the-mask name="blikCode" :mask="['### ###']" v-model="order.blikCode" />
                  <div
                    v-if="submitted && errors.has('blikCode')"
                    class="alert-danger"
                  >
                    {{ errors.first('blikCode') }}
                  </div>
                </div></b-col
              >
            </b-row>
            <br/>
            <b-row>
              <b-col></b-col>
              <b-col></b-col>
              <b-col></b-col>
              <b-col>
                <b-button v-if="order.orderStatus=='created'" pill variant="primary" @click="repeatPayment()">{{
                  $t('repeatPayment')
                }}</b-button></b-col
              >
            </b-row>
    </b-container>
    </form>
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
import { Money } from 'v-money';
import { TheMask } from 'vue-the-mask';
import Address from '../models/address';
import Product from '../models/product';
import Order from '../models/order';
import OrderService from '../services/order.service';

export default {
  components: { Money, TheMask },
  name: 'OrderDetails',
  data() {
    return {
      submitted: false,
      successful: false,
      message: '',
      payUOrderId: this.$store.payUOrderId, 
      order: {},
      money: {
        decimal: '.',
        thousands: '',
        prefix: '',
        suffix: ' PLN',
        precision: 2,
        masked: false,
      },
    };
  },
  mounted() {
    if(this.payUOrderId == undefined){
      this.$router.push({
        path: "/userOrders",
      });
    }else{
    OrderService.findOrderByPayUOrderId(this.payUOrderId).then(
      (data) => {
        let orderDto = data.data;
        this.order = new Order(
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
              productDTO.quantity,
              productDTO.stock,
              productDTO.isActive
            )
          );
        });
        this.order.products = productList;
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
    }
 },
  methods: {
    repeatPayment(){
     this.order.username = this.$store.state.auth.user.username;
     this.$confirm(this.$t("areyousure"), this.$t("repeatpayment.msg"), "warning")
        .then(() => {
          OrderService.repeatPayment(this.order).then(
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
