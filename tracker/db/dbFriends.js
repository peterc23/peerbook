var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveFriendships(id, friendId, callback)
{
    if (id == null || typeof id == 'undefined' || friendId == null || typeof friendId == 'undefined'){
        callback(null, null);
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.FRIENDS_TABLE + ' WHERE ' +
            tableProperties.FRIENDS_ID + ' = ? AND ' + tableProperties.FRIENDS_FRIENDID + ' = ?', [id, friendId], createFriendFromSingleResult, function(friend){
            console.log("before callback");
            console.log(friend);
            console.log(id);
            callback(friend, id);
        });
    }
}

function insertFriendDetails(id, friendId, callback)
{
    if (id == null || typeof id == 'undefined' || friendId == null || typeof friendId == 'undefined') {
        callback(null, id);
    }else{
        client.executeFindSingleQuery('INSERT INTO ' +
            tableProperties.FRIENDS_TABLE + '(' + tableProperties.FRIENDS_ID + ',' + tableProperties.FRIENDS_FRIENDID +
            ') VALUES (?, ?)', [id, friendId], createFriendFromSingleResult, function(dbFriend) {
            callback(dbFriend, id);
        });
    }
}

function createFriendFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
    return factory.createFriend(result[tableProperties.FRIENDS_ID], result[tableProperties.FRIENDS_FRIENDID]);
}

exports.retrieveFriendships = retrieveFriendships;
exports.insertFriendDetails = insertFriendDetails;