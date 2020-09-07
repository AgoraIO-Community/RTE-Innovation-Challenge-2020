//
//  GGAudioBreakTips.m
//  GGameParty
//
//  Created by Victor on 2018/9/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAudioBreakTips.h"

@implementation GGAudioBreakTips

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
 
 UIView *headerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 40)];
 self.tableView.tableHeaderView = headerView;
 headerView.backgroundColor = RGBCOLOR(239, 255, 242);
 UILabel *label = [[UILabel alloc]initWithFrame:headerView.bounds];
 label.textColor = Main_Color;
 label.font = [UIFont systemFontOfSize:12];
 label.text = @"    你需要完成此游戏答题才可以进行组队";
 [headerView addSubview:label];
 
 UIImageView *go = [[UIImageView alloc]initWithFrame:CGRectMake(kScreenW - 70, 0, 60, 40)];
 go.contentMode = UIViewContentModeRight;
 go.image = [UIImage imageNamed:@"icon_datia"];
 [headerView addSubview:go];
}
*/


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        UIView *headerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 40)];
        headerView.backgroundColor = [UIColor redColor];
        UILabel *label = [[UILabel alloc]initWithFrame:headerView.bounds];
        label.textColor = [UIColor whiteColor];
        label.font = [UIFont systemFontOfSize:12];
        label.text = @"    注意:您的语音已断开";
        [headerView addSubview:label];
        
        UIButton *go = [SJUIButtonFactory buttonWithTitle:@"重新连接" titleColor:[UIColor whiteColor]];
        go.titleLabel.font = [UIFont systemFontOfSize:12];
        go.layer.masksToBounds = YES;
        go.layer.cornerRadius = 15;
        go.layer.borderColor = [UIColor whiteColor].CGColor;
        go.layer.borderWidth = 0.5;
        go.frame = CGRectMake(kScreenW - 70, 5, 60, 30);
        [headerView addSubview:go];
        [go addTarget:self action:@selector(reConnect:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:headerView];
    }
    return self;
}

- (void)reConnect:(UIButton *)btn
{
    btn.enabled = NO;
    if (self.reConnectAudioBlock) {
        self.reConnectAudioBlock(btn);
    }
}




@end
