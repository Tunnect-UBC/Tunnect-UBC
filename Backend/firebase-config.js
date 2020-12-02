var admin = require("firebase-admin");

var serviceAccount = require("./tunnect-admin.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://tunnect-b7a87.firebaseio.com"
});

module.exports.admin = admin;
