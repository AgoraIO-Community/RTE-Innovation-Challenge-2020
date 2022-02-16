//
//  AppDelegate+Services.m
//  GGameParty
//
//  Created by Victor on 2018/7/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "AppDelegate+Services.h"
#import "GGGameModel.h"
#import <UserNotifications/UserNotifications.h>
#import "GGNewMessageViewController.h"
//#import "AVOSCloudSNS.h"
@implementation AppDelegate (Services)

///构造自定义参数,上传到mob,然后获得ID,放在网页后面作为参数.


- (void)initChatKit
{
    
    [[GGChatSetting sharedInstance]chatInit];
    AVIMClient *client = [[AVIMClient alloc]initWithUser:[AVUser currentUser]tag:@"iOS"];
    [[LCChatKit sharedInstance] openWithClientId:client.clientId callback:^(BOOL succeeded, NSError *error) {
        if (succeeded)
        {
            NSLog(@"成功连接ID为:=====%@",[LCChatKit sharedInstance].clientId);
         
        }
        else
        {
            NSLog(@"error === %@",error);
        }
    }];
}

- (void)registerForRemoteNotification
{
    // iOS10 兼容
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 10.0) {
        // 使用 UNUserNotificationCenter 来管理通知
        UNUserNotificationCenter *uncenter = [UNUserNotificationCenter currentNotificationCenter];
        // 监听回调事件
        [uncenter setDelegate:self];
        //iOS10 使用以下方法注册，才能得到授权
        [uncenter requestAuthorizationWithOptions:(UNAuthorizationOptionAlert+UNAuthorizationOptionBadge+UNAuthorizationOptionSound)
                                completionHandler:^(BOOL granted, NSError * _Nullable error) {
                                    dispatch_async(dispatch_get_main_queue(), ^{
                                        [[UIApplication sharedApplication] registerForRemoteNotifications];
                                    });
                                    
                                    //TODO:授权状态改变
                                    NSLog(@"%@" , granted ? @"授权成功" : @"授权失败");
                                }];
        // 获取当前的通知授权状态, UNNotificationSettings
        [uncenter getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
            NSLog(@"%s\nline:%@\n-----\n%@\n\n", __func__, @(__LINE__), settings);
            /*
             UNAuthorizationStatusNotDetermined : 没有做出选择
             UNAuthorizationStatusDenied : 用户未授权
             UNAuthorizationStatusAuthorized ：用户已授权
             */
            if (settings.authorizationStatus == UNAuthorizationStatusNotDetermined) {
                NSLog(@"未选择");
            } else if (settings.authorizationStatus == UNAuthorizationStatusDenied) {
                NSLog(@"未授权");
            } else if (settings.authorizationStatus == UNAuthorizationStatusAuthorized) {
                NSLog(@"已授权");
            }
        }];
    }
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
        UIUserNotificationType types = UIUserNotificationTypeAlert |
        UIUserNotificationTypeBadge |
        UIUserNotificationTypeSound;
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:types categories:nil];
        
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    } else {
        UIRemoteNotificationType types = UIRemoteNotificationTypeBadge |
        UIRemoteNotificationTypeAlert |
        UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:types];
    }
#pragma clang diagnostic pop
}

- (void)regiserBaseServiceApplication:(UIApplication *)application WithOptions:(NSDictionary *)launchOptions
{
    
    [AVOSCloud setApplicationId:LEANCLOUDAPPID clientKey:LEANCLOUDCLIENTKEY];
    [AVOSCloud setAllLogsEnabled:NO];
    [LCCKInputViewPluginTakePhoto registerSubclass];
    [LCCKInputViewPluginPickImage registerSubclass];
    [LCCKInputViewPluginLocation registerSubclass];
    [AVIMClient setUnreadNotificationEnabled:YES];
    [AVIMClient setTimeoutIntervalInSeconds:20];
    
    [ShareSDK registerActivePlatforms:@[
                                        @(SSDKPlatformTypeWechat),
                                        @(SSDKPlatformTypeQQ),
                                        ]
                             onImport:^(SSDKPlatformType platformType)
     {
         switch (platformType)
         {
             case SSDKPlatformTypeWechat:
                 [ShareSDKConnector connectWeChat:[WXApi class]];
                 break;
             case SSDKPlatformTypeQQ:
                 [ShareSDKConnector connectQQ:[QQApiInterface class] tencentOAuthClass:[TencentOAuth class]];
                 break;
             default:
                 break;
         }
     }
                      onConfiguration:^(SSDKPlatformType platformType, NSMutableDictionary *appInfo)
     {
         
         switch (platformType)
         {
                 //wxa3eacc1c86a717bc b5bf245970b2a451fb8cebf8a6dff0c1
             case SSDKPlatformTypeWechat:
                 [appInfo SSDKSetupWeChatByAppId:GGWECHATAPPKEY
                                       appSecret:GGWECHATAPPSERC];
                 
//                 [appInfo SSDKSetupWeChatByAppId:@"wxa3eacc1c86a717bc"
                               //        appSecret:@"b5bf245970b2a451fb8cebf8a6dff0c1"];
                 break;
             case SSDKPlatformTypeQQ:
                 [appInfo SSDKSetupQQByAppId:GGQQAPPKEY
                                      appKey:GGQQAPPSERC
                                    authType:SSDKAuthTypeBoth];

                 break;
             default:
                 break;
         }
     }];
}



#pragma mark - initTabbar
- (void)setupViewControllers
{
    GGTeamViewController *firstViewController = [[GGTeamViewController alloc]init];
    
    RTRootNavigationController *firstNavigationController = [[RTRootNavigationController alloc]
                                                   initWithRootViewController:firstViewController];
    
    
    GGNewMessageViewController *secondViewController = [[GGNewMessageViewController alloc] init];
    RTRootNavigationController *secondNavigationController = [[RTRootNavigationController alloc]
                                                    initWithRootViewController:secondViewController];
    
    GGSquareViewController *thirdViewController = [[GGSquareViewController alloc] init];
    RTRootNavigationController *thirdNavigationController = [[RTRootNavigationController alloc]
                                                   initWithRootViewController:thirdViewController];
    GGMyViewController *myViewController = [[GGMyViewController alloc] init];
    RTRootNavigationController *myNavigationController = [[RTRootNavigationController alloc]
                                                initWithRootViewController:myViewController];
    
    
    CYLTabBarController *tabBarController = [[CYLTabBarController alloc] init];
    
    [self customizeTabBarForController:tabBarController];
    [self customizeTabBarAppearance:tabBarController];
    
    [tabBarController setViewControllers:@[
                                           firstNavigationController,
                                           thirdNavigationController,
                                           secondNavigationController,
                                           myNavigationController
                                           ]];
    self.tabbarContoller = tabBarController;
    self.tabbarContoller.delegate = self;
    
    self.currentNav = firstNavigationController;
}

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
{
    RTRootNavigationController *nav = (RTRootNavigationController *)viewController;
  //JPSEInstance.navCtr = nav;
    self.currentNav = nav;
}

- (void)customizeTabBarForController:(CYLTabBarController *)tabBarController {
    
    NSDictionary *dict1 = @{
                            CYLTabBarItemTitle : @"大厅",
                            CYLTabBarItemImage : @"icon_zudui2",
                            CYLTabBarItemSelectedImage : @"icon_zudui",
                            };
    NSDictionary *dict2 = @{
                            CYLTabBarItemTitle : @"消息",
                            CYLTabBarItemImage : @"icon_xiaoxi2",
                            CYLTabBarItemSelectedImage : @"icon_xiaoxi",
                            };
    NSDictionary *dict3 = @{
                            CYLTabBarItemTitle : @"广场",
                            CYLTabBarItemImage : @"icon_guangchang2",
                            CYLTabBarItemSelectedImage : @"icon_guangchang",
                            };
    NSDictionary *dict4 = @{
                            CYLTabBarItemTitle : @"我的",
                            CYLTabBarItemImage : @"icon_wode2",
                            CYLTabBarItemSelectedImage : @"icon_wode",
                            };
    
    NSArray *tabBarItemsAttributes = @[ dict1,dict3,dict2,dict4 ];
    tabBarController.tabBarItemsAttributes = tabBarItemsAttributes;
    tabBarController.view.backgroundColor = [UIColor whiteColor];
    
}

- (void)customizeTabBarAppearance:(CYLTabBarController *)tabBarController
{
    NSMutableDictionary *normalAttrs = [NSMutableDictionary dictionary];
    normalAttrs[NSForegroundColorAttributeName] = [UIColor grayColor];
    
    NSMutableDictionary *selectedAttrs = [NSMutableDictionary dictionary];
    selectedAttrs[NSForegroundColorAttributeName] = Main_Color;
    
    UITabBarItem *tabBar = [UITabBarItem appearance];
    [tabBar setTitleTextAttributes:normalAttrs forState:UIControlStateNormal];
    [tabBar setTitleTextAttributes:selectedAttrs forState:UIControlStateSelected];
    
    [[UITabBar appearance] setBackgroundImage:[UIImage imageNamed:@"bgtab"]];
    [[UITabBar appearance] setBackgroundColor:GGBackGround_Color];
    [[UITabBar appearance] setShadowImage:[[UIImage alloc] init]];
}



@end

