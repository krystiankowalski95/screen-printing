<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('cart') }}</h3>
    </header>
    <div v-if="this.$store.getters['cart/shoppingListSize'] > 0">
      <b-container>
        <b-row>
          <b-col>{{ $t('productName') }}</b-col>
          <b-col>{{ $t('categoryName') }}</b-col>
          <b-col>{{ $t('numberOf') }}</b-col>
          <b-col></b-col>
        </b-row>
      </b-container>
      <b-container
        class="bv-example-row"
        v-for="(product, index) in productList"
        :key="index"
      >
        <b-row style="padding: 5px">
          <b-col>{{ product.name }}</b-col>
          <b-col>{{ $t(product.categoryName) }}</b-col>
          <b-col>
            <number-input
              @click="setChanges()"
              size="small"
              name="quantity"
              v-model="product.quantity"
              :min="1"
              :max="product.stock"
              inline
              controls
            ></number-input
          ></b-col>
          <b-col
            ><b-button pill variant="danger" @click="removeProduct(index)">{{
              $t('removeButton')
            }}</b-button></b-col
          >
        </b-row>
      </b-container>
      <b-container>
        <b-row>
          <b-col style="font-weight: bold">{{ $t('totalcost') }}</b-col>
          <b-col
            > {{
              new Intl.NumberFormat($i18n.locale, {
                style: 'currency',
                currency: 'PLN',
              }).format(totalcost)
            }}</b-col>
          <b-col />
          <b-col />
          <b-col />
        </b-row>
      </b-container>
      <b-button pill variant="primary" @click="goToSumUp()">{{
        $t('sumup')
      }}</b-button>
    </div>
    <div v-if="this.$store.getters['cart/shoppingListSize'] == 0">
      <h3 style="text-align: center">{{ $t('cartempty') }}</h3>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Products',
  data() {
    return {
      productList: [],
      totalcost: 0.0,
    };
  },
  mounted(){
    this.productList = this.$store.getters['cart/shoppingList'];
    this.calculatePrice()
  },
  methods: {
    removeProduct(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('deletingmsg') + this.productList[index].name,
        'warning'
      )
        .then(() => {
          this.$store.dispatch('cart/removeProduct', index);
          this.$router.go();
        })
    },
    goToSumUp() {
      this.$router.push({
        path: '/sumup',
        params: { productList: this.productList },
      });
    },
    setChanges() {
      localStorage.setItem('shoppingList', JSON.stringify(this.productList));
      this.calculatePrice();
    },
    calculatePrice() {
      this.totalcost = 0.0;
      for (let i = 0; i < this.productList.length; i++) {
        let price =
          this.productList[i].price *  this.productList[i].quantity;
        this.totalcost = this.totalcost + price;
      }
    },
  },
};
</script>
