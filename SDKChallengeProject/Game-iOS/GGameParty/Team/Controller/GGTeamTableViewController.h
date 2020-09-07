//
//  GGTeamTableViewController.h
//  GGameParty
//
//  Created by Victor on 2018/7/26.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
#import "GGGameModel.h"
#import "ZJScrollPageView.h"
@interface GGTeamTableViewController : GGRootViewController<ZJScrollPageViewChildVcDelegate>

@property (nonatomic,strong)GGGameModel *gameModel;
@property (nonatomic, copy) void (^shouldHiddenOperateView)(BOOL shouldHidden);

@end
