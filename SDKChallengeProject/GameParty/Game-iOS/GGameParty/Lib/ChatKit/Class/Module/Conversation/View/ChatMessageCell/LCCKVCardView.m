//
//  LCCKVCardView.m
//  ChatKit-OC
//
//  v0.8.5 Created by ElonChan on 16/8/15.
//  Copyright © 2016年 ElonChan . All rights reserved.
//

#import "LCCKVCardView.h"
#if __has_include(<ChatKit/LCChatKit.h>)
#import <ChatKit/LCChatKit.h>
#else
#import "LCChatKit.h"
#endif

@interface LCCKVCardView()

@property (nonatomic, copy) NSString *clientId;

@property (nonatomic, strong) UIImageView *headImage;



@property (nonatomic, strong) UIImageView *ggheadImage;
@property (nonatomic, strong) UILabel *infoLabel;
@property (nonatomic, strong) UILabel *titLabel;
@property (nonatomic, strong) UILabel *tipsLabel;


@end

@implementation LCCKVCardView
//
//+ (id)vCardView {
//    return [[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class]) owner:nil options:nil][0];
//}
//
//- (void)awakeFromNib {
//    [super awakeFromNib];
//    [self setup];
//}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        //UIView *back = [UIView alloc]initWithFrame:CGRect
        self.backgroundColor = [UIColor whiteColor];
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 10;
        
        self.ggheadImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 55, 55)];
        self.ggheadImage.backgroundColor = Main_Color;
        self.ggheadImage.layer.masksToBounds = YES;
        self.ggheadImage.layer.cornerRadius = 55/2;
        [self addSubview:self.ggheadImage];
        
        self.titLabel = [[UILabel alloc]initWithFrame:CGRectMake(75, 10, 150, 25)];
        self.titLabel.text = @"test";
        [self addSubview:self.titLabel];
        
        UITapGestureRecognizer *tapGestureRecognizer =[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(vCardClicked)];
        [self addGestureRecognizer:tapGestureRecognizer];
        
    }
    return self;
}

- (void)setup {
    self.backgroundColor = [UIColor whiteColor];
    
    self.ggheadImage.frame = CGRectMake(10, 10, 55, 55);
    self.titLabel.frame = CGRectMake(75, 10, 150, 25);
    UITapGestureRecognizer *tapGestureRecognizer =[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(vCardClicked)];
    [self addGestureRecognizer:tapGestureRecognizer];
}

//- (CGSize)sizeThatFits:(CGSize)size
//{
//    return CGSizeMake(227, 112);
//}
//

- (void)vCardClicked {
    !self.vCardDidClickedHandler ?: self.vCardDidClickedHandler(self.clientId);
}

- (void)configureWithAvatarURL:(NSURL *)avatarURL title:(NSString *)title clientId:(NSString *)clientId
{
    [self.ggheadImage sd_setImageWithURL:avatarURL placeholderImage:GGDefault_User_Head];
    self.titLabel.text = title;
    self.clientId = clientId;
}

@end
