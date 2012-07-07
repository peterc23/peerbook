var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveRestaurantInfo(restaurantId, callback)
{
    if (restaurantId == null || typeof restaurantId == 'undefined') { callback(null); return; }
    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.RESTAURANTS_TABLE + ' WHERE '
        + tableProperties.RESTAURANTS_ID + ' = ?', [restaurantId], createMenuFromSingleResult, function(menuitem) {
        console.log("******************");
        console.log(menuitem);
        callback(menuitem);
    });
}

function createMenuFromSingleResult(result)
{

    console.log(result);
}
exports.retrieveRestaurantInfo = retrieveRestaurantInfo;



/*



 'SELECT * FROM ' + tableProperties.RESTAURANTS_TABLE + ' JOIN '
 + tableProperties.COUPONS_TABLE + ' ON ' +tableProperties.RESTAURANTS_TABLE+ '.'
 + tableProperties.RESTAURANTS_ID + ' = ' +tableProperties.COUPONS_TABLE+'.'
 + tableProperties.COUPONS_RESTAURANTID + ' WHERE ' + tableProperties.RESTAURANTS_TABLE+ '.'
 + tableProperties.RESTAURANTS_ID +'=?'
    */