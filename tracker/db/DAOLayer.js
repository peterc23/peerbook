var dbFileSystem = require('./dbFileSystem.js');
var dbPeers = require('./dbPeers.js');


//File System
exports.insertNewFile = dbFileSystem.insertNewFile;
exports.retrieveFileInfo = dbFileSystem.retrieveFileInfo;
exports.deleteFile = dbFileSystem.deleteFile;
exports.getAllFileInfo = dbFileSystem.getAllFileInfo;
exports.updateFileInfo = dbFileSystem.updateFileInfo;
exports.retrieveFileInfoById = dbFileSystem.retrieveFileInfoById;
exports.retrieveFileInfoByPath = dbFileSystem.retrieveFileInfoByPath;

//Peers
exports.retrievePeerInfo = dbPeers.retrievePeerInfo;
exports.insertNewPeer = dbPeers.insertNewPeer;
exports.updatePeerStatusById = dbPeers.updatePeerStatusById;
exports.getAllPeerInfo = dbPeers.getAllPeerInfo;
