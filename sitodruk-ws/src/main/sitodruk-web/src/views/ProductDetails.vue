<template>
  <div class="container">
    <header class="jumbotron" style="height: 100px">
      <h3>{{ $t('productDetails') }}</h3>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t('productId') }}</b-col>
        <b-col>{{ product.id }}</b-col>
      </b-row>
      <b-row>
        <b-col>{{ $t('productName') }}</b-col>
        <b-col>{{ product.name }}</b-col>
      </b-row>
      <b-row>
        <b-col>{{ $t('categoryName') }}</b-col>
        <b-col>{{ $t(product.categoryName) }}</b-col>
      </b-row>
      <b-row>
        <b-col>{{ $t('price') }}</b-col>
        <b-col>
          <money v-model="product.price" v-bind="money" disabled name="price"
        /></b-col>
      </b-row>
      <b-row>
        </b-row
      >
      <b-row style="padding: 5px" />
      <b-row>
        <b-col v-if="isClientInRole"> 
          <b-button v-if="product.stock > 0" pill variant="primary" @click="addProductToCart()">
            <font-awesome-icon icon="cart-arrow-down" />{{
              $t('addProcutToCart')
            }}
          </b-button>
          <b-button v-if="product.stock == 0" disabled  pill variant="primary" @click="addProductToCart()">
            <font-awesome-icon icon="cart-arrow-down" />{{
              $t('product.unavailable')
            }}
          </b-button>
        </b-col>
        <b-col></b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { Money } from 'v-money';
import Product from '../models/product';

export default {
  components: { Money },
  name: 'ProductDetails',
  props: ['productName'],
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isManagerInRole() {
      if (this.currentUserAccessLevel == 'MANAGER') {
        return true;
      }
      return false;
    },
    isClientInRole() {
      if (this.currentUserAccessLevel == 'CLIENT') {
        return true;
      }
      return false;
    },
  },
  data() {
    return {
      selected: 1,
      quantityList: [],
      product: this.$store.product,
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
    for (let i = 0; i < 10; i++) {
      this.quantityList.push({ option: i + 1 });
    }
  },
  methods: {
    addProductToCart() {
      this.$store.commit(
        'addProduct',
        new Product(
          this.product.id,
          this.product.name,
          this.product.categoryName,
          this.product.price,
          this.product.dtoVersion,
          this.selected,
          this.product.stock,
          this.product.isActive
        )
      );
      this.$alert(this.$t('productadded'));
      this.$router.push('home');
    },
  },
};
</script>
