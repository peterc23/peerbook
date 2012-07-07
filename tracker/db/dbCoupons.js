var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveRestaurantCoupons(restaurantId, callback)
{
    if (restaurantId == null || typeof restaurantId == 'undefined') {
        callback(null);
    }else{
        client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.COUPONS_TABLE + ' WHERE '
            + tableProperties.COUPONS_RESTAURANTID + ' = ?', [restaurantId], createCouponFromSingleResult, function(coupons) {
            callback(coupons);
        });
    }
}

function createCouponFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }else{
        return factory.createCouponItem(result[tableProperties.COUPONS_ID], result[tableProperties.COUPONS_NAME],
                                    result[tableProperties.COUPONS_DESCRIPTION], result[tableProperties.COUPONS_IMAGE],
                                    result[tableProperties.COUPONS_CODE], result[tableProperties.RESTAURANTS_ID]);
    }
}

exports.retrieveRestaurantCoupons = retrieveRestaurantCoupons;