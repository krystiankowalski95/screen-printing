<template>
  <div class="col-md-12">
  <header class="jumbotron">
    <h3>{{$t('changePassword')}}</h3>
  </header>
      <form name="form" @submit.prevent="changePassword">
        <div v-if="!successful">
          <div class="form-group">
            <label for="password">{{ $t('password') }}</label>
            <input
              v-model="user.password"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="password"
            />            <div
              v-if="submitted && errors.has('password')"
              class="alert-danger"
            >{{errors.first('password')}}</div>
          </div>
           <div class="form-group">
            <label for="confirmPassword">{{ $t('confirmPassword') }}</label>
            <input
              v-model="user.confirmPassword"
              v-validate="'required|min:6|max:40'"
              type="password"
              class="form-control"
              name="confirmPassword"
            />
            <div
              v-if="submitted && errors.has('confirmPassword')"
              class="alert-danger"
            >{{errors.first('confirmPassword')}}</div>
          </div>
          <div class="form-group">
            <button class="btn btn-primary btn-block">{{ $t('changePasswordButton') }}</button>
          </div>
        </div>
      </form>
      <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{message}}</div>
    </div>
</template>

<script>
import User from '../models/user';
import UserService from '../services/user.service';

export default {
  name: 'ChangePassword',
  data() {
     return {
      user: new User('', '', ''),
      submitted: false,
      successful: false,
      message: ''
    };
  },
  mounted() {
      this.user.username = this.$store.state.auth.user.username;
  },
  methods: {
      changePassword(){
          this.message = '';
      this.submitted = true;
      this.$validator.validate().then(isValid => {
        if (isValid) {
            
          UserService.changePassword(this.user).then(
            data => {
              this.message = data.message;
              this.successful = true;
            },
            error => {
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
              this.successful = false;
            }
          );
        }
      });
      }
  }
};
</script>