<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{ $t('confirmAccount') }}</h3>
    </header>
  </div>
</template>

<script>
import UserService from '../services/user.service';

export default {
  name: 'ConfirmAccount',
  data() {
    return {
      content: ''
    };
  },
  mounted() {
    let token = this.$route.query.token;
    console.log(token);
    UserService.confirmAccount(token).then(
      response => {
        this.content = response.data;
        // this.$router.push('home');
      },
    );
  }
};
</script>
