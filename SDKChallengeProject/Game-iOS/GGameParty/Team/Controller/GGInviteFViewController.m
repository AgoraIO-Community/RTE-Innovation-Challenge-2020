//
//  GGInviteFViewController.m
//  GGameParty
//
//  Created by Victor on 2018/9/12.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGInviteFViewController.h"

@interface GGInviteFViewController ()
@property (nonatomic,strong)UIButton  *sureButton;

@property (nonatomic,strong)UIImageView  *jhmbgImage;

@property (nonatomic,strong)UIButton  *haveGiveButton;

@property (nonatomic,strong)UILabel  *jhmLabel;

@end

@implementation GGInviteFViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.navigationController.navigationBarHidden = NO;
    self.title = @"欢喜加一";
    
    
    
    UIImageView *image = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, kScreenW, kScreenW * 0.693)];
    image.image = [UIImage imageNamed:@"jihuoma1"];
//    image.contentMode = UIViewContentModeCenter;
    [self.view addSubview:image];
    image.backgroundColor = [UIColor redColor];
    self.jhmbgImage = image;
    
    UIButton *getBtn = [SJUIButtonFactory buttonWithTitle:@"已发放奖励" titleColor:RGBCOLOR(170, 170, 170)];
    getBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    getBtn.frame = CGRectMake(0, image.frame.size.height/2, kScreenW, 40);
    [image addSubview:getBtn];
    getBtn.hidden = YES;
    self.haveGiveButton = getBtn;
    
    self.jhmLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, kScreenW, image.frame.size.height/2)];
    self.jhmLabel.textAlignment = NSTextAlignmentCenter;
    self.jhmLabel.numberOfLines = 2;
    self.jhmLabel.font = [UIFont systemFontOfSize:16];
    self.jhmLabel.textColor = [UIColor whiteColor];
    [image addSubview:self.jhmLabel];
    
    
    UILabel *tips = [[UILabel alloc]initWithFrame:CGRectMake(20, CGRectGetMaxY(image.frame) - 60, kScreenW - 40, 100)];
    tips.textColor = RGBCOLOR(170, 170, 170);
    tips.font = [UIFont systemFontOfSize:12];
    tips.numberOfLines = 0;
//    tips.backgroundColor = [UIColor redColor ];
    NSString *text = @"领取条件\n\n在应用商店中给app好评并评论：飞鸟昵称+评论内容\n\n工作人员在一周内核实后在此页面领取您的激活码";
    tips.text = text;
    [self.view addSubview:tips];
    
    CGFloat height = [GGAppTool sizeWithText:text font:[UIFont systemFontOfSize:12] maxSize:CGSizeMake(kScreenW - 40, 999)].height;
    tips.frame = CGRectMake(20, CGRectGetMaxY(image.frame) - 60, kScreenW - 40, height);
    
    self.sureButton = [SJUIButtonFactory buttonWithTitle:@"去应用商店给APP好评" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(sureSaveData) tag:3];
    self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(tips.frame) + 20, self.view.frame.size.width - 30, 45);
    self.sureButton.backgroundColor = RGBCOLOR(28, 181, 73);
    [self.sureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.sureButton.layer.masksToBounds = YES;
    self.sureButton.layer.cornerRadius = 22.5;
    self.sureButton.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    [self.view addSubview:self.sureButton];
    
    [self loadData];
}

- (void)loadData
{
    [[AVUser currentUser] refreshInBackgroundWithBlock:^(AVObject * _Nullable object, NSError * _Nullable error) {
        AVUser *user = [AVUser currentUser];
        NSString *code = [user objectForKey:@"gameCode"];
        if ([code length] > 0) {
            //已发放
            self.jhmbgImage.image = [UIImage imageNamed:@"jihuomabg"];
            self.haveGiveButton.hidden = NO;
            [self.sureButton setTitle:@"复制激活码后前往Steam领取" forState:UIControlStateNormal];
            self.jhmLabel.text = [NSString stringWithFormat:@"激活码\n%@",code];
            
        }
        else
        {
            //未发放
        }
    }];
}

- (void)sureSaveData
{
    AVUser *user = [AVUser currentUser];
    if ([[user objectForKey:@"gameCode"] length] > 0)
    {
        NSString *str = [NSString stringWithFormat:@"%@",[user objectForKey:@"gameCode"]];
        UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
        pasteboard.string = str;
        [XHToast showCenterWithText:@"已复制"];
    }
    else
    {
    [[UIApplication sharedApplication]openURL:[NSURL URLWithString:APPURL]];
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
