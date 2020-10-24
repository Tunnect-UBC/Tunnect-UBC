/**
 * This is the first file that is run when you run "npm run-script start-userstore"
 * 
 * This listens on port 3000 by default, and creates the server that is specified in app.js
 * 
 */
const http = require('http');
const app = require('./app');

const port = process.env.PORT || 3000;

const server = http.createServer(app);

server.listen(port);