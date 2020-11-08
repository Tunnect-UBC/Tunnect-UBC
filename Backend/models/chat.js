const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const ChatSchema = new Schema({
  usrID1 : String,
  usrColour1: String,
  usrName1: String,
  usrID2 : String,
  usrColour2: String,
  usrName2: String,
  messages :  [{senderid: String, message: String, timeStamp: Number}],
  lastMessage : String,
  lastTime: Number

});

const Chat = mongoose.model('chat', ChatSchema);
module.exports = Chat;
