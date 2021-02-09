<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('user.list') }}</h3>
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
        :items="userList"
        :fields="fieldSet"
        :current-page="currentPage"
        :per-page="perPage"
        :filter="filter"
        :filter-included-fields="filterOn"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
        :sort-direction="sortDirection"
        stacked="md"
        :empty-text="$t('userListEmpty')"
        :empty-filtered-text="$t('noFilteredUserFound')"
        show-empty
        striped
        @filtered="onFiltered"
      >
        <template #cell(username)="row">
          {{ row.value }}
        </template>

        <template #cell(firstname)="row">
          {{ $t(row.value) }}
        </template>

        <template #cell(lastname)="row">
          {{ $t(row.value) }}
        </template>

        <template #cell(actions)="row">
          <b-button-group v-if="row.item.active == true">
            <b-button disabled variant="success">{{ $t('activate') }}</b-button>
            <b-button @click="deactivate(row.item)" variant="danger">
              {{ $t('deactivate') }}</b-button
            >
          </b-button-group>
          <b-button-group v-if="row.item.active == false">
            <b-button @click="activate(row.item)" variant="success">{{
              $t('activate')
            }}</b-button>
            <b-button disabled variant="danger"
              >{{ $t('deactivate') }}
            </b-button>
          </b-button-group>
        </template>

        <template #cell(actionMenu)="row">
          <b-dropdown id="dd" class="m-md-2">
            <template #button-content>
              {{ $t('user.actions') }}
            </template>
            <b-dropdown-item @click="getDetails(row.item)">{{
              $t('details')
            }}</b-dropdown-item>
            <b-dropdown-item @click="editUser(row.item)">{{
              $t('edit')
            }}</b-dropdown-item>
            <b-dropdown-item v-if="row.item.active" @click="changePassword(row.item)">{{
              $t('changeSelectedUsersPassword')
            }}</b-dropdown-item>
            <b-dropdown-item
              v-if="row.item.confirmed == false"
              @click="sendEmail(row.item)"
              >{{ $t('sendEmail') }}</b-dropdown-item
            >
          </b-dropdown>
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
import UserService from '../services/user.service';
import User from '../models/user';

export default {
  name: 'UserOrders',
  data() {
    return {
      responseList: [],
      userList: [],
      message: '',
      content: '',
      successful: false,
      totalRows: 1,
      currentPage: 1,
      perPage: 5,
      pageOptions: [5, 10, 15],
      sortBy: '',
      sortDesc: false,
      sortDirection: 'asc',
      filter: null,
      filterOn: [],
      fieldSet: [
        {
          key: 'username',
          label: `${this.$t('username')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'firstname',
          label: `${this.$t('firstname')}`,
          sortable: true,
          sortDirection: 'desc',
        },
        {
          key: 'lastname',
          label: `${this.$t('lastname')}`,
          sortable: true,
          class: 'text-center',
        },

        { key: 'actions', label: ""},
        {
          key: 'actionMenu',
          label: "",
        },
      ],
    };
  },
  computed: {
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    isAdminInRole() {
      if (this.currentUserAccessLevel == 'ADMIN') {
        return true;
      }
      return false;
    },
  },
  mounted() {
    if (this.isAdminInRole) {
      UserService.getAllUsers().then(
        (data) => {
          this.responseList = data.data;
          this.responseList.map((userDto) => {
            this.userList.push(
              new User(
                userDto.username,
                userDto.email,
                userDto.password,
                userDto.confirmPassword,
                userDto.firstname,
                userDto.lastname,
                userDto.phoneNumber,
                userDto.token,
                userDto.dtoVersion,
                userDto.confirmed,
                userDto.active,
                userDto.roles
              )
            );
          });
          this.totalRows = this.userList.length;
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
    getDetails(user) {
      this.$store.selectedUser = user;
      this.$router.push({
        path: '/userDetails',
        params: { selectedUser: user },
      });
    },
    onFiltered(filteredItems) {
      this.totalRows = filteredItems.length;
      this.currentPage = 1;
    },

    changePassword(user) {
      this.$store.selectedUser = user;
      UserService.changeOtherUserPassword(user).then(
        (data) => {
          this.responseList = data.data;
          this.$bvModal
            .msgBoxOk(this.$t('ChangePasswordEmailSent'), {
              title: '',
              size: 'sm',
              buttonSize: 'sm',
              okVariant: 'success',
              headerClass: 'p-2 border-bottom-0',
              footerClass: 'p-2 border-top-0',
              centered: true,
            })
            .then((val) => {
              if (val == true) {
                this.$router.go();
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
        }
      );
    },

    sendEmail(user) {
      this.$store.selectedUser = user;
      UserService.sendActivationLink(user).then(
        (data) => {
          this.responseList = data.data;
          this.$bvModal
            .msgBoxOk(this.$t('emailSent'), {
              title: '',
              size: 'sm',
              buttonSize: 'sm',
              okVariant: 'success',
              headerClass: 'p-2 border-bottom-0',
              footerClass: 'p-2 border-top-0',
              centered: true,
            })
            .then((val) => {
              if (val == true) {
                this.$router.go();
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
        }
      );
    },

    editUser(user) {
      this.$store.selectedUser = user;
      this.$router.push({
        path: '/userEdit',
        params: { selectedUser: user },
      });
    },

    activate(user) {
      UserService.activateAccount(user).then(
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

    deactivate(user) {
      UserService.deactivateAccount(user).then(
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
  },
};
</script>
