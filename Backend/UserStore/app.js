const express = require('express');
const app = express();
const morgan = require('morgan');

const userStoreRoutes = require('./api/routes/userstore');

//used for logging requests made
app.use(morgan('dev'));

app.use('/userstore', userStoreRoutes);

//No valid entrypoint
app.use((req, res, next) => {
    const error = new Error('Not found');
    error.status(404);
    next(error);
});

//general error handling code
app.use((error, req, res, next) => {

});

module.exports = app;