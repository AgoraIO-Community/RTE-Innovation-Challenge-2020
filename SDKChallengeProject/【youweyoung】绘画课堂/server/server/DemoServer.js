//var fs = require('fs');
//var https = require('https');
var http = require('http');
var express = require('express');
var { RtcTokenBuilder, RtmTokenBuilder, RtcRole, RtmRole } = require('./index.js')

var PORT = 8000;

// Fill the appID and appCertificate key given by Agora.io
var appID = "c5002f33fc8341c994ca66e208e99855";
var appCertificate = "7c0b9924b5974f8084bf247aa39ede68";

// token expire time, hardcode to 3600 seconds = 1 hour
var expirationTimeInSeconds = 3600
var role = RtcRole.PUBLISHER

var app = express();
app.disable('x-powered-by');
app.set('port', PORT);

app.use(app.router);

var generateRtcToken = function (req, resp) {
    var currentTimestamp = Math.floor(Date.now() / 1000)
    var privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds
    var channelName = req.query.channelName;
    // use 0 if uid is not specified
    var uid = parseInt(req.query.uid) || 0
    if (!channelName) {
        return resp.status(400).json({ 'error': 'channel name is required' }).send();
    }
    console.log({ uid, channelName })

    var key = RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, role, privilegeExpiredTs);

    resp.header("Access-Control-Allow-Origin", "*")
    //resp.header("Access-Control-Allow-Origin", "http://ip:port")
    return resp.json({ 'key': key }).send();
};

app.get('/token', generateRtcToken);


http.createServer(app).listen(app.get('port'), function () {
    console.log('AgoraSignServer starts at ' + app.get('port'));
});

//https.createServer(credentials, app).listen(app.get('port') + 1, function() {
//    console.log('AgoraSignServer starts at ' + (app.get('port') + 1));
//});