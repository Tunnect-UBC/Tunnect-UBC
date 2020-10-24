const express = require('express');
const router = express.Router();


router.get('/', (req, res, next) => {
    res.status(200).json({
        message: "Handling GET requests to /userstore"
    });
});


router.post('/', (req, res, next) => {
    res.status(200).json({
        message: "Handling POST requests to /userstore"
    });
});


router.get('/:userId', (req, res, next) => {
    const id = req.params.userId;
    if (id === 'special') {
        res.status(200).json({
            message: 'You discovered the special Id',
            id: id
        })
    } else {
        res.status(200).json({
            message: 'You passed an ID',
            id: id
        })
    }
});


router.put('/:userId', (req, res, next) => {
    res.status(200).json({
        message: 'This is a put request to ' + req.params.userId
    });
});


router.delete('/:userId', (req, res, next) => {
    res.status(200).json({
        message: 'This is a request to delete ' + req.params.userId
    });
});

module.exports = router;