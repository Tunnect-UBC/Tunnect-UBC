const mongoose = require('mongoose');
const chatDBUrl = 'mongodb://127.0.0.1:27017/chatdb';
//Connect to Mongodb, chat service db
mongoose.connect(chatDBUrl, {useNewUrlParser:true, useUnifiedTopology:true});

//event dectector, look for connection ONCE
mongoose.connection.once('open',function(){
  console.log('Connected to Chat DB');
}).on('error',function(error){
  console.log('Connection error'.error);
});
