var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function saveEatActionPreFacebookPost(eatAction, callback)
{
    if (eatAction[tableProperties.EATACTIONS_MENUITEMID] == null || typeof eatAction[tableProperties.EATACTIONS_MENUITEMID] == 'undefined' ||
        eatAction[tableProperties.EATACTIONS_USERID] == null || typeof eatAction[tableProperties.EATACTIONS_USERID] == 'undefined' ||
        eatAction[tableProperties.EATACTIONS_DATE] == null || typeof eatAction[tableProperties.EATACTIONS_DATE] == 'undefined') {
        callback(null, id);
    }else{
        client.executeInsertSingleQuery('INSERT INTO ' +
            tableProperties.EATACTIONS_TABLE  + '(' + tableProperties.EATACTIONS_MENUITEMID + ',' + tableProperties.EATACTIONS_USERID +
            ',' + tableProperties.EATACTIONS_TAGS + ',' + tableProperties.EATACTIONS_DESCRIPTION + ',' + tableProperties.EATACTIONS_DATE +
            ') VALUES (?, ?, ?, ?, ?)', [eatAction[tableProperties.EATACTIONS_MENUITEMID],eatAction[tableProperties.EATACTIONS_USERID],
            eatAction[tableProperties.EATACTIONS_TAGS], eatAction[tableProperties.EATACTIONS_DESCRIPTION],
            eatAction[tableProperties.EATACTIONS_DATE]], function(err) {
            callback(err);
        });
    }
}

function getEatAction(eatAction, callback){
    if (eatAction[tableProperties.EATACTIONS_USERID] == null || typeof eatAction[tableProperties.EATACTIONS_USERID] == 'undefined' ||
        eatAction[tableProperties.EATACTIONS_DATE] == null || typeof eatAction[tableProperties.EATACTIONS_DATE] == 'undefined') {
        callback(null, id);
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.EATACTIONS_TABLE + ' WHERE ' + tableProperties.EATACTIONS_USERID +
            ' = ? AND ' + tableProperties.EATACTIONS_DATE + ' = ? ', [eatAction[tableProperties.EATACTIONS_USERID],
            eatAction[tableProperties.EATACTIONS_DATE]], createEatActionFromSingleResult, function(result) {
            callback(result);
        });
    }
}

function getEatActionbyId(menuId, callback){
    if(menuId == null || typeof menuId == 'undefined'){
        callback(null);
    }else{
        client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.EATACTIONS_TABLE + ' WHERE ' + tableProperties.EATACTIONS_ID +
        ' = ?', [menuId], createEatActionFromSingleResult, function(result){
            callback(result);
        });
    }
}

function createEatActionFromSingleResult(result)
{
    console.log("CREATE EAT ACTION ENTERED");
    console.log(result);
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
    return factory.dbEatActionObject(result[tableProperties.EATACTIONS_ID], result[tableProperties.EATACTIONS_MENUITEMID],
        result[tableProperties.EATACTIONS_USERID],result[tableProperties.EATACTIONS_TAGS],
        result[tableProperties.EATACTIONS_DESCRIPTION], result[tableProperties.EATACTIONS_DATE], result[tableProperties.EATACTIONS_FBIMGURL]);
}

exports.getEatAction = getEatAction;
exports.getEatActionbyId = getEatActionbyId;
exports.saveEatActionPreFacebookPost = saveEatActionPreFacebookPost;