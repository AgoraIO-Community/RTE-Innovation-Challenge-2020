//
//  GGSysMesModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGSysMesModel : GGRootModel


@property (nonatomic,strong)NSDate *createdAt;
@property (nonatomic,strong)NSString *type;
@property (nonatomic,strong)NSString *title;//publishRange
@property (nonatomic,strong)NSString *publishRange;
@property (nonatomic,strong)AVFile *cover;
@property (nonatomic,strong)NSString *content;
@property (nonatomic,strong)AVUser *relevantPersonne;//relevantPersonne
@property (nonatomic,strong)AVUser *user;


+ (GGSysMesModel *)valueToObj:(AVObject *)obj;

+ (void)querySysMessageWithBlock:(AVArrayResultBlock)resultBlock;


@end
