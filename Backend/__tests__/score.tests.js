const helpers = require("../MatchmakingManager/utils/matchmakerHelpers");
const users = require("../dictionaries/users");

describe("Test for score #1, users have no songs or top artist alike", () => {
    it("Calling score test #1", () => {
        //user1 and user3
        let score = helpers.getScore(users["user1"], users["user3"]);
        expect(score).toEqual(0);
    });
});


describe("Test for score #2, users have the same top artist but no songs alike", () => {
    it("calling score test #2", () => {
        //user1 and user5
        let score = helpers.getScore(users["user1"], users["user5"]);
        expect(score).toEqual(5);
    });
});


describe("Test for score #3, users have different top artists but some songs alike", () => {
    it("calling score test #3", () => {
        //user2 and users6
        let score = helpers.getScore(users["user2"], users["user6"]);
        expect(score).toEqual(2.5);
    });
});


describe("Test for score #4, users have same top artist and all same songs", () => {
    it("calling score test #4", () => {
        //users3 and user4
        let score = helpers.getScore(users["user3"], users["user4"]);
        expect(score).toEqual(10);  
    });
});