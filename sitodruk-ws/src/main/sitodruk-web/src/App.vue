<template>
  <div id="app">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <div class="navbar-nav mr-auto">
        <b-nav-item href="/home"><font-awesome-icon icon="house-user" />{{ $t("homePage") }}</b-nav-item>
        <b-nav-item href="/products"><font-awesome-icon icon="gifts" />{{ $t("products") }}</b-nav-item>
        <b-nav-item href="/orders" v-if="isEmployeeInRole == true"><font-awesome-icon icon="gifts" />{{ $t("orders") }}</b-nav-item>
        <b-nav-item href="/orders" v-if="isAdminInRole == true"><font-awesome-icon icon="gifts" />{{ $t("orders") }}</b-nav-item>
        <b-dropdown v-if="isAdminInRole" id="dropdown" class="m-md-2 navbar-nav ml-auto">
          <template #button-content>
            {{ $t("admin.panel") }}
          </template>
          <b-dropdown-item href="/users">{{ $t("user.list") }}</b-dropdown-item>
          <b-dropdown-item href="/createUser">{{ $t("user.create") }}</b-dropdown-item>
        </b-dropdown>
      </div>

      <div v-if="!currentUser" class="navbar-nav ml-auto">
        <b-nav-item href="/register">
          <font-awesome-icon icon="user-plus" />{{ $t("signup") }}</b-nav-item
        >
        <b-nav-item href="/login">
          <font-awesome-icon icon="sign-in-alt" />{{ $t("signin") }}</b-nav-item
        >
      </div>

      <div v-if="currentUser" class="navbar-nav ml-auto">
        <b-dropdown id="dropdown" class="m-md-2">
          <template #button-content>
            {{ currentUser.username }}
            <font-awesome-icon icon="user" />
          </template>
          <b-dropdown-item href="/profile"
            >{{ $t("profil") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item href="/changePassword">{{
            $t("changePassword")
          }}</b-dropdown-item>
          <b-dropdown-item href="/editUser">{{ $t("editUser") }}</b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-text
            >{{ $t("current.access.level") }}
            <span style="font-weight: bold" v-if="isClientInRole">{{
              $t("client")
            }}</span>
            <span style="font-weight: bold" v-if="isEmployeeInRole">{{
              $t("manager")
            }}</span>
            <span style="font-weight: bold" v-if="isAdminInRole">{{ $t("admin") }}</span>
          </b-dropdown-text>
          <b-dropdown-divider />
          <b-dropdown-item v-if="includesAdmin && isAdminInRole" disabled
            >{{ $t("admin") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item
            v-if="includesAdmin && isAdminInRole == false"
            @click="changeAccessLevel('ADMIN')"
            >{{ $t("admin") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item v-if="includesManager && isEmployeeInRole" disabled
            >{{ $t("manager") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item
            v-if="includesManager && isEmployeeInRole == false"
            @click="changeAccessLevel('EMPLOYEE')"
            >{{ $t("manager") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item v-if="includesClient && isClientInRole" disabled
            >{{ $t("client") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-item
            v-if="includesClient && isClientInRole == false"
            @click="changeAccessLevel('CLIENT')"
            >{{ $t("client") }} <font-awesome-icon icon="user"
          /></b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item v-if="isClientInRole" href="/userOrders">{{
            $t("userOrders")
          }}</b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item @click.prevent="logOut"
            >{{ $t("logout") }} <font-awesome-icon icon="sign-out-alt"
          /></b-dropdown-item>
        </b-dropdown>
      </div>
      <div class="navbar-nav" v-if="isClientInRole">
        <li class="nav-item">
          <router-link to="/cart" class="nav-link">
            <font-awesome-icon icon="shopping-cart" />
            {{ $t("cart") }}
          </router-link>
        </li>
      </div>
      <div class="navbar-nav">
        <b-select v-model="$i18n.locale">
          <option key="pl" value="pl">{{ $t("polish") }}</option>
          <option key="en" value="en">{{ $t("english") }}</option>
        </b-select>
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
    currentUserAccessLevel() {
      return this.$store.state.auth.currentAccessLevel;
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    includesAdmin() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_ADMIN");
      }
      return false;
    },
    includesManager() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_EMPLOYEE");
      }
      return false;
    },
    includesClient() {
      if (this.currentUser && this.currentUser.roles) {
        return this.currentUser.roles.includes("ROLE_CLIENT");
      }
      return false;
    },
    isAdminInRole() {
      if (this.currentUserAccessLevel == "ADMIN") {
        return true;
      }
      return false;
    },
    isEmployeeInRole() {
      if (this.currentUserAccessLevel == "EMPLOYEE") {
        return true;
      }
      return false;
    },
    isClientInRole() {
      if (this.currentUserAccessLevel == "CLIENT") {
        return true;
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
      this.$router.go();
    },
    changeAccessLevel(roleName) {
      this.$store.dispatch("auth/changeAccessLevel", roleName);
      this.$router.push("/home");
    },
  },
};
</script>
