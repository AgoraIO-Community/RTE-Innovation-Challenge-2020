//
//  GGCommentTool.m
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGCommentTool.h"
#import "SquareModel.h"
@implementation GGCommentTool

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.goodButton = [SJUIButtonFactory buttonWithImageName:@"icon_guzhang2" target:self sel:@selector(goodClick:) tag:1];
        [self.goodButton setTitle:@"15" forState:UIControlStateNormal];
        self.goodButton.frame = CGRectMake(20, 0, 60, 40);
        [self addSubview:self.goodButton];
        [self.goodButton setTitleColor:RGBCOLOR(170, 170, 170) forState:UIControlStateNormal];
       

        self.commentButton = [SJUIButtonFactory buttonWithImageName:@"icon_pl" target:self sel:@selector(goodClick:) tag:2];
        [self.commentButton setTitle:@"300" forState:UIControlStateNormal];
        self.commentButton.frame = CGRectMake(CGRectGetMaxX(self.goodButton.frame)+20, 0, 60, 40);
        [self addSubview:self.commentButton];
        [self.commentButton setTitleColor:RGBCOLOR(170, 170, 170) forState:UIControlStateNormal];
        
        self.shareButton = [SJUIButtonFactory buttonWithImageName:@"icon_zf" target:self sel:@selector(goodClick:) tag:3];
        [self.shareButton setTitle:@"转发" forState:UIControlStateNormal];
        self.shareButton.frame = CGRectMake(kScreenW - 70, 0, 50, 40);
        [self.shareButton setTitleColor:RGBCOLOR(170, 170, 170) forState:UIControlStateNormal];
        [self addSubview:self.shareButton];
        
        
        [self.goodButton setImage:[UIImage imageNamed:@"icon_guzhang"] forState:UIControlStateSelected];
        [self.goodButton setTitleColor:Main_Color forState:UIControlStateSelected];
        [self.goodButton setTitleColor:RGBCOLOR(204, 204, 204) forState:UIControlStateNormal];
        
        
        [self.goodButton setImageEdgeInsets:UIEdgeInsetsMake(0.0, -20, 0.0, 0.0)];
        [self.commentButton setImageEdgeInsets:UIEdgeInsetsMake(0.0, -20, 0.0, 0.0)];
        [self.shareButton setImageEdgeInsets:UIEdgeInsetsMake(0.0, -20, 0.0, 0.0)];
        
        
    }
    return self;
}

- (void)goodClick:(UIButton *)btn
{
    if (btn.tag == 1) {
        //点赞
        [self giveVideoGood];
    }
    else
    {
        if (self.operateBtnClickBlock) {
            self.operateBtnClickBlock(btn.tag);
        }
    }
}

- (void)giveVideoGood
{
    self.goodButton.selected = !self.goodButton.selected;
    AVObject *obj = [AVObject objectWithClassName:DB_SQUARE objectId:self.squareModel.objectId];
    
    CGFloat goodNum;
    if (self.goodButton.selected)
    {
        goodNum = [self.squareModel.goodNum floatValue];
        [obj addUniqueObject:[AVUser currentUser].objectId forKey:@"isgood"];
        [obj incrementKey:@"goodNum"];
        goodNum = [self.squareModel.goodNum floatValue] + 1;
    }
    else
    {
        goodNum = [self.squareModel.goodNum floatValue];
        [obj removeObject:[AVUser currentUser].objectId forKey:@"isgood"];
        goodNum = [self.squareModel.goodNum floatValue] - 1;
        [obj incrementKey:@"goodNum" byAmount:[NSNumber numberWithInt:-1]];
    }
    //超过1000显示为1.1k
    NSString * num = [NSString stringWithFormat:@"%ld",(long)goodNum];;
//    if (goodNum<1000)
//    {
//        num = [NSString stringWithFormat:@"%ld",(long)goodNum];
//    }
//    else
//    {
//        CGFloat xxx = goodNum /1000.0f;
//        num = [NSString stringWithFormat:@"%.1fk",xxx];
//    }
    
    [self.goodButton setTitle:num forState:UIControlStateNormal];
    self.squareModel.goodNum = [NSNumber numberWithInteger:[num integerValue]];
    obj.fetchWhenSave = true;
    [obj saveInBackground];
    if (self.giveGoodBlock) {
        self.giveGoodBlock( self.goodButton.selected);
    }
    
}

- (void)setModel:(SquareModel *)model
{
    self.squareModel = model;
    [self.commentButton setTitle:[NSString stringWithFormat:@"%@",model.commentNum] forState:UIControlStateNormal];
    [self.goodButton setTitle:[NSString stringWithFormat:@"%@",model.goodNum] forState:UIControlStateNormal];
    
//    CGFloat  goodNum = [self.squareModel.goodNum floatValue];
//    NSString *num;
//    if (goodNum<1000)
//    {
//        num = [NSString stringWithFormat:@"%ld",(long)goodNum];
//    }
//    else
//    {
//        CGFloat xxx = goodNum /1000.0f;
//        num = [NSString stringWithFormat:@"%.1fk",xxx];
//    }
//    [self.goodButton setTitle:num forState:UIControlStateNormal];

    
    if ([model.isgood containsObject:[AVUser currentUser].objectId]) {
        self.goodButton.selected = YES;
    }
    
}



/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
