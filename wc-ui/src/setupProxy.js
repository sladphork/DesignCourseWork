const {createProxyMiddleware, proxy} = require('http-proxy-middleware')

// Figure out a way to inject the urls for each proxy.
module.exports = function(app) {
  app.use(
    '/iam/*', createProxyMiddleware({
      "target": "http://wc-iam:8080",
      "changeOrigin": true}
    )
  );

  app.use(
    '/professors', createProxyMiddleware({
      "target": "http://wc-professors:8080",
      "changeOrigin": true}
    )
  );

  app.use(
    '/professors/*', createProxyMiddleware({
      "target": "http://wc-professors:8080",
      "changeOrigin": true}
    )
  );

    app.use(
      '/faculties', createProxyMiddleware({
        "target": "http://wc-faculties:8080",
        "changeOrigin": true}
      )
  );
}