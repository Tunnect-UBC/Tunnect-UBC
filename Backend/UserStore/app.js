/**
 * This is the actual server that parses requests
 * 
 * This file:
 *      -Connects to mongodb server as specified by userDBURL (currently looks for localhost)
 *      -Prints notification to display whether or not connection was successful
 *      -Sets up morgan as logger for easy debugging
 *      -Adds header to all possible requests, such that we can avoid all CORS errors
 *      -Specifies route for /userstore, described in ../routes/userstore.js
 *      -Handles errors for any requests that do not provide a valid endpoint
 * 
 */
const helpers = require("../server/init");

const imports = helpers.imports();

const userDBUrl = "mongodb://127.0.0.1:27017/userDB";
const userStoreRoutes = require("./routes/userstore");

helpers.connectMongo(imports.mongoose, userDBUrl);
helpers.connectMorgan(imports.app, imports.morgan, imports.bodyParser);

helpers.setHeaders(imports.app);

imports.app.use("/userstore", userStoreRoutes);

helpers.setEntryError(imports.app);
helpers.setErrorHandle(imports.app);

module.exports = imports.app;