//
//  GGUserInfoCardView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGTeamModel.h"
@interface GGUserInfoCardView : UIView

@property (nonatomic,assign)BOOL tuandui;
@property (nonatomic,assign)BOOL zuijia;
@property (nonatomic,assign)BOOL taidu;
@property (nonatomic,assign)BOOL leyu;

@property (nonatomic, copy) void (^sureBtnClick)(GGUserInfoCardView *userCardView, UIButton *button);

@property (nonatomic, copy) void (^closeBtnClick)(GGUserInfoCardView *userCardView);
//发消息按钮点击
@property (nonatomic, copy) void (^sendMegClick)(GGUserInfoCardView *userCardView,AVUser *user);

//踢出群
@property (nonatomic, copy) void (^getOutRoomClick)(GGUserInfoCardView *userCardView,AVUser *user);

- (instancetype)initWithFrame:(CGRect)frame user:(AVUser *)user teamdModel:(GGTeamModel *)teamModel;
@end
