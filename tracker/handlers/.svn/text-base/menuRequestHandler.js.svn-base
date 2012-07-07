var dao = require('../db/DAOLayer.js');
var log = require('../resources/logging.js');
var errCodes = require('../resources/errorCodes.js');

//*******This function retrieves all the Menus and the Items of each menu for restaurant****
function findAllItems (req,res)
{
    try{
        dao.findAllMenuItemsFromRestaurantId(req.params.restaurantId, function(menuItemList){

            if(menuItemList == null || typeof menuItemList == 'undefined'){
                log.info("Retrieve Restaurant Menu null from DB, restaurant Id: ", req.params.restaurantId);
                res.send(errCodes.ERR_CODE_400, 400);
            } else {
                res.send(menuItemList, 200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurant Menus", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}


function retrieveSpecificMenu (req, res)
{
    try{
        dao.findAllMenuItemsFromMenuId(req.params.menuId, function(menuItems){

            if(menuItems == null || typeof menuItems == 'undefined'){
                log.info("Retrieve MenuItem 400:  ", req.params.menuId);
            }else{
                res.send(menuItems, 200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Menu", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}


//******* This function retrieves all the coupons for a particular restaurant *******
function retrieveRestaurantCoupons (req,res)
{
    try{
        dao.retrieveRestaurantCoupons(req.params.restaurantId, function(coupons){

            if (coupons == null || typeof coupons == 'undefined'){
                log.info("Retrieve Restaurant Coupons null from DB, restaurant Id:", req.params.restaurantId);
                res.send(errCodes.ERR_CODE_400, 400);
            } else {
                res.send(coupons, 200);
            }
        });
    }catch(err){
            log.error("Error when retrieving Restaurant Coupons", err);
            res.send(errCodes.ERR_CODE_400, 400);
    }
}

//**** This function retrieves just the list of Menus for a particular restaurant*****
function retrieveRestaurantMenus (req, res)
{
    try{
        dao.retrieveRestaurantMenus(req.params.restaurantId, function(menus){

            if (menus == null || typeof menus == 'undefined'){
                log.info("Retrieve Restaurant Menus 400, restaurant Id: ", req.params.restaurantId);
                res.send(errCodes.ERR_CODE_400, 400);
            }else{
                res.send(menus,200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurang Menus", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function retrieveRestaurantMenuType (req, res){

    try{
        dao.retrieveRestaurantMenuTypes(req.params.menuId, function(menuTypes){

            if (menuTypes == null || typeof menuTypes == 'undefined'){
                log.info("Retrieve Restaurant MenuType 400, menu Id: ", req.params.menuId);
                res.send(errCodes.ERR_CODE_400, 400);
            }else{
                res.send(menuTypes,200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurang Menus", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}
//**** This function retrieves just the list of Menus for a particular restaurant with friends*****
function retrieveRestaurantMenusF (req, res)
{
    try{
        dao.retrieveRestaurantMenus(req.params.restaurantId, function(menus){

            if (menus == null || typeof menus == 'undefined'){
                log.info("Retrieve Restaurant Menus 400, restaurant Id: ", req.params.restaurantId);
                res.send(errCodes.ERR_CODE_400, 400);
            }else{
                res.send(menus,200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurang Menus", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function retrieveRestaurantFeatures (req, res)
{
    try{
        dao.retrieveRestaurantFeatures(req.params.restaurantId, function(features){

            if (features == null || typeof features == 'undefined'){
                log.info("Retrieve Restaurant Features 400, restaurant Id: ", req.params.restaurantId);
                res.send(errCodes.ERR_CODE_400, 400);
            }else{
                res.send(features,200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurant Features", err);
        res.send(errCodes.ERR_CODE_400, 400);

    }
}

exports.findAllItems = findAllItems;
exports.retrieveSpecificMenu = retrieveSpecificMenu;
exports.retrieveRestaurantCoupons = retrieveRestaurantCoupons;
exports.retrieveRestaurantMenus = retrieveRestaurantMenus;
exports.retrieveRestaurantMenusF = retrieveRestaurantMenusF;
exports.retrieveRestaurantMenuType = retrieveRestaurantMenuType;
exports.retrieveRestaurantFeatures = retrieveRestaurantFeatures;