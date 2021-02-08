<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('orderDetails') }}: {{ order.payUOrderId }}</h3>
    </header>
    <form @submit.prevent="repeatPayment">
      <b-container fluid>
        <b-table
          :items="order.products"
          :fields="fieldSet"
          :sort-by.sync="sortBy"
          :sort-desc.sync="sortDesc"
          :sort-direction="sortDirection"
          stacked="md"
          :empty-text="$t('listEmpty')"
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
        </b-table>
      </b-container>
      <b-container>
        <b-row>
          <b-col style="font-weight: bold">{{ $t('totalcost') }}:</b-col>
          <b-col>{{
            new Intl.NumberFormat($i18n.locale, {
              style: 'currency',
              currency: 'PLN',
            }).format(order.totalValue)
          }}</b-col>
          <b-col></b-col>
          <b-col></b-col>
          <b-col></b-col>
        </b-row>
        <br />
        <b-row>
          <b-col style="font-weight: bold">{{ $t('status') }}:</b-col>
          <b-col>{{ $t(order.orderStatus) }}</b-col>
          <b-col></b-col>
          <b-col></b-col>
          <b-col></b-col>
        </b-row>
      </b-container>
      <br />
      <b-container>
        <b-card no-body class="overflow-hidden" style="max-width: 750px">
          <b-row no-gutters>
            <b-col md="6">
              <b-card-img
                src="https://images.pexels.com/photos/1078850/pexels-photo-1078850.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
                alt="Image"
                class="rounded-0"
              ></b-card-img>
            </b-col>
            <b-col md="6">
              <b-card-body :title="$t('adresDostawy')">
                <b-card-text>
                 {{$t(order.addressDTO.country)}}, {{order.addressDTO.voivodeship}}
                </b-card-text>
                <b-card-text>
                  <the-mask
                    name="postalCode"
                    :mask="['##-###']"
                    disabled
                    v-model="order.addressDTO.postalCode"
                  />, {{ order.addressDTO.city }}
                </b-card-text>
                <b-card-text>
                  {{ $t('street') }}: {{ order.addressDTO.street }}
                  {{ order.addressDTO.streetNumber }}
                </b-card-text>
              </b-card-body>
            </b-col>
          </b-row>
        </b-card>
      </b-container>
      <br />
      <b-container>
        <b-row>
          <b-col></b-col>
          <b-col></b-col>
          <b-col  v-if="order.orderStatus == 'created' && isClientInRole"
            ><label for="blikCode"
              >{{ $t('blikCode') }}*
            </label></b-col
          >
          <b-col>
            <div
              v-if="order.orderStatus == 'created' && isClientInRole"
              class="form-group"
            >
              <the-mask
                name="blikCode"
                v-validate="'required'"
                :mask="['### ###']"
                v-model="order.blikCode"
              />
              <div
                v-if="submitted && errors.has('blikCode')"
                class="alert-danger"
              >
                {{ errors.first('blikCode') }}
              </div>
            </div></b-col
          >
        </b-row>
        <br />
        <b-row>
          <b-col></b-col>
          <b-col></b-col>
          <b-col></b-col>
          <b-col>
            <b-button
              v-if="order.orderStatus == 'created' && isClientInRole"
              pill
              variant="primary"
              @click="repeatPayment()"
              >{{ $t('repeatPayment') }}
              <b-icon icon="credit-card" aria-hidden="true"></b-icon></b-button
          ></b-col>
        </b-row>
      </b-container>
    </form>
    <b-col>
      <b-button class="btn btn-primary pill" @click="goBack()">{{
        $t('goBack')
      }}</b-button>
    </b-col>
    <br />
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
import { TheMask } from 'vue-the-mask';
import Address from '../models/address';
import Product from '../models/product';
import Order from '../models/order';
import OrderService from '../services/order.service';

export default {
  components: { TheMask },
  name: 'OrderDetails',
  data() {
    return {
      submitted: false,
      successful: false,
      message: '',
      payUOrderId: this.$store.payUOrderId,
      order: {},
      sortDesc: false,
      sortDirection: 'asc',
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
          label: `${this.$t('quantityInOrder')}`,
          sortable: true,
          sortByFormatted: true,
          filterByFormatted: true,
        },
      ],
    };
  },
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isEmployeeInRole() {
      if (this.currentUserAccessLevel == 'EMPLOYEE') {
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
  },
  mounted() {
    if (this.payUOrderId == undefined) {
      if (this.isEmployeeInRole) {
        this.$router.push({
          path: '/orders',
        });
      } else if (this.isClientInRole) {
        this.$router.push({
          path: '/userOrders',
        });
      }
    } else {
      OrderService.findOrderByPayUOrderId(this.payUOrderId).then(
        (data) => {
          let orderDto = data.data;
          this.order = new Order(
            orderDto.payUOrderId,
            null,
            orderDto.totalValue,
            null,
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
                productDTO.quantity,
                productDTO.stock,
                productDTO.isActive,
                productDTO.description
              )
            );
          });
          this.order.products = productList;
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
  },
  methods: {
    goBack() {
      if (this.isClientInRole) {
        this.$router.push('/userOrders');
      } else if (this.isEmployeeInRole) {
        this.$router.push('/orders');
      } else {
        this.$router.push('/home');
      }
    },
    repeatPayment() {
      this.order.username = this.$store.state.auth.user.username;
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          this.$bvModal
            .msgBoxConfirm(this.$t('repeatpayment.msg'), {
              title: this.$t('areyousure'),
              size: 'sm',
              buttonSize: 'sm',
              okVariant: 'danger',
              okTitle: this.$t('yes'),
              cancelTitle: this.$t('no'),
              footerClass: 'p-2',
              hideHeaderClose: false,
              centered: true,
            })
            .then((value) => {
              if (value == true) {
                OrderService.repeatPayment(this.order).then(
                  (data) => {
                    this.responseList = data.data;
                    this.successful = true;
                    this.$bvModal
                      .msgBoxOk(this.$t(this.responseList), {
                        title: '',
                        size: 'sm',
                        buttonSize: 'sm',
                        okVariant: 'success',
                        headerClass: 'p-2 border-bottom-0',
                        footerClass: 'p-2 border-top-0',
                        centered: true,
                      }).then((val) => {
                        if(val == true){
                          this.$router.push("/userOrders");
                        }
                      });
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
              }
            })
            .catch((err) => {
              this.message = err.response && err.response.data;
              this.$bvModal.msgBoxConfirm(this.$t(this.message), {
                title: '',
                size: 'sm',
                buttonSize: 'sm',
                okVariant: 'error',
                headerClass: 'p-2 border-bottom-0',
                footerClass: 'p-2 border-top-0',
                centered: true,
              });
            });
        }
      });
    },
  },
};
</script>
