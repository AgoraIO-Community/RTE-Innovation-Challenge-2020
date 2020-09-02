//
//  GGChatTeamRoomCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGChatModel.h"



@interface GGChatTeamRoomCell : UITableViewCell

@property (nonatomic, copy) void (^invitaFriendClick)(GGChatTeamRoomCell *view);
@property (nonatomic, copy) void (^userClick)(GGChatTeamRoomCell *view,NSString *objectId);


@property (nonatomic,strong)UIView *backView;

@property (nonatomic,strong)UILabel *contentLabel;

@property (nonatomic,strong)UIButton *operatBtn;

@property (nonatomic,strong)UIButton *nameBtn;

@property (nonatomic,strong)GGChatModel *model;

- (void)setModel:(GGChatModel *)model;

+ (CGFloat)valiateHeightWithModel:(GGChatModel *)model;

@end
