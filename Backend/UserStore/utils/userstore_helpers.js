const User = require("../../models/users");
const {admin} = require("../../firebase-config");

const notifOpt = {
  priority: "high",
  timeToLive: 60 * 60 * 24
};

const helpers = {
    async getAll() {
        let allResp = [];
        await User.find()
            .exec()
            .then((users) => {

                //console.log(users);
                allResp = [200, users];
            })
            .catch((err) => {
                //console.log(err);
                allResp = [500, err];

            });

        return allResp;
    },

    async get50(hostId) {
        let g50Resp = [];

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
                                g50Resp = [200, users];
                            })
                            .catch((err) => {
                                g50Resp = [500, err];
                            });


                } else {
                    g50Resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                g50Resp = [500, err];
            });

        return g50Resp;
    },

    async postUser(user) {
        //stores this in the database
        let pUserResp = [];
        await user.save()
            .then((result) => {

                //console.log(result);
                pUserResp =  [200, result];
            })
            .catch((err) => {
                //console.log(err);
                pUserResp =  [500, err];
            });

        return pUserResp;
    },

    async getUser(userId) {
        let gUserResp = [];

        await User.findById(userId)
        .exec()
        .then((user) => {
            if (user) {

                gUserResp = [200, user];
                //res.status(200).json(user);
            } else {
                gUserResp = [404, {message: "No valid entry found for provided ID"}];
                //res.status(404).json({message: "No valid entry found for provided ID"});
            }
        })
        .catch((err) => {
            //console.log(err);
            gUserResp = [500, err];
            //res.status(500).json({error: err});
        });

        return gUserResp;
    },

    async patchUser(userId, updateOps) {
        let patResp = [];

        await User.updateOne({ _id: userId }, { $set: updateOps })
        .exec()
        .then((result) => {
            if (result.n > 0) {
                patResp = [200, result];
            }
            else {
                patResp = [404, {message: "No valid entry found for provided ID or propname"}];
            }
        })
        .catch((err) => {
            patResp = [500, err];
        });

        return patResp;
    },

    async deleteUser(userId) {
        let dResp = [];

        await User.deleteOne({
            _id: userId
        })
            .exec()
            .then((result) => {
                if (result.n > 0) {
                    dResp = [200, result];
                } else {
                    dResp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                dResp = [500, err];
            });

        return dResp;
    },

    async addStatus(userId1, userId2, username, notifId, status) {
        let aResp = [];
        var message = "Say hi to " + username + "!";
        const matchNotif = {
          notification: {
            title: "You've Tunnected",
            body: message
          }
        };
        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {
                    let userStatus = user[status];
                    userStatus.push(userId2);


                    if (status === "likes") {
                        await User.updateOne({_id: userId1 }, { $set : {likes: userStatus} })
                            .exec()
                            .then(async (result) => {
                                aResp = [200, result];
                            })
                            .catch((err) => {
                                aResp = [500, err];
                            });
                    } else if (status === "dislikes") {
                        await User.updateOne({_id: userId1 }, { $set : {dislikes: userStatus} })
                            .exec()
                            .then(async (result) => {
                                aResp = [200, result];
                            })
                            .catch((err) => {
                                aResp = [500, err];
                            });
                    } else {
                        await User.updateOne({_id: userId1 }, { $set : {matches: userStatus} })
                            .exec()
                            .then(async (result) => {
                              if(notifId !== "0"){
                                await admin.messaging().sendToDevice(notifId, matchNotif, notifOpt);
                              }
                              aResp = [200, result];
                            })
                            .catch((err) => {
                                aResp = [500, err];
                            });
                    }

                } else {
                    aResp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                aResp = [500, err];
            });
        return aResp;
    },

    async removeStatus(userId1, userId2, status) {
        let rResp = [];

        await User.findById(userId1)
            .exec()
            .then(async (user) => {
                if (user) {

                    if (status === "likes") {
                        await User.updateOne({_id: userId1 }, { $pull : {likes: userId2} })
                            .exec()
                            .then((result) => {
                                rResp = [200, result];
                            })
                            .catch((err) => {
                                rResp = [500, err];
                            });
                    } else if (status === "dislikes") {
                        await User.updateOne({_id: userId1 }, { $pull : {dislikes: userId2} })
                            .exec()
                            .then((result) => {
                                rResp = [200, result];
                            })
                            .catch((err) => {
                                rResp = [500, err];
                            });
                    } else {
                        await User.updateOne({_id: userId1 }, { $pull : {matches: userId2} })
                            .exec()
                            .then((result) => {
                                rResp = [200, result];
                            })
                            .catch((err) => {
                                rResp = [500, err];
                            });
                    }

                } else {
                    rResp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                rResp = [500, err];
            });

        return rResp;
    }
};

module.exports = helpers;
