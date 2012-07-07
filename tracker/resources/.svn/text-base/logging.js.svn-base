var loggly = require('loggly');
var properties = require('./properties.js')
var token = properties.LOGGLY_TOKEN;
var config = {
  subdomain: properties.LOGGLY_SUBDOMAIN,
  auth: {
    username: properties.LOGGLY_USERNAME,
    password: properties.LOGGLY_PASSWORD
  }
};

var client = loggly.createClient(config);


function info(message, data){
console.log('INFO: ' + message + ': ' + data);
client.log(token, 'INFO: ' + message + ': ' + data);
}

function warning(message, data){
console.log('WARN: ' + message + ': ' + data);
client.log(token, 'WARN: ' + message + ': ' + data);
}

function error(message, data){
console.log('ERROR: ' + message + ': ' + data);
client.log(token, 'ERROR: ' + message + ': ' + data);
}

function fatal(message, data){
console.log('FATAL: ' + message + ': ' + data);
client.log(token, 'FATAL: ' + message + ': ' + data);
}

exports.info = info;
exports.warning = warning;
exports.error = error;
exports.fatal = fatal;