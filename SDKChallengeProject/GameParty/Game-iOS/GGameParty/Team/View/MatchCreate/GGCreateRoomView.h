//
//  GGCreateRoomView.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGPopupView.h"

#import "GGGameModel.h"

#import "GGGameTypeModel.h"
@interface GGCreateRoomView : GGPopupView

@property (nonatomic, copy) void (^createBtnClick)(GGCreateRoomView *view, GGGameModel *model,GGGameTypeModel *typeModel,NSInteger maxnum);


- (void)setGameData:(NSArray *)dataArr;

@end
