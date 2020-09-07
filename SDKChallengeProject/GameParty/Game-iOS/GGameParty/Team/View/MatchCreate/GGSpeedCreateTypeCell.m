//
//  GGSpeedCreateTypeCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSpeedCreateTypeCell.h"

@implementation GGSpeedCreateTypeCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.backView = [[UIView alloc]initWithFrame:self.bounds];
        self.backView.backgroundColor = RGBCOLOR( 232, 232, 232);
        
        self.backView.layer.masksToBounds = YES;
        self.backView.layer.cornerRadius = 15;
        self.backView.layer.borderColor = RGBCOLOR( 232, 232, 232).CGColor;
        self.backView.layer.borderWidth = 1;
        [self addSubview:self.backView];
        
        
        self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color alignment:NSTextAlignmentCenter];
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
        self.backView.layer.borderColor = Main_Color.CGColor;
        self.titLabel.textColor = Main_Color;
        
        self.backView.backgroundColor = [UIColor whiteColor];

    }
    else
    {
        self.backView.backgroundColor = RGBCOLOR( 232, 232, 232);
        self.backView.layer.borderColor = RGBCOLOR( 232, 232, 232).CGColor;
        self.titLabel.textColor = GGTitle_Color;
    }
    
}
@end
