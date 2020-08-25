var AV = require('leanengine');
var _ = require('underscore');



//5b9bc3450b61600039601ecb

//{'oldConvId':'5bab3b4c570c3500675d8a99','teamId':'5bab3b4c44d904005ead3f28','userHisId':'5bab3b5b808ca400720f85fd'}

AV.Cloud.define('quitOldTeam',function(request){
    var oldConvId = request.params.oldConvId;//会话Id
    var teamId = request.params.teamId;//teamId
    var userObjectId = request.currentUser.get('objectId');
    var userHisId = request.params.userHisId;

    console.log(userObjectId);
    //只有不是房主才可以执行
    var conv = AV.Object.createWithoutData('_Conversation', oldConvId);
    var team = AV.Object.createWithoutData('tb_Team', teamId);
    var history = AV.Object.createWithoutData('tb_User_History', userHisId);
    return history.destroy().then(function(res){
        team.remove('participants',userObjectId);
        return team.save();
    }).then(function (todo) {
        conv.remove('m',userObjectId);
        return conv.save();
    }).then(function(todo){
        return todo;
    }).catch(function(err){
        return err;
    });
});