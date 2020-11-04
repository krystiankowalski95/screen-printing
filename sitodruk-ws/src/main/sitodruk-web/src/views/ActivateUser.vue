<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{content}}YOLOTRON</h3>
    </header>
  </div>
</template>

<script>
import AuthService from '../services/auth.service';

export default {
  name: 'activateUser',
  data() {
    return {
      content: ''
    };
  },
  mounted() {
    AuthService().confirmAccount.then(
    //   response => {
    //     this.content = response.data;
    //   },
    //   error => {
    //     this.content =
    //       (error.response && error.response.data) ||
    //       error.message ||
    //       error.toString();
    //   }
    );
  }
};
</script>
