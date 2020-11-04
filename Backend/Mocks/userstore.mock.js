const users = [
    {
        _id: "67guyh4i",
        username: "nickham",
        top_artist: "liluzi",
        icon_colour: "0xfff000",
        songs: [
            "Fuzzybrain",
            "Pretty Girl",
        ]
    },
    {
        _id: "9876yh",
        username: "david0",
        top_artist: "liluzi",
        icon_colour: "0xf0f0f0",
        songs: [
            "Heroes",
            "Pretty Girl",
            "Nice to have"
        ]
    },
    {
        _id: "5443erwf",
        username: "elises",
        top_artist: "pinkfloyd",
        icon_colour: "0xffffff",
        songs: [
            "Have a cigar",
            "Wish you were here"
        ]
    }
]

const userstoreMock = jest.fn(() => {
    return users;
});

module.exports = userstoreMock;


