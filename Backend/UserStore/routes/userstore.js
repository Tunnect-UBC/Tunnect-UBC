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
const e = require("express");
const express = require("express");
const router = new express.Router();
const mongoose = require("mongoose");

const User = require("../../models/users");

const helpers = require("../utils/userstore_helpers");


/**
 * GET localhost:3000/userstore - Gets all users in the database
 *
 * If list of users is not empty, response is an array of json Users,
 * or a json error with status 500 on error.
 *
 * User schema described in ../../models/Users
 */
router.get("/", async (req, res, next) => {
    const users = await helpers.getAll();

    if (users[0] === 200) {
        res.status(200).json(users[1]);
    } else {
        res.status(500).json({
            error: users[1]
        });
    }
});


/**
 * GET localhost:3000/userstore/{id}/matches - Up to 20 users from the database,
 * such that none of them have been seen by id yet.
 *
 * If list of users is not empty, response is an array of json Users,
 * or a json error with status 500 on error.
 *
 * User schema described in ../../models/Users
 */
router.get("/:userId/matches", async (req, res, next) => {
    const userId = req.params.userId;
    const users = await helpers.get50(userId);

    res.status(users[0]).json(users[1]);
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
router.post("/", async (req, res, next) => {
    const user = new User({
      _id: req.body._id,
      username: req.body.username,
      iconColour: req.body.iconColour,
      notifId: req.body.notifId,
      favGenre: req.body.favGenre,
      songs: req.body.songs,
      matches: req.body.matches,
      likes: req.body.likes,
      dislikes: req.body.dislikes
    });
    const result = await helpers.postUser(user);

    if (result[0] === 200) {
        res.status(200).json(result[1]);
    } else {
        res.status(500).json({
            error: result[1]
        });
    }
});


/**
 * GET localhost:3000/userstore/{id} - Gets user with id from the database
 *
 * Response is a json User on success. If user can not be found, error status 404,
 * error status 500 for any other errors.
 *
 * User schema described in ../../models/Users
 */
router.get("/:userId", async (req, res, next) => {
    const userId = req.params.userId;

    const result = await helpers.getUser(userId);

    if (result[0] === 200) {
        res.status(200).json(result[1]);
    } else if (result[0] === 500) {
        res.status(500).json({
            error: result[1]
        });
    } else {
        res.status(404).json({
            error: result[1]
        });
    }
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
router.patch("/:userId", async (req, res, next) => {
    const userId = req.params.userId;
    const updateOps = {};

    for (const ops of req.body) {
        updateOps[ops.propName] = ops.value;
    }

    const result = await helpers.patchUser(userId, updateOps);

    if (result[0] === 200) {
        res.status(200).json(result[1]);
    } else if (result[0] === 500) {
        res.status(500).json({
            error: result[1]
        });
    } else {
        res.status(404).json({
            error: result[1]
        });
    }
});


/**
 * PATCH localhost:3000/userstore/{id}/addMatch/{id2} - Adds id2 to id's list of matches
 *
 */
router.patch("/:userId/addMatch/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;

    const notifId = req.body.notifId;
    const username = req.body.username;
    const result = await helpers.addStatus(userId, userId2, username, notifId, "matches");

    res.status(result[0]).json(result[1]);

});


/**
 * PATCH localhost:3000/userstore/{id}/removeMatch/{id2} - Removes id2 from id's list of matches
 *
 */

router.patch("/:userId/removeMatch/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;

    const result = await helpers.removeStatus(userId, userId2, "matches");

    res.status(result[0]).json(result[1]);
});

/**
 * PATCH localhost:3000/userstore/{id}/addLike/{id2} - Adds id2 to id's list of likes
 *
 */
router.patch("/:userId/addLike/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;
    const notifId = "";
    const username = "";

    const result = await helpers.addStatus(userId, userId2, username, notifId, "likes");

    res.status(result[0]).json(result[1]);
});


/**
 * PATCH localhost:3000/userstore/{id}/removeLike/{id2} - Removes id2 from id's list of likes
 *
 */
router.patch("/:userId/removeLike/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;


    const result = await helpers.removeStatus(userId, userId2, "likes");

    res.status(result[0]).json(result[1]);
});

/**
 * PATCH localhost:3000/userstore/{id}/addDislike/{id2} - Adds id2 to id's list of dislikes
 *
 */
router.patch("/:userId/addDislike/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;
    const username = "";
    const notifId = "";

    const result = await helpers.addStatus(userId, userId2, username, notifId, "dislikes");

    res.status(result[0]).json(result[1]);
});


/**
 * PATCH localhost:3000/userstore/{id}/removeDislike/{id2} - Removes id2 from id's list of dislikes
 *
 */
router.patch("/:userId/removeDislike/:userId2", async (req, res, next) => {
    const userId = req.params.userId;
    const userId2 = req.params.userId2;

    const result = await helpers.removeStatus(userId, userId2, "dislikes");

    res.status(result[0]).json(result[1]);
});


/**
 * DELETE localhost:3000/userstore/{id} - Deletes user with id from the database
 *
 * Response is a json result on success, json error with status 500 on error.
 *
 * User schema described in ../../models/Users
 */
router.delete("/:userId", async (req, res, next) => {
    const userId = req.params.userId;

    const result = await helpers.deleteUser(userId);


    if (result[0] === 200) {
        res.status(200).json(result[1]);
    } else if (result[0] === 500) {
        res.status(500).json({
            error: result[1]
        });
    } else {
        res.status(404).json({
            error: result[1]
        });
    }

});

module.exports = router;
