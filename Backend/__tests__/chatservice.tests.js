const supertest = require("supertest");
const utils = require("../chatService/app");
const request = supertest(utils.app);
const helpers = require("../chatService/utils/chatServiceHelpers");
const axios = require("axios");

jest.mock("axios");

const mockdeleteChat = jest.fn().mockReturnValueOnce(0)
                                .mockReturnValueOnce(1)
                                .mockReturnValueOnce(2)
                                .mockReturnValueOnce(-1);

//////////////////DELETE chatservice/userid1/userid2
///delete test, deleteChats = 0
describe("DELETE/:userId1/:userId2 endpoint", () => {
  it("Request to delete chat, success", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});
///delete test, deleteChats = 1
describe("DELETE/:userId/:userId2 endpoint", () => {
  it("Request to delete chat, success", async () => {
        const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(200);
  });
});
///delete test, deleteChats = 2
describe("DELETE/:userId/:userId2 endpoint", () => {
  it("Request to get delete, chat not found", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(404);
  });
});
describe("DELETE/:userId/:userId2 endpoint", () => {
  it("Request to get delete, failed", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(400);
  });
});

const mockgetChats = jest.fn().mockReturnValueOnce([0])
                              .mockReturnValueOnce([1,[1],[1]])
                              .mockReturnValueOnce([-1]);

/////////////////////GET chatService/usrID
//get test, getChats = 0
 describe("GET/:userId endpoint", () => {
  it("Request to get chat array, failed", async () => {
    helpers.getChats = mockgetChats;
    const res = await request.get("/chatservice/123");
    expect(res.statusCode).toEqual(500);
  });
});
//get test, getChats = 1
 describe("GET/:userId endpoint", () => {
  it("Request to get chat array, success", async () => {
    helpers.getChats = mockgetChats;
    const res = await request.get("/chatservice/456");
    expect(res.statusCode).toEqual(200);
  });
});
describe("GET/:userId endpoint", () => {
 it("Request to get chat array, unexpected result", async () => {
   helpers.getChats = mockgetChats;
   const res = await request.get("/chatservice/456");
   expect(res.statusCode).toEqual(400);
 });
});

//////////////////////GET chatService/userid1/userId2
const mockgetMessages = jest.fn().mockReturnValueOnce([0])
                                 .mockReturnValueOnce([1,[1],[1]])
                                 .mockReturnValueOnce([-1]);

describe("GET/:userId1/:userId2 endpoint", () => {
  it("Request to get messages failed", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});

describe("GET/:userId/:userId2 endpoint", () => {
  it("Request to get messages success", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/456/789");
    expect(res.statusCode).toEqual(200);
  });
});
describe("GET/:userId/:userId2 endpoint", () => {
  it("Request to get messages unexpected result", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/456/789");
    expect(res.statusCode).toEqual(400);
  });
});


///////////////////////POST chatService/receiverid
const mockpostMessages = jest.fn().mockReturnValueOnce([0])
                                 .mockReturnValueOnce([1,[1],[1]])
                                 .mockReturnValueOnce([-1]);


describe("POST/:userId endpoint", () => {
  it("Request to post a message failed", async () => {
    axios.get.mockResolvedValue({
       data: { notifId: "123", username: "123"}
     });
    helpers.postMessage = mockpostMessages;
    const res = await request.post("/chatservice/123")
                             .type("json")
                             .send({
                               senderid : "123",
                               message : "hello",
                               timeStamp : 0
                             });
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId endpoint", () => {
  it("post new chat, Bad request body failure", async () => {
    axios.get.mockResolvedValue({
       data: { notifId: "123", username: "123"}
     });
    helpers.postMessage = mockpostMessages;
    const res = await request.post("/chatservice/123")
                             .type("json")
                             .send({
                               senderid : "123",
                             });
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId endpoint", () => {
  it("Request to post a message success", async () => {
    axios.get.mockResolvedValue( {
       data: { notifId: "123", username: "456"}
     });
    helpers.postMessage = mockpostMessages;
    const res = await request.post("/chatservice/456")
                            .type("json")
                            .send({
                                senderid : "123",
                                message : "hello",
                                timeStamp : 0
                            });
    expect(res.statusCode).toEqual(200);
  });
});
describe("POST/:userId endpoint", () => {
  it("Request to post a message unexpected result", async () => {
    axios.get.mockResolvedValue({
       data: { notifId: "123", username: "456"}
     });
    helpers.postMessage = mockpostMessages;
    const res = await request.post("/chatservice/789")
                             .type("json")
                             .send({
                                senderid : "123",
                                message : "hello",
                               timeStamp : 0
                             });
    expect(res.statusCode).toEqual(400);
  });
});

///////////////////POST chat
const mockpostChat = jest.fn().mockReturnValueOnce([0])
                               .mockReturnValueOnce([1,[1],[1]])
                               .mockReturnValueOnce([2])
                               .mockReturnValueOnce([-1]);

describe("POST/:userId1/:userId2 endpoint", () => {
  it("Request to post a chat failed", async () => {
    axios.get.mockResolvedValue({
       data: {
        username : "123",
        iconColour : "yellow"
       }
     }).mockResolvedValue({
            data: {
              username: "456",
              iconColour : "green"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/456")
                            .type("json")
                            .send({
                               timeStamp : 0
                             });
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat bady body request", async () => {
    axios.get.mockResolvedValue({
       data: {
        username : "123",
        iconColour : "yellow"
       }
     }).mockResolvedValue({
            data: {
              username: "456",
              iconColour : "green"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat success", async () => {
    axios.get.mockResolvedValue({
       data: {
         username : "123",
         iconColour : "yellow"
       }
     }).mockResolvedValue( {
            data: {
              username: "456",
              iconColour : "green"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/456/789")
                             .type("json")
                             .send({
                               timeStamp: 0
                             });
    expect(res.statusCode).toEqual(200);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat chat already exists", async () => {
    axios.get.mockResolvedValue({
       data: {
         username : "123",
         iconColour : "yellow"
       }
     }).mockResolvedValue({
            data: {
              username: "456",
              iconColour : "green"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/789")
                             .type("json")
                             .send({
                               timeStamp: 0
                             });
    expect(res.statusCode).toEqual(300);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat unexpected result", async () => {
    axios.get.mockResolvedValue( {
       data: {
         username : "123",
         iconColour : "yellow"
       }
     }
   ).mockResolvedValue({
            data: {
              username: "456",
              iconColour : "green"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/789")
                              .type("json")
                              .send({
                                timeStamp: 0
                              });
    expect(res.statusCode).toEqual(400);
  });
});
