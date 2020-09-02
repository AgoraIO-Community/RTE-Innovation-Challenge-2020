//
//  GGTeamInviteCEll.m
//  GGameParty
//
//  Created by Victor on 2018/8/16.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamInviteCell.h"
#import "GGTeamInviteMessage.h"
#import "LCCKVCardView.h"

@interface GGTeamInviteCell ()
@property (nonatomic, strong) UIView *backView;
@property (nonatomic, strong) UIImageView *ggheadImage;

@property (nonatomic, strong) UILabel *titLabel;
@property (nonatomic, strong) UILabel *tipsLabel;

@property (nonatomic,strong)UILabel *gameNameLabel;
@property (nonatomic,strong)UILabel *infoLabel;

@property (nonatomic,strong)AVIMTypedMessage *message;

@end

@implementation GGTeamInviteCell

- (void)setup {
    
    [self inititase];
    
    [super setup];
}

- (void)inititase
{
//    self.messageContentBackgroundImageView = [[UIImageView alloc]init];
//    [self.contentView addSubview:self.messageContentBackgroundImageView];
    
    self.backView = [[UIView alloc]initWithFrame:CGRectMake(15, 0, 227, 112)];
    self.backView.backgroundColor = [UIColor whiteColor];
    [self.contentView addSubview:self.backView];
    self.backView.layer.masksToBounds = YES;
    self.backView.layer.cornerRadius = 8;
    
    CGRect headImageFrame = CGRectMake(10, 10, 55, 55);

    self.ggheadImage = [[UIImageView alloc]initWithFrame:headImageFrame];
    self.ggheadImage.backgroundColor = Main_Color;
    self.ggheadImage.layer.masksToBounds = YES;
    self.ggheadImage.layer.cornerRadius = 55/2;
    [self.backView addSubview:self.ggheadImage];
    
    
    CGRect titleFrame = CGRectMake(CGRectGetMaxX(headImageFrame)+10, headImageFrame.origin.y+5, 150, 25);
    self.titLabel = [[UILabel alloc]initWithFrame:titleFrame];
    self.titLabel.text = @"test";
    [self.backView addSubview:self.titLabel];
    
    
    CGRect gameFrame = CGRectMake(titleFrame.origin.x, CGRectGetMaxY(titleFrame)+6, 50, 14);
    self.gameNameLabel = [SJUILabelFactory labelWithText:@"绝地求生" textColor:Main_Color font:[UIFont systemFontOfSize:10]];
    self.gameNameLabel.backgroundColor = RGBCOLOR(228,246,232);
    self.gameNameLabel.frame = gameFrame;
    self.gameNameLabel.textAlignment = NSTextAlignmentCenter;
    [self.backView addSubview:self.gameNameLabel];
    
    CGRect infoFrame = CGRectMake(CGRectGetMaxX(gameFrame)+5, gameFrame.origin.y, self.backView.frame.size.width - CGRectGetMaxX(gameFrame) - 5, 14);
    self.infoLabel = [SJUILabelFactory labelWithText:@"太平洋任务" textColor:GGInfo_Color font:[UIFont systemFontOfSize:12]];
    self.infoLabel.frame = infoFrame;
    [self.backView addSubview:self.infoLabel];

    UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(headImageFrame)+10, self.backView.frame.size.width, 1)];
    line.backgroundColor = GGBackGround_Color;
    [self.backView addSubview:line];
    
    
    
    CGRect tipFrame = CGRectMake(0, CGRectGetMaxY(headImageFrame)+10, self.backView.frame.size.width, 36);

    self.tipsLabel = [SJUILabelFactory labelWithText:@"点击加入房间组队" textColor:GGInfo_Color font:[UIFont systemFontOfSize:12]];
    self.tipsLabel.frame = tipFrame;
    [self.backView addSubview:self.tipsLabel];
    self.tipsLabel.textAlignment = NSTextAlignmentCenter;
    
    
    UITapGestureRecognizer *tapGestureRecognizer =[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(teamClicked)];
    [self.backView addGestureRecognizer:tapGestureRecognizer];
}

- (void)teamClicked
{
    NSLog(@"点击了组队");
    NSDictionary  * model = [self.message.attributes objectForKey:@"team"];
    [[NSNotificationCenter defaultCenter]postNotificationName:GGTEAM_INVITE_CLICK_NOTIFATION object:model];
}

- (CGSize)sizeThatFits:(CGSize)size
{
    return CGSizeMake(227+15, 122);
}



- (void)configureCellWithData:(AVIMTypedMessage *)message
{
    [super configureCellWithData:message];
    self.message = message;
    NSDictionary  * model = [message.attributes objectForKey:@"team"];
    
    NSURL *avatarURL = [NSURL URLWithString:[model objectForKey:@"avatar"]];
    [self.ggheadImage sd_setImageWithURL:avatarURL placeholderImage:GGDefault_User_Head];
    self.titLabel.text = [model objectForKey:@"title"];
    NSString *titleColor = [model objectForKey: @"titleColor"];
    NSString *backColor = [model objectForKey: @"backColor"];
    self.gameNameLabel.textColor = GGHEXCOLOR(titleColor);
    self.gameNameLabel.backgroundColor = GGHEXCOLOR(backColor);
    self.infoLabel.text = [NSString stringWithFormat:@"%@",[model objectForKey:@"info"]];
    self.gameNameLabel.text = [model objectForKey:@"game"];
}

#pragma mark -
#pragma mark - LCCKChatMessageCellSubclassing Method

+ (void)load {
    [self registerCustomMessageCell];
}

+ (AVIMMessageMediaType)classMediaType {
    return kAVIMMessageMediaTypeVCard;
}


@end
