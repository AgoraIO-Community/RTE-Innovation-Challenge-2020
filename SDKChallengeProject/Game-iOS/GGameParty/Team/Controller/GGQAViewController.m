//
//  GGQAViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGQAViewController.h"
#import "GGQGameTableViewCell.h"
#import "GGGameModel.h"
#import "GGQuestionViewController.h"

@interface GGQAViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic,strong)NSMutableArray *dataArr;

@end

@implementation GGQAViewController

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self.tableView.mj_header beginRefreshing];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    self.title = @"我的答题";
      self.navigationController.navigationBarHidden = NO;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 10, kScreenW, self.view.frame.size.height - 10) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
    
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
        
        [self loadData];
    }];
}

- (void)loadData
{
    AVUser *user = [AVUser currentUser];
    [user refreshInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
        
        NSArray *anwserArr = [object objectForKey:@"allowGame"];
        [GGGameModel queryGameListWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            [self.tableView.mj_header endRefreshing];
            if (!error)
            {
                [self.dataArr removeAllObjects];
                for(AVObject *obj in objects)
                {
                    GGGameModel *model = [GGGameModel valueToObj:obj];
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
                        [self.dataArr addObject:model];
                    }
                }
                [self.tableView reloadData];
            }
            else
            {
                [XHToast showCenterWithText:@"网络错误"];
            }
        }];
        
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 25;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return @"答题通过即可解除相应板块的组队限制";
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 74 + 15;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    GGQGameTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGQGameTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    
    GGGameModel *model = self.dataArr[indexPath.row];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell setModel:model];
    cell.goBtn.tag = indexPath.row + 1;
    [cell.goBtn addTarget:self action:@selector(go:) forControlEvents:UIControlEventTouchUpInside];
    return cell;
}

- (void)go:(UIButton *)btn
{
    GGGameModel *model = self.dataArr[btn.tag - 1];
    GGQuestionViewController *qu = [[GGQuestionViewController alloc]init];
    qu.gameId = model.objectId;
    qu.title = model.gameName;
    [self.navigationController pushViewController:qu animated:YES];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    GGGameModel *model = self.dataArr[indexPath.row];
    if (model.userNeedQ)
    {
        GGQuestionViewController *qu = [[GGQuestionViewController alloc]init];
        qu.gameId = model.objectId;
        qu.title = [NSString stringWithFormat:@"[%@]答题",model.gameName];
        [self.navigationController pushViewController:qu animated:YES];
    }
}




- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
