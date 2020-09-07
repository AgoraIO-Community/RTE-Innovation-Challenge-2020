//
//  GGTeamTableViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/26.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGTeamTableViewController.h"
#import "GGTeamModel.h"

#import "GGTeamRoomViewController.h"
#import "GGTeamTableViewCell.h"
#import "GGQuestionViewController.h"
#import "GGQAViewController.h"
#import "CYLTableViewPlaceHolder.h"
#import "NoTeamHolder.h"
@interface GGTeamTableViewController ()<UITableViewDelegate,UITableViewDataSource,
AVLiveQueryDelegate,
CYLTableViewPlaceHolderDelegate>
{
    NSInteger _pageNo;
    CGFloat _oldY;//滑动偏移量
}

@property (nonatomic,strong)NSMutableArray *dataArr;

@property (nonatomic,strong)AVLiveQuery *broadcastingTeamQuery;

@property (nonatomic,strong)UITableView *tableView;



@end

@implementation GGTeamTableViewController
#pragma mark - lifeCircle
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self initData];
    
    [self initUI];
    
    [self initHeadView];

    [self loadData];
    
    [self initLiveQuery];
}

- (void)initHeadView
{
    if (self.gameModel.userNeedQ)
    {
        UIView *headerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 40)];
        self.tableView.tableHeaderView = headerView;
        headerView.backgroundColor = RGBCOLOR(239, 255, 242);
        UILabel *label = [[UILabel alloc]initWithFrame:headerView.bounds];
        label.textColor = Main_Color;
        label.font = [UIFont systemFontOfSize:12];
        label.text = @"    你需要完成此游戏答题才可以进行组队";
        [headerView addSubview:label];
        
        UIImageView *go = [[UIImageView alloc]initWithFrame:CGRectMake(kScreenW - 70, 0, 60, 40)];
        go.contentMode = UIViewContentModeRight;
        go.image = [UIImage imageNamed:@"icon_datia"];
        [headerView addSubview:go];
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(goAnwser)];
        [headerView addGestureRecognizer:tap];
    }
    else
    {
        self.tableView.tableHeaderView = [UIView new];
    }
}

- (UIView *)makePlaceHolderView
{
    return [self tablePlace];
}

- (UIView *)tablePlace
{
    __block NoTeamHolder  *netReloader = [[NoTeamHolder alloc] initWithFrame:CGRectMake(0, 0, 0, 0) reloadBlock:^{
        [self.tableView.mj_header beginRefreshing];
    }] ;
    return netReloader;
}


- (BOOL)enableScrollWhenPlaceHolderViewShowing
{
    return YES;
}

- (void)goAnwser
{
    if (self.gameModel)
    {
        GGQuestionViewController *qq = [[GGQuestionViewController alloc]init];
        qq.gameId = self.gameModel.objectId;
        qq.hidesBottomBarWhenPushed = YES;
        qq.title = self.gameModel.gameName;
        qq.anwserCompleteBlock = ^(GGQuestionViewController *vc, NSString *gameId, BOOL isSucccesed) {
            if ([gameId isEqualToString:self.gameModel.objectId] && isSucccesed)
            {
                self.gameModel.userNeedQ = NO;
                [self initHeadView];
            }
        };
        [self.navigationController pushViewController:qq animated:YES];
    }
    else
    {
        GGQAViewController *qa = [[GGQAViewController alloc]init];
        qa.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:qa animated:YES];
    }
}

#pragma mark - liveQuery.Delegate
- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidUpdate:(nonnull id)object updatedKeys:(nonnull NSArray<NSString *> *)updatedKeys
{
    if (liveQuery == self.broadcastingTeamQuery)
    {
        [self loadData];
    }
}

- (void)liveQuery:(AVLiveQuery *)liveQuery objectDidCreate:(id)object
{
    if (liveQuery == self.broadcastingTeamQuery)
    {
        [self loadData];
    }
}


#pragma mark - UI
- (void)initUI
{
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64 - 80 - 61) style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
  
    if (IS_IPHONE_X) {
        self.tableView.frame = CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 83 - 61 - 88 - 25);
    }
    if ([self.tableView respondsToSelector:@selector(setSeparatorInset:)])
    {
        [self.tableView setSeparatorInset:UIEdgeInsetsMake(0, 80, 0, 0)];
    }
    
    
    
    UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 100)];
    
    back.backgroundColor = [UIColor whiteColor];
    UIView *bbbb = [[UIView alloc]initWithFrame:CGRectMake(30, 10, kScreenW - 60, 30)];
    bbbb.layer.masksToBounds = YES;
    bbbb.layer.cornerRadius = 15;
    bbbb.backgroundColor = GGBackGround_Color;
    [back addSubview:bbbb];
    
    NSString *text = @"到底啦~刷新下也许会有新的房间哦!";
    CGFloat width = [GGAppTool sizeWithText:text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(kScreenW, 30)].width;
    CGFloat xxx = ((kScreenW - 60) - width - 80)/2;
    if (width + 80 > kScreenW - 60) {
        bbbb.frame = CGRectMake((kScreenW - width - 80 - 10)/2, 10, width+90, 30);
        xxx = 5;
    }
    UILabel *label = [SJUILabelFactory labelWithText:text textColor:RGBCOLOR(170, 170, 170)];
    label.font = [UIFont systemFontOfSize:12];
    label.textAlignment = NSTextAlignmentCenter;
    label.frame = CGRectMake(xxx, 0, width, 30);
    [bbbb addSubview:label];
    
    UIButton *refre = [UIButton buttonWithType:UIButtonTypeCustom];
    refre.frame = CGRectMake(CGRectGetMaxX(label.frame), 0, 80, 30);
    [refre setTitle:@"刷新列表" forState:UIControlStateNormal];
    [refre setImageEdgeInsets:UIEdgeInsetsMake(0.0, -10, 0.0, 0.0)];
    [refre setImage:[UIImage imageNamed:@"icon_shuaxinnew"] forState:UIControlStateNormal];
    [refre setTitleColor:Main_Color forState:UIControlStateNormal];
    refre.titleLabel.font = [UIFont systemFontOfSize:12];
    [bbbb addSubview: refre];
    [refre addTarget:self action:@selector(refre) forControlEvents:UIControlEventTouchUpInside];
    
    self.tableView.tableFooterView = back;
    
    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
        _pageNo = 0;
        [self loadData];
    }];
    
    
//    [self.tableView.mj_header beginRefreshing];
}

#pragma mark - Data
- (void)initData
{
    _pageNo = 0;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
}


- (void)loadData
{
    if (self.gameModel)
    {
        BOOL userNeedQ = [GGAppTool userCanJoinGame:self.gameModel.objectId];
        if (!userNeedQ)
        {
            self.tableView.tableHeaderView = [UIView new];
        }
    }
    [GGTeamModel queryTeamWithPageNo:_pageNo gameModel:self.gameModel block:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        [self.tableView.mj_header endRefreshing];
        if (!error)
        {
            if (_pageNo == 0)
            {
                [self.dataArr removeAllObjects];
            }
            
            for(AVObject *obj in objects)
            {
                GGTeamModel *model = [GGTeamModel vulueToObj:obj];
                [self.dataArr addObject:model];
            }
            [self.tableView cyl_reloadData];
        }
    }];
}

- (void)initLiveQuery
{
    AVQuery *query = [GGQuery teamLiveQuery];
    if (self.gameModel)//game有值就查询game
    {
        AVObject *obj = [AVObject objectWithClassName:DB_GAME objectId:self.gameModel.objectId];
        [query whereKey:@"game" equalTo:obj];
    }
    self.broadcastingTeamQuery = [[AVLiveQuery alloc]initWithQuery:query];
    self.broadcastingTeamQuery.delegate = self;
    [self.broadcastingTeamQuery subscribeWithCallback:^(BOOL succeeded, NSError * _Nonnull error) {
        if (succeeded)
        {
            NSLog(@"订阅成功");
        }
    }];
}


#pragma mark - Method
- (void)joinRoom:(UIButton *)button
{
    GGTeamModel *model = self.dataArr[button.tag - 100];
    [GGRoomManger pushToRoom:model];
}


- (void)jointWithModel:(GGTeamModel *)model
{
    [GGRoomManger pushToRoom:model];
}

#pragma mark - table.delegate
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

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 95;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [UIView new];
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.1;
}

//
//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
//{
//    return 100;
//}
//
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
//{
//    if (1) {
//        UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 50)];
//        back.backgroundColor = GGBackGround_Color;
//
//        UIView *bbbb = [[UIView alloc]initWithFrame:CGRectMake(30, 10, kScreenW - 60, 30)];
//        bbbb.layer.masksToBounds = YES;
//        bbbb.layer.cornerRadius = 15;
//        //bbbb.backgroundColor = RGBCOLOR(248,248,248);
//        bbbb.backgroundColor = [UIColor whiteColor];
//        [back addSubview:bbbb];
//
//        NSString *text = @"到底啦~刷新下也许会有新的房间哦!";
//        CGFloat width = [GGAppTool sizeWithText:text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(kScreenW, 30)].width;
//        CGFloat xxx = ((kScreenW - 60) - width - 80)/2;
//        if (width + 80 > kScreenW - 60) {
//            bbbb.frame = CGRectMake((kScreenW - width - 80 - 10)/2, 10, width+90, 30);
//            xxx = 5;
//        }
//        UILabel *label = [SJUILabelFactory labelWithText:text textColor:RGBCOLOR(170, 170, 170)];
//        label.font = [UIFont systemFontOfSize:12];
//        label.textAlignment = NSTextAlignmentCenter;
//        label.frame = CGRectMake(xxx, 0, width, 30);
//        [bbbb addSubview:label];
//
//        UIButton *refre = [UIButton buttonWithType:UIButtonTypeCustom];
//        refre.frame = CGRectMake(CGRectGetMaxX(label.frame), 0, 80, 30);
//        [refre setTitle:@"刷新列表" forState:UIControlStateNormal];
//        [refre setImageEdgeInsets:UIEdgeInsetsMake(0.0, -10, 0.0, 0.0)];
//        [refre setImage:[UIImage imageNamed:@"icon_shuaxinnew"] forState:UIControlStateNormal];
//        [refre setTitleColor:Main_Color forState:UIControlStateNormal];
//        refre.titleLabel.font = [UIFont systemFontOfSize:12];
//        [bbbb addSubview: refre];
//        [refre addTarget:self action:@selector(refre) forControlEvents:UIControlEventTouchUpInside];
//
//        return back;
//    }
//    return [UIView new];
//}

- (void)refre
{
    [self.tableView.mj_header beginRefreshing];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    GGTeamTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGTeamTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    
    GGTeamModel *model = self.dataArr[indexPath.row];
    [cell setModel:model];
    [cell.operateButton addTarget:self action:@selector(joinRoom:) forControlEvents:UIControlEventTouchUpInside];
    cell.operateButton.tag = 100 + indexPath.row;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    if (self.gameModel) {
//        if (self.gameModel.userNeedQ)
//        {
//            [self showGoAnwserAlert];
//        }
//        else
//        {
//            GGTeamModel *model = self.dataArr[indexPath.row];
//            [self jointWithModel:model];
//        }
//    }
//    else
//    {
        //全部里面的游戏
        GGTeamModel *model = self.dataArr[indexPath.row];
        BOOL userNeedQ = [GGAppTool userCanJoinGame:model.game.objectId];
        if (userNeedQ)
        {
            [self showGoAnwserAlert];
        }
        else
        {
            [self jointWithModel:model];
        }
        
//    }
    
}

- (void)showGoAnwserAlert
{
    //警告不允许,必须答题
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"为了创建良好的组队环境,你需要完成此游戏答题才可以进行组队" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *action1 = [UIAlertAction actionWithTitle:@"立即答题" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [self goAnwser];
    }];
    UIAlertAction *action2 = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    [alert addAction:action1];
    [alert addAction:action2];
    [self presentViewController:alert animated:YES completion:nil];
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
