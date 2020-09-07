//
//  GGMatchGameCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGMatchGameCell.h"

@implementation GGMatchGameCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        
        self.backView = [[UIView alloc]initWithFrame:self.bounds];
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.alpha = 0.1;
        [self addSubview:self.backView];
        
        self.iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(11.5, 10, 45, 45)];
        self.iconImage.contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.iconImage];
        
        self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:[UIColor whiteColor] alignment:NSTextAlignmentCenter];
        self.titLabel.frame = CGRectMake(0, CGRectGetMaxY(self.iconImage.frame), self.frame.size.width, 25);
        self.titLabel.font = [UIFont systemFontOfSize:14];
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
