//
//  GGTeamRoomViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/27.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamRoomViewController.h"
//#import <QNRTCclientclientKit/QNRTCKit.h>
#import <CommonCrypto/CommonHMAC.h>
#import <CommonCrypto/CommonCryptor.h>//
#import "CCBase64.h"
#import "GGChatTool.h"
#import "GGSendMessageView.h"
#import "GGRoomHeadView.h"
#import "GGNoticeView.h"
#import "GGUserListView.h"
#import "YBPopupMenu.h"
#import "GGRoomEditView.h"
#import "GGUserInfoCardView.h"
#import "GGSetSteamIDView.h"
#import "GGShareRoomView.h"
#import "GGChatModel.h"
#import "GGChatTeamRoomCell.h"
#import "GGUserInfoViewController.h"
#import "GGShareToFriendViewController.h"
#import "GGConversionListViewController.h"
#import "UIPasteboard+YYText.h"
#import <MobLink/MobLink.h>
#import "Waver.h"
#import "NoTeamHolder.h"
#import "GGAudioBreakTips.h"
#import "GGShareToPCView.h"
#import "XWFloatingWindowView.h"


//#import "UINavigationController+FDFullscreenPopGesture.h"
/*
 // 2. 计算HMAC-SHA1签名，并对签名结果做URL安全的Base64编码
 sign = hmac_sha1(encodedRoomAccess, <SecretKey>)
 encodedSign = urlsafe_base64_encode(sign)
 // 3. 将AccessKey与以上两者拼接得到房间鉴权
 roomToken = "<AccessKey>" + ":" + encodedSign + ":" + encodedRoomAccess
 
 */

#define ROOMTIPS @"注意：房间内请文明聊天，素质交友，禁止发布涉及暴力、色情、诈骗等信息，若发现则封号处理，创建良好的沟通环境,人人有责"
#define ROOMTOPTIPS @"房间已在大厅中置顶..."
#define ROOMCREATETIPS @"房间创建成功,邀请伙伴开黑"
#define ROOMDATACHANGED @"队长已修改房间信息"

@interface GGTeamRoomViewController ()<
//QNRTCSessionDelegate,
GGChatToolDelegate,
UITableViewDelegate,UITableViewDataSource,
GGUserInfoClickDelegate,//用户头像点击
YBPopupMenuDelegate,
AgoraRtcEngineDelegate,UIGestureRecognizerDelegate>
{
    BOOL _isShowFloatWindow;
}

@property (nonatomic, assign) BOOL isAdmin;//是否是群组

@property (nonatomic,strong)GGAudioBreakTips *audioBreakTips;//语音断开的提示视图

@property (nonatomic,strong)GGRoomHeadView *headerView;//顶部房间信息

@property (nonatomic,strong)GGNoticeView *noticeView;//公告栏

@property (nonatomic,strong)GGUserListView *userListView;//群用户列表

@property (nonatomic,strong)GGChatTool *chatTool;//关闭麦克风等工具栏

@property (nonatomic,strong)UIButton *invitaFriendBtn;//邀请好友
@property (nonatomic,strong)UIButton *shareToPC;//邀请好友

@property (nonatomic,strong)GGSendMessageView *sendMessageTool;//发送消息的工具栏

@property (nonatomic, strong) AVIMClient *client;//聊天Client

@property (nonatomic,strong)AVIMConversation * conversation;//当前会话

@property (nonatomic,strong)UITableView * tableView;;

@property (nonatomic,strong)NSMutableArray *chatDataArr;

@property (nonatomic,strong)zhPopupController *zh_popupController;

@property (nonatomic,strong)UIImageView *loadingView;

@property (nonatomic,strong) AgoraRtcEngineKit *engine;//语音engine
@property (nonatomic,strong) NSString  *engineToken;
@property (nonatomic,copy)MLSDKScene *scene;//场景回复使用

@property (nonatomic,strong)GGRoomManger *shareRoom;

@end

@implementation GGTeamRoomViewController
+ (NSString *)MLSDKPath
{
    // 该控制器的特殊路径
    return @"/demo/a";
}


// 根据场景信息初始化方法
- (instancetype)initWithMobLinkScene:(MLSDKScene *)scene
{
    if (self = [super init])
    {
        self.scene = scene;
    }
    return self;
}

#pragma mark - controller.lifeCircle
- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    _isShowFloatWindow = YES;

    if ([XWFloatingWindowView isShowingWithViewController:self]) {
        [XWFloatingWindowView remove];//移除
    }
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    if (_isShowFloatWindow && self.shareRoom.currentTeamRoom) {
         [XWFloatingWindowView showWithViewController:self isPop:NO];
    }
    // 每次进入如果有
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _isShowFloatWindow = YES;
    self.navigationController.navigationBarHidden = YES;
    self.rt_disableInteractivePop = NO;
    self.navigationController.interactivePopGestureRecognizer.delegate = self;
    
    self.hk_iconImage = GGDefault_User_Head;

    UIImageView *image = [[UIImageView alloc]initWithFrame:self.view.bounds];
    image.image = [UIImage imageNamed:@"room_Backbg"];
    [self.view addSubview:image];
    
    self.loadingView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, kScreenH - 20)];
    self.loadingView.contentMode = UIViewContentModeScaleAspectFit;
    self.loadingView.image = [UIImage imageNamed:@"bg_roomLoading"];
    [self.view addSubview:self.loadingView];
    
    if (self.teamModel)
    {
        [self jointWithModel:self.teamModel];
    }
    else
    {
        [self jointWithTeamCode:self.teamCode];
    }
}


#pragma mark - 加入群组
- (void)jointWithTeamCode:(NSString *)teamCode
{
    AVQuery *query = [AVQuery queryWithClassName:DB_TEAM];
    [query whereKey:@"teamCode" equalTo:[NSNumber numberWithInteger:[teamCode integerValue]]];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error) {
            if (objects.count > 0) {
                AVObject *obj = objects[0];
                GGTeamModel *model = [GGTeamModel vulueToObj:obj];
                [self jointWithModel:model];
            }
            else
            {
                [XHToast showBottomWithText:@"房间已不存在"];
                [self popCurrentViewController];
            }
        }
        else
        {
            [XHToast showBottomWithText:@"房间已不存在"];
            [self popCurrentViewController];
        }
    }];
}


- (void)jointWithModel:(GGTeamModel *)model
{
    AVQuery *query = [AVQuery queryWithClassName:DB_TEAM];
    [query whereKey:@"objectId" equalTo:model.objectId];
    [query includeKey:@"type"];
    [query includeKey:@"publisher"];
    [query includeKey:@"participants"];
    [query includeKey:@"game"];
    [query includeKey:@"conversation"];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count>0)
            {
                AVObject *object = objects[0];
                
                if ([[object objectForKey:@"delete"] isEqual:@(YES)])
                {
                    [XHToast showCenterWithText:@"此房间已不存在,请刷新页面"];
                    [self popCurrentViewController];
                }
                else
                {
                    
                    GGTeamModel *model = [GGTeamModel vulueToObj:object];
                    if ([model.outUserId containsObject:[AVUser currentUser].objectId])
                    {
                        [XHToast showCenterWithText:@"您已被队长请出,请勿重复加入"];
                        [self popCurrentViewController];
                    }
                    else
                    {
                        self.teamModel = model;
                        [self goRoomWithModel:model];
                    }
                }
            }
            else
            {
                [XHToast showCenterWithText:@"此房间已不存在,请刷新页面"];
                [self popCurrentViewController];
            }
        }
        else
        {
            //网络错误,重新进入
            [XHToast showCenterWithText:@"查询房间网络错误,重新进入"];
            [self popCurrentViewController];
        }
    }];
    
}

- (void)goRoomWithModel:(GGTeamModel *)model
{
    if([model.publisher.objectId isEqualToString:[AVUser currentUser].objectId] || [model.participants containsObject: [AVUser currentUser].objectId])
    {//自己是队长或者已是群成员--直接加入
        [self initALL];
    }
    else if(![self valueModel:model])
    {
        //都不能进
        
        [self popCurrentViewController];
    }
    else
    {
        //加入别人房间---先查出最新房间信息---再加入----
        [GGTeamModel checkUserCanAddNewTeamWithBlock:^(NSArray * _Nullable uuuu, NSError * _Nullable error) {//检查此用户能否加入
            if (!error && uuuu.count == 0)
            {
                AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:model.objectId];
                [obj addUniqueObject:[AVUser currentUser].objectId forKey:@"participants"];
                [obj addUniqueObject:[AVUser currentUser] forKey:@"participantsUser"];
                obj.fetchWhenSave = YES;
                [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                    if (!error)
                    {
                        self.isJoinTeam = YES;
                        
                        self.teamModel.participants = [obj objectForKey:@"participants"];
                        self.teamModel = model;
                        [self initALL];
                        
                        AVObject *game = [AVObject objectWithClassName:DB_GAME objectId:model.game.objectId];
                        AVObject *conversation = [AVObject objectWithClassName:DB_CONVERSATION objectId:model.groupChatId];
                        AVObject *team = [AVObject objectWithClassName:DB_TEAM objectId:model.objectId];
                        [GGTeamModel saveUserGameHistory:game conversation:conversation team:team];
                    }
                    else
                    {
                        [XHToast showCenterWithText:@"加入房间时服务器发生错误,请返回重试"];
                        [self popCurrentViewController];
                    }
                }];
            }
            else
            {
                [XHToast showCenterWithText:@"您已加入其他房间了,请先退出其他房间方可加入"];
                [self popCurrentViewController];
            }
        }];
    }
}


- (BOOL)valueModel:(GGTeamModel *)model
{
    if ([model.isLock isEqual:@(YES)])
    {//上锁并且自己不在内
        [XHToast showCenterWithText:@"此房间是私密房间,无法加入"];
        return NO;
    }
    if (model.participants.count >= [model.maxnum integerValue])
    {
        [XHToast showCenterWithText:@"此房间人员已满,换一个房间试试"];
        //满员且自己不在内
        return NO;
    }
    return YES;
}

- (void)insertUserHistory
{
    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:0];
    for(NSString *temp in self.teamModel.participants)
    {
        if (![temp isEqualToString:[AVUser currentUser].objectId])
        {
            AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:temp];
            [arr addObject:obj];
        }
    }
    [GGTeamModel saveHistoryTeamerWithUserArr:arr];
}

- (void)initALL
{
    self.shareRoom = [GGRoomManger shareInstance];
    self.shareRoom.currentTeamRoom = self;
    self.shareRoom.teamModel = self.teamModel;
    
    self.loadingView.hidden = YES;
    
    if ([self.teamModel.publisher.objectId isEqualToString:[AVUser currentUser].objectId]) {
        AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:self.teamModel.objectId];
        [obj setObject:@(NO) forKey:@"hidden"];
        [obj save];//房主上线的话要设置房间为可见
    }
    [self insertUserHistory];
    
    [self initData];
    
    [self initUI];
    
    [self initChat];
    
    [self loadData];
    
    [self insertSysteamMessage:ROOMTIPS type:2];
    
    if (self.showRoomSetView)
    {
        [self changeRoomData];
        [self insertSysteamMessage:ROOMCREATETIPS type:3];//房间创建成功
    }
    
     [self initRTCServer];
}


#pragma mark - 悬浮窗.Delegate
/**
 * 需要缓存的信息（必须实现，例如url）
 */
- (NSString *)jp_suspensionCacheMsg
{
    return self.teamModel.objectId;
}

- (BOOL)jp_isHideNavigationBar
{
    return YES;
}

- (void)jp_requestSuspensionLogoImageWithLogoView:(UIImageView *)logoView
{
    NSString *url = [[self.teamModel.publisher objectForKey:@"avatar"] objectForKey:@"url"];
    [logoView setImageWithURL:[NSURL URLWithString:url] placeholderImage:GGDefault_User_Head];
}

- (void)takeUpRoom
{
    _isShowFloatWindow = YES;
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - Data
- (void)initData
{
    self.chatDataArr = [NSMutableArray arrayWithCapacity:0];
    
    if ([self.teamModel.publisher.objectId isEqualToString:[AVUser currentUser].objectId])
    {
        self.isAdmin = YES;
    }
    else
    {
        self.isAdmin = NO;
    }
}


- (void)saveCurrentConversationInfoIfExists
{
    NSString *conversationId = self.conversation.conversationId;
    if (conversationId) {
        [LCCKConversationService sharedInstance].currentConversationId = conversationId;
    }
    
    if (_conversation) {
        [LCCKConversationService sharedInstance].currentConversation = _conversation;
    }
}

- (void)clearCurrentConversationInfo
{
    _isShowFloatWindow = NO;
    [LCCKConversationService sharedInstance].currentConversationId = nil;
    [LCCKConversationService sharedInstance].currentConversation = nil;
    self.shareRoom.currentTeamRoom = nil;
    self.shareRoom.teamModel = nil;
}

- (void)initChat
{
    self.client = [LCChatKit sharedInstance].client;
    
    [GGNotificationCenter addObserver:self selector:@selector(reciveMessage:) name:LCCKNotificationMessageReceived object:nil];
    [GGNotificationCenter addObserver:self selector:@selector(kickedClientId:) name:LCCKNotificationCurrentConversationInvalided object:nil];
    [GGNotificationCenter addObserver:self selector:@selector(memberAdded:) name:GGLCCKNotificationMessageMemberAdded object:nil];
    [GGNotificationCenter addObserver:self selector:@selector(memberRemoverd:) name:GGLCCKNotificationMessageMemberRemoved object:nil];
    [GGNotificationCenter addObserver:self selector:@selector(conversationUpdate:) name:LCCKNotificationConversationUpdated object:nil];
    
    
    AVIMConversationQuery *query = [self.client conversationQuery];
    [query getConversationById:self.teamModel.groupChatId callback:^(AVIMConversation *conversation, NSError *error) {
        // 查询对话中最后 20 条消息
        self.conversation = conversation;
        [self saveCurrentConversationInfoIfExists];//保存当前会话
        [self.conversation readInBackground];
        [self joinTeam];
        [conversation queryMessagesWithLimit:20 callback:^(NSArray *objects, NSError *error) {
            for(AVIMTypedMessage *temp in objects)
            {
                if (![temp.attributes objectForKey:@"isWill"]) {
                    GGChatModel *model = [GGChatModel valueToModel:temp conversation:conversation];
                    [self.chatDataArr addObject:model];
                }
            }
            [self.tableView reloadData];
            [self scrollTableToFoot:YES];
            if ([self.teamModel.publisher.objectId isEqualToString:[AVUser currentUser].objectId])
            {
                [self sendWillMessage];//房主下线的话此房间将被隐藏
            }
        }];
    }];
}


- (void)sendWillMessage
{
    AVIMMessageOption *option = [[AVIMMessageOption alloc] init];
    option.will = YES;
    
    AVIMMessage *willMessage = [AVIMTextMessage messageWithText:@"用户已下线" attributes:@{@"isWill":@(YES),@"teamId":self.teamModel.objectId,@"isPublish":@(YES)}];
    [self.conversation sendMessage:willMessage option:option callback:^(BOOL succeeded, NSError * _Nullable error) {
        if (succeeded) {
            NSLog(@"Will message has been sent.");
        }
    }];
}

- (void)joinTeam
{
    if (self.isJoinTeam)
    {
        [self.conversation joinWithCallback:^(BOOL succeeded, NSError * _Nullable error) {
            if (error)
            {
                [XHToast showCenterWithText:@"服务器发生错误,请退出重试"];
            }
        }];
    }
}

- (void)initRTCServer
{
    self.engine = [AgoraRtcEngineKit sharedEngineWithAppId:GGAUDIOAPPID delegate:self];   
    [self.engine setDefaultAudioRouteToSpeakerphone:YES];;
    [self.engine setEnableSpeakerphone:YES];
    [self.engine enableAudioVolumeIndication:500 smooth:3];//打开此行可回调到谁在说话...
    NSUInteger userid = [[[AVUser currentUser] objectForKey:@"userId"] integerValue];
    NSDictionary *dicParameters = [NSDictionary dictionaryWithObject:[NSString stringWithFormat:@"%@",self.teamModel.teamCode]forKey:@"channelName"];
    [AVCloud callFunctionInBackground:@"getDymicToken"
                       withParameters:dicParameters
                                block:^(id object, NSError *error) {
                                    if(error == nil){
                                        // 处理结果
                                        NSString *token = (NSString *)object;
                                        self.engineToken = token;
                                        [self.engine joinChannelByToken:token channelId:[NSString stringWithFormat:@"%@",self.teamModel.teamCode] info:nil uid:userid joinSuccess:nil];
                                    } else {
                                        // 处理报错
                                        [XHToast showBottomWithText:@"加入房间失败,请返回后重试"];
                                    }
                                }];
    
    self.audioBreakTips = [[GGAudioBreakTips alloc]initWithFrame:CGRectMake(0, self.chatTool.frame.origin.y - 40, kScreenW, 40)];
    [self.view addSubview:self.audioBreakTips];
    self.audioBreakTips.hidden = YES;
    @WeakObj(self);
    self.audioBreakTips.reConnectAudioBlock = ^(UIButton *btn) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                btn.enabled = YES;
        });
        selfWeak.audioBreakTips.hidden = YES;
        [selfWeak.engine joinChannelByToken:selfWeak.engineToken channelId:[NSString stringWithFormat:@"%@",selfWeak.teamModel.teamCode] info:nil uid:userid joinSuccess:nil];
    };
}


#pragma mark - UI
- (void)initUI
{
    
    self.headerView = [[GGRoomHeadView alloc]initWithFrame:CGRectMake(0, -20, kScreenW, 105)];
    [self.view addSubview:self.headerView];
    [self.headerView.closeButton addTarget:self action:@selector(showCloseMenu:) forControlEvents:UIControlEventTouchUpInside];
    if (IS_IPHONE_X) {
        self.headerView.mj_y = 0;
    }
    
    UIView *dd = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.headerView.frame), kScreenW, 35)];
    dd.backgroundColor = [UIColor whiteColor];
    dd.alpha = 0.1;
    [self.view addSubview:dd];
    
    self.noticeView = [[GGNoticeView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.headerView.frame), kScreenW, 35)];
    [self.view addSubview:self.noticeView];
    
    if (self.isAdmin)
    {
        UITapGestureRecognizer *tapheader = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(changeRoomData)];
        [self.headerView addGestureRecognizer:tapheader];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(changeRoomData)];
        [self.noticeView addGestureRecognizer:tap];
    }
    
    NSInteger maxNum = [self.teamModel.maxnum integerValue];
    if (maxNum < 4)    maxNum = 4;
    NSInteger index = maxNum - 4 == 0?1:2;
    
    self.userListView = [[GGUserListView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.noticeView.frame) + 10, kScreenW, 20 + 85 * index) withTeamModel:self.teamModel];
    self.userListView.userInteractionEnabled = YES;
    self.userListView.v_delegate = self;
    [self.view addSubview:self.userListView];
    
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.userListView.frame), kScreenW, kScreenH  - CGRectGetMaxY(self.userListView.frame) - 49) style:UITableViewStylePlain];
    self.tableView.backgroundColor = [UIColor clearColor];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.tableFooterView = [UIView new];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [self.view addSubview:self.tableView];
    
    self.chatTool = [[GGChatTool alloc]initWithFrame:CGRectMake(0, kScreenH - 55, kScreenW, 55)];
    self.chatTool.backgroundColor = [UIColor clearColor];
    self.chatTool.v_delegate = self;
    [self.view addSubview:self.chatTool];
    if (IS_IPHONE_X) {
        self.chatTool.mj_y = kScreenH - 65;
        self.tableView.mj_h = kScreenH  - CGRectGetMaxY(self.userListView.frame) - 65;
    }
    
    self.shareToPC = [SJUIButtonFactory buttonWithImageName:@"pic_fas" target:self sel:@selector(shareToPCClick) tag:88];
    self.shareToPC.frame = CGRectMake(kScreenW - 85, self.chatTool.frame.origin.y - 70 - 50, 85, 35);
    [self.view addSubview:self.shareToPC];
    
    self.invitaFriendBtn = [SJUIButtonFactory buttonWithImageName:@"icon_huhuan" target:self sel:@selector(invitaFriendBtnClick) tag:88];
    self.invitaFriendBtn.frame = CGRectMake(kScreenW - 85, self.chatTool.frame.origin.y - 70, 85, 35);
    [self.view addSubview:self.invitaFriendBtn];
}


-(void)initSendMessageView
{
    CGRect rect = CGRectMake(0, 0, kScreenW, 60);
    if (!self.sendMessageTool)
    {
        self.sendMessageTool = [[GGSendMessageView alloc] initWithFrame:rect];
        @WeakObj(self);
        self.sendMessageTool.senderClickedBlock = ^(GGSendMessageView *messageView, UIButton *button) {
            [selfWeak.zh_popupController dismiss];
            [selfWeak sendSysMessage:messageView.textField.text];
        };
    }
    self.zh_popupController = [zhPopupController new];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    [self.zh_popupController presentContentView:self.sendMessageTool duration:0.25 springAnimated:NO];
}



#pragma mark - Method

- (void)reloadHeaderTeamData
{
    [self.headerView setDataWithTeamModel:self.teamModel];
    self.noticeView.contentLabel.text = self.teamModel.desc.length == 0?@"队长没有设置公告内容":self.teamModel.desc;
}

- (void)loadData
{
    [self reloadHeaderTeamData];
    
    NSInteger maxNum = [self.teamModel.maxnum integerValue];
    [self.userListView setUserListDataWithArray:self.teamModel.participants maxNum:maxNum];
}

- (void)scrollTableToFoot:(BOOL)animated
{
    NSInteger s = [self.tableView numberOfSections];  //有多少组
    if (s<1) return;  //无数据时不执行 要不会crash
    NSInteger r = [self.tableView numberOfRowsInSection:s-1]; //最后一组有多少行
    if (r<1) return;
    NSIndexPath *ip = [NSIndexPath indexPathForRow:r-1 inSection:s-1];  //取最后一行数据
    [self.tableView scrollToRowAtIndexPath:ip atScrollPosition:UITableViewScrollPositionBottom animated:animated]; //滚动到最后一行
}

- (void)showSteamIDSetView
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 200);
    NSString *steamId = [[AVUser currentUser] objectForKey:@"steamID"];
    GGSetSteamIDView *view = [[GGSetSteamIDView alloc]initWithFrame:rect withSteamId:steamId];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    view.closeBtnClick = ^(GGSetSteamIDView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    
    view.sureBtnClick = ^(GGSetSteamIDView *view, UIButton *button) {
        [self sendSysMessage:[NSString stringWithFormat:@"%@", [[AVUser currentUser] objectForKey:@"steamID"]]];
    };
}

//私聊
- (void)pushToUserChatWithUser:(AVUser *)user
{
    [[LCChatKit sharedInstance].client createConversationWithName:@"" clientIds:@[user.objectId] attributes:GGPRIVATETYPEATTRDIC options:AVIMConversationOptionUnique temporaryTTL:0 callback:^(AVIMConversation * _Nullable conversation, NSError * _Nullable error) {
        
        GGSingleConversationViewController *conversationViewController = [[GGSingleConversationViewController alloc] initWithConversationId:conversation.conversationId];
        _isShowFloatWindow = NO;
        [self.navigationController pushViewController:conversationViewController animated:YES];
    }];
}
//踢出群


- (void)showUserCard:(AVUser *)user
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 280);
    if ([[AVUser currentUser].objectId isEqualToString:user.objectId])
    {
        rect = CGRectMake(10, 0, kScreenW - 20, 230);
    }
    GGUserInfoCardView *view = [[GGUserInfoCardView alloc]initWithFrame:rect user:user teamdModel:self.teamModel];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeCenter;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    view.closeBtnClick = ^(GGUserInfoCardView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    
    view.sendMegClick = ^(GGUserInfoCardView *userCardView, AVUser *user) {
         [self.zh_popupController dismiss];
        [self pushToUserChatWithUser:user];
    };
    view.getOutRoomClick = ^(GGUserInfoCardView *userCardView, AVUser *user) {
        
          [self.zh_popupController dismiss];
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"确认操作" message:@"踢出后,该用户将永远不能加入此房间,请确认是否踢出此用户?" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"确认踢出" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            [self.conversation removeMembersWithClientIds:@[user.objectId] callback:^(BOOL succeeded, NSError *error) {
                if (succeeded)
                {
                  
                    [XHToast showCenterWithText:@"已成功踢出该成员"];
                    [GGTeamModel deleteUserGameHistoryWithTeamObjectId:self.teamModel.objectId userId:user.objectId];
                }
            }];
        }];
        UIAlertAction *action3 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            NSLog(@"取消");
        }];
        [alert addAction:action1];
        [alert addAction:action3];
        [self presentViewController:alert animated:YES completion:nil];
       
    };
}

- (void)showCloseMenu:(UIButton *)btn
{
    NSString *lockImage = [self.teamModel.isLock isEqual:@(1)]?@"icon_jiesuo":@"icon_shangsuo";
    NSArray *imageArr = @[@"icon_shouqi", @"icon_guanbifj", lockImage];
    NSString *lockTitle = [self.teamModel.isLock isEqual:@(1)]?@"设为公开":@"设为私密";
    NSArray *titleArr;
    if (self.isAdmin)
    {//解散房间 /// 退出房间
        titleArr = @[@"收起房间", @"解散房间", lockTitle];
    }
    else
    {
        titleArr = @[@"收起房间", @"退出房间"];
    }
    
    
    [YBPopupMenu showRelyOnView:btn titles:titleArr icons:imageArr menuWidth:130 otherSettings:^(YBPopupMenu *popupMenu) {
        popupMenu.priorityDirection = YBPopupMenuPriorityDirectionBottom;
        popupMenu.borderWidth = 1;
        popupMenu.borderColor = GGLine_Color;
        popupMenu.delegate = self;
        popupMenu.fontSize = 14;
    }];
}


- (void)changeRoomData
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 250);
    
    GGRoomEditView *view = [[GGRoomEditView alloc]initWithFrame:rect teamModel:self.teamModel];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    view.sureBtnClick = ^(GGRoomEditView *editView, UIButton *button, GGTeamModel *teamModel) {
        self.teamModel.title = teamModel.title;
        self.teamModel.desc = teamModel.desc;
        [self reloadHeaderTeamData];
        [self insertSysteamMessage:ROOMDATACHANGED type:5];//常规提示
    };
    view.closeBtnClick = ^(GGRoomEditView *editView) {
        [self.zh_popupController dismiss];
    };
    
}

- (void)shareToNeibu
{

    GGConversionListViewController *ff = [GGConversionListViewController new];
    ff.title = @"发送给好友";
    NSString *info = [NSString stringWithFormat:@"%@",[self.teamModel.type objectForKey:@"name"]];
    NSString *url = [[self.teamModel.publisher objectForKey: @"avatar"] objectForKey:@"url"];
    NSDictionary *dic = @{@"title":self.teamModel.title,
                          @"avatar": url.length == 0?@"":url,
                          @"game":[self.teamModel.game objectForKey: @"gameName"],
                          @"info":info,
                          @"titleColor":[self.teamModel.game objectForKey: @"titleColor"],
                          @"backColor":[self.teamModel.game objectForKey: @"backColor"],
                          @"teamId":self.teamModel.objectId
                          };
    ff.messageAttr = dic;
    ff.title = @"最近联系人";
    _isShowFloatWindow = NO;
    [self.navigationController pushViewController:ff animated:YES];
}

//邀请朋友
- (void)invitaFriendBtnClick
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 210);
    
    GGShareRoomView *view = [[GGShareRoomView alloc]initWithFrame:rect];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    view.closeBtnClick = ^(GGPopupView *view) {
        [self.zh_popupController dismiss];
    };
    view.sharePlatFormClick = ^(GGShareRoomView *view, NSInteger index) {
        [self.zh_popupController dismiss];
        NSString *key = [GGEncryTool xorEncryptorDecrypt:[NSString stringWithFormat:@"%@",self.teamModel.teamCode]];
        NSString *url = [NSString stringWithFormat:@"%@/%@",GGHOST_URL,key];
        NSString *gameName = [self.teamModel.game objectForKey:@"gameName"];
        NSString *sharetitle = [NSString stringWithFormat:@"%@:%@",gameName,self.teamModel.title];
        NSArray *imageArr = [[[AVUser currentUser] objectForKey:@"avatar"] objectForKey:@"url"];
        GGShareModel *model = [GGShareModel new];
        model.title = sharetitle;
        model.descText = [NSString stringWithFormat:@"%@,点击立即加入我的房间",self.teamModel.desc.length == 0?sharetitle:self.teamModel.desc];
        model.shareUrl = [NSURL URLWithString:url];
        model.imageArray = imageArr;
        
        switch (index)
        {
            case 0:
                //复制链接
                //
                [self pasteRoomUrl:url];
                break;
            case 1:
            //app内
                [self shareToNeibu];
                break;
            case 2:
            //qq
                [self shareWithPlatForm:SSDKPlatformTypeQQ shareModel:model];
                break;
            case 3:
            //微信//好友
                [self shareWithPlatForm:SSDKPlatformSubTypeWechatSession shareModel:model];
                break;
            case 4:
             //朋友圈
                [self shareWithPlatForm:SSDKPlatformSubTypeWechatTimeline shareModel:model];
                break;
            case 100:
                //置顶
                [self setCurrentTeamToTop];
                break;
                
            default:
                break;
        }
    };
}


- (void)shareToPCClick
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 270);
    NSString *key = [GGEncryTool xorEncryptorDecrypt:[NSString stringWithFormat:@"%@",self.teamModel.teamCode]];
    NSString *url = [NSString stringWithFormat:@"%@/%@",GGHOST_URL,key];
    GGShareToPCView *view = [[GGShareToPCView alloc]initWithFrame:rect withUrl:url];
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    view.closeBtnClick = ^(GGShareToPCView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    
    view.sureBtnClick = ^(GGShareToPCView *view, UIButton *button) {
        [self pasteRoomUrl:url];
    };
}



- (void)shareWithPlatForm:(SSDKPlatformType)platform shareModel:(GGShareModel *)shareModel
{
    NSMutableDictionary *shareParams = [NSMutableDictionary dictionary];
    [shareParams SSDKSetupShareParamsByText:shareModel.descText
                                     images:shareModel.imageArray //传入要分享的图片
                                        url:shareModel.shareUrl
                                      title:shareModel.title
                                       type:SSDKContentTypeAuto];
    
    [ShareSDK share:platform //传入分享的平台类型
         parameters:shareParams
     onStateChanged:^(SSDKResponseState state, NSDictionary *userData, SSDKContentEntity *contentEntity, NSError *error) { // 回调处理....
         
     }];
}

- (void)pasteRoomUrl:(NSString *)url
{
    NSString *str = [NSString stringWithFormat:@"%@,%@",@"我正在使用飞聊语音,无需注册打开后方链接一键加入我的开黑语音房间",url];
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = str;
    [XHToast showCenterWithText:@"复制成功"];
}

- (void)setCurrentTeamToTop
{
    AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:self.teamModel.objectId];
    [obj setObject:@(1) forKey:@"isTop"];
    [obj setObject:[NSDate date] forKey:@"sortDate"];
     obj.fetchWhenSave = YES;
    [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        if (succeeded)
        {
            [self insertSysteamMessage:ROOMTOPTIPS type:5];//常规提示
        }
        else
        {
            [XHToast showCenterWithText:@"服务器错误,请重试"];
        }
    }];
}

- (void)sendSysMessage:(NSString *)text
{
    if (text.length > 0)
    {
        NSString *finalText = [NSString stringWithFormat:@"%@:%@",[AVUser currentUser].username,text];
        AVIMMessage *message = [AVIMTextMessage messageWithText:finalText attributes:nil];
        [self.conversation sendMessage:message callback:^(BOOL succeeded, NSError * _Nullable error) {
            if (succeeded)
            {
                [self insertOneMessageAndRefreshTable:(AVIMTypedMessage *)message];
            }
            else if(error.code == 4401)//群已不存在
            {
                [XHToast showCenterWithText:@"队长已解散房间,无法发送消息"];
            }
            else
            {
                [XHToast showCenterWithText:@"发送失败,请重试"];
            }
        }];
    }
    else
    {
        [XHToast showCenterWithText:@"你不能发不空消息"];
    }
}

- (void)insertOneMessageAndRefreshTable:(AVIMTypedMessage *)message
{
    GGChatModel *model = [GGChatModel valueToModel:message conversation:self.conversation];
    [self insertMessageAndReloadData:model];
}

- (void)insertMessageAndReloadData:(GGChatModel *)model
{
    [self.chatDataArr addObject:model];
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.chatDataArr.count - 1 inSection:0];
    [self.tableView insertRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationTop];
    [self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionTop animated:YES];
}

/**
 插入系统消息

 @param text
 @param type
 */
- (void)insertSysteamMessage:(NSString *)text type:(NSInteger)type
{
    GGChatModel *model = [[GGChatModel alloc]init];
    model.text = text;
    model.type = type;
    [self insertMessageAndReloadData:model];
}

- (void)insertSysteamMessageWithUser:(NSString *)userObjectId text:(NSString *)text type:(NSInteger)type
{
    GGChatModel *model = [[GGChatModel alloc]init];
    model.text = text;
    model.type = type;
    model.userObjectId = userObjectId;
    [self insertMessageAndReloadData:model];
}


- (void)popCurrentViewController
{
   // JPSEInstance.suspensionView = nil;
    [self clearCurrentConversationInfo];
    
    _isShowFloatWindow = NO;
    [self.navigationController popViewControllerAnimated:YES];
    if (self.engine) {
        [self.engine leaveChannel:nil];
    }
}




#pragma mark - Menu.Delegate

- (void)quitTeam
{
    if ([self.teamModel.publisher.objectId isEqualToString:[AVUser currentUser].objectId])
    {
        //队长要退了...//清退所有人-----删除此team
        [self.conversation removeMembersWithClientIds:self.teamModel.participants callback:^(BOOL succeeded, NSError *error) {
            if (succeeded) {
                NSLog(@"所有人都被踢出");
            }
        }];
        [XHToast showBottomWithText:@"您已解散了该房间"];
        [GGTeamModel deleteTeamWithTeamObjectId:self.teamModel.objectId];
    }
    else
    {
        [self.conversation quitWithCallback:^(BOOL succeeded, NSError * _Nullable error) {
            if (succeeded)
            {
                NSLog(@"成功退出IM房间");
            }
        }];
        [GGTeamModel deleteUserGameHistoryWithTeamObjectId:self.teamModel.objectId userId:nil];
    }
    [self.engine leaveChannel:nil];
    _isShowFloatWindow = NO;
    [self clearCurrentConversationInfo];
}

- (void)ybPopupMenu:(YBPopupMenu *)ybPopupMenu didSelectedAtIndex:(NSInteger)index
{
    if (index == 0)
    {
        [self takeUpRoom];
    }
    if (index == 1)
    {
        
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"是否退出此房间？" message:@"退出之后您将不会收到该房间的消息，队长退出后此房间将会被全部解散" preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"确认退出" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            [self quitTeam];
            [self popCurrentViewController];
        }];
        UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        
        [alert addAction:action1];
        [alert addAction:action2];
        [self presentViewController:alert animated:YES completion:nil];
        
       
    }
    if (index == 2)
    {
        
        AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:self.teamModel.objectId];
        [obj setObject:[self.teamModel.isLock isEqual:@1]?@(NO):@(YES) forKey:@"isLock"];
        [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
            if (succeeded)
            {
                self.teamModel.isLock = [obj objectForKey:@"isLock"];
                //[XHToast showBottomWithText:[self.teamModel.isLock isEqual:@1]?@"当前房间为隐私状态":@"当前房间为开放状态"];
                NSString *text = [self.teamModel.isLock isEqual:@1]?@"当前房间为隐私状态":@"当前房间为开放状态";
                [self insertSysteamMessage:text type:5];//常规提示
                if(![self.teamModel.isLock isEqual:@1]){
                    [self insertSysteamMessage:@"已将房间设为公开，任何人均可加入" type:5];//常规提示2
                }
                [self reloadHeaderTeamData];
            }
        }];
    }
}

#pragma mark - userList.Delegate
- (void)clickUser:(AVUser *)user
{
    if (!user)
    {
        [self invitaFriendBtnClick];
    }
    else
    {
        //显示用户详情
        [self showUserCard:user];
    }
}

#pragma mark - table.Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.chatDataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    GGChatModel *model = self.chatDataArr[indexPath.row];
    NSString *iden = [NSString stringWithFormat:@"chatIden%ld",(long)model.type];
    GGChatTeamRoomCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGChatTeamRoomCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:iden];
    }
    cell.backgroundColor = [UIColor clearColor];
    [cell setSelectionStyle:UITableViewCellSelectionStyleNone];
    
    cell.invitaFriendClick = ^(GGChatTeamRoomCell *view) {
        [self invitaFriendBtnClick];
    };
    cell.userClick = ^(GGChatTeamRoomCell *view, NSString *objectId) {
        if (objectId)
        {
            AVUser *user = (AVUser *)[AVObject objectWithClassName:DB_USER objectId:objectId];
            [self showUserCard:user];
        }
    };
    [cell setModel:model];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    GGChatModel *model = self.chatDataArr[indexPath.row];
    return [GGChatTeamRoomCell valiateHeightWithModel:model];
}

#pragma mark - chat.Notifation
- (void)conversationUpdate:(NSNotification *)note
{//此方法暂时无用
   AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:self.teamModel.objectId];
    [obj fetchInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
        self.teamModel.title = [object objectForKey:@"title"];
        self.teamModel.desc = [object objectForKey:@"desc"];
        [self reloadHeaderTeamData];
    }];
}

- (void)memberAdded:(NSNotification *)note
{
    NSDictionary *userInfo = note.object;
    AVIMConversation *conversation = [userInfo objectForKey:LCCKMessageNotifacationUserInfoConversationKey];
    NSArray *clientIds = [userInfo objectForKey:@"clientIds"];
    [self conversation:conversation membersAdded:clientIds byClientId:nil];
}

- (void)memberRemoverd:(NSNotification *)note
{
    NSDictionary *userInfo = note.object;
    AVIMConversation *conversation = [userInfo objectForKey:LCCKMessageNotifacationUserInfoConversationKey];
    NSArray *clientIds = [userInfo objectForKey:@"clientIds"];
    [self conversation:conversation membersRemoved:clientIds byClientId:nil];
}

- (void)kickedClientId:(NSNotification *)note
{
    NSString *clientId = note.object;
    NSLog(@"%@",clientId);
    if (![clientId isEqualToString:[AVUser currentUser].objectId])
    {
        [XHToast showCenterWithText:@"您已被队长请出房间"];
    }
    [self.engine leaveChannel:nil];
    [self popCurrentViewController];
}

- (void)reciveMessage:(NSNotification *)note
{
  //  [self.userListView queryOffLine];
    NSDictionary *userInfo = note.object;
    AVIMConversation *conversation = [userInfo objectForKey:LCCKMessageNotifacationUserInfoConversationKey];
    NSArray *messages = [userInfo objectForKey:LCCKDidReceiveMessagesUserInfoMessagesKey];
    
    [self.conversation readInBackground];
    
    if ([conversation.conversationId isEqualToString:self.conversation.conversationId]) {
        for(AVIMTextMessage *message in messages)
        {
            if(![message.attributes objectForKey:@"isWill"])
            {
                [self insertOneMessageAndRefreshTable:message];
            }
        }
    }
}


/**
 可以优化为拿到人员变化通知后直接刷新partarints字段,这样跟精准.当前这么做可能会导致不精准

 @param conversation
 @param clientIds
 @param clientId
 */
- (void)conversation:(AVIMConversation *)conversation membersAdded:(NSArray<NSString *> *)clientIds byClientId:(NSString *)clientId
{
    //clientIds加入到会话中了
    if ([conversation.conversationId isEqualToString:self.conversation.conversationId])
    {
        NSMutableArray *arr = [NSMutableArray arrayWithArray:self.teamModel.participants];
        for(NSString *uuu in clientIds)
        {
            AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:uuu];
            [obj fetchInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
                if (!error)
                {
                    [self insertSysteamMessageWithUser:object.objectId text:[NSString stringWithFormat:@"%@:已加入房间",[object objectForKey:@"username"]] type:4];
                }
            }];
            if (![arr containsObject:uuu])
            {
                [arr addObject:uuu];
                 [self.userListView membersAdd:uuu];
            }
        }
        self.teamModel.participants = arr;
        [self insertUserHistory];
    }
}


- (void)conversation:(AVIMConversation *)conversation membersRemoved:(NSArray<NSString *> * _Nullable)clientIds byClientId:(NSString * _Nullable)clientId
{
    if ([conversation.conversationId isEqualToString:self.conversation.conversationId])
    {
         NSMutableArray *arr = [NSMutableArray arrayWithArray:self.teamModel.participants];
        
        for(NSString *uuu in clientIds)
        {
            AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:uuu];
            [obj fetchInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
                if (!error)
                {
                    [self insertSysteamMessageWithUser:object.objectId text:[NSString stringWithFormat:@"%@:已离开房间",[object objectForKey:@"username"]] type:4];
                }
            }];
            [arr removeObject:uuu];
            [self.userListView membersRemove:uuu];
        }
        self.teamModel.participants = arr;
    }
}



#pragma mark - rtcDeletgate.start
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurWarning:(AgoraWarningCode)warningCode
{
    //该回调方法表示 SDK 运行时出现了（网络或媒体相关的）警告。通常情况下，SDK 上报的警告信息应用程序可以忽略，SDK 会自动恢复。比如和服务器失去连接时，SDK 可能会上报 AgoraRtc_Error_OpenChannelTimeout(106) 警告，同时自动尝试重连。
    
}


- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurError:(AgoraErrorCode)errorCode
{
    //该回调方法表示 SDK 运行时出现了（网络或媒体相关的）错误。通常情况下，SDK 上报的错误意味着 SDK 无法自动恢复，需要应用程序干预或提示用户。 比如启动通话失败时，SDK 会上报 AgoraRtc_Error_StartCall(1002) 错误。应用程序可以提示用户启动通话失败，并调用 leaveChannel() 退出房间。
    NSLog(@"语音log:发生错误,%ld",(long)errorCode);
    //    [XHToast showCenterWithText:@"加入语音房间失败,请重试"];
    //    [engine leaveChannel:^(AgoraChannelStats * _Nonnull stat) {
    //
    //    }];
    //self.audioBreakTips.hidden = YES;
    if (errorCode == AgoraErrorCodeStartCall) {
        [self.engine leaveChannel:nil];
        [XHToast showCenterWithText:@"进入房间语音失败,请重试"];
    }
    if (errorCode == AgoraErrorCodeNetDown) {
        //网络发生错误
        self.audioBreakTips.hidden = NO;
    }
}

- (void)rtcEngineConnectionDidLost:(AgoraRtcEngineKit *)engine
{
    self.audioBreakTips.hidden = NO;
}


- (void)rtcEngine:(AgoraRtcEngineKit *)engine didJoinChannel:(NSString*)channel withUid:(NSUInteger)uid elapsed:(NSInteger) elapsed;
{
    //该回调方法表示该客户端成功加入了指定的房间。同 joinChannelByToken() API 的 joinSuccessBlock 回调。
    self.audioBreakTips.hidden = YES;
    [self.userListView upDateUserOnlineStatus:[NSNumber numberWithUnsignedInteger:uid] offline:NO];
    NSLog(@"语音log:%luuid === @我自己加入语音",(unsigned long)uid);
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine didLeaveChannelWithStats:(AgoraChannelStats *)stats
{
    NSLog(@"语音log:成功退出语音房间");
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine reportAudioVolumeIndicationOfSpeakers:
(NSArray*)speakers totalVolume:(NSInteger)totalVolume
{
    //同 audioVolumeIndicationBlock。提示谁在说话及其音量，默认禁用。可通过 enableAudioVolumeIndication 方法设置。
    //spekaer包括:uid: 说话者的用户IDvolume: 说话者的音量（0~255）
    for(AgoraRtcAudioVolumeInfo *info in speakers)
    {
        //NSLog(@"语音log:正在说话的人:%lu",(unsigned long)info.uid);
        if (info.uid != 0) {
            [self.userListView upDateUserSpeakerWithUid:[NSNumber numberWithUnsignedInteger:info.uid]];
        }
    }
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine didJoinedOfUid:
(NSUInteger)uid elapsed:(NSInteger)elapsed
{
    //同 userJoinedBlock。提示有用户加入了房间。如果该客户端加入房间时已经有人在房间中，SDK也会向应用程序上报这些已在房间中的用户。
    [self.userListView upDateUserOnlineStatus:[NSNumber numberWithUnsignedInteger:uid] offline:NO];
    NSLog(@"语音log:userid === %lu已加入语音群",(unsigned long)uid);
    [self userOffline:uid typeString:@"语音已上线"];
    
    NSNumber  *userId=  [[AVUser currentUser]objectForKey:@"userId"] ;
    if (![userId isEqualToNumber:[NSNumber numberWithUnsignedInteger:uid]]) {
        AudioServicesPlayAlertSoundWithCompletion(1008, ^{
            NSLog(@"---------");
        });
    }
}



- (void)rtcEngine:(AgoraRtcEngineKit *)engine didOfflineOfUid:
(NSUInteger)uid reason:(AgoraUserOfflineReason)reason
{
    //同 userOfflineBlock。提示有用户离开了房间（或掉线）。SDK 判断用户离开房间（或掉线）的依据是超时: 在一定时间内（15 秒）没有收到对方的任何数据包，判定为对方掉线。 在网络较差的情况下，可能会有误报。建议可靠的掉线检测应该由信令来做。
    NSLog(@"语音log:userid === %lu用户离开了这个房间",(unsigned long)uid);
    //掉线了
    [self.userListView upDateUserOnlineStatus:[NSNumber numberWithUnsignedInteger:uid] offline:YES];
    [self userOffline:uid typeString:@"语音已离线"];
}

- (void)userOffline:(NSUInteger)uid typeString:(NSString *)str
{
    
    AVQuery *query = [AVQuery queryWithClassName:DB_USER];
    [query whereKey:@"userId" equalTo:[NSNumber numberWithUnsignedInteger:uid]];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error) {
            if (objects.count>0) {
                AVObject *object = objects[0];
                [self insertSysteamMessageWithUser:object.objectId text:[NSString stringWithFormat:@"%@:%@",[object objectForKey:@"username"],str] type:4];
            }
        }
    }];
}

- (void)rtcEngine:(AgoraRtcEngineKit *)engine
    didAudioMuted:(BOOL)muted byUid:(NSUInteger)uid
{
    NSLog(@"语音log:userid === %lu用户%@了音频静音",(unsigned long)uid,muted?@"开启":@"关闭");
    [self.userListView upDateUserMacStatus:[NSNumber numberWithUnsignedInteger:uid] isMac:!muted];
}

- (void)rtcEngineConnectionDidBanned:(AgoraRtcEngineKit * _Nonnull)engine
{
    NSLog(@"语音log:多次连接不成功,已掉线");
}
#pragma mark - rtx.end


#pragma mark - ToolView.Delegate
- (void)clickTool:(NSInteger)index button:(UIButton *)button
{
    switch (index) {
        case 1://发文字
            [self initSendMessageView];
            break;
        case 2://扬声器静音
            [XHToast showCenterWithText:button.selected?@"扬声器已静音":@"扬声器已开启"];
            [self.engine muteAllRemoteAudioStreams:button.selected];
            
            break;
        case 3://发送ID到公屏
            if ([[[AVUser currentUser] objectForKey:@"steamID"] length] > 0)
            {
                [self sendSysMessage:[NSString stringWithFormat:@"%@", [[AVUser currentUser] objectForKey:@"steamID"]]];
            }
            else
            {
                //提示设置ID
                [self showSteamIDSetView];
            }
            
            break;
        case 4://关闭麦克风
             [self.engine muteLocalAudioStream:button.selected];
            
             [self.userListView upDateUserMacStatus:[[AVUser currentUser] objectForKey:@"userId"] isMac:!button.selected];
            break;
            
        default:
            break;
    }
}

//长按调出设置页面
- (void)longPressSendSteamID
{
   // [self showSteamIDSetView];
}




@end
