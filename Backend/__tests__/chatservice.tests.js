const supertest = require("supertest");
const utils = require("../chatService/app");
const request = supertest(utils.app);
const helpers = require("../chatService/utils/chatServiceHelpers");
const mockAxios = require("jest-mock-axios");


const mockdeleteChat = jest.fn().mockReturnValueOnce(0)
                                .mockReturnValueOnce(1)
                                .mockReturnValueOnce(2)
                                .mockReturnValueOnce(-1);

//////////////////DELETE chatservice/userid1/userid2
///delete test, deleteChats = 0
describe("DELETE/:userId endpoint", () => {
  it("Request to delete chat, success", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});
///delete test, deleteChats = 1
describe("DELETE/:userId endpoint", () => {
  it("Request to delete chat, chat not found", async () => {
        const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(200);
  });
});
///delete test, deleteChats = 2
describe("DELETE/:userId endpoint", () => {
  it("Request to get delete, failed", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(404);
  });
});
describe("DELETE/:userId endpoint", () => {
  it("Request to get delete, failed", async () => {
    helpers.deleteChat = mockdeleteChat;
    const res = await request.delete("/chatservice/123/456");
    expect(res.statusCode).toEqual(400);
  });
});

const mockgetChats = jest.fn().mockReturnValueOnce([0])
                              .mockReturnValueOnce([1,1,1])
                              .mockReturnValueOnce([-1]);

/////////////////////GET chatService/usrID
//get test, getChats = 0
 describe("GET/:userId endpoint", () => {
  it("Request to get chat array", async () => {
    helpers.getChats = mockgetChats;
    const res = await request.get("/chatservice/123");
    expect(res.statusCode).toEqual(500);
  });
});
//get test, getChats = 1
 describe("GET/:userId endpoint", () => {
  it("Request to get chat array", async () => {
    helpers.getChats = mockgetChats;
    const res = await request.get("/chatservice/456");
    expect(res.statusCode).toEqual(200);
  });
});
describe("GET/:userId endpoint", () => {
 it("Request to get chat array", async () => {
   helpers.getChats = mockgetChats;
   const res = await request.get("/chatservice/456");
   expect(res.statusCode).toEqual(400);
 });
});

//////////////////////GET chatService/userid1/userId2
const mockgetMessages = jest.fn().mockReturnValueOnce([0])
                                 .mockReturnValueOnce([1,1,1])
                                 .mockReturnValueOnce([-1]);

describe("GET/:userId1/:userId2 endpoint", () => {
  it("Request to get messages", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});

describe("GET/:userId/:userId2 endpoint", () => {
  it("Request to get messages", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/456/789");
    expect(res.statusCode).toEqual(200);
  });
});
describe("GET/:userId/:userId2 endpoint", () => {
  it("Request to get messages", async () => {
    helpers.getMessages = mockgetMessages;
    const res = await request.get("/chatservice/456/789");
    expect(res.statusCode).toEqual(400);
  });
});


///////////////////////POST chatService/receiverid
const mockpostMessages = jest.fn().mockReturnValueOnce([0])
                                 .mockReturnValueOnce([1,1])
                                 .mockReturnValueOnce([-1]);


describe("POST/:userId endpoint", () => {
  it("Request to post a message", async () => {
    mockAxios.get.mockResponse(() => {
       data: { notifId: "123"}
     })
     mockAxios.get.mockResponse(() => {
            data: {username: "456"}
          }
        );

    helpers.postMessages = mockpostMessages;
    const res = await request.post("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId endpoint", () => {
  it("Request to post a message", async () => {
    mockAxios.get.mockResponse(() => {
       data: { notifId: "123"}
     }).mockResponse(() => {
            data: {username: "456"}
          }
        );
    helpers.postMessages = mockpostMessages;
    const res = await request.post("/chatservice/456/789");
    expect(res.statusCode).toEqual(200);
  });
});
describe("POST/:userId endpoint", () => {
  it("Request to post a message", async () => {
    mockAxios.get.mockResponse(() => {
       data: { notifId: "123"}
     }).mockResponse(() => {
            data: {username: "456"}
          }
        );
    helpers.postMessages = mockpostMessages;
    const res = await request.post("/chatservice/456/789");
    expect(res.statusCode).toEqual(400);
  });
});

///////////////////POST chat
const mockpostChat = jest.fn().mockReturnValueOnce(0)
                               .mockReturnValueOnce(1)
                               .mockReturnValueOnce(2)
                               .mockReturnValueOnce(-1);

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat", async () => {
    mockAxios.get.mockResponse(() => {
       data: {
        username : "123"
       }
     }).mockResponse(() => {
            data: {
              username: "456"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/456");
    expect(res.statusCode).toEqual(500);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat", async () => {
    mockAxios.get.mockResponse(() => {
       data: {
         username : "123"
       }
     }).mockResponse(() => {
            data: {
              username: "456"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/456/78");
    expect(res.statusCode).toEqual(200);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat", async () => {
    mockAxios.get.mockResponse(() => {
       data: {
         username : "123"
       }
     }).mockResponse(() => {
            data: {
              username: "456"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/789");
    expect(res.statusCode).toEqual(300);
  });
});

describe("POST/:userId/:userId2 endpoint", () => {
  it("Request to post a chat", async () => {
    mockAxios.get.mockResponse(() => {
       data: {
         username : "123"
       }
     }
   ).mockResponse(() => {
            data: {
              username: "456"
            }
          }
        );
    helpers.postChat = mockpostChat;
    const res = await request.post("/chatservice/123/789");
    expect(res.statusCode).toEqual(400);
  });
});
