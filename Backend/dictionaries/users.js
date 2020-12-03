/**
 * This is a disctionary of hard coded users. These users are used in:
 *      - score.tests.js
 */

const userDictionary = {
    "user1": 
    {
        _id: "67guyh4i",
        username: "nickham",
        iconColour: "0xfff000",
        notifId: "67guyh4i",
        favGenre: "Rock",
        songs: [
            {
                _id: "54htwg",
                artist: "Mild Orange",
                name: "Stranger",
                genre: "RnB",
                relatedArtists: [
                    "Peach Pit",
                    "Summer Salt",
                    "Vacations"
                ]
            },
            {
                _id: "56y6hgf",
                artist: "Cage the Elephant",
                name: "Trouble",
                genre: "Rock",
                relatedArtists: [
                    "The Black Keys",
                    "Car Seat Headrest",
                    "Radiohead"
                ]
            }
        ],
        matches: [],
        likes: [],
        dislikes: []
    },
    "user2": 
    {
        _id: "fgujrtyn",
        username: "david",
        iconColour: "0xffffff",
        notifId: "fgujrtyn",
        favGenre: "Pop",
        songs: [],
        matches: [],
        likes: [],
        dislikes: []
    },
    "user3": 
    {
        _id: "sdffihjw345",
        username: "elise",
        iconColour: "0x000000",
        notifId: "sdffihjw345",
        favGenre: "Rock",
        songs: [{
            _id: "54htwg",
            artist: "Mild Orange",
            name: "Stranger",
            genre: "RnB",
            relatedArtists: [
                "Peach Pit",
                "Summer Salt",
                "Vacations"
            ]
        }],
        matches: [],
        likes: [],
        dislikes: []
    },
    "user4": 
    {
        _id: "hysdfg",
        username: "lance",
        iconColour: "0xffffff",
        notifId: "hysdfg",
        favGenre: "Pop",
        songs: [],
        matches: [],
        likes: [],
        dislikes: []
    },
}

const users = {
    "user1": 
        {
            _id: "67guyh4i",
            username: "nickham",
            topArtist: "liluzi",
            iconColour: "0xfff000",
            songs: [
                "Fuzzybrain",
                "Pretty Girl"
            ]
        },
    "user2": 
        {
            _id: "9876yh",
            username: "david0",
            topArtist: "liluzi",
            iconColour: "0xf0f0f0",
            songs: [
                "Heroes",
                "Pretty Girl",
                "Something yea",
                "Another song"
            ]
        },

    "user3": 
        {
            _id: "5443erwf",
            username: "elises",
            topArtist: "pinkfloyd",
            iconColour: "0xffffff",
            songs: [
                "Have a cigar",
                "Wish you were here"
            ]
        },

    "user4": 
        {
            _id: "ertete",
            username: "james",
            topArtist: "pinkfloyd",
            iconColour: "0xffffff",
            songs: [
                "Have a cigar",
                "Wish you were here"
            ]
        },

    "user5": 
        {
            _id: "hgt54ef",
            username: "rapper",
            topArtist: "liluzi",
            iconColour: "0xfff000",
            songs: [
                "Nice for what",
                "Molly"
            ]
        },

    "user6": 
        {
            _id: "hgfcbht54e",
            username: "drakefan11",
            topArtist: "drake",
            iconColour: "0xf0f0f0",
            songs: [
                "Heroes",
                "Pretty Girl",
                "Nice to have"
            ]
        }
};

module.exports = userDictionary;