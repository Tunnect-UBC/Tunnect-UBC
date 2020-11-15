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