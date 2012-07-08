var dbFileSystem = require('./dbFileSystem.js');
var dbMenuItems = require('./dbMenuItems.js');
var dbRestaurant = require('./dbRestaurant.js');

//File System
exports.insertNewFile = dbFileSystem.insertNewFile;
exports.retrieveFileInfo = dbFileSystem.retrieveFileInfo;
exports.deleteFile = dbFileSystem.deleteFile;


//Users
exports.retrieveUserDetails = dbUsers.retrieveUserDetails;
exports.addNewUserDetails = dbUsers.addNewUserDetails;
exports.updateUserDetails = dbUsers.updateUserDetails;

//Friends
exports.insertFriendDetails = dbFriends.insertFriendDetails;
exports.retrieveFriendships = dbFriends.retrieveFriendships;

//EatActions
exports.saveEatActionPreFacebookPost = dbEatAction.saveEatActionPreFacebookPost;
exports.getEatAction = dbEatAction.getEatAction;
exports.getEatActionbyId = dbEatAction.getEatActionbyId;

//MenuTypes
exports.retrieveRestaurantMenuTypes = dbMenuTypes.retrieveRestaurantMenuTypes;