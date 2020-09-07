//
//  GGSqVideoViewController.h
//  GGameParty
//
//  Created by Victor on 2018/8/9.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
#import "SJVideoPlayer.h"
#import "SquareModel.h"
@interface GGSqVideoViewController : GGRootViewController
@property (nonatomic, copy) void (^pushToVideoDetailBlock)(GGSqVideoViewController *vc,SJVideoPlayerURLAsset *asset,SquareModel *model,NSInteger index);

@end
