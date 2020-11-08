/**
 * This is the first file that is run when you run "npm run-script start-matchmaker"
 * 
 * This listens on port 3001 by default, and creates the server that is specified in app.js
 * 
 */
const http = require("http");
const app = require("./app");

const port = process.env.PORT || 3001;

const server = http.createServer(app);

server.listen(port, "0.0.0.0");
