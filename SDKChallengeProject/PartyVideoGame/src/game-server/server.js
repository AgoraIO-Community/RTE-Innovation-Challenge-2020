var app = require('express')();
var http = require('http').createServer(app);
var io = require('socket.io')(http, { path: '/api' });

var games = [];

function getOrCreateGame(gameId) {
  var item = games[gameId];
  if (item) {
    return item;
  } else {
    var game = {};
    game.beginTime = Date.now();
    game.onTimerChanged = null;
    game.lines = [];
    game.startGame = () => {

      //initial game
      const { round, roundTime } = game.room;
      game.room.roomState = 1;
      io.to(gameId).emit('gameRoom', game.room);
      let { currentRound, timing } = { currentRound: 1, timing: 0 };
      let total = round * roundTime;
      for (let index = 0; index < game.users.length; index++) {
        game.users[index].userState = 'waiting';
      }
      game.users[0].userState = 'choosing';

      //set game round logic
      game.onTimerChanged = () => {
        timing++;

        if (timing > roundTime) {
          currentRound++;
          //round change logic
          game.users = setNextPlayerState(game.users);
          io.to(gameId).emit('gameUsers', game.users);
          game.cleanCanvas();
          timing -= roundTime;
        }

        var isFinished = (currentRound - 1) * roundTime + timing >= total;
        game.round.currentRound = currentRound;
        game.round.timing = timing;
        game.round.isFinished = isFinished;
        io.to(gameId).emit('gameRound', game.round);

        console.log(game.round);

        if (isFinished) {
          game.stopGame();
         
        }
      };

      //set game timer
      game.timer = setInterval(() => {
        var user = game.users.find(p => p.userState === 'choosing' || p.userState === 'selectWinner');
        if (!user) {//if no user are choosing then continue the clock
          game.onTimerChanged()
        }
        if (Date.now() - game.beginTime > 1000 * 60 * 60 * 6) {//if game run over 6 hours, stop it.
          game.stopGame();
        }
      }, 1000);
    }
    game.stopGame = () => {
      clearInterval(game.timer);
      game.room.roomState = 0;
      game.users.forEach(user => {
        user.score=0;
      });
      delete game.onTimerChanged;
      io.to(gameId).emit('gameRoom', game.room);
    };

    game.updateRoom = (room) => {
      game.room = room;
    };
    game.updateUser = (user) => {
      if (user.userState === 'choosing' || user.userState === 'selectWinner') {
        game.cleanCanvas();
      }
      var index = game.users.findIndex(p => p.uid === user.uid);
      if (index > -1) {
        game.users[index] = user;
      } else {
        game.users.push(user);
      }
    };
    game.updateRound = (round) => {
      game.round = round;
    }
    game.cleanCanvas = () => {
      io.to(gameId).emit("canvasClean", {});
      game.lines = [];
    }
    game.dispose = () => {
      clearInterval(game.startTimer);
      games[gameId] = null;
    }
    games[gameId] = game;
    return game;
  }
}

function setNextPlayerState(users) {

  users.sort((a, b) => {
    if (a.uid < b.uid) { return -1; }
    if (a.uid > b.uid) { return 1; }
    return 0;
  });

  let playingIndex = users.findIndex(p => p.userState !== 'waiting');
  let nextPlayingIndex = playingIndex + 1 >= users.length ? 0 : playingIndex + 1;

  for (let index = 0; index < users.length; index++) {
    const element = users[index];
    if (index === playingIndex) {
      element.userState = 'waiting';
    }
    if (index === nextPlayingIndex) {
      element.userState = 'choosing';
    }
  }
  return users;
}

io.on('connection', (socket) => {

  console.log('a user connected');
  socket.on("disconnect", function () {
    console.log("user disconnected");
  });

  socket.on('join', gameData => {

    var gameId = gameData.id;
    var game = getOrCreateGame(gameId);
    socket.join(gameId);

    socket.on("line", data => {
      io.to(gameId).emit("line", data);
      game.lines.push(data);
    });

    socket.on("canvas", data => {
      io.to(gameId).emit("canvas", data);
    });

    socket.on("canvasClean", () => {
      game.cleanCanvas();
    });

    socket.on("gameRoom", data => {
      game.updateRoom(data);
      io.to(gameId).emit("gameRoom", data);
    });

    socket.on("gameRound", data => {
      game.updateRound(data);
      io.to(gameId).emit("gameRound", data);
    });

    socket.on("gameUserUpdate", data => {
      game.updateUser(data);
      io.to(gameId).emit("gameUsers", game.users);
    });

    socket.on("initialGame", (data) => {
      if (!game.room) {
        game.room = data.gameRoom;
      }
      if (!game.round) {
        game.round = data.gameRound;
      }
      if (!game.users) {
        game.users = data.gameUsers;
      }

      io.to(gameId).emit("gameRoom", game.room);
      io.to(gameId).emit("gameRound", game.round);
      io.to(gameId).emit("gameUsers", game.users);
      io.to(gameId).emit("gameLines", game.lines);
    });

    socket.on("startGame", () => {
      game.startGame();
    });
    socket.on('stopGame', () => {
      game.stopGame();
    })
    socket.on('closeGame', () => {
      game.dispose();
    })

  })

});

http.listen(5000, () => {
  console.log('listening on *:5000');
});