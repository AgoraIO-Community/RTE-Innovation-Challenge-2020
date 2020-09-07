//
//  AppDelegate+Method.m
//  GGameParty
//
//  Created by Victor on 2020/8/24.
//  Copyright © 2020 Victor. All rights reserved.
//

#import "AppDelegate+Method.h"
#import "AppDelegate+Services.h"
#import "AppDelegate+AppTools.h"
@implementation AppDelegate (Method)


#pragma mark - Method


- (void)checkLoginStatus
{
    AVUser *user = [AVUser currentUser];
       if (user)
       {
           [self registerForRemoteNotification];
           [self loginSuccessed];
           [user refreshInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
               if (error){
                   [XHToast showCenterWithText:@"登录信息已过期,请重新登录"];
                   GGLoginViewController *login = [[GGLoginViewController alloc]init];
                   self.window.rootViewController = login;
               }
           }];
       }
       else
       {
           GGLoginViewController *login = [[GGLoginViewController alloc]init];
           self.window.rootViewController = login;
       }
}

/**
 校验用户登录有效期
 */
- (void)valiteUser
{
    AVUser *user = [AVUser currentUser];
    [user isAuthenticatedWithSessionToken:user.sessionToken callback:^(BOOL succeeded, NSError * _Nullable error) {
        if (!succeeded) {
            // 用户的 sessionToken 无效
            [self loginOut];
            GGLoginViewController *login = [[GGLoginViewController alloc]init];
            self.window.rootViewController = login;
        }
    }];
}

/**
 检查用户被封状态
 */
- (void)checkStatus
{
    AVUser *user = [AVUser currentUser];
    if ([[user objectForKey:@"delete"]isEqual:@(YES)])
    {
        [self loginOut];
    }
}


/**
 游戏答题权限更新
 */
- (void)gameLimitUpdate:(NSNotification *)note
{
    [[AVUser currentUser]refresh];
}

/**
 登录成功
 */
- (void)loginSuccessed
{
    [self registerForRemoteNotification];
    [MobLink setDelegate:self];
    [self initChatKit];
    [self checkStatus];
    [self valiteUser];//验证用户sessionToken是否有效
    [self setupViewControllers];
    self.window.rootViewController = self.tabbarContoller;
}

/**
 登出
 */
- (void)loginOut
{
    [AVUser logOut];
    [[LCChatKit sharedInstance]closeWithCallback:^(BOOL succeeded, NSError *error) {
        
    }];
    GGLoginViewController *login = [[GGLoginViewController alloc]init];
    self.window.rootViewController = login;
    self.tabbarContoller = nil;
}



#pragma mark - 场景还原.deledate
- (void)IMLSDKStartCheckScene {
    NSLog(@"Start Check Scene");
}

- (void)IMLSDKEndCheckScene {
    NSLog(@"End Check Scene");
}

-(void)IMLSDKWillRestoreScene:(MLSDKScene *)scene Restore:(void (^)(BOOL, RestoreStyle))restoreHandler
{
    NSLog(@"Will Restore Scene - Path:%@",scene.path);
}


@end
