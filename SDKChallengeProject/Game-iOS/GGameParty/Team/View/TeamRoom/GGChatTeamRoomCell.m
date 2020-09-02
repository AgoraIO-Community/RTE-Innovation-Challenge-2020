//
//  GGChatTeamRoomCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChatTeamRoomCell.h"


@interface GGChatTeamRoomCell ()



@end

@implementation GGChatTeamRoomCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        self.backView = [[UIView alloc]init];
        [self.contentView addSubview:self.backView];
        
        
        self.contentLabel = [[UILabel alloc]init];
        [self addSubview:self.contentLabel];
        
        self.nameBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [self addSubview:self.nameBtn];
        
        self.operatBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [self addSubview:self.operatBtn];
    }
    return self;
}

- (NSString *)getName:(NSString *)text
{
    NSArray *array = [text componentsSeparatedByString:@":"]; //使用:符号分割
    if (array.count > 1)
    {
        NSString *name = [NSString stringWithFormat:@"%@:",array[0]];
        return name;
    }
    return @"";
}

- (void)setModel:(GGChatModel *)model
{
//    self.model = model;
    CGFloat wwwww = self.contentView.frame.size.width - 85 - 10;
    self.backView.frame = CGRectMake(10, 0, wwwww, self.contentView.frame.size.height);
    
    
    UIFont *namefont = [UIFont systemFontOfSize:12];
    self.contentLabel.font = namefont;
    
    if (model.type == 1)
    {//普通聊天文本类消息  用户名+消息内容 ,以冒号隔开.
        self.nameBtn.titleLabel.font = namefont;
        [self.nameBtn setTitleColor:RGBCOLOR(170, 170, 179) forState:UIControlStateNormal];
        
        self.contentLabel.textColor = [UIColor whiteColor];
        self.contentLabel.font = namefont;
        self.contentLabel.numberOfLines = 0;
        
        NSString *text = model.text;
        NSString *name = [self getName:text];
        
        [self.nameBtn setTitle:name forState:UIControlStateNormal];
        self.contentLabel.text = text;
        
        CGSize nameSize = [GGAppTool sizeWithText:name font:namefont maxSize:CGSizeMake(999, 20) ];
        self.nameBtn.frame = CGRectMake(10, 5, nameSize.width, nameSize.height);
        
        CGSize contentSize = [GGAppTool sizeWithText:text font:namefont maxSize:CGSizeMake(wwwww, 9999)];
        self.contentLabel.frame = CGRectMake(self.nameBtn.frame.origin.x, 5, contentSize.width, contentSize.height);
    }
    if (model.type == 2)
    {//房间公告类 -- 房间公告五背景,纯文字
        self.contentLabel.text = model.text;
        CGSize size = [GGAppTool sizeWithText:model.text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(wwwww, 9999)];
        self.contentLabel.textColor = RGBCOLOR(170, 170, 170);
        self.contentLabel.numberOfLines = 0;
        self.contentLabel.frame = CGRectMake(10, 5, size.width, size.height);
    }
    if (model.type == 3)
    {//房间创建成功类 -带按钮 邀请
        
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.alpha = 0.1;
        self.backView.layer.masksToBounds = YES;
        self.backView.layer.cornerRadius = 14;
        
        self.contentLabel.text = model.text;
        self.contentLabel.textColor = RGBCOLOR(170, 170, 170);
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        
        
        [self.operatBtn setTitle:@"邀请" forState:UIControlStateNormal];
        [self.operatBtn addTarget:self action:@selector(inviateFriend) forControlEvents:UIControlEventTouchUpInside];
        [self.operatBtn setTitleColor:Main_Color forState:UIControlStateNormal];
        self.operatBtn.titleLabel.font = namefont;
        
        CGSize size = [GGAppTool sizeWithText:model.text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(wwwww, 9999)];
        
        self.contentLabel.frame = CGRectMake(20, 5, size.width+5, 27);
        self.operatBtn.frame = CGRectMake(CGRectGetMaxX(self.contentLabel.frame), 5, 40, 27);
        self.backView.frame =  CGRectMake(10, 5, size.width +  60, 27);
        
    }
    if (model.type == 4)
    {//xxx加入/离开  带标示
        
        NSString *name = [self getName:model.text];
        NSString *text = [model.text substringFromIndex:name.length];

        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.alpha = 0.1;
        self.backView.layer.masksToBounds = YES;
        self.backView.layer.cornerRadius = 14;
        
        self.contentLabel.text = text;
        self.contentLabel.textColor = RGBCOLOR(170, 170, 170);
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        
        [self.nameBtn setTitle:name forState:UIControlStateNormal];
        [self.nameBtn addTarget:self action:@selector(userBtnClick) forControlEvents:UIControlEventTouchUpInside];
        [self.nameBtn setTitleColor:Main_Color forState:UIControlStateNormal];
        self.nameBtn.titleLabel.font = namefont;
        
        
        CGSize nameSize = [GGAppTool sizeWithText:name font:namefont maxSize:CGSizeMake(999, 20) ];
        self.nameBtn.frame = CGRectMake(20, 5, nameSize.width, 27);
        
        CGSize contentSize = [GGAppTool sizeWithText:text font:namefont maxSize:CGSizeMake(wwwww, 9999)];
        self.contentLabel.frame = CGRectMake(CGRectGetMaxX(self.nameBtn.frame), 5, contentSize.width, 27);
        
        
        self.backView.frame =  CGRectMake(10, 5, nameSize.width + contentSize.width + 20, 27);
    }
    if (model.type == 5)
    {//普通提示类 -- 纯文本,无高亮标示
        self.backView.backgroundColor = [UIColor whiteColor];
        self.backView.alpha = 0.1;
        self.backView.layer.masksToBounds = YES;
        self.backView.layer.cornerRadius = 14;
        
        self.contentLabel.text = model.text;
        self.contentLabel.textColor = RGBCOLOR(170, 170, 170);
        self.contentLabel.textAlignment = NSTextAlignmentCenter;
        
        CGSize size = [GGAppTool sizeWithText:model.text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(wwwww, 9999)];
        self.backView.frame = CGRectMake(10, 5, size.width + 20, 27);
        self.contentLabel.frame = CGRectMake(20, 5, size.width, 27);
    }
    
}

- (void)userBtnClick
{
    if (self.userClick)
    {
        self.userClick(self, self.model.userObjectId);
    }
}

- (void)inviateFriend
{
    if (self.invitaFriendClick)
    {
        self.invitaFriendClick(self);
    }
}

+ (CGFloat)valiateHeightWithModel:(GGChatModel *)model
{
    CGFloat wwwww = kScreenW - 85 - 10;
    UIFont *namefont = [UIFont systemFontOfSize:12];

    if (model.type == 1)
    {//普通消息
        NSString *text = model.text;

        CGSize contentSize = [GGAppTool sizeWithText:text font:namefont maxSize:CGSizeMake(wwwww, 9999)];
        return contentSize.height + 10;
       
    }
    if (model.type == 2)
    {//房间公告类 -- 房间公告五背景,纯文字
         CGSize size = [GGAppTool sizeWithText:model.text font:namefont maxSize:CGSizeMake(wwwww, 9999)];
        return size.height + 10;
    }
    if (model.type == 3)
    {//房间创建成功类 -带按钮 邀请
        return 27+10;
    }
    if (model.type == 4)
    {//xxx加入/离开  带标示
        return 27+10;
    }
    if (model.type == 5)
    {//普通提示类 -- 纯文本,无高亮标示
        return 27+10;
    }
    return 27+10;;
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
