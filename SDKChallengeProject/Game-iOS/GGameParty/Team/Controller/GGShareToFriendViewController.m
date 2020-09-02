//
//  GGShareFriendViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/11.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGShareToFriendViewController.h"

@interface GGShareToFriendViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (nonatomic, strong) AVIMClient *client;//聊天Client
@property (nonatomic,strong)UITableView *tableView;
@property (nonatomic,strong)NSMutableArray *dataArr;


@end

@implementation GGShareToFriendViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];

    self.client = [LCChatKit sharedInstance].client;
    
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64) style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
    
    [self loadData];
}

- (void)loadData
{
    AVIMConversationQuery *query = [self.client conversationQuery];
    [query whereKey:@"objectId" containedIn:@[[AVUser user].objectId]];
    
    query.limit = 100;
    [query whereKey:AVIMAttr(@"type") notEqualTo:GGROOMTYPEATTR];
    [query whereKey:AVIMAttr(@"type") notEqualTo:GGPRIVATETYPEATTR];
    [query whereKey:@"sys" notEqualTo:@(YES)];
    
    [query findConversationsWithCallback:^(NSArray *objects, NSError *error) {
        
        NSLog(@"查询成功！");
        for(AVObject *obj in objects)
        {
            [self.dataArr addObject:obj];
        }
        [self.tableView reloadData];
        
    }];
}



#pragma mark - table.delegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
        
        //        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    AVObject *obj = self.dataArr[indexPath.row];
    cell.textLabel.text = [obj objectForKey:@"name"];
//    GGTeamModel *model = self.dataArr[indexPath.row];
//    [cell setModel:model];
//    [cell.operateButton addTarget:self action:@selector(joinRoom:) forControlEvents:UIControlEventTouchUpInside];
//    cell.operateButton.tag = 100 + indexPath.row;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    //点击发布到首页那么进行置顶
//    GGTeamModel *model = self.dataArr[indexPath.row];
//    [self jointWithModel:model];
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
