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
              v-model="product.name"
              v-validate="'required|min:3|max:20'"
              type="text"
              class="form-control"
              name="name"
            />
            <div
              v-if="submitted && errors.has('name')"
              class="alert-danger"
            >{{errors.first('name')}}</div>
          </div>

          <div class="form-group">
            <label for="categoryName">{{ $t('categoryName') }}</label>
            <input
              v-model="product.categoryName"
              v-validate="'required|min:2|max:20'"
              type="text"
              class="form-control"
              name="categoryName"
            />
            <div
              v-if="submitted && errors.has('categoryName')"
              class="alert-danger"
            >{{errors.first('categoryName')}}</div>
          </div>

          <div class="form-group">
            <label for="price">{{ $t('price') }}</label>
            <input
              v-model="product.price"
              v-validate="'required|min:3|max:20'"
              type="text"
              class="form-control"
              name="price"
            />
            <div
              v-if="submitted && errors.has('price')"
              class="alert-danger"
            >{{errors.first('price')}}</div>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block">{{ $t('addProduct') }}</button>
          </div>
        </div>
      </form>

      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{message}}</div>
    </div>
  </div>
</template>

<script>
import Product from '../models/product';
import ProductService from '../services/product.service';

export default {
  name: 'AddProduct',
  data() {
    return {
      product: new Product('','', '', ''),
      submitted: false,
      successful: false,
      message: ''
    };
  },
  methods: {
    addProduct() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then(isValid => {
        if (isValid) {
          ProductService.addProduct(this.product).then(
            data => {
              this.message = data.message;
              this.successful = true;
            },
            error => {
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
              this.successful = false;
            }
          );
        }
      });
    }
  }
};
</script>