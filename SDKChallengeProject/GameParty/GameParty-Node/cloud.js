var AV = require('leanengine');
var _ = require('underscore');
var sha1 = require('sha1');

var AgoraTokenGenerator = require('./AccessToken');
var AgoraDynamicKeyGenerator = require('./DynamicKey');


// var appID = "565813b5fefd4804a1303dc32d9e8a38";
// var appCertificate = "aea99f559732414eaafa71a5d9f79fe7";

//
var appID = "00d8c568abeb46e5b2a603df2555f28c";
var appCertificate = "2d5ca4f0e3604138aca2bdbe3a2ebe37";

var request = require('request');

var RongAppKey = "tdrvipksr9xq5";
var RongAppSerc = "DoQNHd7qZjgR";

var RongSDK = require('rongcloud-sdk')({
  appkey: RongAppKey,
  secret: RongAppSerc
});

const createNonceStr = function () {
  return Math.random().toString(36).substr(2, 15);
};
// 生成时间戳 秒值 借用微信的
const createTimestamp = function () {
  return parseInt(new Date().getTime() / 1000) + "";
};

const generateSHA1SignatureByHex = (appSecret, nonce, timestamp) => {
  const sha1Str = appSecret + nonce + timestamp;
  const SHA1 = sha1(sha1Str);
  return SHA1;
}


AV.Cloud.define('getRCIMToken', function (req, resp) {
  var userObjectId = req.currentUser.toJSON().objectId;
  var username = req.currentUser.toJSON().username;
  //var avatar = req.currentUser.toJSON().avatar;
  
  var User = RongSDK.User;
  var user = {
    id: userObjectId,
    name: username,
    //portrait: avatar.url?avatar.url:''
    portrait: 'logo.png'
  };
  User.register(user).then(result => {
    console.log(result);
    return resp.success(result);
  }, error => {
        // return resp.success(body);
    console.log(error);
  });


//   var nonce = createNonceStr;
//   var timestamp = createTimestamp;
//   var sign = generateSHA1SignatureByHex(RongAppSerc, nonce, timestamp);
//   console.log(sign);
});

/*
 * Hook函数* 创建组队后建立群聊
 */
AV.Cloud.afterSave('Group', function (request) {
  var title = request.object.get('title');
  var userObjectId = request.currentUser.toJSON().objectId;
  var objId = request.object.toJSON().objectId;

  var Group = RongSDK.Group;
  var group = {
    id:objId,
    name:title,
    members: [{id:userObjectId}]
  };
  console.log(group);
  Group.create(group).then(result => {
    console.log(result);
    // return resp.success(result);
  },error => {
    console.log(error);

  });
});



AV.Cloud.define('joinGroup', function (req, resp) {
  var userObjectId = req.currentUser.toJSON().objectId;

  var groupId = req.params.groupId;
  var groupName = req.params.title;
  
  var Group = RongSDK.Group;
  var user = {
    id: groupId,
    name: groupName,
    member: {id:userObjectId}
  };
  console.log("groupJoin === > ",user);
  Group.join(user).then(result => {
    console.log(result);
    return resp.success(result);
  }, error => {
        // return resp.success(body);
    console.log(error);
  });
});




AV.Cloud.define('getWXUserInfo', function (req, resp) {
  //code  
  var code = req.params.code.toString();

  const url = 'https://api.weixin.qq.com/sns/oauth2/access_token?appid=' + 'wxcd1d0b740f17ebe8' + '&secret=' + '59a0e2e5dbf1408f5770a5d446f9d0fe' + '&code=' + code + '&grant_type=' + 'authorization_code' + '&scope=snsapi_login,snsapi_userinfo';
  request(url, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      return resp.success(body);
    }
  })
});













AV.Cloud.define('getWXUserDetail', function (req, resp) {
  var access_token = req.params.access_token.toString();
  var openid = req.params.openid.toString();
  const url = 'https://api.weixin.qq.com/sns/userinfo?access_token=' + access_token + '&openid=' + openid;
  request(url, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      console.log(body)
      return resp.success(body);
    }
  })
});

/* 获取Key --- Web使用 传入userId和channelName*/
AV.Cloud.define('getDymicKey', function (req, resp) {

  var userId = req.currentUser.toJSON().userId;
  var channelName = req.params.channelName.toString();
  if (!channelName) {
    return resp.status(400).json({
        'error': 'channel name is required'
      })
      .send();
  }
  var ts = Math.round(new Date().getTime() / 1000);
  var rnd = Math.round(Math.random() * 100000000);
  var key = AgoraDynamicKeyGenerator.generate(appID, appCertificate, channelName, ts, rnd);
  return resp.success(key);
});

/* 获取Key --- Web使用 传入userId和channelName*/
AV.Cloud.define('getDymicToken', function (req, resp) {

  var userId = req.currentUser.toJSON().userId;
  var channelName = req.params.channelName.toString();
  if (!channelName) {
    return resp.status(400).json({
      'error': 'channel name is required'
    }).send();
  }
  var token = new AgoraTokenGenerator.AccessToken(appID, appCertificate, channelName, userId);
  console.log('token===>', token.build());
  return resp.success(token.build());

});

/*
AV.Cloud.define('queryUserDefault', function (request) {
  
  var query = new AV.Query('_User');
  query.doesNotExist('formTypes');
  query.limit(1000);
  query.find().then(function (results){
    console.log(results.length);
    var checksType = [];
    var type1 = AV.Object.createWithoutData('Check_Type', '5bdbb84875657100672e8081');
    var type2 = AV.Object.createWithoutData('Check_Type', '5bd81dd844d904005ee2cc01');
    var type3 = AV.Object.createWithoutData('Check_Type', '5bd81df29f54540066d0dc61');
    var type4 = AV.Object.createWithoutData('Check_Type', '5bdbb86dfb4ffe005d2ee6b6');
    var type5 = AV.Object.createWithoutData('Check_Type', '5bdbb8569f54540066da25a6');
    var type6 = AV.Object.createWithoutData('Check_Type', '5bdbb87075657100672e830a');
    checksType.push(type1);
    checksType.push(type2);
    checksType.push(type3);
    checksType.push(type4);
    checksType.push(type5);
    checksType.push(type6);
    
    var formType = [];
    var form1 = AV.Object.createWithoutData('Form_Type', '5bdc527cac502e00622a8d17');
    var form2 = AV.Object.createWithoutData('Form_Type', '5bdc52deac502e00622a97d0');
    formType.push(form1);
    formType.push(form2);

    var resultsArray = results;
     var arr = [];
    for (var k = 0, length = resultsArray.length; k < length; k++) {
       var temoUser = resultsArray[k];
       var todo = AV.Object.createWithoutData('_User', temoUser.toJSON().objectId);
      todo.set('formTypes',formType);
      todo.set('checkTypes',checksType);
      arr.push(todo);
    }
   return AV.Object.saveAll(arr);
  }).then(function(results){
    
  }).catch(function(error){
    console.log(error);
  });

});

AV.Cloud.define('registerSome', function (request) {
  var query = new AV.Query('tb_UserInfo');
  query.notEqualTo('chongfu',true);
  query.notEqualTo('toUser',true);
  query.notEqualTo('notphonenumber',true);
  query.limit(1000);
  query.ascending('createdAt');
  var arr = [];
  var brr = [];
  query.find().then(function (results) {
    
    var resultsArray = results;

    for (var k = 0, length = resultsArray.length; k < length; k++) {
      var tempTeam = resultsArray[k];

      var TodoFolder = AV.Object.extend('_User');
      var todoFolder = new TodoFolder();
      todoFolder.set('username',tempTeam.toJSON().userid);
      var pwd =  tempTeam.toJSON().pwd;
      if(!pwd){
        pwd = "123456";
        todoFolder.set('mobilePhoneNumber',tempTeam.toJSON().userid);
      }
      todoFolder.set('password',pwd);
      arr.push(todoFolder);
      var todo = AV.Object.createWithoutData('tb_UserInfo', tempTeam.toJSON().objectId);
      todo.set('toUser',true);
      brr.push(todo);
    }
    return AV.Object.saveAll(arr);
  }).then(function(objects){
    return AV.Object.saveAll(brr);
  }).then(function(objects){
    
  }).catch(function(error){
    console.log(error);
  });
});
*/


/*
 * 每天凌晨2点点执行删除无用的team 方法
 */
AV.Cloud.define('delete_unTeam_Timer', function (request) {
  var query = new AV.Query('tb_Team');
  query.equalTo('delete', false);
  query.sizeEqualTo('participants', 1);
  query.limit(1000);

  var fordeleteUserHistory = [];

  query.find().then(function (results) {
    var pendingObjects = [];
    var resultsArray = results;

    for (var k = 0, length = resultsArray.length; k < length; k++) {
      var tempTeam = resultsArray[k];
      var participants = tempTeam.toJSON().participants; //如何识别participants
      if (participants.length == 1) {
        var obj = AV.Object.createWithoutData('tb_Team', tempTeam.toJSON().objectId);
        obj.set('delete', true);
        obj.set('deleteReson', '长期无人使用的空房间,系统已自动删除');
        pendingObjects.push(obj);

        var uuQuery = new AV.Query('tb_User_History');
        console.log(participants[0]);
        uuQuery.equalTo('userObjectId', participants[0]);
        fordeleteUserHistory.push(uuQuery);
      }
    }
    //删除Team表
    return AV.Object.saveAll(pendingObjects);
  }).then(function (objects) {
    // 成功删除Team表
    console.log('今日共计删除无用队伍:', objects.length, '条');
    console.log('批量查询实例', fordeleteUserHistory.length);
    //找到所有要删除的对象
    var alldeleteQuery = AV.Query.or(...fordeleteUserHistory);
    return alldeleteQuery.find();
  }).then(function (results) {
    // console.log('批量查询实例',fordeleteUserHistory);
    var deleteObjects = [];
    for (var y = 0, length = results.length; y < length; y++) {
      var userHisObj = results[y];
      console.log(userHisObj.toJSON());
      var deleteObj = AV.Object.createWithoutData('tb_User_History', userHisObj.toJSON().objectId);
      deleteObjects.push(deleteObj);
    }
    return AV.Object.destroyAll(deleteObjects)
  }).then(function () {
    // 成功
    console.log('删除成功');
  }).catch(function (error) {
    console.log(error);
  });
});



/*
 * Hook函数* 当遗愿消息被发送的时候若用户是匿名用户则需要做业务清理
 */
AV.Cloud.onIMMessageSent((request) => {

  //console.log('消息发送 ==== >params', request.params);
  let content = request.params.content;
  var userId = request.params.fromPeer;
  var query = new AV.Query('_User');
  if (content.search("isWill") != -1) {
    //只有匿名用户和房主才会发送遗愿消息
    var contentJson = JSON.parse(content);
    var teamId = contentJson._lcattrs.teamId;
    var isPublish = contentJson._lcattrs.isPublish;
    console.log('teamId ====> ', teamId);
    var team = AV.Object.createWithoutData('tb_Team', teamId);
    if (isPublish) {
      //是房主发的.
      team.set('hidden', true);
      // team.set('delete', true);
      team.save().then(function (todo) {
        console.log('房主下线了,此队伍将隐藏');
      }, function (error) {
        console.error(error);
      });
    } else {
      team.remove('participants', userId);
      return team.save().then(function (todo) {
        console.log('清理离线用户成功===>', userId);
        var conversation = AV.Object.createWithoutData('_Conversation', request.params.convId);
        conversation.remove('m', userId);
        conversation.save();
      }, function (error) {
        // 异常处理
        console.error(error);
      });
    }
  }
});


/*
 * Hook函数* 发送系统消息时候进行推送
 */
AV.Cloud.afterSave('tb_Sys_Message', function (request) {
  var content = request.object.get('content');
  var publishRange = request.object.get('publishRange'); //发送范围
  if (content) {
    if (publishRange === "all") {
      console.log('发布了全体推送');
      AV.Push.send({
        data: {
          alert: content
        }
      });
    } else {
      console.log('发布了个体推送' + publishRange);
      var query = new AV.Query('_Installation');
      query.equalTo('channels', publishRange);
      query.find().then(function (results) {
        console.log(results);
      }, function (error) {
        console.log(error);
      });
      AV.Push.send({
        where: query,
        data: {
          alert: content
        }
      });
    }
  }
});

/*
 * Hook函数* 当发布视频到广场后进行广播推送
 */
AV.Cloud.afterSave('tb_Square', function (request) {
  var title = request.object.get('title');
  var desc = request.object.get('desc'); //发送范围
  console.log(title + '视频已推送到所有人');
  AV.Push.send({
    //    prod: 'dev',
    data: {
      alert: title,
      action: 'push_square'
    }
  });
});

module.exports = AV.Cloud;