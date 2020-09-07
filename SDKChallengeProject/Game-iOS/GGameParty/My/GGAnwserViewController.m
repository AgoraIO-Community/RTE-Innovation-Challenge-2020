//
//  GGAnwserViewController.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAnwserViewController.h"
#import "GGQAViewController.h"
@interface GGAnwserViewController ()

@end

@implementation GGAnwserViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIImageView *image = [[UIImageView alloc]initWithFrame:CGRectMake(0, 64, kScreenW, 200-64)];
    image.image = [UIImage imageNamed:@"icon_tongg"];
    image.contentMode = UIViewContentModeCenter;
    [self.view addSubview:image];
    
    UILabel *jieguo = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color font:[UIFont systemFontOfSize:18]];
    jieguo.frame = CGRectMake(0, CGRectGetMaxY(image.frame), kScreenW, 30);
    jieguo.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:jieguo];
    
    UILabel *jieguo2 = [SJUILabelFactory labelWithText:@"" textColor:GGTitle_Color font:[UIFont systemFontOfSize:16]];
    jieguo2.frame = CGRectMake(0, CGRectGetMaxY(jieguo.frame), kScreenW, 20);
    jieguo2.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:jieguo2];
    
    UILabel *jieguo3 = [SJUILabelFactory labelWithText:@"" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:13]];
    jieguo3.frame = CGRectMake(0, CGRectGetMaxY(jieguo2.frame)+10, kScreenW, 20);
    jieguo3.textAlignment = NSTextAlignmentCenter;
    [self.view addSubview:jieguo3];
    
    UIButton *back = [SJUIButtonFactory buttonWithTitle:@"返回" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(back) tag:3];
    back.frame = CGRectMake(15,CGRectGetMaxY(jieguo3.frame)+30, self.view.frame.size.width - 30, 45);
    back.backgroundColor = RGBCOLOR(28, 181, 73);
    [back setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    back.layer.masksToBounds = YES;
    back.layer.cornerRadius = 22.5;
    back.titleLabel.font = [UIFont boldSystemFontOfSize:18];
    [self.view addSubview:back];
    
    if (self.score >= self.allScore - 2)
    {
        self.title = @"测试通过";
        jieguo.text = @"测试通过";
        jieguo2.text = [NSString stringWithFormat:@"答对%ld道,答错%d道",(long)_score,self.allScore-_score];
        jieguo3.text = @"你现在可以在相关游戏板块中进行组队啦";
        //提交数据
       image.image = [UIImage imageNamed:@"icon_tongg"];

        
    }
    else
    {
        image.image = [UIImage imageNamed:@"icon_butongg"];
        self.title = @"测试不通过";
        jieguo.text = @"测试不通过";
        jieguo2.text = [NSString stringWithFormat:@"答对%ld道,答错%d道",(long)_score,self.allScore-_score];
        jieguo3.text = @"不用灰心,您可以继续重新测试";
    }
    
}

- (void)back
{
    BOOL isExst = NO;
    for (UIViewController *controller in self.navigationController.viewControllers)
    {
        
        if ([controller isKindOfClass:[GGQAViewController class]])
        {
            isExst = YES;
            GGQAViewController *A =(GGQAViewController *)controller;
            [self.navigationController popToViewController:A animated:YES];
            return;
        }
        
    }
    
    if (isExst == NO) {
        [self.navigationController popToRootViewControllerAnimated:YES];
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
