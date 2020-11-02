const request = require('supertest')
const app = require('../MatchmakingManager/app')

const output = [
    {
        score: 7.5,
        user: "david0"
    },
    {
        score: 0,
        user: "elises"
    }
]

describe('GET matchmaker endpoint', () => {
  it('should perform a get request for the matchmaker', async () => {
    const res = await request(app)
      .get('/matchmaker')
      .send({
        hostId: "67guyh4i"
      });
    expect(res.statusCode).toEqual(200);
    expect(res.body).toEqual(output);
  })
})