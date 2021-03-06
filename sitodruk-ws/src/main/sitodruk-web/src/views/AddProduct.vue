<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img
        id="obrazek"
        src="//live.staticflickr.com/65535/33863541658_265433b18a.jpg"
        class="obrazek"
      />
      <form name="form" @submit.prevent="addProduct">
        <div v-if="!successful">
          <div class="form-group">
            <label for="name">{{ $t('productName') }}*</label>
            <input
              v-model="product.name"
              v-validate="'required|min:3|max:200'"
              type="text"
              class="form-control"
              name="name"
            />
            <div v-if="submitted && errors.has('name')" class="alert-danger">
              {{ errors.first('name') }}
            </div>
          </div>

          <div class="form-group">
            <label for="categoryName">{{ $t('categoryName') }}*</label>
            <select
              v-validate="'required'"
              name="categoryName"
              class="form-control"
              @change="changeProductCategory($event)"
            >
              <option value="" selected disabled>{{ $t('choose') }}</option>
              <option
                v-for="(category, index) in productCategories"
                :value="category.category"
                :key="index"
              >
                {{ $t(category.category) }}
              </option>
            </select>
            <div
              v-if="submitted && errors.has('categoryName')"
              class="alert-danger"
            >
              {{ errors.first('categoryName') }}
            </div>
          </div>

          <div class="form-group">
            <label for="name">{{ $t('description') }}*</label>
            <input
              v-model="product.description"
              v-validate="'required|min:3|max:200'"
              type="text"
              class="form-control"
              name="description"
            />
            <div
              v-if="submitted && errors.has('description')"
              class="alert-danger"
            >
              {{ errors.first('description') }}
            </div>
          </div>

          <div class="form-group">
            <label for="stock">{{ $t('quantity') }}*</label>
            <number-input
              name="stock"
              v-model="product.stock"
              :min="0"
              :max="99"
              inline
              controls
            ></number-input>
            <div v-if="submitted && errors.has('stock')" class="alert-danger">
              {{ errors.first('quantity') }}
            </div>
          </div>

          <div class="form-group">
            <label for="price">{{ $t('price') }}*</label>
            <money
              v-model="product.price"
              v-bind="money"
              v-validate="'required'"
              class="form-control"
              name="price"
            />
            <div v-if="submitted && errors.has('price')" class="alert-danger">
              {{ errors.first('price') }}
            </div>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block">
              {{ $t('addProduct') }}
            </button>
          </div>
        </div>
      </form>
      <div class="form-group">
        <button class="btn btn-primary btn-block" @click="goBack()">
          {{ $t('goBack') }}
        </button>
      </div>

      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ $t(message.message) }}
      </div>
    </div>
  </div>
</template>

<script>
import Product from '../models/product';
import Category from '../models/category';
import ProductService from '../services/product.service';
import { Money } from 'v-money';

export default {
  components: { Money },
  name: 'AddProduct',
  data() {
    return {
      product: new Product('', '', '', '', '', '', '', '', ''),
      productCategories: [],
      selectedProductCategory: null,
      submitted: false,
      successful: false,
      message: '',
      money: {
        decimal: this.$i18n.locale == 'pl' ? ',' : '.',
        thousands: '',
        prefix: this.$i18n.locale == 'en' ? 'PLN ' : '',
        suffix: this.$i18n.locale == 'pl' ? 'zł' : '',
        precision: 2,
        masked: false,
      },
    };
  },
  mounted() {
    ProductService.getAllProductCategories().then((data) => {
      let responseList = data.data;
      responseList.map((category) => {
        this.productCategories.push(new Category(category.categoryName));
      });
    });
  },
  methods: {
    goBack() {
      this.$router.push('/products');
    },
    addProduct() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          this.$confirm(
            this.$t('areyousure'),
            this.$t('adding.product'),
            'info'
          ).then(() => {
            this.product.isActive = false;
            ProductService.addProduct(this.product).then(
              (data) => {
                this.message = data.message;
                this.successful = true;
                this.$alert(this.$t('product.has.been.added'));
                this.$router.push('/products');
              },
              (error) => {
                this.message = error.response && error.response.data;
                if (this.message.status == 401) {
                  this.$store.dispatch('auth/logout');
                  this.$alert(this.$t('session.timed.out'));
                  this.$router.push('/login');
                }
                this.successful = false;
              }
            );
          });
        }
      });
    },

    changeProductCategory(event) {
      this.product.categoryName = event.target.value;
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