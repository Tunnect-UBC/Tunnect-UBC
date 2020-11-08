const helpers = {
    imports() {
        const express = require("express");
        const app = express();
        const morgan = require("morgan");
        const bodyParser = require("body-parser");
        const mongoose = require("mongoose");
        return {
            express: express,
            app: app,
            morgan: morgan,
            bodyParser: bodyParser,
            mongoose: mongoose
        }
    },

    connectMongo(mongoose, dbUrl) {
        mongoose.connect(dbUrl, {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });
        
        mongoose.connection.once("open", () => {
            //console.log("Database connected:", userDBUrl);
        });
          
        mongoose.connection.on("error", (err) => {
            //console.error("connection error:", err);
        });
    },

    connectMorgan(app, morgan, bodyParser) {
        app.use(morgan("dev"));
        app.use(bodyParser.urlencoded({extended: false}));
        app.use(bodyParser.json());
    },

    setHeaders(app) {
        app.use((req, res, next) => {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "*");
            if (req.method === "OPTIONS") {
                res.header("Access-Control-Allow-Methods", "PUT, POST, GET, DELETE, PATCH");
                return res.status(200).json({});
            }
            next();   
        });
    }

}

module.exports = helpers;