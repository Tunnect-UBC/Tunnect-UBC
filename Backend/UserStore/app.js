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

imports.app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "*");
    if (req.method === "OPTIONS") {
        res.header("Access-Control-Allow-Methods", "PUT, POST, GET, DELETE, PATCH");
        return res.status(200).json({});
    }
    next();   
});

imports.app.use("/userstore", userStoreRoutes);

imports.app.use((req, res, next) => {
    const error = new Error("Not found");
    error.status = 404;
    next(error);
});

imports.app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message,
            status: error.status
        }
    });
});

module.exports = imports.app;