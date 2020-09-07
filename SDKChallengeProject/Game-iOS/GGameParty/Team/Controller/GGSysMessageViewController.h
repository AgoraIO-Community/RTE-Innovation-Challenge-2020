//
//  GGSysMessageViewController.h
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
@class GGSysMesModel;
@interface GGSysMessageViewController : GGRootViewController
@property (nonatomic, copy) void (^pushSysMsgDetailBlock)(GGSysMesModel *model);

@end
