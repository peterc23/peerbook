var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function addNewMenuItem(restaurantId, name, type, callback)
{ 
    client.executeInsertSingleQuery(
        'INSERT INTO menuitems (restaurantId, name, type) VALUES (?, ?, ?)', 
        [restaurantId, name, type],
        function(id) {
            menuItem.id = id;
            callback();
        }
    );
}

function updateMenuItem(restaurantId, name, type, id, callback)
{

    client.executeUpdateSingleQuery(
        'UPDATE menuitems SET restaurantId = ?, name = ?, type = ? WHERE id = ?',
        [restaurantId, name, type, id],
        function() {
            callback();
        }
    );
}

function findMenuItemById(id, callback)
{
    client.executeFindSingleQuery('SELECT * FROM ' + tableProperties.MENUITEMS_TABLE + ' WHERE ' +
        tableProperties.MENUITEMS_ID + ' = ? LIMIT 1', [id], createMenuItemFromSingleResult,
        function(menuitem) {
            callback(menuitem);
        });
}

function findAllMenuItemsFromRestaurant(restaurant, callback)
{
    if (restaurant == null || typeof restaurant == 'undefined') { callback(null); return; }
    
    findAllMenuItemsFromRestaurantId(restaurant.restaurantId, callback);
}

function findAllMenuItemsFromMenuId(menuId, callback)
{
    if (menuId == null || typeof menuId == 'undefined'){ callback(null); return;}

    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.MENUITEMS_TABLE + ' WHERE ' +
        tableProperties.MENUITEMS_MENUID + ' = ?', [menuId], createMenuItemFromSingleResult, function(menuitems){
        callback(menuitems)
    });


}
function findAllMenuItemsFromRestaurantId(restaurantId, callback)
{
    console.log("restaurantId = " + restaurantId);
    if (restaurantId == null || typeof restaurantId == 'undefined') { callback(null); return; }
    
    client.executeFindMultipleQuery('SELECT b.' + tableProperties.MENUS_MENUNAME + ', a.' +
        tableProperties.MENUITEMS_ID + ', a.' + tableProperties.MENUITEMS_MENUID + ', a.' +
        tableProperties.MENUITEMS_NAME + ', a.' + tableProperties.MENUITEMS_OBJECTTYPE + ', a.' +
        tableProperties.MENUITEMS_MENUTYPE + ', a.' + tableProperties.MENUITEMS_DESCRIPTION + ', a.' +
        tableProperties.MENUITEMS_PICTURE + ', a.' + tableProperties.MENUITEMS_PRICE + ' FROM ' +
        tableProperties.MENUITEMS_TABLE + ' a join ' + tableProperties.MENUS_TABLE + ' b on b.' +
        tableProperties.MENUS_ID + '=a.' + tableProperties.MENUITEMS_MENUID + ' WHERE ' +
        tableProperties.MENUS_RESTAURANTID + ' = ?', [restaurantId], createMenuItemFromSingleResult,

        function(menuItem) {

            callback(menuItem);
        
        });
}

function createMenuItemFromSingleResult(result)
{   
    if (result == null || typeof result == 'undefined') { callback(null); return; }
    return factory.createMenuItem(result[tableProperties.MENUITEMS_ID], result[tableProperties.MENUITEMS_MENUID],
        result[tableProperties.MENUITEMS_NAME], result[tableProperties.MENUITEMS_OBJECTTYPE],
        result[tableProperties.MENUITEMS_MENUTYPE], result[tableProperties.MENUITEMS_DESCRIPTION],
        result[tableProperties.MENUITEMS_PICTURE], result[tableProperties.MENUITEMS_PRICE]);
}

exports.addNewMenuItem = addNewMenuItem;
exports.updateMenuItem = updateMenuItem;
exports.findMenuItemById = findMenuItemById;
exports.findAllMenuItemsFromMenuId = findAllMenuItemsFromMenuId;
exports.findAllMenuItemsFromRestaurant = findAllMenuItemsFromRestaurant;
exports.findAllMenuItemsFromRestaurantId = findAllMenuItemsFromRestaurantId;
exports.createMenuItemFromSingleResult = createMenuItemFromSingleResult;
