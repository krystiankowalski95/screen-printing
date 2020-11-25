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
            <label for="name">{{ $t('productName') }}</label>
            <input
              disabled
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
            <label for="categoryName">{{ $t('categoryName') }}</label>
            <select
              name="categoryName"
              class="form-control"
              selected disabled>
              <option value="product.categoryName">
                {{ $t(product.categoryName) }}
              </option>
            </select>
            <div
              v-if="submitted && errors.has('categoryName')"
              class="alert-danger">
              {{ errors.first('categoryName') }}
            </div>
          </div>

          <div class="form-group">
            <label for="price">{{ $t('price') }}</label>
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
              {{ $t('edit') }}
            </button>
          </div>
        </div>
      </form>

      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import ProductService from '../services/product.service';
import Product from '../models/product';

export default {
  name: 'Products',
  data() {
    return {
      productName: this.$store.productName,
      product: null,
      submitted: false,
      successful: false,
      message: '',
      money: {
          decimal: '.',
          thousands: '',
          prefix: '',
          suffix: ' PLN',
          precision: 2,
          masked: false
        }
    };
  },
  mounted() {
    ProductService.findProductByName(this.productName).then(
      (data) => {
        console.log(data);
        let prod = data.data;
        this.product = 
            new Product(
              prod.id,
              prod.name,
              prod.categoryName,
              prod.price,
              prod.dtoVersion
            )
        });
      (error) => {
        this.content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();
      }
  },
  methods: {
    removeProduct(index){
    ProductService.removeProduct(this.productList[index]).then(
     (data) => {
        this.responseList = data.data;
        console.log(data.status);
      },
      (error) => {
        this.content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();
      }
    );
    }
  }
};
</script>
