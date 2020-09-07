//
//  GGRoomManger.h
//  GGameParty
//
//  Created by Victor on 2018/9/16.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>
@class GGTeamRoomViewController;
@interface GGRoomManger : NSObject

+ (instancetype)shareInstance;

@property (nonatomic, strong) GGTeamModel *teamModel;

@property (strong, nonatomic)GGTeamRoomViewController *currentTeamRoom;


+ (void)pushToRoom:(GGTeamModel *)model;

@end
