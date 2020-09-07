//
//  GGQuestionModel.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGQuestionModel.h"

@implementation GGQuestionModel

+ (void)queryQuestionWithGame:(NSString *)objectId withBlock:(AVArrayResultBlock)resultBlock
{
    AVQuery *query = [AVQuery queryWithClassName:DB_GAME_Question];
    [query whereKey:@"game" equalTo:objectId];
    [query findObjectsInBackgroundWithBlock:resultBlock];
}

+ (GGQuestionModel *)valueToObj:(AVObject *)obj
{
    GGQuestionModel *model = [[GGQuestionModel alloc]init];
    model.objectId = obj.objectId;
    model.game = [obj objectForKey:@"game"];
    model.option = [obj objectForKey:@"option"];
    model.anwser = [obj objectForKey:@"anwser"];
    model.question = [obj objectForKey:@"question"];
    return model;
}

@end
