//
//  GGNoticeView.m
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGNoticeView.h"

@implementation GGNoticeView
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.nabaImage = [SJUIImageViewFactory imageViewWithImageName:@"icon_gonggao"];
        self.nabaImage.frame = CGRectMake(15, 10, 12, 16);
        [self addSubview:self.nabaImage];
        
        self.noticeLabel = [SJUILabelFactory labelWithText:@"公告:" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:12]];
        self.noticeLabel.frame = CGRectMake(CGRectGetMaxX(self.nabaImage.frame) + 5, 0, 32, self.frame.size.height);
        [self addSubview:self.noticeLabel];
        
        self.contentLabel = [[ZScrollLabel alloc] initWithFrame:CGRectMake(CGRectGetMaxX(self.noticeLabel.frame)+5, 0, self.frame.size.width - CGRectGetMaxX(self.noticeLabel.frame) - 10, self.frame.size.height)];
        self.contentLabel.textColor = [UIColor whiteColor];
        self.contentLabel.font = [UIFont systemFontOfSize:12];
        self.contentLabel.scrollDuration = 13;
        [self addSubview:self.contentLabel];
        self.contentLabel.text = @"哈哈哈哈滚动公告哈哈哈哈滚动公告哈哈哈哈滚动公告哈哈哈哈滚动公告哈哈哈哈滚动公告";

       
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
