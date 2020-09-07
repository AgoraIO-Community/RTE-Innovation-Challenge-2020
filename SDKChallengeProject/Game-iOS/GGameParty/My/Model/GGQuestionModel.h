//
//  GGQuestionModel.h
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGQuestionModel : GGRootModel

@property (nonatomic,strong)NSArray *option;

@property (nonatomic,strong)NSString *question;

@property (nonatomic,strong)NSNumber *anwser;

@property (nonatomic,strong)NSString *game;

@property (nonatomic,strong)NSString *objectId;

+ (GGQuestionModel *)valueToObj:(AVObject *)obj;

+ (void)queryQuestionWithGame:(NSString *)objectId withBlock:(AVArrayResultBlock)resultBlock;

@end
