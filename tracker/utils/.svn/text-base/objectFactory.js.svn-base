var tableProperties = require('../resources/tableProperties.js');

function createNewUser(facebookId, name, email, fbToken, picture)
{
    return createUser(null, facebookId, name, email, fbToken, picture);
}

function createUser(facebookId, name, email, fbToken, image)
{
    if (facebookId == null || typeof facebookId == 'undefined' || name == null ||
        typeof name == 'undefined' || fbToken == null ||
        typeof fbToken == 'undefined')
    {
        return;
    }else{
        var user = {};

        user[tableProperties.USERS_ID] = facebookId;
        user[tableProperties.USERS_NAME] = name;
        user[tableProperties.USERS_EMAIL] = email;
        user[tableProperties.USERS_FBTOKEN] = fbToken;
        user[tableProperties.USERS_IMAGE] = image;

        return user;
    }
}

function createMenuItem(id, menuId, name, objectType, menuType, description, picture, price)
{
    var menuItem = {};

    menuItem[tableProperties.MENUITEMS_ID] = id;
    menuItem[tableProperties.MENUITEMS_MENUID] = menuId;
    menuItem[tableProperties.MENUITEMS_NAME] = name;
    menuItem[tableProperties.MENUITEMS_OBJECTTYPE] = objectType;
    menuItem[tableProperties.MENUITEMS_MENUTYPE] = menuType;
    menuItem[tableProperties.MENUITEMS_DESCRIPTION] = description;
    menuItem[tableProperties.MENUITEMS_PICTURE] = picture;
    menuItem[tableProperties.MENUITEMS_PRICE] = price;
    
    return menuItem;
}

function createRestaurant(id, facebookPlaceId, name, picture, link, city, state, country, latitude, logitude)
{
    var restaurant = {};
    
    restaurant.id = id;
    restaurant.facebookPlaceId = facebookPlaceId;
    restaurant.name = name;
    restaurant.picture = picture;
    restaurant.link = link;
    restaurant.city = city;
    restaurant.state = state;
    restaurant.country = country;
    restaurant.latitude = latitude;
    restaurant.longitude = longitude;
    
    return restaurant;

}

function createEatAction(id, restaurantId, menuItemId, eatActionId, userId, tags, description, date, picture, posted)
{
     var eatAction = {};
     
     eatAction.id = id;
     eatAction.restaurantId = restaurantId;
     eatAction.menuItemId = menuItemId;
     eatAction.userId = userId;
     eatAction.tags = tags;
     eatAction.description = description;
     eatAction.date = date;
     eatAction.picture = picture;
     eatAction.posted = posted;
     
     return eatAction;
}

function createRestaurantPage(id, restaurantId, menu)
{
    var restaurantPage = {};
    
    restaurantPage.id = id;
    restaurantPage.restaurantId = restaurantId;
    restaurantPage.menu = menu;

    return restaurantPage;
}

function createCouponItem(id, name, description, image, code, restaurantId)
{
    if (id == null || typeof id == 'undefined' || name == null || typeof name == 'undefined' || description == null ||
        typeof description == 'undefined' || image == null || typeof image == 'undefined' || code == null ||
        typeof code == 'undefined' || restaurantId == null || typeof restaurantId == 'undefined')
    {
        return;
    }else{
        var coupon = {};

        coupon[tableProperties.COUPONS_ID] = id;
        coupon[tableProperties.COUPONS_NAME] = name;
        coupon[tableProperties.COUPONS_DESCRIPTION] = description;
        coupon[tableProperties.COUPONS_IMAGE] = image;
        coupon[tableProperties.COUPONS_CODE] = code;
        coupon[tableProperties.COUPONS_RESTAURANTID] = restaurantId;

        return coupon;
   }

}

function createFeatureItem(id, name, description, tagline, image, menuitemId, restaurantId)
{
    if (id == null || typeof id == 'undefined' || name == null || typeof name == 'undefined' || description == null ||
        typeof description == 'undefined' || tagline == null || typeof tagline == 'undefined' || image == null ||
        typeof image == 'undefined' || menuitemId == null || typeof menuitemId == 'undefined' || restaurantId == null ||
        typeof restaurantId == 'undefined')
    {
        return;
    }else{
        var feature = {};

        feature[tableProperties.FEATURES_ID] = id;
        feature[tableProperties.FEATURES_NAME] = name;
        feature[tableProperties.FEATURES_DESCRIPTION] = description;
        feature[tableProperties.FEATURES_TAGLINE] = tagline;
        feature[tableProperties.FEATURES_IMAGE] = image;
        feature[tableProperties.FEATURES_MENUITEMID] = menuitemId;
        feature[tableProperties.FEATURES_RESTAURANTID] = restaurantId;

        return feature;
    }

}

function createMenu(id, menuName, restaurantId, description)
{
    if (id == null || typeof id == 'undefined' || menuName == null || typeof menuName == 'undefined' || restaurantId == null ||
        typeof restaurantId == 'undefined' || description == null || typeof description == 'undefined')
    {
        return;
    }else{
        var menu = {};

        menu[tableProperties.MENUS_ID] = id;
        menu[tableProperties.MENUS_MENUNAME] = menuName;
        menu[tableProperties.MENUS_RESTAURANTID] = restaurantId;
        menu[tableProperties.MENUS_DESCRIPTION] = description;

        return menu;
    }
}

function createMenuType(id, menuTypeName, menuTypeImg, menuTypeMenuId, menuTypeFoodType){
    if(id == null || typeof id == 'undefined' || menuTypeName == null || typeof menuTypeName == 'undefined' ||
        menuTypeImg == null || typeof menuTypeImg == 'undefined' || menuTypeMenuId == null || typeof menuTypeMenuId == 'undefined'
        || menuTypeFoodType == null || typeof menuTypeFoodType == 'undefined'){
        return;
    }else{
        var menuType = {};

        menuType[tableProperties.MENUTYPE_ID] = id;
        menuType[tableProperties.MENUTYPE_MENUNAME] = menuTypeName;
        menuType[tableProperties.MENUTYPE_MENUIMG] = menuTypeImg;
        menuType[tableProperties.MENUTYPE_MENUID] = menuTypeMenuId;
        menuType[tableProperties.MENUTYPE_FOODTYPE] = menuTypeFoodType;

        return menuType;
    }
}

function createFriend(id, friendId)
{
    if(id == null || typeof id == 'undefined' || friendId == null || typeof friendId == 'undefined')
    {
        return;
    }else{
        var friend = {};

        friend[tableProperties.FRIENDS_ID] = id;
        friend[tableProperties.FRIENDS_FRIENDID] = friendId;

        return friend;
    }

}

function fbEatActionObject(menuItemId, restaurantId, tagFriends, description, menuType, fbToken, imgURL)
{
    if (menuItemId == null || typeof menuItemId == 'undefined' || restaurantId == null || typeof restaurantId == 'undefined'
        || menuType == null || typeof menuType == 'undefined' || fbToken == null || typeof fbToken == 'undefined')
    {
        return;
    }else{
        var eatActionObject = {};

        eatActionObject[tableProperties.EATACTIONS_MENUITEMID] = menuItemId;
        eatActionObject[tableProperties.EATACTIONS_RESTAURANTID] = restaurantId;
        eatActionObject[tableProperties.EATACTIONS_FBTAGFRIENDS] = tagFriends;
        eatActionObject[tableProperties.EATACTIONS_DESCRIPTION] = description;
        eatActionObject[tableProperties.EATACTIONS_MENUTYPE] = menuType;
        eatActionObject[tableProperties.EATACTIONS_FBTOKEN] = fbToken;
        eatActionObject[tableProperties.EATACTIONS_FBIMGURL] = imgURL;
        eatActionObject[tableProperties.EATACTIONS_DATE] = Date.now();

        return eatActionObject;
    }
}

function dbEatActionObject(id, menuItemId, userId, tags, description, date, imgUrl){
    if (id == null || typeof id == 'undefined' || menuItemId == null || typeof menuItemId == 'undefined' ||
        userId == null || typeof userId == 'undefined' || date == null || typeof date == 'undefined')
    {
        return;
    }else{
        var eatActionObject = {};

        eatActionObject[tableProperties.EATACTIONS_ID] = id;
        eatActionObject[tableProperties.EATACTIONS_MENUITEMID] = menuItemId;
        eatActionObject[tableProperties.EATACTIONS_USERID] = userId;
        eatActionObject[tableProperties.EATACTIONS_TAGS] = tags;
        eatActionObject[tableProperties.EATACTIONS_DESCRIPTION] = description;
        eatActionObject[tableProperties.EATACTIONS_DATE] = date;
        eatActionObject[tableProperties.EATACTIONS_FBIMGURL] = imgUrl;

        return eatActionObject;
    }

}

exports.createRestaurantPage = createRestaurantPage;
exports.createUser = createUser;
exports.createMenu = createMenu;
exports.createMenuType = createMenuType;
exports.createMenuItem = createMenuItem;
exports.createRestaurant = createRestaurant;
exports.createEatAction = createEatAction;
exports.createCouponItem = createCouponItem;
exports.createFeatureItem = createFeatureItem;
exports.createFriend = createFriend;
exports.fbEatActionObject = fbEatActionObject;
exports.dbEatActionObject = dbEatActionObject;

