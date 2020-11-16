const request = require("supertest");
const app = require("../chatService/app");

jest.mock("./deleteChat");
const deleteChat = require("../routes/chatservice");
deleteChat.mockImplementationOnce(() => 0)
          .mockImplementationOnce(() => 1)
          .mockImplementationOnce(() => 2);

describe("GET/:userId endpoint", () => {
  it("Request to get chat array", async () => {
    const res = await request(app)
      .get("/chatservice/123/456");
    expect(res.statusCode).toEqual(200);
  });
});
