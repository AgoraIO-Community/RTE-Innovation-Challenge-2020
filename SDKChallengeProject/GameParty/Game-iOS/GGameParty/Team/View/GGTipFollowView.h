//
//  GGTipFollowView.h
//  GGameParty
//
//  Created by Victor on 2018/8/19.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGTipFollowView : UIView

@property (nonatomic, strong) UIButton *followBtn;
@property (nonatomic, copy) void (^haveFollowUser)(GGTipFollowView *editView);
@property (nonatomic, copy) void (^closeTips)(GGTipFollowView *editView);

@property (nonatomic,strong)NSString *userId;
- (instancetype)initWithFrame:(CGRect)frame type:(NSString *)type;
@end
