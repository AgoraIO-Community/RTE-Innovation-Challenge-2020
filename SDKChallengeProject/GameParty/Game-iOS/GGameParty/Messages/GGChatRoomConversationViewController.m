//
//  GGChatRoomConversationViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/17.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChatRoomConversationViewController.h"
#import "GGTipFollowView.h"
@interface GGChatRoomConversationViewController ()

@end

@implementation GGChatRoomConversationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationController.navigationBar setBackgroundImage:[UIImage imageWithColor:[UIColor whiteColor]] forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
    
    [self.navigationController.navigationBar setBarTintColor:[UIColor whiteColor]];
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:GGTitle_Color,NSForegroundColorAttributeName,nil]];
    
    GGTipFollowView *tips = [[GGTipFollowView alloc]initWithFrame:CGRectMake(15, 10, kScreenW - 30, 40)type:@"1"];
    tips.layer.masksToBounds = YES;
    tips.layer.cornerRadius = 5;
    tips.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:tips];
    tips.closeTips = ^(GGTipFollowView *editView) {
        editView.hidden = YES;
    };
    
    
    UIBarButtonItem *right = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"icon_guanbi3"] style:UIBarButtonItemStyleDone target:self action:@selector(quitConver)];
    self.navigationItem.rightBarButtonItem = right;
}

- (void)quitConver
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)dealloc
{
  //  NSLog(@"%@释放",[NSString stringWithFormat:@"%s",class_getName(self.class)]);
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
