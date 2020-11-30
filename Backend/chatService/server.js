

const http = require("http");
//imports app from app.js
const app = require("./app");

//set port hardcoded to 5000
const port = 5000;
const server = http.createServer(app.app);

server.listen(port);
