var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveRestaurantMenuTypes(menuId, callback)
{
    if (menuId == null || typeof menuId == 'undefined')
    {
        callback(null);
        return;
    }
    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.MENUTYPE_TABLE + ' WHERE '
        + tableProperties.MENUTYPE_MENUID + ' = ?' , [menuId], createMenuTypeFromSingleResult, function(menuType) {
        callback(menuType);
    });
}

function createMenuTypeFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined') { callback(null); return; }
    return factory.createMenuType(result[tableProperties.MENUTYPE_ID], result[tableProperties.MENUTYPE_MENUNAME],
        result[tableProperties.MENUTYPE_MENUIMG], result[tableProperties.MENUTYPE_MENUID], result[tableProperties.MENUTYPE_FOODTYPE]);
}
exports.retrieveRestaurantMenuTypes = retrieveRestaurantMenuTypes;