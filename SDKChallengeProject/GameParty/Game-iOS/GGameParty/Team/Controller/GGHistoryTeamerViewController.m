//
//  GGHistoryTeamerViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGHistoryTeamerViewController.h"
#import "GGHistoryTeamerCell.h"
#import "GGUserInfoViewController.h"
@interface GGHistoryTeamerViewController ()<UITableViewDelegate,UITableViewDataSource>
{
    NSInteger _pageNo;
    CGFloat _oldY;//滑动偏移量
}

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic,strong)NSMutableArray *dataArr;

@end

@implementation GGHistoryTeamerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    _pageNo = 0;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 20 - 61 - 49) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = GGBackGround_Color;
    [self.view addSubview:self.tableView];
    
    if (IS_IPHONE_X) {
        self.tableView.frame = CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 44 - 61 - 83);
    }
    
    if (self.isFollowView) {
        self.tableView.frame = CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64) ;
        if (IS_IPHONE_X) {
            self.tableView.frame = CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64);
        }
        
        self.tableView.mj_footer = [MJRefreshFooter footerWithRefreshingBlock:^{
            _pageNo ++;
            [self loadData];
        }];
    }
    
    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
        _pageNo = 0;
        [self loadData];
    }];
    
    [self.tableView.mj_header beginRefreshing];
}

- (void)loadData
{
    if (self.isFollowView)
    {
        AVQuery *query= [AVUser followeeQuery:[AVUser currentUser].objectId];
        [query includeKey:@"followee"];
        query.limit = 20;
        query.skip = _pageNo * 20;
        [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            [self.tableView.mj_header endRefreshing];
            [self.tableView.mj_footer endRefreshing];
            if (!error)
            {
                if (_pageNo == 0)
                {
                    [self.dataArr removeAllObjects];
                }
                for(AVUser *user in objects)
                {
                    GGUserModel *model = [GGUserModel valueToObj:user];
                    [self.dataArr addObject:model];
                }
                [self.tableView reloadData];
            }
        }];
    }
    else
    {
        [GGTeamModel queryHistoryTeamerBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            [self.tableView.mj_header endRefreshing];
            if (_pageNo == 0)
            {
                [self.dataArr removeAllObjects];
            }
            for(AVObject *obj in objects)
            {
                AVUser *user = [obj objectForKey:@"teamer"];
                GGUserModel *model = [GGUserModel valueToObj:user];
                [self.dataArr addObject:model];
            }
            [self.tableView reloadData];
        }];
    }
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if (!self.isFollowView) {
        UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 50)];
        back.backgroundColor = GGBackGround_Color;
        UILabel *label = [SJUILabelFactory labelWithText:@"仅展示最近30条与我组队过的用户" textColor:RGBCOLOR(170, 170, 170)];
        label.font = [UIFont systemFontOfSize:12];
        label.textAlignment = NSTextAlignmentCenter;
        //label.backgroundColor = RGBCOLOR(248,248,248);
        label.backgroundColor = [UIColor whiteColor];
        label.frame = CGRectMake(15, 10, kScreenW - 30, 30);
        label.layer.masksToBounds = YES;
        label.layer.cornerRadius = 12.5;
        [back addSubview:label];
        return back;
    }
    return [UIView new];
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

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (!self.isFollowView) {
        return 60;
    }
    return 0.1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.1;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [[UIView alloc]init];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 84;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    GGHistoryTeamerCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGHistoryTeamerCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    
    GGUserModel *model = self.dataArr[indexPath.row];
    [cell.headImage setImageWithURL:[NSURL URLWithString:model.avatar.url] placeholderImage:GGDefault_User_Head];
    cell.nameLabel.text = model.username;
    cell.sexImage.image = [UIImage imageNamed:model.sex?@"icon_nan":@"icon_nv"];
    
    NSInteger temp = [model.age integerValue];
    NSString *tempStr = [NSString stringWithFormat:@"%ld",(long)temp];
    NSString *info = [NSString stringWithFormat:@"%@岁 %@ %@", [tempStr isEqualToString:@"0"]?@"0":tempStr,model.lastAddress.length == 0?@"":@"·",model.lastAddress.length == 0?@"":model.lastAddress];
    cell.infoLabel.text = info;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    GGUserModel *model = self.dataArr[indexPath.row];
    
    if (self.userInfoClickBlock)
    {
        self.userInfoClickBlock(self, model);
    }
    else
    {
        GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
        view.hidesBottomBarWhenPushed = YES;
        view.objectId = model.objectId;
        [self.navigationController pushViewController:view animated:YES];
    }
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
