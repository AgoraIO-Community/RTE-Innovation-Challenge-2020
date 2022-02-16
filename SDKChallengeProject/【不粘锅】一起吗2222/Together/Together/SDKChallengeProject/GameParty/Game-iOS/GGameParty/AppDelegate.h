//
//  AppDelegate.h
//  GGameParty
//
//  Created by Victor on 2018/7/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FloatingWindow.h"
#import "CYLTabBarController.h"
@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic)CYLTabBarController *tabbarContoller;

@property (strong, nonatomic)UINavigationController *currentNav;




@end

