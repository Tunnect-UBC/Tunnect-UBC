const supertest = require("supertest");
const app = require("../UserStore/app.js");
const request = supertest(app);

const helpers = require("../UserStore/utils/userstore_helpers");


describe("GET/: endpoint", () => {
    it("Request to get all users, error", async () => {
        helpers.get_all = jest.fn().mockReturnValueOnce(undefined);
                            
        const res = await request.get("/userstore");
        
        
        expect(res.statusCode).toEqual(500);
    });
});


describe("GET/: endpoint", () => {
    it("Request to get all users, success", async () => {
        helpers.get_all = jest.fn().mockReturnValueOnce({
                                       "_id": "n765rtg",
                                       "topArtist": "kanye"
                                   });

        const res = await request.get("/userstore");
        
        expect(res.statusCode).toEqual(200);
    });
});


describe("POST/: endpoint", () => {
    it("Request to post user to database, error", async () => {
        helpers.post_user = jest.fn().mockReturnValueOnce(0);
                            
        const res = await request.post("/userstore").send({
            _id: "h75fvg",
            username: "nickham",
            topArtist: "uzi",
            iconColour: "0xffffff",
            songs: [],
            matches: []
        });
        
        
        expect(res.statusCode).toEqual(500);
    });
});


describe("POST/: endpoint", () => {
    it("Request to post user to database, success", async () => {
        helpers.post_user = jest.fn().mockReturnValueOnce(1);
                            
        const res = await request.post("/userstore").send({
            _id: "h75fvg",
            username: "nickham",
            topArtist: "uzi",
            iconColour: "0xffffff",
            songs: [],
            matches: []
        });
        
        
        expect(res.statusCode).toEqual(200);
    });
});