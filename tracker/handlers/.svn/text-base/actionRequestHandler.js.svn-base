var dao = require('../db/DAOLayer.js');
var log = require('../resources/logging.js');
var errCodes = require('../resources/errorCodes.js');
var fbAPI = require('../API/FacebookAPI.js');
var factory = require('../utils/objectFactory.js');
var tableProperties = require('../resources/tableProperties.js');
var API = require('../resources/API.js');
var s3 = require('../API/S3Picture.js');
var properties = require('../resources/properties.js');
var fbAction = require('../API/FacebookActions.js');

function eatAction(req, res)
{
    try{
        var jsonObj = req.body;
        if (jsonObj == null || typeof jsonObj == 'undefined'){
            log.info("Eat Action Request NULL", req.body);
        }else{
            dao.findMenuItemById(jsonObj[API.API_EATACTIONS_MENUITEMID], function(menuItem){

                if(menuItem == null || typeof menuItem == 'undefined'){
                    log.error(errCodes.INVALID_MENU_ITEM, menuItem);
                    res.send(errCodes.INVALID_MENU_ITEM, 400);
                }else{
                    var eatActionObject = factory.fbEatActionObject(jsonObj[API.API_EATACTIONS_MENUITEMID],
                        jsonObj[API.API_EATACTIONS_RESTAURANTID], jsonObj[API.API_EATACTIONS_TAGFRIENDS],
                        jsonObj[API.API_EATACTIONS_DESCRIPTION], jsonObj[API.API_EATACTIONS_MENUTYPE],
                        jsonObj[API.API_EATACTIONS_FBTOKEN], jsonObj[API.API_EATACTIONS_IMGURL]);
                    if (eatActionObject[tableProperties.EATACTIONS_FBIMGURL] == null ||
                        typeof eatActionObject[tableProperties.EATACTIONS_FBIMGURL] == 'undefined') {
                        eatActionObject[tableProperties.EATACTIONS_FBIMGURL] = properties.DEFAULT_EAT_PIC;
                        fbAction.postMenuItemToFaceBookAndSaveResults(eatActionObject, res);
                    }else{
                        s3.eatPicture(eatActionObject[tableProperties.EATACTIONS_FBIMGURL], eatActionObject[tableProperties.EATACTIONS_ID], function(imageUrl){
                            eatActionObject[tableProperties.EATACTIONS_FBIMGURL] = imageUrl;
                            fbAction.postMenuItemToFaceBookAndSaveResults(eatActionObject, res);
                        });

                    }
                }


                /*var postData = {
                    menuItemId : jsonObj.menuItemId,
                    restaurantId : jsonObj.restaurantId,
                    tagFriends : jsonObj.tagFriends,
                    description : jsonObj.description,
                    menuType : menuItem.menuType,
                    facebookAccessToken : jsonObj.facebookAccessToken,
                    originalImageUrl : (req && req.files && req.files.upload ? req.files.upload.path : null)
                };*/


            });

        }

    }catch(err){
        log.error("Error when retrieving Menu", err);
        res.send(errCodes.ERR_CODE_400, 400);
    }
}


function getFacebookObject(req, res) {
    log.info("GetFacebookObject request called");
    console.log("id = " + req.params.objectId);

    if (!req || !req.body || !req.params.objectId)
    {
        res.send(400);
        return;
    }

    var objectId = req.params.objectId;

    dao.getEatActionbyId(objectId, function(eatAction){
        if (!eatAction)
        {
            log.error("Eat Action is null, " + objectId);
            res.send(400);
            return;
        }

        dao.findMenuItemById(eatAction[tableProperties.EATACTIONS_MENUITEMID], function(menuItem){
            if (menuItem)
            {
                var body = '<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US">' +
                    '<head>' +
                    '<meta property="fb:app_id" content="'+properties.FB_APP_ID+'" />' +
                    '<meta property="og:type" content="'+properties.FB_APP_NAMESPACE+':entree" />' +
                    '<meta property="og:title" content="'+menuItem[tableProperties.MENUITEMS_NAME]+'" />' +
                    '<meta property="og:image" content="'+eatAction[tableProperties.EATACTIONS_FBIMGURL]+'" />' +
                    '<meta property="og:description" content="'+eatAction[tableProperties.EATACTIONS_DESCRIPTION]+'" />' +
                    '</head>' +
                    '</html>';

                res.send(body, {"Content-Type": "text/html"}, 200);
                log.info("GetFacebookObject Function returned meta tag file successful","");
            }
            else
            {
                log.fatal("CANNOT RETURN MENU ITEM FOR GET FACEBOOK OBJECT REQUEST, cannot find menu item "+objectId+" in database","");
                res.send(400);
            }
        });
    });
}

exports.getFacebookObject = getFacebookObject;
exports.eatAction = eatAction;