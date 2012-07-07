var log = require('../resources/logging.js');
var errCodes = require('../resources/errorCodes.js');
var fbLogin = require('../API/FacebookLogin.js');
var fbAPI = require('../API/FacebookAPI.js');
var fbFriends = require('../API/FacebookFriends.js');

function userLoginRequest (req,res)
{
    var fbToken = req.body.fbtoken;
    try{
        fbLogin.saveUserDetails(fbToken, function(fbId){
            if (fbId == null || typeof fbId == 'undefined'){
                log.info("Retrieve User Details null from DB, user Token:", fbToken);
                res.send(errCodes.ERR_CODE_400, 400);
            } else {
                res.send(errCodes.LOGIN_SUCCESS_CODE, 200);
                updateFriends(fbId, fbToken, function(code){
                    if(code == errCodes.FRIENDS_FAIL_CODE){
                        log.warning("Error retriving Freinds", errCodes.FRIENDS_FAIL_CODE);
                    }
                });
            }
        });
    }catch(err){
        log.error("Error when loging in User", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}

function updateFriends (id,accessToken, callback)
{
    try{
        fbFriends.updateFriends(id, accessToken, function(fbId, accessToken, code){
            console.log(code);
            callback(code)
        });
    }catch(err){
        log.error("Friends Update error", err);
        callback(errCodes.FRIENDS_FAIL_CODE);
    }
}

exports.userLoginRequest = userLoginRequest;