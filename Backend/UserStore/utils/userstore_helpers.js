const User = require("../../models/users");
const {admin} = require("../../firebase-config");

const notifOpt = {
  priority: "high",
  timeToLive: 60 * 60 * 24
};

const helpers = {
    async getAll() {
        let resp = [];
        await User.find()
            .exec()
            .then((users) => {

                //console.log(users);
                resp = [200, users];
            })
            .catch((err) => {
                //console.log(err);
                resp = [500, err];

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
                                resp = [500, err];
                            });


                } else {
                    resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                resp = [500, err];
            });

        return resp;
    },

    async postUser(user) {
        //stores this in the database
        let resp = [];
        await user.save()
            .then((result) => {

                //console.log(result);
                resp =  [200, result];
            })
            .catch((err) => {
                //console.log(err);
                resp =  [500, err];
            });

        return resp;
    },

    async getUser(userId) {
        let resp = [];

        await User.findById(userId)
        .exec()
        .then((user) => {
            if (user) {

                resp = [200, user];
                //res.status(200).json(user);
            } else {
                resp = [404, {message: "No valid entry found for provided ID"}];
                //res.status(404).json({message: "No valid entry found for provided ID"});
            }
        })
        .catch((err) => {
            //console.log(err);
            resp = [500, err];
            //res.status(500).json({error: err});
        });

        return resp;
    },

    async patchUser(userId, updateOps) {
        let resp = [];

        await User.updateOne({ _id: userId }, { $set: updateOps })
        .exec()
        .then((result) => {
            if (result.n > 0) {
                resp = [200, result];
            }
            else {
                resp = [404, {message: "No valid entry found for provided ID or propname"}];
            }
        })
        .catch((err) => {
            resp = [500, err];
        });

        return resp;
    },

    async deleteUser(userId) {
        let resp = [];

        await User.deleteOne({
            _id: userId
        })
            .exec()
            .then((result) => {
                if (result.n > 0) {
                    resp = [200, result];
                } else {
                    resp = [404, {message: "No valid entry found for provided ID"}];
                }
            })
            .catch((err) => {
                resp = [500, err];
            });

        return resp;
    },

    async addStatus(userId1, userId2, username, notifId, status) {
        let resp = [];
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
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else if (status === "dislikes") {
                        await User.updateOne({_id: userId1 }, { $set : {dislikes: userStatus} })
                            .exec()
                            .then(async (result) => {
                                resp = [200, result];
                            })
                            .catch((err) => {
                                resp = [500, err];
                            });
                    } else {
                        await User.updateOne({_id: userId1 }, { $set : {matches: userStatus} })
                            .exec()
                            .then(async (result) => {
                              if(notifId !== "0"){
                                await admin.messaging().sendToDevice(notifId, matchNotif, notifOpt);
                              }
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
