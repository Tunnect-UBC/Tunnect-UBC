/**
 * This is the actual server that parses requests
 * 
 * This file:
 *      -Sets up morgan as logger for easy debugging
 *      -Adds header to all possible requests, such that we can avoid all CORS errors
 *      -Specifies route for /matchmaker, described in ../routes/matchmaker.js
 *      -Handles errors for any requests that do not provide a valid endpoint
 * 
 */
const helpers = require("../server/init");
const imports = helpers.imports();

const matchmakerRoutes = require("./routes/matchmaker");

helpers.connectMorgan(imports.app, imports.morgan, imports.bodyParser);

helpers.setHeaders(imports.app);

imports.app.use("/matchmaker", matchmakerRoutes);

helpers.setEntryError(imports.app);
helpers.setErrorHandle(imports.app);

module.exports = imports.app;