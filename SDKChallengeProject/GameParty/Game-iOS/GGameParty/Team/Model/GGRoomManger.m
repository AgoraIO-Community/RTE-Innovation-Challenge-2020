//
//  GGRoomManger.m
//  GGameParty
//
//  Created by Victor on 2018/9/16.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRoomManger.h"

@implementation GGRoomManger

static GGRoomManger *_defaultManager;


+ (void)pushToRoom:(GGTeamModel *)model
{
    AppDelegate * app = (AppDelegate*)[UIApplication sharedApplication].delegate;
    GGRoomManger *manger = [GGRoomManger shareInstance];
    if (!manger.teamModel)
    {
        GGTeamRoomViewController *room = [[GGTeamRoomViewController alloc]init];
        room.hidesBottomBarWhenPushed = YES;
        room.teamModel = model;
        
        [app.currentNav pushViewController:room animated:YES];
    }
    else
    {
        if ([manger.teamModel.objectId isEqualToString:model.objectId]) {
            //是同一个
            //回到原房间
            if (!manger.currentTeamRoom)
            {
                GGTeamRoomViewController *room = [[GGTeamRoomViewController alloc]init];
                room.hidesBottomBarWhenPushed = YES;
                room.teamModel = manger.teamModel;
                [app.currentNav pushViewController:room animated:YES];
            }
            else
            {
                GGTeamRoomViewController *room = manger.currentTeamRoom;
                room.hidesBottomBarWhenPushed = YES;
                [app.currentNav pushViewController:room animated:YES];
            }
        }
        else
        {//弹出提示
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:model?@"你必须先离开或解散上次的房间才可以加入其他房间":@"你当前已有组队,继续加入将会自动退出原房间,请务必谨慎" preferredStyle:UIAlertControllerStyleAlert];
            
            UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"加入新房间" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
                NSLog(@"加入新房间");
                //先退出--->再加入
                [XWFloatingWindowView remove];
                [[GGRoomManger shareInstance].currentTeamRoom quitTeam];
                [self pushToRoom:model];
            }];
            UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"回到旧房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                NSLog(@"回到旧房间");
                if (!manger.currentTeamRoom)
                {
                    GGTeamRoomViewController *room = [[GGTeamRoomViewController alloc]init];
                    room.hidesBottomBarWhenPushed = YES;
                    room.teamModel = manger.teamModel;
                    [app.currentNav pushViewController:room animated:YES];
                }else
                {
                    GGTeamRoomViewController *room = manger.currentTeamRoom;
                    room.hidesBottomBarWhenPushed = YES;
                    [app.currentNav pushViewController:room animated:YES];
                }
            }];
            
            UIAlertAction *action3 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                NSLog(@"取消");
            }];
            
            if (manger.currentTeamRoom) {
                [alert addAction:action1];
            }
            [alert addAction:action2];
            [alert addAction:action3];
            [app.currentNav presentViewController:alert animated:YES completion:nil];
        }
    }
}


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
