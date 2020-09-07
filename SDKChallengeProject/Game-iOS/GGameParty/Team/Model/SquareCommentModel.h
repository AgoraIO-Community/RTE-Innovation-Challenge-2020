//
//  SquareCommentModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"
@class SquareModel;
@interface SquareCommentModel : GGRootModel

@property (nonatomic,strong)NSString *objectId;
@property (nonatomic,strong)NSString *comment;
@property (nonatomic,strong)NSArray *isgood;
@property (nonatomic,strong)NSNumber *goodNum;

@property (nonatomic,assign)BOOL userIsGood;

@property (nonatomic,strong)AVUser *publisher;

+ (void)queryCommentPageNo:(NSInteger)page square:(SquareModel *)model resultBlock:(AVArrayResultBlock)resultBlock;
//新增评论
+ (void)addComment:(NSString *)comment square:(SquareModel *)model resultBlock:(AVBooleanResultBlock)block;
+ (void)giveCommentGood:(SquareCommentModel *)model resultBlock:(AVBooleanResultBlock)block;


@end
