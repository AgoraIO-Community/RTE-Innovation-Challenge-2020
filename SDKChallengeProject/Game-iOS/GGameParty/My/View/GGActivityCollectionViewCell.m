//
//  GGActivityCollectionViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/9.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGActivityCollectionViewCell.h"

@implementation GGActivityCollectionViewCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.imageView = [[UIImageView alloc]initWithFrame:self.bounds];
//        self.imageView.layer.masksToBounds = YES;
//        self.imageView.layer.cornerRadius = 5;;
        [self addSubview:self.imageView];
        self.imageView.backgroundColor = RGBCOLOR(170, 170, 170);
    }
    return self;
}

@end
