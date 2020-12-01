//This file handles all the API requests and different routes for the chat service
//Chat schema found in models/Chat
//Message schema found in models/Message
//status(200) indicates running smoothly :: status(404) connection error :: status(500) err handling


const express = require("express");
const router = new express.Router();
const mongoose = require("mongoose");
const axios = require("axios");

const helpers = require("../utils/chatServiceHelpers");

const Chat = require("../../models/chat");

/////////////////////GET requests//////////////////////
//syntax reminder::router.get('/' -targets requests aimed at /products

/**
*GET request for a list of available chats for a user
**/

router.get("/:userId", async (req, res, next) => {
  const id = req.params.userId;
  const result = await helpers.getChats(id);
  if(result[0] == 0){
    res.status(500).json(result[1]);
  }
  else if(result[0] == 1){
       res.status(200).json(result[1].concat(result[2]));
  }
  else{
    res.status(400).json({
      message: "unknown"
    });
  }
});


/**
*GET request for list of messages and their senders
*returns in the form of
*  [ {sender_name: String, message: String} ]
**/

router.get("/:userid1/:userid2", async (req, res, next) => {
   const id1 = req.params.userid1;
   const id2 = req.params.userid2;

   result = await helpers.getMessages(id1, id2);
   if(result[0] == 0){
     res.status(500).json(result[1]);
   }
   else if(result[0] == 1){
     res.status(200).json(result[1].concat(result[2]));
   }
   else{
     res.status(400).json({
       message: "unknown"
     });
   }

});


/**
*put a message in the messagedb and update the corressponding chat's message list
**/
router.post("/:receiverid", async (req, res, next) => {
     const senderid = req.body.senderid;
     const receiverid = req.params.receiverid;
     const message = req.body.message;
     const timeStamp = req.body.timeStamp;
     var notifId = "";
     var senderName = "";

     await axios.get("http://localhost:3000/userstore/" + receiverid, {params: {}})
     .then(async (response1) => {
        notifId = response1.data.notifId;
        await axios.get("http://localhost:3000/userstore/" + senderid, {params: {}})
        .then(async (response2) => {
          senderName = response2.data.username;
        });
     });
     result = await helpers.postMessage(senderid, senderName, receiverid, notifId, message, timeStamp);
     if(result[0] === 0){
       res.status(500).json(result[1]);
     }
     else if(result[0] === 1){
       res.status(200).json({});
     }
     else{
       res.status(400).json({
         message: "unkown"
       });
      }
  });

/**
*add a chat to the chatsdb
**/
router.post("/:usrid1/:usrid2", async (req, res, next) => {
   const usrid1 = req.params.usrid1;
   const usrid2 = req.params.usrid2;
   const timeStamp = req.body.timeStamp;
   var usr1name;
   var usr1colour;
   var chat;
    await axios.get("http://localhost:3000/userstore/" + usrid1, {params: {}})
   .then(async (response) => {
    usr1name = response.data.username;
    usr1colour = response.data.icon_colour;
    await axios.get("http://localhost:3000/userstore/" + usrid2, {params: {}})
    .then(async(response2) => {
     chat = new Chat({
      usrID1: usrid1,
      usrColour1: usr1colour,
      usrName1: usr1name,
      usrID2: usrid2,
      usrColour2: response2.data.icon_colour,
      usrName2: response2.data.username,
      messages: [{senderid: "tunnect", message: "Congrats: you've tunnected! Start a chat and say hi :)", timeStamp: timeStamp}],
      lastMessage: "Congrats: you've tunnected! Start a chat and say hi :)",
      lastTime: timeStamp
     });
   });
   });
   result = await helpers.postChat(chat, usrid1, usrid2);
   if(result[0] === 0){
      res.status(500).json({
        message: "db error"
      });
   }
   else if(result[0] === 1){
     res.status(200).json({});
   }
   else if(result[0] === 2){
     res.status(300).json({
       message: "Chat already exists"
     });
    }
    else{
      res.status(400).json({
        message: "unknown"
      })
    }

});

/**
* Deletes a chat(but not all messages tied to the chat)
**/
router.delete("/:userId1/:userId2", async (req, res, next) => {
    const id1 = req.params.userId1;
    const id2 = req.params.userId2;

    result = await helpers.deleteChat(id1, id2);
    if(result == 0){
      res.status(500).json({
        message: "db error"
      });
    }
    else if(result == 1){
      res.status(200).json({});
    }
    else if(result == 2){
      res.status(404).json({
        message: "chat not found"
      });
    }
    else{
      res.status(400).json({
        message: "unknown"
      });
    }
});


module.exports = router;
