//
//  GGUserListView.h
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol GGUserInfoClickDelegate <NSObject>

@optional
- (void)clickUser:(AVUser *)user;
@end

@interface GGUserListView : UIView

@property (nonatomic,strong)UICollectionView *collectionView;

- (instancetype)initWithFrame:(CGRect)frame withTeamModel:(GGTeamModel *)model;


- (void)setUserListDataWithArray:(NSArray *)array maxNum:(NSInteger)maxNum;//设置初始化时的人员列表


- (void)upDateUserSpeakerWithUid:(NSNumber *)uid;//更新是否正在说话
- (void)upDateUserMacStatus:(NSNumber *)uid isMac:(BOOL)isMac;//更新麦克风
- (void)upDateUserOnlineStatus:(NSNumber *)uid offline:(BOOL)offline;//更新在线离线状态

- (void)membersRemove:(NSString *)objectId;
- (void)membersAdd:(NSString *)objectId;

@property (nonatomic,assign)id<GGUserInfoClickDelegate>v_delegate;


@end
