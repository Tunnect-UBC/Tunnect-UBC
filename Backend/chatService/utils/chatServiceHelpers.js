const Chat = require("../../models/chat");
const axios = require("axios");

const helpers = {

  async deleteChat(user1, user2) {
   var resp = -1;
      await Chat.deleteMany({
        usrID1: user1,
        usrID2: user2
      }).then(async (result1) => {
        if(result1.deletedCount === 0){
            await Chat.deleteMany({
            usrID1: user2,
            usrID2: user1
          }).then((result2) => {
            if(result2.deletedCount === 1){
              resp = 1;
            }
            else {
              resp = 2;
            }
          });
        }
        else {
           resp = 1;
        }
      })
      .catch((err) => {
        resp = 0;
      });
      return resp;
   },

   async getChats(id) {
       if (id == "all"){
         await Chat.find({}, async function(err, result){
           if(err){
             return 0;
           }
           else {
             return 1;
           }
         });
       }
       else {
         await Chat.find({usrID1: id}, "usrID2 usrColour2 usrName2 lastMessage lastTime", async function(err, result1){
           if(err){
             return 0;
           }
           else if(!result1.length){
             await Chat.find({usrID2: id}, "usrID1 usrColour1 usrName1 lastMessage lastTime", async function (err, result2) {
               if(err){
                 return 0;
               }
               else {
                 return 1;
               }
             });
           }
           else{
             return 1;
           }
         });
       }
   },

   async getMessages(userid1, userid2){
     await Chat.find({usrID1: userid1, usrID2: userid2}, "messages", async function (err, result1) {
       if(!result1.length) {
         await Chat.find({usrID1: userid2, usrID2: userid1}, "messages", function (err, result2) {
           if(err){
             return 0;
           }
           else {
             return 1;
           }
         });
       }
       else if (err){
          return 0;
       }
       else {
         return 1;
       }
      }
     );

   },

async postMessage(senderid, receiverid, message, timeStamp) {
  await Chat.updateOne({usrID1: senderid, usrID2: receiverid},
           {$push: {messages : [{senderid: senderid, message: message, timeStamp: timeStamp}]},
           $set: {lastMessage: message, lastTime: timeStamp}})
           .then(async (result) => {
             await Chat.updateOne({usrID1: receiverid, usrID2: senderid},
           {$push: {messages : [{senderid: senderid, message: message, timeStamp: timeStamp}]},
           $set: {lastMessage: message, lastTime: timeStamp}}, function(err, result){});})
          .then((result) => {
            return 1;
          })
          .catch((err) => {
             return 0;
           });
 },


async postChat(usrid1, usrid2, timeStamp){
  await axios.get("http://localhost:3000/userstore/" + usrid1, {params: {}})
  .then(async (response) => {
  const usr1 = usrid1;
  const usr2 = usrid2;
  var usr1name = response.data.username;
  var usr1colour = response.data.icon_colour;

 await axios.get("http://localhost:3000/userstore/" + usrid2, {params: {}})
 .then(async(response2) => {
  var chat = new Chat({
    usrID1: usr1,
    usrColour1: usr1colour,
    usrName1: usr1name,
    usrID2: usr2,
    usrColour2: response2.data.icon_colour,
    usrName2: response2.data.username,
    messages: [{senderid: "tunnect", message: "Congrats: you've tunnected! Start a chat and say hi :)", timeStamp: timeStamp}],
    lastMessage: "Congrats: you've tunnected! Start a chat and say hi :)",
    lastTime: timeStamp
  });
  await Chat.find({usrID1: usr1, usrID2: usr2}, async function(err, result1){
    if(!result1.length){
      await Chat.find({usrID1: usr2, usrID2: usr1}, async function(err, result2){
        if(!result2.length){
          chat.save()
                 .then((result) => {
                   return 1;
                 })
                 .catch((err) => {
                   return 0;
                 });
        }
        else {
          return 2;
        }
      });
    } else {
      return 2;
    }
  });
 })
 .catch((err) => {
  return 0;
 });
});
}

}
module.exports = helpers;
