//
//  GGSysMesModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSysMesModel.h"

@implementation GGSysMesModel

+ (GGSysMesModel *)valueToObj:(AVObject *)obj
{
    GGSysMesModel *model = [[GGSysMesModel alloc]init];
    model.type = [obj objectForKey:@"type"];
    model.title = [obj objectForKey:@"title"];
    model.cover = [obj objectForKey:@"cover"];
    model.content = [obj objectForKey:@"content"];
    model.relevantPersonne = [obj objectForKey:@"relevantPersonne"];
    model.user = [obj objectForKey:@"user"];
    model.publishRange = [obj objectForKey:@"publishRange"];
    model.createdAt = obj.createdAt;
    return model;
}

+ (void)querySysMessageWithBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery  *queryMy = [AVQuery queryWithClassName:DB_SYS_MESSAGE];
    [queryMy whereKey:@"publishRange" equalTo:[AVUser currentUser].objectId];
    
    AVQuery  *queryall = [AVQuery queryWithClassName:DB_SYS_MESSAGE];
    [queryall whereKey:@"publishRange" equalTo:@"all"];
    
    AVQuery *query = [AVQuery orQueryWithSubqueries:[NSArray arrayWithObjects:queryall,queryMy,nil]];
//    [query includeKey:@"relevantPersonne"];
    [query includeKey:@"user"];
    [query orderByDescending:@"updatedAt"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}


@end
