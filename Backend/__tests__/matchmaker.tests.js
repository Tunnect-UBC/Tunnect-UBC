/*global jest*/

const supertest = require("supertest");
const app = require("../MatchmakingManager/app.js");
const request = supertest(app);

const axios = require("axios");

jest.mock("axios");

const helpers = require("../UserStore/utils/userstore_helpers");

describe("GET/:hostId endpoint", () => {
  it("Request ranked matches, where user1.song.length == 0, genre is the same for user1, and different for user2", async () => {

    //mock
    axios.get.mockResolvedValue({
      data: [
        {
          _id: "h75fvg",
          username: "nickham",
          favGenre: "Rock",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "765y",
          username: "user1",
          favGenre: "Rock",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "35trf",
          username: "user2",
          favGenre: "Pop",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        }
      ]
    });

    const res = await request.get("/matchmaker/h75fvg");

    expect(res.statusCode).toEqual(200);
    expect(JSON.parse(res.text)).toEqual([
      {
        _id: "765y",
        score: 3
      },
      {
        _id: "35trf",
        score: 0
      }
    ]);
  });
});




describe("GET/:hostId endpoint", () => {
  it("Request ranked matches, where user1.song.length != 0, genre is the same for user1, and different for user2", async () => {

    //mock
    axios.get.mockResolvedValue({
      data: [
        {
          _id: "h75fvg",
          username: "nickham",
          favGenre: "Rock",
          iconColour: "0",
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
        },
        {
          _id: "gsdfgsd",
          artist: "Mild Orange",
          name: "Smthign else",
          genre: "RnB",
          relatedArtists: [
              "Peach Pit",
              "Summer Salt",
              "Vacations"
          ]
        },
        {
          _id: "dfsfdf",
          artist: "Peach Pit",
          name: "Stranger",
          genre: "RnB",
          relatedArtists: [
              "Peach Pit",
              "Summer Salt",
              "Vacations"
          ]
        },
        {
          _id: "ssdffds",
          artist: "Samee",
          name: "Genre",
          genre: "Rock",
          relatedArtists: [
              "1",
              "2",
              "3"
          ]
        }],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "765y",
          username: "user1",
          favGenre: "Rock",
          iconColour: "0",
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
        {
          _id: "35trf",
          username: "user2",
          favGenre: "Pop",
          iconColour: "0",
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
        }
      ]
    })

    const res = await request.get("/matchmaker/h75fvg");

    expect(res.statusCode).toEqual(200);
    expect(JSON.parse(res.text)).toEqual([
      {
        _id: "765y",
        score: 6.791666666666667
      },
      {
        _id: "35trf",
        score: 3.5
      }
    ])
  });
});














describe("GET/:hostId endpoint", () => {
  it("Request ranked matches, where user1.song.length == 0, genre is the same for user1, and different for user2,", async () => {

    //mock
    axios.get.mockResolvedValue({
      data: [
        {
          _id: "h75fvg",
          username: "nickham",
          favGenre: "Rock",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "765y",
          username: "user1",
          favGenre: "Rock",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "35trf",
          username: "user2",
          favGenre: "Pop",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "45ygf",
          username: "user2",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "gdrfg",
          username: "user3",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "dfgd",
          username: "user4",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "45ygf",
          username: "user5",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },

        {
          _id: "dg",
          username: "user6",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "gdf",
          username: "user7",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "ftgy",
          username: "user8",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "fgdfgd",
          username: "user9",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "sddf",
          username: "user10",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "dsfsdf",
          username: "user11",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "sdfsfsdf",
          username: "user12",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "kugrfgf",
          username: "user13",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "8ujn",
          username: "user14",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "ngt5",
          username: "user15",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
        {
          _id: "cde3",
          username: "user16",
          favGenre: "Indie",
          iconColour: "0",
          songs: [],
          matches: [],
          likes: [],
          dislikes: []
        },
      ]
    })

    const res = await request.get("/matchmaker/h75fvg");

    expect(res.statusCode).toEqual(200);
    expect(JSON.parse(res.text)).toEqual([
      {
        _id: "765y",
        score: 3
      },
      {
        "_id": "35trf",
        "score": 0,
      },
      {
        "_id": "45ygf",
        "score": 0,
      },
      {
        "_id": "gdrfg",
        "score": 0,
     },
     {
        "_id": "dfgd",
        "score": 0,
     },
     {
        "_id": "45ygf",
        "score": 0,
     },
     {
        "_id": "dg",
        "score": 0,
     },
      {
        "_id": "gdf",
        "score": 0,
     },
     {
        "_id": "ftgy",
        "score": 0,
     },
     {
        "_id": "fgdfgd",
        "score": 0,
     },
     {
        "_id": "sddf",
        "score": 0,
     },
     {
        "_id": "dsfsdf",
        "score": 0,
     },
     {
        "_id": "sdfsfsdf",
        "score": 0,
     },
     {
        "_id": "kugrfgf",
        "score": 0,
     },
     {
        "_id": "8ujn",
        "score": 0,
     },

    ])
  });
});
