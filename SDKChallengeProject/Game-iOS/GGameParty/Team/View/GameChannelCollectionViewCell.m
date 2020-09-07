//
//  GameChannelCollectionViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GameChannelCollectionViewCell.h"

@implementation GameChannelCollectionViewCell
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        //设置CollectionViewCell中的图像框
        self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetHeight(self.frame))];
        self.imageView.userInteractionEnabled = YES;
        [self addSubview:self.imageView];
        //55 + 5 + 23 + 5 + 16.5 + 5 + 30 = 140
        CGFloat yy = (self.frame.size.height - 140)/2;
        self.headImage = [SJUIImageViewFactory imageViewWithImageName:@"icon_linshi"];
        CGFloat bigWidth = (kScreenW - 30)/2;
        CGFloat headWidth = 55;
        CGFloat headX = (bigWidth - 55)/2;
        self.headImage.frame = CGRectMake(headX, yy, headWidth, headWidth);
        [self addSubview:self.headImage];
        
        self.titLabel = [SJUILabelFactory labelWithText:@"绝地球赛" textColor:[UIColor whiteColor] font:[UIFont boldSystemFontOfSize:16]];
        self.titLabel.frame = CGRectMake(0, CGRectGetMaxY(self.headImage.frame) + 5, bigWidth, 23);
        [self addSubview:self.titLabel];
        self.titLabel.textAlignment = NSTextAlignmentCenter;
        
        self.numLabel = [SJUILabelFactory labelWithText:@"0人在聊" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:12]];
        self.numLabel.frame = CGRectMake(0, CGRectGetMaxY(self.titLabel.frame) + 5, bigWidth, 16.5);
        [self addSubview:self.numLabel];
        self.numLabel.textAlignment = NSTextAlignmentCenter;
        
        self.joinButton = [SJUIButtonFactory buttonWithTitle:@"进入该群聊" titleColor:[UIColor blackColor]];
        self.joinButton.frame = CGRectMake((bigWidth - 85)/2, CGRectGetMaxY(self.numLabel.frame) + 5, 85, 30);

        self.joinButton.titleLabel.font = [UIFont systemFontOfSize:12];
        [self.joinButton setBackgroundImage:[UIImage imageNamed:@"icon_jinru"] forState:UIControlStateNormal];
//        self.joinButton.layer.masksToBounds = YES;
//        self.joinButton.layer.cornerRadius = 15;
//        self.joinButton.layer.borderWidth = 1;
//        self.joinButton.layer.borderColor = [UIColor whiteColor].CGColor;
        [self addSubview:self.joinButton];

    }
    return self;
}

- (void)setGameModel:(GGGameModel *)model
{
    [self.imageView setImageWithURL:[NSURL URLWithString:model.gameBg.url] placeholderImage:[UIImage imageNamed:@"room_Backbg"]];
    self.titLabel.text = model.gameName;
    [self.headImage sd_setImageWithURL:[NSURL URLWithString:model.gameicon.url]];
    
    AVIMClient *client = [LCChatKit sharedInstance].client;
    NSString *conversationId=model.chatId;
        AVIMConversationQuery *query = [client conversationQuery];
        // 根据已知 Id 获取对话实例，当前实例为聊天室。
        [query getConversationById:conversationId callback:^(AVIMConversation *conversation, NSError *error) {
            // 查询在线人数
            [conversation countMembersWithCallback:^(NSInteger number, NSError *error) {
              //  NSLog(@"%ld",number);
                self.numLabel.text = [NSString stringWithFormat:@"%ld人在聊",(long)number];
            }];
    }];
    
}

@end
