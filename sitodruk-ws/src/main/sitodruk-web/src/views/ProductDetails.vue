<template>
  <div class="container">
    <header class="jumbotron" style="height:100px">
      <h3>{{ $t('productDetails') }}</h3>
    </header>
      <b-container>
        <b-row >
          <b-col>{{ $t('productId') }}</b-col>
          <b-col>{{product.id}}</b-col>
        </b-row>
        <b-row>
             <b-col>{{ $t('productName') }}</b-col>
              <b-col>{{product.name}}</b-col>
        </b-row>
         <b-row>
            <b-col>{{ $t('categoryName') }}</b-col>
            <b-col>{{product.categoryName}}</b-col>
        </b-row>
         <b-row>
            <b-col>{{ $t('price') }}</b-col>
            <b-col>{{product.price}}</b-col>
        </b-row>
        <b-row>
            <b-button pill variant="primary" @click="edit()">{{ $t('edit') }}</b-button>
        </b-row>
      </b-container>
    </div>
</template>

<script>


export default {
  name: 'Product details',
  props: ['productName'],
  data() {
    return {
      product: this.$store.product
    };
  },
  mounted() {
  },
  methods: {
    edit() {
      this.$store.productName = this.product.name;
      this.$router.push({path: '/editProduct', params: {productName: this.product.productName}});
    },
  }
};
</script>
