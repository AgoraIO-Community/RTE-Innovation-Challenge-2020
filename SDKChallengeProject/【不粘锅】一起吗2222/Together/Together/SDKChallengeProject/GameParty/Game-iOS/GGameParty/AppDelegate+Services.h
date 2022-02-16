//
//  AppDelegate+Services.h
//  GGameParty
//
//  Created by Victor on 2018/7/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "AppDelegate.h"

@interface AppDelegate (Services)<UITabBarControllerDelegate>

- (void)initChatKit;

- (void)registerForRemoteNotification;
- (void)regiserBaseServiceApplication:(UIApplication *)application WithOptions:(NSDictionary *)launchOptions;



- (void)customizeTabBarAppearance:(CYLTabBarController *)tabBarController;
- (void)customizeTabBarForController:(CYLTabBarController *)tabBarController;
- (void)setupViewControllers;

@end
