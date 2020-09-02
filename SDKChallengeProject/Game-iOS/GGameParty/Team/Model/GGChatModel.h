//
//  GGChatModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGChatModel : GGRootModel

@property (nonatomic, strong) NSString *text;

@property (nonatomic, strong) AVIMConversation *conversation;

@property (nonatomic, assign) NSInteger  type;

@property (nonatomic, strong) NSString  *userObjectId;

@property (nonatomic, strong) AVIMTypedMessage * message;



+ (GGChatModel *)valueToModel:(AVIMTypedMessage *)message conversation:(AVIMConversation *)conversation;

@end
