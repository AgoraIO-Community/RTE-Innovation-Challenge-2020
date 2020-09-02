//
//  GGAccooutSafeViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAccooutSafeViewController.h"
#import "GGChangePhoneView.h"
@interface GGAccooutSafeViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic,strong)UITableView *tableView;
@end

@implementation GGAccooutSafeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"帐号与安全";
    self.navigationController.navigationBarHidden = NO;
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.backgroundColor = GGBackGround_Color;
    [self.tableView setSeparatorColor:GGLine_Color];

    [self.view addSubview:self.tableView];
    
}



- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1;
}


- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [UIView new];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:iden];
    }
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;

    cell.textLabel.text = @[@"绑定手机号",@"绑定微信号",@"切换帐号"][indexPath.section];
    BOOL isPhone = [AVUser currentUser].mobilePhoneNumber.length == 0;
    BOOL isAuth = [[[AVUser currentUser] objectForKey:@"authData"] count] == 0;
    NSString *phone = isPhone?@"点击绑定":[AVUser currentUser].mobilePhoneNumber;
    cell.detailTextLabel.text = @[phone,isAuth?@"点击绑定":@"已绑定",@""][indexPath.section];
    
    cell.detailTextLabel.font = [UIFont systemFontOfSize:13];
    cell.textLabel.textColor = GGTitle_Color;
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return @[@"绑定后即可使用手机号码登录,更安全放心",@"授权后即可使用微信快捷登录",@"退出后将不能收到新消息"][section];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0)
    {
        [self changePhone];
    }
    if (indexPath.section == 1)
    {
        BOOL isAuth = [[[AVUser currentUser] objectForKey:@"authData"] count] == 0;
        if (isAuth)
        {
            [self bangdingWechat];
        }
    }
    if (indexPath.section == 2)
    {
        UIAlertController *alertLength = [UIAlertController alertControllerWithTitle:@"警告" message:@"退出后将无法接受新消息,是否确认退出" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *suer = [UIAlertAction actionWithTitle:@"退出登录" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            [[NSNotificationCenter defaultCenter]postNotificationName:LOGIN_OUT_NOTIFATION object:nil];

        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
        [alertLength addAction:suer];
        [alertLength addAction:cancel];
        [self presentViewController:alertLength animated:YES completion:nil];
    }
}

- (void)bangdingWechat
{
    [ShareSDK getUserInfo:SSDKPlatformTypeWechat
           onStateChanged:^(SSDKResponseState state, SSDKUser *user, NSError *error)
     {
         if (state == SSDKResponseStateSuccess)
         {
             NSNumber * platform = [NSNumber numberWithInteger:3];
             NSString * access_token = user.credential.token;
             NSString * uid = user.uid;
             NSDictionary *dic = @{@"platform":platform,
                                   @"access_token":access_token,
                                   @"openid":uid,
                                   @"expires_at":[NSDate date],
                                   @"id":uid
                                   };
             AVUser *userer = [AVUser currentUser];
             [userer setObject:dic forKey:@"authData"];
             [userer saveInBackground];
         }
         else
         {
             NSLog(@"%@",error);
         }
         
     }];
}

- (void)changePhone
{
    CGRect rect = CGRectMake(15, 0, kScreenW - 30, 250);
    
    GGChangePhoneView *view = [[GGChangePhoneView alloc]initWithFrame:rect];
    __weak typeof(self) weak_self = self;
    __weak typeof(view) weak_kbview1 = view;
    BOOL isPhone = [AVUser currentUser].mobilePhoneNumber.length == 0;

    if (isPhone) {
        view.lblStatus.text = @"绑定手机号";
    }
    
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = NO;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    //zh_KeyboardView *kbview1 = [[zh_KeyboardView alloc] initWithFrame:rect];
    view.successChangeBlock = ^(GGChangePhoneView *loginView, NSString *phone) {
        [[AVUser currentUser]refreshInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
            if (!error) {
                [weak_self.tableView reloadData];
            }
        }];
    };
    view.closeBtnClick = ^(GGLoginView *loginView) {
        [self.zh_popupController dismiss];
    };
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
