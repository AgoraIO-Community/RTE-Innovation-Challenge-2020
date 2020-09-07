//
//  QueryMatchTimer.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef void(^TimerBlock)();

@interface QueryMatchTimer : NSObject

- (void)startTimerWithBlock:(TimerBlock)timerBlock;

- (void)stopTimer;
@end
