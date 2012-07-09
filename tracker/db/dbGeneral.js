var mysql = require('mysql');
var properties = require('../resources/properties.js');

var client = mysql.createClient({
  host: properties.DB_HOST,
  port: properties.DB_PORT,
  database: properties.DB_SERVERNAME,
  user: properties.DB_USERNAME,
  password: properties.DB_PW
});

function executeFindSingleQuery(statement, parameters, builder, callback)
{
    //console.log("executing find single query statement: "+statement+", with parameters: "+parameters);
    client.query(
        statement, parameters, function(err, result, fields) {
            if (err) {
                console.log("DB Single Query: ", err);
                callback(null);
            }else{
                if (callback) callback(builder(result[0]));
            }
            //client.end();
        }
    );
}

function executeFindMultipleQuery(statement, parameters, builder, callback)
{
    //console.log("executing find multiple query statement: "+statement+", with parameters: "+parameters);
    client.query(
        statement, parameters, function(err, results, fields) {
            if (err) {
                console.log("DB Multiple Query: ", err);
                callback(null);
            }else{
                if (callback) callback(buildFromMultipleResult(results, builder));
            }
            //client.end();
        }
    );
}

function executeUpdateSingleQuery(statement, parameters, callback)
{
    //console.log("executing update query statement: "+statement+", with parameters: "+parameters);
    client.query(statement, parameters, function (err, info) {
            if (err) {
                console.log("DB Single Update: ", err);
                callback(err);
            }else{
                if (callback) callback("SUCCESS");
            }
            //client.end();
        }
    );
}

function executeInsertSingleQuery(statement, parameters, callback)
{
    console.log("executing inesrt single query statement: "+statement+", with parameters: "+parameters);
    client.query(statement, parameters, function (err, info) {
        if (err) {
            console.log("DB Single Insert: ",err);
            callback(err);
        }else{
            if (callback) callback(info);
        }
            //client.end();
        }
    );
}

function executeDeleteSingleQuery(statement, parameters, callback)
{
    console.log("executing delete single query statement: "+statement+", with parameters: "+parameters);
    client.query(statement, parameters, function (err, info) {
            if (err) {
                console.log("DB Single delete: ",err);
                callback(err);
            }else{
                if (callback) callback(info);
            }
            //client.end();
        }
    );
}

function buildFromMultipleResult(results, builder)
{
    if (builder == null || results == null || typeof results == 'undefined' || results.length == 0) return null;
    
    var list = [];
    
    for (var i = 0; i < results.length; i++)
    {
        list.push(builder(results[i]));
    }
    
    return list;
}

exports.client = client;
exports.executeFindSingleQuery = executeFindSingleQuery;
exports.executeFindMultipleQuery = executeFindMultipleQuery;
exports.executeUpdateSingleQuery = executeUpdateSingleQuery;
exports.executeInsertSingleQuery = executeInsertSingleQuery;
exports.executeDeleteSingleQuery = executeDeleteSingleQuery;