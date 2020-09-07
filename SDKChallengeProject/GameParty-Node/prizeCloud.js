// //
// //  prizeCloud.js
// //  GGame
// //
// //  Created by Victor on 2018/8/5.
// //  Copyright © 2018年 Victor. All rights reserved.
// //


// var AV = require('leanengine');

// //Promise 示意
// AV.Cloud.define('test', function (request) {
//     var chapterIds = [
//       '584e1c408e450a006c676162', // 第一章
//       '584e1c43128fe10058b01cf5', // 第二章
//       '581aff915bbb500059ca8d0b'  // 第三章
//     ];
  
//     new AV.Query('Chapter').get(chapterIds[0]).then(function (chapter0) {
//       // 向页面添加内容
//       addHtmlToPage(chapter0.get('content'));
//       // 返回新的 Promise
//       return new AV.Query('Chapter').get(chapterIds[1]);
//     }).then(function (chapter1) {
//       addHtmlToPage(chapter1.get('content'));
//       return new AV.Query('Chapter').get(chapterIds[2]);
//     }).then(function (chapter2) {
//       addHtmlToPage(chapter2.get('content'));
//       // 完成
//     });
//   });

// /*
// * 获取奖励列表
// * params:pageNo:分页,从0开始
// * return:array <temp_Invite>
// */
// AV.Cloud.define('queryUserPrize',function(request){
//     var pageNo = request.params.pageNo;
//     var currentUser = request.currentUser;
//     var query = new AV.Query('temp_Invite');
//     query.equalTo('user',currentUser);
//     //query.exists('code');
//     //query.exists('beUser');
//     query.limit(100);
//     query.skip(100 * pageNo);
//     query.ascending('createdAt');
//     return query.find().then(function(results){
//         return results;
//     });
// });

// /*
// * 获取大奖列表
// * return:array <temp_Prize>
// */
// AV.Cloud.define('queryUserBigPrize',function(request){
//     var currentUser = request.currentUser;
//     var query = new AV.Query('temp_Prize');
//     query.equalTo('user',currentUser);
//     query.equalTo('type','2');
//     query.limit(100);
//     query.ascending('createdAt');
//     return query.find().then(function(results){
//         return results;
//     });
// });


// /*
// * 开奖
// * params:type:1是普通奖品,2是大奖
// * params:invite:temp_Invite表的objectid
// * returan:temp_Prize的一个实例
// */
//   AV.Cloud.define('getPrize', function (request) {
  
//     var currentUser = request.currentUser;
//     var type = request.params.type;//1是普通奖品. 2是大作
//     var inviteObjId = request.params.invite.toString();
// // ---- 大作的开奖方式-严谨-核实用户数据的真实性才开奖
//     if(type.toString() == '2'){
//         //如果是大作---那么要: 检查此用户是否能领取大作.
//         var bquery = new AV.Query('temp_Invite');
//         bquery.equalTo('user',currentUser);
//         bquery.equalTo('type','1');
//         var bqueryCount;
//         var cqueryCount;
//         return bquery.count().then(function (count) {
            
//             bqueryCount = 12;
//             if(bqueryCount < 10){
//                 //不允许领取 
//                 return 'no limits';
//             }
//             var cquery = new AV.Query('temp_Invite');
//              cquery.equalTo('user',currentUser);
//              cquery.equalTo('type','2');
//              return cquery.count();
//         }).then(function (count){
//             cqueryCount = count;
            
//             var indexx = bqueryCount%10;
//             console.log('bcount==>' + bqueryCount + 'ccount==>'+cqueryCount + indexx);
//             if(indexx > cqueryCount){
//             //可以领取
//                 console.log('允许领取');
//                 var query = new AV.Query('temp_Prize');
//                 query.equalTo('status', false);
//                 query.equalTo('type', type.toString());
//                 var prizeCode;
//                 return query.first().then(function (results) {
//                     console.log(results);
//                   //1.取出一个奖品并保存更新状态为不可用,填充领奖用户字段
//                   prizeCode = results.toJSON().code;
//                   var todo = AV.Object.createWithoutData('temp_Prize', results.toJSON().objectId);
//                   todo.set('status', true);
//                   todo.set('user', currentUser);
//                   todo.fetchWhenSave = true;
//                   return todo.save();
//                 }).then(function (obj) {
//                   //保存成功后-->更新temp_Invite字段code值
//                   var prizeObj = obj;
//                   return obj.fetch();
//                 }).then(function(obj){
//                     return obj;
//                 });;
//             }
//             else
//             {
//                 return 'no limits';
//             }
//         });
//     }
// // ----- 普通游戏开奖方式---不严谨.
//     var query = new AV.Query('temp_Prize');
//     query.equalTo('status', false);
//     query.equalTo('type', type.toString());
//     var prizeCode;
//     return query.first().then(function (results) {
//       //1.取出一个奖品并保存更新状态为不可用,填充领奖用户字段
//       prizeCode = results.toJSON().code;
//       var todo = AV.Object.createWithoutData('temp_Prize', results.toJSON().objectId);
//       todo.set('status', true);
//       todo.set('user', currentUser);
//       todo.fetchWhenSave = true;
//       return todo.save();
//     }).then(function (obj) {
//       //保存成功后-->更新temp_Invite字段code值
//       var prizeObj = obj;
//       // var code = prizeObj.toJSON().code;
//       var inviteObj = AV.Object.createWithoutData('temp_Invite', inviteObjId);
//       inviteObj.set('prize', prizeObj);
//       console.log(prizeCode);
//       inviteObj.set('code', prizeCode);
//       inviteObj.fetchWhenSave = true;
//       return inviteObj.save();
//     }).then(function (finalObj) {
//       return finalObj;
//     });
//   });

