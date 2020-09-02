//
//  GGSendCommentView.h
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGSendCommentView : UIView
@property (nonatomic, copy) void (^senderClickedBlock)(GGSendCommentView *keyboardView, UIButton *button);

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UITextField *textField;
@property (nonatomic, strong) UIButton *senderButton;


@end
