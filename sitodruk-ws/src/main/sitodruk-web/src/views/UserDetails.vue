<template>
  <div class="col-md-12">
    <header class="jumbotron">
      <h1>{{ user.username }}</h1>
    </header>
    <div class="card card-container">
      <div class="form-group">
        <label for="firstname">{{ $t('firstname') }}</label>
        <input
          v-model="user.firstname"
          disabled
          type="text"
          class="form-control"
          name="firstname"
        />
      </div>
      <div class="form-group">
        <label for="lastname">{{ $t('lastname') }}</label>
        <input
          v-model="user.lastname"
          disabled
          type="text"
          class="form-control"
          name="lastname"
        />
      </div>
      <div class="form-group">
        <label for="username">{{ $t('username') }}</label>
        <input
          v-model="user.username"
          disabled
          type="text"
          class="form-control"
          name="username"
        />
      </div>
      <div class="form-group">
        <label for="email">{{ $t('email') }}</label>
        <input
          v-model="user.email"
          disabled
          type="email"
          class="form-control"
          name="email"
        />
      </div>
      <div class="form-group">
        <label for="phoneNumber">{{ $t('phoneNumber') }}</label>
        <input
          v-model="user.phoneNumber"
          disabled
          type="text"
          class="form-control"
          name="phoneNumber"
        />
      </div>
      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >
        {{ $t(message.message) }}
      </div>
    </div>
  </div>
</template>

<script>
import UserService from '../services/user.service';
import User from '../models/user';

export default {
  name: 'UserDetails',
  data() {
    return {
      user: new User(),
      message: '',
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
    if (this.$store.selectedUser == undefined) {
       this.$router.push('/users');
    } else {
      if (!this.isAdminInRole) {
        this.$router.push('/home');
      } else {
        UserService.getUserProfile(this.$store.selectedUser).then(
          (data) => {
            this.user = new User(
              data.data.username,
              data.data.email,
              data.data.password,
              data.data.confirmPassword,
              data.data.firstname,
              data.data.lastname,
              data.data.phoneNumber,
              data.data.token,
              data.data.dtoVersion,
              data.data.confirmed,
              data.data.active,
              data.data.roles
            );
            data.data;
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
    }
  },
};
</script>
