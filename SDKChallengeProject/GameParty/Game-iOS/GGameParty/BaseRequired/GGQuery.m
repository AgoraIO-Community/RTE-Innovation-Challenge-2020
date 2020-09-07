//
//  GGQuery.m
//  GGameParty
//
//  Created by Victor on 2018/8/19.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGQuery.h"

@implementation GGQuery

+ (AVQuery *)teamListQuery
{
    AVQuery *query = [AVQuery queryWithClassName:DB_TEAM];
    [query whereKey:@"status" notEqualTo:@(NO)];
    [query whereKey:@"hidden" notEqualTo:@(YES)];
    [query whereKey:@"delete" equalTo:@(NO)];
    [query includeKey:@"type"];
    [query includeKey:@"publisher"];
    [query includeKey:@"participants"];
    [query includeKey:@"game"];
    [query includeKey:@"conversation"];
    [query includeKey:@"area"];
    [query orderByDescending:@"sortDate"];
    return query;
}


+ (AVQuery *)teamLiveQuery
{
    AVQuery *query = [AVQuery queryWithClassName:DB_TEAM];
//    [query whereKey:@"status" notEqualTo:@(NO)];
//    [query whereKey:@"hidden" notEqualTo:@(YES)];
//    [query whereKey:@"delete" equalTo:@(NO)];
    [query includeKey:@"type"];
    [query includeKey:@"publisher"];
    [query includeKey:@"participants"];
    [query includeKey:@"game"];
    [query includeKey:@"conversation"];
    [query includeKey:@"area"];
    [query orderByDescending:@"sortDate"];
    return query;
}

@end
