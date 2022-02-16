//
//  GGAlertView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAlertView.h"

@implementation GGAlertView

- (instancetype)initWithFrame:(CGRect)frame title:(NSString *)title desc:(NSString *)desc cancelTitle:(NSString *)cancelTitle sureBtn:(NSString *)sureTitle
{
    if (self = [super initWithFrame:frame])
    {
        [self addSubview:self.waitbackView];
        [self.waitbackView addSubview:self.activityIndicator];
        
        [self addSubview:self.backView];
        [self.backView addSubview:self.btnClose];
        [self.backView addSubview:self.lblStatus];
        self.lblStatus.text = title;
        
        UILabel *desc = [SJUILabelFactory labelWithText:desc textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:14]];
        desc.frame = CGRectMake(16, 16+26+10, self.frame.size.width - 16, 20);
        [self.backView addSubview:desc];
        
        if (cancelTitle) {
            UIButton *btn = [SJUIButtonFactory buttonWithTitle:cancelTitle titleColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:14] target:self sel:@selector(cancel)];
            
        }
        
        
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
