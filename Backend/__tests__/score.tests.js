const helpers = require("../MatchmakingManager/utils/matchmakerHelpers");

const userDictionary = require("../dictionaries/users");

describe("Test for score #1, user1.songs.length != 0, genreScore == 0", () => {
    it("Calling score test #1", () => {
        helpers.songScore = jest.fn((song, user2) => {
            return 0;
        });
        let score = helpers.getScore(userDictionary.user1, userDictionary.user2);
        expect(score).toEqual(0);
    });
});


describe("Test for score #2, user1.songs.length == 0, genreScore == 0", () => {
    it("Calling score test #2", () => {
        helpers.songScore = jest.fn((song, user2) => {
            return 0;
        });
        let score = helpers.getScore(userDictionary.user2, userDictionary.user1);
        expect(score).toEqual(0);
    });
});


describe("Test for score #3, user1.songs.length != 0, genreScore == 3", () => {
    it("Calling score test #3", () => {
        helpers.songScore = jest.fn((song, user2) => {
            if (song._id === user2.songs[0]._id) {
                return 3;
            } else {
                return 0;
            }
        });
        let score = helpers.getScore(userDictionary.user3, userDictionary.user1);
        expect(score).toEqual(10);
    });
});


describe("Test for score #4, user1.songs.length == 0, genreScore == 3", () => {
    it("Calling score test #4", () => {
        helpers.songScore = jest.fn((song, user2) => {
            return 0;
        });
        let score = helpers.getScore(userDictionary.user2, userDictionary.user4);
        expect(score).toEqual(3);
    });
});
