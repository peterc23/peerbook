var client = require('./dbGeneral.js');
var factory = require('../utils/objectFactory');
var tableProperties = require('../resources/tableProperties.js');

function retrieveRestaurantFeatures(restaurantId, callback)
{
    if (restaurantId == null || typeof restaurantId == 'undefined') { callback(null); return; }
    client.executeFindMultipleQuery('SELECT * FROM ' + tableProperties.FEATURES_TABLE + ' WHERE '
        + tableProperties.FEATURES_RESTAURANTID + ' = ?', [restaurantId], createFeatureFromSingleResult, function(features) {
        callback(features);
    });
}

function createFeatureFromSingleResult(result)
{
    if (result == null || typeof result == 'undefined')
    {
        return;
    }
    return factory.createFeatureItem(result[tableProperties.FEATURES_ID], result[tableProperties.FEATURES_NAME],
                                    result[tableProperties.FEATURES_DESCRIPTION], result[tableProperties.FEATURES_TAGLINE],
                                    result[tableProperties.FEATURES_IMAGE], result[tableProperties.FEATURES_MENUITEMID],
                                    result[tableProperties.FEATURES_RESTAURANTID]);
}

exports.retrieveRestaurantFeatures = retrieveRestaurantFeatures;