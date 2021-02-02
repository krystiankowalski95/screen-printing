<template>
  <div class="col-md-12">
    <div class="card card-container" v-if="!successful">
      <form name="form" @submit.prevent="sendEmail">
        <div class="form-group">
          <label for="email">{{ $t('email') }}*</label>
          <input
            v-model="user.email"
            v-validate="'required|email|max:100'"
            type="text"
            class="form-control"
            name="email"
          />
          <div v-if="submitted && errors.has('email')" class="alert-danger">
            {{ errors.first('email') }}
          </div>
        </div>
        <div class="form-group">
          <button class="btn btn-primary btn-block" :disabled="loading">
            <span
              v-show="loading"
              class="spinner-border spinner-border-sm"
            ></span>
            <span>{{ $t('sendResetPasswordMail') }}</span>
          </button>
        </div>
        <div
          v-if="message"
          class="alert"
          :class="successful ? 'alert-success' : 'alert-danger'"
        >
          {{ $t(message.message) }}
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import User from '../models/user';
import UserService from '../services/user.service';

export default {
  name: 'Login',
  data() {
    return {
      user: new User(''),
      loading: false,
      message: '',
      successful: false,
      submitted: false
    };
  },
  methods: {
    sendEmail() {
      this.loading = true;
      this.submitted = true;
      this.$validator.validateAll().then((isValid) => {
        if (!isValid) {
          this.loading = false;
          return;
        } else {
          this.user.language = this.$i18n.locale;
          UserService.resetPassword(this.user).then(
            (response) => {
              this.message = response.data;
              this.successful = true;
              this.$alert(this.$t('email.has.been.sent'));
              this.$router.push('/login');
            },
            (error) => {
              this.$alert(this.$t('email.has.been.sent'));
              this.$router.push('/login');
              this.loading = false;
              this.successful = false;
              this.message = error.response && error.response.data;
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
</style>