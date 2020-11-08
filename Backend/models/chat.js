const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const ChatSchema = new Schema({
  usrID1 : String,
  usrID2 : String,
  messages :  [{senderid: String, sender_name: String, message: String}],
  lastmessage : String
});

const Chat = mongoose.model('chat', ChatSchema);
module.exports = Chat;
