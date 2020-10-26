/**
 * This file handles all the API requests and different routes for the userstore
 * 
 * The api supports:
 *      GET    localhost:3001/matchmaker - Gets all users in the database (line 26)
 */
const http = require('http'); 
const express = require('express');
const router = express.Router();
const helpers = require('../utils/matchmakerHelpers');

const userStoreUrl = 'http:/localhost:3000/userstore';


/**
 * GET localhost:3001/ - Gets all users in the database
 * 
 * If list of users is not empty, response is an array of json Users,
 * or a json error with status 500 on error.
 * 
 * User schema described in ../../models/Users
 */
router.get('/', (req, res, next) => {
    //hostId is the id of the user who is looking for a match
    const hostId = req.body.hostId;

    //this is to get the list of all users, such that we can rank them
    http.get(userStoreUrl, resp => {
        let data = "";

        //A chunk of data has been received
        resp.on("data", chunk => {
            data += chunk;
        })

        //The whole response has been received. Print out the result.
        resp.on("end", () => {
            //need to call helper function to calculate rank users, based on score
            const jsonRankings = helpers.rank(JSON.parse(data), hostId);
            console.log(jsonRankings);
            res.status(200).json(jsonRankings);
        })
    })
        .on("error", err=> {
            console.log("Error: " + err.message);
            res.status(500).json({
                error: err
            });
        })

});

module.exports = router;