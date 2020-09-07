//
//  GGUserListModel.m
//  GGameParty
//
//  Created by Victor on 2018/9/17.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserListModel.h"

@implementation GGUserListModel

- (instancetype)init
{
    if (self = [super init]) {
        self.isOnline = YES;
        self.isNoMac = YES;
    }
    return self;
}
+ (GGUserListModel *)modelWithUser:(AVUser *)user
{
    GGUserListModel *model = [[GGUserListModel alloc]init];
    model.user = user;
    
    return model;
}

@end
