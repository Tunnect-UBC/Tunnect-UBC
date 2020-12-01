//const helpers = {
//const helpers = require('./matchmakingHelpers.js');

//module.exports = {
const helpers = {
    songScore(user1song, user2) {

        let artistFlag = 0;
        let relatedFlag = 0;
        let genreFlag = 0;

        for (let i = 0; i < user2.songs.length; i++) {
            if (user2.songs[i]._id === user1song._id) {
                return 3;
            } else if (user2.songs[i].artist === user1song.artist) {
                artistFlag = 1;
            } else if (user2.songs[i].relatedArtists.includes(user1song.artist)) {
                relatedFlag = 1;
            } else if (user1song.genre === user2.favGenre) {
                genreFlag = 1;
            }
        }

        if (artistFlag) {
            return 2;
        } else if (relatedFlag) {
            return 1;
        } else if (genreFlag) {
            return 0.5;
        } else {
            return 0;
        }
    },

    getScore(user1, user2) {
        let score = 0;

        user1.songs.forEach((song) => {
            score += helpers.songScore(song, user2);
        });

        if (user1.songs.length !== 0) {
            score = score / user1.songs.length;
        }

        const songsScore = score * (7/3);
        var genreScore = 0;
        if (user1.favGenre === user2.favGenre) {
            genreScore = 3;
        }

        return songsScore + genreScore;
    },

        /**
         * VERY IMPORTANT
         *
         * this function requires that the host is present in the allUsers list. This is
         * the case for the basic implementation of of matchmaker where all users in db will be selected.
         * This will be different tho if we get more users and start to only select a subset of them...
         */
    rank(allUsers, hostId) {
        const users = allUsers.filter((user) => user._id !== hostId);
        const host = allUsers.find((user) => user._id === hostId);

        let usersScore = [];

        //this sets up the array of objects, with their corresponding score
        users.forEach((user) => {
            usersScore.push({
                _id: user._id,
                score: helpers.getScore(host, user)
            });
        });

        //sorts the array by score
        usersScore.sort((a, b) => b.score - a.score);
        return usersScore;
    }
};

module.exports = helpers;
