const helpers = {
    getScore: function(user1, user2) {
        let score = 0;
        //console.log(user2.top_artist);

        user1.songs.forEach(song => {
            if (user2.songs.includes(song)) {
                score++;
            }
        })

	if (user1.songs.length != 0) {    
            score = score / user1.songs.length;
	}

        if (user1.top_artist == user2.top_artist) {
            score++
        }

        return 5 * score;
    },

    /**
     * VERY IMPORTANT
     * 
     * this function requires that the host is present in the allUsers list. This is
     * the case for the basic implementation of of matchmaker where all users in db will be selected.
     * This will be different tho if we get more users and start to only select a subset of them...
     */
    rank: function(allUsers, hostId) {
        const users = allUsers.filter(user => user._id != hostId);
        const host = allUsers.find(user => user._id == hostId);
    
        console.log(users);

        let usersScore = [];

        //this sets up the array of objects, with their corresponding score
        users.forEach(user => {
            //console.log(user);
            usersScore.push({
                user: user.username,
                score: this.getScore(host, user)
            });
        })

        //sorts the array by score
        usersScore.sort((a, b) => b.score - a.score);
        return usersScore;
    }
}

module.exports = helpers;
