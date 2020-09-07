//
//  GGUserListView.m
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserListView.h"
#import "GGUserListCell.h"
#import "GGUserListModel.h"
@interface GGUserListView()<UICollectionViewDelegate,
UICollectionViewDataSource>

@property (nonatomic,strong)NSMutableArray *clientsArr;//objectId array

@property (nonatomic,strong)NSMutableArray *dataArr;//AVUser array
@property (nonatomic,assign)NSInteger maxNum;

@property (nonatomic,strong)GGTeamModel *model;

@end


@implementation GGUserListView

- (instancetype)initWithFrame:(CGRect)frame withTeamModel:(GGTeamModel *)model
{
    self.model = model;
    return [self initWithFrame:frame];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        self.dataArr = [NSMutableArray arrayWithCapacity:0];
        _maxNum = 0;
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 10;
        flowLayout.minimumLineSpacing = 10;
        flowLayout.itemSize =  CGSizeMake(60,60);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        
        [self.dataArr addObject:[GGUserListModel modelWithUser:(AVUser *)self.model.publisher]];
        
        //设置CollectionView的属性
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, kScreenW, self.frame.size.height) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = NO;
        [self addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGUserListCell class] forCellWithReuseIdentifier:@"usercell"];
    }
    return self;
}


- (void)refreshUserList DEPRECATED_MSG_ATTRIBUTE("废弃---查询队友使用partimants字段");

{
    AVQuery *query = [AVQuery queryWithClassName:DB_USER_HISTORY];
    AVObject *team = [AVObject objectWithClassName:DB_TEAM objectId:self.model.objectId];
    [query whereKey:@"team" equalTo:team];
    [query includeKey:@"user"];
    [query orderByAscending:@"createdAt"];
    query.cachePolicy = kAVCachePolicyIgnoreCache;

    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error) {
            [self.dataArr removeAllObjects];
            for(AVObject *obj in objects)
            {
                AVUser *user = [obj objectForKey:@"user"];
                GGUserListModel *model = [[GGUserListModel alloc]init];
                model.user = user;
                model.isNoMac = YES;
                model.isOnline = YES;
                model.isSpeaking = NO;
                [self.dataArr addObject:model];
            }
            [self.collectionView reloadData];
            
            [self queryOffLine];
        }
    }];
}


- (void)setUserListDataWithArray:(NSArray *)array maxNum:(NSInteger)maxNum
{
    _maxNum = maxNum;
    self.clientsArr = [NSMutableArray arrayWithArray:array];
  //  [self refreshUserList];//初始化基本信息
//
//    NSMutableArray *arr = [NSMutableArray arrayWithCapacity:0];
//    for(NSString *objectId in array)
//    {
//        AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:objectId];
//        [arr addObject:obj];
//    }
//    [AVObject fetchAllInBackground:arr block:^(NSArray * _Nullable objects, NSError * _Nullable error) {
//        if (!error) {
//            [self.dataArr removeAllObjects];
//            for(AVObject *obj in objects)
//            {
//                GGUserListModel *model = [[GGUserListModel alloc]init];
//                model.user = obj;
//                model.isNoMac = YES;
//                model.isOnline = YES;
//                model.isSpeaking = NO;
//                [self.dataArr addObject:model];
//            }
//            [self.collectionView reloadData];
//
//            [self queryOffLine];
//        }
//    }];

    NSMutableArray *queryArr = [NSMutableArray arrayWithCapacity:array.count];
    for(NSString *objectId in array)
    {
        AVQuery *query = [AVQuery queryWithClassName:DB_USER];
        [query whereKey:@"objectId" equalTo:objectId];
        [queryArr addObject:query];
    }
    AVQuery *query = [AVQuery orQueryWithSubqueries:queryArr];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {

        [self.dataArr removeAllObjects];
        for(AVObject *obj in objects)
        {
            GGUserListModel *model = [[GGUserListModel alloc]init];
            model.user = obj;
            model.isNoMac = YES;
            model.isOnline = YES;
            model.isSpeaking = NO;
            [self.dataArr addObject:model];
        }
        
//        self.dataArr = [NSMutableArray arrayWithArray:[[self.dataArr reverseObjectEnumerator] allObjects]];
        [self.collectionView reloadData];

        [self queryOffLine];
    }];
}


- (void)queryOffLine
{
    
    NSMutableArray *idsArr = [NSMutableArray arrayWithCapacity:0];
    for(GGUserListModel *model in self.dataArr)
    {
        NSString *idstring = model.user.objectId;
        [idsArr addObject:idstring];
    }
    [[LCChatKit sharedInstance].client queryOnlineClientsInClients:idsArr callback:^(NSArray<NSString *> * _Nullable clientIds, NSError * _Nullable error) {
        if (!error)
        {
            for(GGUserListModel *model in self.dataArr)
            {
                if ([clientIds containsObject:model.user.objectId ])
                {
                    model.isOnline = YES;
                }
                else
                {
                    model.isOnline = NO;
                }
            }
            [self.collectionView reloadData];
        }
    }];
}

#pragma mark - public.Method

- (void)membersAdd:(NSString *)objectId
{
    if (![self.clientsArr containsObject:objectId]) {
        [self.clientsArr addObject:objectId];
        AVObject *obj = [AVObject objectWithClassName:DB_USER objectId:objectId];
        [obj fetchInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
            if (!error) {
                GGUserListModel *model = [[GGUserListModel alloc]init];
                model.user = object;
                model.isNoMac = YES;
                model.isOnline = YES;
                model.isSpeaking = NO;
                [self.dataArr addObject:model];
                [self.collectionView reloadData];
            }
            else
            {
                [self.clientsArr removeObject:objectId];
            }
        }];
    }else{
        //已经是队友了--->无需处理
    }
}

- (void)membersRemove:(NSString *)objectId
{
    if ([self.clientsArr containsObject:objectId]) {
        //remove掉
        [self.clientsArr removeObject:objectId];
        
        for(int i = 0;i < self.dataArr.count;i++)
        {
            GGUserListModel *model = self.dataArr[i];
            if([model.user.objectId isEqualToString:objectId])
            {
                [self.dataArr removeObjectAtIndex:i];
                //break;
            }
        }
        dispatch_async(dispatch_get_main_queue(), ^{
           // [self.collectionView reloadData];
            [self.collectionView reloadSections:[NSIndexSet indexSetWithIndex:0]];
            

        });
        
    }
    else
    {
        //本来就不是队友===>无需处理
    }
}



//谁在说话
- (void)upDateUserSpeakerWithUid:(NSNumber *)uid
{
    for(int i = 0;i < self.dataArr.count;i++)
    {
        GGUserListModel *model = self.dataArr[i];
        NSNumber *userId = [model.user objectForKey:@"userId"];
        if ([userId isEqual:uid]) {
            model.isSpeaking = YES;
            model.isOnline = YES;//在说话一定在线
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [self.collectionView reloadItemsAtIndexPaths:@[indexPath]];
            break;
        }
    }
}

//谁关闭/开启了麦克风
- (void)upDateUserMacStatus:(NSNumber *)uid isMac:(BOOL)isMac
{
    //isMac==YES ===>说明麦克风开启状态
    for(int i = 0;i < self.dataArr.count;i++)
    {
        GGUserListModel *model = self.dataArr[i];
        NSNumber *userId = [model.user objectForKey:@"userId"];
        if ([userId isEqual:uid]) {
            model.isNoMac = isMac;
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [self.collectionView reloadItemsAtIndexPaths:@[indexPath]];
            break;
        }
    }
}

- (void)upDateUserOnlineStatus:(NSNumber *)uid offline:(BOOL)offline
{
    for(int i = 0;i < self.dataArr.count;i++)
    {
        GGUserListModel *model = self.dataArr[i];
        NSNumber *userId = [model.user objectForKey:@"userId"];
        if ([userId isEqual:uid]) {
            model.isOnline = !offline;
            model.isNoMac = YES;//重置麦克风状态,假如离线前麦克风是关闭状态,否则会引起再次上线的时候麦克风状态依然是关闭的
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:i inSection:0];
            [self.collectionView reloadItemsAtIndexPaths:@[indexPath]];
            break;
        }
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
    return _maxNum;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"usercell";
    GGUserListCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    GGUserListModel *model;
    if (self.dataArr.count > indexPath.row )
    {
        model = self.dataArr[indexPath.row];
    }
    else
    {
        model = nil;
    }

    [cell setUserListModel:model];
    return cell;
}

// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat width = 60;
    CGFloat height = 90;
    return  CGSizeMake(width,height);
}

// 定义整个CollectionViewCell与整个View的间距
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(10, 10,10, 10);//（上、左、下、右）
}


//  定义每个UICollectionView的横向间距
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    CGFloat jianju = (self.frame.size.width - 60 * 4 - 20)/4;
    return jianju;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
    
    GGUserListModel *model;
    if (self.dataArr.count > indexPath.row )
    {
        model = self.dataArr[indexPath.row];
    }
    else
    {
        model = nil;
    }

    if ([self.v_delegate respondsToSelector:@selector(clickUser:)])
    {
        [self.v_delegate clickUser:model.user];
    }
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}



/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
