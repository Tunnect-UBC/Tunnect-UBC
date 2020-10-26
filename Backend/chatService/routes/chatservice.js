//This file handles all the API requests and different routes for the chat service
//Chat schema found in models/Chat
//Message schema found in models/Message
//status(200) indicates running smoothly :: status(404) connection error :: status(500) err handling


const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');


const Chat = require('../../models/chat');
const Message = require('../../models/message');

/////////////////////GET requests//////////////////////
//syntax reminder::router.get('/' -targets requests aimed at /products

/**
*GET request for a list of available chats for a user
**/
router.get('/:userId', (req, res, next) => {
  const id = req.params.userId;
  if (id === 'all'){
    Chat.find({}, function(err, result){
      if(err){
        res.send(err);
      } else {
        res.send(result);
      }
    });
  } else {
    Chat.find({usrID1: id}, 'usrID2 lastmessage', function(err, result){
      if(err){
        res.send(err);
      } else {
        res.send(result);
      }
    });
   }
});


/**
*GET request for list of messages and their senders
*returns in the form of
*  [ {sender_name: String, message: String} ]
**/

router.get('/:userid1/:userid2', (req, res, next) => {
   const id1 = req.params.userid1;
   const id2 = req.params.userid2;

  Chat.find({usrID1: id1}, 'messages', function(err, result1){
    if (err) {
      Chat.find({usrID1: id2}, 'messages', function(err, result2){
        if(err){
          res.send(err);
        } else {
          res.send(result2);
        }
      });
    } else {
      res.send(result1);
    }
  });
});


///////////////////PUT REQUEST/////////////////////

/**
*put a message in the messagedb and update the corressponding chat's message list
**/
router.post('/:receiverid', (req, res, next) => {
  const message = new Message({
    senderid: req.body.senderid,
    sender_name: req.body.sender_name,
    sender_colour: req.body.sender_colour,
    message: req.body.message
  });
  message.save()
         .then(Chat.update({usrID1: req.body.senderid, usrID2: req.params.recieverid},
           {$push: {messages : [{sender_name: req.body.sender_name, message: req.body.message}]}}))
         .then(Chat.update({usrID1: req.params.recieverid, usrID2: req.body.senderid},
           {$push: {messages : [{sender_name: req.body.sender_name, message: req.body.message}]}}))
          .then(Chat.update({usrID1: req.body.senderid, usrID2: req.params.recieverid},
           {lastmessage: req.body.message}))
          .then(Chat.update({usrID1: req.params.recieverid, usrID2: req.body.senderid},
           {lastmessage: req.body.message}))
          .then(result => {
           console.log(result);
           res.status(200).json({
             message: "POST to messagedb",
             createdMessage: result
           });
         })
         .catch(err => {
           console.log(err);
           res.status(500).json({
             error:err
           })
         });
});

/**
*add a chat to the chatsdb
**/
router.post('/:usrid1/:usrid2', (req, res, next) => {
  const usr1 = req.params.usrid1;
  const usr2 = req.params.usrid2;
  var chat = new Chat({
    usrID1: usr1,
    usrID2: usr2,
    messages: [{sender_name: "Tunnect", message: "Congrats: you've tunnected! Start a chat and say hi :)"}],
    lastmessage: "Congrats: you've tunnected! Start a chat and say hi :)"
  });
  chat.save()
         .then(result => {
           console.log(result);
           res.status(200).json({
             message: "POST to chatdb",
             createdMessage: result
           });
         })
         .catch(err => {
           console.log(err);
           res.status(500).json({
             error:err
           })
         });
});


module.exports = router;
