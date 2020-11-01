//This file handles all the API requests and different routes for the chat service
//Chat schema found in models/Chat
//Message schema found in models/Message
//status(200) indicates running smoothly :: status(404) connection error :: status(500) err handling


const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const axios = require('axios');


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
        res.status(500).json({
          error: err
        });
      } else {
        res.send(result);
      }
    });
  } else {
    Chat.find({usrID1: id}, 'usrID2 lastmessage', function(err, result1){
      if(err){
        res.status(500).json({
          error: err
        });
      }
      else if (!result1.length){
        Chat.find({usrID2: id}, 'usrID1 lastmessage', function (err, result2) {
          if(err){
            res.status(500).json({
              error: err
            });
          }
          else {
            res.send(result2);
          }
        });
      }
      else {
        res.send(result1);
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

  Chat.find({usrID1: id1, usrID2: id2}, 'messages', function (err, result1) {
    if(!result1.length) {
      Chat.find({usrID1: id2, usrID2: id1}, 'messages', function (err, result2) {
        if(err){
          res.status(500).json({
            error: err
          });
        }
        else {
          res.status(200).json(result2);
        }
      });
    }
    else if (err){
      res.status(500).json({
        error: err
      });
    }
    else {
      res.status(200).json(result1);
    }
   }
  );
});


/**
*put a message in the messagedb and update the corressponding chat's message list
**/
router.post('/:receiverid', (req, res, next) => {
  axios.get('http://localhost:3000/userstore/' + req.body.senderid, {params: {}})
  .then((response) => {
  const message = new Message({
    senderid: req.body.senderid,
    sender_name: response.data.username,
    sender_colour: response.data.icon_colour,
    message: req.body.message
  });
  console.log(response.data);
  message.save()
         .then(result => {
           console.log(result);
           res.status(200).json(result);
         })
         .then(result => {Chat.updateOne({usrID1: req.body.senderid, usrID2: req.params.receiverid},
           {$push: {messages : [{senderid: req.body.senderid, sender_name: response.data.username, message: req.body.message}]}}, function(err, result){})})
         .then(result => {Chat.updateOne({usrID1: req.params.receiverid, usrID2: req.body.senderid},
           {$push: {messages : [{senderid: req.body.senderid, sender_name: response.data.username, message: req.body.message}]}}, function(err, result){})})
          .then(result => {Chat.updateOne({usrID1: req.body.senderid, usrID2: req.params.receiverid},
           {$set: {lastmessage: req.body.message}}, function(err, result){})})
          .then(result => {Chat.updateOne({usrID1: req.params.receiverid, usrID2: req.body.senderid},
           {$set: {lastmessage: req.body.message}}, function(err, result){})})
           .catch(err => {
             res.status(500).json({
               error: err
             })
           });
      })
      .catch(err => {
        res.status(404).json({
          message: "User does not exist"
          });
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
    messages: [{senderid: "tunnect", sender_name: "Tunnect", message: "Congrats: you've tunnected! Start a chat and say hi :)"}],
    lastmessage: "Congrats: you've tunnected! Start a chat and say hi :)"
  });
  Chat.find({usrID1: usr1, usrID2: usr2}, function(err, result1){
    if(!result1.length){
      Chat.find({usrID1: usr2, usrID2: usr1}, function(err, result2){
        if(!result2.length){
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
        }
        else {
          console.log("chat already exists");
          res.status(200).json({
            message: "chat already exists"
          })
        }
      })
    } else {
      console.log("chat already exists");
      res.status(200).json({
        message: "chat already exists"
      })
    }
  });
});

/**
* Deletes a chat(but not all messages tied to the chat)
**/
router.delete('/:userId1/:userId2', (req, res, next) => {
    const id1 = req.params.userId1;
    const id2 = req.params.userId2;
    Chat.remove({
        usrID1: id1,
        usrID2: id2
    })
        .then(result => {
          if (!result.deletedCount){
            Chat.remove({
              usrID1: id2,
              usrID2: id1
            }).then(result => {
              res.status(200).json(result);
            })
          }
            res.status(200).json(result);
        })
        .catch(err => {
            console.log(err);
            res.statuts(500).json({error: err});
        });
});

module.exports = router;
