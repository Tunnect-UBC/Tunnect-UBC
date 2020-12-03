/**
 * This file handles all the API requests and different routes for the userstore
 *
 * The api supports:
 *      GET    localhost:3001/matchmaker - Gets all users in the database (line 26)
 */
const http = require("http");
const express = require("express");
const router = new express.Router();
const helpers = require("../utils/matchmakerHelpers");

const axios = require(axios");

//const userstoreMock = require("../../Mocks/userstore.mock");

const userStoreUrl = "http://localhost:3000/userstore/";


/**
 * GET localhost:3001/ - Gets all users in the database
 *
 * If list of users is not empty, response is an array of json Users,
 * or a json error with status 500 on error.
 *
 * User schema described in ../../models/Users
 */
router.get("/:hostId", async (req, res, next) => {
    //hostId is the id of the user who is looking for a match
    const hostId = req.params.hostId;

    await axios.get(userStoreUrl + hostId + "/matches", {params: {}})
            .then(async (response) => {
                //const jsonRankings = helpers.rank(JSON.parse(response.data), hostId);
                const jsonRankings = helpers.rank(response.data, hostId);
                let filteredRankings;
                if (jsonRankings.length > 15) {
                    filteredRankings = jsonRankings.slice(0, 15);
                } else {
                    filteredRankings = jsonRankings;
                }
                res.status(200).json(filteredRankings);
            })
            .catch(async (error) => {
                res.status(500).json({
                    error
                });
            });




    //this is to get the list of all users, such that we can rank them
    /*http.get(userStoreUrl + hostId + "/matches", (resp) => {
        let data = "";

        //A chunk of data has been received
        resp.on("data", (chunk) => {
            data += chunk;
        });

        //The whole response has been received. Print out the result.
        resp.on("end", () => {
        //need to call helper function to calculate rank users, based on score
            //if (JSON.parse(data).find((user) => user._id === hostId)) {
            const jsonRankings = helpers.rank(JSON.parse(data), hostId);
            let filteredRankings;
            if (jsonRankings.length > 15) {
                filteredRankings = jsonRankings.slice(0, 15);
            } else {
                filteredRankings = jsonRankings;
            }
            res.status(200).json(filteredRankings);
        });
    })
        .on("error", (err) => {
            res.status(500).json({
                error: err
            });
        });
*/
});

module.exports = router;
