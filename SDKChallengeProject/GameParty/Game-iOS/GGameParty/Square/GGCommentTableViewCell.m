//
//  GGCommentTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGCommentTableViewCell.h"
#import "UIButton+AFNetworking.h"
@implementation GGCommentTableViewCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        self.headImagBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        self.headImagBtn.frame = CGRectMake(15, 10, 44, 44);
//        self.headImagBtn.layer.masksToBounds = YES;
//        self.headImagBtn.layer.cornerRadius = 44/2;
        [self.contentView addSubview:self.headImagBtn];
        [self.headImagBtn addTarget:self action:@selector(headClick) forControlEvents:UIControlEventTouchUpInside];
        
        
        CAShapeLayer *layer = [[CAShapeLayer alloc] init];
        UIBezierPath *path = [UIBezierPath bezierPathWithArcCenter:CGPointMake(22, 22) radius:22  startAngle:0 endAngle:M_PI * 2 clockwise:YES];
        layer.path = path.CGPath;
        self.headImagBtn.layer.mask = layer;
        
        
        
        self.nameLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:15] textColor:[UIColor whiteColor]];
        self.nameLabel.frame = CGRectMake(CGRectGetMaxX(self.headImagBtn.frame)+10, 10, 200, 20);
        [self.contentView addSubview:self.nameLabel];
        
        self.descLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:13] textColor:[UIColor whiteColor]];
        CGFloat width = kScreenW - CGRectGetMaxX(self.headImagBtn.frame) - 10 - 62;
        self.descLabel.frame = CGRectMake(CGRectGetMaxX(self.headImagBtn.frame)+10, CGRectGetMaxY(self.nameLabel.frame), width, 20);
        [self.contentView addSubview:self.descLabel];
        self.descLabel.numberOfLines = 0;
        
        self.goodButton = [SJUIButtonFactory buttonWithImageName:@"icon_guzhang2"];
        self.goodButton.frame = CGRectMake(kScreenW - 60, 10, 55, 25);
        [self.contentView addSubview:self.goodButton];
        [self.goodButton addTarget:self action:@selector(goGood) forControlEvents:UIControlEventTouchUpInside];
        [self.goodButton setImage:[UIImage imageNamed:@"icon_guzhang"] forState:UIControlStateSelected];
        [self.goodButton setTitleColor:Main_Color forState:UIControlStateSelected];
        [self.goodButton setTitleColor:RGBCOLOR(204, 204, 204) forState:UIControlStateNormal];
    }
    return self;
}

- (void)headClick
{
    if (self.goUserInfoVCBlock) {
        self.goUserInfoVCBlock(self.commentModel.publisher.objectId);
    }
}

- (void)goGood
{
    self.goodButton.selected = !self.goodButton.selected;
    AVObject *obj = [AVObject objectWithClassName:DB_SQUARE_COMMENT objectId:self.commentModel.objectId];
    CGFloat goodNum;
    if (self.goodButton.selected)
    {
        [obj incrementKey:@"goodNum"];
        goodNum = [self.commentModel.goodNum floatValue] + 1;
    }else
    {
        goodNum = [self.commentModel.goodNum floatValue] - 1;
        [obj incrementKey:@"goodNum" byAmount:[NSNumber numberWithInt:-1]];
    }
    //超过1000显示为1.1k
    NSString *num;
    if (goodNum<1000) {
        num = [NSString stringWithFormat:@"%ld",(long)goodNum];
    }
    else
    {
        CGFloat xxx = goodNum /1000.0f;
        num = [NSString stringWithFormat:@"%.1fk",xxx];
    }
    [self.goodButton setTitle:num forState:UIControlStateNormal];
//    self.commentModel.goodNum = [NSNumber numberWithFloat:goodNum];

    obj.fetchWhenSave = true;
    [obj saveInBackground];
    if (self.giveGoodBlock) {
        self.giveGoodBlock( self.goodButton.selected, self.index);
    }
    
}

- (void)setModel:(SquareCommentModel *)model index:(NSInteger)index
{
    self.commentModel = model;
    self.index = index;
    if ([model.goodNum integerValue]> 1) {
        NSString *num;
        if ([model.goodNum integerValue] < 1000) {
            num = [NSString stringWithFormat:@"%@",model.goodNum];
        }else
        {
            CGFloat iii = [model.goodNum floatValue];
            CGFloat xxx = iii/1000.0f;
            num = [NSString stringWithFormat:@"%.1fk",xxx];
        }
        [self.goodButton setTitle:num forState:UIControlStateNormal];
    }
    
    self.goodButton.selected = model.userIsGood;
    
    
    AVUser *user = model.publisher;
    AVFile *avatar = [user objectForKey:@"avatar"];
    [self.headImagBtn setImageForState:UIControlStateNormal withURL:[NSURL URLWithString:avatar.url]];
    self.nameLabel.text = user.username;
    self.descLabel.text = model.comment;
    CGFloat width = kScreenW - CGRectGetMaxX(self.headImagBtn.frame) - 10 - 62;
    CGSize size = [GGAppTool sizeWithText:model.comment font:[UIFont systemFontOfSize:13] maxSize:CGSizeMake(width, 999)];
    self.descLabel.frame = CGRectMake(CGRectGetMaxX(self.headImagBtn.frame)+10, CGRectGetMaxY(self.nameLabel.frame), width, size.height+10);
    
}

+ (CGFloat)calcuHeithWith:(SquareCommentModel *)model
{
    CGFloat width = kScreenW - 54 - 10 - 62;

    CGSize size = [GGAppTool sizeWithText:model.comment font:[UIFont systemFontOfSize:13] maxSize:CGSizeMake(width, 999)];
    if (size.height < 20) {
        return 10+44+10;
    }
    return 10+44+size.height;
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
