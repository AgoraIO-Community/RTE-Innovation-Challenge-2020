//
//  GGFollowTeamModel.h
//  GGameParty
//
//  Created by Victor on 2018/8/10.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGFollowTeamModel : GGRootModel
@property (nonatomic, strong) AVUser *user;
@property (nonatomic, strong) GGTeamModel  *teamModel;

@property (nonatomic, strong) NSDate  *createdAt;

@end
