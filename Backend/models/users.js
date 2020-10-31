const mongoose = require('mongoose');

/**
 * Adding song array... hasnt been compiled or tested...
 */
const userSchema = mongoose.Schema({
    _id: String,
    username: String,
    top_artist: String,
    icon_colour: String,
    songs: Array
});

module.exports = mongoose.model('User', userSchema);