var dao = require('../db/DAOLayer.js');
var log = require('../resources/logging.js');
var errCodes = require('../resources/errorCodes.js');


function join (req,res)
{
    try{
        var peer = req.body;

        if(peer.address == null || typeof peer.address == 'undefiend' || peer.port == null || peer.port == 'undefined'){
            res.send()
        }

        dao.findAllMenuItemsFromRestaurantId(req.params.restaurantId, function(menuItemList){

            if(menuItemList == null || typeof menuItemList == 'undefined'){
                console.log("Retrieve Restaurant Menu null from DB: ", req.params);
                res.send(errCodes.ERR, 400);
            } else {
                res.send(menuItemList, 200);
            }
        });
    }catch(err){
        log.error("Error when retrieving Restaurant Menus", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function leave (req,res)
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

exports.join = join;
exports.leave = leave;