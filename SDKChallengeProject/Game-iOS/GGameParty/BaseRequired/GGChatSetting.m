//
//  GGChatSetting.m
//  GGameParty
//
//  Created by Victor on 2018/8/27.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChatSetting.h"
#import "LCCKConversationListViewModel.h"
#import "GGUserInfoViewController.h"
@interface GGChatSetting()
@property (nonatomic, strong) LCCKConversationListViewModel *model ;

@end

@implementation GGChatSetting
#pragma mark - 胶水函数
+ (void)invokeThisMethodInApplicationWillResignActive:(UIApplication *)application
{
    [[LCChatKit sharedInstance] syncBadge];
}

+ (void)invokeThisMethodInApplicationWillTerminate:(UIApplication *)application
{
    [[LCChatKit sharedInstance] syncBadge];
}

+ (void)invokeThisMethodInApplication:(UIApplication *)application
         didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    if (application.applicationState == UIApplicationStateActive) {
        // 应用在前台时收到推送，只能来自于普通的推送，而非离线消息推送
    } else {
        /*!
         *  当使用 https://github.com/leancloud/leanchat-cloudcode 云代码更改推送内容的时候
         {
         aps = {
         alert = "lcckkit : sdfsdf";
         badge = 4;
         sound = default;
         };
         convid = 55bae86300b0efdcbe3e742e;
         }
         */
        [[LCChatKit sharedInstance] didReceiveRemoteNotification:userInfo];
    }
}



+ (void)invokeThisMethodAfterLoginSuccessWithClientId:(NSString *)clientId
                                              success:(LCCKVoidBlock)success
                                               failed:(LCCKErrorBlock)failed
{
    [[self sharedInstance] chatInit];

    [[LCChatKit sharedInstance] openWithClientId:clientId
                                        callback:^(BOOL succeeded, NSError *error) {
                                            if (succeeded) {
                                                //[self saveLocalClientInfo:clientId];
                                                !success ?: success();
                                            } else {
                                                !failed ?: failed(error);
                                            }
                                        }];
}

- (void)lcck_setupNotification {
    [[LCChatKit sharedInstance] setShowNotificationBlock:^(UIViewController *viewController, NSString *title,
                                                           NSString *subtitle, LCCKMessageNotificationType type) {
        [self lcck_showNotificationWithTitle:title subtitle:subtitle type:type];
    }];
}
- (void)lcck_showNotificationWithTitle:(NSString *)title
                                     subtitle:(NSString *)subtitle
                                         type:(LCCKMessageNotificationType)type {
  //  [LCCKUtil showNotificationWithTitle:title subtitle:subtitle type:type];
    NSLog(@"收到消息%@",title);
}

#pragma mark - chatKit初始化
- (void)lcck_setting
{
    [self lcck_setupAppInfo];
    
    [self lcck_setFetchProfiles];
    
    [self lcck_setupBadge];
    
    [self lcck_setupAvatarImageCornerRadius];
        
    [self lcck_setupForceReconect];
    
    [self initUnreadCount];
    
    LCCKConversationListViewModel *model = [[LCCKConversationListViewModel alloc]initWithConversationListViewController:[LCCKConversationListViewController new]];
    
    [[LCCKConversationListService sharedInstance]setMarkBadgeWithTotalUnreadCountBlock:^(NSInteger totalUnreadCount, __kindof UIViewController *controller) {
        [self lcck_markBadgeWithTotalUnreadCount:totalUnreadCount controller:controller];
    }];
    self.model = model;
    
}

- (void)initUnreadCount
{
    NSString *url = [NSString stringWithFormat:@"https://nsnwnyel.api.lncld.net/1.2/rtm/clients/%@/unread-count",[AVUser currentUser].objectId];
    AFHTTPSessionManager *manger = [AFHTTPSessionManager manager];
    [manger.requestSerializer setValue:@"NSnWnyEL3aKpDdMA5Co9Jbcy-gzGzoHsz" forHTTPHeaderField:@"X-LC-Id"];
    [manger.requestSerializer setValue:@"FTQCB3XNYjvWaiivrg2DNSRj" forHTTPHeaderField:@"X-LC-Key"];
    [manger GET:url parameters:nil progress:^(NSProgress * _Nonnull downloadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"%@",responseObject);
        NSInteger totalUnreadCount = [[responseObject objectForKey:@"count"] integerValue];
        
        [self lcck_markBadgeWithTotalUnreadCount:totalUnreadCount controller:nil];
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"%@",error);
    }];
}

- (void)lcck_setupBadge
{
    
    [[LCChatKit sharedInstance] setMarkBadgeWithTotalUnreadCountBlock:^(
                                                                        NSInteger totalUnreadCount, UIViewController *controller) {
        [self lcck_markBadgeWithTotalUnreadCount:totalUnreadCount controller:controller];
    }];
}

- (void)lcck_markBadgeWithTotalUnreadCount:(NSInteger)totalUnreadCount
                                       controller:(UIViewController *)controller {
    
    AppDelegate *app = (AppDelegate*)[UIApplication sharedApplication].delegate;
    UINavigationController *nav =  (UINavigationController *)[app.tabbarContoller.viewControllers objectAtIndex:1];
    if (totalUnreadCount > 0) {
        NSString *badgeValue = [NSString stringWithFormat:@"%ld", (long)totalUnreadCount];
        if (totalUnreadCount > 99) {
            badgeValue = LCCKBadgeTextForNumberGreaterThanLimit;
        }
        nav.cyl_tabBarController.tabBarItem.badgeValue = totalUnreadCount == 0?@"":[NSString stringWithFormat:@"%ld",(long)totalUnreadCount];
        [nav tabBarItem].badgeValue = badgeValue;
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:totalUnreadCount];
    } else {
        [nav tabBarItem].badgeValue = nil;
        [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    }
    
   
}



- (void)chatInit
{
    [self lcck_setting];
}

+ (instancetype)sharedInstance
{
    static GGChatSetting *_sharedLCChatKitExample = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _sharedLCChatKitExample = [[self alloc] init];
    });
    return _sharedLCChatKitExample;
}

- (void)lcck_setupAppInfo {
#ifndef __OPTIMIZE__
//    [[LCChatKit sharedInstance] setUseDevPushCerticate:YES];
#endif
    
    [[LCChatKit sharedInstance]setDisablePreviewUserId:YES];
    [LCChatKit setAppId:LEANCLOUDAPPID appKey:LEANCLOUDCLIENTKEY];
}

- (void)lcck_setFetchProfiles {
    
    [[LCCKUserSystemService sharedInstance]setFetchProfilesBlock:^(NSArray<NSString *> *userIds, LCCKFetchProfilesCompletionHandler completionHandler) {
        if (userIds.count == 0)
        {
            return;
        }
        NSMutableArray *users = [NSMutableArray arrayWithCapacity:userIds.count];
        for (NSString *clientId in userIds) {
            //查询 _User 表需开启 find 权限
            AVQuery *userQuery = [AVQuery queryWithClassName:DB_USER];
            AVObject *user = [userQuery getObjectWithId:clientId];
            if (user) {
                //"avatar" 是 _User 表的头像字段
                AVFile *file = [user objectForKey:@"avatar"];
                GGUserModel *user_ = [GGUserModel userWithUserId:user.objectId name:[user objectForKey:@"username"] avatarURL:[NSURL URLWithString:file.url] clientId:clientId];
                [users addObject:user_];
            }else{
                //注意：如果网络请求失败，请至少提供 ClientId！
                GGUserModel *user_ = [GGUserModel userWithClientId:clientId];
                [users addObject:user_];
            }
        }
        !completionHandler ?: completionHandler([users copy], nil);
    }];
    
    [[LCChatKit sharedInstance] setOpenProfileBlock:^(NSString *userId, id<LCCKUserDelegate> user, __kindof UIViewController *parentController) {
        if (!userId) {
            //用户不存在
        }else{
            NSString *currentClientId = [LCChatKit sharedInstance].clientId;
//            AppDelegate * app = (AppDelegate*)[UIApplication sharedApplication].delegate;
            GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
            view.hidesBottomBarWhenPushed = YES;
            view.objectId = userId;
            [parentController.navigationController pushViewController:view animated:YES];
        }
    }];
    
    [[LCChatKit sharedInstance] setFetchProfilesBlock:^(NSArray<NSString *> *userIds,
                                                        LCCKFetchProfilesCompletionHandler completionHandler) {
        if (userIds.count == 0)
        {
            return;
        }
        NSMutableArray *users = [NSMutableArray arrayWithCapacity:userIds.count];
        for (NSString *clientId in userIds) {
            //查询 _User 表需开启 find 权限
            AVQuery *userQuery = [AVQuery queryWithClassName:DB_USER];
            AVObject *user = [userQuery getObjectWithId:clientId];
            if (user) {
                //"avatar" 是 _User 表的头像字段
                AVFile *file = [user objectForKey:@"avatar"];
                GGUserModel *user_ = [GGUserModel userWithUserId:user.objectId name:[user objectForKey:@"username"] avatarURL:[NSURL URLWithString:file.url] clientId:clientId];
                [users addObject:user_];
            }else{
                //注意：如果网络请求失败，请至少提供 ClientId！
                GGUserModel *user_ = [GGUserModel userWithClientId:clientId];
                [users addObject:user_];
            }
        }
        !completionHandler ?: completionHandler([users copy], nil);
    }];
}

- (void)lcck_setupAvatarImageCornerRadius {
    [[LCChatKit sharedInstance] setAvatarImageViewCornerRadiusBlock:^CGFloat(CGSize avatarImageViewSize) {
        if (avatarImageViewSize.height > 0) {
            return avatarImageViewSize.height / 2;
        }
        return 5;
    }];
}


- (void)lcck_setupForceReconect
{
    [[LCChatKit sharedInstance] setForceReconnectSessionBlock:
     ^(NSError *aError, BOOL granted, __kindof UIViewController *viewController, LCCKReconnectSessionCompletionHandler completionHandler) {
         BOOL isSingleSignOnOffline = (aError.code == 4111);
         if (isSingleSignOnOffline) {
             if (granted == YES)
             {
                 NSString *clientId = [AVUser currentUser].objectId;
                [[LCChatKit sharedInstance] openWithClientId:clientId
                                                        force:granted
                                                     callback:
                  ^(BOOL succeeded, NSError *error) {
                      
                      !completionHandler ?: completionHandler(succeeded, error);
                  }];
                 return;
             }
             else
             {
                 [GGNotificationCenter postNotificationName:LOGIN_OUT_NOTIFATION object:nil];//退出登录
                 !completionHandler ?: completionHandler(YES, nil);
                 return;
             }
         }
     }];
}

@end
