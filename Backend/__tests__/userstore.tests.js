const supertest = require("supertest");
const app = require("../UserStore/app.js");
const request = supertest(app);

const helpers = require("../UserStore/utils/userstore_helpers");


describe("GET/: endpoint", () => {
    it("Request to get all users, error", async () => {
        helpers.getAll = jest.fn().mockReturnValueOnce([500, undefined]);

        const res = await request.get("/userstore");


        expect(res.statusCode).toEqual(500);
    });
});


describe("GET/: endpoint", () => {
    it("Request to get all users, success", async () => {
        helpers.getAll = jest.fn().mockReturnValueOnce([200, {
                "_id": "n765rtg",
                "favGenre": "Pop"
        }]);

        const res = await request.get("/userstore");

        expect(res.statusCode).toEqual(200);
    });
});


describe("GET/: userId", () => {
    it("Request to get one users, database failure", async () => {
        helpers.getUser = jest.fn().mockReturnValueOnce([500, undefined]);

        const res = await request.get("/userstore/123");

        expect(res.statusCode).toEqual(500);
    });
});

describe("GET/: userId", () => {
    it("Request to get one users, user not found", async () => {
        helpers.getUser = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);

        const res = await request.get("/userstore/123");

        expect(res.statusCode).toEqual(404);
    });
});

describe("GET/: userId", () => {
    it("Request to get one users, user not found", async () => {
        helpers.getUser = jest.fn().mockReturnValueOnce([200, {
            _id: "123",
            username: "nickham",
            favGenre: "Rock",
            iconColour: "0",
            songs: [],
            matches: [],
            likes: [],
            dislikes: []
        }]);

        const res = await request.get("/userstore/123");

        expect(res.statusCode).toEqual(200);
    });
});


describe("POST/: endpoint", () => {
    it("Request to post user to database, error", async () => {
        helpers.postUser = jest.fn().mockReturnValueOnce([500, {
            error: "serverError"
        }]);

        const res = await request.post("/userstore").send({
            _id: "h75fvg",
            username: "nickham",
            favGenre: "Rock",
            iconColour: "0",
            songs: [],
            matches: [],
            likes: [],
            dislikes: []
        });


        expect(res.statusCode).toEqual(500);
    });
});


describe("POST/: endpoint", () => {
    it("Request to post user to database, success", async () => {
        helpers.postUser = jest.fn().mockReturnValueOnce([200, {
            status: "accepted"
        }]);

        const res = await request.post("/userstore").send({
            _id: "h75fvg",
            username: "nickham",
            favGenre: "Indie Pop",
            iconColour: "0xffffff",
            songs: [],
            matches: [],
            likes: [],
            dislikes: []
        });


        expect(res.statusCode).toEqual(200);
    });
});
