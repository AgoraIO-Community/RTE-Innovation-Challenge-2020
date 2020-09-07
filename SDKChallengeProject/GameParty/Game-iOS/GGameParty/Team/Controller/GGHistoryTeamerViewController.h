//
//  GGHistoryTeamerViewController.h
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"

@interface GGHistoryTeamerViewController : GGRootViewController

@property (nonatomic, copy) void (^userInfoClickBlock)(GGHistoryTeamerViewController *vc,GGUserModel *model);
@property (nonatomic, copy) void (^shouldHiddenOperateView)(BOOL shouldHidden);
@property (nonatomic,assign)BOOL isFollowView;//是不是关注的页面


@end
