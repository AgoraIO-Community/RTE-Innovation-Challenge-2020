//
//  GGFollowViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGFollowViewController.h"
#import "GGHistoryTeamerCell.h"
#import "GGFollowTeamModel.h"
#import "GGFollowTeamCell.h"
#import "GGUserInfoViewController.h"
#import "CYLTableViewPlaceHolder.h"
#import "NoTeamHolder.h"
@interface GGFollowViewController ()<UITableViewDelegate,UITableViewDataSource,CYLTableViewPlaceHolderDelegate>
{
    NSInteger _pageNo;
    CGFloat _oldY;//滑动偏移量
}
@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic,strong)NSMutableArray *dataArr;

@end

@implementation GGFollowViewController


- (UIView *)makePlaceHolderView
{
    return [self tablePlace];
}

- (UIView *)tablePlace
{
    __block NoTeamHolder  *netReloader = [[NoTeamHolder alloc] initWithFrame:CGRectMake(0, 0, 0, 0) reloadBlock:^{
        [self.tableView.mj_header beginRefreshing];
    }] ;
    [netReloader changeTitle:@"关注的好友都没有在玩游戏,一会儿刷新看看"];
    return netReloader;
}


- (BOOL)enableScrollWhenPlaceHolderViewShowing
{
    return YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _pageNo = 0;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];//CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 20 - 61 - 49)
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 20 - 61 - 49)  style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = GGBackGround_Color;
    [self.view addSubview:self.tableView];
    
    if (IS_IPHONE_X) {//CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 44 - 61 - 83)
        self.tableView.frame = CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 44 - 61 - 83);
    }
    
    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
        
        [self loadData];
    }];
    
    [self.tableView.mj_header beginRefreshing];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if ([scrollView isEqual: self.tableView]) {
        if (self.tableView.contentOffset.y > _oldY) {
            // 上滑
            if (self.shouldHiddenOperateView) {
                self.shouldHiddenOperateView(YES);
            }
        }
        else{
            // 下滑
            if (self.shouldHiddenOperateView) {
                self.shouldHiddenOperateView(NO);
            }
            
        }
    }
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    // 获取开始拖拽时tableview偏移量
    _oldY = self.tableView.contentOffset.y;
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    if (self.shouldHiddenOperateView) {
        self.shouldHiddenOperateView(NO);
    }
    
}


- (void)loadData
{
    AVQuery *query= [AVUser followeeQuery:[AVUser currentUser].objectId];
    [query includeKey:@"followee"];
    query.limit = 20;
    query.skip = _pageNo * 20;
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        [self.tableView.mj_header endRefreshing];
        if (!error)
        {
            if (_pageNo == 0)
            {
                [self.dataArr removeAllObjects];
            }
            
            NSMutableArray *teamArr = [NSMutableArray arrayWithCapacity:0];
            for(AVUser *user in objects)
            {
                AVQuery *teamQuery = [AVQuery queryWithClassName:DB_USER_HISTORY];
                [teamQuery whereKey:@"userObjectId" equalTo:user.objectId];
                [teamArr addObject:teamQuery];
            }            
            AVQuery *queryAll = [AVQuery orQueryWithSubqueries:teamArr];
            [queryAll includeKey:@"user"];
            [queryAll includeKey:@"team"];
            [queryAll includeKey:@"team.type"];
            [queryAll includeKey:@"team.participants"];
            [queryAll includeKey:@"team.game"];
            [queryAll includeKey:@"team.conversation"];
            [queryAll orderByDescending:@"updatedAt"];
            [queryAll findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
                if (!error)
                {
                    for(AVObject *obj in objects)
                    {
                        GGFollowTeamModel *model = [[GGFollowTeamModel alloc]init];
                        model.user = [obj objectForKey:@"user"];
                        model.teamModel = [GGTeamModel vulueToObj:[obj objectForKey:@"team"]];
                        model.createdAt = obj.createdAt;
                        [self.dataArr addObject:model];
                    }
                     [self.tableView cyl_reloadData];
                }
            }];
        }
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 107+40;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    GGFollowTeamCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGFollowTeamCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    
    GGFollowTeamModel *model = self.dataArr[indexPath.row];
    [cell setModel:model];
    cell.joinTeamBlock = ^(GGFollowTeamCell *cell, GGTeamModel *model) {
        
        if (self.joinTeamBlock) {
            self.joinTeamBlock(model);
        }
    };
    
    cell.userInfoClickBlock = ^(GGFollowTeamCell *cell, AVUser *user) {
        
        if (self.userInfoClickBlock) {
            self.userInfoClickBlock(user);
        }
    };
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
   // GGFollowTeamModel *model = self.dataArr[indexPath.row];

//    if (self.userInfoClickBlock)
//    {
//        self.userInfoClickBlock(self, model);
//    }
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
