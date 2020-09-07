//
//  GGHistoryTeamerCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGHistoryTeamerCell.h"

@implementation GGHistoryTeamerCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 20, 44, 44)];
        self.headImage.image = [UIImage imageNamed:@"icon_linshi"];
        [self.contentView addSubview:self.headImage];
        
        UIImageView *touxiangkuang = [[UIImageView alloc]initWithFrame:CGRectMake(15, 20, 44, 44)];
        touxiangkuang.image = [UIImage imageNamed:@"icon_touxiangkuang"];
        [self.contentView addSubview:touxiangkuang];
        
        
        self.nameLabel = [SJUILabelFactory labelWithText:@"斯内克" textColor:GGTitle_Color font:[UIFont systemFontOfSize:16]];
        self.nameLabel.frame = CGRectMake(CGRectGetMaxX(self.headImage.frame) + 10, self.headImage.frame.origin.y, kScreenW - CGRectGetMaxX(self.headImage.frame) - 40, 22);
        [self.contentView addSubview:self.nameLabel];
        
        self.sexImage = [[UIImageView alloc]initWithFrame:CGRectMake(self.nameLabel.frame.origin.x, CGRectGetMaxY(self.nameLabel.frame) + 3, 15, 15)];
        self.sexImage.image = [UIImage imageNamed:@"icon_nan"];
        self.sexImage.contentMode = UIViewContentModeCenter;
        [self.contentView addSubview:self.sexImage];
        
        self.infoLabel = [SJUILabelFactory labelWithText:@"22岁:六安市" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:12]];
        self.infoLabel.frame = CGRectMake(CGRectGetMaxX(self.sexImage.frame) + 5, self.sexImage.frame.origin.y, kScreenW - CGRectGetMaxX(self.headImage.frame) - 50, 15);
        [self.contentView addSubview:self.infoLabel];
        
        
    }
    return self;
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
