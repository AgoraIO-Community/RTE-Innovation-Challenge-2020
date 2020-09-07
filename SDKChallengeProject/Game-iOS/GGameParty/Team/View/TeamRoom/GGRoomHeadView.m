//
//  GGRoomHeadView.m
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRoomHeadView.h"

@implementation GGRoomHeadView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.titleLabel = [SJUILabelFactory labelWithText:@"快来人啊" textColor:[UIColor whiteColor] font:[UIFont boldSystemFontOfSize:18]];
        self.titleLabel.frame = CGRectMake(15, 50, kScreenW - 30 -60, 30);
        [self addSubview:self.titleLabel];
        
        
        UIView *bbb = [[UIView alloc]initWithFrame:CGRectMake(15, CGRectGetMaxY(self.titleLabel.frame), 60, 15)];
        bbb.backgroundColor = [UIColor whiteColor];
        bbb.alpha = 0.6;
        bbb.layer.masksToBounds = YES;
        bbb.layer.cornerRadius = 3;
        [self addSubview:bbb];
        
        self.gameLabel = [SJUILabelFactory labelWithText:@"绝地球赛" textColor:[UIColor blackColor] font:[UIFont systemFontOfSize:12]];
        self.gameLabel.frame = CGRectMake(15, CGRectGetMaxY(self.titleLabel.frame), 60, 15);
//        self.gameLabel.backgroundColor = [UIColor clearColor];
        self.gameLabel.textAlignment = NSTextAlignmentCenter;
//        self.gameLabel.alpha = 0.6;
        [self addSubview:self.gameLabel];
        //self.gameLabel.textColor = RGBCOLOR(170, 170, 170);
        self.gameLabel.textColor = GGTitle_Color;
        
        
        self.infoLabel = [SJUILabelFactory labelWithText:@"快来人啊" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
        self.infoLabel.frame = CGRectMake(CGRectGetMaxX(self.gameLabel.frame) + 10, self.gameLabel.frame.origin.y, kScreenW - CGRectGetMaxX(self.gameLabel.frame) -  10  -60, 15);
        [self addSubview:self.infoLabel];
        
        
        self.lockView = [[UIImageView alloc]init];
        self.lockView.image = [UIImage imageNamed:@"icon_suoshangl"];
        [self addSubview:self.lockView];
        
        CGFloat ww = 18;
        CGFloat yy = (self.frame.size.height - 38)/2 + 20;
        CGFloat xx = self.frame.size.width - ww - 13;
        
        self.closeButton = [SJUIButtonFactory buttonWithImageName:@"icon_guanbifangjian"];
        self.closeButton.frame = CGRectMake(xx - 10, yy - 10, ww + 20, ww+20);
        [self addSubview:self.closeButton];
        
    }
    return self;
}
- (void)setDataWithTeamModel:(GGTeamModel *)model
{
    self.titleLabel.text = model.title;
    
    NSString *gameName = [model.game objectForKey: @"gameName"];
    
    self.gameLabel.text = gameName;
//    NSString *titleColor = [model.game objectForKey: @"titleColor"];
//    NSString *backColor = [model.game objectForKey: @"backColor"];
//    self.gameLabel.textColor = GGHEXCOLOR(titleColor);
//    self.gameLabel.backgroundColor = GGHEXCOLOR(backColor);
    
    
    NSString *allNum = [NSString stringWithFormat:@"%@",model.maxnum];
    NSString *currentNum = [NSString stringWithFormat:@"%lu",(unsigned long)model.participants.count];
    NSString *info = [NSString stringWithFormat:@"%@ · %@/%@",[model.type objectForKey:@"name"],currentNum,allNum];
    self.infoLabel.text = info;
    
    CGSize size = [GGAppTool sizeWithText:info font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(999, 15)];
    self.infoLabel.frame = CGRectMake(CGRectGetMaxX(self.gameLabel.frame) + 10, self.gameLabel.frame.origin.y, size.width +10, 15);
    
    if ([model.isLock isEqual:@(YES)])
    {
        self.lockView.frame = CGRectMake(CGRectGetMaxX(self.infoLabel.frame), self.infoLabel.frame.origin.y, 15, 15);
    }
    else
    {
        self.lockView.frame = CGRectZero;
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
