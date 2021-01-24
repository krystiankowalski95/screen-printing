<template>
  <div class="container">
    <b-carousel
      id="carousel-1"
      v-model="slide"
      :interval="4000"
      controls
      indicators
      background="#ababab"
      img-width="1024"
      img-height="480"
      style="text-shadow: 1px 1px 2px #333;"
    >
      <b-carousel-slide img-src="https://cdn.pixabay.com/photo/2016/03/09/11/33/silk-screen-1246169_1280.jpg" />
      <b-carousel-slide img-src="https://cdn.pixabay.com/photo/2016/04/21/01/21/paintings-1342452_1280.jpg"/>
      <b-carousel-slide img-src="https://cdn.pixabay.com/photo/2017/09/30/02/25/painting-2801050_1280.jpg"/>
      <b-carousel-slide img-src="https://cdn.pixabay.com/photo/2017/07/03/20/17/abstract-2468874_1280.jpg"/>
    </b-carousel>
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
  },
};
</script>
