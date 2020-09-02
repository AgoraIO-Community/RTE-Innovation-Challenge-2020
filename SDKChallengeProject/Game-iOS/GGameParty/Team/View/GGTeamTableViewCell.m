//
//  GGTeamTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/7/29.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamTableViewCell.h"
#import "GGTeamModel.h"
@implementation GGTeamTableViewCell

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
    
    CGRect headImageFrame = CGRectMake(15, 20, 55, 55);
    self.headImage = [[UIImageView alloc]initWithFrame:headImageFrame];
    [self.contentView addSubview:self.headImage];
    
    UIImageView *touxiangkuang = [[UIImageView alloc]initWithFrame:headImageFrame];
    touxiangkuang.image = [UIImage imageNamed:@"icon_touxiangkuang"];
    [self.contentView addSubview:touxiangkuang];
    
    CGRect titleFrame = CGRectMake(CGRectGetMaxX(headImageFrame)+10, 25, kScreenW - 90 - CGRectGetMaxX(headImageFrame) - 20, 25);
    self.titLabel = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color font:[UIFont systemFontOfSize:16]];
    self.titLabel.frame = titleFrame;
    [self.contentView addSubview:self.titLabel];
    
    CGRect gameFrame = CGRectMake(titleFrame.origin.x, CGRectGetMaxY(titleFrame)+6, 50, 14);
    self.gameNameLabel = [SJUILabelFactory labelWithText:@"" textColor:Main_Color font:[UIFont systemFontOfSize:10]];
    self.gameNameLabel.backgroundColor = RGBCOLOR(228,246,232);
    self.gameNameLabel.frame = gameFrame;
    self.gameNameLabel.textAlignment = NSTextAlignmentCenter;
    [self.contentView addSubview:self.gameNameLabel];
    
    CGRect infoFrame = CGRectMake(CGRectGetMaxX(gameFrame)+5, gameFrame.origin.y, kScreenW - CGRectGetMaxX(gameFrame) - 5 - 90, 14);
    self.infoLabel = [SJUILabelFactory labelWithText:@"" textColor:GGInfo_Color font:[UIFont systemFontOfSize:12]];
    self.infoLabel.frame = infoFrame;
    [self.contentView addSubview:self.infoLabel];
    
    CGRect operatFrame = CGRectMake(kScreenW - 90, 31, 75, 33);
    self.operateButton = [SJUIButtonFactory buttonWithTitle:@"加入" titleColor:Main_Color];
    self.operateButton.titleLabel.font = [UIFont systemFontOfSize:14];
    self.operateButton.frame = operatFrame;
    [self.operateButton setBackgroundImage:[UIImage imageNamed:@"jiarukuang"] forState:UIControlStateNormal];
    [self.contentView addSubview:self.operateButton];
    
    self.shodwView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 95)];
    self.shodwView.backgroundColor = [UIColor whiteColor];
    self.shodwView.alpha = 0.6;
    self.shodwView.hidden = YES;
    [self.contentView addSubview:self.shodwView];
}

- (void)setModel:(GGTeamModel *)model
{
    NSString *url = [[model.publisher objectForKey:@"avatar"] objectForKey:@"url"];
    [self.headImage setImageWithURL:[NSURL URLWithString:url] placeholderImage:GGDefault_User_Head];

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
          [self.operateButton setBackgroundImage:[UIImage imageNamed:@"jinrukuang"] forState:UIControlStateNormal];
        [self.operateButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [self.operateButton setTitle:@"进入" forState:UIControlStateNormal];
    }
    else
    {
        self.operateButton.backgroundColor = [UIColor whiteColor];
        if ([model.isLock  isEqual: @1])
        {//上锁
            self.shodwView.hidden = NO;
            self.operateButton.enabled = NO;
              [self.operateButton setBackgroundImage:[UIImage imageNamed:@"icon_manyuankuang"] forState:UIControlStateNormal];
            [self.operateButton setTitleColor:[UIColor grayColor]  forState:UIControlStateNormal];
          
            [self.operateButton setTitle:@"已上锁" forState:UIControlStateDisabled];
        }
        else if ([allNum isEqualToString:currentNum])
        {//满员
           
            self.shodwView.hidden = NO;
            self.operateButton.enabled = NO;
           [self.operateButton setTitleColor:[UIColor grayColor]  forState:UIControlStateNormal];
          [self.operateButton setBackgroundImage:[UIImage imageNamed:@"icon_manyuankuang"] forState:UIControlStateNormal];
            [self.operateButton setTitle:@"满员" forState:UIControlStateDisabled];
            
        }
        else
        {
            self.shodwView.hidden = YES;
            self.operateButton.enabled = YES;
          [self.operateButton setBackgroundImage:[UIImage imageNamed:@"jiarukuang"] forState:UIControlStateNormal];
            [self.operateButton setTitleColor:Main_Color  forState:UIControlStateNormal];
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
