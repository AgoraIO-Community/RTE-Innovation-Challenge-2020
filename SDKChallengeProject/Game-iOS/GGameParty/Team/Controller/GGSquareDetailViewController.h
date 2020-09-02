//
//  GGSquareDetailViewController.h
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
@class SquareModel, SJVideoPlayerURLAsset;

@interface GGSquareDetailViewController : GGRootViewController


- (instancetype)initWithVideo:(SquareModel *)video asset:(SJVideoPlayerURLAsset *__nullable)asset;

- (instancetype)initWithVideo:(SquareModel *)video beginTime:(NSTimeInterval)beginTime;

@property (nonatomic,assign)NSInteger index;

@end
