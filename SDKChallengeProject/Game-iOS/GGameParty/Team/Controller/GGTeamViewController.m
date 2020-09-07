//
//  GGTeamViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamViewController.h"
#import "GGTeamModel.h"
#import "GGGameModel.h"
#import "SGPagingView.h"
#import "GGTeamTableViewController.h"
#import "ZJScrollPageView.h"
#import "GGSpeedMatchView.h"
#import "GGFindingView.h"
#import "QueryMatchTimer.h"
#import "GGHaveFindView.h"
#import "GGContinueFindView.h"
#import "GGCreateRoomView.h"
#import "GGTeamRoomViewController.h"
//#import "JPSuspensionEntrance.h"
#import "GGHistoryTeamerViewController.h"
#import "GGUserInfoViewController.h"
#import "GGFollowViewController.h"
#import "GGSearchViewController.h"
#import "AYCheckManager.h"
#import "DesktopADView.h"
#import "GGGameManger.h"

@interface GGTeamViewController ()<
AVLiveQueryDelegate,
GGNavBarDelegate,
ZJScrollPageViewDelegate>

@property (nonatomic,strong)NSMutableArray *gameDataArr;

@property(weak, nonatomic)ZJScrollPageView *scrollPageView;

@property (nonatomic,strong)UIButton *matchBtn;

@property (nonatomic,strong)UIView *matchView;

@property (nonatomic,strong)UIButton *createRoomBtn;

@property (nonatomic,strong)AVLiveQuery *matchingQuery;

@property (nonatomic,strong)GGGameModel *gameModel;

@property (nonatomic,strong)AppNavView *navView;

@property (nonatomic,strong)UIImageView *loadingImage;

@property (nonatomic,strong)GGFindingView *findingView;

@property (nonatomic, strong) GGGameModel *selectGameModel;

@property (nonatomic, strong) NSArray *selectGameTypeArr;

@property (nonatomic, strong) GGHistoryTeamerViewController *historyView;

@property (nonatomic, strong) GGFollowViewController *followView;

@end


@implementation GGTeamViewController

#pragma mark - viewController.lifeCircle
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBar.hidden = YES;
   
    [self initData];
    
    self.navView = [[AppNavView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 61) titleArr:@[@"大厅",@"动态",@"最近"] isNeedSearch:YES];
    self.navView.v_delegate = self;
    [self.view addSubview:self.navView];
    
    if (IS_IPHONE_X) {
        self.navView.mj_y = 44;
    }
    
    self.loadingImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - 64 - CGRectGetMaxY(self.navView.frame))];
    self.loadingImage.contentMode = UIViewContentModeTop;
    self.loadingImage.image = [UIImage imageNamed:@"bgzhuangtai"];
    [self.view addSubview:self.loadingImage];
    
    [self loadGameData];
    
    [self checkCurrentTeam];
}

- (void)checkADShow
{
    SDWebImageManager *manager = [SDWebImageManager sharedManager] ;
    [manager downloadImageWithURL:[NSURL URLWithString:@"http://lc-nsnwnyel.cn-n1.lcfile.com/7cb8f4561424adf87a0b.png"] options:0 progress:^(NSInteger   receivedSize, NSInteger expectedSize) {
        // progression tracking code
    }  completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType,   BOOL finished, NSURL *imageURL) {
        if (image) {
          //  [self showADView:image];
            // do something with image
        }
    }];
}

- (void)showADView:(UIImage *)image
{
    
}


- (void)checkCurrentTeam
{
    AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
    [query whereKey:@"userObjectId" equalTo:[AVUser currentUser].objectId];
    [query includeKey:@"team"];
    [query selectKeys:@[@"team.delete"]];
    [query getFirstObjectInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
        if (!error) {
            GGTeamModel *team = [GGTeamModel vulueToObj:[object objectForKey:@"team"]];
            if ([team.isDelete isEqual:@(YES)])
            {
                [object deleteInBackground];
            }
            else
            {
                [self initFlowatView:team];
            }
        }
    }];
}

- (void)initFlowatView:(GGTeamModel *)teamModel
{
    [GGRoomManger shareInstance].teamModel = teamModel;
}

- (void)checkUpdate
{
    AYCheckManager *checkManger = [AYCheckManager sharedCheckManager];
    [checkManger checkVersionWithAlertTitle:@"发现新版本" nextTimeTitle:@"下次再说" confimTitle:@"立即更新" skipVersionTitle:nil];
}

#pragma mark - data
- (void)initData
{
    self.gameDataArr = [NSMutableArray arrayWithCapacity:0];
}

//加载游戏数据
- (void)loadGameData
{
    [GGGameModel queryGameListWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            self.loadingImage.hidden = YES;
            [self.gameDataArr removeAllObjects];
            NSMutableArray *saveArr = [NSMutableArray arrayWithCapacity:0];//需要存到本地的数组
            for(AVObject *obj in objects)
            {
                GGGameModel *model = [GGGameModel valueToObj:obj];
                NSArray *anwserArr = [[AVUser currentUser] objectForKey:@"allowGame"];
                if (model.isNeedQ)
                {
                    BOOL userNeedQ = YES;
                    for(NSString *temp in anwserArr)
                    {
                        if ([temp isEqualToString:model.objectId])
                        {
                            userNeedQ = NO;
                        }
                    }
                    model.userNeedQ = userNeedQ;
                }
                [self.gameDataArr addObject:model];
                
                [saveArr addObject:@{@"gameId":model.objectId,
                                     @"isNeedQ":model.isNeedQ?@"1":@"0"
                                     }];
                
            }
            
            [GGUserStandarDefault setObject:saveArr forKey:GGGAME_DATA_KEY];
            [GGUserStandarDefault synchronize];
            
            [self initUI];
        }
        
    }];
}



#pragma mark - Method--创建房间
- (void)gpcCreateRoom
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 340);
    
    GGCreateRoomView *view = [[GGCreateRoomView alloc]initWithFrame:rect];
    [view setGameData:self.gameDataArr];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    view.closeBtnClick = ^(GGPopupView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    view.createBtnClick = ^(GGCreateRoomView *view, GGGameModel *model, GGGameTypeModel *typeModel, NSInteger maxnum) {
        [self.zh_popupController dismiss];
        
        [self createRoomWith:model gameType:typeModel maxNum:maxnum];
        
    };
}



- (void)createRoomWith:(GGGameModel *)gameModel gameType:(GGGameTypeModel *)gameType maxNum:(NSInteger)maxNum
{
    BOOL userNeedQ = [GGAppTool userCanJoinGame:gameModel.objectId];
    if (userNeedQ)
    {
        [self showNormalGoAnwserAlert];
    }
    else
    {
        NSString *title = [GGAppTool getRandomRoomTitle];
        NSString *desc = @"";
        NSArray *chatUserArr = @[[AVUser currentUser].objectId,GGMATHINECUSTOMER];
        NSArray *userArr = @[[AVUser currentUser].objectId];
        NSArray *userPointerArr = @[[AVUser currentUser]];
        AVIMClient *client =  [LCChatKit sharedInstance].client;
        //创建一个群---
        [GGTeamModel checkUserCanAddNewTeamWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            if (!error && objects.count == 0)
            {
                [self analyticsEventStatistics:EVENT_CREATE];
                [client createConversationWithName:title clientIds:chatUserArr attributes:GGROOMTYPEATTRDIC options:AVIMConversationOptionNone callback:^(AVIMConversation * _Nullable conversation, NSError * _Nullable error) {
                    if (!error) {
                        //创建群聊成功        -----
                        AVObject *obj = [AVObject objectWithClassName:DB_TEAM];
                        [obj setObject:title  forKey:@"title"];
                        AVObject *type = [AVObject objectWithClassName:DB_GAME_TYPE objectId:gameType.objectId];

                        [obj setObject:type forKey:@"type"];
                        [obj setObject:[NSNumber numberWithInteger:maxNum] forKey:@"maxnum"];
                        [obj setObject:@"iOS" forKey:@"paltform"];
                        [obj setObject:[AVUser currentUser] forKey:@"publisher"];
                        
                        AVObject *game = [AVObject objectWithClassName:DB_GAME objectId:gameModel.objectId];
                        [obj setObject:game forKey:@"game"];
                        
                        [obj setObject:@(NO) forKey:@"isLock"];
                        [obj setObject:desc forKey:@"desc"];
                        [obj setObject:@(YES) forKey:@"status"];
                        
                        [obj setObject:userArr forKey:@"participants"];
                        [obj setObject:userPointerArr forKey:@"participantsUser"];
                        [obj setObject:conversation.conversationId forKey:@"groupChatId"];
                        [obj setObject:[NSDate date] forKey:@"sortDate"];
                        
                        AVObject *conv = [AVObject objectWithClassName:DB_CONVERSATION objectId:conversation.conversationId];
                        [obj setObject:conv forKey:@"conversation"];
                        
                        AVSaveOption *option = [[AVSaveOption alloc]init];
                        option.fetchWhenSave = YES;
                     
                        [obj saveInBackgroundWithOption:option block:^(BOOL succeeded, NSError * _Nullable error) {
                            if (succeeded)
                            {
                                GGTeamModel *model = [GGTeamModel vulueToObj:obj];
                                GGTeamRoomViewController *team = [[GGTeamRoomViewController alloc]init];
                                team.teamModel = model;
                                team.hidesBottomBarWhenPushed = YES;
                                team.showRoomSetView = YES;
                                [self.navigationController pushViewController:team animated:YES];
                                [GGTeamModel saveUserGameHistory:game conversation:conv team:obj];
                                
                            }
                            else
                            {
                                [XHToast showBottomWithText:@"error"];
                            }
                        }];
                    }
                    else
                    {
                        NSString *str = [NSString stringWithFormat:@"%@",error.description];
                        [XHToast showBottomWithText:str];
                    }
                    
                }];
            }
            else
            {
                
                 [GGRoomManger pushToRoom:nil];
//                [XHToast showCenterWithText:@"已加入其他房间了,请先退出其他房间方可加入"];
            }
        }];
    }
}

#pragma mark - Method---匹配
/**
 开始匹配
 */
- (void)goMatch
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 320);
    
    GGSpeedMatchView *view = [[GGSpeedMatchView alloc]initWithFrame:rect];
    [view setGameData:self.gameDataArr];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    view.closeBtnClick = ^(GGPopupView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    
    view.matchBtnClick = ^(GGSpeedMatchView *view, GGGameModel *model, NSArray *selectArr) {
        [self.zh_popupController dismiss];
        
        BOOL userNeedQ = [GGAppTool userCanJoinGame:model.objectId];
        if (userNeedQ)
        {
            [self showNormalGoAnwserAlert];
        }
        else
        {
            self.selectGameModel = model;
            self.selectGameTypeArr = selectArr;
            [self analyticsEventStatistics:EVENT_MATCH];
            [GGTeamModel checkUserCanAddNewTeamWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
                if (!error && objects.count == 0)
                {
                    
                    [self findingViewAndHandView];
                    [self queryRoom:model typeArr:selectArr];
                }
                else
                {
                       [GGRoomManger pushToRoom:nil];
//                    [XHToast showCenterWithText:@"已加入其他房间了,请先退出其他房间方可加入"];
                }
            }];
        }
    };
}

/**
 查询房间的liveQuery方法
 */
- (void)queryRoom:(GGGameModel *)model typeArr:(NSArray *)selectArr
{
    NSMutableArray *queryArr = [NSMutableArray arrayWithCapacity:0];
    for(NSDictionary *temp in selectArr)
    {
        AVQuery *query = [AVQuery queryWithClassName:DB_TEAM];
        [query whereKey:@"isLock" equalTo:@(NO)];
        [query whereKey:@"status" notEqualTo:@(NO)];
        [query whereKey:@"hidden" notEqualTo:@(YES)];
        [query whereKey:@"delete" equalTo:@(NO)];
        
        AVObject *game = [AVObject objectWithClassName:DB_GAME objectId:model.objectId];
        [query whereKey:@"game" equalTo:game];
        AVObject *type = [AVObject objectWithClassName:DB_GAME_TYPE objectId:temp[@"objectId"]];
        [query whereKey:@"type" equalTo:type];
        [queryArr addObject:query];
    }
    AVQuery *query = [AVQuery orQueryWithSubqueries:queryArr];
    [query orderByDescending:@"updatedAt"];
    
    self.matchingQuery = [[AVLiveQuery alloc]initWithQuery:query];
    self.matchingQuery.delegate = self;
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            //没发生错误
            self.matchView.hidden = YES;
            [self.view addSubview:self.findingView];
            if (objects.count == 0)
            {
                //没有相关房间
                [self subscribeMatching];
            }
            else
            {
                //筛选查出来的有没有满员的
                NSMutableArray *arr = [NSMutableArray arrayWithCapacity:0];
                for(AVObject *obj in objects)
                {
                    NSInteger maxnum = [[obj objectForKey:@"maxnum"] integerValue];
                    NSArray *participants = [obj objectForKey:@"participants"];
                    NSInteger currentNum = [participants count];
                    if (currentNum < maxnum)
                    {
                        //符合条件-----加入
                        [arr addObject:obj];
                        
                    }
                }
                if (arr.count > 0 )
                {
                    [self haveFindRoomAndJoin:arr[0]];
                    
                }
                else
                {
                     [self subscribeMatching];
                }
            }
        }
    }];
}

- (void)haveFindRoomAndJoin:(AVObject *)obj
{
    [self userStopFindRoom];
    [self findedRoomAndHandleView];
    [self joinTeam:obj];
    if (self.matchingQuery) {
        [self.matchingQuery unsubscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
            
        }];
    }
}

- (void)subscribeMatching
{
    [self.matchingQuery subscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
        if (succeeded) {
            //成功订阅
        }
        else
        {
            
        }
    }];
}

/**
 用户主动停止匹配房间
 */
- (void)userStopFindRoom
{
    self.matchView.hidden = NO;
    if (self.matchingQuery) {
        [self.matchingQuery unsubscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
            
        }];
    }
    [self.findingView stopTimer];
    [self.findingView removeFromSuperview];
    self.findingView = nil;
}


/**
 已找到房间后的页面UI处理
 */
- (void)findedRoomAndHandleView
{
    //self.findingView
    self.matchView.hidden = NO;
    [self.findingView removeFromSuperview];
    self.findingView = nil;
}


/**
 正在查找房间的页面UI处理
 */
- (void)findingViewAndHandView
{
    self.matchView.hidden = YES;
    [self.view addSubview:self.findingView];
    @WeakObj(self);
    self.findingView.findedFailedBlock = ^(GGFindingView *view) {
      //寻找失败
        [selfWeak.matchingQuery unsubscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
           //取消订阅
        }];
        [selfWeak showFindFailedView];
    };
}

/**
 查询超时且已超过30秒,已失败.提示下一步操作
 */
- (void)showFindFailedView
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 85);
    GGHaveFindView *view = [[GGHaveFindView alloc]initWithFrame:rect];
    [view changeTitle:@"未匹配到房间,正在为你创建房间"];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeCenter;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    [self userStopFindRoom];
    
    view.closeBtnClick = ^(GGHaveFindView *userInfoCard)
    {
        [self.zh_popupController dismiss];
        
        if (self.selectGameTypeArr.count > 0)
        {
            GGGameTypeModel *type = [GGGameTypeModel valueToObj:self.selectGameTypeArr[0]];
            NSInteger maxnum = [type.maxnum integerValue];
            [self createRoomWith:self.selectGameModel gameType:type maxNum:maxnum];
        }
    };
    
}



/**
 加入房间的方法

 @param obj 要加入的房间model--DB_Team的一个数据模型
 */
- (void)joinTeam:(AVObject *)obj
{
    //正式加入 ----- 初始化房间+显示房间倒计时
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 85);
    GGHaveFindView *view = [[GGHaveFindView alloc]initWithFrame:rect];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeCenter;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
 
    view.closeBtnClick = ^(GGHaveFindView *userInfoCard)
    {
        [self.zh_popupController dismiss];
        [GGTeamModel checkUserCanAddNewTeamWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            if (!error && objects.count == 0)
            {
                GGTeamRoomViewController *room = [[GGTeamRoomViewController alloc]init];
                room.hidesBottomBarWhenPushed = YES;
                room.teamModel = [GGTeamModel vulueToObj:obj];
                [self.navigationController pushViewController:room animated:YES];
            }
            else
            {
                  [XHToast showCenterWithText:@"已加入其他房间了,请先退出其他房间方可加入"];
            }
            
        }];
    };
}



#pragma mark - Nav.Delegate
- (void)shouldGoSearch
{
    [self gpcCreateRoom];
    
//    GGSearchViewController *search = [[GGSearchViewController alloc]init];
//    search.hidesBottomBarWhenPushed = YES;
//    [self.navigationController pushViewController:search animated:YES];
}

- (void)clickTitle:(NSString *)title index:(NSInteger)index
{
    if ([title isEqualToString:@"大厅"])
    {
        [self.view bringSubviewToFront:self.scrollPageView];
        
    }
    if ([title isEqualToString:@"动态"])
    {
        if (!self.followView) {
            self.followView = [[GGFollowViewController alloc]init];
            self.followView.view.frame = CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - CGRectGetMaxY(self.navView.frame) - 49);
            @WeakObj(self);
            self.followView.userInfoClickBlock = ^(AVUser *user) {
                GGUserInfoViewController *vc = [[GGUserInfoViewController alloc]init];
                vc.hidesBottomBarWhenPushed = YES;
                vc.objectId = user.objectId;
                [selfWeak.navigationController pushViewController:vc animated:YES];
            };
            self.followView.shouldHiddenOperateView = ^(BOOL shouldHidden) {
                    [selfWeak hiddenOperateView:shouldHidden];
            };
            self.followView.joinTeamBlock = ^(GGTeamModel *model) {
                BOOL userNeedQ = [GGAppTool userCanJoinGame:model.game.objectId];
                if (userNeedQ)
                {
                    [selfWeak showNormalGoAnwserAlert];
                }
                else
                {
                    GGTeamRoomViewController *room = [[GGTeamRoomViewController alloc]init];
                    room.hidesBottomBarWhenPushed = YES;
                    room.teamModel = model;
                    [selfWeak.navigationController pushViewController:room animated:YES];
                }
            };
            [self.view addSubview:self.followView.view];
        }
        [self.view bringSubviewToFront:self.followView.view];
    }
    if ([title isEqualToString:@"最近"])
    {
        if (!self.historyView)
        {
            self.historyView = [[GGHistoryTeamerViewController alloc]init];
          self.historyView.view.frame = CGRectMake(0, CGRectGetMaxY(self.navView.frame), kScreenW, kScreenH - CGRectGetMaxY(self.navView.frame) - 49);
            [self.view addSubview:self.historyView.view];
            @WeakObj(self);
            self.historyView.userInfoClickBlock = ^(GGHistoryTeamerViewController *vc, GGUserModel *model) {
                GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
                view.hidesBottomBarWhenPushed = YES;
                view.objectId = model.objectId;
                [selfWeak.navigationController pushViewController:view animated:YES];
            };
            self.historyView.shouldHiddenOperateView = ^(BOOL shouldHidden) {
                [selfWeak hiddenOperateView:shouldHidden];
            };
        }
        [self.view bringSubviewToFront:self.historyView.view];
    }
    [self.view bringSubviewToFront:self.matchView];
}

#pragma mark - UI.init
- (void)initUI
{

    NSMutableArray *titleArr = [NSMutableArray arrayWithCapacity:0];
    [titleArr addObject:@""];
    
    for(GGGameModel *model in self.gameDataArr)
    {
        GGTeamTableViewController *team = [[GGTeamTableViewController alloc]init];
        team.gameModel = model;
        [titleArr addObject:model.gameName];
    }
    
    self.automaticallyAdjustsScrollViewInsets = NO;
    ZJSegmentStyle *style = [[ZJSegmentStyle alloc] init];
    style.showLine = NO;
    style.normalTitleColor = RGBCOLOR(51, 51, 51);
    style.selectedTitleColor = GGBackGround_Color;
    style.segmentHeight = 80;
    style.showImage = YES;
    style.imagePosition = TitleImagePositionLeft;
    style.autoAdjustTitlesWidth = YES;
    
    CGRect scrollPageViewFrame = CGRectMake(0, CGRectGetMaxY(self.navView.frame), self.view.bounds.size.width, self.view.bounds.size.height - 64.0);
    
    if (!self.scrollPageView) {
        ZJScrollPageView *scrollPageView = [[ZJScrollPageView alloc] initWithFrame:scrollPageViewFrame segmentStyle:style titles:titleArr parentViewController:self delegate:self];
        self.scrollPageView = scrollPageView;
        [self.view addSubview:self.scrollPageView];
    }
    __weak typeof(self) weakSelf = self;
    self.scrollPageView.extraBtnOnClick = ^(UIButton *extraBtn){
        weakSelf.title = @"点击了extraBtn";
        NSLog(@"点击了extraBtn");
    };
    
    [self initOtherUI];
    
}

- (GGFindingView *)findingView
{
    if (!_findingView)
    {
        _findingView = [[GGFindingView alloc]initWithFrame:CGRectMake(15, self.view.frame.size.height - 68 - 49,self.view.frame.size.width - 30, 68)];
        _findingView.layer.masksToBounds = YES;
        _findingView.layer.cornerRadius = 5;
        @WeakObj(self);
        _findingView.stopFindBlock = ^(GGFindingView *view) {
            [selfWeak userStopFindRoom];
        };
        if (IS_IPHONE_X) {
            _findingView.frame = CGRectMake(15, self.view.frame.size.height - 68 - 83,self.view.frame.size.width - 30, 68);
        }
    }
    return _findingView;
}

- (void)initOtherUI
{
    self.matchView = [[UIView alloc]initWithFrame:CGRectMake(15, self.view.frame.size.height - 63 - 49, kScreenW - 30, 55)];
    [self.view addSubview:self.matchView];
    if (IS_IPHONE_X) {
        self.matchView.mj_y = kScreenH - 63 - 88;
    }
    
    self.matchBtn = [SJUIButtonFactory buttonWithTitle:@"一键快速组队" titleColor:[UIColor whiteColor] backgroundColor:[UIColor clearColor] imageName:@"icon_speed" target:self sel:@selector(goMatch) tag:888];
    self.matchBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    [self.matchBtn setBackgroundImage:[UIImage imageNamed:@"bg_yijian"] forState:UIControlStateNormal];
    self.matchBtn.frame = CGRectMake(0, 0, self.matchView.frame.size.width * 0.73, 55);
    [self.matchView addSubview:self.matchBtn];
    
    self.createRoomBtn = [SJUIButtonFactory buttonWithTitle:@"创建房间" titleColor:[UIColor whiteColor] backgroundColor:[UIColor clearColor] imageName:@"" target:self sel:@selector(gpcCreateRoom) tag:999];
    self.createRoomBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    self.createRoomBtn.frame = CGRectMake(CGRectGetMaxX(self.matchBtn.frame), 0, self.matchView.frame.size.width * 0.27, 55);
    [self.createRoomBtn setBackgroundImage:[UIImage imageNamed:@"bg_chuangj"] forState:UIControlStateNormal];
    [self.matchView addSubview:self.createRoomBtn];
    
//    self.createRoomBtn.frame = self.matchView.bounds;
    
}

#pragma mark - pageView.Delegate
- (NSInteger)numberOfChildViewControllers
{
    return self.gameDataArr.count+1;
}

/// 设置图片
- (void)setUpTitleView:(ZJTitleView *)titleView forIndex:(NSInteger)index
{
    if (index == 0)
    {
        //全部
        titleView.normalImage = [UIImage imageNamed:@"bg_all_zi2"];
        titleView.selectedImage = [UIImage imageNamed:@"bg_all_zi"];
    }
    else
    {
        GGGameModel *model =  self.gameDataArr[index-1];
        [titleView.iconimageView setImageWithURL:[NSURL URLWithString:model.gameicon.url]];
        titleView.normalImage = [UIImage imageNamed:@"bg_huise"];
        titleView.selectedImage = [UIImage imageNamed:@"bg_all"];
    }
}

- (UIViewController<ZJScrollPageViewChildVcDelegate> *)childViewController:(UIViewController<ZJScrollPageViewChildVcDelegate> *)reuseViewController forIndex:(NSInteger)index
{
    if (index == 0)
    {
        // 注意这个效果和tableView的deque...方法一样, 会返回一个可重用的childVc
        // 请首先判断返回给你的是否是可重用的
        // 如果为nil就重新创建并返回
        // 如果不为nil 直接使用返回给你的reuseViewController并进行需要的设置 然后返回即可
        GGTeamTableViewController *childVc = (GGTeamTableViewController *)reuseViewController;
        if (childVc == nil) {
            childVc = [[GGTeamTableViewController alloc] init];
        }
        childVc.shouldHiddenOperateView = ^(BOOL shouldHidden) {
            [self hiddenOperateView:shouldHidden];
        };
        return childVc;
    }
    else
    {
        GGGameModel *model =  self.gameDataArr[index-1];
        GGTeamTableViewController *team = (GGTeamTableViewController *)reuseViewController;
        if (team == nil)
        {
            team = [[GGTeamTableViewController alloc] init];
        }
        team.gameModel = model;
        team.shouldHiddenOperateView = ^(BOOL shouldHidden) {
            [self hiddenOperateView:shouldHidden];
        };
        
        return team;
    }
}

- (void)scrollPageController:(UIViewController *)scrollPageController childViewControllDidAppear:(UIViewController *)childViewController forIndex:(NSInteger)index
{
    [GGGameManger shareInstance].index = index;
}

- (void)hiddenOperateView:(BOOL)isHidden
{
    [UIView animateWithDuration:0.5 animations:^{
        self.matchView.mj_y = isHidden?self.view.frame.size.height:self.view.frame.size.height - 63 - 49;
        if (IS_IPHONE_X) {
            self.matchView.mj_y = isHidden?kScreenH:kScreenH - 63 - 88;
        }
    } completion:nil];
}

- (BOOL)shouldAutomaticallyForwardAppearanceMethods
{
    return NO;
}


#pragma mark - liveQuery.Delegate ---
- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidCreate:(id)object
{
    if (liveQuery == self.matchingQuery)
    {
        NSLog(@"匹配到用户:收到DidCreate: %@",object);
        [self checkJoin:object];
    }
}

- (void)checkJoin:(id)object
{
    NSInteger maxnum = [[object objectForKey:@"maxnum"] integerValue];
    NSArray *participants = [object objectForKey:@"participants"];
    NSInteger currentNum = [participants count];
    if (currentNum < maxnum)
    {
        [self haveFindRoomAndJoin:object];
    }
}

- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidDelete:(id)object
{
    NSLog(@"收到delete%@",object);
}

- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidEnter:(id)object updatedKeys:(NSArray<NSString *> *)updatedKeys
{
    NSLog(@"收到DidEnter%@",object);
}

- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidLeave:(id)object updatedKeys:(NSArray<NSString *> *)updatedKeys
{
    NSLog(@"收到DidLeave%@",object);
}

- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidUpdate:(id)object updatedKeys:(NSArray<NSString *> *)updatedKeys
{
    NSLog(@"收到DidUpdate%@",object);
}



#pragma mark - 暂时废弃方法---但后续可能会用到
/**
 加入匹配队列 ---- 匹配队列当前阶段不做了
 */
- (void)addToMatchingQueueWithGame:(GGGameModel *)model typeArr:(NSArray *)selectArr
{
    AVObject *obj = [AVObject objectWithClassName:DB_MATCHING];
    [obj setObject:[AVUser currentUser] forKey:@"publisher"];
    
    NSDate *finishDate = [[NSDate alloc]initWithTimeIntervalSinceNow:30];
    [obj setObject:finishDate forKey:@"finishTime"];//如果愿意一直等待---则此字段为空
    
    [obj setObject:selectArr forKey:@"gameTypeArr"];
    
    AVObject *game = [AVObject objectWithClassName:DB_GAME objectId:model.objectId];
    [obj setObject:game forKey:@"game"];
    
    [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        if (succeeded)
        {
            //加入成功
            AVQuery *query = [AVQuery queryWithClassName:DB_MATCHING];
            [query whereKey:@"publisher" notEqualTo:[AVUser currentUser]];//不是自己
            [query whereKey:@"game" equalTo:game];//
            [query whereKey:@"gameTypeArr" containsAllObjectsInArray:selectArr];
            [query includeKey:@"publisher"];
            self.matchingQuery = [[AVLiveQuery alloc]initWithQuery:query];
            self.matchingQuery.delegate = self;
            [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
                if (!error)
                {
                    if (objects.count == 0)
                    {
                        //没有人.把自己标示成队列标示 --- 此房间第一人
                        [obj setObject:[AVUser currentUser].objectId forKey:@"matchMark"];
                        [obj saveInBackground];//存到表里面去
                    }
                    else
                    {
                        
                    }
                }
            }];
            [self.matchingQuery subscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
                //订阅回调//
                if (succeeded)
                {
                    NSLog(@"已进入匹配队列");
                }
            }];
        }
        
    }];
}

@end
