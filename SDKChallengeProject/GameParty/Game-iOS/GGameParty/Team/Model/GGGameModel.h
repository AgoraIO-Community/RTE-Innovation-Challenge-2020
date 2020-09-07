//
//  GGGameModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGGameModel : GGRootModel

@property (nonatomic,strong)NSString *gameName;

@property (nonatomic,strong)NSString *gamePlatform;

@property (nonatomic,strong)NSArray *taskType;

@property (nonatomic,strong)NSString *objectId;

@property (nonatomic,strong)NSString *titleColor;

@property (nonatomic,strong)NSString *backColor;

@property (nonatomic,strong)NSString *chatId;
@property (nonatomic,strong)AVFile *gameicon;

@property (nonatomic,strong)NSNumber *sort;

@property (nonatomic,assign)BOOL isNeedQ;

@property (nonatomic,assign)BOOL userNeedQ;

@property (nonatomic, strong) AVFile *gameBg;

+ (GGGameModel *)valueToObj:(AVObject *)obj;
+ (void)testAddNewGame;
+ (void)queryGameListWithBlock:(AVArrayResultBlock)resultBlock;

+ (void)queryGameConersationListWithBlock:(AVArrayResultBlock)resultBlock;

@end
