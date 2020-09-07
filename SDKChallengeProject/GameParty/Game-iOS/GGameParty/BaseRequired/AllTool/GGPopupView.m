//
//  GGPopupView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGPopupView.h"

@implementation GGPopupView



- (UIView *)waitbackView
{
    if (!_waitbackView)
    {
        self.waitbackView = [UIView new];
        self.waitbackView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.waitbackView.layer.cornerRadius = 10;
        self.waitbackView.clipsToBounds = YES;
        self.waitbackView.backgroundColor = [UIColor whiteColor];
    }
    return _waitbackView;
}

- (UIActivityIndicatorView *)activityIndicator
{
    if (!_activityIndicator)
    {
        self.activityIndicator = [[UIActivityIndicatorView alloc]initWithFrame:self.waitbackView.bounds];
        self.activityIndicator.backgroundColor = [UIColor whiteColor];
        self.activityIndicator.activityIndicatorViewStyle =  UIActivityIndicatorViewStyleWhiteLarge;
        [self.waitbackView addSubview:self.activityIndicator];
        self.activityIndicator.color = Main_Color;
        [self.activityIndicator startAnimating];
    }
    return _activityIndicator;
}

- (UIView *)backView
{
    if (!_backView)
    {
        self.backView = [UIView new];
//        [self addSubview:self.backView];
        self.backView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.backView.layer.cornerRadius = 10;
        self.backView.clipsToBounds = YES;
        self.backView.backgroundColor = [UIColor whiteColor];
    }
    return _backView;
}

- (UIButton *)btnClose
{
    if (!_btnClose)
    {
        self.btnClose = [UIButton mm_buttonWithTarget:self action:@selector(actionClose)];
        [self.backView addSubview:self.btnClose];
        self.btnClose.frame = CGRectMake(self.frame.size.width - 50, 5, 40, 40);
        //  [self.btnClose setTitle:@"Close" forState:UIControlStateNormal];
        [self.btnClose setImage:[UIImage imageNamed:@"icon_guanbi"] forState:UIControlStateNormal];
        [self.btnClose setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        self.btnClose.titleLabel.font = [UIFont systemFontOfSize:14];
        
    }
    return _btnClose;
}

- (UILabel *)lblStatus
{
    if (!_lblStatus)
    {
        self.lblStatus = [UILabel new];
//        [self.backView addSubview:self.lblStatus];
        self.lblStatus.frame = CGRectMake(16, 16, 145, 26);
        self.lblStatus.textColor = MMHexColor(0x333333FF);
        self.lblStatus.font = [UIFont boldSystemFontOfSize:23];
        //self.lblStatus.text = @"房间设置";
        self.lblStatus.textAlignment = NSTextAlignmentLeft;
    }
    return _lblStatus;
}


- (void)actionClose
{
    //  [self hide];
    if (self.closeBtnClick)
    {
        self.closeBtnClick(self);
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
