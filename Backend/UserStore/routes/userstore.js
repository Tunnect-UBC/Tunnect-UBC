const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const User = require('../../models/users');


router.get('/', (req, res, next) => {
    User.find()
        .exec()
        .then(users => {
            console.log(users);
            if (users.length >= 0) {
                res.status(200).json(users);
            }
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            });
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
        .then(result => {
            console.log(result);
            res.status(200).json({
                message: "Handling POST requests to /userstore",
                createdUser: result
            });
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error:err
            })
        });
    
    
});


router.get('/:userId', (req, res, next) => {
    const id = req.params.userId;
    User.findById(id)
        .exec()
        .then(user => {
            console.log(user);
            if (user) {
                res.status(200).json(user);
            } else {
                res.status(404).json({messgae: 'No valid entry found for provided ID'});
            }
        })
        .catch(err => {
            console.log(err);
            res.statuts(500).json({error: err});
        });
});


router.patch('/:userId', (req, res, next) => {
    const id = req.params.userId;
    const updateOps = {};
    
    for (const ops of req.body) {
        updateOps[ops.propName] = ops.value;
    }

    User.update({ _id: id }, { $set: updateOps })
        .exec()
        .then(result => {
            console.log(res);
            res.status(200).json(result);
        })
        .catch(err => {
            console.log(err);
            res.status(500).json({
                error: err
            })
        })
});


router.delete('/:userId', (req, res, next) => {
    const id = req.params.userId;
    User.remove({
        _id: id
    })
        .exec()
        .then(result => {
            res.status(200).json(result);
        })
        .catch(err => {
            console.log(err);
            res.statuts(500).json({error: err});
        });
});

module.exports = router;