//
//  GGFindingView.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGFindingView : UIView

@property (nonatomic, copy) void (^findedFailedBlock)(GGFindingView *view);
@property (nonatomic, copy) void (^stopFindBlock)(GGFindingView *view);


@property (nonatomic,strong)UILabel *titLabel;

@property (nonatomic,strong)UILabel *currentOnlineLabel;

@property (nonatomic,strong)UIButton *stopBtn;
- (void)stopBtnClick;
- (void)continueFind;
- (void)stopTimer;

@end
