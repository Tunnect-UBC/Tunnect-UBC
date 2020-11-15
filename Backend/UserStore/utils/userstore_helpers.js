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
    } 
};
//};

module.exports = helpers;