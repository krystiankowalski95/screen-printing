<template>
  <div class="container">
    <header class="jumbotron">
      <h3>
        <strong>{{ currentUser.username }}</strong> Profile
      </h3>
        <div class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/changePassword" class="nav-link">
            <font-awesome-icon icon="plus-square" />{{ $t('changePassword') }}
          </router-link>
        </li>
      </div>
    </header>
    <p>
      <strong>Email:</strong>
      {{ currentUser.email }}
    </p>
    <strong>Authorities:</strong>
    <ul v-for="(role,index) in currentUser.roles" :key="index">
      <li v-if="(role == 'ROLE_CLIENT')">
        {{ $t("roleClient") }}
      </li>
      <li v-if="(role == 'ROLE_MANAGER')">
        {{ $t("roleManager") }}
      </li>
      <li v-if="(role == 'ROLE_ADMIN')">
        {{ $t("roleAdmin") }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "Profile",
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/login");
    }
  },
};
</script>
