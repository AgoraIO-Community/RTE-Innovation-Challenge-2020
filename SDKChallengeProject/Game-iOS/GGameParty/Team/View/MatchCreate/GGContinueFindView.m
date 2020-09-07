//
//  GGContinueFindView.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGContinueFindView.h"
@interface GGContinueFindView ()

@property(nonatomic,strong)UIButton *createRoomBtn;

@property(nonatomic,strong)UIButton *stopFind;

@property(nonatomic,strong)UIButton *continueFind;

@property(nonatomic,strong)UILabel *timeLabel;

@end
@implementation GGContinueFindView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        UIView *back = [[UIView alloc]initWithFrame:self.bounds];
        back.backgroundColor = [UIColor whiteColor];
        back.layer.cornerRadius = 10;
        back.layer.masksToBounds = YES;
        [self addSubview:back];
    

        UILabel *tit = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, self.frame.size.width, 25)];
        tit.text = @"当前时段符合您要求的组队较少";
        tit.font = [UIFont boldSystemFontOfSize:16];
        tit.textAlignment = NSTextAlignmentCenter;
        [self addSubview:tit];
    
        UILabel *desc = [[UILabel alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(tit.frame) + 5, self.frame.size.width, 18)];
        desc.text = @"不用灰心，您可以创建房间来邀请队友";
        desc.font = [UIFont systemFontOfSize:12];
        desc.textAlignment = NSTextAlignmentCenter;
        desc.textColor = RGBCOLOR(170, 170, 170);
        [self addSubview:desc];
        
        self.createRoomBtn = [SJUIButtonFactory buttonWithTitle:@"创建房间" titleColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:14] target:self sel:@selector(createRoom)];
        self.createRoomBtn.backgroundColor = Main_Color;
        self.createRoomBtn.layer.masksToBounds = YES;
        self.createRoomBtn.layer.cornerRadius = 22.5;
        [self addSubview:self.createRoomBtn];
        self.createRoomBtn.frame = CGRectMake(35, CGRectGetMaxY(desc.frame) + 17, self.frame.size.width - 70, 40);
        
        self.stopFind = [SJUIButtonFactory buttonWithTitle:@"停止寻找" titleColor:[UIColor blackColor] font:[UIFont systemFontOfSize:14] target:self sel:@selector(stopClick)];
        [self addSubview:self.stopFind];
        self.stopFind.frame = CGRectMake(0, self.frame.size.height - 40, self.frame.size.width /2, 40);
        
        self.continueFind = [SJUIButtonFactory buttonWithTitle:@"继续寻找" titleColor:[UIColor blackColor] font:[UIFont systemFontOfSize:14] target:self sel:@selector(continueClick)];
        [self addSubview:self.continueFind];
        self.continueFind.frame = CGRectMake(CGRectGetMaxX(self.stopFind.frame), self.frame.size.height - 40, self.frame.size.width /2, 40);
    }
    return self;
}

- (void)stopClick
{
    if (self.stopFindClick) {
        self.stopFindClick(self);
    }
}


- (void)continueClick
{
    if (self.continueFindClick) {
        self.continueFindClick(self);
    }
}

- (void)createRoom
{
    if (self.createRoomClick) {
        self.createRoomClick(self);
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
