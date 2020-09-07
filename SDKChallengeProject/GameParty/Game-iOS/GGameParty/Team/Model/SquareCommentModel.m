//
//  SquareCommentModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "SquareCommentModel.h"
#import "SquareModel.h"
@implementation SquareCommentModel

+ (void)queryCommentPageNo:(NSInteger)page square:(SquareModel *)model resultBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_SQUARE_COMMENT];
    query.limit = 200;
    query.skip = 200*page;
    [query whereKey:@"squeryId" equalTo:model.objectId];
    [query includeKey:@"publisher"];
    
    [query orderByDescending:@"goodNum"];
    [query findObjectsInBackgroundWithBlock:resultBlock];
    
}


+ (void)addComment:(NSString *)comment square:(SquareModel *)model resultBlock:(AVBooleanResultBlock)block
{
    AVObject *obj = [AVObject objectWithClassName:DB_SQUARE_COMMENT];

    [obj setObject:[AVUser currentUser] forKey:@"publisher"];
    [obj setObject:comment forKey:@"comment"];
    [obj setObject:model.objectId forKey:@"squeryId"];//主数据的主键ID
    [obj saveInBackgroundWithBlock:block];
}


+ (void)giveCommentGood:(SquareCommentModel *)model resultBlock:(AVBooleanResultBlock)block
{
    AVObject *obj = [AVObject objectWithClassName:DB_SQUARE_COMMENT objectId:model.objectId ];
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
