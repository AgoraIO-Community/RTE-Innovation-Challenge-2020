//
//  GGSpeedCreateCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSpeedCreateCell.h"

@implementation GGSpeedCreateCell

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        
        self.backView = [[UIView alloc]initWithFrame:self.bounds];
        self.backView.backgroundColor = RGBCOLOR(232, 232, 232);
        [self addSubview:self.backView];
        
        self.iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(8, 8, 33, 33)];
        self.iconImage.contentMode = UIViewContentModeScaleAspectFit;
        [self addSubview:self.iconImage];
        
        self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color alignment:NSTextAlignmentCenter];
        self.titLabel.frame = CGRectMake(CGRectGetMaxX(self.iconImage.frame), 0, self.frame.size.width - CGRectGetMaxX(self.iconImage.frame), self.frame.size.height);
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
//        self.backView.alpha = 1;
        self.backView.backgroundColor = Main_Color;
        self.titLabel.textColor = [UIColor whiteColor];
    }
    else
    {
//        self.backView.alpha = 0.1;
        self.backView.backgroundColor = RGBCOLOR(232, 232, 232);
        self.titLabel.textColor = GGTitle_Color;
        
    }
    
}
@end
