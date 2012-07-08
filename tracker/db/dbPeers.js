var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function insertNewPeer(peerInfo, callback)
{
    if (peerInfo[tableProperties.PEERS_HOSTNAME] == null || typeof peerInfo[tableProperties.PEERS_HOSTNAME] == 'undefined' ||
        peerInfo[tableProperties.PEERS_PORT] == null || typeof peerInfo[tableProperties.PEERS_PORT] == 'undefined' ||
        peerInfo[tableProperties.PEERS_STATUS == null || typeof peerInfo[tableProperties.PEERS_STATUS]]) {
        callback(null);
    }else{
        client.executeInsertSingleQuery('INSERT INTO ' +
            tableProperties.PEERS_TABLE  + '(' + tableProperties.PEERS_HOSTNAME + ',' + tableProperties.PEERS_PORT + ',' +
            tableProperties.PEERS_STATUS + ') VALUES (?, ?, ?)',[peerInfo[tableProperties.PEERS_HOSTNAME],
            peerInfo[tableProperties.PEERS_PORT], peerInfo[tableProperties.PEERS_STATUS]],
            function(err) {
                callback(err);
            });
    }
}

function retrievePeerInfo(peerInfo, callback){
    if (peerInfo[tableProperties.PEERS_HOSTNAME] == null || typeof  peerInfo[tableProperties.PEERS_HOSTNAME] == 'undefined' ||
        peerInfo[tableProperties.PEERS_PORT] == null || typeof  peerInfo[tableProperties.PEERS_PORT] == 'undefined'){
        callback(null);
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.PEERS_TABLE + ' WHERE ' + tableProperties.PEERS_HOSTNAME +
        ' = ? AND ' + tableProperties.PEERS_PORT + ' = ?', [peerInfo[tableProperties.PEERS_HOSTNAME], peerInfo[tableProperties.PEERS_PORT]]
        ,createPeerInfoFromSingleResult, function(result){
                callback(result);
            });
    }
}

function updatePeerStatusById(peerInfo, callback){
    if (peerInfo[tableProperties.PEERS_ID] == null || typeof  peerInfo[tableProperties.PEERS_ID] == 'undefined' ||
        peerInfo[tableProperties.PEERS_STATUS] == null || typeof  peerInfo[tableProperties.PEERS_STATUS] == 'undefined'){
        callback(null);
    }else{
        client.executeUpdateSingleQuery('UPDATE ' + tableProperties.PEERS_TABLE + ' SET ' + tableProperties.PEERS_STATUS +
            ' = ? WHERE ' + tableProperties.PEERS_ID + ' = ?', [peerInfo[tableProperties.PEERS_ID]], function(err){
                callback(err);
            });
    }
}

function createPeerInfoFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
    return factory.dbPeerInfoObject(result[tableProperties.PEERS_ID], result[tableProperties.PEERS_HOSTNAME],
        result[tableProperties.PEERS_PORT],result[tableProperties.PEERS_STATUS]);
}

exports.insertNewPeer = insertNewPeer;
exports.retrievePeerInfo = retrievePeerInfo;
exports.updatePeerStatusById = updatePeerStatusById;