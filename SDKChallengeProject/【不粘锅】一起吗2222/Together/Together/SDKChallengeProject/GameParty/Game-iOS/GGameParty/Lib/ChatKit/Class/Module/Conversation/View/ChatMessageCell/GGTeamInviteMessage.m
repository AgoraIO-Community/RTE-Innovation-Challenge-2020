//
//  GGTeamInviteMessage.m
//  GGameParty
//
//  Created by Victor on 2018/8/16.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamInviteMessage.h"

@implementation GGTeamInviteMessage

/*!
 * 有几个必须添加的字段：
 *  - degrade 用来定义如何展示老版本未支持的自定义消息类型
 *  - typeTitle 最近对话列表中最近一条消息的title，比如：最近一条消息是图片，可设置该字段内容为：`@"图片"`，相应会展示：`[图片]`。
 *  - summary 会显示在 push 提示中
 *  - conversationType 用来展示在推送提示中，以达到这样的效果： [群消息]Tom：hello gays!
 * @attention 务必添加这几个字段，ChatKit 内部会使用到。
 */
- (instancetype)initWithClientId:(NSString *)clientId  conversationType:(LCCKConversationType)conversationType attr:(NSDictionary *)dic {
    self = [super init];
    if (!self) {
        return nil;
    }
    [self lcck_setObject:@"组队消息" forKey:LCCKCustomMessageTypeTitleKey];
    [self lcck_setObject:@"这是一条组队邀请消息，当前版本过低无法显示，请尝试升级APP查看" forKey:LCCKCustomMessageDegradeKey];
    [self lcck_setObject:@"有人向您发送了游戏邀请，请打开APP查看" forKey:LCCKCustomMessageSummaryKey];
    [self lcck_setObject:@(conversationType) forKey:LCCKCustomMessageConversationTypeKey];
    [self lcck_setObject:clientId forKey:@"clientId"];
    [self lcck_setObject:dic forKey:@"team"];
    //定向群消息，仅部分用户可见，需要实现 `-setFilterMessagesBlock:`, 详情见 LCChatKitExample 中的演示
    //    [self lcck_setObject:@[ @"Tom", @"Jerry"] forKey:LCCKCustomMessageOnlyVisiableForPartClientIds];
    return self;
}

+ (instancetype)vCardMessageWithClientId:(NSString *)clientId  conversationType:(LCCKConversationType)conversationType attr:(NSDictionary *)dic {
    return [[self alloc] initWithClientId:clientId conversationType:conversationType attr:dic];
}

#pragma mark -
#pragma mark - Override Methods

#pragma mark -
#pragma mark - AVIMTypedMessageSubclassing Method

+ (void)load {
    [self registerSubclass];
}

+ (AVIMMessageMediaType)classMediaType {
    return kAVIMMessageMediaTypeVCard;
}

@end
