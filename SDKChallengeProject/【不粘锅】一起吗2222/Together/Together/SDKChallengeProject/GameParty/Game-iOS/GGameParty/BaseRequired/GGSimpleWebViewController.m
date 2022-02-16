//
//  GGSimpleWebViewController.m
//  GGameParty
//
//  Created by Victor on 2018/9/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSimpleWebViewController.h"
#import <WebKit/WebKit.h>

@interface GGSimpleWebViewController ()<WKNavigationDelegate,WKUIDelegate>
@property (nonatomic, strong) WKWebView *webView;

@property (weak, nonatomic) CALayer *progresslayer;


@end

@implementation GGSimpleWebViewController
//
//- (void)shareTo
//{
//    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
//    [shareParams SSDKSetupShareParamsByText:self.title.length==0?@"分享链接":self.title
//                                     images:[UIImage imageNamed:@"ggshare_icon"]
//                                        url:self.URL
//                                      title:self.title.length==0?@"分享链接":self.title
//                                       type:SSDKContentTypeAuto];
//    [shareParams SSDKEnableUseClientShare];
//    SSUIShareSheetConfiguration *config = [SSUIShareSheetConfiguration new];
//    config.style = SSUIActionSheetStyleSimple;
//    config.cancelButtonHidden = NO;
//    [ShareSDK showShareActionSheet:nil customItems:@[@(SSDKPlatformSubTypeWechatTimeline),@(SSDKPlatformSubTypeWechatSession),@(SSDKPlatformSubTypeQQFriend),@(SSDKPlatformSubTypeQZone)] shareParams:shareParams sheetConfiguration:config onStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
//
//    }];
//}
//
//- (void)viewDidLoad
//{
//    [super viewDidLoad];
//
//    self.navigationController.navigationBarHidden = NO;
//
//    UIBarButtonItem *right = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"icon_webShare"] style:UIBarButtonItemStyleDone target:self action:@selector(shareTo)];
//    self.navigationItem.rightBarButtonItem = right;
//
//    //
//    NSString *url = self.URL.absoluteString;
//    NSString *accessToken = [AVUser currentUser].sessionToken;
//    NSString *newUrl = [NSString stringWithFormat:@"%@%@token=%@",url,[url hasSuffix:@"?"]?@"":@"?",accessToken];
//    NSURL *newURL = [NSURL URLWithString:newUrl];
//    NSLog(@"url >>>>>>>>>>>> %@",newUrl);
//    self.webView = [[WKWebView alloc] initWithFrame:CGRectMake(0, 0, kScreenW, kScreenH - 64)];
//    [self.webView loadRequest:[NSURLRequest requestWithURL:newURL]];
//    self.webView.navigationDelegate = self;
//    self.webView.UIDelegate = self;
//    //开了支持滑动返回
//    self.webView.allowsBackForwardNavigationGestures = YES;
//    [self.view addSubview:self.webView];
//
//    UIView *progress = [[UIView alloc]initWithFrame:CGRectMake(0, 0, CGRectGetWidth(self.view.frame), 3)]; progress.backgroundColor = [UIColor clearColor]; [self.view addSubview:progress];
//    CALayer *layer = [CALayer layer]; layer.frame = CGRectMake(0, 0, 0, 3); layer.backgroundColor = Main_Color.CGColor; [progress.layer addSublayer:layer]; self.progresslayer = layer;
//
//    [self.webView addObserver:self forKeyPath:@"estimatedProgress" options:NSKeyValueObservingOptionNew context:nil];
//
//}
//
//- (void)dealloc{ [(WKWebView *)self.webView removeObserver:self forKeyPath:@"estimatedProgress"]; }
//
//- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSString *,id> *)change context:(void *)context{ if ([keyPath isEqualToString:@"estimatedProgress"]) { self.progresslayer.opacity = 1; if ([change[@"new"] floatValue] < [change[@"old"] floatValue]) { return; } self.progresslayer.frame = CGRectMake(0, 0, self.view.bounds.size.width * [change[@"new"] floatValue], 3); if ([change[@"new"] floatValue] == 1) { dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.4 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{ self.progresslayer.opacity = 0; }); dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{ self.progresslayer.frame = CGRectMake(0, 0, 0, 3); }); } }else{ [super observeValueForKeyPath:keyPath ofObject:object change:change context:context]; }
//
//}
//
//
//// 页面开始加载时调用
//-(void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation{
//
//}
//// 当内容开始返回时调用
//- (void)webView:(WKWebView *)webView didCommitNavigation:(WKNavigation *)navigation{
//
//}
//// 页面加载完成之后调用
//- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation{//这里修改导航栏的标题，动态改变
//    self.title = webView.title;
//}
//// 页面加载失败时调用
//- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation
//{
//
//}
//// 接收到服务器跳转请求之后再执行
//- (void)webView:(WKWebView *)webView didReceiveServerRedirectForProvisionalNavigation:(WKNavigation *)navigation{
//
//}
//// 在收到响应后，决定是否跳转
//- (void)webView:(WKWebView *)webView decidePolicyForNavigationResponse:(WKNavigationResponse *)navigationResponse decisionHandler:(void (^)(WKNavigationResponsePolicy))decisionHandler{
//
//    WKNavigationResponsePolicy actionPolicy = WKNavigationResponsePolicyAllow;
//    //这句是必须加上的，不然会异常
//    decisionHandler(actionPolicy);
//
//}
//// 在发送请求之前，决定是否跳转
//- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler{
//
//    self.title = webView.title;
//
//    WKNavigationActionPolicy actionPolicy = WKNavigationActionPolicyAllow;
//
//
//    if (navigationAction.navigationType==WKNavigationTypeBackForward) {//判断是返回类型
//
//        //同时设置返回按钮和关闭按钮为导航栏左边的按钮 这里可以监听左滑返回事件，仿微信添加关闭按钮。
//        //   self.navigationItem.leftBarButtonItems = @[self.backBtn, self.closeBtn];
//        //可以在这里找到指定的历史页面做跳转
//        //        if (webView.backForwardList.backList.count>0) {                                  //得到栈里面的list
//        //            DLog(@"%@",webView.backForwardList.backList);
//        //            DLog(@"%@",webView.backForwardList.currentItem);
//        //            WKBackForwardListItem * item = webView.backForwardList.currentItem;          //得到现在加载的list
//        //            for (WKBackForwardListItem * backItem in webView.backForwardList.backList) { //循环遍历，得到你想退出到
//        //                //添加判断条件
//        //                [webView goToBackForwardListItem:[webView.backForwardList.backList firstObject]];
//        //            }
//        //        }
//    }
//    //这句是必须加上的，不然会异常
//    decisionHandler(actionPolicy);
//}
//
//
//
//- (void)didReceiveMemoryWarning {
//    [super didReceiveMemoryWarning];
//    // Dispose of any resources that can be recreated.
//}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end

