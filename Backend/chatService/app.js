//**
//*This is the server that parses requests for chatdb
//*
//*This file:
//*    -Connects to the chat database through chatDBUrl
//*    -Prints notification if connection was successfil

const express = require('express');
const app = express();
const morgan = require('morgan');
const bodyParser = require('body-parser');

const mongoose = require('mongoose');
//const chatDBUrl = 'mongodb://127.0.0.1:27017/chatdb';
const messageDBUrl = 'mongodb://127.0.0.1:27017/messagedb';

const chatserviceRoutes = require('./routes/chatservice');

//Connect to Mongodb, chat service db
//var chatDB = mongoose.createConnection(chatDBUrl, {
//  useNewUrlParser:true,
//  useUnifiedTopology:true
//});

var messageDB = mongoose.connect(messageDBUrl, {
  useNewUrlParser:true,
  useUnifiedTopology:true
});

mongoose.connection.once('open', _ => {
  console.log('Connected to MongoDB')
})
mongoose.connection.on('error', err => {
  console.error('Connection error ', err)
})

//used for logging requests made
app.use(morgan('dev'));
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

//adding headers to all responses to avoid CORS errors
app.use((req,res,next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', '*');
  if (req.method == 'OPTIONS') {
    res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, PATCH');
    return res.status(200).json({});
  }
  next();
});

//middleware, sends chatservice requests to chatservice.js
app.use('/chatservice', chatserviceRoutes);

//No valid entrypoint
app.use((req, res, next) => {
    const error = new Error('Not found');
    error.status = 404;
    next(error);
});


//general error handling code
app.use((error, req, res, next) => {
    res.status(error.status || 500);
    res.json({
        error: {
            message: error.message,
            status: error.status
        }
    });
});

module.exports = app;
