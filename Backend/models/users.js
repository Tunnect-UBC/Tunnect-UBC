const mongoose = require("mongoose");
//const Song = require("./song");

const song = {
    _id: String,
    artist: String,
    name: String,
    genre: String,
    relatedArtists: [String]
};

const userSchema = new mongoose.Schema({
    _id: String,
    username: String,
    topArtist: String,
    iconColour: String,
    notifId: String,
    songs: Array,
    matches: Array,
    likes: Array,
    dislikes: Array,
    favGenre: String,
    iconColour: Number,
    songs: [song],
    matches: [String],
    likes: [String],
    dislikes: [String]

});

module.exports = mongoose.model("User", userSchema);
