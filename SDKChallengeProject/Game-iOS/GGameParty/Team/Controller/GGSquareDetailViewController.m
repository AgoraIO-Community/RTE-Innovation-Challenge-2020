////
////  GGSquareDetailViewController.m
////  GGameParty
////
////  Created by Victor on 2018/7/24.
////  Copyright © 2018年 Victor. All rights reserved.
////
//
//#import "GGSquareDetailViewController.h"
//#import "SJVideoPlayerURLAsset+SJControlAdd.h"
//#import "SJVideoPlayerHelper.h"
//#import "SquareModel.h"
//#import "SJBaseVideoPlayer.h"
//#import "SJLightweightTopItem.h"
//#import "UIViewController+SJVideoPlayerAdd.h"
//#import "SquareCommentModel.h"
//#import "GGCommentTableViewCell.h"
//#import "GGCommentTool.h"
//#import "GGUserInfoViewController.h"
//#import "GGSendMessageView.h"
//#import "GGSendCommentView.h"
//#import "SJBaseVideoPlayer.h"
//#import "SJAttributeWorker.h"
//#import "SJVideoPlayerRegistrar.h"
//#import <ShareSDK/ShareSDK.h>
//#import <ShareSDKUI/ShareSDKUI.h>
//
//@interface GGSquareDetailViewController ()<SJVideoPlayerHelperUseProtocol,UITableViewDelegate,UITableViewDataSource>
//{
//    NSInteger _pageNo;
//}
//@property (nonatomic, strong) UIView *playerSuperView;
//
//@property (nonatomic, strong) SJVideoPlayerHelper *videoPlayerHelper;
//@property (nonatomic, strong) SquareModel *video;
//@property (nonatomic, strong) SJVideoPlayerURLAsset *asset;
//
//@property (nonatomic, strong) SJVideoPlayer *videoPlayer;
//
//@property (nonatomic,strong)NSMutableArray *dataArr;
//@property (nonatomic,strong)UITableView *tableView;
//
//@property (nonatomic,strong)UILabel *titLabel;
//@property (nonatomic,strong)UILabel *descLabel;
//
//@property (nonatomic,strong)UIImageView *headImage;
//@property (nonatomic,strong)UILabel *nameLabel;
//@property (nonatomic,strong)UILabel *timeLabel;
//
////GGCommentTool
//@property (nonatomic,strong)GGCommentTool *commentTool;
//
////GGSendCommentView *sendMessageTool
//@property (nonatomic,strong)GGSendCommentView *sendMessageTool;
//
//@end
//
//@implementation GGSquareDetailViewController
//{
//    SJBaseVideoPlayer *player;
//}
//
//@synthesize playerSuperView = _playerSuperView;
//
//
//- (instancetype)initWithVideo:(SquareModel *)video asset:(SJVideoPlayerURLAsset *__nullable)asset {
//    if ( !asset ) return [self initWithVideo:video beginTime:0];
//
//    self = [super init];
//    if ( !self ) return nil;
//    _video = video;
//    _asset = [[SJVideoPlayerURLAsset alloc] initWithOtherAsset:asset playModel:nil];
//    _asset.title = self.video.title;
//    _asset.alwaysShowTitle = YES;
//    [player play];
//    return self;
//}
//
//
//- (instancetype)initWithVideo:(SquareModel *)video beginTime:(NSTimeInterval)beginTime {
//    self = [super init];
//    if ( !self ) return nil;
//    _video = video;
//    _asset = [[SJVideoPlayerURLAsset alloc] initWithURL:[NSURL URLWithString:self.video.url.url] specifyStartTime:beginTime];
//    _asset.title = video.title;
//    _asset.alwaysShowTitle = YES;
//    return self;
//}
//
//- (void)scrollViewDidScroll:(UIScrollView *)scrollView
//{
//    [self.sendMessageTool.textField resignFirstResponder];
//}
//
//- (void)viewDidAppear:(BOOL)animated
//{
//    [super viewDidAppear:animated];
//    [self.videoPlayer vc_viewDidAppear];
//}
//
//- (void)viewWillDisappear:(BOOL)animated
//{
//    [super viewWillDisappear:animated];
//    [self.videoPlayer vc_viewWillDisappear];
//}
//
//- (void)viewDidDisappear:(BOOL)animated
//{
//    [super viewDidDisappear:animated];
//    [self.videoPlayer vc_viewDidDisappear];
//}
//
//
//- (void)viewDidLoad
//{
//    [super viewDidLoad];
//    _pageNo = 0;
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(transformView:) name:UIKeyboardWillChangeFrameNotification object:nil];
//
//
//    [self.navigationController setNavigationBarHidden:YES animated:YES];
//    self.playerSuperView = [SJUIViewFactory viewWithBackgroundColor:[UIColor blackColor]];
//    self.playerSuperView.frame = CGRectMake(0, 0, kScreenW, kScreenW * 0.5625);
//    [self.view addSubview:self.playerSuperView];
//
//    _videoPlayer = [SJVideoPlayer player];
//    _videoPlayer.view.frame = CGRectMake(0, 0, kScreenW, kScreenW * 0.5625);
//    [self.view addSubview:_videoPlayer.view];
//    _videoPlayer.URLAsset = _asset;
//    self.view.backgroundColor = RGBCOLOR(34, 34, 34);
//
//    UIView *detaiBack = [UIView new];
//
//    self.titLabel = [SJUILabelFactory labelWithFont:[UIFont boldSystemFontOfSize:16] textColor:[UIColor whiteColor]];
//    self.titLabel.frame = CGRectMake(15, 10, kScreenW - 30, 30);
//    [detaiBack addSubview:self.titLabel];
//    self.titLabel.text = self.video.title;
//
//    self.descLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:12] textColor:RGBCOLOR(170, 170, 170)];
//    CGFloat descHeight = [GGAppTool sizeWithText:self.video.desc font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(kScreenW - 30, 999)].height;
//    self.descLabel.frame = CGRectMake(15, CGRectGetMaxY(self.titLabel.frame), kScreenW - 30, descHeight+10);
//    self.descLabel.numberOfLines = 0;
//    [detaiBack addSubview:self.descLabel];
//    self.descLabel.text = self.video.desc;
//
//    self.commentTool = [[GGCommentTool alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.descLabel.frame), kScreenW, 40)];
//    [detaiBack addSubview:self.commentTool];
//    [self.commentTool setModel:self.video];
//
//    @WeakObj(self);
//    self.commentTool.operateBtnClickBlock = ^(NSInteger btnTag) {
//        if (btnTag == 2) {
//
//            [selfWeak.tableView scrollsToTop];
//        }
//        if (btnTag == 3) {
//            //分享
//            SquareModel *model = selfWeak.video;
//            NSArray* imageArray = @[model.videoCover.url];
//            if (imageArray)
//            {
//                NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
//                [shareParams SSDKSetupShareParamsByText:model.desc
//                                                 images:imageArray
//                                                    url:[NSURL URLWithString:model.url.url]
//                                                  title:model.title
//                                                   type:SSDKContentTypeAuto];
//                [shareParams SSDKEnableUseClientShare];
//
//                SSUIShareSheetConfiguration *config = [SSUIShareSheetConfiguration new];
//                config.style = SSUIActionSheetStyleSimple;
//                config.cancelButtonHidden = YES;
//
//                [ShareSDK showShareActionSheet:selfWeak.view customItems:@[@(SSDKPlatformSubTypeWechatTimeline),@(SSDKPlatformSubTypeWechatSession),@(SSDKPlatformSubTypeQQFriend),@(SSDKPlatformSubTypeQZone)] shareParams:shareParams sheetConfiguration:config onStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
//
//                }];
//            }
//        }
//    };
//    self.commentTool.giveGoodBlock = ^(BOOL isGood) {
//        if (isGood) {
//
//        }
//        else
//        {
//
//        }
//    };
//
//    detaiBack.frame = CGRectMake(0, 0, kScreenW, CGRectGetMaxY(self.commentTool.frame));
//    detaiBack.backgroundColor = RGBCOLOR(34, 34, 34);
//
//
//    self.dataArr = [NSMutableArray arrayWithCapacity:0];
//
//    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.playerSuperView.frame), kScreenW, kScreenH - CGRectGetMaxY(self.playerSuperView.frame) - 40) style:UITableViewStylePlain];
//    self.tableView.backgroundColor = RGBCOLOR(20, 20, 20);
//    self.tableView.delegate = self;
//    self.tableView.dataSource = self;
//    [self.view addSubview:self.tableView];
//    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
//    self.tableView.tableHeaderView =detaiBack;
//
//    CGRect rect = CGRectMake(0, kScreenH - 45, kScreenW, 45);
//    GGSendCommentView *sendMessageTool = [[GGSendCommentView alloc] initWithFrame:rect];
//    [self.view addSubview:sendMessageTool];
//    sendMessageTool.senderClickedBlock = ^(GGSendCommentView *keyboardView, UIButton *button) {
//        //[self.zh_popupController dismiss];
//        [keyboardView.textField resignFirstResponder];
//        button.enabled = NO;
//        NSString *text = keyboardView.textField.text;
//        if (text.length > 0) {
//            [selfWeak addComment:text];
//
//        }
//    };
//    self.sendMessageTool = sendMessageTool;
//    [self loadData];
//
//    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
//        _pageNo = 0;
//        [self loadData];
//    }];
//
//    self.tableView.mj_footer = [MJRefreshFooter footerWithRefreshingBlock:^{
//        _pageNo++;
//        [self loadData];
//    }];
//}
//
//
//-(void)transformView:(NSNotification *)note
//{
//    NSDictionary *userInfo = note.userInfo;
//    CGFloat duration = [userInfo[@"UIKeyboardAnimationDurationUserInfoKey"] doubleValue];
//
//    CGRect keyFrame = [userInfo[@"UIKeyboardFrameEndUserInfoKey"] CGRectValue];
//    CGFloat moveY = keyFrame.origin.y - self.view.frame.size.height;//这个64是我减去的navigationbar加上状态栏20的高度,可以看自己的实际情况决定是否减去;
//
//    [UIView animateWithDuration:duration animations:^{
//        self.sendMessageTool.transform = CGAffineTransformMakeTranslation(0, moveY);
//    }];
//
////    [self.sendMessageTool setFrame:CGRectMake(0, self.view.frame.origin.y+deltaY, kScreenW, 45)];
//}
//
//
//- (void)loadData
//{
//    [SquareCommentModel queryCommentPageNo:_pageNo square:self.video resultBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
//        [self.tableView.mj_footer endRefreshing];
//        [self.tableView.mj_header endRefreshing];
//        if (!error)
//        {
//            if (_pageNo == 0) {
//                [self.dataArr removeAllObjects];
//            }
//
//            for(AVObject *obj in objects)
//            {
//                SquareCommentModel *model = [[SquareCommentModel alloc]init];
//                model.objectId = obj.objectId;
//                model.isgood = obj[@"isgood"];
//                model.publisher = obj[@"publisher"];
//                model.comment  = obj[@"comment"];
//                model.goodNum  = obj[@"goodNum"];
//                model.userIsGood = NO;
//                [self.dataArr addObject:model];
//            }
//            [self.tableView reloadData];
//        }
//    }];
//}
//
//- (void)addComment:(NSString *)text
//{
//    [SquareCommentModel addComment:text square:self.video resultBlock:^(BOOL succeeded, NSError * _Nullable error) {
//        self.sendMessageTool.senderButton.enabled = YES;
//        if (!error)
//        {
//             self.sendMessageTool.textField.text = @"";
//            [self loadData];
//            [XHToast showBottomWithText:@"发表成功"];
//
//            AVObject *obj = [AVObject objectWithClassName:DB_SQUARE objectId:self.video.objectId];
//            [obj incrementKey:@"commentNum"];
//            obj.fetchWhenSave = true;
//            [obj saveInBackground];
//            self.video.commentNum = [NSNumber numberWithInt:[self.video.commentNum integerValue] + 1 ];
//            [self.commentTool.commentButton setTitle:[NSString stringWithFormat:@"%@",self.video.commentNum] forState:UIControlStateNormal];
//        }
//        else
//        {
//            [XHToast showBottomWithText:@"发表失败,请重试"];
//        }
//    }];
//    //计数器增加
//
//}
//
//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
//{
//    return 1;
//}
//
//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
//{
//    return self.dataArr.count;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    SquareCommentModel *model = self.dataArr[indexPath.row];
//    return [GGCommentTableViewCell calcuHeithWith:model];
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    static NSString *iden = @"iden";
//    GGCommentTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
//    if (cell == nil)
//    {
//        cell = [[GGCommentTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
//    }
//    cell.backgroundColor = RGBCOLOR(34, 34, 34);
//    SquareCommentModel *model = self.dataArr[indexPath.row];
//    [cell setModel:model index:indexPath.row];
//    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
//    cell.goUserInfoVCBlock = ^(NSString *userId) {
//        GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
//        view.hidesBottomBarWhenPushed = YES;
//        view.objectId = userId;
//        [self.navigationController pushViewController:view animated:YES];
//    };
//    cell.giveGoodBlock = ^(BOOL isGood, NSInteger index) {
//        SquareCommentModel *cellModel = self.dataArr[index];
//        cellModel.userIsGood = isGood;
//        cellModel.goodNum = [NSNumber numberWithInt:isGood?[cellModel.goodNum integerValue]+1:[cellModel.goodNum integerValue]-1];
//        [self.dataArr replaceObjectAtIndex:index withObject:cellModel];
//
//    };
//
//    return cell;
//}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    SquareCommentModel *obj = self.dataArr[indexPath.row];
//    [SquareCommentModel giveCommentGood:obj resultBlock:^(BOOL succeeded, NSError * _Nullable error) {
//        if (!error)
//        {
//
//        }
//    }];
//}
//
//
//- (UIStatusBarStyle)preferredStatusBarStyle {
//    return UIStatusBarStyleLightContent;
//}
//
//- (BOOL)prefersHomeIndicatorAutoHidden {
//    return YES;
//}
//
//- (BOOL)prefersStatusBarHidden
//{
//    return YES;
//}
//
////
////- (void)viewWillAppear:(BOOL)animated
////{
////    [super viewWillAppear:animated];
////    [self.navigationController setNavigationBarHidden:YES animated:YES];
////}
////
////- (void)viewDidAppear:(BOOL)animated
////{
////    [super viewDidAppear:animated];
////    self.videoPlayerHelper.vc_viewDidAppearExeBlock();
////}
////
////- (void)viewWillDisappear:(BOOL)animated
////{
////    [super viewWillDisappear:animated];
////    [self.navigationController setNavigationBarHidden:NO animated:YES];
////    self.videoPlayerHelper.vc_viewWillDisappearExeBlock();
////}
////
////- (void)viewDidDisappear:(BOOL)animated
////{
////    [super viewDidDisappear:animated];
////    self.videoPlayerHelper.vc_viewDidDisappearExeBlock();
////}
//
//- (void)didReceiveMemoryWarning {
//    [super didReceiveMemoryWarning];
//    // Dispose of any resources that can be recreated.
//}
//
///*
//#pragma mark - Navigation
//
//// In a storyboard-based application, you will often want to do a little preparation before navigation
//- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
//    // Get the new view controller using [segue destinationViewController].
//    // Pass the selected object to the new view controller.
//}
//*/
//
//@end
