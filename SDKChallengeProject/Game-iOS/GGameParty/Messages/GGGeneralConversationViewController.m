//
//  GGGeneralConversationViewController.m
//  GGameParty
//
//  Created by Victor on 2018/9/17.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGGeneralConversationViewController.h"

@interface GGGeneralConversationViewController ()

@end

@implementation GGGeneralConversationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    @WeakObj(self);
    [self setFetchConversationHandler:^(AVIMConversation *conversation, LCCKConversationViewController *conversationController) {
        if (!conversation) {
            [XHToast showBottomWithText:@"会话获取失败"];
            [selfWeak.navigationController popViewControllerAnimated:YES];
        }
    }];
    
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
