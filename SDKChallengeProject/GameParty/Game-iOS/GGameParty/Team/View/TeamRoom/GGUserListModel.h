//
//  GGUserListModel.h
//  GGameParty
//
//  Created by Victor on 2018/9/17.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGUserListModel : NSObject

@property (nonatomic, strong) AVObject *user;

@property (nonatomic, assign) BOOL isNoMac;

@property (nonatomic, assign) BOOL isSpeaking;

@property (nonatomic, assign) BOOL isOnline;

+ (GGUserListModel *)modelWithUser:(AVUser *)user;

@end
