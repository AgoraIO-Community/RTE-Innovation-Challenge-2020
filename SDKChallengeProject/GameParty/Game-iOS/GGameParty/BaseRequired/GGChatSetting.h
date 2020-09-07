//
//  GGChatSetting.h
//  GGameParty
//
//  Created by Victor on 2018/8/27.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGChatSetting : NSObject

+ (void)invokeThisMethodInApplicationWillResignActive:(UIApplication *)application;
+ (void)invokeThisMethodInApplicationWillTerminate:(UIApplication *)application;
+ (void)invokeThisMethodInApplication:(UIApplication *)application
         didReceiveRemoteNotification:(NSDictionary *)userInfo;
+ (void)invokeThisMethodAfterLoginSuccessWithClientId:(NSString *)clientId
                                              success:(LCCKVoidBlock)success
                                               failed:(LCCKErrorBlock)failed;

- (void)lcck_setupNotification;
- (void)lcck_showNotificationWithTitle:(NSString *)title
                              subtitle:(NSString *)subtitle
                                  type:(LCCKMessageNotificationType)type;



+ (instancetype)sharedInstance;
- (void)lcck_setting;
- (void)chatInit;
- (void)lcck_setupAppInfo;
- (void)lcck_setFetchProfiles;
- (void)lcck_setupBadge;
- (void)lcck_setupAvatarImageCornerRadius;
- (void)lcck_setupForceReconect;
@end
