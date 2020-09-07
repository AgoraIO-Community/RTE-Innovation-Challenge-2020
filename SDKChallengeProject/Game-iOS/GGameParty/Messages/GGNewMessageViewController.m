//
//  GGNewMessageViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGNewMessageViewController.h"
#import "GGConversionListViewController.h"
#import "GGSysMessageViewController.h"
#import "AppNavView.h"
#import "GGSysMesModel.h"
#import "GGSingleConversationViewController.h"
#import "GGChatRoomConversationViewController.h"
#import "GGUserInfoViewController.h"
@interface GGNewMessageViewController ()<GGNavBarDelegate,
LCCKConversationListViewControllerDelegate>
@property (nonatomic,strong)AppNavView *navView;

@property (nonatomic,strong)GGConversionListViewController *conversitonView;

@property (nonatomic,strong)GGSysMessageViewController *sysMsgView;

@end

@implementation GGNewMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = YES;
    self.navView = [[AppNavView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 61) titleArr:@[@"聊天消息",@"系统消息"]];
    self.navView.v_delegate = self;
    [self.view addSubview:self.navView];
    if (IS_IPHONE_X) {
        self.navView.mj_y = 44;
    }
    
    [self.navView showRedPointIndex:1];
    
    [self clickTitle:@"聊天消息" index:0];
}


#pragma mark - Nav.Delegate
- (void)clickTitle:(NSString *)title index:(NSInteger)index
{
    [self.navView hiddenRedPointIndex:index];
    if ([title isEqualToString:@"聊天消息"])
    {
      //  [self.view bringSubviewToFront:self.scrollPageView];
        if (!_conversitonView) {
            _conversitonView = [[GGConversionListViewController alloc]init];
            _conversitonView.view.frame =  CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - CGRectGetMaxY(self.navView.frame) - 49);
            _conversitonView.delegate = self;
              [self.view addSubview:self.conversitonView.view];
        }
         [self.view bringSubviewToFront:self.conversitonView.view];
    }
    if ([title isEqualToString:@"系统消息"])
    {
        if (!_sysMsgView) {
            _sysMsgView = [[GGSysMessageViewController alloc]init];
            _sysMsgView.view.frame =  CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - CGRectGetMaxY(self.navView.frame) - 49);
            [self.view addSubview:self.sysMsgView.view];
        @WeakObj(self);
            _sysMsgView.pushSysMsgDetailBlock = ^(GGSysMesModel *model) {
                if ([model.type isEqualToString:@"3"])
                {
//                    GGSimpleWebViewController *webVC = [[GGSimpleWebViewController alloc] init];
//                    webVC.URL = [NSURL URLWithString:model.content];
//                    webVC.hidesBottomBarWhenPushed = YES;
//                    [selfWeak.navigationController pushViewController:webVC animated:YES];
                }
                if ([model.type isEqualToString:@"1"])
                {
                    AVUser *user = model.user;
                    GGUserInfoViewController *info = [[GGUserInfoViewController alloc]init];
                    info.objectId = user.objectId;
                    info.hidesBottomBarWhenPushed = YES;
                    [selfWeak.navigationController pushViewController:info animated:YES];
                }
                //type=2是举报消息
                
            };
        }
        [self.view bringSubviewToFront:self.sysMsgView.view];
    }
    
}

- (void)conversation:(AVIMConversation *)conversation tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    //跳转到聊天详情
    if (conversation.transient == YES) {
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        [conversation joinWithCallback:^(BOOL succeeded, NSError *error) {
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            if (succeeded) {
                NSLog(@"加入成功！");
                GGChatRoomConversationViewController *conversationVC = [[GGChatRoomConversationViewController alloc] initWithConversationId:conversation.conversationId];
                conversationVC.hidesBottomBarWhenPushed = YES;
                [self.rt_navigationController pushViewController:conversationVC animated:YES];
            }
            else
            {
                [XHToast showCenterWithText:@"加入失败,请重试"];
            }
        }];
    }
    else
    {
        //
        GGSingleConversationViewController *conversationVC = [[GGSingleConversationViewController alloc] initWithConversationId:conversation.conversationId];
        conversationVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:conversationVC animated:YES];
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
