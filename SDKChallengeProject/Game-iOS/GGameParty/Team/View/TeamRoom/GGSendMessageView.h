//
//  GGSendMessageView.h
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGSendMessageView : UIView

@property (nonatomic, copy) void (^senderClickedBlock)(GGSendMessageView *keyboardView, UIButton *button);

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UITextField *textField;
@property (nonatomic, strong) UIButton *senderButton;

@end
