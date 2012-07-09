var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function insertNewFile(fileInfo, callback)
{
    if (fileInfo[tableProperties.FILESYSTEM_TIMESTAMP] == null || typeof fileInfo[tableProperties.FILESYSTEM_TIMESTAMP] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_PATH] == null || typeof fileInfo[tableProperties.FILESYSTEM_PATH] == 'undefined'
) {
        callback(null);
    }else{
        client.executeInsertSingleQuery('INSERT INTO ' +
            tableProperties.FILESYSTEM_TABLE  + '(' + tableProperties.FILESYSTEM_TIMESTAMP + ',' + tableProperties.FILESYSTEM_CHECKSUM +
            ',' + tableProperties.FILESYSTEM_PATH + ') VALUES (?, ?, ?)',
            [fileInfo[tableProperties.FILESYSTEM_TIMESTAMP],fileInfo[tableProperties.FILESYSTEM_CHECKSUM],
                fileInfo[tableProperties.FILESYSTEM_PATH]], function(err) {
            callback(err);
        });
    }
}

function retrieveFileInfo(fileInfo, callback){
    if (fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_TIMESTAMP] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined'){
        callback(null)
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.FILESYSTEM_TABLE + ' WHERE ' +
            tableProperties.FILESYSTEM_CHECKSUM + ' = ? AND ' + tableProperties.FILESYSTEM_TIMESTAMP + ' = ?', [fileInfo[tableProperties.FILESYSTEM_CHECKSUM,
            fileInfo[tableProperties.FILESYSTEM_TIMESTAMP]]], createFileInfoFromSingleResult, function(result){
            callback(result);
        });
    }
}

function deleteFile(fileInfo, callback)
{
    if (fileInfo[tableProperties.FILESYSTEM_ID] == null || typeof eatAction[tableProperties.FILESYSTEM_ID] == 'undefined') {
        callback(null);
    }else{
        client.executeDeleteSingleQuery('DELETE * FROM ' + tableProperties.FILESYSTEM_TABLE  + ' WHERE ' +
            tableProperties.FILESYSTEM_ID + ' = ?', [fileInfo[tableProperties.FILESYSTEM_ID]], function(err) {
            callback(err);
        });
    }
}

function createFileInfoFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
    return factory.dbFileInfoObject(result[tableProperties.FILESYSTEM_ID], result[tableProperties.FILESYSTEM_TIMESTAMP],
        result[tableProperties.FILESYSTEM_CHECKSUM],result[tableProperties.FILESYSTEM_EDITING],
        result[tableProperties.FILESYSTEM_PATH]);
}

exports.insertNewFile = insertNewFile;
exports.retrieveFileInfo = retrieveFileInfo;
exports.deleteFile = deleteFile;