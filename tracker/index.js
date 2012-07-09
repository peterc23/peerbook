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
app.post("/peer/join", writeBackBoneHeaders, peersRequestHandler.join);
app.post("/peer/leave", writeBackBoneHeaders, peersRequestHandler.leave);

app.post("/peer/status", writeBackBoneHeaders, peersRequestHandler.getStatus);



//handler for Files
app.post("/file/insert", writeBackBoneHeaders, fileSystemRequestHandler.insert);
app.post("/file/delete", writeBackBoneHeaders, fileSystemRequestHandler.delete);
app.post("/file/read", writeBackBoneHeaders, fileSystemRequestHandler.read);
app.post("/file/write", writeBackBoneHeaders, fileSystemRequestHandler.write);
app.post("/file/update", writeBackBoneHeaders, fileSystemRequestHandler.fileReieved);

app.post("/file/status", writeBackBoneHeaders, fileSystemRequestHandler.getStatus);

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

