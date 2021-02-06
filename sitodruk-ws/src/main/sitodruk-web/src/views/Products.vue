<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('productList') }}</h3>
      <div v-if="isEmployeeInRole" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/addProduct" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t('addProduct') }}
          </router-link>
        </li>
      </div>
    </header>
    <b-container fluid>
      <b-row>
        <b-col lg="6" class="my-1">
          <b-form-group
            :label="$t('selectCategory')"
            label-for="sort-by-select"
            label-cols-sm="3"
            label-align-sm="right"
            label-size="sm"
            class="mb-0"
            v-slot="{ ariaDescribedby }"
          >
            <b-input-group size="sm">
              <b-form-select
                id="sort-by-select"
                v-model="filter"
                :options="sortOptions"
                :aria-describedby="ariaDescribedby"
                class="w-75"
              >
                <template #first>
                  <option value="">{{ $t('none') }}</option>
                </template>
              </b-form-select>
            </b-input-group>
          </b-form-group>
        </b-col>
        <b-col lg="6" class="my-1">
          <b-form-group
            :label="$t('filter')"
            label-for="filter-input"
            label-cols-sm="3"
            label-align-sm="right"
            label-size="sm"
            class="mb-0"
          >
            <b-input-group size="sm">
              <b-form-input
                id="filter-input"
                v-model="filter"
                type="search"
                :placeholder="$t('typeToSearch')"
              ></b-form-input>

              <b-input-group-append>
                <b-button :disabled="!filter" @click="filter = ''"
                  >{{ $t('clearFilter') }}
                </b-button>
              </b-input-group-append>
            </b-input-group>
          </b-form-group>
        </b-col>
      </b-row>
      <b-table
        :items="productList"
        :fields="computedFields"
        :current-page="currentPage"
        :per-page="perPage"
        :filter="filter"
        :filter-included-fields="filterOn"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
        :sort-direction="sortDirection"
        stacked="md"
        :empty-text="$t('listEmpty')"
        :empty-filtered-text="$t('noFilteredItemsFound')"
        show-empty
        striped
        @filtered="onFiltered"
      >
        <template #cell(name)="row">
          {{ row.value }}
        </template>

        <template #cell(categoryName)="row">
          {{ $t(row.value) }}
        </template>

        <template #cell(price)="row">
          {{
            new Intl.NumberFormat($i18n.locale, {
              style: 'currency',
              currency: 'PLN',
            }).format(row.value)
          }}
        </template>

        <template #cell(actions)="row">
          <b-button size="sm" variant="primary" @click="getDetails(row.index)"
            >{{ $t('details') }}
          </b-button>
        </template>

        <template #cell(managerActions)="row" v-if="isEmployeeInRole">
          <b-button-group v-if="row.item.isActive == true" size="sm">
            <b-button disabled variant="success">{{ $t('activate') }}</b-button>
            <b-button @click="deactivate(row.index)" variant="danger">
              {{ $t('deactivate') }}</b-button
            >
          </b-button-group>
          <b-button-group v-if="row.item.isActive == false" size="sm">
            <b-button @click="activate(row.index)" variant="success">{{
              $t('activate')
            }}</b-button>
            <b-button disabled variant="danger"
              >{{ $t('deactivate') }}
            </b-button>
          </b-button-group>
        </template>

        <template #cell(edit)="row" v-if="isEmployeeInRole">
          <b-button
            pill
            size="sm"
            variant="secondary"
            @click="edit(row.index)"
            >{{ $t('edit') }}</b-button
          >
        </template>
      </b-table>
      <b-row>
        <b-col sm="5" md="6" class="my-1">
          <b-form-group
            :label="$t('perPage')"
            label-for="per-page-select"
            label-cols-sm="6"
            label-cols-md="4"
            label-cols-lg="3"
            label-align-sm="right"
            label-size="sm"
            class="mb-0"
          >
            <b-form-select
              id="per-page-select"
              v-model="perPage"
              :options="pageOptions"
              size="sm"
            ></b-form-select>
          </b-form-group>
        </b-col>
        <b-col sm="7" md="6" class="my-1">
          <b-pagination
            v-model="currentPage"
            :total-rows="totalRows"
            :per-page="perPage"
            align="fill"
            size="sm"
            class="my-0"
          ></b-pagination>
        </b-col>
      </b-row>
    </b-container>
    <div
      v-if="message"
      class="alert"
      :class="successful ? 'alert-success' : 'alert-danger'"
    >
      {{ $t(message.message) }}
    </div>
  </div>
</template>

<script>
import ProductService from '../services/product.service';
import Product from '../models/product';
import Category from '../models/category';

export default {
  name: 'Products',
  data() {
    return {
      message: '',
      responseList: [],
      productList: [],
      productCategories: [],
      totalRows: 1,
      currentPage: 1,
      perPage: 3,
      pageOptions: [1, 3, 5],
      sortBy: '',
      sortDesc: false,
      sortDirection: 'asc',
      filter: null,
      filterOn: [],
      successful: false,
      fieldSet: [
        {
          key: 'name',
          label: `${this.$t('productName')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'categoryName',
          label: `${this.$t('categoryName')}`,
          sortable: true,
          class: 'text-center',
        },
        {
          key: 'stock',
          label: `${this.$t('quantity')}`,
          sortable: true,
          sortByFormatted: true,
          filterByFormatted: true,
        },
        {
          key: 'price',
          label: `${this.$t('price')}`,
          sortable: true,
          sortByFormatted: true,
          filterByFormatted: true,
        },
        { key: 'actions', label: `${this.$t('options')}` },
        {
          key: 'managerActions',
          label: `${this.$t('employeeOperations')}`,
          requiresEmployee: true,
        },
        {
          key: 'edit',
          label: '',
          requiresEmployee: true,
        },
      ],
    };
  },
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isEmployeeInRole() {
      if (this.currentUserAccessLevel == "EMPLOYEE") {
        return true;
      }
      return false;
    },
    isClientInRole() {
      if (this.currentUserAccessLevel == 'CLIENT') {
        return true;
      }
      return false;
    },
    computedFields() {
      if (!this.isEmployeeInRole)
        return this.fieldSet.filter((field) => !field.requiresEmployee);
      else return this.fieldSet;
    },
    sortOptions() {
      return this.productCategories.map((f) => {
        return { text: `${this.$t(f.category)}`, value: f.category };
      });
    },
  },
  mounted() {
    if (this.isEmployeeInRole) {
      ProductService.getAllProductsManager().then(
        (data) => {
          this.responseList = data.data;
          this.responseList.map((productDTO) => {
            this.productList.push(
              new Product(
                productDTO.id,
                productDTO.name,
                productDTO.categoryName,
                productDTO.price,
                productDTO.dtoVersion,
                productDTO.quantity,
                productDTO.stock,
                productDTO.isActive,
                productDTO.description
              )
            );
          });
          this.totalRows = this.productList.length;
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch('auth/logout');
            this.$router.push({
              path: '/login',
            });
          }
        }
      );
    } else {
      ProductService.getAllProducts().then(
        (data) => {
          this.responseList = data.data;
          this.responseList.map((productDTO) => {
            this.productList.push(
              new Product(
                productDTO.id,
                productDTO.name,
                productDTO.categoryName,
                productDTO.price,
                productDTO.dtoVersion,
                productDTO.quantity,
                productDTO.stock,
                productDTO.isActive,
                productDTO.description
              )
            );
          });
          this.totalRows = this.productList.length;
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch('auth/logout');
            this.$router.push({
              path: '/login',
            });
          }
        }
      );
    }

    ProductService.getAllProductCategories().then((data) => {
      let responseList = data.data;
      responseList.map((category) => {
        this.productCategories.push(new Category(category.categoryName));
      });
    });
  },
  methods: {
    edit(index) {
      this.$store.productName = this.productList[index].name;
      this.$router.push({
        path: '/editProduct',
        params: { productName: this.productList[index].name },
      });
    },
    removeProduct(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('deletingmsg') + this.productList[index].name,
        'warning'
      ).then(() => {
        ProductService.removeProduct(this.productList[index]).then(
          (data) => {
            this.responseList = data.data;
          },
          (error) => {
            this.message = error.response && error.response.data;
            if (this.message.status == 401) {
              this.$store.dispatch('auth/logout');
              this.$router.push({
                path: '/login',
              });
            }
          }
        );
      });
    },
    activate(index) {
      ProductService.activateProduct(this.productList[index]).then(
        (data) => {
          this.responseList = data.data;
          this.$router.go();
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch('auth/logout');
            this.$router.push({
              path: '/login',
            });
          }
        }
      );
    },

    deactivate(index) {
      ProductService.deactivateProduct(this.productList[index]).then(
        (data) => {
          this.responseList = data.data;
          this.$router.go();
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch('auth/logout');
            this.$router.push({
              path: '/login',
            });
          }
        }
      );
    },

    getDetails(index) {
      this.$store.product = this.productList[index];
      this.$router.push({
        path: '/productDetails',
        params: { productName: this.productList[index].name },
      });
    },
    onFiltered(filteredItems) {
      this.totalRows = filteredItems.length;
      this.currentPage = 1;
    },
  },
};
</script>
