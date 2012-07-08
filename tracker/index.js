var express = require('express');
var peersRequestHandler = require('./handlers/peersRequestHandler.js');
var fileSystemRequestHandler = require('./handlers/fileSystemRequestHandler.js');
var properties = require('./resources/properties.js');

// create server
var app = express.createServer();
// configurations
app.configure(function() {
  app.use(express.bodyParser({
    uploadDir: './tmp',
    keepExtensions: true
  }));
  app.use(express.methodOverride());
  app.use(app.router);
  app.register('.html', require('jade'));
  app.use(express.static(__dirname + '/views'));
});


//handler for Peers
app.post("/api/join", peersRequestHandler.join);
app.post("/api/leave", peersRequestHandler.leave);

//handler for Files
app.post("/api/insert", fileSystemRequestHandler.insert);
app.post("/api/delete", fileSystemRequestHandler.delete);
app.post("/api/read", fileSystemRequestHandler.read);
app.post("/api/write", fileSystemRequestHandler.write);


// decalre routing

//app.get("/api/menu/:restaurantId", menuRequestHandler.openMenu);
//app.post("/api/menu/:restaurantId", menuRequestHandler.openRestaurant);
app.get("/api/menu/:restaurantId/search", menuRequestHandler.findAllItems);
app.get("/api/menu/:restaurantId/menus", menuRequestHandler.retrieveRestaurantMenus); //retrieve Menus
app.post("/api/menu/:restaurantId/menus", menuRequestHandler.retrieveRestaurantMenusF); //retrieve menus with friends

app.get("/api/menu/:restaurantId/subMenu/:menuId", menuRequestHandler.retrieveRestaurantMenuType);//retrieve specfic menu type

app.get("/api/menu/:restaurantId/menus/:menuId", menuRequestHandler.retrieveSpecificMenu);//retrieve Specific Menu based on ID

app.get("/api/menu/:restaurantId/featured", menuRequestHandler.retrieveRestaurantFeatures);// retrieve Features

app.get("/api/menu/:restaurantId/coupons", menuRequestHandler.retrieveRestaurantCoupons); //retrieve Coupons

app.post("/api/fblogin", userRequestHandler.userLoginRequest); //save User Info

app.post("/api/action/eatItem", actionRequestHandler.eatAction); //handle eat action

app.post("/facebookmenu", function (req, res) {
    res.sendfile("views/facebookmenu/login.html");
});

app.get("/getFacebookObject/:objectId", actionRequestHandler.getFacebookObject);

// stub api calls
/*
app.get("/api/menus/:restaurantId", testhandler.menus);
app.get("/api/coupons/:restaurantId", testhandler.coupons);
app.get("/api/featured/:restaurantId", testhandler.featured);
*/
// start the server
app.listen(properties.APP_PORT);
console.log("server started");

