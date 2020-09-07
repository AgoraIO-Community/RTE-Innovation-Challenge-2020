//
//  GGTeamModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/25.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"
#import "GGGameModel.h"
@interface GGTeamModel : GGRootModel
+ (GGTeamModel *)shareInstance;
@property (nonatomic,strong)AVObject *publisher;
@property (nonatomic,strong)AVObject *game;
@property (nonatomic,strong)AVObject *area;
@property (nonatomic,strong)NSString *desc;
@property (nonatomic,strong)NSNumber *isLock;
@property (nonatomic,strong)NSArray *participants;//nsstring对象
@property (nonatomic,strong)NSArray *outUserId;//nsstring对象被踢出的user

@property (nonatomic,strong)AVObject *type;//类型
@property (nonatomic,strong)NSString *title;
@property (nonatomic,strong)NSString *objectId;
@property (nonatomic,strong)NSNumber *teamCode;//编码
@property (nonatomic,strong)NSNumber *status;
@property (nonatomic,strong)NSDate *createdAt;
@property (nonatomic,strong)NSDate *updatedAt;

@property (nonatomic,strong)NSString *groupChatId;
@property (nonatomic,strong)NSNumber *isDelete;

@property (nonatomic,assign)NSNumber *maxnum;
+ (GGTeamModel *)vulueToObj:(AVObject *)obj;

+ (void)createRoomWith;


+ (void)queryTeamWithPageNo:(NSInteger)pageNo gameModel:(GGGameModel *)game block:(AVArrayResultBlock)resultBlock;
+ (void)queryTeamWithTitleField:(NSString *)title withPageNo:(NSInteger)pageNo gameModel:(GGGameModel *)game block:(AVArrayResultBlock)resultBlock;

/**
 检查用户是否有权限加入其他组队
 */
+ (void)checkUserCanAddNewTeamWithBlock:(AVArrayResultBlock)resultBlock;

/**
 删除此组队---此方法会清退群内所有人员
 */
+ (void)deleteTeamWithTeamObjectId:(NSString *)teamObjectId;


/**
 保存用户进入房间信息记录

 */
+ (void)saveUserGameHistory:(AVObject *)game conversation:(AVObject *)conversation team:(AVObject *)team;
+ (void)saveUserGameHistory:(AVObject *)game conversation:(AVObject *)conversation team:(AVObject *)team withBlock:(AVBooleanResultBlock)block;


/**
 删除用户进入房间记录,此方法会将team表该人员信息一并删除
 */
+ (void)deleteUserGameHistory;

/**
 删除用户组队信息--踢人时需要执行此方法
 
 @param teamObjectId team表的id
 @param userObjectId 被删的用户id
 */

+ (void)deleteUserGameHistoryWithTeamObjectId:(NSString *)teamObjectId userId:(NSString *)userObjectId;



#pragma mark - 最近队友
/**
 保存用户的最近队友

 @param user 被保存到用户
 */
+ (void)saveHistoryTeamer:(AVObject *)user;
+ (void)saveHistoryTeamerWithUserArr:(NSArray *)userArr;

+ (void)queryHistoryTeamerBlock:(AVArrayResultBlock)resultBlock;

@end
