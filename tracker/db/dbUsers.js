var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveUserDetails(user, callback)
{
    if (user == null || typeof user == 'undefined') {
        callback(null, user);
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.USERS_TABLE + ' WHERE '
            + tableProperties.USERS_ID + ' = ?', [user[tableProperties.USERS_ID]], createUserFromSingleResult, function(dbUser) {
            callback(dbUser, user);
        });
    }
}

function addNewUserDetails(id, name, email, fbToken, image, callback)
{
    client.executeInsertSingleQuery(
        'INSERT INTO ' + tableProperties.USERS_TABLE + ' (' + tableProperties.USERS_ID + ',' + tableProperties.USERS_NAME
        + ',' + tableProperties.USERS_EMAIL + ',' + tableProperties.USERS_FBTOKEN + ',' + tableProperties.USERS_IMAGE +
        ') VALUE (?, ?, ?, ?, ?)', [id, name, email, fbToken, image],
        function(info) {
            callback(info);
        }
    );
}

function updateUserDetails(id, name, email, fbToken, image, callback)
{

    client.executeUpdateSingleQuery(
        'UPDATE ' + tableProperties.USERS_TABLE + ' SET ' + tableProperties.USERS_NAME
        + ' = ?, ' + tableProperties.USERS_EMAIL + ' = ?, ' + tableProperties.USERS_FBTOKEN + ' = ?, ' +
            tableProperties.USERS_IMAGE + ' = ? WHERE id = ?',
        [name, email, fbToken, image, id],
        function() {
            callback();
        }
    );
}

function createUserFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
        return factory.createUser(result[tableProperties.USERS_ID], result[tableProperties.USERS_NAME],
                                    result[tableProperties.USERS_EMAIL], result[tableProperties.USERS_FBTOKEN],
                                    result[tableProperties.USERS_IMAGE]);
}

exports.retrieveUserDetails = retrieveUserDetails;
exports.addNewUserDetails = addNewUserDetails;
exports.updateUserDetails = updateUserDetails;