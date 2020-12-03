const Chat = require("../../models/chat");
const axios = require("axios");
const utils =  require("../app");
const {admin} = require("../../firebase-config");

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
        }else {
           resp = 1;
         }
      })
      .catch((err) => {
        resp = 0;
      });
      return resp;
   },

   async getChats(id) {
     var chatResp = [];
       if (id === "all"){
         await Chat.find({}, async function(err, result){
           if(err){
             chatResp = [0, err];
           }
           else {
             chatResp = [1, result];
           }
         });
       }
       else {
          await Chat.find({usrID1: id}, "usrID2 usrColour2 usrName2 lastMessage lastTime", async function(err, result1){
           if(err){
             chatResp = [0, err];
           }
           else {
             chatResp = [1, result1];
          }}).then(async(result) => {
              await Chat.find({usrID2: id}, "usrID1 usrColour1 usrName1 lastMessage lastTime", async function (err, result2) {
               if(err){
                resp =[0, err];
               }
               else {
                 chatResp[2] = result2;
               }
             });
           });
       }
       return chatResp;
   },

   async getMessages(userid1, userid2){
     var messResp = [];
     await Chat.find({usrID1: userid1, usrID2: userid2}, "messages", async function (err, result1) {
       if (err){
         messResp = [0, err];
       }
       else {
         messResp = [1, result1];
       }
     }).then(async(res) => {
        await Chat.find({usrID1: userid2, usrID2: userid1}, "messages", async function (err, result2) {
          if(err){
           messResp = [0, err];
          }
          else {
           messResp[2] = result2;
          }
        });
      });
     return messResp;
   },

async postMessage(senderid, senderName, receiverid, notifId, message, timeStamp) {
  var resp = [];
  const messNotif = {
    notification: {
      title: senderName,
      body: message
    }
  };
  await Chat.updateOne({usrID1: senderid, usrID2: receiverid},
           {$push: {messages : [{senderid, message, timeStamp}]},
           $set: {lastMessage: message, lastTime: timeStamp}})
           .then(async (result) => {
             await Chat.updateOne({usrID1: receiverid, usrID2: senderid},
           {$push: {messages : [{ senderid, message, timeStamp}]},
           $set: {lastMessage: message, lastTime: timeStamp}});})
          .then(async (result) => {
            if(notifId !== "0"){
            await admin.messaging().sendToDevice(notifId, messNotif, utils.notifOpt);
          }
            resp = [1];
          })
          .catch((err) => {
             resp = [0, err];
           });
           return resp;
 },


async postChat(chat, usr1, usr2){
  var resp = [];
  await Chat.find({usrID1: usr1, usrID2: usr2}, async function(err, result1){
      if (err) {
        resp = [0,err];
      }
      else{
        resp = [1, result1];
      }
  })
  .then(async (res1) => {
    await Chat.find({usrID1: usr2, usrID2: usr1}, async function(err, result2){
      if(err) {
        resp = [0, err];
      }
      else {
        resp[2] = result2;
      }
   });
  })
  .then(async (res2) => {
    if(resp[1].length === 0 && resp[2].length === 0){
      await chat.save()
            .then((result) => {
               resp[0] = 1;
            })
            .catch((err) => {
               resp = [0, err];
            });
    }
    else {
      resp[0] = 2;
    }
  })
  .catch((err) => {
  resp = [0, err];
  });
 return resp;
}

};
module.exports = helpers;
