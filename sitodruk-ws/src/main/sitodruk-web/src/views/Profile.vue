<template>
  <div class="container">
    <header class="jumbotron">
      <div class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/changePassword" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t("changePassword") }}
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/editUser" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t("editUser") }}
          </router-link>
        </li>
      </div>
    </header>
    <p><strong>{{ $t('username')}}:</strong> {{ currentUser.username}}</p>
    <p><strong>{{ $t('firstname')}}:</strong> {{ this.user.firstname}}</p>
    <p><strong>{{ $t('lastname')}}:</strong> {{ this.user.lastname}}</p>
    <p><strong>{{ $t('email')}}:</strong> {{ this.user.email}}</p>
    <p><strong>{{ $t('phoneNumber')}}:</strong> {{ this.user.phoneNumber}}</p>

  </div>
</template>

<script>
import UserService from "../services/user.service";
import User from "../models/user";

export default {
  name: "Profile",
  user: new User(),
  message: "",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    } else {
      UserService.getUserProfile(this.$store.state.auth.user).then(
        (data) => {
          this.user = new User(data.data.username, data.data.email, data.data.password,data.data.confirmPassword,data.data.firstname,data.data.lastname,data.data.phoneNumber,data.data.token,data.data.dtoVersion,data.data.confirmed,data.data.active,data.data.roles);
          data.data;
          console.log(this.user);
        },
        (error) => {
          this.message = error.response && error.response.data;
          if (this.message.status == 401) {
            this.$store.dispatch("auth/logout");
          }
        }
      );
    }
  },
};
</script>
