<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('orders') }}</h3>
    </header>
    <b-container fluid>
      <b-row>
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
        :items="orderList"
        :fields="fieldSet"
        :current-page="currentPage"
        :per-page="perPage"
        :filter="filter"
        :filter-included-fields="filterOn"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
        :sort-direction="sortDirection"
        stacked="md"
        :empty-text="$t('orderListEmpty')"
        :empty-filtered-text="$t('noFilteredOrdersFound')"
        show-empty
        striped
        @filtered="onFiltered"
      >
        <template #cell(id)="row">
          {{ row.index + 1 }}
        </template>

        <template #cell(username)="row">
          {{ row.value }}
        </template>

        <template #cell(orderStatus)="row">
          {{ $t(row.value) }}
        </template>

        <template #cell(totalValue)="row">
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

        <template #cell(managerActions)="row">
          <b-button-group v-if="row.item.username != currentUser.username">
            <b-button size="sm"
              v-if="row.item.orderStatus == 'paid'"
              @click="completeOrder(row.index)"
              variant="success"
              >{{ $t('completeOrder') }}</b-button
            >
            <b-button size="sm"
              v-if="row.item.orderStatus != 'paid'"
              disabled
              variant="success"
              >{{ $t('completeOrder') }}</b-button
            >
            <b-button size="sm"
              disabled
              v-if="row.item.orderStatus != 'created'"
              variant="danger"
              >{{ $t('cancelOrder') }}
            </b-button>
            <b-button size="sm"
              v-if="row.item.orderStatus == 'created'"
              variant="danger"
              @click="cancelOrder(row.index)"
              >{{ $t('cancelOrder') }}
            </b-button>
          </b-button-group>
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
import OrderService from '../services/order.service';
import Order from '../models/order';
import Product from '../models/product';
import Address from '../models/address';

export default {
  name: 'UserOrders',
  data() {
    return {
      responseList: [],
      orderList: [],
      message: '',
      content: '',
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
          key: 'username',
          label: `${this.$t('username')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'orderStatus',
          label: `${this.$t('status')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'totalValue',
          label: `${this.$t('totalcost')}`,
          sortable: true,
          class: 'text-center',
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
    currentUser() {
      return this.$store.state.auth.user;
    },
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isEmployeeInRole() {
      if (this.currentUserAccessLevel == 'EMPLOYEE') {
        return true;
      }
      return false;
    },
  },
  mounted() {
    if (this.isEmployeeInRole) {
      OrderService.getAllOrders().then(
        (data) => {
          this.responseList = data.data;
          this.responseList.map((orderDto) => {
            let temp = new Order(
              orderDto.payUOrderId,
              null,
              orderDto.totalValue,
              orderDto.username,
              null,
              new Address(
                orderDto.addressDTO.country,
                orderDto.addressDTO.voivodeship,
                orderDto.addressDTO.city,
                orderDto.addressDTO.postalCode,
                orderDto.addressDTO.street,
                orderDto.addressDTO.streetNumber
              ),
              orderDto.orderStatus,
              orderDto.dtoVersion
            );
            let productList = [];
            orderDto.products.map((productDTO) => {
              productList.push(
                new Product(
                  productDTO.id,
                  productDTO.name,
                  productDTO.categoryName,
                  productDTO.price,
                  productDTO.dtoVersion,
                  productDTO.stock,
                  productDTO.quantity,
                  productDTO.isActive,
                  productDTO.description
                )
              );
            });
            temp.products = productList;
            this.orderList.push(temp);
          });
          this.totalRows = this.orderList.length;
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
      this.$router.push({
        path: '/home',
      });
    }
  },
  methods: {
    getDetails(index) {
      this.$store.payUOrderId = this.orderList[index].payUOrderId;
      this.$router.push({
        path: '/orderDetails',
        params: { payUOrderId: this.orderList[index].payUOrderId },
      });
    },

    onFiltered(filteredItems) {
      this.totalRows = filteredItems.length;
      this.currentPage = 1;
    },

    cancelOrder(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('cancelorder.msg'),
        'warning'
      ).then(() => {
        OrderService.cancelOrder(this.orderList[index]).then(
          (data) => {
            this.responseList = data.data;
            this.successful = true;
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
            this.successful = false;
          }
        );
      });
    },

    completeOrder(index) {
      this.$confirm(
        this.$t('areyousure'),
        this.$t('complete.msg'),
        'warning'
      ).then(() => {
        OrderService.completeOrder(this.orderList[index]).then(
          (data) => {
            this.responseList = data.data;
            this.successful = true;
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
            this.successful = false;
          }
        );
      });
    },
  },
};
</script>
