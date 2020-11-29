<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t("cart") }}</h3>
      <!-- <div v-if="isManagerInRole" class="navbar-nav ml-auto"> -->
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t("productId") }}</b-col>
        <b-col>{{ $t("productName") }}</b-col>
        <b-col>{{ $t("categoryName") }}</b-col>
        <b-col>{{ $t("quantity") }}</b-col>
        <b-col>{{ $t("goToDetails") }}</b-col>
        <b-col>{{ $t("removeSelectedProduct") }}</b-col>

        <!-- <b-col v-if="isManagerInRole">{{ $t('removeSelectedProduct') }}</b-col> -->
      </b-row>
    </b-container>
    <b-container class="bv-example-row" v-for="(product, index) in productList" :key="index">
      <b-row style="padding: 5px">
        <b-col draggable="true">{{ index + 1 }}</b-col>
        <b-col>{{ product.name }}</b-col>
        <b-col>{{ product.categoryName }}</b-col>
        <b-col>{{ product.quantity }}</b-col>
        <b-col
          ><b-button pill variant="primary" @click="getDetails(index)">{{ $t("details") }}</b-button></b-col
        >
        <!--  <b-col v-if="isManagerInRole"><b-button pill variant="danger" @click="removeProduct(index)">{{ $t('removeButton') }}</b-button></b-col> -->
        <b-col
          ><b-button pill variant="danger" @click="removeProduct(index)">{{ $t("removeButton") }}</b-button></b-col
        >
      </b-row>
    </b-container>
  </div>
</template>

<script>
export default {
  name: "Products",
  data() {
    return {
      productList: this.$store.getters.shoppingList,
    };
  },
  mounted() {},
  methods: {
    removeProduct(index) {
      this.$confirm(this.$t("areyousure"), this.$t("deletingmsg") + this.productList[index].name, "warning")
        .then(() => {
          this.$store.commit("removeProduct", index);
        })
        .catch(() => console.log());
    },
  },
};
</script>
