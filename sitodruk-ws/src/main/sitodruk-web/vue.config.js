
// vue.config.js
module.exports = {
      pluginOptions: {
            i18n: {
              locale: 'pl',
              fallbackLocale: 'en',
              localeDir: 'locales',
              enableInSFC: true
            }
          },
      // proxy all webpack dev-server requests starting with /api
      // to our Spring Boot backend (localhost:8098) using http-proxy-middleware
      // see https://cli.vuejs.org/config/#devserver-proxy
      // devServer: {
      //       proxy: {
      //             '/api': {
      //                   target: console.env.VUE_APP_BASE_API_URL, // this configuration needs to correspond to the Spring Boot backends' application.properties server.port
      //                   ws: true,
      //                   changeOrigin: true
      //             }
      //       }
      // },
      // Change build paths to make them Maven compatible
      // see https://cli.vuejs.org/config/
      outputDir: './target/dist',
      assetsDir: 'static'
};