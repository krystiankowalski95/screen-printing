<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('sumup')}}</h3>
    </header>
    <div v-if="this.$store.getters.shoppingListSize > 0">
      <b-container>
        <b-row>
          <b-col>{{ $t('productName') }}</b-col>
          <b-col>{{ $t('categoryName') }}</b-col>
          <b-col>{{ $t('numberOf') }}</b-col>
        </b-row>
      </b-container>
      <b-container
        class="bv-example-row"
        v-for="(product, index) in productList"
        :key="index"
      >
        <b-row style="padding: 5px">
          <b-col>{{ product.name }}</b-col>
          <b-col>{{ $t(product.categoryName) }}</b-col>
          <b-col>
            <number-input
              @change="calculatePrice()"
              size="small"
              name="quantity"
              v-model="product.quantity"
              disabled
            ></number-input
          ></b-col>
          <!--  <b-col v-if="isManagerInRole"><b-button pill variant="danger" @click="removeProduct(index)">{{ $t('removeButton') }}</b-button></b-col> -->
        </b-row>
      </b-container>
      <b-container>
        <b-row>
          <b-col style="font-weight: bold">{{ $t('totalcost') }}</b-col>
          <b-col
            ><money
              disabled
              v-model="totalcost"
              v-bind="money"
              v-validate="'required'"
              class="form-input"
              name="price"
          /></b-col>
        </b-row>
      </b-container>
      <br />
      <form name="form" @submit.prevent="placeOrder">
        <div v-if="!successful">
          <b-container>
            <b-row>
              <b-col
                ><label for="country">{{ $t('country') }}</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.country"
                    v-validate="'required|min:3|max:200'"
                    type="text"
                    class="form-control"
                    name="country"
                  />
                  <div
                    v-if="submitted && errors.has('country')"
                    class="alert-danger"
                  >
                    {{ errors.first('country') }}
                  </div>
                </div></b-col
              >
              <b-col
                ><label for="voivodeship">{{ $t('voivodeship') }}</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.voivodeship"
                    v-validate="'required|min:3|max:200'"
                    type="text"
                    class="form-control"
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
                    v-model="address.city"
                    v-validate="'required|min:3|max:200'"
                    type="text"
                    class="form-control"
                    name="city"
                  />
                  <div
                    v-if="submitted && errors.has('city')"
                    class="alert-danger"
                  >
                    {{ errors.first('city') }}
                  </div>
                </div></b-col
              >
              <b-col
                ><label for="postalCode">{{ $t('postalCode') }}</label></b-col
              >
              <b-col>
                <div class="form-group">
                   <the-mask name="postalCode" :mask="['##-###']" v-model="address.postalCode" v-validate="'required'" />
                  <div
                    v-if="submitted && errors.has('postalCode')"
                    class="alert-danger"
                  >
                    {{ errors.first('postalCode') }}
                  </div>
                </div></b-col
              >
            </b-row>
            <b-row>
               <b-col
                ><label for="street">{{ $t('street') }}</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.street"
                    v-validate="'min:3|max:200'"
                    type="text"
                    class="form-control"
                    name="street"
                  />
                  <div
                    v-if="submitted && errors.has('street')"
                    class="alert-danger"
                  >
                    {{ errors.first('street') }}
                  </div>
                </div></b-col
              >
              <b-col
                ><label for="streetNumber">{{
                  $t('streetNumber')
                }}</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.streetNumber"
                    v-validate="'required|min:1|max:200'"
                    type="text"
                    class="form-control"
                    name="streetNumber"
                  />
                  <div
                    v-if="submitted && errors.has('streetNumber')"
                    class="alert-danger"
                  >
                    {{ errors.first('streetNumber') }}
                  </div>
                </div></b-col
              >
            </b-row>
          </b-container>
          <br />
          <br />
          <b-container>
            <b-row>
              <b-col></b-col>
              <b-col></b-col>
              <b-col
                ><label for="blikCode">{{ $t('blikCode') }}</label></b-col
              >
              <b-col>
                <div class="form-group">
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
                <b-button pill variant="primary" @click="placeOrder()">{{
                  $t('placeOrder')
                }}</b-button></b-col
              >
            </b-row>
          </b-container>
        </div>
      </form>
    </div>
    <div v-if="message" class="alert" :class="successful ? 'alert-success' : 'alert-danger'">
        {{ message.message }}
      </div>
    <div v-if="this.$store.getters.shoppingListSize == 0">
      <h3 style="text-align: center">{{ $t('cartempty') }}</h3>
    </div>
  </div>
</template>

<script>
import { Money } from 'v-money';
import {TheMask} from 'vue-the-mask'
import Address from '../models/address';
import Order from '../models/order';
import OrderService from "../services/order.service";

export default {
  components: { Money, TheMask },
  name: 'SumUp',
  data() {
    return {
      submitted: false,
      successful: false,
      message: "",
      productList: this.$store.getters.shoppingList,
      totalcost: 0.0,
      order: new Order('','','','','',''),
      address: new Address('', '', '', '', '', ''),
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
    this.calculatePrice();
  },
  methods: {
    calculatePrice() {
      this.totalcost = 0;
      for (let i = 0; i < this.productList.length; i++) {
        let price =
          this.productList[i].price *
          this.productList[i].quantity;
        this.totalcost = this.totalcost + price;
      }
    },
    placeOrder() {
      this.order.products = this.productList;
      this.order.address = this.address;
      this.order.totalValue = this.totalcost;
      this.order.username = this.$store.state.auth.user.username;
      console.log(this.order);
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          this.$confirm(this.$t("areyousure"), this.$t("placingorder"), "info")
        .then(() => {
          OrderService.placeOrder(this.order).then(
            (data) => {
              this.responseList = data.data;
              this.successful = true;
            },
            (error) => {
              this.message = (error.response && error.response.data);
              this.successful = false;
            }
          );
        })
        }});
    },
  },
};
</script>
