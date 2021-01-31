<template>
  <div class="col-md-12">
    <header class="jumbotron">
      <h3>{{ $t('change.user.password') }} {{user.username}}</h3>
    </header>
    <div class="card card-container">
      <form name="form" @submit.prevent="changePassword">
        <div v-if="!successful">
          <div class="form-group">
            <label for="password">{{ $t('password') }}</label>
            <input
              v-model="user.password"
              v-validate="'required|min:6|max:40|confirmed:confirmPassword'"
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
            <label for="confirmPassword">{{ $t('confirmPassword') }}</label>
            <input
              v-model="user.confirmPassword"
              v-validate="'required|min:6|max:40'"
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
    changePassword() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then((isValid) => {
        if (isValid) {
          UserService.changeOtherUserPassword(this.user).then(
            (data) => {
              this.message = data.message;
              this.successful = true;
              this.$router.push('/users');
            },
            (error) => {
              this.message = error.response && error.response.data;

              this.successful = false;
            }
          );
        }
      });
    },
  },
};
</script>