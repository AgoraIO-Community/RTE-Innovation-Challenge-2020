//
//  GGUserListCell.m
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserListCell.h"
#import "GGUserListModel.h"
@interface GGUserListCell()

@end


@implementation GGUserListCell
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        CGFloat width = 60;
        UIImageView *head = [UIImageView new];
        head.frame = CGRectMake(0, 0 , width, width);
        head.layer.masksToBounds = YES;
        head.layer.cornerRadius = 30;
        head.image = GGDefault_User_Head;
        self.avatarImage = head;
        [self addSubview:head];
        
        
        self.isSpeakImage = [[UIImageView alloc]init];
        self.isSpeakImage.frame = CGRectMake(width - 14, -10, 28, 28);
        [ self addSubview:self.isSpeakImage];
        self.isSpeakImage.image = [UIImage imageNamed:@"icon_talk"];
        
        self.noMacImage = [[UIImageView alloc]init];
        self.noMacImage.frame = CGRectMake((width - 41.5)/2, width - 15, 41.5, 15);
        [self addSubview:self.noMacImage];
        self.noMacImage.image = [UIImage imageNamed:@"icon_bimai"];
        
        
        self.offLineView = [[UIImageView alloc]init];
        self.offLineView.frame = CGRectMake((width - 41.5)/2, width - 15, 41.5, 15);
        [self addSubview:self.offLineView];
        self.offLineView.image = [UIImage imageNamed:@"icon_lixian"];
        
        
        
        UILabel *name = [SJUILabelFactory labelWithText:@"王二麻" textColor:[UIColor blackColor] font:[UIFont systemFontOfSize:11]];
        name.frame = CGRectMake(head.frame.origin.x + 2.5, CGRectGetMaxY(head.frame) + 5, head.frame.size.width - 5, 20);
        name.backgroundColor = [UIColor whiteColor];
        name.alpha = 0.3;
        name.textAlignment = NSTextAlignmentCenter;
        name.layer.masksToBounds = YES;
        name.layer.cornerRadius = 10;
        [self addSubview:name];
        self.nameLabel = name;
        
        self.offLineView.hidden = YES;
        self.isSpeakImage.hidden = YES;
        self.noMacImage.hidden = YES;
        
    }
    return self;
}


- (void)setUserListModel:(GGUserListModel *)model
{
    AVObject *user = model.user;
    [self setWithModel:user];
    if (model) {
        self.offLineView.hidden = model.isOnline;
        self.noMacImage.hidden = model.isNoMac;//isNoMac = YES,表示麦克风开启状态
        self.isSpeakImage.hidden = !model.isSpeaking;
        
        if (model.isSpeaking) {
           
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                model.isSpeaking = NO; //2秒不说话则取消说话标识
                self.isSpeakImage.hidden = YES;
            });
        }
    }else
    {
        self.offLineView.hidden = YES;
        self.noMacImage.hidden = YES;
        self.isSpeakImage.hidden = YES;
    }
}

- (void)setWithModel:(AVObject *)user
{
    if (user)
    {
        self.nameLabel.hidden = NO;
        self.nameLabel.text = [user objectForKey:@"username"];
        [self.avatarImage sd_setImageWithURL:[NSURL URLWithString:[[user objectForKey:@"avatar"] objectForKey:@"url"]] placeholderImage:GGDefault_User_Head];
        if ([user.objectId isEqualToString:[AVUser currentUser].objectId]) {
            self.avatarImage.layer.borderColor = Main_Color.CGColor;
            self.avatarImage.layer.borderWidth = 2;
        }
        else{
            self.avatarImage.layer.borderColor = [UIColor clearColor].CGColor;
            self.avatarImage.layer.borderWidth = 0;
        }
    }
    else
    {
        self.avatarImage.layer.borderColor = [UIColor clearColor].CGColor;
        self.avatarImage.layer.borderWidth = 0;
        
        self.avatarImage.image = [UIImage imageNamed:@"icon_kongwei"];
        
        self.nameLabel.hidden = YES;
        
        self.offLineView.hidden = YES;
        self.isSpeakImage.hidden = YES;
        self.noMacImage.hidden = YES;
    }
}

@end
