const User = require("../../models/users");

const helpers = {
    async get_all() {
        let resp = [];
        await User.find()
            .exec()
            .then((users) => {
                //console.log(users);
                resp = [1, users];
            })
            .catch((err) => {
                //console.log(err);
                resp = [0, err];
            });

        return resp;
    },

    async post_user(user) {
        //stores this in the database
        let resp = [];
        
        await user.save()
            .then((result) => {
                //console.log(result);
                resp =  [1, result];
            })
            .catch((err) => {
                //console.log(err);
                resp =  [0, err];
            });

        return resp;
    },

    async get_user(userId) {
        let resp = [];

        await User.findById(userId)
        .exec()
        .then((user) => {
            //console.log(user);
            if (user) {
                resp = [1, user];
                //res.status(200).json(user);
            } else {
                resp = [-1, {message: "No valid entry found for provided ID"}];
                //res.status(404).json({message: "No valid entry found for provided ID"});
            }
        })
        .catch((err) => {
            //console.log(err);
            resp = [0, err];
            //res.status(500).json({error: err});
        });
        
        return resp;
    },

    async patch_user(userId, updateOps) {
        let resp = [];

        await User.updateOne({ _id: userId }, { $set: updateOps })
        .exec()
        .then((result) => {
            //console.log(res);
            if (result.n > 0) {
                resp = [1, result];
            }
            else {
                resp = [-1, {message: "No valid entry found for provided ID or propname"}];
            } 
            //res.status(200).json(result);
        })
        .catch((err) => {
            //console.log(err);
            resp = [0, err];
            //res.status(500).json({
            //    error: err
            //});
        });

        return resp;
    },

    async delete_user(userId) {
        let resp = [];
        
        await User.deleteOne({
            _id: userId
        })
            .exec()
            .then((result) => {
                if (result.n > 0) {
                    resp = [1, result];
                } else {
                    resp = [-1, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                //console.log(err);
                resp = [0, err];
            });

        return resp;
    },

    async addMatch(userId1, userId2) {
        let resp = [];
        
        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {
                    let userMatches = user.matches;
                    userMatches.push(userId2);

                    await User.updateOne({_id: userId1 }, { $set : {matches: userMatches} })
                                .exec()
                                .then((result) => {
                                    resp = [200, result];
                                })
                                .catch((err) => {
                                    resp = [500, err];
                                });

                } else {
                    resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                resp = [500, err];
            });

        console.log(resp);

        return resp;
    },

    async removeMatch(userId1, userId2) {
        let resp = [];

        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {
                    await User.updateOne({_id: userId1 }, { $pull : {matches: userId2} })
                                .exec()
                                .then((result) => {
                                    resp = [200, result];
                                })
                                .catch((err) => {
                                    resp = [500, err];
                                });

                } else {
                    resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                resp = [500, err];
            });

        console.log(resp);

        return resp;
    }
};

module.exports = helpers;