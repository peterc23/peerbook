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

exports.dbFileInfoObject = dbFileInfoObject;

