var dao = require('../db/DAOLayer.js');
var errCodes = require('../resources/errorCodes.js');


function read (req,res)
{
    try{
        dao.findAllMenuItemsFromRestaurantId(req.params.restaurantId, function(menuItemList){

            if(menuItemList == null || typeof menuItemList == 'undefined'){
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

function write (req,res)
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

function insertFile (req,res)
{
    try{
        dao.insertNewFile(req.params, function(info){
            //TODO: add relationship
                dao.retrieveFileInfo(req.params, function(fileInfo){
                    if(fileInfo == null || typeof fileInfo == 'undefined'){
                        log.info(errCodes.ERR_FILE_NOT_FOUND, req.params.restaurantId);
                        res.send(errCodes.ERR_FILE_NOT_FOUND, 400);
                    } else {
                        res.send(fileInfo, 200);
                    }
                });
        });
    }catch(err){
        console.log(errCodes.ERR_FILE_NOT_FOUND, err);
        res.send(errCodes.ERR_FILE_NOT_FOUND, 400);
    }
}

function deleteFile (req,res)
{
    try{
        dao.deleteFile(req.params, function(err){
            //TODO: delete relationsihps
            if(err == null || typeof err == 'undefined'){
                res.send("File cannot be found", 400);
            } else {
                res.send("File Deleted", 200);
            }
        });
    }catch(err){
        res.send("File cannot be found error", 400);
    }
}
/*
function fileReieved(req, res){
    try{
        dao.updateFileRecieved(req.params, funciton(err){

        });
    }catch(err){
        console.log(err);
        res.send("File cannot be found error", 400);
    }
}
*/
exports.insertFile = insertFile;
exports.deleteFile = deleteFile;
exports.read = read;
exports.write = write;
//exports.fileReieved = fileReieved;