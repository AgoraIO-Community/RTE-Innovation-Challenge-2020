//
//  GGChangeDataViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChangeDataViewController.h"
#import "GGSetSteamIDView.h"
#import "GGSetUserNameView.h"
//#import "GGUserAgePickerView.h"
#import "QFDatePickerView.h"
@interface GGChangeDataViewController ()<UITableViewDelegate,UITableViewDataSource,
UIImagePickerControllerDelegate,UINavigationControllerDelegate>
@property (nonatomic,strong)UIImageView *headImage;
@property (nonatomic,strong)UILabel *nameLabel;
@property (nonatomic,strong)UIButton *manBtn;

@property (nonatomic,strong)UIButton *nvBtn;

@property (nonatomic,strong)UITableView *tableView;

@property (nonatomic,strong)UILabel *ageLabel;

@property(nonatomic,strong) UIImagePickerController *imagePicker; //声明全局的UIImagePickerController


@end

@implementation GGChangeDataViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    //[self initNavView];
    self.ggNavView.titleLabel.text = @"修改个人资料";
    self.title = @"修改个人资料";
    
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height) style:UITableViewStyleGrouped];
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [self.view addSubview:self.tableView];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = GGBackGround_Color;

}


#pragma mark - table.Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return section == 0?1:3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
//    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
//    if (cell == nil)
//    {
//
//    }
      UITableViewCell *cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:iden];
    if (indexPath.section == 0)
    {
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        cell.detailTextLabel.textColor = GGTitle_Color;
        cell.detailTextLabel.text = @"更换头像";
        AVFile *file = [[AVUser currentUser] objectForKey:@"avatar"];
        NSString *url = file.url;
        [cell.imageView setImageWithURL:[NSURL URLWithString:url] placeholderImage:GGDefault_User_Head];
        cell.imageView.layer.masksToBounds = YES;
        cell.imageView.layer.cornerRadius = 30;
        CGSize itemSize = CGSizeMake(60, 60);
        UIGraphicsBeginImageContextWithOptions(itemSize, NO, UIScreen.mainScreen.scale);
        CGRect imageRect = CGRectMake(0.0, 0.0, itemSize.width, itemSize.height);
        [cell.imageView.image drawInRect:imageRect];
        cell.imageView.image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    }
    else
    {
        cell.textLabel.text = @[@"我的昵称",@"年龄",@"性别"][indexPath.row];
        if (indexPath.row!=2)
        {
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            cell.detailTextLabel.textColor = GGTitle_Color;
           cell.detailTextLabel.text = @[[AVUser currentUser].username,[NSString stringWithFormat:@"%@",[[AVUser currentUser] objectForKey:@"age"]]][indexPath.row];
        }
        cell.textLabel.textColor = RGBCOLOR(170, 170, 170);
        if (indexPath.row == 2)
        {
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

            UIButton *man = [UIButton buttonWithType:UIButtonTypeCustom];
            [man setImage:[UIImage imageNamed:@"icon_nande2"] forState:UIControlStateNormal];
            man.frame = CGRectMake(kScreenW - 165, 17, 65, 28);
            [cell.contentView addSubview:man];
            man.tag = 1;
            self.manBtn = man;
            [man addTarget:self action:@selector(chooseSex:) forControlEvents:UIControlEventTouchUpInside];
            
            
            UIButton *nv = [UIButton buttonWithType:UIButtonTypeCustom];
            [nv setImage:[UIImage imageNamed:@"icon_nvde"] forState:UIControlStateNormal];
            nv.frame = CGRectMake(kScreenW - 165 + 15 + 65, 17, 65, 28);
            [cell.contentView addSubview:nv];
            nv.tag = 2;
            self.nvBtn = nv;
            
            [self.manBtn addTarget:self action:@selector(chooseSex:) forControlEvents:UIControlEventTouchUpInside];
            [self.nvBtn addTarget:self action:@selector(chooseSex:) forControlEvents:UIControlEventTouchUpInside];
            
            AVUser *currentUser = [AVUser currentUser];
            BOOL sex = [[currentUser objectForKey:@"sex"] boolValue];
            if (sex) {
                  [man setImage:[UIImage imageNamed:@"icon_nande"] forState:UIControlStateNormal];
                [nv setImage:[UIImage imageNamed:@"icon_nvde2"] forState:UIControlStateNormal];
            }
            else{
                 [nv setImage:[UIImage imageNamed:@"icon_nvde"] forState:UIControlStateNormal];
                [man setImage:[UIImage imageNamed:@"icon_nande2"] forState:UIControlStateNormal];

            }
        }
     
    }
    return cell;
}

- (void)chooseSex:(UIButton *)btn
{
    if (btn.tag == 1) {
        [self.manBtn setImage:[UIImage imageNamed:@"icon_nande"] forState:UIControlStateNormal];
          [self.nvBtn setImage:[UIImage imageNamed:@"icon_nvde2"] forState:UIControlStateNormal];
    }
    else
    {
        [self.manBtn setImage:[UIImage imageNamed:@"icon_nande2"] forState:UIControlStateNormal];
        [self.nvBtn setImage:[UIImage imageNamed:@"icon_nvde"] forState:UIControlStateNormal];
    }
    
    NSInteger tag = btn.tag;
    AVUser *user = [AVUser currentUser];
    [user setObject:tag == 1?@(YES):@(NO) forKey:@"sex"];
    [user saveInBackground];
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return indexPath.section == 0?90:62;
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 10;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    return [UIView new];
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    return [UIView new];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (indexPath.section == 0)
    {
        [self choosePhoto];
    }
    else
    {
        if (indexPath.row == 0)
        {
            [self updateName];
        }
        if (indexPath.row == 1)
        {
            [self updateAge];
        }
        if (indexPath.row == 3)
        {
            
        }
    }
}

- (void)updateAge
{
    QFDatePickerView *datePickerView = [[QFDatePickerView alloc]initYearPickerViewWithResponse:^(NSString *str) {
        if ([str isEqualToString:@"至今"])
        {
            str = @"0";
        }
        else
        {
            str = [NSString stringWithFormat:@"%ld",2018 - [str integerValue]];
        }
        AVUser *user = [AVUser currentUser];
        [user setObject:[NSNumber numberWithInteger:[str integerValue]] forKey:@"age"];
        [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
            [MBProgressHUD hideHUDForView:self.view animated:YES];
            if (succeeded)
            {
                 [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:1] withRowAnimation:UITableViewRowAnimationFade];
            }
            else
            {
                [XHToast showCenterWithText:@"网络错误,上传失败,请重试"];
            }
        }];
    }];
    [datePickerView show];
  
}

- (void)updateName
{
    CGRect rect = CGRectMake(10, 0, kScreenW - 20, 200);
    NSString *steamId = [[AVUser currentUser] objectForKey:@"username"];
    GGSetUserNameView *view = [[GGSetUserNameView alloc]initWithFrame:rect withSteamId:steamId];
    view.lblStatus.text = @"修改昵称";
    view.tipsLabel.text = @"";
    view.titleTextField.placeholder = @"输入你的昵称";
    self.zh_popupController = [zhPopupController popupControllerWithMaskType:zhPopupMaskTypeBlackTranslucent];
    self.zh_popupController.layoutType = zhPopupLayoutTypeBottom;
    self.zh_popupController.dismissOnMaskTouched = YES;
    [self.zh_popupController presentContentView:view duration:0.25 springAnimated:NO];
    
    view.closeBtnClick = ^(GGSetSteamIDView *userInfoCard)
    {
        [self.zh_popupController dismiss];
    };
    
    view.sureBtnClick = ^(GGSetSteamIDView *view, UIButton *button) {
        [self.tableView reloadSections:[NSIndexSet indexSetWithIndex:1] withRowAnimation:UITableViewRowAnimationFade];
    };
}

- (void)choosePhoto
{
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    NSUInteger sourceType = 0;

    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
    {
        imagePickerController.delegate = self; //设置代理
        imagePickerController.allowsEditing = YES;
        imagePickerController.sourceType = sourceType; //图片来源
    }
    sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.sourceType = sourceType;
    
    [self presentViewController:imagePickerController animated:YES completion:^{
        [MBProgressHUD hideHUDForView:self.view animated:YES];
    }];
}

//2.x  用户选中图片之后的回调
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingImage:(UIImage *)image editingInfo:(NSDictionary *)editingInfo
{
    NSMutableDictionary * dict= [NSMutableDictionary dictionaryWithDictionary:editingInfo];
    
    [dict setObject:image forKey:@"UIImagePickerControllerEditedImage"];
    
    //直接调用3.x的处理函数
    [self imagePickerController:picker didFinishPickingMediaWithInfo:dict];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissViewControllerAnimated:YES completion:^{}];
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage]; //通过key值获取到图片
//    NSData *imageData = UIImagePNGRepresentation(image);
    NSData *imageData = UIImageJPEGRepresentation(image, 0.5);
    
    AVFile *file = [AVFile fileWithData:imageData];
    AVUser *user = [AVUser currentUser];
    [user setObject:file forKey:@"avatar"];
    [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        if (succeeded) {
               [self.tableView reloadData];
        }
        else
        {
            [XHToast showCenterWithText:@"网络错误,上传失败,请重试"];
        }
    }];
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
