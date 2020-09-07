//
//  GGTipFollowView.m
//  GGameParty
//
//  Created by Victor on 2018/8/19.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTipFollowView.h"

@implementation GGTipFollowView
- (instancetype)initWithFrame:(CGRect)frame type:(NSString *)type
{
    if (self = [super initWithFrame:frame])
    {
        UILabel *label = [SJUILabelFactory labelWithText:@"   关注即可随时查看TA的组队动态" textColor:RGBCOLOR(119, 119, 119) font:[UIFont systemFontOfSize:12]];
        label.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
        //label.backgroundColor = [UIColor redColor];
        [self addSubview:label];
    
        
        
        self.followBtn = [SJUIButtonFactory buttonWithImageName:@"icon_guanbi"];
        
        self.followBtn.frame = CGRectMake(self.frame.size.width - 65, 7.5, 55, 25);
        self.followBtn.titleLabel.font = [UIFont systemFontOfSize:12];
        [self addSubview:self.followBtn];
        
        [self.followBtn addTarget:self action:@selector(closeTip) forControlEvents:UIControlEventTouchUpInside];
        if (type) {
            label.text = @"   禁止发布广告及诈骗信息等，违者将严肃处理。";
            self.followBtn.frame = CGRectMake(self.frame.size.width - 40 , 0, 40, 40);
        }
        
    }
    return self;
}

- (void)closeTip
{
    if (self.closeTips) {
        self.closeTips(self);
    }
}


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        UILabel *label = [SJUILabelFactory labelWithText:@"   关注即可随时查看TA的组队动态" textColor:RGBCOLOR(119, 119, 119) font:[UIFont systemFontOfSize:12]];
        label.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
        //label.backgroundColor = [UIColor redColor];
        [self addSubview:label];
        
        
        self.followBtn = [SJUIButtonFactory buttonWithTitle:@"关注" titleColor:Main_Color];
        self.followBtn.layer.masksToBounds = YES;
        self.followBtn.layer.cornerRadius = 13;
        self.followBtn.layer.borderWidth = 1;
        self.followBtn.layer.borderColor = Main_Color.CGColor;
        self.followBtn.frame = CGRectMake(self.frame.size.width - 65, 7.5, 55, 25);
        self.followBtn.titleLabel.font = [UIFont systemFontOfSize:12];
        [self addSubview:self.followBtn];
        
        [self.followBtn addTarget:self action:@selector(followo) forControlEvents:UIControlEventTouchUpInside];
    }
    return self;
}

- (void)followo
{
    [[AVUser currentUser]follow:self.userId andCallback:^(BOOL succeeded, NSError * _Nullable error) {
        if (self.haveFollowUser) {
            self.haveFollowUser(self);
        }
    }];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
