//
//  GGSquareViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSquareViewController.h"
#import "SquareModel.h"
//#import "SJVideoPlayer.h"
//#import "UIViewController+SJVideoPlayerAdd.h"
#import "UIScrollView+SJRefreshAdd.h"
#import "UIView+SJVideoPlayerAdd.h"
//#import "SJVideoPlayer.h"
#import "GGSquareTableViewCell.h"
#import "SquareModel.h"
//#import "GGSquareDetailViewController.h"
#import "GameChannelCollectionViewCell.h"
#import "GGGameModel.h"
//#import "GGSqVideoViewController.h"
@interface GGSquareViewController ()<
UICollectionViewDelegate,
UICollectionViewDataSource,
GGNavBarDelegate>
{
    
  
}

//@property (nonatomic, strong) GGSqVideoViewController *videoVC;



@property (nonatomic,strong)NSMutableArray *channelDataArr;//游戏房间数据源


@property (nonatomic,strong)UICollectionView *collectionView;

@property (nonatomic,strong)AppNavView *navView;

@end

@implementation GGSquareViewController

#pragma mark - viewController.lifeCircle
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
  //  [self.player vc_viewDidAppear];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
  //  [self.player vc_viewWillDisappear];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
 //   [self.player vc_viewDidDisappear];
}

//- (BOOL)prefersStatusBarHidden
//{
//    return [self.player vc_prefersStatusBarHidden];
//}

//- (UIStatusBarStyle)preferredStatusBarStyle
//{
//    return [self.player vc_preferredStatusBarStyle];
//}

- (BOOL)prefersHomeIndicatorAutoHidden
{
    return YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = YES;
    
    [self initData];

    [self initUI];
    
    [self loadGameData];
    
}
#pragma mark - UI
- (void)initUI
{
    self.navView = [[AppNavView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 61)titleArr:@[@"游戏房间"]];
    self.navView.v_delegate = self;
    [self.view addSubview:self.navView];
    if (IS_IPHONE_X) {
        self.navView.mj_y = 44;
    }
    
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    flowLayout.minimumInteritemSpacing = 10;
    flowLayout.minimumLineSpacing = 10;
    flowLayout.itemSize =  CGSizeMake((kScreenW - 30)/2,(kScreenW - 30)/2 * 1.17);
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    //设置CollectionView的属性
    self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - 64 - 61) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor whiteColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    self.collectionView.scrollEnabled = YES;
    [self.view addSubview:self.collectionView];
    //注册Cell
    [self.collectionView registerClass:[GameChannelCollectionViewCell class] forCellWithReuseIdentifier:@"cell"];
    
    self.collectionView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
        
        [self loadGameData];
    }];
}

- (void)clickTitle:(NSString *)title index:(NSInteger)index
{
//    if (index == 0)
//    {
//        self.videoVC = nil;
//        [self.view bringSubviewToFront:self.collectionView];
//    }
//    else
//    {
//        if (!self.videoVC) {
//            self.videoVC = [[GGSqVideoViewController alloc]init];
//            self.videoVC.view.frame = CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, self.collectionView.frame.size.height );
//            [self.view addSubview:self.videoVC.view];
//            
//            @WeakObj(self);
//            self.videoVC.pushToVideoDetailBlock = ^(GGSqVideoViewController *vc, SJVideoPlayerURLAsset *asset,SquareModel *model,NSInteger index) {
//                
//                GGSquareDetailViewController *detail = [[GGSquareDetailViewController alloc] initWithVideo:model asset:asset ];
//                detail.index = index;
//                detail.hidesBottomBarWhenPushed = YES;
//                [selfWeak.navigationController pushViewController:detail animated:YES];
//            };
//        }
//        [self.view bringSubviewToFront:self.videoVC.view];
//
//    }
}
#pragma mark - Data
- (void)initData
{
    self.channelDataArr = [NSMutableArray arrayWithCapacity:0];
}

- (void)loadGameData
{
    [GGGameModel queryGameConersationListWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        [self.collectionView.mj_header endRefreshing];
        if (!error)
        {
            [self.channelDataArr removeAllObjects];
            for(AVObject *obj in objects)
            {
                GGGameModel *model = [GGGameModel valueToObj:obj];
                [self.channelDataArr addObject:model];
            }
            [self.collectionView reloadData];
        }
    }];
}


#pragma mark - Method

- (void)joinChat:(UIButton *)btn
{
    btn.enabled = NO;
    GGGameModel *model = self.channelDataArr[btn.tag - 100];
    AVIMClient *client = [LCChatKit sharedInstance].client;
    NSString *conversationId=model.chatId;
    AVIMConversationQuery *query = [client conversationQuery];
    // 根据已知 Id 获取对话实例，当前实例为聊天室。
    [query getConversationById:conversationId callback:^(AVIMConversation *conversation, NSError *error) {
        if (!error && conversation)
        {
            [conversation joinWithCallback:^(BOOL succeeded, NSError *error) {
                btn.enabled = YES;
                if (succeeded) {
                    NSLog(@"加入成功！");
                    GGChatRoomConversationViewController *conversationVC = [[GGChatRoomConversationViewController alloc] initWithConversationId:model.chatId];
                    conversationVC.title = conversationVC.title;
                    conversationVC.hidesBottomBarWhenPushed = YES;
                    [self.rt_navigationController pushViewController:conversationVC animated:YES];
                    
                }
                else
                {
                    [XHToast showCenterWithText:@"加入失败,请重试"];
                }
            }];
        }
        else
        {
            btn.enabled = YES;
            [XHToast showCenterWithText:@"获取会话失败,请重试"];
        }
    }];
}

- (void)conversation:(AVIMConversation *)conversation tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    //跳转到聊天详情
    LCCKConversationViewController *conversationVC = [[LCCKConversationViewController alloc] initWithConversationId:conversation.conversationId];
    conversationVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:conversationVC animated:YES];
}



#pragma mark - collectionView.Delegate

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.channelDataArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"cell";
    GameChannelCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
  //  cell.layer.masksToBounds = YES;
//    cell.layer.cornerRadius = 10;
//    cell.backgroundColor = Main_Color;
    cell.joinButton.tag = indexPath.row+100;
    [cell.joinButton addTarget:self action:@selector(joinChat:) forControlEvents:UIControlEventTouchUpInside];
    GGGameModel *model = self.channelDataArr[indexPath.row];
    [cell setGameModel:model];
    return cell;
}

// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return  CGSizeMake((kScreenW - 30)/2,(kScreenW - 30)/2 * 1.17);
}

// 定义整个CollectionViewCell与整个View的间距
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(10, 10,10, 10);//（上、左、下、右）
}


//  定义每个UICollectionView的横向间距
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
   // Medal *p = self.medals[indexPath.item];
    NSLog(@"---------------------");
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}




@end
