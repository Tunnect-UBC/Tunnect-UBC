const jest = require("jest-mock");

const users = [
    {
        _id: "67guyh4i",
        username: "nickham",
        topArtist: "liluzi",
        iconColour: "0xfff000",
        songs: [
            "Fuzzybrain",
            "Pretty Girl",
        ]
    },
    {
        _id: "9876yh",
        username: "david0",
        topArtist: "liluzi",
        iconColour: "0xf0f0f0",
        songs: [
            "Heroes",
            "Pretty Girl",
            "Nice to have"
        ]
    },
    {
        _id: "5443erwf",
        username: "elises",
        topArtist: "pinkfloyd",
        iconColour: "0xffffff",
        songs: [
            "Have a cigar",
            "Wish you were here"
        ]
    }
];

const userstoreMock = jest.fn(() => {
    return users;
});

module.exports = userstoreMock;


