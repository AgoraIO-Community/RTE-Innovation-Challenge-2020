//
//  GGSquareTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSquareTableViewCell.h"
#import "SJUIFactory.h"
#import "UIView+SJUIFactory.h"
#import "YYLabel.h"
#import "GGGameModel.h"
#import "GGCommentTool.h"
@interface GGSquareTableViewCell()

@property (nonatomic, strong) UIImageView *avatarImageView;
@property (nonatomic, strong) YYLabel *nameLabel;
@property (nonatomic, strong) UIImageView *playImageView;






@end



@implementation GGSquareTableViewCell


+ (CGFloat)heightWithVideo:(SquareModel *)video
{
    return kScreenW * 0.5625 + 40;
}

#pragma mark -

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if ( !self ) return nil;
    self.coverImageView = [SJUIImageViewFactory imageViewWithViewMode:UIViewContentModeScaleAspectFill];
    self.coverImageView.frame = CGRectMake(0, 0, kScreenW, kScreenW * 0.5625);
    [self.contentView addSubview:self.coverImageView];
    
    self.coverImageView.userInteractionEnabled = YES;
    self.coverImageView.tag = 101;
    [self.coverImageView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTap)]];
    
    CGFloat xx = (kScreenW - 80)/2;
    
    
    self.playImageView = [SJUIImageViewFactory imageViewWithImageName:@"icon_bof"];
    self.playImageView.frame = CGRectMake(xx, 0, 80, self.coverImageView.frame.size.height);
   // [self.coverImageView addSubview:self.playImageView];
    
    self.playButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.playButton setImage:[UIImage imageNamed:@"icon_bof"] forState:UIControlStateNormal];
    self.playButton.frame = CGRectMake(xx, 0, 80, self.coverImageView.frame.size.height);
     [self.coverImageView addSubview:self.playButton];
    [self.playButton addTarget:self action:@selector(playSome) forControlEvents:UIControlEventTouchUpInside];
    

    self.commentTool = [[GGCommentTool alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.coverImageView.frame), kScreenW, 40)];
    [self.contentView addSubview:self.commentTool];

    
    return self;
}

- (void)goodClick:(UIButton *)btn
{
    
}

- (void)playSome
{
    [self handleTap];
}

- (void)handleTap
{
    if ( [_delegate respondsToSelector:@selector(clickedPlayOnTabCell:)] ) {
        [_delegate clickedPlayOnTabCell:self];
    }
}


- (void)_addTapGesture {
#warning should be set it tag. 应该设置它的`tag`. 请不要设置为0.
    _coverImageView.userInteractionEnabled = YES;
    _coverImageView.tag = 101;
    [_coverImageView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTap)]];
}


- (void)setModel:(SquareModel *)model
{
    if ( model == _model ) return;
    _model = model;
    [_coverImageView sd_setImageWithURL:[NSURL URLWithString:model.videoCover.url]];
    [self.commentTool setModel:self.model];
}


#pragma mark -
static NSString *sj_processTime(NSTimeInterval createDate, NSTimeInterval nowDate) {
    
    double value = nowDate - createDate;
    
    if ( value < 0 ) {
        return @"火星时间";
    }
    
    NSInteger year  = value / 31104000;
    NSInteger month = value / 2592000;
    NSInteger week  = value / 604800;
    NSInteger day   = value / 86400;
    NSInteger hour  = value / 3600;
    NSInteger min   = value / 60;
    
    if ( year > 0 ) {
        return [NSString stringWithFormat:@"%zd年前", year];
    }
    else if ( month > 0 ) {
        return [NSString stringWithFormat:@"%zd月前", month];
    }
    else if ( week > 0 ) {
        return [NSString stringWithFormat:@"%zd周前", week];
    }
    else if ( day > 0 ) {
        return [NSString stringWithFormat:@"%zd天前", day];
    }
    else if ( hour > 0 ) {
        return [NSString stringWithFormat:@"%zd小时前", hour];
    }
    else if ( min > 0 ) {
        return [NSString stringWithFormat:@"%zd分钟前", min];
    }
    else {
        return @"刚刚";
    }
    return @"";
}

@end
