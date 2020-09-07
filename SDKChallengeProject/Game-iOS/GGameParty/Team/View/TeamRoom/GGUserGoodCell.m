//
//  GGUserGoodCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserGoodCell.h"



@interface GGUserGoodCell()


@end


@implementation GGUserGoodCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        
        UIImageView *image = [[UIImageView alloc]initWithFrame:self.bounds];
        [self addSubview:image];
        self.imageView = image;
        
        
        self.numLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 10, self.frame.size.width, self.frame.size.height/3)];
        [self.imageView addSubview:self.numLabel];
        self.numLabel.textAlignment = NSTextAlignmentCenter;
        self.numLabel.font = [UIFont systemFontOfSize:15];
        self.numLabel.textColor = [UIColor whiteColor];
        
        self.titLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.numLabel.frame), self.frame.size.width, self.frame.size.height/3)];
        [self.imageView addSubview:self.titLabel];
        self.titLabel.textAlignment = NSTextAlignmentCenter;
        self.titLabel.font = [UIFont systemFontOfSize:11];
        self.titLabel.textColor = [UIColor whiteColor];
        
        
        self.goodImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.titLabel.frame)- 15, self.frame.size.width, self.frame.size.height/3)];
        [self.imageView addSubview:self.goodImage];
        self.goodImage.contentMode = UIViewContentModeCenter;
        self.goodImage.image = [UIImage imageNamed:@"icon_zan"];
        self.titLabel.hidden = YES;
        
        self.goodImage.hidden = YES;

    }
    return self;
}


@end
