//
//  GGGameManger.h
//  GGameParty
//
//  Created by Victor on 2018/9/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGGameManger : NSObject
+ (instancetype)shareInstance;

@property (nonatomic, assign) NSInteger index;


@end
