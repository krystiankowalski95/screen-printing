<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark" style="height: ">
      <a href class="navbar-brand" @click.prevent></a>
      <div class="navbar-nav mr-auto">
        <li class="nav-item">
          <router-link to="/home" class="nav-link"> <font-awesome-icon icon="house-user" />{{ $t("homePage") }} </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/products" class="nav-link"> <font-awesome-icon icon="gifts" />{{ $t("products") }} </router-link>
        </li>
        <li v-if="isAdminInRole" class="nav-item">
          <router-link to="/admin" class="nav-link">Admin Board</router-link>
        </li>
        <li v-if="isManagerInRole" class="nav-item">
          <router-link to="/mod" class="nav-link">Manager Board</router-link>
        </li>
        <li v-if="isClientInRole" class="nav-item">
          <router-link to="/user" class="nav-link">User</router-link>
        </li>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/register" class="nav-link"> <font-awesome-icon icon="user-plus" />{{ $t("signup") }} </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login" class="nav-link"> <font-awesome-icon icon="sign-in-alt" />{{ $t("signin") }} </router-link>
        </li>
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <li class="nav-item">
          <router-link to="/profile" class="nav-link">
            <font-awesome-icon icon="user" />
            {{ currentUser.username }}
          </router-link>
        </li>
        <li class="nav-item">
          <a class="nav-link" href @click.prevent="logOut"> <font-awesome-icon icon="sign-out-alt" />{{ $t("logout") }} </a>
        </li>
      </div>
      <div class="navbar-nav">
        <span class="nav-item"> {{ this.$store.getters.shoppingListSize }}</span>
        <select v-model="$i18n.locale">
          <option key="pl" value="pl">{{ $t("polish") }}</option>
          <option key="en" value="en">{{ $t("english") }}</option>
        </select>
      </div>
    </nav>

    <div class="container">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    isAdminInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_ADMIN");
      }

      return false;
    },
    isManagerInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_MANAGER");
      }

      return false;
    },
    isClientInRole() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_CLIENT");
      }

      return false;
    },
  },
  name: "locale-changer",
  data() {
    return { langs: ["pl", "en"] };
  },
  methods: {
    logOut() {
      this.$store.dispatch("auth/logout");
      this.$router.push("/login");
    },
  },
};
</script>
