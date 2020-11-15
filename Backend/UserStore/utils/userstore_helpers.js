const helpers = {
    async get_all() {
        console.log("THIS IS REAL GET ALL");
        
        User.find()
            .exec()
            .then((users) => {
                //console.log(users);
                if (users.length >= 0) {
                    return users;
                }
            })
            .catch((err) => {
                //console.log(err);
                return undefined;
            });
    },

    async post_user(user) {
        //stores this in the database
        user.save()
        .then((result) => {
            //console.log(result);
            return 1;
        })
        .catch((err) => {
            //console.log(err);
            return 0;
        });

    }
};
//};

module.exports = helpers;