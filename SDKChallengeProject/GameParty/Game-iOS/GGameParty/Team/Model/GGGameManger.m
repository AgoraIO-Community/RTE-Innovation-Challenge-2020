//
//  GGGameManger.m
//  GGameParty
//
//  Created by Victor on 2018/9/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGGameManger.h"

@implementation GGGameManger
static GGGameManger *_defaultManager;

+ (instancetype)shareInstance
{
    if (!_defaultManager) {
        _defaultManager = [[self alloc] init];
    }
    return _defaultManager;
}

- (instancetype)init
{
    if (!_defaultManager) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            _defaultManager = [super init];
        });
    }
    return _defaultManager;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone {
    if (!_defaultManager) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            _defaultManager = [super allocWithZone:zone];
        });
    }
    return _defaultManager;
}
- (id)copyWithZone:(NSZone *)zone{
    return _defaultManager;
}
- (id)mutableCopyWithZone:(NSZone *)zone{
    return _defaultManager;
}
@end
