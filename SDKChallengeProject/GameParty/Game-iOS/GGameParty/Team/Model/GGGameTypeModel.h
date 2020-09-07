//
//  GGGameTypeModel.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGGameTypeModel : GGRootModel

@property (nonatomic,strong)NSString *name;
@property (nonatomic,strong)NSString *objectId;

@property (nonatomic,strong)NSNumber *maxnum;

+ (GGGameTypeModel *)valueToObj:(AVObject *)obj;

@end
