<template>
  <div class="col-md-12">
    <header class="jumbotron">
      <h1>{{ user.username }}</h1>
      <div class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/changePassword" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t('changePassword') }}
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/editUser" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t('editUser') }}
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/userOrders" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t('userOrders') }}
          </router-link>
        </li>
      </div>
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
  name: 'Profile',
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
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    } else {
      UserService.getUserProfile(this.$store.state.auth.user).then(
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
          }
        }
      );
    }
  },
};
</script>
