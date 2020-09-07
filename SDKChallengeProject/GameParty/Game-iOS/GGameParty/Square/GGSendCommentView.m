//
//  GGSendCommentView.m
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSendCommentView.h"
#import "UIView+Layout.h"
#import "UIColor+Extend.h"

@implementation GGSendCommentView



- (instancetype)init {
    return [self initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = RGBCOLOR(57, 57, 57);
        
        _textField = [[UITextField alloc] init];
        _textField.font = [UIFont systemFontOfSize:17];
        _textField.placeholder = @" 在这说说你的看法";
        _textField.clearButtonMode = UITextFieldViewModeWhileEditing;
        _textField.tintColor = [UIColor colorWithHexString:@"569EED"];
//        [_textField becomeFirstResponder];
        _textField.textColor = [UIColor whiteColor];
        [self addSubview:_textField];
        UIColor *color = RGBCOLOR(119, 119, 119);
        _textField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:_textField.placeholder attributes:@{NSForegroundColorAttributeName: color}];

        
        _senderButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _senderButton.userInteractionEnabled = YES;
        [_senderButton setTitle:@"发表" forState:UIControlStateNormal];
        [_senderButton setBackgroundColor:Main_Color];
        _senderButton.titleLabel.font = [UIFont systemFontOfSize:12];
        
        _senderButton.layer.masksToBounds = YES;
        _senderButton.layer.cornerRadius = 15;
        [_senderButton addTarget:self action:@selector(senderClicked:) forControlEvents:UIControlEventTouchUpInside];
        _senderButton.imageView.contentMode = UIViewContentModeCenter;
        [self addSubview:_senderButton];
        
        //        [_textField becomeFirstResponder];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    CGFloat height = self.height - 15;
    CGFloat padding = 10;
    
    _senderButton.size = CGSizeMake(height+20, height);
    _senderButton.right = self.width - padding;
    _senderButton.centerY = self.height / 2;
    
    CGFloat spacing = 15;
    _textField.height = height;
    _textField.width = self.width - 2 * padding - _senderButton.width - spacing;
    _textField.x = 10;
    _textField.centerY = self.height / 2;
}

- (void)senderClicked:(UIButton *)sender {
    if (self.senderClickedBlock) {
        self.senderClickedBlock(self, sender);
    }
}

@end
