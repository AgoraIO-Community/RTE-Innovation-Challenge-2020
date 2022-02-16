//
//  AppDelegate.m
//  GGameParty
//
//  Created by Victor on 2018/7/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "AppDelegate.h"
#import "AppDelegate+Services.h"
#import "GGConversionListViewController.h"
#import "AppDelegate+AppTools.h"
#import "zhPopupController.h"
#import "HKFloatManager.h"
#import "GGNewMessageViewController.h"
#import <MobLink/MobLink.h>
#import "WMDragView.h"
#import "AppDelegate+Method.h"
#import "AppDelegate+LifeCircle.h"

@interface AppDelegate ()<IMLSDKRestoreDelegate>

@end

@implementation AppDelegate

#pragma mark - app.lifecircle
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    self.window = [[UIWindow alloc]initWithFrame:[UIScreen mainScreen].bounds];
    
    self.window.backgroundColor = [UIColor whiteColor];
    
    [self regiserBaseServiceApplication:application WithOptions:launchOptions];
    
    [self checkLoginStatus];
  
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loginSuccessed) name:LOGIN_SUCCESSED_NOTIFATION object:nil];//登录成功的通知
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loginOut) name:LOGIN_OUT_NOTIFATION object:nil];//退出登录通知
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(gameLimitUpdate:) name:GGUSER_GAME_lIMITS_UPDATE object:nil];//用户游戏权限更新通知
    [self.window makeKeyAndVisible];
    
    return YES;
}




@end
