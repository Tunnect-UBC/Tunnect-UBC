/**
 * This file handles all the API requests and different routes for the userstore
 * 
 * The api supports:
 *      GET    localhost:3000/userstore - Gets all users in the database (line 26)
 *      POST   localhost:3000/userstore - Posts a new user to the database, with details passed in through body (line 57)
 *      GET    localhost:3000/userstore/{id} - Gets the user from the database with the specified id (line 93)
 *      PATCH  localhost:3000/userstore/{id} - Patches the user specified by id, and updates fields (line 134)
 *      DELETE localhost:3000/userstore/{id} - Deletes the user specified by id, from the database (line 164)
 */
const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const User = require('../../models/users');


/**
 * GET localhost:3000/userstore - Gets all users in the database
 * 
 * If list of users is not empty, response is an array of json Users,
 * or a json error with status 500 on error.
 * 
 * User schema described in ../../models/Users
 */
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


/**
 * POST localhost:3000/userstore - Posts a new user to the database
 * 
 * Gets all the values needed from the request body. A body may look like
 *      {
 *          "_id": "56y56gr",
 *          "username": "username123",
 *          "top_artist": "Lil Uzi"
 *      }
 * 
 * Response is json error on error with status 500
 * User schema described in ../../models/Users
 */
router.post('/', (req, res, next) => {
    //an example of how one might extract info about user from body
    const user = new User({
        _id: req.body._id,
        username: req.body.username,
        top_artist: req.body.top_artist,
        icon_colour: req.body.icon_colour,
        songs: req.body.songs
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


/**
 * GET localhost:3000/userstore/{id} - Gets user with id from the database
 * 
 * Response is a json User on success. If user can not be found, error status 404,
 * error status 500 for any other errors.
 * 
 * User schema described in ../../models/Users
 */
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


/**
 * PATCH localhost:3000/userstore/{id} - Patches user by id in the database.
 * 
 * NOTE: we can change as many or a few things in user as we like, but we cannot add new fields,
 * only modify existing ones
 * 
 * Gets all the values needed from the request body. Body is an array of objects, each with key/value pairs
 *      [
 *          {
 *              "propName": "username",
 *              "value": "lanceholland"
 *          },
 *          {
 *              "propName": "top_artist",
 *              "value": "Ruel"
 *          }
 * 
 *      ]
 * 
 * Response is json error on error with status 500
 * User schema described in ../../models/Users
 */
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


/**
 * DELETE localhost:3000/userstore/{id} - Deletes user with id from the database
 * 
 * Response is a json result on success, json error with status 500 on error.
 * 
 * User schema described in ../../models/Users
 */
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