<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{ $t('confirmAccount') }}</h3>
    </header>
    <div
        v-if="message"
        class="alert"
        :class="successful ? 'alert-success' : 'alert-danger'"
      >{{ $t(message.message) }}</div>
    </div>
</template>

<script>
import UserService from '../services/user.service';

export default {
  name: 'ConfirmAccount',
  data() {
    return {
      content: '',
      message: '',
      successful: false
    };
  },
  mounted() {
    let token = this.$route.query.token;
    UserService.confirmAccount(token).then(
      response => {
        this.message = response.data;
        this.successful = true;
        this.$alert(this.$t('account.confirmed'));
              this.$router.push("/login");
      },
      error => {
              this.message = 
                (error.response && error.response.data);
              this.successful = false;
            }
    );
  }
};
</script>
