//
//  GGAlertView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGPopupView.h"

@interface GGAlertView : GGPopupView

- (instancetype)initWithFrame:(CGRect)frame title:(NSString *)title desc:(NSString *)desc cancelTitle:(NSString *)cancelTitle sureBtn:(NSString *)sureTitle;

@end
