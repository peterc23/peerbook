var dao = require('../db/DAOLayer.js');
var errCodes = require('../resources/errorCodes.js');
var tableProperties = require('../resources/tableProperties.js');
var factory = require('../utils/objectFactory.js');


function join (req,res)
{
    try{
        var peer = req.body;

        if(typeof peer[tableProperties.PEERS_HOSTNAME] == 'undefiend' || peer[tableProperties.PEERS_PORT] == 'undefined'){
            res.send(errCodes.ERR_PEER_NOT_SPECIFIED, 400);
            return;
        }else{
            peer[tableProperties.PEERS_STATUS] = tableProperties.PEERS_CONNECTED;
            dao.retrievePeerInfo(peer, function(peerInfo){
                if (typeof peerInfo == 'undefined' || peerInfo == null){
                    dao.insertNewPeer(peer, function(err){
                        if (err == null || typeof err == 'undefined'){
                            res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
                            return;
                        }else{
                            dao.retrievePeerInfo(peer, function(newPeerInfo){
                                console.log("AMIGOS");
                                console.log(newPeerInfo);
                                var returnObj = factory.convertToJava(tableProperties.OBJECT_TYPE_PEER, newPeerInfo);
                                res.send(returnObj, 200);
                                return;
                            });
                        }
                    });
                }else{
                    peerInfo[tableProperties.PEERS_STATUS] = tableProperties.PEERS_CONNECTED;
                    dao.updatePeerStatusById(peerInfo, function(newPeerInfo){
                        if (typeof peerInfo == 'undefined' || peerInfo == null){
                            res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
                            return;
                        }else{
                            var returnObj = factory.convertToJava(tableProperties.OBJECT_TYPE_PEER, peerInfo);
                            res.send(returnObj, 200);
                            return;
                        }
                    });
                }
            });
        }
    }catch(err){
        console.log(errCodes.ERR_PEER_NOT_INSERTEDerr);
        res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
    }
}

function leave (req,res)
{
    try{
        var peer = req.body;
        peer[tableProperties.PEERS_STATUS] = tableProperties.PEERS_DISCONNECTED;
        dao.updatePeerStatusById(peer, function(newPeerInfo){

            if(newPeerInfo == null || typeof newPeerInfo == 'undefined'){
                res.send(errCodes.ERR_PEER_NOT_SPECIFIED, 400);
                return;
            } else {
                res.send("Success", 200);
                return;
            }
        });
    }catch(err){
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function getStatus (req, res){
    try{
        dao.getAllPeerInfo(function(peerInfoList){
           if(peerInfoList == null || typeof peerInfoList == 'undefined'){
               res.send(errCodes.ERR_CANNOT_RETRIEVE_PEERS, 400);
               return;
           }else{
               var returnList = [];
               for(var i=0; i< peerInfoList.length; i++){
                   if (peerInfoList[i][tableProperties.PEERS_STATUS] == tableProperties.PEERS_CONNECTED){
                       var fileObj = factory.convertToJava(tableProperties.OBJECT_TYPE_PEER, peerInfoList[i]);
                        returnList.push(fileObj);
                   }
               }
               var returnObj = {};
               returnObj[tableProperties.OBJECT_PEER_LIST] = returnList;
               returnObj = factory.convertToJava(tableProperties.OBJECT_TYPE_PEER_LIST, returnObj);
                   res.send(returnObj, 200);

           }
        });
    }catch(err){
        console.log(errCodes.ERR_CANNOT_RETRIEVE_PEERS);
        console.log(err);
        res.send(errCodes.ERR_CANNOT_RETRIEVE_PEERS, 400);
    }
}

exports.join = join;
exports.leave = leave;
exports.getStatus = getStatus;