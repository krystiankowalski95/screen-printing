<template>
  <div class="col-md-12">
    <div class="card card-container">
      <img id="obrazek" src="//live.staticflickr.com/65535/33863541658_265433b18a.jpg" class="obrazek" />
      <form name="form" @submit.prevent="editProduct">
        <div v-if="!successful">
          <div class="form-group">
            <label for="name">{{ $t("productName") }}</label>
            <input disabled v-model="product.name" v-validate="'required|min:3|max:200'" type="text" class="form-control" name="name" />
            <div v-if="submitted && errors.has('name')" class="alert-danger">
              {{ errors.first("name") }}
            </div>
          </div>

          <div class="form-group">
            <label for="categoryName">{{ $t("categoryName") }}</label>
            <select name="categoryName" class="form-control" selected disabled>
              <option value="product.categoryName">
                {{ $t(product.categoryName) }}
              </option>
            </select>
            <div v-if="submitted && errors.has('categoryName')" class="alert-danger">
              {{ errors.first("categoryName") }}
            </div>
          </div>

            <div class="form-group">
            <label for="stock">{{ $t('quantity') }}</label>
             <number-input name="stock"  v-model="product.stock" :min="0" :max="99" inline controls></number-input>
            <div
              v-if="submitted && errors.has('stock')"
              class="alert-danger"
            >{{errors.first('stock')}}</div>
          </div>


          <div class="form-group">
            <label for="price">{{ $t("price") }}</label>
            <money v-model="product.price" v-bind="money" v-validate="'required'" class="form-control" name="price" />
            <div v-if="submitted && errors.has('price')" class="alert-danger">
              {{ errors.first("price") }}
            </div>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block swal2-confirm swal2-styled">
              {{ $t("edit") }}
            </button>
          </div>
        </div>
      </form>

      <div v-if="message" class="alert" :class="successful ? 'alert-success' : 'alert-danger'">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import ProductService from "../services/product.service";
import Product from "../models/product";
import { Money } from "v-money";

export default {
  components: { Money },
  name: "products",
  data() {
    return {
      productName: this.$store.productName,
      product: null,
      submitted: false,
      successful: false,
      message: "",
      money: {
        decimal: ".",
        thousands: "",
        prefix: "",
        suffix: " PLN",
        precision: 2,
        masked: false,
      },
    };
  },
  mounted() {
    ProductService.findProductByName(this.productName).then((data) => {
      let prod = data.data;
      this.product = new Product(prod.id, prod.name, prod.categoryName, prod.price,prod.dtoVersion ,prod.quantity, prod.stock);
      });
    (error) => {
      this.content = (error.response && error.response.data) || error.message || error.toString();
    };
  },
  methods: {
    editProduct() {
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          ProductService.editProduct(this.product).then(
            (data) => {
              this.message = data.message;
              this.successful = true;
            },
            (error) => {
              this.message = (error.response && error.response.data) || error.message || error.toString();
              this.successful = false;
            }
          );
        }
      });
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
