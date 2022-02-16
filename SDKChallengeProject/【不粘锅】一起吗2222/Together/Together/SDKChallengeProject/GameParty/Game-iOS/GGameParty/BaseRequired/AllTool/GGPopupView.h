//
//  GGPopupView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGPopupView : UIView

@property (nonatomic, copy) void (^closeBtnClick)(GGPopupView *view);

@property (nonatomic, strong) UILabel     *lblStatus;

@property (nonatomic, strong) UIButton    *btnClose;

@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;

@property (nonatomic,strong)UIView *backView;

@property (nonatomic,strong)UIView *waitbackView;

@end
