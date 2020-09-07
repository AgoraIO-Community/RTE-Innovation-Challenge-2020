//
//  GGFollowTeamCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/10.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGFollowTeamCell.h"
#import "GGTeamModel.h"
#import "GGFollowTeamModel.h"
#import "UIButton+AFNetworking.h"
@implementation GGFollowTeamCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        
        [self initUI];
        
    }
    return self;
}

- (void)initUI
{
    CGRect headImageFrame = CGRectMake(15, 20, 33, 33);
    self.headImage = [UIButton buttonWithType:UIButtonTypeCustom];
    self.headImage.frame = headImageFrame;
    [self.headImage addTarget:self action:@selector(userInfoClick) forControlEvents:UIControlEventTouchUpInside];
    [self.contentView addSubview:self.headImage];
    
    UIImageView *touxiangkuang = [[UIImageView alloc]initWithFrame:headImageFrame];
    touxiangkuang.image = [UIImage imageNamed:@"icon_touxiangkuang"];
    [self.contentView addSubview:touxiangkuang];
    
    
    
    CAShapeLayer *layer = [[CAShapeLayer alloc] init];
    UIBezierPath *path = [UIBezierPath bezierPathWithArcCenter:CGPointMake(33/2, 33/2) radius:33/2  startAngle:0 endAngle:M_PI * 2 clockwise:YES];
    layer.path = path.CGPath;
    self.headImage.layer.mask = layer;
    
    self.nameLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:12] textColor:RGBCOLOR(50, 87, 148)];
    self.nameLabel.text = @"喜加一";
    self.nameLabel.frame = CGRectMake(CGRectGetMaxX(self.headImage.frame) + 5, headImageFrame.origin.y, 200, 17);
    [self.contentView addSubview:self.nameLabel];
    
    self.timeLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:12] textColor:RGBCOLOR(170, 170, 170)];
    self.timeLabel.text = @"1小时前";
    self.timeLabel.frame = CGRectMake(CGRectGetMaxX(self.headImage.frame) + 5, CGRectGetMaxY(self.nameLabel.frame), 200, 14);
    [self.contentView addSubview:self.timeLabel];
    
    self.teamBackView = [UIView new];
    self.teamBackView.backgroundColor = RGBCOLOR(248, 248, 248);
    self.teamBackView.layer.masksToBounds = YES;
    self.teamBackView.layer.cornerRadius = 5;
    self.teamBackView.frame = CGRectMake(headImageFrame.origin.x, CGRectGetMaxY(self.headImage.frame)+10, kScreenW - 2 * headImageFrame.origin.x, 64);
    [self.contentView addSubview:self.teamBackView];
    
    
    CGRect titleFrame = CGRectMake(10, 10, kScreenW - 40 - 20, 25);
    self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color font:[UIFont systemFontOfSize:16]];
    self.titLabel.frame = titleFrame;
    [self.teamBackView addSubview:self.titLabel];
    
    CGRect gameFrame = CGRectMake(titleFrame.origin.x, CGRectGetMaxY(titleFrame)+6, 50, 14);
    self.gameNameLabel = [SJUILabelFactory labelWithText:@"" textColor:Main_Color font:[UIFont systemFontOfSize:10]];
    self.gameNameLabel.backgroundColor = RGBCOLOR(228,246,232);
    self.gameNameLabel.frame = gameFrame;
    self.gameNameLabel.textAlignment = NSTextAlignmentCenter;
    [self.teamBackView addSubview:self.gameNameLabel];
    
    CGRect infoFrame = CGRectMake(CGRectGetMaxX(gameFrame)+5, gameFrame.origin.y, self.teamBackView.frame.size.width - CGRectGetMaxX(gameFrame) - 5 - 90, 14);
    self.infoLabel = [SJUILabelFactory labelWithText:@"" textColor:GGInfo_Color font:[UIFont systemFontOfSize:12]];
    self.infoLabel.frame = infoFrame;
    [self.teamBackView addSubview:self.infoLabel];
    
    CGRect operatFrame = CGRectMake(self.teamBackView.frame.size.width - 90, 15, 75, 33);
    self.operateButton = [SJUIButtonFactory buttonWithTitle:@"" titleColor:Main_Color];
    self.operateButton.layer.masksToBounds = YES;
    self.operateButton.titleLabel.font = [UIFont systemFontOfSize:14];
    self.operateButton.layer.cornerRadius = 16;
    self.operateButton.layer.borderColor = Main_Color.CGColor;
    self.operateButton.layer.borderWidth = 1;
    self.operateButton.frame = operatFrame;
    [self.teamBackView addSubview:self.operateButton];
    [self.operateButton setTitleColor:[UIColor grayColor] forState:UIControlStateDisabled];
    
    self.shodwView = [[UIView alloc]initWithFrame:self.teamBackView.bounds];
    self.shodwView.backgroundColor = [UIColor whiteColor];
    self.shodwView.alpha = 0.6;
    self.shodwView.hidden = YES;
    [self.teamBackView addSubview:self.shodwView];
    
    [self.operateButton addTarget:self action:@selector(join) forControlEvents:UIControlEventTouchUpInside];

}

- (void)join
{
    if (self.joinTeamBlock) {
        self.joinTeamBlock(self, self.followModel.teamModel);
    }
}

- (void)userInfoClick
{
    if (self.userInfoClickBlock) {
        self.userInfoClickBlock(self, self.followModel.user);
    }
}

- (void)setModel:(GGFollowTeamModel *)followTeamModel
{
    self.followModel = followTeamModel;;
    
    AVUser *user = followTeamModel.user;
    NSString *url = [[user objectForKey:@"avatar"] objectForKey:@"url"];
    
    
    [self.headImage setImageForState:UIControlStateNormal withURL:[NSURL URLWithString:url]placeholderImage:GGDefault_User_Head];
    
    self.nameLabel.text = user.username;
    NSString *time =  [NSString stringWithFormat:@"%@",followTeamModel.createdAt];
    self.timeLabel.text = [GGAppTool compareCurrentTime:followTeamModel.createdAt];
    
    
    
    GGTeamModel *model = followTeamModel.teamModel;
    
    self.titLabel.text = model.title;
    NSString *gameName = [model.game objectForKey: @"gameName"];
    self.gameNameLabel.text = gameName;
    
    NSString *titleColor = [model.game objectForKey: @"titleColor"];
    NSString *backColor = [model.game objectForKey: @"backColor"];
    self.gameNameLabel.textColor = GGHEXCOLOR(titleColor);
    self.gameNameLabel.backgroundColor = GGHEXCOLOR(backColor);
    
    if (gameName.length == 0)
    {
        self.gameNameLabel.hidden = YES;
        self.infoLabel.frame = CGRectMake(self.titLabel.frame.origin.x, CGRectGetMaxY(self.titLabel.frame)+6, self.infoLabel.frame.size.width, self.infoLabel.frame.size.height);
    }
    
    NSString *allNum = [NSString stringWithFormat:@"%@",model.maxnum];
    NSString *currentNum = [NSString stringWithFormat:@"%lu",(unsigned long)model.participants.count];
    self.infoLabel.text = [NSString stringWithFormat:@"%@ · %@/%@",[model.type objectForKey:@"name"],currentNum,allNum];
    
    [self.operateButton setTitle:@"加入" forState:UIControlStateNormal];
    
    if ([model.publisher.objectId isEqualToString:[AVUser currentUser].objectId] || [model.participants containsObject: [AVUser currentUser].objectId])
    {//自己发布的房间或者自己是成员
        self.shodwView.hidden = YES;
        self.operateButton.enabled = YES;
        self.operateButton.layer.borderColor = Main_Color.CGColor;
        self.operateButton.backgroundColor = Main_Color;
        [self.operateButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [self.operateButton setTitle:@"进入" forState:UIControlStateNormal];
    }
    else
    {
        if ([model.isLock  isEqual: @1])
        {//上锁
            self.shodwView.hidden = NO;
            self.operateButton.enabled = NO;
            self.operateButton.layer.borderColor = [UIColor grayColor].CGColor;
            [self.operateButton setTitle:@"已上锁" forState:UIControlStateDisabled];
        }
        else if ([allNum isEqualToString:currentNum])
        {//满员
            self.operateButton.enabled = NO;
            self.shodwView.hidden = NO;
            self.operateButton.layer.borderColor = [UIColor grayColor].CGColor;
            [self.operateButton setTitle:@"满员" forState:UIControlStateDisabled];
            
        }
        else
        {
            self.shodwView.hidden = YES;
            self.operateButton.enabled = YES;
            self.operateButton.layer.borderColor = Main_Color.CGColor;
            [self.operateButton setTitle:@"加入" forState:UIControlStateNormal];
        }
    }
    
    
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
