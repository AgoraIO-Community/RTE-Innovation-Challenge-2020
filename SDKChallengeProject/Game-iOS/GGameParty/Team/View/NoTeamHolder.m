//
//  NoTeamHolder.m
//  GGameParty
//
//  Created by Victor on 2018/9/5.
//  Copyright © 2018年 Victor. All rights reserved.
//




float const width_displayNoWifiView  = 200.0 ;
float const height_displayNoWifiView = 80 ;

float const width_labelshow          = 180.0 ;
float const height_labelshow         = 50 ;
float const fontSize_labelshow       = 12 ;

float const flexY_lb_bt              = 10.0 ;

float const width_bt                 = 60.0 ;
float const height_bt                = 30.0 ;
float const fontSize_bt              = 15.0 ;

#import "NoTeamHolder.h"

@interface NoTeamHolder ()

@property (nonatomic, strong) UIImageView *nowifiImgView ;
@property (nonatomic, strong) UILabel *lb ;
@property (nonatomic, strong) UIButton *bt ;
@property (nonatomic, copy) ReloadButtonClickBlock reloadButtonClickBlock ;

@end



@implementation NoTeamHolder


- (void)showInView:(UIView *)viewWillShow {
    [viewWillShow addSubview:self] ;
}

- (void)dismiss {
    [self removeFromSuperview] ;
}

- (instancetype)initWithFrame:(CGRect)frame
                  reloadBlock:(ReloadButtonClickBlock)reloadBlock {
    self = [super initWithFrame:frame];
    if (self) {
        self.reloadButtonClickBlock = reloadBlock ;
        [self setup] ;
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews] ;
    
    CGRect rectWifi = CGRectZero ;
    rectWifi.size = CGSizeMake(width_displayNoWifiView, height_displayNoWifiView) ;
    rectWifi.origin.x = (self.frame.size.width - width_displayNoWifiView) / 2.0 ;
    rectWifi.origin.y = (self.frame.size.height - height_displayNoWifiView - height_labelshow - flexY_lb_bt - height_bt) / 2.0  - 50;
    self.nowifiImgView.frame = rectWifi ;
    
    CGRect rectLabel = CGRectZero ;
    rectLabel.origin.x = (self.frame.size.width - width_labelshow) / 2.0 ;
    rectLabel.origin.y = rectWifi.origin.y + rectWifi.size.height ;
    rectLabel.size = CGSizeMake(width_labelshow, height_labelshow) ;
    self.lb.frame = rectLabel ;
    
    CGRect rectButton = CGRectZero ;
    rectButton.origin.x = (self.frame.size.width - width_bt) / 2.0 ;
    rectButton.origin.y = rectLabel.origin.y + rectLabel.size.height + flexY_lb_bt ;
    rectButton.size = CGSizeMake(width_bt, height_bt) ;
    self.bt.frame = rectButton ;
}

- (void)changeTitle:(NSString *)title
{
    _lb.text =title; ;
}

- (void)setup {
    [self configure] ;
    [self nowifiImgView] ;
    [self lb] ;
    [self bt] ;
}

- (void)configure {
    self.backgroundColor = [UIColor whiteColor] ;
}

- (UIImageView *)nowifiImgView {
    if (!_nowifiImgView) {
        _nowifiImgView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"bg_noteam"]] ;
        _nowifiImgView.contentMode = UIViewContentModeCenter ;
        if (![_nowifiImgView superview]) {
            [self addSubview:_nowifiImgView] ;
        }
    }
    return _nowifiImgView ;
}

- (UILabel *)lb {
    if (!_lb) {
        _lb = [[UILabel alloc] init] ;
        _lb.text = @"没有相关房间,你可以创建房间或刷新看看" ;
        _lb.font = [UIFont boldSystemFontOfSize:fontSize_labelshow] ;
        _lb.textAlignment = NSTextAlignmentCenter ;
        _lb.numberOfLines = 2;
        _lb.textColor = RGBCOLOR(170, 170, 170);
        if (![_lb superview]) {
            [self addSubview:_lb] ;
        }
    }
    return _lb ;
}

- (UIButton *)bt {
    if (!_bt) {
        _bt = [[UIButton alloc] init] ;
        [_bt setTitle:@"刷新" forState:0] ;
        [_bt setTitleColor:Main_Color forState:0] ;
        _bt.titleLabel.font = [UIFont systemFontOfSize:fontSize_bt] ;
        _bt.layer.cornerRadius = 15 ;
        _bt.layer.borderWidth = 1.0f ;
        _bt.layer.borderColor = Main_Color.CGColor ;
        [_bt addTarget:self action:@selector(reloadButtonClicked) forControlEvents:UIControlEventTouchUpInside] ;
        if (![_bt superview]) {
            [self addSubview:_bt] ;
        }
    }
    return _bt ;
}

- (void)reloadButtonClicked {
    self.reloadButtonClickBlock() ;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
