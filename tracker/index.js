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
  app.use(express.static(__dirname + '/views'));
});

//handler for Peers
app.post("/peer/join", peersRequestHandler.join);
app.post("/peer/leave", peersRequestHandler.leave);

app.get("/peer/status", peersRequestHandler.getStatus);



//handler for Files
app.post("/file/insert", fileSystemRequestHandler.insert);
app.post("/file/delete", fileSystemRequestHandler.delete);
app.post("/file/read", fileSystemRequestHandler.read);
app.post("/file/write", fileSystemRequestHandler.write);
app.post("/file/update", fileSystemRequestHandler.fileReieved);

app.get("/file/status", fileSystemRequestHandler.getStatus);

app.post("/test", writeBackBoneHeaders, fileSystemRequestHandler.test);


function writeBackBoneHeaders(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    next();
}

// start the server
app.listen(properties.APP_PORT);
console.log("Tracker started");

