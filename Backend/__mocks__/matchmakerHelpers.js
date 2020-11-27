const getScore = jest.fn((user1, user2) => {
    console.log("We out here in mock !");
    return userScores.find(o => o._id === user2);
});

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

module.export = getScore;