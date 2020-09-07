//
//  GGShareCollectionViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGShareCollectionViewCell.h"

@implementation GGShareCollectionViewCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 60, 55 - 17)];
        self.iconImage.contentMode = UIViewContentModeCenter;
        [self addSubview:self.iconImage];
        
        self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color alignment:NSTextAlignmentCenter];
        self.titLabel.frame = CGRectMake(0, CGRectGetMaxY(self.iconImage.frame), 60, 17);
        self.titLabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.titLabel];
    }
    return self;
}
@end
