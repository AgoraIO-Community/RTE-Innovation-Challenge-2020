//
//  QueryMatchTimer.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "QueryMatchTimer.h"

@interface QueryMatchTimer ()

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, strong) TimerBlock timerBlock;

@end

@implementation QueryMatchTimer

- (void)startTimerWithBlock:(TimerBlock)timerBlock {
    
    self.timer = [NSTimer timerWithTimeInterval:3 target:self selector:@selector(_timerAction) userInfo:nil repeats:YES];
    
    [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSRunLoopCommonModes];
    _timerBlock = timerBlock;
    
}

- (void)_timerAction {
    if (self.timerBlock) {
        self.timerBlock();
    }
}

- (void)stopTimer {
    [self.timer invalidate];
}


@end
