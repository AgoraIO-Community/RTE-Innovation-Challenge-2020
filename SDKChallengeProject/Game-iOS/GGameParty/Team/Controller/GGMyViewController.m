//
//  GGMyViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGMyViewController.h"
#import <CoreLocation/CoreLocation.h>
#import "GGSendMessageView.h"
#import "GGChangeDataViewController.h"
#import "GGHistoryTeamerViewController.h"
#import "GGQAViewController.h"
#import "GGActivityView.h"
#import "UserFeedBackViewController.h"
#import "AYCheckManager.h"
#import "GGAccooutSafeViewController.h"
#import "GGInviteFViewController.h"
#import "GGShareAppView.h"
#import <SafariServices/SafariServices.h>
#import "GGVIPViewController.h"
@interface GGMyViewController ()<UITableViewDelegate,UITableViewDataSource,
CLLocationManagerDelegate>
{
    CLLocationManager *locationmanager;//定位服务
    NSString *currentCity;
    CLLocation *_currentLocation;
    BOOL _haveActivity;
}
@property (nonatomic,strong)zhPopupController *zh_popupController;

@property (nonatomic,strong)UITableView *tableView;

@property (nonatomic,strong)UIImageView *headImageView;

@property (nonatomic,strong)UIImageView *sexImageView;

@property (nonatomic,strong)UILabel *ageLabel;

@property (nonatomic,strong)UILabel *nameLabel;

@property (nonatomic,strong)UILabel *tuanduiLabel;

@property (nonatomic,strong)UILabel *zuijiaLabel;

@property (nonatomic,strong)UILabel *taiduLabel;

@property (nonatomic,strong)UILabel *leyuLabel;

@property (nonatomic,strong)UILabel *steamIDLabel;

@property (nonatomic,strong)NSString *inviteId;

@end

@implementation GGMyViewController


- (void)testCreateUserInfo
{
    AVUser *user = [AVUser currentUser];
    user.username = @"修改的昵称";
    [user setObject:[NSNumber numberWithInt:25] forKey:@"age"];
    [user setObject:@(YES) forKey:@"sex"];
    [user setObject:@"沧海两笑" forKey:@"steamID"];
    NSArray *arr = @[@{@"tuandui":@"123"},
                     @{@"zuijia":@"123"},
                     @{@"taidu":@"99"},
                     @{@"leyu":@"3l"}];
    [user setObject:arr forKey:@"userGoodData"];
    [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [self loadData];
    }];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self loadData];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = YES;
    self.view.backgroundColor = [UIColor whiteColor];
    UIView *top = [[UIView alloc]initWithFrame:CGRectMake(0, -40, kScreenW, 20)];
    top.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:top];
    
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, kScreenH - 49) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [self.view addSubview:self.tableView];
    self.tableView.backgroundColor = GGBackGround_Color;
    [self.tableView setSeparatorColor:GGLine_Color];


    UIView *headerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 315)];
    headerView.backgroundColor = [UIColor whiteColor];
    self.tableView.tableHeaderView = headerView;
    headerView.userInteractionEnabled = YES;
    
    UIImageView *image = [[UIImageView alloc]initWithFrame:headerView.frame];
    image.image = [UIImage imageNamed:@"bg_wenli"];
    [headerView addSubview:image];
    
    
    
    //上方
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, 40, kScreenW, 80)];
    [headerView addSubview:topView];
   
    
    UIImageView *headBtn = [SJUIImageViewFactory imageViewWithImageName:@"icon_linshi"];
    headBtn.frame = CGRectMake(20, 0, 80, 80);
    [topView addSubview:headBtn];
    self.headImageView = headBtn;
    headBtn.contentMode = UIViewContentModeScaleToFill;
    
    UIImageView *touxiangkuang = [[UIImageView alloc]initWithFrame:CGRectMake(20, 0, 80, 80)];
    touxiangkuang.image = [UIImage imageNamed:@"icon_touxiangkuang"];
    [topView addSubview:touxiangkuang];
    
    
    UILabel *nameLabel = [SJUILabelFactory labelWithText:@"飞鸟新人" textColor:GGTitle_Color font:[UIFont boldSystemFontOfSize:23]];
    nameLabel.frame = CGRectMake(CGRectGetMaxX(headBtn.frame)+10, 9.5, kScreenW - CGRectGetMaxX(headBtn.frame) - 10, 35);
    [topView addSubview:nameLabel];
    self.nameLabel = nameLabel;
    
    UIImageView *sex = [SJUIImageViewFactory imageViewWithImageName:@"icon_nv"];
    sex.frame = CGRectMake(nameLabel.frame.origin.x, CGRectGetMaxY(nameLabel.frame) + 10, 13, 13);
    [topView addSubview:sex];
    self.sexImageView = sex;
    
    UILabel *ageLabel = [SJUILabelFactory labelWithText:@"22岁 · 六安市" textColor:GGTitle_Color font:[UIFont systemFontOfSize:12]];
    ageLabel.frame = CGRectMake(CGRectGetMaxX(sex.frame)+2, sex.frame.origin.y - 2, kScreenW - CGRectGetMaxX(sex.frame) - 2, 16);
    [topView addSubview:ageLabel];
    self.ageLabel = ageLabel;
    
    
    //中间
    CGFloat ww = (kScreenW - 60)/4;
    UIView *middelView = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(topView.frame) + 20, kScreenW, ww)];
    [headerView addSubview:middelView];
   // NSArray *arr = @[@"团队主力",@"最佳队友",@"态度友好",@"乐于助人"];
    NSArray *numArr = @[@"15",@"239",@"3k+",@"999"];
    NSArray *imageArr = @[@"icon_zhuli",@"icon_zuijia",@"icon_youhao",@"icon_zhuren"];
    
    
    for(int i = 0;i < 4; i++)
    {
        
        UIImageView *zanBack = [[UIImageView alloc]initWithFrame:CGRectMake(15 +  ww * i + 10 *i,0, ww, ww)];
        zanBack.image = [UIImage imageNamed:imageArr[i]];
        [middelView addSubview:zanBack];
        
        UILabel *numLabel = [SJUILabelFactory labelWithText:numArr[i] textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:18]];
        numLabel.frame = CGRectMake(zanBack.frame.origin.x, 12, ww, ww * 0.3);
        numLabel.textAlignment = NSTextAlignmentCenter;
        [middelView addSubview:numLabel];
        if (i == 0)
        {
            self.tuanduiLabel = numLabel;
        }
        if (i == 1)
        {
            self.zuijiaLabel = numLabel;
        }
        if (i == 2)
        {
            self.taiduLabel = numLabel;
        }
        if (i == 3)
        {
            self.leyuLabel = numLabel;
        }
    }
  
    /*
    //下方
    UIView *bottomView = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(middelView.frame) + 20, kScreenW, 60)];
    [headerView addSubview:bottomView];
    
    
    UIImageView *steamImage = [SJUIImageViewFactory imageViewWithImageName:@"bg_steam"];
    steamImage.frame = CGRectMake(15, 0, kScreenW - 30, 60);
    steamImage.contentMode = UIViewContentModeScaleToFill;
    [bottomView addSubview:steamImage];
    
    UILabel *setSteam = [SJUILabelFactory labelWithText:@"设置我的SteamID" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:14]];
    setSteam.frame = CGRectMake(15, 0, 120, 60);
    [steamImage addSubview:setSteam];
    
    UILabel *steamID = [SJUILabelFactory labelWithText:@"沧海一笑" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:14]];
    steamID.frame = CGRectMake(CGRectGetMaxX(setSteam.frame), 0, steamImage.frame.size.width - CGRectGetMaxX(setSteam.frame) - 28, 60);
    steamID.textAlignment = NSTextAlignmentRight;
    [steamImage addSubview:steamID];
    self.steamIDLabel = steamID;
    
    
    UIImageView *arrowImage = [SJUIImageViewFactory imageViewWithImageName:@"icon_qianj"];
    arrowImage.frame = CGRectMake(steamImage.frame.size.width - 23, 23.5, 8, 13);
    [steamImage addSubview:arrowImage];
     
     UITapGestureRecognizer *bottomGes = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(changeSteamID)];
     [bottomView addGestureRecognizer:bottomGes];
    */
    //
    CGFloat headerViewHeight = CGRectGetMaxY(middelView.frame) + 15;
    headerView.frame = CGRectMake(0, 0, kScreenW, headerViewHeight);
    
    
    UITapGestureRecognizer *topGes = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(changeUserData)];
    [topView addGestureRecognizer:topGes];
    
    

   // [self testCreateUserInfo];
    
    [self loadData];
    
//    [GGNotificationCenter addObserver:self selector:@selector(HAVE_ACTIVITY) name:@"HAVE_ACTIVITY" object:nil];
    
}

- (void)HAVE_ACTIVITY
{
//    _haveActivity = YES;
    [self.tableView reloadData];
}


- (void)loadData
{
   
    
    AVUser *user = [AVUser currentUser];
    NSString *username = user.username.length == 0?@"飞鸟小白":[AVUser currentUser].username;
    NSNumber *age = [user objectForKey:@"age"];
    BOOL sex = [[user objectForKey:@"sex"] boolValue];//1是男
    NSString *steamID = [user objectForKey:@"steamID"];
    NSString *avatarUrl = [[user objectForKey:@"avatar"]objectForKey:@"url"];
    
    self.nameLabel.text = username;
    [self.headImageView setImageWithURL:[NSURL URLWithString:avatarUrl] placeholderImage:GGDefault_User_Head];
   
    if (sex)
    {
        [self.sexImageView setImage:[UIImage imageNamed:@"icon_nan"]];
    }
    else
    {
        [self.sexImageView setImage:[UIImage imageNamed:@"icon_nv"]];
    }
    
    AVQuery *query = [AVQuery queryWithClassName:DB_User_Extra];
    [query whereKey:@"user" equalTo:[AVUser currentUser].objectId];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            NSArray* goodArr;
            if (objects.count > 0)
            {
                AVObject *obj = objects[0];
                goodArr = @[[obj objectForKey:@"tuandui"],
                            [obj objectForKey:@"zuijia"],
                            [obj objectForKey:@"taidu"],
                            [obj objectForKey:@"leyu"]];
            }
            else
            {
                goodArr = @[[NSNumber numberWithInteger:0],
                            [NSNumber numberWithInteger:0],
                            [NSNumber numberWithInteger:0],
                            [NSNumber numberWithInteger:0]];
            }
            self.tuanduiLabel.text = [NSString stringWithFormat:@"%@",goodArr[0]];
            self.zuijiaLabel.text = [NSString stringWithFormat:@"%@",goodArr[1]];
            self.taiduLabel.text = [NSString stringWithFormat:@"%@",goodArr[2]];
            self.leyuLabel.text = [NSString stringWithFormat:@"%@",goodArr[3]];
        }
    }];
    
    
    self.ageLabel.text = [NSString stringWithFormat:@"%@岁",age];
    self.steamIDLabel.text = steamID.length == 0?@"点击填写":steamID;
    
    
    if ([CLLocationManager locationServicesEnabled])
    {
        locationmanager = [[CLLocationManager alloc]init];
        locationmanager.delegate = self;
        [locationmanager requestAlwaysAuthorization];
        currentCity = [NSString new];
        [locationmanager requestWhenInUseAuthorization];
        
        //设置寻址精度
        locationmanager.desiredAccuracy = kCLLocationAccuracyBest;
        locationmanager.distanceFilter = 5.0;
        [locationmanager startUpdatingLocation];
    }
}

#pragma mark - location.delegate
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations
{
    [locationmanager stopUpdatingHeading];
    CLLocation *currentLocation = [locations lastObject];
    CLGeocoder *geoCoder = [[CLGeocoder alloc]init];
    //打印当前的经度与纬度
    NSLog(@"%f,%f",currentLocation.coordinate.latitude,currentLocation.coordinate.longitude);
    _currentLocation = currentLocation;
    
    //反地理编码
    [geoCoder reverseGeocodeLocation:currentLocation completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        if (placemarks.count > 0)
        {
            CLPlacemark *placeMark = placemarks[0];
            currentCity = placeMark.locality;
            if (currentCity)
            {
                self.ageLabel.text = [NSString stringWithFormat:@"%@岁 · %@", [[AVUser currentUser]objectForKey:@"age"],currentCity];
                AVGeoPoint *point = [AVGeoPoint geoPointWithLocation:_currentLocation];
                [[AVUser currentUser]setObject:point forKey:@"lastLocation"];
                [[AVUser currentUser]setObject:currentCity forKey:@"lastAddress"];
                [[AVUser currentUser]saveInBackground];
            }
        }
    }];
    
}


- (void)changeSteamID
{
    NSLog(@"修改ID");
    CGRect rect = CGRectMake(0, 0, kScreenW, 60);
    AVUser *user = [AVUser currentUser];
   NSString *steamID =  [user objectForKey:@"steamID"];
    GGSendMessageView *sendMessageTool = [[GGSendMessageView alloc] initWithFrame:rect];
    sendMessageTool.textField.text = steamID;
   // __block GGMyViewController *  blockSelf = self;
    
    sendMessageTool.senderClickedBlock = ^(GGSendMessageView *messageView, UIButton *button) {
        [self.zh_popupController dismiss];
        if (![messageView.textField.text isEqualToString:steamID])
        {
            [user setObject:messageView.textField.text forKey:@"steamID"];
            [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                self.steamIDLabel.text = [user objectForKey:@"steamID"];
            }];
        }
      
    };
    self.zh_popupController = [zhPopupController new];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    [self.zh_popupController presentContentView:sendMessageTool duration:0.25 springAnimated:NO];
    
}

- (void)changeUserData
{
    GGChangeDataViewController *change = [[GGChangeDataViewController alloc]init];
    change.hidesBottomBarWhenPushed = YES;
    [self.rt_navigationController pushViewController:change animated:YES complete:^(BOOL finished) {
        
    }];
}

#pragma mark - table.Delegate

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [UIView new];
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [UIView new];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    CGFloat hh = (kScreenW - 45)/3;
//    if (_haveActivity) {
//        return indexPath.section == 0?hh+30:60;
//    }
    return indexPath.section == 0?0.1:60;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 0;
    }
    return section == 1?3:3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
#warning apple不允许有检查更新这个东西
    static NSString *iden = @"dasdsa";//@"订阅飞鸟服务", @"icon_vip",
    NSArray *arr = @[@[@"我的关注",@"邀请好友",@"给飞鸟好评"],
                     @[@"账号与安全",@"意见反馈",@"隐私政策"]];
    NSArray *imageArr = @[@[@"icon_guanzhu",@"icon_yaoqing",@"icon_renwu"],//icon_datimy
                          @[@"icoin_anquan",@"icon_geren",@"icon_shengji"]];
    NSArray *descArr = @[@[@"更多特权",@"随时查看好友组队动态",@"",[[AVUser currentUser] objectForKey:@"gameCode"]?@"":@"好评赢游戏"],
                          @[@"",@"",@"",@"当前版本v1.1"]];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:iden];
    }
    if (indexPath.section > 0) {
        cell.imageView.image = [UIImage imageNamed:imageArr[indexPath.section - 1][indexPath.row]];
        cell.textLabel.text = arr[indexPath.section - 1][indexPath.row];
        cell.detailTextLabel.text = descArr[indexPath.section - 1][indexPath.row];
        cell.detailTextLabel.font = [UIFont systemFontOfSize:12];
        cell.detailTextLabel.numberOfLines = 0;
        cell.accessoryType =  UITableViewCellAccessoryDisclosureIndicator;
        
    }
    if (indexPath.section == 0) {
        for(UIView *temp in cell.contentView.subviews)
        {
            [temp removeFromSuperview];
        }
       
        CGFloat hh = (kScreenW - 45)/3;
        GGActivityView *view = [[GGActivityView alloc]initWithFrame:CGRectZero];
        [cell.contentView addSubview:view];
        view.clickActivityBlock = ^(GGActivityView *editView, NSString *url) {
//            GGSimpleWebViewController *webVC = [[GGSimpleWebViewController alloc]init];
//            webVC.URL = [NSURL URLWithString:url];
//            webVC.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:webVC animated:YES];
        };
        
        if (_haveActivity) {
            view.frame = CGRectMake(0, 0, kScreenW, hh+30);
        }
    }
    if (indexPath.section == 1 && indexPath.row == 3) {
        cell.detailTextLabel.textColor = [UIColor redColor];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 1)
    {
//        if (indexPath.row == 0)
//        {
//            GGVIPViewController *view = [[GGVIPViewController alloc]init];
//            view.hidesBottomBarWhenPushed = YES;
//            view.title = @"我的飞鸟";
//            [self.navigationController pushViewController: view animated:YES];
//        }
        
        if (indexPath.row == 0)
        {
            GGHistoryTeamerViewController *view = [[GGHistoryTeamerViewController alloc]init];
            view.hidesBottomBarWhenPushed = YES;
            view.title = @"我的关注";
            view.isFollowView = YES;
            [self.navigationController pushViewController: view animated:YES];
        }
//        if (indexPath.row == 1)
//        {
//            GGQAViewController *qa = [[GGQAViewController alloc]init];
//            qa.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController: qa animated:YES];
//        }
        if (indexPath.row == 1) {
            [self shareAPP];
        }
        if (indexPath.row == 2) {
            GGInviteFViewController *invite = [[GGInviteFViewController alloc]init];
            invite.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:invite animated:YES];
        }
    }
    if (indexPath.section == 2) {
        if (indexPath.row == 0) {
            GGAccooutSafeViewController *feed = [[GGAccooutSafeViewController alloc]init];
            feed.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:feed animated:YES];
        }
        if (indexPath.row == 1) {
            UserFeedBackViewController *feed = [[UserFeedBackViewController alloc]init];
            feed.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:feed animated:YES];
        }
        if (indexPath.row == 2) {
//            AYCheckManager *checkManger = [AYCheckManager sharedCheckManager];
//            [checkManger checkVersion];
            SFSafariViewController *safariVC = [[SFSafariViewController alloc] initWithURL:[NSURL URLWithString:@"http://www.guyuyin.com/privacypolicy-yuyin.html"] entersReaderIfAvailable:YES];
            [self presentViewController:safariVC animated:YES completion:nil];
            
        }
    }
}

- (void)shareAPP
{
    
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 160);
    
    GGShareAppView *view = [[GGShareAppView alloc]initWithFrame:rect];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    view.closeBtnClick = ^(GGPopupView *view) {
        [self.zh_popupController dismiss];
    };
    
    view.sharePlatFormClick = ^(GGShareAppView *view, NSInteger index) {
        [self.zh_popupController dismiss];
        switch (index)
        {
            case 0:
                [self sharewithPlatform:SSDKPlatformTypeQQ];
                break;
            case 1:
                [self sharewithPlatform:SSDKPlatformSubTypeWechatSession];
                break;
            case 2:
                [self sharewithPlatform:SSDKPlatformSubTypeQQFriend];
                break;
            case 3:
                [self sharewithPlatform:SSDKPlatformSubTypeWechatTimeline];
                break;
            default:
                break;
        }
    };
}

- (void)sharewithPlatform:(SSDKPlatformType)platform
{
    
    AVUser *user = [AVUser currentUser];
    NSString *inviteId = [NSString stringWithFormat:@"%@",[user objectForKey:@"userId"]];
    
    
    //    [GGAppTool getMobId:@{@"inviteId":inviteId} path:nil source:user.objectId result:^(NSString *mobid) {
    //        if (mobid) {
    // NSString *shareUrl = [NSString stringWithFormat:@"%@/a/invite?userid=%@",GGHOST_URL,inviteId];//分享出去的链接
    NSString *shareUrl = APPURL;
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    [shareParams SSDKSetupShareParamsByText:@"steam玩家专属语音软件,无需下载直接用"
                                     images:[UIImage imageNamed:@"ggshare_icon"]
                                        url:[NSURL URLWithString:shareUrl]
                                      title:@"兄dei,快来搞事情,点开领一个游戏"
                                       type:SSDKContentTypeAuto];
    
    [shareParams SSDKEnableUseClientShare];
    SSUIShareSheetConfiguration *config = [SSUIShareSheetConfiguration new];
    config.style = SSUIActionSheetStyleSimple;
    config.cancelButtonHidden = NO;
    
    [ShareSDK share:platform parameters:shareParams onStateChanged:^(SSDKResponseState state, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error) {
        
    }];
//
//    [ShareSDK showShareActionSheet:nil customItems:@[@(SSDKPlatformSubTypeWechatTimeline),@(SSDKPlatformSubTypeWechatSession),@(SSDKPlatformSubTypeQQFriend),@(SSDKPlatformSubTypeQZone)] shareParams:shareParams sheetConfiguration:config onStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
//
//    }];
    //        }
    //        else
    //        {
    //            NSLog(@"邀请id获取失败");
    //        }
    //    }];
}

@end
