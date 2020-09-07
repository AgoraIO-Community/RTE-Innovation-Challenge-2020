var AV = require('leanengine');
var Promise = require('bluebird');
var _ = require('underscore');

var router = require('express').Router();
var redisClient = require('../redis').redisClient;


AV.Cloud.define('getGameTypeData', function (req, resp) {

  redisClient.set('gameData','123').catch(console.error);
  //  fetchGameDataFromeCache();  
  //   var query = new AV.Query('tb_Game');
  //   query.equalTo('isShow',true);
  //   query.equalTo('delete',false);
  //   query.include('taskType');
  //   query.ascending('sort');
  //  return query.find().then(list => {
  //       return list;
  //    //   console.log(list);
  //   });
    
//     new AV.Query('tb_Game').find().then(function(posts) {
//     return fetchUsersFromCache(posts.map(function(post) {
//       return post.get('author').id;
//     })).then(function(users) {
//       res.json(posts.map(function(post) {
//         return _.extend(post.toJSON(), {
//           author: _.find(users, {id: post.get('author').id})
//         });
//       }))
//     });
//   }).catch(next);
});


function fetchGameDataFromeCache(){
    // return redisClient.mget(_.uniq())
    var query = new AV.Query('tb_Game');
    query.equalTo('isShow',true);
    query.equalTo('delete',false);
    query.include('taskType');
    query.ascending('sort');
    query.find().then(list => {
     // console.log(list);
      redisClient.set('gameData','123').catch(console.error);
  });
    // return redisClient.get(userId).then(function(cachedUser) {
    //     if (cachedUser) {
    //       // 反序列化为 AV.Object
    //       return new AV.User(JSON.parse(cachedUser), {parse: true});
    //     } else {
    //       new AV.Query(AV.User).get(userId).then(function(user) {
    //         if (user) {
    //           // 将序列化后的 JSON 字符串存储到 LeanCache
    //           redisClient.set(redisUserKey(userId), JSON.stringify(user)).catch(console.error);
    //         }
    
    //         return user;
    //       });
    //     }
    //   });

}

/* 从缓存中读取一组 User, 如果没有找到则从云存储中查询（会进行去重并合并为一个查询）*/
function fetchUsersFromCache(userIds) {
    // 先从 LeanCache 中查询
    return redisClient.mget(_.uniq(userIds).map(redisUserKey)).then(function(cachedUsers) {
      var parsedUsers = cachedUsers.map(function(user) {
        // 对 User（也就是 AV.Object）进行反序列化
        return new AV.User(JSON.parse(user), {parse: true});
      });
  
      // 找到 LeanCache 中没有缓存的那些 User
      var missUserIds = _.uniq(userIds.filter(function(userId) {
        return !_.find(parsedUsers, {id: userId});
      }));
  
      return Promise.try(function() {
        if (missUserIds.length) {
          // 从云存储中查询 LeanCache 中没有的 User
          return new AV.Query(AV.User).containedIn('objectId', missUserIds).find();
        } else {
          return [];
        }
      }).then(function(latestUsers) {
        if (latestUsers.length) {
          // 将从云存储中查询到的 User 缓存到 LeanCache, 此处为异步
          redisClient.mset(_.flatten(latestUsers.map(function(user) {
            return [redisUserKey(user.id), JSON.stringify(user)];
          }))).catch(console.error);
        }
  
        // 将来自缓存和来自云存储的用户组合到一起作为结果返回
        return userIds.map(function(userId) {
          return _.find(parsedUsers, {id: userId}) || _.find(latestUsers, {id: userId});
        });
      });
    });
  }

module.exports = router;