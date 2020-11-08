const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const MessageSchema = new Schema({
  senderid : { type: String},
  message : { type: String},
  timeStamp: Long
});

const Message = mongoose.model('message', MessageSchema);
module.exports = Message;
