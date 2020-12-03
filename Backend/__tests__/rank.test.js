const helpers = require("../MatchmakingManager/utils/matchmakerHelpers");


const userScores = [
    {
        "_id": "12345",
        "score": 5
    },
    {
        "_id": "h1ya",
        "score": 7
    },
    {
        "_id": "b00ling",
        "score": 3
    },
    {
        "_id": "xA-12",
        "score": 0
    },
    {
        "_id": "76tgre",
        "score": 10
    },
];

const getScoreMock = jest.fn((user1, user2) => {
    const returnUser = userScores.find((obj) => (obj._id === user2._id));
    if (returnUser !== undefined) {
        return returnUser.score;
    } else {
        return undefined;
    }
});


const allUsers = [
    {
        "_id": "12345",
        "username": "numberguy"
    },
    {
        "_id": "h1ya",
        "username": "yes guy"
    },
    {
        "_id": "b00ling",
        "username": "booler123"
    },
    {
        "_id": "xA-12",
        "username": "elon"
    },
    {
        "_id": "76tgre",
        "username": "orge"
    },
];

describe("test that ranking works with mocked users", () => {
    it("calling rank", () => {

        helpers.getScore = getScoreMock;
        const rankings = helpers.rank(allUsers, "12345");


        expect(rankings).toEqual([
            {
                "_id": "76tgre",
                "score": 10
            },
            {
                "_id": "h1ya",
                "score": 7
            },
            {
                "_id": "b00ling",
                "score": 3
            },
            {
                "_id": "xA-12",
                "score": 0
            }
        ]);
    });
});
