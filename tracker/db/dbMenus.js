var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveRestaurantMenus(restaurantId, callback)
{
    if (restaurantId == null || typeof restaurantId == 'undefined') { callback(null); return; }
    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.MENUS_TABLE + ' WHERE '
        + tableProperties.MENUS_RESTAURANTID + ' = ?', [restaurantId], createMenuFromSingleResult, function(menus) {
        callback(menus);
    });
}

function createMenuFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined') { callback(null); return; }
    return factory.createMenu(result[tableProperties.MENUS_ID], result[tableProperties.MENUS_MENUNAME],
                              result[tableProperties.MENUS_RESTAURANTID], result[tableProperties.MENUS_DESCRIPTION]);
}
exports.retrieveRestaurantMenus = retrieveRestaurantMenus;