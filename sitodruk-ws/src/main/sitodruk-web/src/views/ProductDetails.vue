<template>
  <div class="container">
    <header class="jumbotron" style="height:100px">
      <h3>Product details</h3>
    </header>
      <b-container>
        <b-row >
          <b-col>Id</b-col>
          <!-- <b-col>{{product.id}}</b-col> -->
        </b-row>
        <b-row>
             <b-col>Product name</b-col>
              <b-col>{{this.product}}</b-col>
        </b-row>
        <!-- <b-row>
            <b-col>Product Category</b-col>
            <b-col>{{product.name}}</b-col>
        </b-row>
         <b-row>
            <b-col>Price</b-col>
            <b-col>{{product.price}}</b-col>
        </b-row> -->
      </b-container>
    </div>
</template>

<script>


export default {
  name: 'Product details',
  props: ['productName'],
  data() {
    console.log(this.$props.productName)
    return {
      product: this.$props.productName
    };
  },
  methods: {
    // getDetails(index) {
    //   (this.productList[index]);
    //   this.$router.push({path: '/productDetails', params: {product: this.productList[index]}});
    // }
  }
};
</script>
