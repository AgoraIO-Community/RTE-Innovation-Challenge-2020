//
//  AppDelegate+LifeCircle.m
//  GGameParty
//
//  Created by Victor on 2020/8/24.
//  Copyright © 2020 Victor. All rights reserved.
//

#import "AppDelegate+LifeCircle.h"

@implementation AppDelegate (LifeCircle)

- (void)application:(UIApplication *)app didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    [AVOSCloud handleRemoteNotificationsWithDeviceToken:deviceToken];
}


- (void)applicationWillResignActive:(UIApplication *)application {
    [GGChatSetting invokeThisMethodInApplicationWillResignActive:application];
    
}

- (void)applicationWillTerminate:(UIApplication *)application {
    [GGChatSetting invokeThisMethodInApplicationWillTerminate:application];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    [GGChatSetting invokeThisMethodInApplication:application didReceiveRemoteNotification:userInfo];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
    NSLog(@"%@",error);
}
@end
