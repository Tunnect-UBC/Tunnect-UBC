const supertest = require("supertest");
const app = require("../UserStore/app.js");
const request = supertest(app);

const helpers = require("../UserStore/utils/userstore_helpers");


describe("GET/: endpoint", () => {
    it("Request to get all users, error", async () => {
        helpers.get_all = jest.fn().mockReturnValueOnce([500, undefined]);
                            
        const res = await request.get("/userstore");
        
        
        expect(res.statusCode).toEqual(500);
    });
});


describe("GET/: endpoint", () => {
    it("Request to get all users, success", async () => {
        helpers.get_all = jest.fn().mockReturnValueOnce([200, {
                "_id": "n765rtg",
                "favGenre": "Pop"
        }]);

        const res = await request.get("/userstore");
        
        expect(res.statusCode).toEqual(200);
    });
});


describe("GET/: userId", () => {
    it("Request to get one users, database failure", async () => {
        helpers.get_user = jest.fn().mockReturnValueOnce([500, undefined]);

        const res = await request.get("/userstore/123");
        
        expect(res.statusCode).toEqual(500);
    });
});

describe("GET/: userId", () => {
    it("Request to get one users, user not found", async () => {
        helpers.get_user = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);

        const res = await request.get("/userstore/123");
        
        expect(res.statusCode).toEqual(404);
    });
});

describe("GET/: userId", () => {
    it("Request to get one users, user not found", async () => {
        helpers.get_user = jest.fn().mockReturnValueOnce([200, {
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
        helpers.post_user = jest.fn().mockReturnValueOnce([500, {
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
        helpers.post_user = jest.fn().mockReturnValueOnce([200, {
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


describe("GET/:userId/matches", () => {
    it("Request to get up to 50 users, including self, user not found", async () => {
        helpers.get_50 = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);

        const res = await request.get("/userstore/123/matches");
        
        expect(res.statusCode).toEqual(404);
    });
});

describe("GET/:userId/matches", () => {
    it("Request to get up to 50 users, including self, database failure", async () => {
        helpers.get_50 = jest.fn().mockReturnValueOnce([500, {error: "Invalid Query"}]);

        const res = await request.get("/userstore/123/matches");
        
        expect(res.statusCode).toEqual(500);
    });
});

describe("GET/:userId/matches", () => {
    it("Request to get up to 50 users, including self, success", async () => {
        helpers.get_50 = jest.fn().mockReturnValueOnce([200, [
            {
                _id: "123",
                username: "nickham",
                favGenre: "Rock",
                iconColour: "0",
                songs: [],
                matches: [],
                likes: [],
                dislikes: []
            },
            {
                _id: "456",
                username: "testuser2",
                favGenre: "Pop",
                iconColour: "0xffffff",
                songs: [
                    "Test Drive"
                ],
                matches: [],
                likes: [],
                dislikes: []
            }
        ]]);

        const res = await request.get("/userstore/123/matches");
        
        expect(res.statusCode).toEqual(200);
    });


    describe("PATCH/:userId", () => {
        it("Request to patch user, user not found", async () => {
            helpers.patch_user = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);
    
            const res = await request.patch("/userstore/123").send([{
                "propName": "username",
                "value": "Lance Holland"
            }]);
            
            expect(res.statusCode).toEqual(404);
        });
    });

    describe("PATCH/:userId", () => {
        it("Request to patch user, database error", async () => {
            helpers.patch_user = jest.fn().mockReturnValueOnce([500, {error: "Invalid Query"}]);
    
            const res = await request.patch("/userstore/123").send([{
                "propName": "username",
                "value": "Lance Holland"
            }]);
            
            expect(res.statusCode).toEqual(500);
        });
    });

    describe("PATCH/:userId", () => {
        it("Request to patch user, success", async () => {
            helpers.patch_user = jest.fn().mockReturnValueOnce([200, {nModified: "1"}]);
    
            const res = await request.patch("/userstore/123").send([{
                "propName": "username",
                "value": "Lance Holland"
            }]);
            
            expect(res.statusCode).toEqual(200);
        });
    });

    describe("PATCH/:userId/addMatch/:userId2", () => {
        it("Request to add user2 to user1's list of matches, success", async () => {
            helpers.addStatus = jest.fn().mockReturnValueOnce([200, {nModified: "1"}]);
    
            const res = await request.patch("/userstore/123/addMatch/456");
            
            expect(res.statusCode).toEqual(200);
        });
    });

    describe("PATCH/:userId/addMatch/:userId2", () => {
        it("Request to remove user2 to user1's list of matches, user not found", async () => {
            helpers.removeStatus = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);
    
            const res = await request.patch("/userstore/123/removeMatch/456");
            
            expect(res.statusCode).toEqual(404);
        });
    });

    describe("PATCH/:userId/addLike/:userId2", () => {
        it("Request to add user2 to user1's list of likes, success", async () => {
            helpers.addStatus = jest.fn().mockReturnValueOnce([200, {nModified: "1"}]);
    
            const res = await request.patch("/userstore/123/addLike/456");
            
            expect(res.statusCode).toEqual(200);
        });
    });

    describe("PATCH/:userId/removeLike/:userId2", () => {
        it("Request to remove user2 to user1's list of likes, database error", async () => {
            helpers.removeStatus = jest.fn().mockReturnValueOnce([500, {error: "Invalid Query"}]);
    
            const res = await request.patch("/userstore/123/removeLike/456");
            
            expect(res.statusCode).toEqual(500);
        });
    });

    describe("PATCH/:userId/addDislike/:userId2", () => {
        it("Request to add user2 to user1's list of likes, success", async () => {
            helpers.addStatus = jest.fn().mockReturnValueOnce([200, {nModified: "1"}]);
    
            const res = await request.patch("/userstore/123/addDislike/456");
            
            expect(res.statusCode).toEqual(200);
        });
    });

    describe("PATCH/:userId/removeDislike/:userId2", () => {
        it("Request to remove user2 to user1's list of likes, database error", async () => {
            helpers.removeStatus = jest.fn().mockReturnValueOnce([500, {error: "Invalid Query"}]);
    
            const res = await request.patch("/userstore/123/removeDislike/456");
            
            expect(res.statusCode).toEqual(500);
        });
    });

    describe("DELETE/:userId", () => {
        it("Request to delete user by userId, success", async () => {
            helpers.delete_user = jest.fn().mockReturnValueOnce([200, {nModified: "1"}]);
    
            const res = await request.delete("/userstore/123");
            
            expect(res.statusCode).toEqual(200);
        });
    });

    describe("DELETE/:userId", () => {
        it("Request to delete user by userId, database failure", async () => {
            helpers.delete_user = jest.fn().mockReturnValueOnce([500, {error: "Invalid Query"}]);
    
            const res = await request.delete("/userstore/123");
            
            expect(res.statusCode).toEqual(500);
        });
    });

    describe("DELETE/:userId", () => {
        it("Request to delete user by userId, user not found", async () => {
            helpers.delete_user = jest.fn().mockReturnValueOnce([404, {message: "No valid entry found for provided ID"}]);
    
            const res = await request.delete("/userstore/123");
            
            expect(res.statusCode).toEqual(404);
        });
    });
});

