//
//  GGSysMessageTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/10.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSysMessageTableViewCell.h"

@implementation GGSysMessageTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        self.contentImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 15, kScreenW - 60, (kScreenW - 60)/3)];
        [self.contentView addSubview:self.contentImage];
        
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 15, 50, 50)];
        self.headImage.layer.masksToBounds = YES;
        self.headImage.layer.cornerRadius = 25;
        [self.contentView addSubview:self.headImage];
        
        self.titLabel = [[UILabel alloc]initWithFrame:CGRectMake(CGRectGetMaxX(self.headImage.frame)+10, 15, kScreenW - 30 - CGRectGetMaxX(self.headImage.frame) - 10 , 25)];
        [self.contentView addSubview:self.titLabel];
        
        self.descLabel = [[UILabel alloc]initWithFrame:CGRectMake(CGRectGetMaxX(self.headImage.frame)+10, CGRectGetMaxY(self.titLabel.frame), kScreenW - 30 - CGRectGetMaxX(self.headImage.frame) - 10 , 30)];
        [self.contentView addSubview:self.descLabel];
        self.descLabel.textColor = RGBCOLOR(170, 170, 170);
        self.descLabel.font = [UIFont systemFontOfSize:12];
        self.descLabel.numberOfLines = 2;
        
    }
    return self;
}

- (void)setModel:(GGSysMesModel *)model
{
    
    if ([model.type isEqualToString:@"1"])
    {//关注消息
        AVFile *file = [model.user objectForKey:@"avatar"];
        [self.headImage setImageWithURL:[NSURL URLWithString:file.url] placeholderImage:GGDefault_User_Head];
        NSString *str = [NSString stringWithFormat:@"%@关注了你",model.user.username];
        NSString *time = [GGAppTool compareCurrentTime:model.createdAt ];
        self.titLabel.text = str;
        self.descLabel.text = time;
    }
    if ([model.type isEqualToString:@"3"]) {
        AVFile *file = model.cover;//公告消息
        [self.contentImage sd_setImageWithURL:[NSURL URLWithString:file.url]];
        self.titLabel.frame = CGRectMake(self.contentImage.frame.origin.x, CGRectGetMaxY(self.contentImage.frame), self.contentImage.frame.size.width, 40);
        self.titLabel.text = model.title;
    }
    if  ([model.type isEqualToString:@"0"] || [model.type isEqualToString:@"2"])
    {
        if ([model.type isEqualToString:@"0"]) {//警告封禁消息
            self.headImage.image = [UIImage imageNamed:@"icon_jg"];
        }
        
        if ([model.type isEqualToString:@"2"]) {//举报消息
            self.headImage.image = [UIImage imageNamed:@"icon_jb"];
        }
        self.titLabel.text = model.title;
        self.descLabel.text = model.content;
    }
}

- (void)setFrame:(CGRect)frame
{
    // 更改x、宽度
    frame.origin.x = 15;
    frame.size.width -= 15 * 2;
    
    // 更改顶部间距、每个cell之间的间距
    frame.origin.y += 15;;
    frame.size.height -= 15;
    
    [super setFrame:frame];
}



@end
