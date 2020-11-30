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

    async get_50(hostId) {
        let resp = [];

        await User.findById(hostId)
            .exec()
            .then(async (user) => {
                if (user) {

                    await User.find( { $and: [
                                                { _id: { $nin: user.matches}},
                                                { _id: { $nin: user.likes}},
                                                { _id: { $nin: user.dislikes}},
                                                { _id: { $ne: user._id }}
                                            ]})
                            .limit(50)
                            .exec()
                            .then((users) => {
                                users.push(user);
                                resp = [200, users];
                            })
                            .catch((err) => {
                                //console.log(err);
                                resp = [500, err];
                            });


                } else {
                    resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                resp = [500, err];
            });

        //console.log(resp);

        return resp;
    },

    async post_user(user) {
        //stores this in the database
        let resp = [];

        await user.save()
            .then((result) => {
                console.log(result);
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

    async addStatus(userId1, userId2, status) {
        let resp = [];

        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {
                    let userStatus = user[status];
                    userStatus.push(userId2);


                    if (status === "likes") {
                        await User.updateOne({_id: userId1 }, { $set : {likes: userStatus} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else if (status === "dislikes") {
                        await User.updateOne({_id: userId1 }, { $set : {dislikes: userStatus} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else {
                        await User.updateOne({_id: userId1 }, { $set : {matches: userStatus} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    }

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

    async removeStatus(userId1, userId2, status) {
        let resp = [];

        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {

                    if (status === "likes") {
                        await User.updateOne({_id: userId1 }, { $pull : {likes: userId2} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else if (status === "dislikes") {
                        await User.updateOne({_id: userId1 }, { $pull : {dislikes: userId2} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else {
                        await User.updateOne({_id: userId1 }, { $pull : {matches: userId2} })
                            .exec()
                            .then((result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    }

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
