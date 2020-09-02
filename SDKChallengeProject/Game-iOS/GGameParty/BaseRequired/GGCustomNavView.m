//
//  GGCustomNavView.m
//  GGameParty
//
//  Created by Victor on 2018/8/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGCustomNavView.h"

@implementation GGCustomNavView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.backgroundColor = [UIColor whiteColor];
        UIImageView *back = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.height, self.frame.size.height)];
        back.image = [UIImage imageNamed:@"icon_fanhuiback"];
        back.contentMode = UIViewContentModeCenter;
        [self addSubview:back];
        
        self.titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(CGRectGetMaxX(back.frame), 0, kScreenW - CGRectGetMaxX(back.frame), self.frame.size.height)];
        self.titleLabel.font = [UIFont systemFontOfSize:18];
        [self addSubview:self.titleLabel];
    }
    return self;
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
