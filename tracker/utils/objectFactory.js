var tableProperties = require('../resources/tableProperties.js');

function dbFileInfoObject(fileId, timestamp, checksum, editing, path){
    if(typeof fileId == 'undefined' || typeof timestamp == 'undefined' || typeof checksum == 'undefined' ||
        typeof editing == 'undefined' || typeof path == 'undefined'){
        return;
    }else{
        var fileInfo = {};

        fileInfo[tableProperties.FILESYSTEM_ID] = fileId;
        fileInfo[tableProperties.FILESYSTEM_TIMESTAMP] = timestamp;
        fileInfo[tableProperties.FILESYSTEM_CHECKSUM] = checksum;
        fileInfo[tableProperties.FILESYSTEM_EDITING] = editing;
        fileInfo[tableProperties.FILESYSTEM_PATH] = path;

        return fileInfo
    }
}

function dbPeerInfoObject(peerId, hostname, port, status){
    if(typeof peerId == 'undefined' || typeof  hostname == 'undefined' || typeof port == 'undefined' || typeof status == 'undefined'){
        return;
    }else{
        var peerInfo = {};

        peerInfo[tableProperties.PEERS_ID] = peerId;
        peerInfo[tableProperties.PEERS_HOSTNAME] = hostname;
        peerInfo[tableProperties.PEERS_PORT] = port;
        peerInfo[tableProperties.PEERS_STATUS] = status;

        return peerInfo;
    }
}

exports.dbFileInfoObject = dbFileInfoObject;
exports.dbPeerInfoObject =dbPeerInfoObject;

