<template>
  <div class="container">
    <header class="jumbotron">
      <h3>Product list</h3>
    </header>
    <div class="container">
        <ul id="product-list" >
        <li v-for="product in productList" :key="product.id" style="background-color: grey;">
          <div class="circle">
            {{product.id }} {{product.name }} {{product.categoryName }}
          </div>
          
        </li>
      </ul>
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
      responseList: [],
      productList: [],
    };
  },
  mounted() {
    ProductService.getAllProducts().then(
      (data) => {
        this.responseList = data.data;

        this.responseList.map((product,index) => {
          this.productList.push(
            new Product(
              product.id,
              product.name,
              product.categoryName,
              product.prime,
              product.active,
              product.dtoVersion
            ),
          );
          console.log(this.productList[index]);
        });
      },
      (error) => {
        this.content =
          (error.response && error.response.data) ||
          error.message ||
          error.toString();
      }
    );
  },
};
</script>
