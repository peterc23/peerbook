var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function insertNewFile(fileInfo, callback)
{
    if (fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_PATH] == null || typeof fileInfo[tableProperties.FILESYSTEM_PATH] == 'undefined'
) {
        callback(null);
    }else{
        client.executeInsertSingleQuery('INSERT INTO ' +
            tableProperties.FILESYSTEM_TABLE  + ' ( ' + tableProperties.FILESYSTEM_CHECKSUM +
            ',' + tableProperties.FILESYSTEM_PATH + ' ) VALUES ( ? , ? )',
            [fileInfo[tableProperties.FILESYSTEM_CHECKSUM], fileInfo[tableProperties.FILESYSTEM_PATH]],
            function(err) {
                callback(err);
        });
    }
}

function retrieveFileInfoById(fileInfo, callback){
    if(fileInfo[tableProperties.FILESYSTEM_ID] == null || typeof fileInfo[tableProperties.FILESYSTEM_ID] == 'undefined'){
        callback(null);
        return;
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.FILESYSTEM_TABLE + ' WHERE ' +
        tableProperties.FILESYSTEM_ID + ' = ?', [fileInfo[tableProperties.FILESYSTEM_ID]], createFileInfoFromSingleResult,
        function(result){
           callback(result);
        });
    }
}

function retrieveFileInfoByPath(fileInfo, callback){
    if(fileInfo[tableProperties.FILESYSTEM_PATH] == null || typeof fileInfo[tableProperties.FILESYSTEM_PATH] == 'undefined'){
        callback(null);
        return;
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.FILESYSTEM_TABLE + ' WHERE ' +
            tableProperties.FILESYSTEM_PATH + ' = ?', [fileInfo[tableProperties.FILESYSTEM_PATH]], createFileInfoFromSingleResult,
            function(result){
                callback(result);
            });
    }
}

function retrieveFileInfo(fileInfo, callback){
    if (fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_PATH] == null || typeof  fileInfo[tableProperties.FILESYSTEM_PATH] == 'undefined'){
        callback(null);
        return;
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.FILESYSTEM_TABLE + ' WHERE ' +
            tableProperties.FILESYSTEM_CHECKSUM + ' = ? AND ' + tableProperties.FILESYSTEM_PATH + ' = ?',
            [fileInfo[tableProperties.FILESYSTEM_CHECKSUM],fileInfo[tableProperties.FILESYSTEM_PATH]],
            createFileInfoFromSingleResult,
            function(result){
                callback(result);
        });
    }
}

function deleteFile(fileInfo, callback)
{
    if (fileInfo[tableProperties.FILESYSTEM_ID] == null || typeof fileInfo[tableProperties.FILESYSTEM_ID] == 'undefined') {
        callback(null);
        return;
    }else{
        client.executeDeleteSingleQuery('DELETE * FROM ' + tableProperties.FILESYSTEM_TABLE  + ' WHERE ' +
            tableProperties.FILESYSTEM_ID + ' = ?', [fileInfo[tableProperties.FILESYSTEM_ID]], function(err) {
            callback(err);
        });
    }
}

function getAllFileInfo(callback){
    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.FILESYSTEM_TABLE, [] ,createFileInfoFromSingleResult,
        function(fileList){
            callback(fileList);
        });
}

function updateFileInfo(fileInfo, callback){
    if (fileInfo[tableProperties.FILESYSTEM_ID] == null || typeof  fileInfo[tableProperties.FILESYSTEM_ID] == 'undefined' ||
        fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == null || typeof fileInfo[tableProperties.FILESYSTEM_CHECKSUM] == 'undefined'){
        callback(null);
        return;
    }else{
        client.executeUpdateSingleQuery('UPDATE ' + tableProperties.FILESYSTEM_TABLE + ' SET ' + tableProperties.FILESYSTEM_CHECKSUM +
            ' = ? WHERE ' + tableProperties.FILESYSTEM_ID + ' = ?',
            [fileInfo[tableProperties.FILESYSTEM_CHECKSUM],
                fileInfo[tableProperties.FILESYSTEM_ID]],
            function(info){
                callback(info);
            });

    }

}

function createFileInfoFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return null;
    }
    return factory.dbFileInfoObject(result[tableProperties.FILESYSTEM_ID], result[tableProperties.FILESYSTEM_TIMESTAMP],
        result[tableProperties.FILESYSTEM_CHECKSUM],result[tableProperties.FILESYSTEM_EDITING],
        result[tableProperties.FILESYSTEM_PATH]);
}

exports.insertNewFile = insertNewFile;
exports.retrieveFileInfo = retrieveFileInfo;
exports.deleteFile = deleteFile;
exports.getAllFileInfo = getAllFileInfo;
exports.updateFileInfo = updateFileInfo;
exports.retrieveFileInfoById = retrieveFileInfoById;
exports.retrieveFileInfoByPath = retrieveFileInfoByPath;