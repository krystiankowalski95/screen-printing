<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('cart') }}</h3>
      <!-- <div v-if="isManagerInRole" class="navbar-nav ml-auto"> -->
    </header>
    <div v-if="this.$store.getters.shoppingListSize > 0">
      <b-container>
        <b-row>
          <b-col>{{ $t('productId') }}</b-col>
          <b-col>{{ $t('productName') }}</b-col>
          <b-col>{{ $t('categoryName') }}</b-col>
          <b-col>{{ $t('numberOf') }}</b-col>
          <b-col></b-col>

          <!-- <b-col v-if="isManagerInRole">{{ $t('removeSelectedProduct') }}</b-col> -->
        </b-row>
      </b-container>
      <b-container
        class="bv-example-row"
        v-for="(product, index) in productList"
        :key="index"
      >
        <b-row style="padding: 5px">
          <b-col draggable="true">{{ index + 1 }}</b-col>
          <b-col>{{ product.product.name }}</b-col>
          <b-col>{{ product.product.categoryName }}</b-col>
          <b-col>{{ product.product.quantity }}</b-col>
          <!--  <b-col v-if="isManagerInRole"><b-button pill variant="danger" @click="removeProduct(index)">{{ $t('removeButton') }}</b-button></b-col> -->
          <b-col
            ><b-button pill variant="danger" @click="removeProduct(index)">{{
              $t('removeButton')
            }}</b-button></b-col
          >
        </b-row>
      </b-container>
      <b-container>
        <b-row>
          <b-col>{{ $t('totalcost') }}</b-col>
          <b-col>{{ this.totalcost }}</b-col>
        </b-row>
      </b-container>
    </div>
    <div v-if="this.$store.getters.shoppingListSize == 0">
      <h3 style="text-align: center">{{ $t('cartempty') }}</h3>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Products',
  data() {
    return {
      productList: this.$store.getters.shoppingList,
      totalcost: null,
    };
  },
  mounted() {
    this.calculatePrice();
  },
  methods: {
    removeProduct(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('deletingmsg') + this.productList[index].product.name,
        'warning'
      )
        .then(() => {
          this.$store.commit('removeProduct', index);
        })
        .catch(() => console.log());
    },
    calculatePrice() {
      for (let i = 0; i < this.$store.getters.shoppingListSize; i++) {
        let price =
          this.productList[i].product.price *
          this.productList[i].product.quantity;
        this.totalcost = this.totalcost + price;
      }
    },
  },
};
</script>
