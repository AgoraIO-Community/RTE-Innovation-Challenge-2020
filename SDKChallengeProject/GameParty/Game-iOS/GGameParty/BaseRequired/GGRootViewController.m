//
//  GGRootViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"
#import "GGTeamRoomViewController.h"
#import "GGQAViewController.h"
#import "UIImage+Color.h"
#include "JDStatusBarNotification.h"

@interface GGRootViewController ()

@end

@implementation GGRootViewController
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [AVAnalytics beginLogPageView:[NSString stringWithFormat:@"%s",class_getName(self.class)]];
}

- (void)dealloc
{
  //  NSLog(@"%@释放",[NSString stringWithFormat:@"%s",class_getName(self.class)]);
}

- (void)analyticsEventStatistics:(NSString *)event
{
    [AVAnalytics event:event];
}



- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [AVAnalytics endLogPageView:[NSString stringWithFormat:@"%s",class_getName(self.class)]];
}

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
    self.view.backgroundColor = [UIColor whiteColor];;
    self.rt_disableInteractivePop = NO;
    
    [self.navigationController.navigationBar setBackgroundImage:[UIImage imageWithColor:[UIColor whiteColor]] forBarMetrics:UIBarMetricsDefault];
    [self.navigationController.navigationBar setShadowImage:[UIImage new]];
    
    [self.navigationController.navigationBar setBarTintColor:[UIColor whiteColor]];
    [self.navigationController.navigationBar setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:GGTitle_Color,NSForegroundColorAttributeName,nil]];
    
   
}



- (UIStatusBarStyle)preferredStatusBarStyle
{
        return UIStatusBarStyleDefault;
}


- (void)initNavView
{
    self.ggNavView = [[GGCustomNavView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 60)];
    
    [self.view addSubview:self.ggNavView];
}

- (void)showNormalGoAnwserAlert
{
    //警告不允许,必须答题
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"你需要完成此游戏答题才可以进行组队" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"立即答题" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        //textFields是一个数组，获取所输入的字符串
        [self goNormalAnwser];
    }];
    UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    [alert addAction:action1];
    [alert addAction:action2];
    [self presentViewController:alert animated:YES completion:nil];
}


- (void)goNormalAnwser
{
        GGQAViewController *qa = [[GGQAViewController alloc]init];
        qa.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:qa animated:YES];
    
}



- (void)shareWithShareDict:(GGShareModel *)model
{
    NSArray* imageArray = @[[UIImage imageNamed:@"shareImg.png"]];
    // （注意：图片必须要在Xcode左边目录里面，名称必须要传正确，如果要分享网络图片，可以这样传image参数 images:@[@"http://mob.com/Assets/images/logo.png?v=20150320"]）
    if (imageArray)
    {
        NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
        [shareParams SSDKSetupShareParamsByText:@"分享内容"
                                         images:imageArray
                                            url:[NSURL URLWithString:@"http://mob.com"]
                                          title:@"分享标题"
                                           type:SSDKContentTypeAuto];
        //有的平台要客户端分享需要加此方法，例如微博
        [shareParams SSDKEnableUseClientShare];
        //2、分享（可以弹出我们的分享菜单和编辑界面）
        [ShareSDK showShareActionSheet:nil
                           customItems:nil shareParams:shareParams sheetConfiguration:nil onStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
                               switch (state)
                               {
                                   case SSDKResponseStateSuccess:
                                   {
                                       UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"分享成功"
                                                                                           message:nil
                                                                                          delegate:nil
                                                                                 cancelButtonTitle:@"确定"
                                                                                 otherButtonTitles:nil];
                                       [alertView show];
                                       break;
                                   }
                                   case SSDKResponseStateFail:
                                   {
                                       UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"分享失败"
                                                                                       message:[NSString stringWithFormat:@"%@",error]
                                                                                      delegate:nil
                                                                             cancelButtonTitle:@"OK"
                                                                             otherButtonTitles:nil, nil];
                                       [alert show];
                                       break;
                                   }
                                   default:
                                       break;
                               }
                           }];
        
    }
    
    
}


- (void)statusBarshow
{
    
    [JDStatusBarNotification addStyleNamed:@"test"
                                   prepare:^JDStatusBarStyle*(JDStatusBarStyle *style) {
                                       style.barColor = Main_Color;
                                       style.textColor = [UIColor whiteColor];
                                       style.font = [UIFont systemFontOfSize:14];
                                       style.heightForIPhoneX = JDStatusBarHeightForIPhoneXHalf;
                                       return style;
                                   }];
    [JDStatusBarNotification showWithStatus:@"正在跟我一起玩.点击回到" styleName:@"test"];
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
