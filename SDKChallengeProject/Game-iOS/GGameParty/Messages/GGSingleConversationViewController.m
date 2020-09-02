//
//  GGSingleConversationViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/17.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSingleConversationViewController.h"
#import "GGTipFollowView.h"
#import "GGUserInfoViewController.h"
@interface GGSingleConversationViewController ()

@end

@implementation GGSingleConversationViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    [self.navigationController.navigationBar setBackgroundImage:[UIImage imageWithColor:[UIColor whiteColor]] forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
    
    [self.navigationController.navigationBar setBarTintColor:[UIColor whiteColor]];
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:GGTitle_Color,NSForegroundColorAttributeName,nil]];
    
    AVIMConversationQuery *query = [[LCChatKit sharedInstance].client conversationQuery];
    [query getConversationById:self.conversationId callback:^(AVIMConversation *conversation, NSError *error) {
        // Tom 查看会话中成员的数量
        NSArray *members = conversation.members;
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"NOT (SELF IN %@)", @[                                                                                                                                                                             [LCChatKit sharedInstance].clientId]];
        NSArray *arr =[members filteredArrayUsingPredicate:predicate];
        if (arr.count > 0) {
            NSString *peerId = arr[0];
            self.userId = peerId;
            [self checkFollow];
            
            UIBarButtonItem *right = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"icon_goDetail"] style:UIBarButtonItemStyleDone target:self action:@selector(goDetail)];
//            self.navigationItem.rightBarButtonItem = right;
        }
    }];
    
    //icon_dating
   
}

- (void)goDetail
{
    GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
    view.hidesBottomBarWhenPushed = YES;
    view.objectId = self.userId;
    [self.navigationController pushViewController:view animated:YES];
}

- (void)checkFollow
{
    AVQuery *queryfollow = [AVUser followeeQuery:[AVUser currentUser].objectId];
    
    AVObject *userobj = [AVObject objectWithClassName:DB_USER objectId:self.userId];
    [queryfollow whereKey:@"followee" equalTo:userobj];
    
    [queryfollow findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count == 0)
            {
                //没关注
                NSLog(@"关注即可随时查看TA的组队动态");
                [self showFollowView];
            }
        }
    }];
}

- (void)showFollowView
{
    GGTipFollowView *tips = [[GGTipFollowView alloc]initWithFrame:CGRectMake(15, 10, kScreenW - 30, 40)];
    tips.userId = self.userId;
    tips.layer.masksToBounds = YES;
    tips.layer.cornerRadius = 5;
    tips.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:tips];
    tips.haveFollowUser = ^(GGTipFollowView *editView) {
        editView.hidden = YES;
    };
    
}


- (void)dealloc
{
    //NSLog(@"%@释放",[NSString stringWithFormat:@"%s",class_getName(self.class)]);
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
