//
//  GGQGameTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGQGameTableViewCell.h"

@implementation GGQGameTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        UIView *back = [[UIView alloc]initWithFrame:CGRectMake(10, 0, kScreenW - 20, 74)];
        back.layer.borderColor = GGLine_Color.CGColor;
        back.layer.borderWidth = 1;
        [self.contentView addSubview:back];
        back.layer.masksToBounds = YES;
        back.layer.cornerRadius = 5;
        
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 18, 38, 38)];
        self.headImage.image = [UIImage imageNamed:@"icon_linshi"];
//        self.headImage.layer.masksToBounds = YES;
//        self.headImage.layer.cornerRadius = 38/2;
        [back addSubview:self.headImage];
        
        self.nameLabel = [SJUILabelFactory labelWithText:@"斯内克" textColor:GGTitle_Color font:[UIFont systemFontOfSize:16]];
        self.nameLabel.frame = CGRectMake(CGRectGetMaxX(self.headImage.frame) + 10, self.headImage.frame.origin.y, kScreenW - CGRectGetMaxX(self.headImage.frame) - 40, 22);
        [back addSubview:self.nameLabel];
        
        
        self.infoLabel = [SJUILabelFactory labelWithText:@"最多答错两道可通过测试" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:12]];
        self.infoLabel.frame = CGRectMake(CGRectGetMaxX(self.headImage.frame) + 10, CGRectGetMaxY(self.nameLabel.frame), kScreenW - CGRectGetMaxX(self.headImage.frame) - 50, 15);
        [back addSubview:self.infoLabel];
        
        self.goBtn  = [SJUIButtonFactory buttonWithTitle:@"前往答题" titleColor:[UIColor whiteColor]];
        self.goBtn.frame = CGRectMake(back.frame.size.width - 70, 24, 55, 25);
        self.goBtn.titleLabel.font = [UIFont systemFontOfSize:12];
        self.goBtn.backgroundColor = Main_Color;
        self.goBtn.layer.masksToBounds = YES;
        self.goBtn.layer.cornerRadius = 10;
        [back addSubview:self.goBtn];
        
    }
    return self;
}

- (void)setModel:(GGGameModel *)model
{
    [self.headImage setImageWithURL:[NSURL URLWithString:model.gameicon.url] placeholderImage:GGDefault_User_Head];
    self.nameLabel.text = model.gameName;
    if (!model.userNeedQ)
    {//无需答题
        [self.goBtn setTitle:@"答题通过" forState:UIControlStateNormal];
        self.goBtn.titleLabel.font = [UIFont systemFontOfSize:12];
        [self.goBtn setTitleColor:RGBCOLOR(170, 170, 170) forState:UIControlStateNormal];
        self.goBtn.backgroundColor = [UIColor whiteColor];
    }
    else
    {
        [self.goBtn setTitle:@"答题" forState:UIControlStateNormal];
        self.goBtn.titleLabel.font = [UIFont systemFontOfSize:12];
        [self.goBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.goBtn.backgroundColor = Main_Color;
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
