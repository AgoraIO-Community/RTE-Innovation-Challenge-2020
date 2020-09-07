//
//  GGGameModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGGameModel.h"

@implementation GGGameModel


+ (void)testAddNewGame
{
  //  GGGameModel *game = [[GGGameModel alloc]init];
    AVObject *game = [AVObject objectWithClassName:DB_GAME];
    [game setObject:@"绝地求生" forKey:@"gameName"];
    [game setObject:@"1" forKey:@"sort"];
    [game setObject:@"steam" forKey:@"gamePlatform"];
    
    AVObject *obj = [AVObject objectWithClassName:DB_GAME_TYPE];
    [obj setObject:@"双排(double)" forKey:@"name"];
    [obj setObject:@(2) forKey:@"maxnum"];
    
    
    AVObject *obj2 = [AVObject objectWithClassName:DB_GAME_TYPE];
    [obj2 setObject:@"四排" forKey:@"name"];
    [obj2 setObject:@(4) forKey:@"maxnum"];
    
    
    [AVObject saveAll:@[obj,obj2]];
    
    [game setObject:@[obj,obj2] forKey:@"taskType"];
    [game saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        
    }];
}

+ (GGGameModel *)valueToObj:(AVObject *)obj
{
    GGGameModel *model = [[GGGameModel alloc]init];
    model.objectId  = obj.objectId;
    model.taskType = [obj objectForKey:@"taskType"];
    model.sort = [obj objectForKey:@"sort"];
    model.gameicon = [obj objectForKey:@"gameicon"];
    model.titleColor = [obj objectForKey:@"titleColor"];
    model.backColor = [obj objectForKey:@"backColor"];
    model.gameName = [obj objectForKey:@"gameName"];
    model.gamePlatform = [obj objectForKey:@"gamePlatform"];
    model.gameBg = [obj objectForKey:@"gameBg"];
    model.chatId = [obj objectForKey:@"chatId"];
    model.isNeedQ = [[obj objectForKey:@"isNeedQ"] boolValue];
    return model;
}

+ (void)queryGameListWithBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_GAME];
    [query whereKey:@"isShow" equalTo:[NSNumber numberWithBool:YES]];
    [query whereKey:@"delete" equalTo:[NSNumber numberWithBool:NO]];
//    query.cachePolicy = kAVCachePolicyCacheElseNetwork;
    [query includeKey:@"taskType"];
    [query orderByAscending:@"sort"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}

+ (void)queryGameConersationListWithBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_GAME];
    [query whereKey:@"delete" equalTo:[NSNumber numberWithBool:NO]];
    [query includeKey:@"taskType"];
    [query orderByAscending:@"sort"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}

@end
