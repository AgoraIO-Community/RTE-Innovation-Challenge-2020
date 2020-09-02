//
//  SquareModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "SquareModel.h"

@implementation SquareModel

+ (void)querySqueryDataPageNo:(NSInteger)page resultBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_SQUARE];
    query.limit = 10;
    query.skip = 10*page;
    [query orderByAscending:@"createdAt"];
    [query includeKey:@"publisher"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}


+ (void)giveGood:(SquareModel *)model resultBlock:(AVBooleanResultBlock)block
{
    AVObject *obj = [AVObject objectWithClassName:DB_SQUARE objectId:model.objectId ];
    NSMutableArray *arrary = [NSMutableArray arrayWithArray:model.isgood];
    AVUser *currentUser =   [AVUser currentUser];
    BOOL isGood = YES;//判断是否已赞过,赞过了就取消
    for(AVUser *user in arrary)
    {
        if ([user.objectId isEqualToString:currentUser.objectId])
        {
            isGood = NO;
        }
    }
    
    if (isGood)
    {
        [arrary addObject:[AVUser currentUser]];
    }
    else
    {
        [arrary removeObject:[AVUser currentUser]];
    }
    
    
    [obj setObject:arrary forKey:@"isgood"];
    [obj saveInBackgroundWithBlock:block];
}




@end
