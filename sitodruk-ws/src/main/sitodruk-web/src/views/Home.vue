<template>
  <div class="container">
    <header class="jumbotron">
      <h3>{{content}}</h3>
      <h3>{{ $t('hello') }}</h3>
    </header>
  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      content: ''
    };
  },
  mounted() {
  }
};
</script>
