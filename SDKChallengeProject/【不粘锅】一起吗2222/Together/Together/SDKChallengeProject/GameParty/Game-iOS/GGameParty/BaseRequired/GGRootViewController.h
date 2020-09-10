//
//  GGRootViewController.h
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RTRootNavigationController.h"
#import "GGCustomNavView.h"
#import "GGShareModel.h"
@interface GGRootViewController : UIViewController

@property (nonatomic,strong)GGCustomNavView *ggNavView;
- (void)showNormalGoAnwserAlert;
- (void)initNavView;
- (void)shareWithShareDict:(GGShareModel *)model;
- (void)analyticsEventStatistics:(NSString *)event;

@end
