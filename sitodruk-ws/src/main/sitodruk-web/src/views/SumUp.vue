<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('sumup') }}</h3>
    </header>
    <div v-if="this.$store.getters['cart/shoppingListSize'] > 0">
      <b-container fluid>
        <b-table
          :items="productList"
          :fields="fieldSet"
          :sort-by.sync="sortBy"
          :sort-desc.sync="sortDesc"
          :sort-direction="sortDirection"
          stacked="md"
          :empty-text="$t('listEmpty')"
          show-empty
          striped
          @filtered="onFiltered"
        >
          <template #cell(name)="row">
            {{ row.value }}
          </template>

          <template #cell(categoryName)="row">
            {{ $t(row.value) }}
          </template>

          <template #cell(quantity)="row">
            {{ $t(row.value) }}
          </template>

          <template #cell(price)="row">
            {{
              new Intl.NumberFormat($i18n.locale, {
                style: 'currency',
                currency: 'PLN',
              }).format(row.value)
            }}
          </template>
        </b-table>
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
                ><label for="country">{{ $t('country') }}*</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <country-select
                    name="country"
                    countryName="true"
                    usei18n="true"
                    removePlaceholder="true"
                    v-model="address.country"
                    :country="address.country"
                    topCountry="PL"
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
                ><label for="voivodeship"
                  >{{ $t('voivodeship') }}*</label
                ></b-col
              >
              <b-col>
                <div class="form-group">
                  <region-select
                    name="voivodeship"
                    removePlaceholder="true"
                    regionName="true"
                    v-model="address.voivodeship"
                    :country="address.country"
                    defaultRegion="PL"
                    :region="region"
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
                ><label for="city">{{ $t('city') }}*</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.city"
                    v-validate="'required|min:3|max:100'"
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
                ><label for="postalCode">{{ $t('postalCode') }}*</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <the-mask
                    name="postalCode"
                    :mask="['##-###']"
                    v-model="address.postalCode"
                    v-validate="'required'"
                  />
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
                ><label for="street">{{ $t('street') }}*</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.street"
                    v-validate="'required|min:3|max:100'"
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
                ><label for="streetNumber"
                  >{{ $t('streetNumber') }}*</label
                ></b-col
              >
              <b-col>
                <div class="form-group">
                  <input
                    v-model="address.streetNumber"
                    v-validate="'required|min:1|max:11'"
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
                ><label for="blikCode">{{ $t('blikCode') }}*</label></b-col
              >
              <b-col>
                <div class="form-group">
                  <the-mask
                    name="blikCode"
                    :mask="['### ###']"
                    v-validate="'required'"
                    v-model="order.blikCode"
                  />
                  <div
                    v-if="submitted && errors.has('blikCode')"
                    class="alert-danger"
                  >
                    {{ errors.first('blikCode') }}
                  </div>
                </div></b-col
              >
            </b-row>
            <br />
            <b-row>
              <b-col></b-col>
              <b-col></b-col>
              <b-col></b-col>
              <b-col>
                <b-button pill variant="primary" @click="placeOrder()"
                  >{{ $t('placeOrder') }}
                  <b-icon
                    icon="credit-card"
                    aria-hidden="true"
                  ></b-icon> </b-button
              ></b-col>
            </b-row>
            <b-row>
              <b-col
                ><b-button pill variant="primary" @click="goBack()">{{
                  $t('goBack')
                }}</b-button></b-col
              ><b-col></b-col>
              <b-col></b-col>
              <b-col></b-col>
              <b-col></b-col>
              <b-col></b-col>
            </b-row>
          </b-container>
        </div>
      </form>
    </div>
    <div
      v-if="message"
      class="alert"
      :class="successful ? 'alert-success' : 'alert-danger'"
    >
      {{ $t(message.message) }}
    </div>
    <div
      v-if="
        this.$store.getters['cart/shoppingListSize'] == 0 &&
        this.successful == false
      "
    >
      <h3 style="text-align: center">{{ $t('cartempty') }}</h3>
    </div>
  </div>
</template>

<script>
import { Money } from 'v-money';
import { TheMask } from 'vue-the-mask';
import Address from '../models/address';
import Order from '../models/order';
import OrderService from '../services/order.service';
import Vue from 'vue';
import vueCountryRegionSelect from 'vue-country-region-select';
Vue.use(vueCountryRegionSelect);

export default {
  components: { Money, TheMask },
  name: 'SumUp',
  data() {
    return {
      submitted: false,
      successful: false,
      message: '',
      productList: [],
      totalcost: 0.0,
      order: new Order('', '', '', '', '', ''),
      address: new Address('', '', '', '', '', ''),
      money: {
        decimal: '.',
        thousands: '',
        prefix: '',
        suffix: ' PLN',
        precision: 2,
        masked: false,
      },
      sortDesc: false,
      sortDirection: 'asc',
      fieldSet: [
        {
          key: 'name',
          label: `${this.$t('productName')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'categoryName',
          label: `${this.$t('categoryName')}`,
          sortable: true,
          class: 'text-center',
        },
        {
          key: 'quantity',
          label: `${this.$t('quantityInOrder')}`,
          sortable: true,
          sortByFormatted: true,
          filterByFormatted: true,
        },
        {
          key: 'price',
          label: `${this.$t('price')}`,
          sortable: true,
          sortByFormatted: true,
          filterByFormatted: true,
        },
      ],
    };
  },
  mounted() {
    if (this.$store.getters['cart/shoppingListSize'] == 0) {
      this.$router.push('/cart');
    }
    this.productList = this.$store.getters['cart/shoppingList'];
    this.calculatePrice();
  },
  methods: {
    goBack() {
      this.$router.push('/cart');
    },
    goToHomePage() {
      this.$store.commit('clearShoppingList');
      this.$router.push({
        path: '/home',
      });
    },
    calculatePrice() {
      this.totalcost = 0;
      for (let i = 0; i < this.productList.length; i++) {
        let price = this.productList[i].price * this.productList[i].quantity;
        this.totalcost = this.totalcost + price;
      }
    },
    placeOrder() {
      this.submitted = true;
      this.order.products = this.productList;
      this.order.address = this.address;
      this.order.totalValue = this.totalcost;
      this.order.username = this.$store.state.auth.user.username;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          this.$bvModal
            .msgBoxConfirm(this.$t('repeatpayment.msg'), {
              title: this.$t('areyousure'),
              size: 'sm',
              buttonSize: 'sm',
              okVariant: 'danger',
              okTitle: this.$t('yes'),
              cancelTitle: this.$t('no'),
              footerClass: 'p-2',
              hideHeaderClose: false,
              centered: true,
            })
            .then((value) => {
              if (value == true) {
                OrderService.placeOrder(this.order).then(
                  (data) => {
                    this.responseList = data;
                    this.message = this.responseList.data;
                    this.successful = true;
                    this.$store.dispatch('cart/clearShoppingList');
                    this.$alert(this.$t('order.placed'));
                    this.$router.push('/home');
                  },
                  (error) => {
                    this.message = error.response && error.response.data;
                    if (this.message.message == 'product.not.available') {
                      this.$store.dispatch('cart/clearShoppingList');
                      this.$alert(this.$t('product.not.available'));
                      this.$router.push('/home');
                    }
                    if (this.message.message == 'insufficient.stock') {
                      this.$store.dispatch('cart/clearShoppingList');
                      this.$alert(this.$t('insufficient.stock'));
                      this.$router.push('/home');
                    }
                    if (this.message.status == 401) {
                      this.$store.dispatch('auth/logout');
                      this.$router.push({
                        path: '/login',
                      });
                    }
                    this.successful = false;
                  }
                );
              }
            })
            .catch((err) => {
              this.message = err.response && err.response.data;
              this.$bvModal.msgBoxConfirm(this.$t(this.message), {
                title: '',
                size: 'sm',
                buttonSize: 'sm',
                okVariant: 'error',
                headerClass: 'p-2 border-bottom-0',
                footerClass: 'p-2 border-top-0',
                centered: true,
              });
            });
        }
      });
    },
  },
};
</script>
