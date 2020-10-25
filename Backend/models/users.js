const mongoose = require('mongoose');

const userSchema = mongoose.Schema({
    _id: String,
    username: String,
    top_artist: String,
    icon_colour: String
});

module.exports = mongoose.model('User', userSchema);