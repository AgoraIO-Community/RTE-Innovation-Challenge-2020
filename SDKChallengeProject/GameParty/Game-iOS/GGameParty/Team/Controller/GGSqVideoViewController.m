////
////  GGSqVideoViewController.m
////  GGameParty
////
////  Created by Victor on 2018/8/9.
////  Copyright © 2018年 Victor. All rights reserved.
////
//#import "SJBaseVideoPlayer.h"
//#import "SJAttributeWorker.h"
//#import "SJVideoPlayerRegistrar.h"
//#import "GGSqVideoViewController.h"
//#import "GGSquareViewController.h"
//#import "SquareModel.h"
//#import "SJVideoPlayer.h"
////#import "UIViewController+SJVideoPlayerAdd.h"
//#import "UIScrollView+SJRefreshAdd.h"
//#import "UIView+SJVideoPlayerAdd.h"
//#import "SJVideoPlayer.h"
//#import "GGSquareTableViewCell.h"
//#import "SquareModel.h"
//#import "GGSquareDetailViewController.h"
//#import "GameChannelCollectionViewCell.h"
//#import "GGGameModel.h"
//#import "GGCommentTool.h"
//#import <ShareSDK/ShareSDK.h>
//#import <ShareSDKUI/ShareSDKUI.h>
//@interface GGSqVideoViewController ()<UITableViewDelegate,UITableViewDataSource,
//GGVideoListTableViewCellDelegate>
//{
//    NSInteger _pageNo;
//}
//
//@property (nonatomic,strong)NSMutableArray *dataArr;//视频数据源
//@property (nonatomic,strong)UITableView *tableView;
//@property (nonatomic, assign) UITableViewCell *cell;
//@property (nonatomic, strong, nullable) SJVideoPlayer *videoPlayer;
//
//
//@end
//
//@implementation GGSqVideoViewController
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
//- (void)viewDidLoad {
//    [super viewDidLoad];
//    self.dataArr = [NSMutableArray arrayWithCapacity:0];
//    _pageNo = 0;
//    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 61 - 49 - 20) style:UITableViewStylePlain];
//    self.tableView.delegate = self;
//    self.tableView.dataSource = self;
//    self.sj_displayMode = SJPreViewDisplayMode_Origin;
//    [self.view addSubview:self.tableView];
//    self.sj_displayMode = SJPreViewDisplayMode_Origin;
//    if (IS_IPHONE_X) {
//        self.tableView.mj_h = self.view.frame.size.height - 61 - 49 - 60;
//    }
//    
//    self.tableView.tableFooterView = [UIView new];
//    [self loadVideoData];
////    [self.tableView.mj_header beginRefreshing];
//    
//    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
//        _pageNo = 0;
//        [self loadVideoData];
//    }];
//    self.tableView.mj_footer = [MJRefreshFooter footerWithRefreshingBlock:^{
//        _pageNo ++;
//        [self loadVideoData];
//    }];
//}
//
//
//
//- (void)loadVideoData
//{
//    [SquareModel querySqueryDataPageNo:_pageNo resultBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
//        [self.tableView.mj_header endRefreshing];
//        if (!error)
//        {
//            [self.tableView.mj_header endRefreshing];
//            [self.tableView.mj_footer endRefreshing];
//            
//            if (_pageNo == 0) {
//                [self.dataArr removeAllObjects];
//            }
//            
//            for(NSDictionary *dic in objects)
//            {
//                SquareModel *model = [[SquareModel alloc]init];
//                model.title = dic[@"title"];
//                AVFile *file = dic[@"videoCover"];
//                AVFile *file2 = dic[@"url"];
//                model.videoCover = file;
//                model.url = file2;
//                model.title = dic[@"title"];
//                model.objectId = dic[@"objectId"];
//                model.isgood = dic[@"isgood"];
//                model.desc = dic[@"desc"];
//                model.commentNum = dic[@"commentNum"];
//                model.goodNum = dic[@"goodNum"];
//                [self.dataArr addObject:model];
//            }
//            [self.tableView reloadData];
//        }
//    }];
//}
//
//
//
//#pragma mark - table.Delegate
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
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    static NSString *iden = @"iden";
//    GGSquareTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
//    if (cell == nil)
//    {
//        cell = [[GGSquareTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
//    }
//    SquareModel *model = self.dataArr[indexPath.row];
//    cell.model = model;
//    cell.delegate = self;
//    cell.coverImageView.tag = indexPath.row+1;
//    
//    cell.commentTool.operateBtnClickBlock = ^(NSInteger btnTag) {
//        if (btnTag == 2) {
//            
//            //评论
//            [self tableView:tableView didSelectRowAtIndexPath:indexPath];
//        }
//        if (btnTag == 3) {
//            //分享
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
//                [ShareSDK showShareActionSheet:self.view.window customItems:@[@(SSDKPlatformSubTypeWechatTimeline),@(SSDKPlatformSubTypeWechatSession),@(SSDKPlatformSubTypeQQFriend),@(SSDKPlatformSubTypeQZone)] shareParams:shareParams sheetConfiguration:config onStateChanged:^(SSDKResponseState state, SSDKPlatformType platformType, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error, BOOL end) {
//                    
//                }];
//            }
//        }
//    };
//    return cell;
//}
//
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//    return [GGSquareTableViewCell heightWithVideo:self.dataArr[indexPath.row]];
//}
//
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    
//    SquareModel *model = self.dataArr[indexPath.row];
//    SJVideoPlayerURLAsset *asset = self.videoPlayer.URLAsset;
//    if (self.pushToVideoDetailBlock)
//    {
//        self.pushToVideoDetailBlock(self, asset,model,indexPath.row);
//    }
//   
//}
//
//- (void)clickedPlayBtnOnTabCell:(GGSquareTableViewCell *)cell playerSuperview:(UIView *)playerSuperview
//{
//    SJPlayModel *playModel = [SJPlayModel UITableViewCellPlayModelWithPlayerSuperviewTag:playerSuperview.tag  // 请务必设置tag, 且不能等于0. 由于重用机制, 当视图滚动时, 播放器需要通过此tag寻找其父视图
//                                                                             atIndexPath:[self.tableView indexPathForCell:cell]
//                                                                               tableView:self.tableView];
//    SJVideoPlayerURLAsset *asset =
//    [[SJVideoPlayerURLAsset alloc] initWithURL:[NSURL URLWithString:@"http://..."]
//                                     playModel:playModel];
//    
//    // 2. 设置资源标题
//    asset.title = @"DIY心情转盘 #手工##手工制作##卖包子喽##1块1个##卖完就撤#";
//    // 3. 默认情况下, 小屏时不显示标题, 全屏后才会显示, 这里设置一直显示标题
//    asset.alwaysShowTitle = YES;
//    
//    _videoPlayer = [SJVideoPlayer player];
//    [playerSuperview addSubview:_videoPlayer.view];
//    [_videoPlayer.view mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.edges.offset(0);
//    }];
//    // 设置资源
//    _videoPlayer.URLAsset = asset;
//}
//
//
//
//- (void)clickedPlayOnTabCell:(GGSquareTableViewCell *)cell
//{
//    NSIndexPath *indexPath = [self.tableView indexPathForCell:cell];
//    SquareModel *model = self.dataArr[indexPath.row];
//    
//    SJPlayModel *playModel = [SJPlayModel UITableViewCellPlayModelWithPlayerSuperviewTag:cell.coverImageView.tag  // 请务必设置tag, 且不能等于0. 由于重用机制, 当视图滚动时, 播放器需要通过此tag寻找其父视图
//                                                                             atIndexPath:[self.tableView indexPathForCell:cell]
//                                                                               tableView:self.tableView];
//    SJVideoPlayerURLAsset *asset = [[SJVideoPlayerURLAsset alloc] initWithURL:[NSURL URLWithString:model.url.url] playModel:playModel];
//    asset.title = model.title;
//    asset.alwaysShowTitle = YES;
//    _videoPlayer = [SJVideoPlayer player];
//    
//    [cell.coverImageView addSubview:_videoPlayer.view];
//    [_videoPlayer.view mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.edges.offset(0);
//    }];
//    _videoPlayer.URLAsset = asset;
//}
//
//
//- (void)playerNeedPlayNewAssetAtIndexPath:(NSIndexPath *)indexPath
//{
//    SquareModel *model = self.dataArr[indexPath.row];
//    
//    GGSquareTableViewCell *cell = [self.tableView cellForRowAtIndexPath:indexPath];
//    NSURL *URL = [NSURL URLWithString:cell.model.url.url];
//    SJPlayModel *playModel = [SJPlayModel UITableViewCellPlayModelWithPlayerSuperviewTag:cell.coverImageView.tag atIndexPath:indexPath tableView:self.tableView];
//    SJVideoPlayerURLAsset *asset = [[SJVideoPlayerURLAsset alloc] initWithURL:URL playModel:playModel];
//    asset.title = model.title;
//    asset.alwaysShowTitle = YES;
//    
//    // [self playWithAsset:asset playerParentView:cell.coverImageView];
//}
//
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
