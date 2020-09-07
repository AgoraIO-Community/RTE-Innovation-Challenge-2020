//
//  GGHaveFindView.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGHaveFindView : UIView
@property (nonatomic, copy) void (^closeBtnClick)(GGHaveFindView *view);

//@property (nonatomic, copy) void (^startTimerGoRoom)(GGHaveFindView *view);

- (void)changeTitle:(NSString *)title;
@end
