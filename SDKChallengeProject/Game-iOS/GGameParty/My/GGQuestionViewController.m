//
//  GGQuestionViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGQuestionViewController.h"
#import "GGQuestionModel.h"
#import "GGAnwserTableViewCell.h"
#import "GGAnwserViewController.h"
@interface GGQuestionViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic,strong)NSMutableArray *dataArr;

@property (nonatomic,strong)UIButton  *submitButton;


@end

@implementation GGQuestionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationController.navigationBarHidden = NO;
    
    self.dataArr = [NSMutableArray arrayWithCapacity:0];
    self.tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, self.view.frame.size.height - 64 - 20) style:UITableViewStyleGrouped];
   
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.tableFooterView = [UIView new];
    [self.tableView setSeparatorColor:GGLine_Color];
    self.tableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.tableView];
    self.tableView.allowsMultipleSelection = YES;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;

    [self queryQuestion];
    
    UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 5 + 45 + 20)];
    
    self.submitButton = [SJUIButtonFactory buttonWithTitle:@"提交答案" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(submitAnwser) tag:3];
    //self.submitButton.frame = CGRectMake(15, self.view.frame.size.height - 55 - 64, self.view.frame.size.width - 30, 45);
    
    self.submitButton.frame = CGRectMake(15, 5, kScreenW - 30, 45);
    self.submitButton.backgroundColor = RGBCOLOR(28, 181, 73);
    [self.submitButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.submitButton.layer.masksToBounds = YES;
    self.submitButton.layer.cornerRadius = 22.5;
    self.submitButton.titleLabel.font = [UIFont boldSystemFontOfSize:18];
    [back addSubview:self.submitButton];
    if (IS_IPHONE_X) {

//           self.submitButton.frame = CGRectMake(15, self.view.frame.size.height - 55 - 64 - 30, self.view.frame.size.width - 30, 45);
    }
    
    self.tableView.tableFooterView = back;
    
    //NSArray *indexPaths = self.tableView.indexPathsForSelectedRows;

}

- (void)submitAnwser
{
    NSArray *indexPaths = self.tableView.indexPathsForSelectedRows;
    if (indexPaths.count < self.dataArr.count)
    {
        //没填写完整
        [XHToast showCenterWithText:@"有选项没填写"];
    }
    if (indexPaths.count == self.dataArr.count)
    {
        //验证答案
        NSInteger score = 0;
        for (int i = 0; i<self.dataArr.count; i++)
        {
            NSIndexPath *indexPath = indexPaths[i];
            GGQuestionModel *model = self.dataArr[i];
            if (indexPath.row == [model.anwser integerValue] - 1) {
                //正确
                score = score + 1;
            }
        }
        if (score >= self.dataArr.count - 2)
        {
            [MBProgressHUD showHUDAddedTo:self.view animated:YES];
            AVUser *user = [AVUser currentUser];
            [user addUniqueObject:self.gameId forKey:@"allowGame"];
            [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                [MBProgressHUD hideHUDForView:self.view animated:YES];
                if (succeeded)
                {
                    [[NSNotificationCenter defaultCenter]postNotificationName:GGUSER_GAME_lIMITS_UPDATE object:nil];
                    if (self.anwserCompleteBlock) {
                        self.anwserCompleteBlock(self, self.gameId, YES);
                        [self.navigationController popViewControllerAnimated:YES];
                    }
                    else
                    {
                        GGAnwserViewController *an = [[GGAnwserViewController alloc]init];
                        an.allScore = self.dataArr.count;
                        an.score = score;
                        [self.navigationController pushViewController:an animated:YES];
                    }
                }
                else
                {
                    [XHToast showBottomWithText:@"提交失败,请重试"];
                }
            }];
        }
        else
        {
            GGAnwserViewController *an = [[GGAnwserViewController alloc]init];
            an.allScore = self.dataArr.count;
            an.score = score;
            [self.navigationController pushViewController:an animated:YES];
        }
        
    }
}

- (void)queryQuestion
{
    [GGQuestionModel queryQuestionWithGame:self.gameId withBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            for(AVObject *obj in objects)
            {
                GGQuestionModel *model = [GGQuestionModel valueToObj:obj];
                [self.dataArr addObject:model];
            }
            [self.tableView reloadData];
        }
        else
        {
            [XHToast showCenterWithText:@"网络错误,请重试"];
        }
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}
//
//- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
//{
//    GGQuestionModel *model = self.dataArr[section];
//    NSString *string = [NSString stringWithFormat:@"%ld:%@",(long)section + 1, model.question];
//    return string;
//}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 40;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    GGQuestionModel *model = self.dataArr[section];
    UIView *back = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 40)];
    back.backgroundColor = [UIColor whiteColor];
    
    UILabel *label = [SJUILabelFactory labelWithText:[NSString stringWithFormat:@"%ld",(long)section + 1] textColor:[UIColor whiteColor]];
    label.textAlignment = NSTextAlignmentCenter;
    label.backgroundColor = [UIColor blackColor];
    label.frame = CGRectMake(20, 15/2, 25, 25);
    label.layer.masksToBounds = YES;
    label.layer.cornerRadius = 12.5;
    [back addSubview:label];
    
    
    
    UILabel *text = [SJUILabelFactory labelWithText:[NSString stringWithFormat:@"%@",model.question] textColor:[UIColor blackColor]];
    text.frame = CGRectMake(CGRectGetMaxX(label.frame) + 10, 0, kScreenW - CGRectGetMaxX(label.frame) - 10, 40);
    [back addSubview:text];
    text.numberOfLines = 0;
    
//    UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, 1)];
//    line.backgroundColor = RGBCOLOR(170, 170, 170);
//    [back addSubview:line];
//    
    return back;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    GGQuestionModel *model = self.dataArr[section];
    return model.option.count;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *iden = @"iden";
    GGAnwserTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:iden];
    if (cell == nil)
    {
        cell = [[GGAnwserTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:iden];
    }
    GGQuestionModel *model = self.dataArr[indexPath.section];
    
    NSString *oo = @"A";
    if (indexPath.row == 0)
    {
        oo = @"A";
    }
    if (indexPath.row == 1)
    {
        oo = @"B";
    }
    if (indexPath.row == 2)
    {
        oo = @"C";
    }
    if (indexPath.row == 3)
    {
        oo = @"D";
    }
    NSString *string = [NSString stringWithFormat:@"%@:%@",oo, model.option[indexPath.row]];
    cell.textLabel.text = string;
    cell.textLabel.highlightedTextColor = [UIColor whiteColor];
    cell.textLabel.textColor = GGTitle_Color;
    cell.selectedBackgroundView = [[UIView alloc] initWithFrame:cell.frame] ;
    cell.selectedBackgroundView.backgroundColor = RGBCOLOR(51, 51, 51);

//    cell.selectionStyle =
//    cell.accessoryType = UITableViewCellAccessoryCheckmark;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
//    [tableView deselectRowAtIndexPath:indexPath animated:YES];
//    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
//    if (cell.accessoryType == UITableViewCellAccessoryCheckmark)
//    {
//        cell.accessoryType = UITableViewCellAccessoryNone;
//    }
//    else
//    {
//        cell.accessoryType = UITableViewCellAccessoryCheckmark;
//    }
    GGQuestionModel *model = self.dataArr[indexPath.section];
    NSArray *arr = model.option;
    
    for (int i = 0; i<arr.count; i++)
    {
        NSIndexPath *seleInexPath = [NSIndexPath indexPathForRow:i inSection:indexPath.section];
//        GGAnwserTableViewCell *cell = [tableView cellForRowAtIndexPath:seleInexPath];
        [tableView deselectRowAtIndexPath:seleInexPath animated:YES];
    }
    [tableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionNone];
    
}

- (void)addQuestion
{
    
    AVObject *obj = [AVObject objectWithClassName:DB_GAME_Question];
    [obj setObject:self.gameId forKey:@"game"];
    [obj setObject:@"请问哪辆车能承受5发以上的RPG" forKey:@"question"];
    NSArray *arr = @[@"坦克",@"骷髅马",@"T20",@"破败"];
    [obj setObject:arr forKey:@"option"];
    [obj setObject:[NSNumber numberWithInt:1] forKey:@"anwser"];
    NSArray *avarr = @[obj,obj,obj,obj,obj,obj,obj,obj,obj,obj];
    [AVObject saveAllInBackground:avarr];
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
