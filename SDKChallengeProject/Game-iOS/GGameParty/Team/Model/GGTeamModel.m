//
//  GGTeamModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamModel.h"
#import "GGGameModel.h"
@implementation GGTeamModel

+ (GGTeamModel *)shareInstance
{
    
    return nil;
}

+ (GGTeamModel *)vulueToObj:(AVObject *)obj
{
    GGTeamModel *model = [[GGTeamModel alloc]init];
    model.area = [obj objectForKey:@"area"];
    model.publisher = [obj objectForKey:@"publisher"];
    model.game = [obj objectForKey:@"game"];
    model.desc = [obj objectForKey:@"desc"];
    model.isLock = [obj objectForKey:@"isLock"];
    model.participants = [obj objectForKey:@"participants"];
    model.type = [obj objectForKey:@"type"];
    model.title = [obj objectForKey:@"title"];
    model.status = [obj objectForKey:@"status"];
    model.objectId = obj.objectId;
    model.createdAt = obj.createdAt;
    model.updatedAt = obj.updatedAt;
    model.maxnum = [obj objectForKey:@"maxnum"];
    model.groupChatId = [obj objectForKey:@"groupChatId"];
    model.teamCode = [obj objectForKey:@"teamCode"];
    model.outUserId = [obj objectForKey:@"outUserId"];
    model.isDelete = [obj objectForKey:@"delete"];
    return model;
}

+ (void)createRoomWith
{
    AVObject *obj = [[AVObject alloc]initWithClassName:DB_TEAM];
    [obj setObject:@"快来玩啊,小哥哥小姐姐贾咳咳咳咳咳咳" forKey:@"title"];
    AVObject *type = [AVObject objectWithClassName:DB_GAME_TYPE objectId:@"5b5800822f301e00393428f4"];
    [obj setObject:type forKey:@"type"];
    [obj setObject:[AVUser currentUser] forKey:@"publisher"];
    
    AVObject *game = [AVObject objectWithClassName:DB_GAME objectId:@"5b57fff99f54540035e38925"];
    [obj setObject:game forKey:@"game"];
    
    [obj setObject:@(NO) forKey:@"isLock"];
    [obj setObject:@"请大家不要互相攻击,和平组队.完成单杀" forKey:@"desc"];
    [obj setObject:@(YES) forKey:@"status"];
    AVUser *user = [AVUser currentUser];
    [obj setObject:@[user] forKey:@"participants"];
    [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        
    }];
}

+ (void)checkUserCanAddNewTeamWithBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
    [query whereKey:@"userObjectId" equalTo:[AVUser currentUser].objectId];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}



+ (void)queryTeamWithTitleField:(NSString *)title withPageNo:(NSInteger)pageNo gameModel:(GGGameModel *)game block:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [GGQuery teamListQuery];
    
    NSString *string = title;
    if ([title rangeOfString:@"?"].location != NSNotFound)
    {
        string = [string stringByReplacingOccurrencesOfString:@"?" withString:@"\\?"];
    }
    [query whereKey:@"title" containsString:string];
    query.limit = 200;
    query.skip = 200*pageNo;
    if (game)//game有值就查询game
    {
        AVObject *obj = [AVObject objectWithClassName:DB_GAME objectId:game.objectId];
        [query whereKey:@"game" equalTo:obj];
    }
    
    [query findObjectsInBackgroundWithBlock:resultBlock];
}

+ (void)queryTeamWithPageNo:(NSInteger)pageNo gameModel:(GGGameModel *)game block:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [GGQuery teamListQuery];
    query.limit = 200;
    query.skip = 200*pageNo;
    if (game)//game有值就查询game
    {
        AVObject *obj = [AVObject objectWithClassName:DB_GAME objectId:game.objectId];
        [query whereKey:@"game" equalTo:obj];
    }
    [query findObjectsInBackgroundWithBlock:resultBlock];
}

+ (void)deleteTeamWithTeamObjectId:(NSString *)teamObjectId
{
    AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:teamObjectId];
    [obj setObject:@(YES) forKey:@"delete"];
    [obj saveInBackground];
    [obj fetchInBackgroundWithBlock:^(AVObject *object, NSError *error) {
        if (!error)
        {
            NSArray *arr  = [object objectForKey:@"participants"];
            NSMutableArray *querrArr = [NSMutableArray arrayWithCapacity:0];
            for(NSString *temp in arr)
            {
                AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
                [query whereKey:@"userObjectId" equalTo:temp];
                [querrArr addObject:query];
            }
            AVQuery *query = [AVQuery orQueryWithSubqueries:querrArr];
            [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
                if (!error)
                {
                    [AVObject deleteAllInBackground:objects block:^(BOOL succeeded, NSError * _Nullable error) {
                        if (!error)
                        {
                             NSLog(@"所有人已被清退,此房间已被销毁");

                        }
                    }];
                }
            }];
        }
    }];
}



+ (void)saveUserGameHistory:(AVObject *)game conversation:(AVObject *)conversation team:(AVObject *)team withBlock:(AVBooleanResultBlock)block
{
    AVObject *obj = [AVObject objectWithClassName:DB_USER_HISTORY];
    [obj setObject:[AVUser currentUser] forKey:@"user"];
    [obj setObject:[AVUser currentUser].objectId forKey:@"userObjectId"];
    [obj setObject:game forKey:@"game"];
    [obj setObject:conversation forKey:@"conversation"];
    [obj setObject:team forKey:@"team"];
    [obj saveInBackgroundWithBlock:block];
}

+ (void)saveUserGameHistory:(AVObject *)game conversation:(AVObject *)conversation team:(AVObject *)team
{
    [GGTeamModel saveUserGameHistory:game conversation:conversation team:team withBlock:^(BOOL succeeded, NSError * _Nullable error) {
        
    }];
}

+ (void)deleteUserGameHistory
{
    AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
    [query whereKey:@"userObjectId" equalTo:[AVUser currentUser].objectId];
//    [query whereKey:@"delete" equalTo:@(NO)];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
                for(AVObject *obj in objects)
                {
                    [obj deleteInBackground];
                }
            }
        }
    }];
}



/**
 删除用户组队信息--踢人时需要执行此方法

 @param teamObjectId team表的id
 @param userObjectId 被删的用户id
 */
+ (void)deleteUserGameHistoryWithTeamObjectId:(NSString *)teamObjectId userId:(NSString *)userObjectId
{
    NSString *userId = userObjectId?userObjectId:[AVUser currentUser].objectId;
    
    AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
    [query whereKey:@"userObjectId" equalTo:userId];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
//                for(AVObject *obj in objects)
//                {
//                    [obj deleteInBackground];
//                }
                [AVObject deleteAll:objects];
            }
        }
    }];
    
    
    AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:teamObjectId];
    if (userObjectId.length > 0)
    {//如果是踢人的话,那么要把字段值更新到outuserid字段.否则就是正常退出房间
        [obj addUniqueObject:userObjectId forKey:@"outUserId"];
    }
    [obj removeObject:userId forKey:@"participants"];
    obj.fetchWhenSave = YES;
    [obj saveInBackground];
    
}


+ (void)saveHistoryTeamer:(AVObject *)user
{
    AVObject *obj = [AVObject objectWithClassName:DB_TEAM_HISTORY];
    [obj setObject:[AVUser currentUser] forKey:@"user"];
    [obj setObject:user forKey:@"teamer"];
    [obj saveInBackground];
}


+ (void)saveHistoryTeamerWithUserArr:(NSArray *)userArr
{//userArr里面是一个个AVUser的pointer对象
    if (userArr.count > 0)
    {
        NSMutableArray *queryArr = [NSMutableArray arrayWithCapacity:0];
        NSMutableArray *objArr = [NSMutableArray arrayWithCapacity:0];
        for(AVObject *obj in userArr)
        {
            AVQuery *query = [AVQuery queryWithClassName:DB_TEAM_HISTORY];
            [query whereKey:@"user" equalTo:[AVUser currentUser]];
            [query whereKey:@"teamer" equalTo:obj];
            [queryArr addObject:query];
            
            AVObject *saveObj = [AVObject objectWithClassName:DB_TEAM_HISTORY];
            [saveObj setObject:[AVUser currentUser] forKey:@"user"];
            [saveObj setObject:obj forKey:@"teamer"];
            [objArr addObject:saveObj];//所有要保存的对象
        }
        NSMutableArray *finalArr = [NSMutableArray arrayWithArray:objArr];
        
        AVQuery *allQuery = [AVQuery orQueryWithSubqueries:queryArr];
        allQuery.limit = 30;
        [allQuery findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            if (!error)
            {//查出这些用户是否已经是当前用户的最近队友
                for(AVObject *obj in objects)
                {
                    NSString *teamerObj = [[obj objectForKey:@"teamer"] objectForKey:@"objectId"];
                    for(AVObject *saveObj in objArr)
                    {//已经是队友的那么可以移除了.
                        NSString *saveTeamerObj = [[obj objectForKey:@"teamer"] objectForKey:@"objectId"];
                        if ([teamerObj isEqualToString:saveTeamerObj])
                        {
                            [finalArr removeObject:saveObj];
                        }
                    }
                }
                [AVObject saveAll:finalArr];
            }
        }];
    }
    
}


+ (void)queryHistoryTeamerBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_TEAM_HISTORY];
    [query whereKey:@"user" equalTo:[AVUser currentUser]];
    query.limit = 30;
    [query orderByDescending:@"updatedAt"];
    [query includeKey:@"teamer"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
    
}



@end
