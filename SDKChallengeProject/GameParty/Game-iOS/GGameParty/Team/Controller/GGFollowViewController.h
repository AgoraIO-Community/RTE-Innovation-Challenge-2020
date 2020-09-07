//
//  GGFollowViewController.h
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"

@interface GGFollowViewController : GGRootViewController
@property (nonatomic, copy) void (^userInfoClickBlock)(AVUser *user);
@property (nonatomic, copy) void (^joinTeamBlock)(GGTeamModel *model);
@property (nonatomic, copy) void (^shouldHiddenOperateView)(BOOL shouldHidden);
@end
