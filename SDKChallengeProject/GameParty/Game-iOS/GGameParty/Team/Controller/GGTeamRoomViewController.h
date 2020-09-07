//
//  GGTeamRoomViewController.h
//  GGameParty
//
//  Created by Victor on 2018/7/27.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
#import "GGTeamModel.h"

@interface GGTeamRoomViewController : GGRootViewController

@property (nonatomic,strong)GGTeamModel *teamModel;

@property (nonatomic,assign)BOOL showRoomSetView;

@property (nonatomic,assign)BOOL isJoinTeam;

@property (nonatomic,strong)NSString *teamCode;

@property (nonatomic, strong) UIImage *hk_iconImage;

- (void)quitTeam;
@end
