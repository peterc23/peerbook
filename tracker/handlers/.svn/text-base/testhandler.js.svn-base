var dao = require('../db/DAOLayer.js');
var fs = require('fs');
var async = require('async');

exports.apptest = function (req, res) 
{
    //console.log(req);
    console.log("gooooo");
    console.log(req.params.name);
    res.contentType('text/plain');
    res.send({name:"hihihihihihih"});
  
    //var id = '1';
    //dao.findAllMenuItemsFromRestaurantId(id, function (menuItemList){
    //   console.log(menuItemList); 
    //});
}
exports.restaurant = function (req,res)
{
console.log("gothere");
res.send({name:"this is it right here"});
}

exports.widgetsettings = function(req,res)
{ 
    async.parallel({
        leftwidget: function(callback){
            fs.readFile('./views/facebookmenu/couponwidget.html', 'utf-8', function(error, content) {
                callback(null, content);        
            });
        },
        rightwidget: function(callback){
            fs.readFile('../views/facebookmenu/guestfeedbackwidget.html', 'utf-8', function(error, content) {
                callback(null, content);       
            });
        },
        bottomwidget: function(callback){
            fs.readFile('../views/facebookmenu/newsfeedwidget.html', 'utf-8', function(error, content) {
                callback(null, content);
            });
        }},
        function (err, results) {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "X-Requested-With");
            res.header("Access-Control-Allow-Headers", "Content-Type");
            res.contentType('application/json');
            console.log(JSON.stringify(results));
            res.send('{widgets:'+JSON.stringify(results)+'}');
        }
    );
}

exports.menulist = function(req,res)
{
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    res.contentType('application/json');
    res.send('[{"name":"helloworld1"},{"name":"helloworld2"}]');
}

exports.coupons = function(req, res)
{
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    res.contentType('application/json');
    res.send('{coupons:[{"couponid":"id-123",image:"asdasd.jpg","facepile":["asd.png","asdasd.png"]},"friendcount":"25"}]}');
}

exports.menus = function(req, res)
{
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    res.contentType('application/json');
    res.send('{"menus":[{"id":"1","name":"Dinner Menu"},{"id":"2","name":"Lunch Menu"},{"id":"3","name":"Bar Menu"},{"id":"4","name":"Dessert Menu"},{"id":"5","name":"Kid\'s Menu"}]}');
}

exports.featured = function(req, res)
{
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    res.contentType('application/json');
    res.send('{"tagline":"Have you tried our ...","name":"Sirloin Oscar","description":"asdasd","friendcount":"123","picture":"asdasdasd.png"}');
}