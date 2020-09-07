//
//  GGChatModel.m
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChatModel.h"

@implementation GGChatModel

+ (GGChatModel *)valueToModel:(AVIMTypedMessage *)message conversation:(AVIMConversation *)conversation
{
    GGChatModel *model = [[GGChatModel alloc]init];
    model.type = 1;
    model.text = message.text;
    model.message = message;
    if (conversation) {
        model.conversation = conversation;
    }
    return model;
}

@end
