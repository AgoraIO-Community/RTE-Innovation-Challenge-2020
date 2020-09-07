//
//  GGSearchViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSearchViewController.h"
#import "GGTeamModel.h"

#import "GGTeamRoomViewController.h"
#import "GGTeamTableViewCell.h"
#import "GGHistoryTeamerCell.h"
#import "GGUserInfoViewController.h"

@interface GGSearchViewController ()<UITextFieldDelegate,
UITableViewDelegate,UITableViewDataSource>
{
    NSInteger _pageNo;
}


@property (nonatomic,strong)UITextField *searchTextfield;

@property (nonatomic,strong)UIButton *searchBtn;

@property (nonatomic,strong)NSMutableArray *dataArr;
@property (nonatomic,strong)NSMutableArray *teamArr;

@property (nonatomic,strong)UITableView *tableView;


@end

@implementation GGSearchViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = YES;
    [self initData];
    [self initNAv];
    [self initUI];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self goSearch];
    return YES;
}

- (void)goSearch
{
    if (self.searchTextfield.text.length > 0)
    {
        [self.searchTextfield resignFirstResponder];
        [self loadData];
    }
    else
    {
        
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    [self.searchTextfield resignFirstResponder];
}


- (void)initData
{
    _pageNo = 0;
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
    self.teamArr = [NSMutableArray arrayWithCapacity:0];
}


- (void)querySearch
{
    AVSearchQuery *searchQuery = [AVSearchQuery searchWithQueryString:self.searchTextfield.text];
    [searchQuery setQueryString:[NSString stringWithFormat:@"%@*",self.searchTextfield.text]];
    
    searchQuery.className = DB_TEAM;
    //    searchQuery.highlights = @"title,desc";
    searchQuery.limit = 10;
    searchQuery.cachePolicy = kAVCachePolicyNetworkOnly;
    searchQuery.maxCacheAge = 60;
    searchQuery.fields = @[@"title", @"desc"];
    
    [searchQuery includeKey:@"type"];
    [searchQuery includeKey:@"publisher"];
    [searchQuery includeKey:@"participants"];
    [searchQuery includeKey:@"game"];
    [searchQuery includeKey:@"conversation"];
    [searchQuery orderByDescending:@"updatedAt"];
    
    
    [searchQuery findInBackground:^(NSArray *objects, NSError *error) {
        for (AVObject *object in objects)
        {
            //            NSString *appUrl = [object objectForKey:@"_app_url"];
            //            NSString *deeplink = [object objectForKey:@"_deeplink"];
            //            NSString *hightlight = [object objectForKey:@"_highlight"];
            
            GGTeamModel *model = [GGTeamModel vulueToObj:object];
            [self.teamArr addObject:model];
        }
        [self.tableView reloadData];
    }];
    
}

- (void)loadData
{
    [GGUserModel queryUserWithName:self.searchTextfield.text withBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        
        if (!error) {
            if (_pageNo == 0)
            {
                [self.dataArr removeAllObjects];
            }
            for(AVObject *obj in objects)
            {
                AVUser *user = (AVUser *)obj;
                GGUserModel *model = [GGUserModel valueToObj:user];
                [self.dataArr addObject:model];
            }
            [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:0] withRowAnimation:UITableViewRowAnimationAutomatic];
            if (self.dataArr.count == 0) {
                [XHToast showBottomWithText:@"未搜索到相关内容"];
            }
        }
    }];
    
    [GGTeamModel queryTeamWithTitleField:self.searchTextfield.text withPageNo:_pageNo gameModel:nil block:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        
        if (!error)
        {
            if (_pageNo == 0)
            {
                [self.teamArr removeAllObjects];
            }
            
            for(AVObject *obj in objects)
            {
                GGTeamModel *model = [GGTeamModel vulueToObj:obj];
                [self.teamArr addObject:model];
            }
            [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:1] withRowAnimation:UITableViewRowAnimationAutomatic];
            
            if (self.teamArr.count == 0) {
                [XHToast showBottomWithText:@"未搜索到相关内容"];
            }
        }
    }];
    
}
- (void)loadView {
    [super loadView];
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
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 36;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(30, 0, kScreenW - 30, 36)];
    label.font = [UIFont systemFontOfSize:12];
    label.textColor = RGBCOLOR(170, 170, 170);
    if (section == 0) {
        if (self.dataArr.count>0) {
            label.text = @"     用户";
            //return @"用户";
        }
    }
    if (section == 1) {
        if (self.teamArr.count>0) {
            label.text = @"     房间";
//            return @"房间";
        }
    }
    
    return label;
}
//
//- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
//{
//    if (section == 0) {
//        if (self.dataArr.count>0) {
//            return @"用户";
//        }
//    }
//    if (section == 1) {
//        if (self.teamArr.count>0) {
//            return @"房间";
//        }
//    }
//    return @"";
//}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return indexPath.section ==0?84:95;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return section ==0 ?self.dataArr.count:self.teamArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        static NSString *iden = @"useriden";
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
        NSString *info = [NSString stringWithFormat:@"%@岁 · %@", [tempStr isEqualToString:@"0"]?@"0":tempStr,model.lastAddress.length == 0?@"":model.lastAddress];
        cell.infoLabel.text = info;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        return cell;
    }
    
    static NSString *iden = @"iden";
    GGTeamTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGTeamTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    
    GGTeamModel *model = self.teamArr[indexPath.row];
    [cell setModel:model];
    [cell.operateButton addTarget:self action:@selector(joinRoom:) forControlEvents:UIControlEventTouchUpInside];
    cell.operateButton.tag = 100 + indexPath.row;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0) {
        GGUserModel *model = self.dataArr[indexPath.row];
        GGUserInfoViewController *view = [[GGUserInfoViewController alloc]init];
        view.hidesBottomBarWhenPushed = YES;
        view.objectId = model.objectId;
        [self.navigationController pushViewController:view animated:YES];
    }
    else
    {
        GGTeamModel *model = self.teamArr[indexPath.row];
        [self jointWithModel:model];
    }
}


- (void)initNAv
{
    UIView *stb= [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 20)];
    stb.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:stb];
    
    UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 44)];
    back.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:back];
    if (IS_IPHONE_X) {
        stb.mj_h = 44;
        back.mj_y = 44;
    }
    
    UIButton *backBtn = [SJUIButtonFactory buttonWithImageName:@"icon_fanhuiaa" target:self sel:@selector(goBack) tag:1];
    backBtn.frame = CGRectMake(0, 0, 44, 44);
    
    [back addSubview:backBtn];
    
    self.searchTextfield = [SJUITextFieldFactory textFieldWithPlaceholder:@"搜索用户和房间" placeholderColor:RGBCOLOR(170, 170, 170) text:@"" font:[UIFont systemFontOfSize:14] textColor:GGTitle_Color keyboardType:UIKeyboardTypeDefault returnKeyType:UIReturnKeySearch backgroundColor:[UIColor whiteColor]];
    self.searchTextfield.frame = CGRectMake(CGRectGetMaxX(backBtn.frame), 0, kScreenW - CGRectGetMaxX(backBtn.frame) - 70, 44);
    [back addSubview:self.searchTextfield];
    self.searchTextfield.delegate = self;
    [self.searchTextfield becomeFirstResponder];
    
    self.searchBtn = [SJUIButtonFactory buttonWithTitle:@"搜索" titleColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:14] target:self sel:@selector(goSearch)];
    self.searchBtn.layer.masksToBounds = YES;
    self.searchBtn.layer.cornerRadius = 12;
    self.searchBtn.backgroundColor = Main_Color;
    self.searchBtn.frame = CGRectMake(kScreenW - 70, 19/2, 55, 25);
    [back addSubview:self.searchBtn];
    
    
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(back.frame), kScreenW, self.view.frame.size.height - CGRectGetMaxY(back.frame)) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 0.1)];
    [self.tableView setSeparatorColor:GGLine_Color];
    [self.view addSubview:self.tableView];
    self.tableView.backgroundColor = RGBCOLOR(241,241,241);
    
    self.tableView.estimatedRowHeight = 0;
    self.tableView.estimatedSectionHeaderHeight = 0;
    self.tableView.estimatedSectionFooterHeight = 0;
    if (@available(iOS 11.0, *)) {
        
        _tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        
    }
}


- (void)initUI
{
    
}


- (void)goBack
{
    [self.navigationController popViewControllerAnimated:YES];
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

