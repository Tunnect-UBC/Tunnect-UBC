const mongoose = require("mongoose");

/**
 * Adding song array... hasnt been compiled or tested...
 */
const userSchema = new mongoose.Schema({
    _id: String,
    username: String,
    topArtist: String,
    iconColour: Number,
    songs: Array,
    matches: Array,
    likes: Array,
    dislikes: Array
});

module.exports = mongoose.model("User", userSchema);
