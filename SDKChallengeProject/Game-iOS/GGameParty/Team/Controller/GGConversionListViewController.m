//
//  GGConversionListViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGConversionListViewController.h"
#import "GGTeamInviteMessage.h"
#import "GGTeamInviteCell.h"
#import "LCCKVCardView.h"
@interface GGConversionListViewController ()<LCCKConversationListViewControllerDelegate>

@end

@implementation GGConversionListViewController

- (UIBarButtonItem *)rt_customBackItemWithTarget:(id)target
                                          action:(SEL)action
{
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    [btn setImage:[UIImage imageNamed:@"icon_fanhuibbb"] forState:UIControlStateNormal];
    [btn sizeToFit];
    [btn addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:btn];
}




- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    if (self.messageAttr)
    {
        self.delegate = self;
    }
    
}

- (void)sendTeamMessage:(AVIMConversation *)conversation
{
    GGTeamInviteMessage *vCardMessage = [GGTeamInviteMessage vCardMessageWithClientId:[AVUser currentUser].objectId conversationType:[conversation lcck_type] attr:self.messageAttr];
    [conversation sendMessage:vCardMessage callback:^(BOOL succeeded, NSError * _Nullable error) {
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        if (succeeded) {
            UIAlertController *alertLength = [UIAlertController alertControllerWithTitle:@"提示" message:@"已成功发送" preferredStyle:UIAlertControllerStyleAlert];
            UIAlertAction *suer = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [self.navigationController popViewControllerAnimated:YES];
            }];
            [alertLength addAction:suer];
            [self presentViewController:alertLength animated:YES completion:nil];
        }
        else
        {
            [XHToast showCenterWithText:@"发送失败,请重试"];
        }
    }];
}

//由会话列表进入聊天详情界面
- (void)conversation:(AVIMConversation *)conversation tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    if (conversation.transient == YES)
    {
        [conversation joinWithCallback:^(BOOL succeeded, NSError *error) {
            if (succeeded)
            {
                [self sendTeamMessage:conversation];
            }
            else
            {
                [MBProgressHUD hideHUDForView:self.view animated:YES];
                [XHToast showCenterWithText:@"加入失败,请重试"];
            }
        }];
    }
    else
    {
        [self sendTeamMessage:conversation];
    }
}


@end
