//
//  GGMatchGameTypeCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGMatchGameTypeCell.h"

@implementation GGMatchGameTypeCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.backView = [[UIView alloc]initWithFrame:self.bounds];
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.alpha = 0.1;
        self.backView.layer.masksToBounds = YES;
        self.backView.layer.cornerRadius = 15;
        [self addSubview:self.backView];

        
        self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:[UIColor whiteColor] alignment:NSTextAlignmentCenter];
        self.titLabel.frame = self.bounds;
        self.titLabel.font = [UIFont systemFontOfSize:12];
        self.titLabel.textAlignment = NSTextAlignmentCenter;
        [self addSubview:self.titLabel];
    }
    return self;
}


- (void)setSelected:(BOOL)selected
{
    [super setSelected:selected];
    if(selected)
    {
        self.backView.alpha = 1;
        self.titLabel.textColor = GGTitle_Color;
    }
    else
    {
        self.backView.alpha = 0.1;
        self.titLabel.textColor = [UIColor whiteColor];   
    }
    
}


@end
