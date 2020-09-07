//
//  GGUserModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserModel.h"

@interface GGUserModel()


@end

@implementation GGUserModel

@synthesize userId = _userId;
@synthesize name = _name;
@synthesize avatarURL = _avatarURL;
@synthesize clientId = _clientId;

- (instancetype)initWithUserId:(NSString *)userId name:(NSString *)name avatarURL:(NSURL *)avatarURL clientId:(NSString *)clientId {
    self = [super init];
    if (!self) {
        return nil;
    }
    _userId = userId;
    _name = name;
    _avatarURL = avatarURL;
    _clientId = clientId;
    return self;
}


+ (instancetype)userWithUserId:(NSString *)userId name:(NSString *)name avatarURL:(NSURL *)avatarURL clientId:(NSString *)clientId{
    GGUserModel *user = [[GGUserModel alloc] initWithUserId:userId name:name avatarURL:avatarURL clientId:clientId];
    return user;
}
- (instancetype)initWithUserId:(NSString *)userId name:(NSString *)name avatarURL:(NSURL *)avatarURL {
    return [self initWithUserId:userId name:name avatarURL:avatarURL clientId:userId];
}
+ (instancetype)userWithUserId:(NSString *)userId name:(NSString *)name avatarURL:(NSURL *)avatarURL {
    return [self userWithUserId:userId name:name avatarURL:avatarURL clientId:userId];
}
- (instancetype)initWithClientId:(NSString *)clientId {
    return [self initWithUserId:nil name:nil avatarURL:nil clientId:clientId];
}
+ (instancetype)userWithClientId:(NSString *)clientId {
    return [self userWithUserId:nil name:nil avatarURL:nil clientId:clientId];
}
- (id)copyWithZone:(NSZone *)zone {
    return [[GGUserModel alloc] initWithUserId:self.userId
                                       name:self.name
                                  avatarURL:self.avatarURL
                                   clientId:self.clientId
            ];
}
- (void)encodeWithCoder:(NSCoder *)aCoder {
    [aCoder encodeObject:self.userId forKey:@"userId"];
    [aCoder encodeObject:self.name forKey:@"name"];
    [aCoder encodeObject:self.avatarURL forKey:@"avatarURL"];
    [aCoder encodeObject:self.clientId forKey:@"clientId"];
}
- (id)initWithCoder:(NSCoder *)aDecoder {
    if(self = [super init]){
        _userId = [aDecoder decodeObjectForKey:@"userId"];
        _name = [aDecoder decodeObjectForKey:@"name"];
        _avatarURL = [aDecoder decodeObjectForKey:@"avatarURL"];
        _clientId = [aDecoder decodeObjectForKey:@"clientId"];
    }
    return self;
}

+ (GGUserModel *)valueToObj:(AVUser *)obj
{
    GGUserModel *model = [[GGUserModel alloc]init];
    model.username = obj.username;
    model.avatar = [obj objectForKey:@"avatar"];
    model.objectId = obj.objectId;
    model.age = [obj objectForKey:@"age"];
    model.sex = [[obj objectForKey:@"sex"] boolValue];
    model.lastAddress = [obj objectForKey:@"lastAddress"];
    
    return model;
}

#pragma mark - Method

+ (void)queryUserWithName:(NSString *)name withBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_USER];
    NSString *string = name;
    if ([name rangeOfString:@"?"].location != NSNotFound)
    {
       string = [string stringByReplacingOccurrencesOfString:@"?" withString:@"\\?"];
    }
       [query whereKey:@"username" containsString:string];
    [query orderByDescending:@"updatedAt"];

    [query findObjectsInBackgroundWithBlock:resultBlock];
}


//发送验证码
+ (void)sendVerificationCode:(NSString *)phoneNumber callback:(AVBooleanResultBlock)callback
{
    [AVSMS requestShortMessageForPhoneNumber:phoneNumber options:nil callback:callback];
}

//注册并登录
+ (void)loginOrRegisterWithPhoneNumber:(NSString *)phoneNumber withVerificationCode:(NSString *)smscode  block:(AVUserResultBlock)block
{
    [AVUser signUpOrLoginWithMobilePhoneNumberInBackground:phoneNumber smsCode:smscode block:block];
}

//查询两个用户间的拉黑和关注情况
+ (void)queryUserRelationWithUser:(AVUser *)user withBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *queryfollow = [AVQuery queryWithClassName:DB_FOLLOW];
    [queryfollow whereKey:@"operator" equalTo:[AVUser currentUser]];
    [queryfollow whereKey:@"recipient" equalTo:user];
    [queryfollow findObjectsInBackgroundWithBlock:resultBlock];
}

//添加拉黑和关注
+ (void)addRelation:(NSString *)relation WithUser:(AVUser *)user withBlock:(AVBooleanResultBlock)block//relation:1 关注 2 拉黑
{
    AVQuery *query = [AVQuery queryWithClassName:DB_FOLLOW];
    [query whereKey:@"operator" equalTo:[AVUser currentUser]];
    [query whereKey:@"recipient" equalTo:user];
    [query whereKey:@"relation" equalTo:relation];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            AVObject *obj = [AVObject objectWithObjectId:DB_FOLLOW];
            
            [obj setObject:[AVUser currentUser] forKey:@"operator"];
            [obj setObject:user forKey:@"recipient"];
            if (objects.count > 0)
            {//说明已关注,取消关注 ---此行数据删除
                for(AVObject *obj in objects)
                {
                    [obj deleteInBackgroundWithBlock:block];
                }
            }
            else
            {//添加关注
                [obj setObject:@"1" forKey:@"type"];
                [obj saveInBackgroundWithBlock:block];
            }
        }
    }];
}


+ (void)postFollowMsg:(NSString *)objId
{
    AVObject *obj = [AVObject objectWithClassName:DB_SYS_MESSAGE];
    [obj setObject:objId forKey:@"publishRange"];
    [obj setObject:@"关注消息" forKey:@"title"];
    [obj setObject:@"1" forKey:@"type"];
    [obj setObject:[AVUser currentUser]  forKey:@"user"];
    [obj setObject:[NSString stringWithFormat:@"%@关注了您",[AVUser currentUser].username] forKey:@"content"];
    [obj setObject:[AVObject objectWithClassName:DB_USER objectId:objId]  forKey:@"relevantPersonne"];
    [obj saveInBackground];
}

//查询用户所有关注
+ (void)queryUserRelation:(NSString *)realtion withBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_FOLLOW];
    [query whereKey:@"operator" equalTo:[AVUser currentUser]];
    [query whereKey:@"relation" equalTo:@"relation"];
    [query includeKey:@"recipient"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}
+ (void)userGoodRelation:(NSString *)beGooder
{
    AVObject *obj = [AVObject objectWithClassName:DB_GOOD_HISTORY];
    [obj setObject:[AVUser currentUser].objectId forKey:@"gooder"];
    [obj setObject:beGooder forKey:@"beGooder"];
 
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateTime = [formatter stringFromDate:[NSDate date]];
    [obj setObject:dateTime forKey:@"date"];
    [obj saveInBackground];
}

+ (void)queryGoodRelation:(NSString *)beGooder withBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_GOOD_HISTORY];
    [query whereKey:@"gooder" equalTo:[AVUser currentUser].objectId];
    //[query whereKey:beGooder equalTo:@"beGooder"];
    [query whereKey:@"beGooder" equalTo:beGooder];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateTime = [formatter stringFromDate:[NSDate date]];
    [query whereKey:@"date" equalTo:dateTime];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}



@end
