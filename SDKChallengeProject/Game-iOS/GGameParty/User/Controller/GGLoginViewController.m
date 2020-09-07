//
//  GGLoginViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGLoginViewController.h"
#import "GGLoginView.h"
#import "STLVideoViewController.h"
//#import "AVUser+SNS.h"
//#import "AVOSCloudSNS.h"

#import <AVFoundation/AVFoundation.h>

@interface GGLoginViewController ()

@property (nonatomic,strong)AVPlayer *player;//播放器对象
@property (nonatomic,strong)AVPlayerItem *currentPlayerItem;


@property (nonatomic,strong)UIButton *wechatLoginBtn;



@property (nonatomic,strong)UIButton *phoneLoginBtn;
@property (nonatomic,strong)zhPopupController *zh_popupController;

@property (nonatomic, strong) GGLoginView *loginView;




@end

@implementation GGLoginViewController



- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    UIImageView *imageView = [SJUIImageViewFactory imageViewWithImageName:@"login_bigBack"];
    imageView.frame = CGRectMake(0, 0, kScreenW, kScreenH);
    [self.view addSubview:imageView];
    [self.view bringSubviewToFront:imageView];
    [self setVideoPlayer];
  
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self.view sendSubviewToBack: imageView];
    });
    
    //icon_weixinshare icon_weixin
    self.wechatLoginBtn = [SJUIButtonFactory buttonWithImageName:@"icon_weixinlogin" target:self sel:@selector(wechatLogin) tag:1];
    [self.wechatLoginBtn setTitle:@"微信登录" forState:UIControlStateNormal];
    self.wechatLoginBtn.frame = CGRectMake(37.5, kScreenH - 114 - 10, kScreenW - 75, 45);
    [self.view addSubview:self.wechatLoginBtn];
    self.wechatLoginBtn.layer.masksToBounds = YES;
    self.wechatLoginBtn.layer.cornerRadius = 23;
    self.wechatLoginBtn.backgroundColor = Main_Color;
    self.wechatLoginBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [self.wechatLoginBtn setImageEdgeInsets:UIEdgeInsetsMake(0.0, -20, 0.0, 0.0)];
    
    UIView *backView = [[UIView alloc]initWithFrame:CGRectMake(37.5, kScreenH - 40 - 30, kScreenW - 75, 45)];
    backView.backgroundColor = [UIColor blackColor];
    backView.alpha = 0.3;
    [self.view addSubview:backView];
    backView.layer.masksToBounds = YES;
    backView.layer.cornerRadius = 23;
    
    self.phoneLoginBtn = [SJUIButtonFactory buttonWithTitle:@"手机号码登录" titleColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:16] backgroundColor:[UIColor clearColor] target:self sel:@selector(phoneLogin) tag:1];
    self.phoneLoginBtn.frame = CGRectMake(37.5, kScreenH - 40 - 30, kScreenW - 75, 45);
    [self.view addSubview:self.phoneLoginBtn];
    
    
    if (![[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"weixin://"]]){
        self.wechatLoginBtn.hidden = YES;
        self.phoneLoginBtn.frame = CGRectMake(37.5, kScreenH - 114 - 10, kScreenW - 75, 45);
        self.phoneLoginBtn.backgroundColor = Main_Color;
        self.phoneLoginBtn.layer.cornerRadius = 23;
        self.phoneLoginBtn.layer.masksToBounds = YES;
    }
}
- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary *)change
                       context:(void *)context {
    AVPlayerItem *playerItem = (AVPlayerItem *)object;
    if ([keyPath isEqualToString:@"status"]) {
        //获取playerItem的status属性最新的状态
        AVPlayerStatus status = [[change objectForKey:@"new"] intValue];
        switch (status) {
            case AVPlayerStatusReadyToPlay:{
                //获取视频长度
                //更新显示:视频总时长(自定义方法显示时间的格式)
                //开始播放视频
                [self.player play];
                break;
            }
            case AVPlayerStatusFailed:{//视频加载失败，点击重新加载
              
                break;
            }
            case AVPlayerStatusUnknown:{
                NSLog(@"加载遇到未知问题:AVPlayerStatusUnknown");
                break;
            }
            default:
                break;
        }
    } else if ([keyPath isEqualToString:@"loadedTimeRanges"]) {
        //获取视频缓冲进度数组，这些缓冲的数组可能不是连续的
        NSArray *loadedTimeRanges = playerItem.loadedTimeRanges;
        //获取最新的缓冲区间
        CMTimeRange timeRange = [loadedTimeRanges.firstObject CMTimeRangeValue];
        //缓冲区间的开始的时间
        NSTimeInterval loadStartSeconds = CMTimeGetSeconds(timeRange.start);
        //缓冲区间的时长
        NSTimeInterval loadDurationSeconds = CMTimeGetSeconds(timeRange.duration);
        //当前视频缓冲时间总长度
        NSTimeInterval currentLoadTotalTime = loadStartSeconds + loadDurationSeconds;
        //NSLog(@"开始缓冲:%f,缓冲时长:%f,总时间:%f", loadStartSeconds, loadDurationSeconds, currentLoadTotalTime);
        //更新显示：当前缓冲总时长
    }
}

- (void)wechatLoginnn
{
//    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
//    [AVOSCloudSNS loginWithCallback:^(id object, NSError *error) {
//        [MBProgressHUD hideHUDForView:self.view animated:YES];
//        if (error) {
//            
//        } else {
//            NSString *accessToken = object[@"access_token"];
//            NSString *username = object[@"username"];
//            NSString *avatar = object[@"avatar"];
//            NSDictionary *rawUser = object[@"raw-user"]; // 性别等第三方平台返回的用户信息
//            [object setObject:[rawUser objectForKey:@"unionid"] forKey:@"id"];
//            [MBProgressHUD showHUDAddedTo:self.view animated:YES];
//            [AVUser loginWithAuthData:object platform:AVOSCloudSNSPlatformWeiXin block:^(AVUser *userer, NSError *error) {
//                if (!error)
//                {//如果用户不是第一次使用微信登录,那么就不要修改头像等信息了.第一次使用时username和openId是
//                    NSString *ipadd =  [GGAppTool getCurrentIpAddress];
//                    if (ipadd) {
//                        [userer setObject:ipadd forKey:@"ipAddress"];
//                        [userer saveEventually];
//                    }
//                    
//                    if (userer.username.length > 20) {
//                        userer.username = username;
//                        AVFile *file = [AVFile fileWithRemoteURL:[NSURL URLWithString:avatar]];
//                        [userer setObject:file forKey:@"avatar"];
//                        [userer setObject:[[rawUser objectForKey:@"sex"] integerValue] == 0?@(YES):@(NO)forKey:@"sex"];
//                        [userer saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
//                            if (error.code == 202) {
//                                //用户名被占用
//                                NSString *str = [NSString stringWithFormat:@"%@%u",username,arc4random() % 101];
//                                userer.username = str;
//                                [userer saveInBackground];
//                            }
//                        }];
//                    }
//                    [MBProgressHUD hideHUDForView:self.view animated:YES];
//                     [[NSNotificationCenter defaultCenter]postNotificationName:LOGIN_SUCCESSED_NOTIFATION object:nil];
//                }
//                else
//                {
//                    [MBProgressHUD hideHUDForView:self.view animated:YES];
//                    [XHToast showBottomWithText:@"发生错误,请重试"];
//                }
//            }];
//            
//        }
//    } toPlatform:AVOSCloudSNSWeiXin];
}

- (void)wechatLogin
{
    [ShareSDK getUserInfo:SSDKPlatformTypeWechat
           onStateChanged:^(SSDKResponseState state, SSDKUser *user, NSError *error)
     {
         if (state == SSDKResponseStateSuccess)
         {             
             NSNumber * platform = [NSNumber numberWithInteger:3];
             NSString * access_token = user.credential.token;
             NSString * uid = user.uid;
             NSString *username = user.nickname;
             NSDictionary *dic = @{@"platform":platform,
                                   @"access_token":access_token,
                                   @"openid":uid,
                                   @"expires_at":[NSDate date],
                                   @"id":uid
                                   };
             NSString *avatar = user.icon;
             
             NSDictionary *thirdPartyData = @{
             // 必须
             @"openid":uid,
             @"access_token":access_token,
             @"expires_in":@7200,
             };

             AVUser *user = [AVUser user];
             AVUserAuthDataLoginOption *option = [AVUserAuthDataLoginOption new];
             option.platform = LeanCloudSocialPlatformWeiXin;
             [user loginWithAuthData:thirdPartyData platformId:LeanCloudSocialPlatformWeiXin options:option callback:^(BOOL succeeded, NSError * _Nullable error) {
                 if (succeeded) {
                     
                     NSLog(@"登录成功");
                           if ([user.username isEqualToString:uid]) {
                                              user.username = username;
                                              AVFile *file = [AVFile fileWithRemoteURL:[NSURL URLWithString:avatar]];
                                              [user setObject:file forKey:@"avatar"];
                                            
                                              [user saveInBackground];
                                          }
                                          else
                                          {
                                              [[NSNotificationCenter defaultCenter]postNotificationName:LOGIN_SUCCESSED_NOTIFATION object:nil];
                                          }
                     
                 }else{
                     NSLog(@"登录失败：%@",error.localizedFailureReason);
                 }
             }];
//
//             [AVUser loginWithAuthData:dic block:^(AVUser * _Nullable userer, NSError * _Nullable error) {
//                 if (!error)
//                 {//如果用户不是第一次使用微信登录,那么就不要修改头像等信息了.第一次使用时username和openId是
//                     if ([userer.username isEqualToString:uid]) {
//                         userer.username = username;
//                         AVFile *file = [AVFile fileWithRemoteURL:[NSURL URLWithString:avatar]];
//                         [userer setObject:file forKey:@"avatar"];
//                         [userer setObject:user.gender == 0?@(YES):@(NO)forKey:@"sex"];
//                         [userer saveInBackground];
//                     }
//                     else
//                     {
//                         [[NSNotificationCenter defaultCenter]postNotificationName:LOGIN_SUCCESSED_NOTIFATION object:nil];
//                     }
//                 }
//             }];

             
         }
         else
         {
             NSLog(@"%@",error);
         }

     }];
}

//{
//    city = "";
//    country = "";
//    headimgurl = "http://thirdwx.qlogo.cn/mmopen/vi_32/VzjUV1Io39xvDTkib7icvHpZaW7UpxuWhSyZ1jcEgyJhlFSqQOQHW046tsumqicnyqG8bOLNZ2vEReJPz8dJIKuGA/132";
//    language = "zh_CN";
//    nickname = "\U51fa\U5dee\U541b";
//    openid = oazTlwTuItKXsVNpwVJOclU7SPSU;
//    privilege =     (
//    );
//    province = "";
//    sex = 0;
//    unionid = ox7NLsyC7RaF0KV2jHI15k52WnhQ;
//}

- (void)phoneLogin
{
    CGRect rect = CGRectMake(15, 0, kScreenW - 30, 250);
    if (!self.loginView) {
        self.loginView = [[GGLoginView alloc]initWithFrame:rect];
    }
    
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
     self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:self.loginView duration:0.25 springAnimated:NO];
    @WeakObj(self);
    self.loginView.loginBtnClick = ^(GGLoginView *loginView, UIButton *button) {
        
    };
    self.loginView.closeBtnClick = ^(GGLoginView *loginView) {
        [selfWeak.zh_popupController dismiss];
    };
}



- (void)setVideoPlayer
{
    
    NSString* localFilePath=[[NSBundle mainBundle]pathForResource:@"BridgeLoop-640p" ofType:@"mp4"];
    NSURL *localVideoUrl = [NSURL fileURLWithPath:localFilePath];
    AVPlayerItem *playerItem = [[AVPlayerItem alloc] initWithURL:localVideoUrl];
    self.currentPlayerItem = playerItem;
    self.player = [[AVPlayer alloc] initWithPlayerItem:playerItem];
    
    AVPlayerLayer *avLayer = [AVPlayerLayer playerLayerWithPlayer:self.player];
    avLayer.videoGravity = AVLayerVideoGravityResizeAspect;
    avLayer.frame = self.view.bounds;
    [self.view.layer addSublayer:avLayer];
    
    [self.player play];

    
    [self.player.currentItem addObserver:self forKeyPath:@"status" options:NSKeyValueObservingOptionNew context:nil];
    //观察loadedTimeRanges，可以获取缓存进度，实现缓冲进度条
    [self.player.currentItem addObserver:self forKeyPath:@"loadedTimeRanges" options:NSKeyValueObservingOptionNew context:nil];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(moviePlayDidEnd:) name:AVPlayerItemDidPlayToEndTimeNotification object:self.player.currentItem];
   
}

- (void)moviePlayDidEnd:(NSNotification*)notification{

    AVPlayerItem*item = [notification object];

    [item seekToTime:kCMTimeZero];

    [self.player play];

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
