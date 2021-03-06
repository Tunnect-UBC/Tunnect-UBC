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

imports.app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "*");
    if (req.method === "OPTIONS") {
        res.header("Access-Control-Allow-Methods", "PUT, POST, GET, DELETE, PATCH");
        return res.status(200).json({});
    }
    next();   
});

imports.app.use("/matchmaker", matchmakerRoutes);

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