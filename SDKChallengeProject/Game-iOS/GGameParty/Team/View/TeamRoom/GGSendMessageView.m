//
//  GGSendMessageView.m
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSendMessageView.h"
#import "UIView+Layout.h"
#import "UIColor+Extend.h"
@implementation GGSendMessageView

- (instancetype)init {
    return [self initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor colorWithHexString:@"FFFFF0"];
        
        _textField = [[UITextField alloc] init];
        _textField.font = [UIFont systemFontOfSize:17];
        _textField.placeholder = @" 输入消息";
        _textField.clearButtonMode = UITextFieldViewModeWhileEditing;
        _textField.layer.masksToBounds = YES;
        _textField.layer.cornerRadius = 4;
        _textField.layer.borderWidth = 0.5;
        _textField.layer.borderColor = [UIColor lightGrayColor].CGColor;
        _textField.backgroundColor = [UIColor lightTextColor];
        _textField.tintColor = [UIColor colorWithHexString:@"569EED"];
        [_textField becomeFirstResponder];
        [self addSubview:_textField];
        
        _senderButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _senderButton.userInteractionEnabled = YES;
        [_senderButton setImage:[UIImage imageNamed:@"sender"] forState:UIControlStateNormal];
        [_senderButton addTarget:self action:@selector(senderClicked:) forControlEvents:UIControlEventTouchUpInside];
        _senderButton.imageView.contentMode = UIViewContentModeCenter;
        [self addSubview:_senderButton];
        
//        [_textField becomeFirstResponder];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    CGFloat height = self.height - 20;
    CGFloat padding = 15;
    
    _senderButton.size = CGSizeMake(height, height);
    _senderButton.right = self.width - padding;
    _senderButton.centerY = self.height / 2;
    
    CGFloat spacing = 15;
    _textField.height = height;
    _textField.width = self.width - 2 * padding - _senderButton.width - spacing;
    _textField.x = 20;
    _textField.centerY = self.height / 2;
}

- (void)senderClicked:(UIButton *)sender {
    if (self.senderClickedBlock) {
        self.senderClickedBlock(self, sender);
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
