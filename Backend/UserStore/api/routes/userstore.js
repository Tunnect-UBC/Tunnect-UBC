const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const User = require('../models/users');


router.get('/', (req, res, next) => {
    res.status(200).json({
        message: "Handling GET requests to /userstore"
    });
});


router.post('/', (req, res, next) => {
    //an example of how one might extract info about user from body
    const user = new User({
        _id: req.body._id,
        username: req.body.username,
        top_artist: req.body.top_artist
    });
    
    //stores this in the database
    user.save()
        .then(result => console.log(result))
        .catch(err => console.log(err));
    
    res.status(200).json({
        message: "Handling POST requests to /userstore",
        user: user
    });
});


router.get('/:userId', (req, res, next) => {
    const id = req.params.userId;
    User.findById(id)
        .exec()
        .then(user => {
            console.log(user);
            res.status(200).json(user);
        })
        .catch(err => {
            console.log(err);
            res.statuts(500).json({error: err});
        });
});


router.put('/:userId', (req, res, next) => {
    res.status(200).json({
        message: 'This is a put request to ' + req.params.userId
    });
});


router.delete('/:userId', (req, res, next) => {
    res.status(200).json({
        message: 'This is a request to delete ' + req.params.userId
    });
});

module.exports = router;