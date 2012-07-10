var dao = require('../db/DAOLayer.js');
var errCodes = require('../resources/errorCodes.js');
var tableProperties = require('../resources/tableProperties.js');
var factory = require('../utils/objectFactory.js');

function write (req,res)
{
    try{
        dao.updateFileInfo(req.body, function(info){

            if(info == null || typeof info == 'undefined'){
                console.log(errCodes.ERR_UPDATING_FILE);
                res.send(errCodes.ERR_UPDATING_FILE, 400);
            } else {
                res.send(errCodes.FILE_WRITE_SUCCESS, 200);
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

function getStatus (req, res){
    try{
        dao.getAllFileInfo(function(fileInfoList){
            if(fileInfoList == null || typeof fileInfoList == 'undefined'){
                res.send(errCodes.ERR_CANNOT_RETRIEVE_FILES, 400);
                return;
            }else{
                var returnList = [];
                for(var i=0; i< fileInfoList.length; i++){
                        var fileObj = factory.convertToJava(tableProperties.OBJECT_TYPE_FILE, fileInfoList[i]);
                        returnList.push(fileObj);
                    }
                }
                var returnObj = {};
                returnObj[tableProperties.OBJECT_FILE_LIST] = returnList;
                returnObj = factory.convertToJava(tableProperties.OBJECT_TYPE_FILE_LIST, returnObj);
                res.send(returnObj, 200);
        });
    }catch(err){
        console.log(errCodes.ERR_CANNOT_RETRIEVE_FILES);
        console.log(err);
        res.send(errCodes.ERR_CANNOT_RETRIEVE_FILES, 400);
    }
}


function test(req, res){
    console.log("testing screen");
    console.log(req.body);
    var jsonObject = {};
    jsonObject.abcd = "your name";
    jsonObject.hostname = "123";
    jsonObject.port = "12345";
    res.send(jsonObject, 200);
}

exports.insertFile = insertFile;
exports.deleteFile = deleteFile;
exports.write = write;
exports.test = test;
exports.getStatus = getStatus;
//exports.fileReieved = fileReieved;