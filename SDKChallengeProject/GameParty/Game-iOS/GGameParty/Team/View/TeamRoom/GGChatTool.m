//
//  GGChatTool.m
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChatTool.h"
#import "IBWaterWaveView.h"
#import "Waver.h"
@implementation GGChatTool


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        CGFloat wwww = 35 * 3 + 40;
        CGFloat iconWW = 35;
        CGFloat yyyy = 10;
        self.messageBtn = [SJUIButtonFactory buttonWithImageName:@"icon_wenzi" target:self sel:@selector(btnClick:) tag:1];
        self.messageBtn.frame = CGRectMake(10, yyyy, iconWW, iconWW);
        [self addSubview:self.messageBtn];
        
        
        self.hornSwitchBtn = [SJUIButtonFactory buttonWithImageName:@"icon_yinyue" target:self sel:@selector(btnClick:) tag:2];
        
         [self.hornSwitchBtn setImage:[UIImage imageNamed:@"icon_yinyue2"] forState:UIControlStateSelected];
        self.hornSwitchBtn.frame = CGRectMake(CGRectGetMaxX(self.messageBtn.frame) + 10, yyyy, iconWW, iconWW);
        [self addSubview:self.hornSwitchBtn];
        
        
        self.sendIDBtn = [SJUIButtonFactory buttonWithImageName:@"icon_id" target:self sel:@selector(btnClick:) tag:3];
        self.sendIDBtn.frame = CGRectMake(CGRectGetMaxX(self.hornSwitchBtn.frame) + 10, yyyy, iconWW, iconWW);
        [self addSubview:self.sendIDBtn];
        
        UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(btnLong)];
          longPress.minimumPressDuration = 0.8;
        [self.sendIDBtn addGestureRecognizer:longPress];
        
//        Waver * waver = [[Waver alloc] initWithFrame:CGRectMake(CGRectGetMaxX(self.sendIDBtn.frame) + 10, yyyy, kScreenW - wwww - 10, 35)];
//        waver.backgroundColor = [UIColor whiteColor];
//        waver.waveColor = Main_Color;
//        waver.layer.masksToBounds = YES;
//        waver.layer.cornerRadius = 18;
//        __weak Waver * weakWaver = waver;
//        waver.waverLevelCallback = ^(Waver *waver) {
//            CGFloat ww = arc4random()%10000;
//            CGFloat normalizedValue  = ww/20000;
//            weakWaver.level = normalizedValue;
//        };
//        self.waver = waver;
//        [self addSubview:waver];
        
        self.macSwitchBtn = [SJUIButtonFactory buttonWithTitle:@"关闭说话" titleColor:[UIColor blackColor] backgroundColor:[UIColor whiteColor] imageName:@"" target:self sel:@selector(btnClick:) tag:4];
        self.macSwitchBtn.frame = CGRectMake(CGRectGetMaxX(self.sendIDBtn.frame) + 10, yyyy, kScreenW - wwww - 10, 35);
        [self.macSwitchBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        
        
        self.macSwitchBtn.layer.masksToBounds = YES;
        self.macSwitchBtn.layer.cornerRadius = 18;
//        self.macSwitchBtn.alpha = 0.6;
        [self.macSwitchBtn setTitle:@"点击说话" forState:UIControlStateSelected];
        [self.macSwitchBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
        
        [self addSubview:self.macSwitchBtn];
    }
    return self;
}

- (void)btnLong
{
    if ([self.v_delegate respondsToSelector:@selector(longPressSendSteamID)])
    {
        [self.v_delegate longPressSendSteamID];
    }
}

- (void)btnClick:(UIButton *)button
{
    button.selected = !button.selected;
    if (button.tag == 4) {
        if (button.selected) {
            self.macSwitchBtn.backgroundColor = [UIColor clearColor];
            self.macSwitchBtn.layer.borderColor = [UIColor whiteColor].CGColor;
            self.macSwitchBtn.layer.borderWidth = 2;
        }else
        {
            self.macSwitchBtn.backgroundColor = [UIColor whiteColor];
            self.macSwitchBtn.layer.borderWidth = 0;
        }
    }
    if ([self.v_delegate respondsToSelector:@selector(clickTool:button:)])
    {
        [self.v_delegate clickTool:button.tag button:button];
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
