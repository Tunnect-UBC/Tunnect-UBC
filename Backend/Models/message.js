const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const MessageSchema = new Schema({
  senderid : { type: String},
  sender_name : { type : String},
  sender_colour : {type : String},
  message : { type: String}
});

const Message = mongoose.model('message', MessageSchema);
module.exports = Message;
