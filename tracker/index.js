var express = require('express');
var peersRequestHandler = require('./handlers/peersRequestHandler.js');
var fileSystemRequestHandler = require('./handlers/fileSystemRequestHandler.js');
var syncStatusHandler = require('./handlers/syncStatusHandler.js');
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
app.post("/peer/join", writeBackBoneHeaders, peersRequestHandler.join);
app.post("/peer/leave", writeBackBoneHeaders, peersRequestHandler.leave);

app.post("/peer/status", writeBackBoneHeaders, peersRequestHandler.getStatus);



//handler for Files
app.post("/file/insert", writeBackBoneHeaders, fileSystemRequestHandler.insertFile);
app.post("/file/delete", writeBackBoneHeaders, fileSystemRequestHandler.deleteFile);
app.post("/file/write", writeBackBoneHeaders, fileSystemRequestHandler.write);
app.post("/file/checksum", writeBackBoneHeaders, fileSystemRequestHandler.checkSum);
app.post("/file/status", writeBackBoneHeaders, fileSystemRequestHandler.getStatus);

app.post("/test", writeBackBoneHeaders, fileSystemRequestHandler.test);

app.post("/sync/status", writeBackBoneHeaders, syncStatusHandler.status);
app.post("/sync/stop", writeBackBoneHeaders, syncStatusHandler.stop);
app.post("/sync/start", writeBackBoneHeaders, syncStatusHandler.start);


function writeBackBoneHeaders(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    next();
}

// start the server
app.listen(properties.APP_PORT);
console.log("Tracker started");

