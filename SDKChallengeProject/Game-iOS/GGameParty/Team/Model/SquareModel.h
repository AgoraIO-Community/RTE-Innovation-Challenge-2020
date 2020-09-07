//
//  SquareModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface SquareModel : GGRootModel

@property (nonatomic,strong)NSString *type;
@property (nonatomic,strong)NSString *createAt;
@property (nonatomic,strong)NSDictionary *publisher;
@property (nonatomic,strong)NSString *desc;
@property (nonatomic,strong)NSString *objectId;
@property (nonatomic,strong)NSString *title;
@property (nonatomic,strong)NSString *shareUrl;
@property (nonatomic,strong)AVFile *videoCover;//视频封面
@property (nonatomic,strong)AVFile *url;//视频地址
@property (nonatomic,strong)NSArray *isgood;//视频地址

@property (nonatomic,strong)NSNumber *commentNum;
@property (nonatomic,strong)NSNumber *goodNum;

+ (void)querySqueryDataPageNo:(NSInteger)page resultBlock:(AVArrayResultBlock)resultBlock;
+ (void)giveGood:(SquareModel *)model resultBlock:(AVBooleanResultBlock)block;



@end
