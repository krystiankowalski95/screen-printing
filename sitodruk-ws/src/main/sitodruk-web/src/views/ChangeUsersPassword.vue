<template>
  <div class="col-md-12">
    <header class="jumbotron">
      <h3>{{ $t('change.user.password') }} {{ user.username }}</h3>
    </header>
    <div class="card card-container">
      <form name="form" @submit.prevent="changePassword">
        <div v-if="!successful">
          <div class="form-group">
            <label for="password">{{ $t('password') }}*</label>
            <input
              v-model="user.password"
              v-validate="'required|min:8|max:40|confirmed:confirmPassword'"
              type="password"
              class="form-control"
              name="password"
            />
            <div
              v-if="submitted && errors.has('password')"
              class="alert-danger"
            >
              {{ errors.first('password') }}
            </div>
          </div>
          <div class="form-group">
            <label for="confirmPassword">{{ $t('confirmPassword') }}*</label>
            <input
              v-model="user.confirmPassword"
              v-validate="'required|min:8|max:40'"
              type="password"
              ref="confirmPassword"
              class="form-control"
              name="confirmPassword"
            />
            <div
              v-if="submitted && errors.has('confirmPassword')"
              class="alert-danger"
            >
              {{ errors.first('confirmPassword') }}
            </div>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block">
              {{ $t('changePasswordButton') }}
            </button>
          </div>
        </div>
      </form>
       <div class="form-group">
        <button
          class="btn btn-primary btn-block swal2-confirm swal2-styled"
          @click="goBack()"
        >
          {{ $t('goBack') }}
        </button>
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
import User from '../models/user';
import UserService from '../services/user.service';

export default {
  name: 'ChangePassword',
  data() {
    return {
      user: new User(),
      submitted: false,
      successful: false,
      message: '',
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
    if (this.$store.selectedUser == undefined) {
      this.$router.push('/users');
    } else {
      if (!this.isAdminInRole) {
        this.$router.push('/home');
      } else {
        UserService.findAccount(this.$store.selectedUser).then(
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
  methods: {
    goBack() {
      this.$router.push('/users');
    },
    changePassword() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          UserService.changeOtherUserPassword(this.user).then(
            (data) => {
              this.message = data.message;
              this.successful = true;
              this.$alert(this.$t('password.has.been.set'));
              this.$router.push('/users');
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
      });
    },
  },
};
</script>



<style scoped>
label {
  display: block;
  margin-top: 10px;
}

.card-container.card {
  max-width: 350px !important;
  padding: 40px 40px;
}

.card {
  background-color: #f7f7f7;
  padding: 20px 25px 30px;
  margin: 0 auto 25px;
  margin-top: 50px;
  -moz-border-radius: 2px;
  -webkit-border-radius: 2px;
  border-radius: 2px;
  -moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  -webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}

.profile-img-card {
  width: 96px;
  height: 96px;
  margin: 0 auto 10px;
  display: block;
  -moz-border-radius: 50%;
  -webkit-border-radius: 50%;
  border-radius: 50%;
}
</style>