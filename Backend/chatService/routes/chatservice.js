//This file handles all the API requests and different routes for the chat service
//Chat schema found in models/Chat
//Message schema found in models/Message
//status(200) indicates running smoothly :: status(404) connection error :: status(500) err handling


const express = require("express");
const router = new express.Router();
const mongoose = require("mongoose");
const axios = require("axios");


const Chat = require("../../models/chat");

/////////////////////GET requests//////////////////////
//syntax reminder::router.get('/' -targets requests aimed at /products

/**
*GET request for a list of available chats for a user
**/
router.get("/:userId", (req, res, next) => {
  const id = req.params.userId;
  if (id === "all"){
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
    Chat.find({usrID1: id}, "usrID2 usrColour2 usrName2 lastMessage lastTime", function(err, result1){
      if(err){
        res.status(500).json({
          error: err
        });
      }
      else if (!result1.length){
        Chat.find({usrID2: id}, "usrID1 usrColour1 usrName1 lastMessage lastTime", function (err, result2) {
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

router.get("/:userid1/:userid2", (req, res, next) => {
   const id1 = req.params.userid1;
   const id2 = req.params.userid2;

  Chat.find({usrID1: id1, usrID2: id2}, "messages", function (err, result1) {
    if(!result1.length) {
      Chat.find({usrID1: id2, usrID2: id1}, "messages", function (err, result2) {
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
router.post("/:receiverid", (req, res, next) => {
  Chat.updateOne({usrID1: req.body.senderid, usrID2: req.params.receiverid},
           {$push: {messages : [{senderid: req.body.senderid, message: req.body.message, timeStamp: req.body.timeStamp}]},
           $set: {lastMessage: req.body.message, lastTime: req.body.timeStamp}})
           .then((result) => {
            Chat.updateOne({usrID1: req.params.receiverid, usrID2: req.body.senderid},
           {$push: {messages : [{senderid: req.body.senderid, message: req.body.message, timeStamp: req.body.timeStamp}]},
           $set: {lastMessage: req.body.message, lastTime: req.body.timeStamp}}, function(err, result){});})
          .then((result) => {
            res.status(200).json({});
          })
          .catch((err) => {
             res.status(500).json({
               error: err
             });
           });
  });

/**
*add a chat to the chatsdb
**/
router.post("/:usrid1/:usrid2", (req, res, next) => {
  axios.get("http://localhost:3000/userstore/" + req.params.usrid1, {params: {}})
  .then((response) => {
  const usr1 = req.params.usrid1;
  const usr2 = req.params.usrid2;
  var usr1name = response.data.username;
  var usr1colour = response.data.icon_colour;

 axios.get("http://localhost:3000/userstore/" + req.params.usrid2, {params: {}})
 .then((response2) => {
  var chat = new Chat({
    usrID1: usr1,
    usrColour1: usr1colour,
    usrName1: usr1name,
    usrID2: usr2,
    usrColour2: response2.data.icon_colour,
    usrName2: response2.data.username,
    messages: [{senderid: "tunnect", message: "Congrats: you've tunnected! Start a chat and say hi :)", timeStamp: req.body.timeStamp}],
    lastMessage: "Congrats: you've tunnected! Start a chat and say hi :)",
    lastTime: req.body.timeStamp
  });
  Chat.find({usrID1: usr1, usrID2: usr2}, function(err, result1){
    if(!result1.length){
      Chat.find({usrID1: usr2, usrID2: usr1}, function(err, result2){
        if(!result2.length){
          chat.save()
                 .then((result) => {
                   res.status(200).json({
                     message: "POST to chatdb",
                     createdMessage: result
                   });
                 })
                 .catch((err) => {
                   //console.log(err);
                   res.status(500).json({
                     error:err
                   });
                 });
        }
        else {
          //console.log("chat already exists");
          res.status(200).json({
            message: "chat already exists"
          });
        }
      });
    } else {
      //console.log("chat already exists");
      res.status(200).json({
        message: "chat already exists"
      });
    }
  });
 })
 .catch((err) => {
   res.status(404).json({
     message: "User2 does not exist"
     });
 });
});
});

/**
* Deletes a chat(but not all messages tied to the chat)
**/
router.delete("/:userId1/:userId2", (req, res, next) => {
    const id1 = req.params.userId1;
    const id2 = req.params.userId2;
    Chat.remove({
        usrID1: id1,
        usrID2: id2
    })
        .then((result) => {
          if (!result.deletedCount){
            Chat.remove({
              usrID1: id2,
              usrID2: id1
            }).then((result) => {
              res.status(200).json(result);
            })
          }
            res.status(200).json(result);
        })
        .catch((err) => {
            //console.log(err);
            res.statuts(500).json({error: err});
        });
});

module.exports = router;
