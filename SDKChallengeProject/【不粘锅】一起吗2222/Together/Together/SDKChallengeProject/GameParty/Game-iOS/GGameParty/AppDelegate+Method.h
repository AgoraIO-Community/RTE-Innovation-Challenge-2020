//
//  AppDelegate+Method.h
//  GGameParty
//
//  Created by Victor on 2020/8/24.
//  Copyright © 2020 Victor. All rights reserved.
//

#import "AppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface AppDelegate (Method)<IMLSDKRestoreDelegate>

- (void)valiteUser;

- (void)checkStatus;
- (void)gameLimitUpdate:(NSNotification *)note;

- (void)gameLimitUpdate:(NSNotification *)note;

- (void)loginSuccessed;
- (void)loginOut;
- (void)checkLoginStatus;



@end

NS_ASSUME_NONNULL_END
