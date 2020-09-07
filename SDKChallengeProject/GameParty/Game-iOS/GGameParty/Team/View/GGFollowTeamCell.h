//
//  GGFollowTeamCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/10.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
@class GGFollowTeamModel;
@class GGTeamModel;
@interface GGFollowTeamCell : UITableViewCell
@property (nonatomic, copy) void (^joinTeamBlock)(GGFollowTeamCell *cell,GGTeamModel *model);
@property (nonatomic, copy) void (^userInfoClickBlock)(GGFollowTeamCell *cell,AVUser *user);


@property (nonatomic,strong)UIButton *headImage;
@property (nonatomic,strong)UILabel *nameLabel;
@property (nonatomic,strong)UILabel *timeLabel;

@property (nonatomic,strong)UIView *teamBackView;
@property (nonatomic,strong)UILabel *titLabel;
@property (nonatomic,strong)UILabel *gameNameLabel;
@property (nonatomic,strong)UILabel *infoLabel;
@property (nonatomic,strong)UIButton *operateButton;

@property (nonatomic,strong)UIView *shodwView;

@property (nonatomic,strong)GGFollowTeamModel *followModel;

- (void)setModel:(GGFollowTeamModel *)model;
@end
