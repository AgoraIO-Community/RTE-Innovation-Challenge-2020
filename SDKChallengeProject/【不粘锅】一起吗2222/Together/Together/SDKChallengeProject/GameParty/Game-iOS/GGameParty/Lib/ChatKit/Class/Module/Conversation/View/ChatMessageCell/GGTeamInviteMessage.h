//
//  GGTeamInviteMessage.h
//  GGameParty
//
//  Created by Victor on 2018/8/16.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <AVOSCloudIM/AVOSCloudIM.h>
static AVIMMessageMediaType const kAVIMMessageMediaTypeVCard = 1;

@interface GGTeamInviteMessage : AVIMTypedMessage<AVIMTypedMessageSubclassing>
- (instancetype)initWithClientId:(NSString *)clientId conversationType:(LCCKConversationType)conversationType attr:(NSDictionary *)dic;
+ (instancetype)vCardMessageWithClientId:(NSString *)clientId conversationType:(LCCKConversationType)conversationType attr:(NSDictionary *)dic;




@end
