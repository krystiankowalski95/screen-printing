<template>
  <div class="col-md-12">
    <b-card
      class="card card-container"
      :title="product.name"
      img-src="//live.staticflickr.com/65535/33863541658_265433b18a.jpg"
    >
      <b-card-text>
        {{ product.description }}
      </b-card-text>

      <b-container>
        <b-row>
          <b-col>{{ $t('categoryName') }}</b-col>
          <b-col>{{ $t(product.categoryName) }}</b-col>
        </b-row>
        <b-row>
          <b-col>{{ $t('price') }}</b-col>
          <b-col class="input">
            {{
              new Intl.NumberFormat($i18n.locale, {
                style: 'currency',
                currency: 'PLN',
              }).format(product.price)
            }}
          </b-col>
        </b-row>
        <b-row> </b-row>
        <b-row style="padding: 5px" />
        <b-row>
          <b-col v-if="isClientInRole">
            <b-button
              v-if="product.stock > 0"
              v-b-modal.modal-scoped
              @click="addProductToCart()"
              pill
              size="sm"
              variant="primary"
            >
              <font-awesome-icon icon="cart-arrow-down" />{{
                $t('addProcutToCart')
              }}
            </b-button>
            <b-button v-if="product.stock == 0" disabled pill variant="primary">
              <font-awesome-icon icon="cart-arrow-down" />{{
                $t('product.unavailable')
              }}
            </b-button>
          </b-col>
        </b-row>
        <b-row style="padding: 5px">
          <b-col>
            <b-button
              class="btn btn-primary pill"
              size="sm"
              @click="goBack()"
              >{{ $t('goBack') }}</b-button
            >
          </b-col>
        </b-row>
      </b-container>
    </b-card>

    <b-modal id="modal-scoped">
      <template #modal-header="{ close }">
        <h3>{{ $t('productadded') }}</h3>
      </template>

      <b-container fluid>
        <b-row class="mb-1 text-center">
          <b-col></b-col>
          <b-col
            ><b-iconstack
              font-scale="5"
              variant="success"
              style="width: 120px; height: 120px"
            >
              <b-icon stacked icon="square"></b-icon>
              <b-icon stacked icon="check"></b-icon> </b-iconstack
          ></b-col>
          <b-col></b-col>
        </b-row>
      </b-container>
      <template #modal-footer="{ ok }">
        <b-button size="sm" variant="success" @click="proceed()">
          {{ $t('ok') }}
        </b-button>
      </template>
    </b-modal>
  </div>
</template>
<script>
import Product from '../models/product';

export default {
  name: 'ProductDetails',
  props: ['productName'],
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isEmployeeInRole() {
      if (this.currentUserAccessLevel == 'EMPLOYEE') {
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
    if (this.product == undefined) {
      this.$router.push('/products');
    }
    for (let i = 0; i < 10; i++) {
      this.quantityList.push({ option: i + 1 });
    }
  },
  methods: {
    goBack() {
      this.$router.push('/products');
    },
    proceed() {
      this.$router.push('/home');
    },
    addProductToCart() {
      this.$store.dispatch(
        'cart/addProduct',
        new Product(
          this.product.id,
          this.product.name,
          this.product.categoryName,
          this.product.price,
          this.product.dtoVersion,
          this.selected,
          this.product.stock,
          this.product.isActive,
          this.product.description
        )
      );
    },
  },
};
</script>


<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 450px !important;
  padding: 5px 5px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 122px;
  height: 122px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}
</style>
