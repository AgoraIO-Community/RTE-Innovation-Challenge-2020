var router = require('express').Router();

router.use('/associated-data', require('./associated-data'));
router.use('/captcha', require('./captcha'));
router.use('/leaderboard', require('./leaderboard'));
router.use('/pubsub', require('./pubsub'));
router.use('/readonly', require('./readonly'));
router.use('/redlock', require('./redlock'));
router.use('/task-queue', require('./task-queue'));

router.use('/game-type-cache', require('./game-type-cache'));

module.exports = router;
