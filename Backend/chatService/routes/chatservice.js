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
  if(result == 1){
    res.status(200).json({});
  }
  else {
    res.status(500).json({});
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

   const result = await helpers.getMessages(id1, id2);
   if(result == 1){
     res.status(200).json({});
   }
   else {
     res.status(500).json({});
   }

});


/**
*put a message in the messagedb and update the corressponding chat's message list
**/
router.post("/:receiverid", async (req, res, next) => {
  const senderid = req.body.senderid;
  const receiverid = req.params.recieverid;
  const message = req.body.message;
  const timeStamp = req.body.timeStamp

  const result = await helpers.postMessage(senderid, receiverid, message, timeStamp);
  if(result == 1){
    res.status(200).json({});
  }
  else{
    res.status(500).json({});
  }
  });

/**
*add a chat to the chatsdb
**/
router.post("/:usrid1/:usrid2", async (req, res, next) => {
  const usrid1 = req.params.usrid1;
  const usrid2 = req.params.usrid2;
  const timeStamp = req.body.timeStamp;
  const result = await helpers.postChat(usrid1, usrid2, timeStamp);
  if(result == 1){
    res.status(200).json({});
  }
  else if(result == 0){
    res.status(300).json({});
  }
  else if(result == 2){
    res.status(404).json({});
  }
  else {
    res.status(500).json({});
  }
});

/**
* Deletes a chat(but not all messages tied to the chat)
**/
router.delete("/:userId1/:userId2", async (req, res, next) => {
    const id1 = req.params.userId1;
    const id2 = req.params.userId2;

    const result = await helpers.deleteChat(id1, id2);
    if(result === 1){
      res.status(200).json({message: "Chat deleted"});
    }
    else if(result === 0){
      res.status(404).json({message: "Chat not found"});
    }
    else{
      res.status(500).json({error: "Request Error"});
    }
});


module.exports = router;
