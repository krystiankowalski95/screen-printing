<template>
  <div class="container">
    <header class="jumbotron" style="height: 150px">
      <h3>{{ $t('user.list') }}</h3>
    </header>
    <b-container>
      <b-row>
        <b-col>{{ $t('id') }}</b-col>
        <b-col>{{ $t('username') }}</b-col>
        <b-col>{{ $t('firstname') }}</b-col>
        <b-col>{{ $t('lastname') }}</b-col>
        <b-col></b-col>
        <b-col></b-col>
        <b-col></b-col>
      </b-row>
    </b-container>
    <b-container
      class="bv-example-row"
      v-for="(user, index) in userList"
      :key="index"
    >
      <b-row style="padding: 5px">
        <b-col draggable="true">{{ index + 1 }}</b-col>
        <b-col>{{ user.username }}</b-col>
        <b-col>{{ user.firstname }}</b-col>
        <b-col>{{ user.lastname }}</b-col>
        <b-col>
          <b-button-group v-if="user.active == true">
            <b-button disabled variant="success">{{ $t('activate') }}</b-button>
            <b-button @click="deactivate(index)" variant="danger">
              {{ $t('deactivate') }}</b-button
            >
          </b-button-group>
          <b-button-group v-if="user.active == false">
            <b-button @click="activate(index)" variant="success">{{
              $t('activate')
            }}</b-button>
            <b-button disabled variant="danger"
              >{{ $t('deactivate') }}
            </b-button>
          </b-button-group>
        </b-col>
        <b-col>
          <b-dropdown id="dd" class="m-md-2">
            <template #button-content>
              {{ $t('user.actions') }}
            </template>
            <b-dropdown-item @click="getDetails(index)">{{
              $t('details')
            }}</b-dropdown-item>
            <b-dropdown-item @click="editUser(index)">{{
              $t('edit')
            }}</b-dropdown-item>
            <b-dropdown-item @click="changePassword(index)">{{
              $t('changeSelectedUsersPassword')
            }}</b-dropdown-item>
           <b-dropdown-item v-if="user.confirmed == false" @click="sendEmail(index)">{{
              $t('sendEmail')
            }}</b-dropdown-item>
          </b-dropdown>
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
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdminInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes('ROLE_ADMIN');
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
      this.$store.selectedUser = this.userList[index];
      this.$router.push({
        path: '/userDetails',
        params: { selectedUser: this.userList[index] },
      });
    },

     changePassword(index) {
      this.$store.selectedUser = this.userList[index];
      this.$router.push({
        path: '/changeUsersPassword',
        params: { selectedUser: this.userList[index] },
      });
    },

    sendEmail(index) {
      this.$store.selectedUser = this.userList[index];
      UserService.sendActivationLink(this.userList[index]).then(
        (data) => {
          this.responseList = data.data;
           this.$alert(this.$t('emailSent'));
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


    editUser(index) {
      this.$store.selectedUser = this.userList[index];
      this.$router.push({
        path: '/userEdit',
        params: { selectedUser: this.userList[index] },
      });
    },

    activate(index) {
      UserService.activateAccount(this.userList[index]).then(
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
      UserService.deactivateAccount(this.userList[index]).then(
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
