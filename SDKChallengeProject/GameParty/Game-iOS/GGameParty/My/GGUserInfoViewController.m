//
//  GGUserInfoViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserInfoViewController.h"
#import "GGUserGoodCell.h"
#import "GGTeamTableViewCell.h"
#import "GGTeamModel.h"
@interface GGUserInfoViewController ()<UICollectionViewDelegate,
UICollectionViewDataSource>

{
    BOOL _canGood;
    NSString *_goodObjectId;
}
@property (nonatomic, strong) GGTeamModel *teamModel;

@property (nonatomic,assign)BOOL tuandui;
@property (nonatomic,assign)BOOL zuijia;
@property (nonatomic,assign)BOOL taidu;
@property (nonatomic,assign)BOOL leyu;

@property (nonatomic,strong)AVUser *user;

@property (nonatomic,strong)AVObject *goodObject;

@property (nonatomic,strong)UIImageView *backImageView;
@property (nonatomic,strong)UIImageView *headImageView;

@property (nonatomic,strong)UIImageView *sexImageView;

@property (nonatomic,strong)UILabel *ageLabel;

@property (nonatomic,strong)UILabel *steamId;

@property (nonatomic,strong)UIView *backView;
@property (nonatomic,strong)UILabel *noGoodLabel;
@property (nonatomic,strong)UILabel *nameLabel;

@property (nonatomic,strong)UICollectionView *collectionView;

@property (nonatomic,strong)UIButton *sendMsgBtn;

@property (nonatomic,strong)UIButton *followBtn;

@property (nonatomic,strong)UIButton *reportBtn;


@property (nonatomic,strong)NSArray  *goodArr;



@end

@implementation GGUserInfoViewController

- (UIStatusBarStyle)preferredStatusBarStyle
{
    return UIStatusBarStyleLightContent;
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    [self actionClose];
}


- (void)actionClose
{
    BOOL isMy = [[AVUser currentUser].objectId isEqualToString:self.user.objectId];
    if (isMy == NO && _canGood && (self.taidu != NO || self.zuijia != NO || self.tuandui != NO || self.leyu != NO))
    {
        AVObject *obj;
        if (_goodObjectId) {
            obj = [AVObject objectWithClassName:DB_User_Extra objectId:_goodObjectId];
        }
        else
        {
            obj = [AVObject objectWithClassName:DB_User_Extra];
        }
        
        [obj setObject:self.user.objectId forKey:@"user"];
        obj.fetchWhenSave = true;
        if (self.tuandui)
        {
            [obj incrementKey:@"tuandui" byAmount:@(1)];
        }
        if (self.zuijia)
        {
            [obj incrementKey:@"zuijia" byAmount:@(1)];
        }
        if (self.taidu)
        {
            [obj incrementKey:@"taidu" byAmount:@(1)];
        }
        if (self.leyu)
        {
            [obj incrementKey:@"leyu" byAmount:@(1)];
        }
        [obj saveInBackground];
        [GGUserModel userGoodRelation:self.user.objectId];
        _canGood = NO;
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = YES;
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.tuandui = NO;
    self.zuijia = NO;
    self.taidu = NO;
    self.leyu = NO;
    _canGood = NO;
    //userInfoLoading
    UIImageView *loading = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, kScreenH)];
    loading.contentMode = UIViewContentModeTop;
    loading.image = [UIImage imageNamed:@"userInfoLoading"];
    [self.view addSubview:loading];
    
    AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:self.objectId];
    [obj fetchInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
        if (!error) {
            loading.hidden = YES;
            self.user = (AVUser *)object;
            [self initUI];
            NSString *headUrl = [[self.user objectForKey:@"avatar"] objectForKey:@"url"];
              [self.backImageView setImageWithURL:[NSURL URLWithString:headUrl] placeholderImage:GGDefault_User_Head];
            [self.headImageView setImageWithURL:[NSURL URLWithString:headUrl] placeholderImage:GGDefault_User_Head];
            
            self.nameLabel.text = [self.user objectForKey:@"username"];
            
            NSNumber *sex = [self.user objectForKey:@"sex"];
            self.sexImageView.image = [UIImage imageNamed:[sex isEqual:@(YES)]?@"icon_nan":@"icon_nv"];
            
            NSInteger temp = [[self.user objectForKey:@"age"] integerValue];
            NSString *tempStr = [NSString stringWithFormat:@"%ld",(long)temp];
            NSString *lastAdd = [self.user objectForKey:@"lastAddress"];
            
            
            NSString *info = [NSString stringWithFormat:@"%@岁 %@ %@", [tempStr isEqualToString:@"0"]?@"0":tempStr,lastAdd.length == 0?@"":@"·",lastAdd.length == 0?@"":lastAdd];

            
            self.ageLabel.text = info;
            
            self.steamId.text = [self.user objectForKey:@"steamID"];
            
             self.noGoodLabel.text = [[self.user objectForKey:@"warn"] length] == 0?@"":[self.user objectForKey:@"warn"];
        }
    }];
    
    AVQuery *query = [AVQuery queryWithClassName:DB_User_Extra];
    [query whereKey:@"user" equalTo:self.objectId];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
                AVObject *obj = objects[0];
                _goodObjectId = obj.objectId;
                self.goodArr = @[[obj objectForKey:@"tuandui"],
                                 [obj objectForKey:@"zuijia"],
                                 [obj objectForKey:@"taidu"],
                                 [obj objectForKey:@"leyu"]];
            }
            else
            {
                self.goodArr = @[[NSNumber numberWithInteger:0],
                                 [NSNumber numberWithInteger:0],
                                 [NSNumber numberWithInteger:0],
                                 [NSNumber numberWithInteger:0]];
            }
            
            [self.collectionView reloadData];
        }
    }];
    
  
    
    
    AVQuery *queryfollow = [AVUser followeeQuery:[AVUser currentUser].objectId];
    AVObject *userobj = [AVObject objectWithClassName:DB_USER objectId:self.objectId];
    [queryfollow whereKey:@"followee" equalTo:userobj];
    [queryfollow findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count != 0)
            {
                [self.followBtn setTitle:@"取消关注" forState:UIControlStateNormal];
            }
        }
    }];
    
    [GGUserModel queryGoodRelation:self.objectId withBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
                //不能点赞
                _canGood = NO;
            }else
            {
                _canGood = YES;
            }
        }
    }];
}

- (void)initUI
{
    CGFloat hhh = kScreenW * 0.76;
    self.backView = [UIView new];
    self.backView.frame = CGRectMake(0, 0, self.view.frame.size.width, kScreenW * 0.76);
    self.backView.backgroundColor = [UIColor whiteColor];
//    self.view.userInteractionEnabled = YES;
    [self.view addSubview:self.backView];

    UIImageView *image = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, kScreenW * 0.76)];
    image.image = [UIImage imageNamed:@"room_Backbg"];
    image.contentMode = UIViewContentModeScaleAspectFill;
    [self.backView addSubview:image];
    self.backImageView = image;
    
    UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, kScreenW * 0.76)];
    back.backgroundColor = [UIColor blackColor];
    back.alpha = 0.5;
    [image addSubview:back];
    
    UIButton *backBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [backBtn setImage:[UIImage imageNamed:@"icon_fanhuiaa"] forState:UIControlStateNormal];
    backBtn.frame = CGRectMake(10, 30, 40, 30);
    [self.backView addSubview:backBtn];
    [backBtn addTarget:self action:@selector(popLast) forControlEvents:UIControlEventTouchUpInside];
    if (SJ_is_iPhoneX()) {
        backBtn.frame = CGRectMake(10, 44 , 40, 30);
    }
    
    self.reportBtn = [UIButton buttonWithType:UIButtonTypeCustom];
  //  [self.reportBtn setTitle:@"举报" forState:UIControlStateNormal];
    self.reportBtn.frame = CGRectMake(kScreenW - 50, 30, 40, 30);
    [self.backView addSubview:self.reportBtn];
    //report
    [self.reportBtn addTarget:self action:@selector(report) forControlEvents:UIControlEventTouchUpInside];
    
    CGFloat width = 100;
    CGFloat xx = (self.view.frame.size.width - 100)/2;
    CGFloat yy = (hhh - width)/2;
    
    self.headImageView = [[UIImageView alloc]initWithFrame:CGRectMake(xx, yy - 30, width, width)];
    self.headImageView.image = [UIImage imageNamed:@"testimage"];
    [self.backView addSubview:self.headImageView];
    self.headImageView.layer.masksToBounds = YES;
    self.headImageView.layer.cornerRadius = width/2;
    
    UIImageView *sex = [SJUIImageViewFactory imageViewWithImageName:@"icon_nv"];
    sex.frame = CGRectMake(CGRectGetMaxX(self.headImageView.frame) - 15, CGRectGetMaxY(self.headImageView.frame) - 15, 13, 13);
    [self.backView addSubview:sex];
    self.sexImageView = sex;
    
    UILabel *nameLabel = [SJUILabelFactory labelWithText:@"飞聊新人" textColor:[UIColor whiteColor] font:[UIFont boldSystemFontOfSize:18]];
    nameLabel.frame = CGRectMake(0, CGRectGetMaxY(self.headImageView.frame) + 15, self.backView.frame.size.width, 25);
    nameLabel.textAlignment = NSTextAlignmentCenter;
    [self.backView addSubview:nameLabel];
    self.nameLabel = nameLabel;
    
    
    UILabel *ageLabel = [SJUILabelFactory labelWithText:@"22岁 · 六安市" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
    ageLabel.frame = CGRectMake(0, CGRectGetMaxY(self.nameLabel.frame)+10, nameLabel.frame.size.width, 16);
    ageLabel.textAlignment = NSTextAlignmentCenter;
    [self.backView addSubview:ageLabel];
    self.ageLabel = ageLabel;
    
    UILabel *dddLabel = [SJUILabelFactory labelWithText:@"" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
    dddLabel.frame = CGRectMake(0, CGRectGetMaxY(self.ageLabel.frame), nameLabel.frame.size.width, 16);
    dddLabel.textColor = [UIColor redColor];
    dddLabel.textAlignment = NSTextAlignmentCenter;
    [self.backView addSubview:dddLabel];
    self.noGoodLabel = dddLabel;
    
    UIView *steamback = [UIView new];
    steamback.frame = CGRectMake(0, CGRectGetMaxY(self.backView.frame), kScreenW, 50);
    steamback.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:steamback];
    
    UILabel *steamId = [SJUILabelFactory labelWithFont:[UIFont boldSystemFontOfSize:14] textColor:[UIColor blackColor] alignment:NSTextAlignmentRight];
    steamId.text = @"声梦奇遇";
    steamId.frame = CGRectMake(0, 0, kScreenW - 20, steamback.frame.size.height);
    [steamback addSubview:steamId];
    self.steamId = steamId;
    
    UILabel *steam = [SJUILabelFactory labelWithText:@"TA的steamID"];
    steam.textColor = [UIColor blackColor];
    steam.frame = CGRectMake(20, 0, kScreenW - 20, steamback.frame.size.height);
    [steamback addSubview:steam];
    
    
    UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(steamback.frame), kScreenW, 1)];
    line.backgroundColor = GGLine_Color;
    [self.view addSubview:line];
    
    UILabel *goodLabel = [SJUILabelFactory labelWithText:@"给TA的行为点个赞"];
    goodLabel.textColor = [UIColor blackColor];
    goodLabel.frame = CGRectMake(20, CGRectGetMaxY(line.frame), kScreenW - 20, 50);
    [self.view addSubview:goodLabel];
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    flowLayout.minimumInteritemSpacing = 10;
    flowLayout.minimumLineSpacing = 10;
    flowLayout.itemSize =  CGSizeMake(60,65);
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    //设置CollectionView的属性
    self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(goodLabel.frame), self.view.frame.size.width, 70) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor whiteColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    self.collectionView.scrollEnabled = NO;
    self.collectionView.allowsMultipleSelection = YES;
    [self.view addSubview:self.collectionView];
//    [self.collectionView becomeFirstResponder];
    
    //注册Cell
    [self.collectionView registerClass:[GGUserGoodCell class] forCellWithReuseIdentifier:@"usergoodcell"];
    
    UIView *line2 = [[UIView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(self.collectionView.frame), kScreenW, 1)];
    line2.backgroundColor = GGLine_Color;
    [self.view addSubview:line2];
    
    
//    CALayer *layer = [CALayer layer];
//    layer.frame = CGRectMake(15, kScreenH - 55, kScreenW/2 - 30, 40);
//    layer.backgroundColor = [UIColor colorWithRed:157.0f/255.0f green:213.0f/255.0f blue:172.0f/255.0f alpha:1.0f].CGColor;
//    layer.shadowColor = [UIColor colorWithRed:157.0f/255.0f green:213.0f/255.0f blue:172.0f/255.0f alpha:1.0f].CGColor;
//    layer.shadowOpacity = 1;
//    layer.shadowOffset = CGSizeMake(0, 3);
//    layer.shadowRadius = 20;
//    [self.view.layer addSublayer:layer];
    
    
    
    self.sendMsgBtn = [SJUIButtonFactory buttonWithTitle:@"发消息" titleColor:[UIColor whiteColor]];
    self.sendMsgBtn.backgroundColor = RGBCOLOR(36, 192, 77);
    self.sendMsgBtn.layer.masksToBounds = YES;
    self.sendMsgBtn.layer.cornerRadius = 20;
    self.sendMsgBtn.frame = CGRectMake(15, kScreenH - 55, kScreenW/2 - 30, 40);
    [self.view addSubview:self.sendMsgBtn];
    [self.sendMsgBtn addTarget:self action:@selector(sendMsg) forControlEvents:UIControlEventTouchUpInside];
    

    self.followBtn = [SJUIButtonFactory buttonWithTitle:@"关注TA" titleColor:[UIColor whiteColor]];
    self.followBtn.backgroundColor = RGBCOLOR(255, 171, 45);
    self.followBtn.layer.masksToBounds = YES;
    self.followBtn.layer.cornerRadius = 20;
    self.followBtn.frame = CGRectMake(kScreenW/2 + 15, kScreenH - 55, kScreenW/2 - 30, 40);
    [self.view addSubview:self.followBtn];
    [self.followBtn addTarget:self action:@selector(follow:) forControlEvents:UIControlEventTouchUpInside];
    
    AVQuery *queryTeam = [AVQuery queryWithClassName:DB_USER_HISTORY];
    [queryTeam whereKey:@"userObjectId" equalTo:self.objectId];
    [queryTeam findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0) {
                
                AVObject *obj = objects[0];
                AVQuery *query = [GGQuery teamListQuery];
                [query whereKey:@"objectId" equalTo:[[obj objectForKey:@"team"] objectForKey:@"objectId"]];
                [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
                    if (!error && objects.count>0)
                    {
                        UILabel *goodLabel = [SJUILabelFactory labelWithText:@"当前所在房间"];
                        goodLabel.textColor = [UIColor blackColor];
                        goodLabel.frame = CGRectMake(20, CGRectGetMaxY(line2.frame), kScreenW - 20, 30);
                        [self.view addSubview:goodLabel];

                        AVObject *teamObj = objects[0];
                        GGTeamTableViewCell *cell = [[GGTeamTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"iden"];
                        cell.frame = CGRectMake(0, CGRectGetMaxY(goodLabel.frame) - 20, kScreenW, 95);
//                        cell.backgroundColor = [UIColor whiteColor];
                        GGTeamModel *model = [GGTeamModel vulueToObj:teamObj];
                        [cell setModel:model];
                         [cell.operateButton addTarget:self action:@selector(joinRoom) forControlEvents:UIControlEventTouchUpInside];
                        [self.view addSubview:cell];
                        
                        self.teamModel = model;
                        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapJoin)];
                        [cell addGestureRecognizer:tap];
                    }
                }];
            }
        }
    }];
    
}

#pragma mark - 加入
- (void)tapJoin
{
    [self joinTeam:self.teamModel];
}

- (void)joinRoom
{
    [self tapJoin];
}

- (void)joinTeam:(GGTeamModel*)model
{
    BOOL userNeedQ = [GGAppTool userCanJoinGame:model.game.objectId];
    if (userNeedQ)
    {
        [self showNormalGoAnwserAlert];
    }
    else
    {
        [GGRoomManger pushToRoom:model];
    }
}

- (void)sendMsg
{
    [[LCChatKit sharedInstance].client createConversationWithName:@"" clientIds:@[self.objectId] attributes:GGPRIVATETYPEATTRDIC options:AVIMConversationOptionUnique temporaryTTL:0 callback:^(AVIMConversation * _Nullable conversation, NSError * _Nullable error) {
        
        GGSingleConversationViewController *conversationViewController = [[GGSingleConversationViewController alloc] initWithConversationId:conversation.conversationId];
        [self.navigationController pushViewController:conversationViewController animated:YES];
    }];
}

- (void)popLast
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)report
{
    
}

- (void)follow:(UIButton *)btn
{
    if ([btn.titleLabel.text isEqualToString:@"取消关注"])
    {
        [[AVUser currentUser] unfollow:self.objectId andCallback:^(BOOL succeeded, NSError *error) {
            if (succeeded)
            {
                [btn setTitle:@"关注TA" forState:UIControlStateNormal];
            }
        }];
    }
    else
    {
        [[AVUser currentUser]follow:self.objectId andCallback:^(BOOL succeeded, NSError * _Nullable error) {
            if (succeeded) {
                [GGUserModel postFollowMsg:self.user.objectId];
                [btn setTitle:@"取消关注" forState:UIControlStateNormal];
            }
            
        }];
    }
}


#pragma mark - collectionView.Delegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    //return self.dataArr.count;
    return 4;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"usergoodcell";
    GGUserGoodCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    //  NSArray *titleArr = @[@"团队主力",@"最佳队友",@"态度友好",@"乐于助人"];
    NSArray *imgArr = @[@"icon_zhuli",@"icon_zuijia",@"icon_youhao",@"icon_zhuren"];
    if (self.goodArr.count == 4)
    {
        NSString *temp = [NSString stringWithFormat:@"%@",self.goodArr[indexPath.row]];
        cell.numLabel.text = temp;
        cell.imageView.image = [UIImage imageNamed:imgArr[indexPath.row]];
    }
    return cell;
}



// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat width = 62;
    CGFloat height = 65;
    return  CGSizeMake(width,height);
}

// 定义整个CollectionViewCell与整个View的间距
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(0, 10,0, 10);//（上、左、下、右）
}


//  定义每个UICollectionView的横向间距
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    CGFloat jianju = (self.view.frame.size.width - 62 * 4 - 20)/3;
    return jianju;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
    if (_canGood)
    {
         GGUserGoodCell *cell = (GGUserGoodCell *)[collectionView cellForItemAtIndexPath:indexPath];
        if (self.goodArr.count == 4)
        {
           
            NSInteger num = [cell.numLabel.text integerValue] + 1;
            NSString *temp = [NSString stringWithFormat:@"%ld",(long)num];
            cell.numLabel.text = temp;
            cell.goodImage.hidden = NO;
            [UIView animateWithDuration:0.3 animations:^{
                cell.goodImage.mj_y = cell.goodImage.frame.origin.y + 5;
            } completion:^(BOOL finished) {
                cell.goodImage.mj_y = cell.goodImage.frame.origin.y - 5;
            }];
        }
        if (indexPath.row == 0)
        {
            _tuandui = YES;
        }
        if (indexPath.row == 1)
        {
            _zuijia = YES;
        }
        if (indexPath.row == 2)
        {
            _taidu = YES;
        }
        if (indexPath.row == 3)
        {
            _leyu = YES;
        }
    }
}

- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(nonnull NSIndexPath *)indexPath
{
    if (_canGood)
    {
        if (self.goodArr.count == 4)
        {
            GGUserGoodCell *cell = (GGUserGoodCell *)[collectionView cellForItemAtIndexPath:indexPath];
            NSInteger num = [cell.numLabel.text integerValue] - 1;
            NSString *temp = [NSString stringWithFormat:@"%ld",(long)num];
            cell.numLabel.text = temp;
            cell.goodImage.hidden = YES;
        }
        if (indexPath.row == 0)
        {
            _tuandui = NO;
        }
        if (indexPath.row == 1)
        {
            _zuijia = NO;
        }
        if (indexPath.row == 2)
        {
            _taidu = NO;
        }
        if (indexPath.row == 3)
        {
            _leyu = NO;
        }
    }
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
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
