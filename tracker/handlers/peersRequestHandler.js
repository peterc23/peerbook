var dao = require('../db/DAOLayer.js');
var errCodes = require('../resources/errorCodes.js');
var tableProperties = require('../resources/tableProperties.js');


function join (req,res)
{
    try{
        var peer = req.body;

        if(typeof peer[tableProperties.PEERS_HOSTNAME] == 'undefiend' || peer[tableProperties.PEERS_PORT] == 'undefined'){
            res.send(errCodes.ERR_PEER_NOT_SPECIFIED, 400);
        }else{
            peer[tableProperties.PEERS_STATUS] = tableProperties.PEERS_CONNECTED;
            dao.retrievePeerInfo(peer, function(peerInfo){
                if (typeof peerInfo == 'undefined' || peerInfo == null){
                    dao.insertNewPeer(peer, function(err){
                        if (err == null || typeof err == 'undefined'){
                            res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
                        }else{
                            dao.retrieveFileInfo(peer, function(newPeerInfo){
                                res.send(newPeerInfo, 200);
                            });
                        }
                    });
                }else{
                    peerInfo[tableProperties.PEERS_STATUS] = tableProperties.PEERS_CONNECTED;
                    dao.updatePeerStatusById(peerInfo, function(newPeerInfo){
                        if (typeof peerInfo == 'undefined' || peerInfo == null){
                            res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
                        }else{
                            res.send(peerInfo, 200);
                        }
                    })
                }
            })
        }
    }catch(err){
        console.log(errCodes.ERR_PEER_NOT_INSERTEDerr);
        res.send(errCodes.ERR_PEER_NOT_INSERTED, 400);
    }
}

function leave (req,res)
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
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function getStatus (req, res){
    try{
        dao.getAllPeerInfo(function(peerInfoList){
           if(typeof peerInfoList == 'undefined'){
               res.send(errCodes.ERR_CANNOT_RETRIEVE_PEERS, 400);
           }else{
               var returnList = [];
               if (typeof peerInfoList == 'undefined'){
                   res.send(errCodes.ERR_CANNOT_RETRIEVE_PEERS, 400);
               }else{
                   for(var i=0; i< peerInfoList.length; i++){
                       if (peerInfoList[i][tableProperties.PEERS_STATUS] == tableProperties.PEERS_CONNECTED){
                           returnList.push(peerInfoList[i]);
                       }
                   }
                   res.send(returnList, 200);
               }
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