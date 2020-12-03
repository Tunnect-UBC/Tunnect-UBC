const helpers = require("../MatchmakingManager/utils/matchmakerHelpers");

const userDictionary = require("../dictionaries/users");

describe("Test for songScore, song matches", () => {
    it("Calling songScore test #1", () => {
        let score = helpers.songScore({
            _id: "54htwg",
            artist: "Mild Orange",
            name: "Stranger",
            genre: "RnB",
            relatedArtists: [
                "Peach Pit",
                "Summer Salt",
                "Vacations"
            ]
        }, userDictionary.user1);
        expect(score).toEqual(3);
    });
});

describe("Test for songScore, artist matches", () => {
    it("Calling songScore test #2", () => {
        let score = helpers.songScore({
            _id: "654rd",
            artist: "Cage the Elephant",
            name: "Cigarette Daydreams",
            genre: "Rock",
            relatedArtists: [
                "The Black Keys",
                "Car Seat Headrest",
                "Radiohead"
            ]
        }, userDictionary.user1);
        expect(score).toEqual(2);
    });
});

describe("Test for songScore, related artist match", () => {
    it("Calling songScore test #3", () => {
        let score = helpers.songScore({
            _id: "4h5uih",
            artist: "Car Seat Headrest",
            name: "Bodies",
            genre: "Rock",
            relatedArtists: []
        }, userDictionary.user1);
        expect(score).toEqual(1);
    });
});

describe("Test for songScore, genre Match", () => {
    it("Calling songScore test #4", () => {
        let score = helpers.songScore({
            _id: "dtg89dgjf",
            artist: "ACDC",
            name: "Highway to hell",
            genre: "Rock",
            relatedArtists: [
                "Some",
                "Other",
                "Heavy Metals"
            ]
        }, userDictionary.user1);
        expect(score).toEqual(0.5);
    });
});

describe("Test for songScore, no match", () => {
    it("Calling songScore test #5", () => {
        let score = helpers.songScore({
            _id: "ghd78",
            artist: "Clairo",
            name: "Bags",
            genre: "Pop",
            relatedArtists: [
                "Cuco",
                "Billie",
                "Snail Mail"
            ]
        }, userDictionary.user1);
        expect(score).toEqual(0);
    });
});