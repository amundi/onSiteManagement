module.exports = {
  devServer: {
    port: 9000,
    proxy: {
      "/": {
        target: "http://localhost:8080",
        secure: false,
        logLevel: "debug",
        changeOrigin: true,
        xfwd: true
      }
    },
    disableHostCheck: true
  }
};
