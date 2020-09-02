//
//  GGSysMessageViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSysMessageViewController.h"
#import "GGSysMesModel.h"
#import "GGSysMessageTableViewCell.h"
@interface GGSysMessageViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic,strong)NSMutableArray *dataArr;


@end

@implementation GGSysMessageViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64 - 61) style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
    
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    
    self.tableView.mj_header = [GGRefreshHeader ggrefreshHeaderBlock:^{
       // [self testSysMessageALl];
        [self loadData];
    }];
    [self.tableView.mj_header beginRefreshing];
}


- (void)loadData
{
    [GGSysMesModel querySysMessageWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            [self.tableView.mj_header endRefreshing];
            [self.dataArr removeAllObjects];
            for(AVObject *obj in objects)
            {
                GGSysMesModel *model = [GGSysMesModel valueToObj:obj];
                [self.dataArr addObject:model];
            }
            [self.tableView reloadData];
        }
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat ww = (kScreenW - 60)/3 + 40 + 30;
    GGSysMesModel *model = self.dataArr[indexPath.row];

    return [model.type isEqualToString:@"3"]?ww:95;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    GGSysMesModel *model = self.dataArr[indexPath.row];
    NSString *iden = [NSString stringWithFormat:@"iden%@",model.type];
    
    GGSysMessageTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGSysMessageTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    cell.backgroundColor = RGBCOLOR(244, 244, 244);
    cell.layer.masksToBounds = YES;
    cell.layer.cornerRadius = 5;
    [cell setModel:model];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    GGSysMesModel *model = self.dataArr[indexPath.row];
    if (self.pushSysMsgDetailBlock) {
        self.pushSysMsgDetailBlock(model);
    }
//    if ([model.type isEqualToString:@"3"])
//    {
//        if (self.pushSysMsgDetailBlock) {
//            self.pushSysMsgDetailBlock(model);
//        }
//    }
//    if ([model.type isEqualToString:@"1"])
//    {
//       
//    }
}


- (void)testSysMessage
{
    AVObject *obj = [AVObject objectWithClassName:DB_SYS_MESSAGE];
    [obj setObject:[AVUser currentUser] forKey:@"user"];
    [obj setObject:@"1" forKey:@"type"];
    [obj setObject:[AVUser currentUser].objectId forKey:@"range"];
    [obj setObject:@"关注消息2" forKey:@"title"];
    AVObject * relevantPersonne = [AVObject objectWithClassName:@"_User" objectId:@"5b59a1c10b616000312434f4"];
    [obj setObject:relevantPersonne forKey:@"relevantPersonne"];//相关人员
    [obj saveInBackground];

}

- (void)testSysMessageALl{
    AVObject *obj = [AVObject objectWithClassName:DB_SYS_MESSAGE];
    [obj setObject:[AVUser currentUser] forKey:@"user"];
    [obj setObject:@"1" forKey:@"type"];
    [obj setObject:@"5b837fc70b61600063f5d035" forKey:@"publishRange"];
    [obj setObject:@"关注消息" forKey:@"title"];
    [obj setObject:@"测试发送一次个人消息hook函数" forKey:@"content"];
    AVObject * relevantPersonne = [AVObject objectWithClassName:@"_User" objectId:@"5b59a1c10b616000312434f4"];
    [obj setObject:relevantPersonne forKey:@"relevantPersonne"];//相关人员
    [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        
    }];
}


- (void)testSysMessage2
{
    AVObject *obj = [AVObject objectWithClassName:DB_SYS_MESSAGE];
    [obj setObject:[AVUser currentUser] forKey:@"user"];//接收人
    [obj setObject:@"2" forKey:@"type"];
    [obj setObject:[AVUser currentUser].objectId forKey:@"range"];
    [obj setObject:@"举报结果2" forKey:@"title"];
    [obj setObject:@"你举报的用户经过核查已被封禁.感谢你的举报,飞聊提倡绿色的游戏环境和游戏玩家" forKey:@"content"];
    AVObject * relevantPersonne = [AVObject objectWithClassName:@"_User" objectId:@"5b59a1c10b616000312434f4"];
    [obj setObject:relevantPersonne forKey:@"relevantPersonne"];//相关人员
    [obj saveInBackground];
}


- (void)testSysMessage3
{
    AVObject *obj = [AVObject objectWithClassName:DB_SYS_MESSAGE];
    [obj setObject:[AVUser currentUser] forKey:@"user"];//发布人
    [obj setObject:@"3" forKey:@"type"];
    [obj setObject:@"all" forKey:@"range"];//接收人
    [obj setObject:@"[喜+1活动22]飞聊全平台开放下载,所有用户可以进行邀请其他用户获得丰厚奖励" forKey:@"title"];
    [obj setObject:@"www.toutiao.com" forKey:@"content"];
    [obj saveInBackground];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    
    
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
