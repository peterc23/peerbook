var express = require('express');
var peersRequestHandler = require('./handlers/peersRequestHandler.js');
var fileSystemRequestHandler = require('./handlers/fileSystemRequestHandler.js');
var properties = require('./resources/properties.js');

// create server
var app = express.createServer();
// configurations
app.configure(function() {
  app.use(express.bodyParser({
    uploadDir: './tmp',
    keepExtensions: true
  }));
  app.use(express.methodOverride());
  app.use(app.router);
  app.register('.html', require('jade'));
  app.use(express.static(__dirname + '/views'));
});


//handler for Peers
app.post("/api/join", peersRequestHandler.join);
app.post("/api/leave", peersRequestHandler.leave);

//handler for Files
app.post("/api/insert", fileSystemRequestHandler.insert);
app.post("/api/delete", fileSystemRequestHandler.delete);
app.post("/api/read", fileSystemRequestHandler.read);
app.post("/api/write", fileSystemRequestHandler.write);
app.post("/api/update", fileSystemRequestHandler.fileReieved);

app.post("/facebookmenu", function (req, res) {
    res.sendfile("views/facebookmenu/login.html");
});

app.get("/getFacebookObject/:objectId", actionRequestHandler.getFacebookObject);

// start the server
app.listen(properties.APP_PORT);
console.log("Tracker started");

