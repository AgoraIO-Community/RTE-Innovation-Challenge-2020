//
//  GGGameTypeModel.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGGameTypeModel.h"

@implementation GGGameTypeModel

+ (GGGameTypeModel *)valueToObj:(AVObject *)obj
{
    GGGameTypeModel *model = [[GGGameTypeModel alloc]init];
    model.name = [obj objectForKey:@"name"];
    model.objectId = [obj objectForKey:@"objectId"];
    model.maxnum = [obj objectForKey:@"maxnum"];
    return model;
}

@end
