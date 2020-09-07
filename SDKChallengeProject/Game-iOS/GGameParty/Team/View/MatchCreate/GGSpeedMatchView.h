//
//  GGSpeedMatchView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGPopupView.h"
#import "GGGameModel.h"
@interface GGSpeedMatchView : GGPopupView

@property (nonatomic, copy) void (^matchBtnClick)(GGSpeedMatchView *view, GGGameModel *model,NSArray *selectArr);


- (void)setGameData:(NSArray *)dataArr;

@end
