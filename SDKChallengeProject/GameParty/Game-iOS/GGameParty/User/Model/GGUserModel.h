//
//  GGUserModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootModel.h"

@interface GGUserModel : GGRootModel<LCCKUserDelegate>

@property (nonatomic,strong)NSString *username;

@property (nonatomic,assign)BOOL sex;

@property (nonatomic,strong)NSString *lastAddress;

@property (nonatomic,strong)NSNumber *age;

@property (nonatomic,strong)NSString *objectId;


@property (nonatomic,strong)AVFile *avatar;

+ (GGUserModel *)valueToObj:(AVUser *)obj;

- (BOOL)isEqualToUer:(GGUserModel *)user;

- (void)saveToDiskWithKey:(NSString *)key;

+ (id)loadFromDiskWithKey:(NSString *)key;
+ (void)postFollowMsg:(NSString *)objId;
+ (void)queryUserRelationWithUser:(AVUser *)user withBlock:(AVArrayResultBlock)resultBlock;
+ (void)addRelation:(NSString *)relation WithUser:(AVUser *)user withBlock:(AVBooleanResultBlock)block;//relation:1 关注 2 拉黑
+ (void)queryUserRelation:(NSString *)realtion withBlock:(AVArrayResultBlock)resultBlock;

+ (void)userGoodRelation:(NSString *)beGooder;

+ (void)queryGoodRelation:(NSString *)beGooder withBlock:(AVArrayResultBlock)resultBlock;

+ (void)queryUserWithName:(NSString *)name withBlock:(AVArrayResultBlock)resultBlock;

@end
