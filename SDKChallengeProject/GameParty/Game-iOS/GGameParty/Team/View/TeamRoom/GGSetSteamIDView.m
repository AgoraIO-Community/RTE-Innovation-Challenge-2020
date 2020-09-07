//
//  GGSetSteamIDView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSetSteamIDView.h"


@interface GGSetSteamIDView()


@end

@implementation GGSetSteamIDView


- (instancetype)init
{
    return [self initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame withSteamId:(NSString *)steamID
{
    self.steamId = steamID;
    return [self initWithFrame:frame];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    
    if ( self )
    {
        
        self.waitbackView = [UIView new];
        [self addSubview:self.waitbackView];
        self.waitbackView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.waitbackView.layer.cornerRadius = 10;
        self.waitbackView.clipsToBounds = YES;
        self.waitbackView.backgroundColor = [UIColor whiteColor];
        
        self.activityIndicator = [[UIActivityIndicatorView alloc]initWithFrame:self.waitbackView.bounds];
        self.activityIndicator.backgroundColor = [UIColor whiteColor];
        self.activityIndicator.activityIndicatorViewStyle =  UIActivityIndicatorViewStyleWhiteLarge;
        [self.waitbackView addSubview:self.activityIndicator];
        self.activityIndicator.color = [UIColor redColor];
        [self.activityIndicator startAnimating];
        
        
        
        self.backView = [UIView new];
        [self addSubview:self.backView];
        self.backView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.backView.layer.cornerRadius = 10;
        self.backView.clipsToBounds = YES;
        self.backView.backgroundColor = [UIColor whiteColor];
        
        self.btnClose = [UIButton mm_buttonWithTarget:self action:@selector(actionClose)];
        [self.backView addSubview:self.btnClose];
        self.btnClose.frame = CGRectMake(self.frame.size.width - 50, 5, 40, 40);
        //  [self.btnClose setTitle:@"Close" forState:UIControlStateNormal];
        [self.btnClose setImage:[UIImage imageNamed:@"icon_guanbi"] forState:UIControlStateNormal];
        [self.btnClose setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        self.btnClose.titleLabel.font = [UIFont systemFontOfSize:14];
        
        self.lblStatus = [UILabel new];
        [self.backView addSubview:self.lblStatus];
        self.lblStatus.frame = CGRectMake(16, 16, 145, 26);
        self.lblStatus.textColor = MMHexColor(0x333333FF);
        self.lblStatus.font = [UIFont boldSystemFontOfSize:23];
        self.lblStatus.text = @"设置快捷短语";
        self.lblStatus.textAlignment = NSTextAlignmentLeft;
        
        
       // UILabel *tips = [SJUILabelFactory labelWithText:@"设置后可快速发送至公屏,可在\"我的\"中修改" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:12]];
         UILabel *tips = [SJUILabelFactory labelWithText:@"设置后可快速发送,如设为你的游戏id" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:12]];
        tips.frame = CGRectMake(self.lblStatus.frame.origin.x, CGRectGetMaxY(self.lblStatus.frame) + 5, self.frame.size.width, 16);
        [self.backView addSubview:tips];
        self.tipsLabel = tips;
        
        
        self.titleTextField = [[UITextField alloc]initWithFrame:CGRectMake(self.lblStatus.frame.origin.x, CGRectGetMaxY(tips.frame) + 20, self.frame.size.width  - self.lblStatus.frame.origin.x * 2, 25)];
        self.titleTextField.placeholder = @"使用快捷短语";
        self.titleTextField.text = self.steamId;
        [self.backView addSubview:self.titleTextField];
        self.titleTextField.font = [UIFont systemFontOfSize:14];
//        [self.titleTextField setValue:[UIFont systemFontOfSize:14.f weight:UIFontWeightThin] forKeyPath:@"_placeholderLabel.font"];
        
           self.titleTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"使用快捷短语"attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:14.f weight:UIFontWeightThin] }];
        
        UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(self.titleTextField.frame.origin.x, CGRectGetMaxY(self.titleTextField.frame)+2, self.titleTextField.frame.size.width, 1)];
        line1.backgroundColor = RGBCOLOR(238, 238, 238);
        [self.backView addSubview:line1];
        
        
        self.sureButton = [SJUIButtonFactory buttonWithTitle:@"保存并发送" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(sureSaveData) tag:3];
        self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(line1.frame) + 20, self.frame.size.width - 30, 45);
        self.sureButton.backgroundColor = RGBCOLOR(28, 181, 73);
        [self.sureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.sureButton.layer.masksToBounds = YES;
        self.sureButton.layer.cornerRadius = 22.5;
        self.sureButton.titleLabel.font = [UIFont boldSystemFontOfSize:18];
        [self.backView addSubview:self.sureButton];
    }
    return self;
}


- (void)sureSaveData
{
    
    NSString *title = self.titleTextField.text;
  
    if (title.length == 0)
    {
        
    }
    else
    {
        [self.titleTextField resignFirstResponder];
        [self.backView setHidden:YES];
          AVUser *user = [AVUser currentUser];
        [user setObject:self.titleTextField.text forKey:@"steamID"];
        [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
            [self.backView setHidden:NO];
            if (succeeded)
            {
                 [self actionClose];
                if (self.sureBtnClick)
                {
                    self.sureBtnClick(self, self.sureButton);
                }
                else
                {
                     [XHToast showCenterWithText:@"服务器出现错误,稍后再试"];
                }
            }
        }];
    }
}


- (void)actionClose
{
    //  [self hide];
    if (self.closeBtnClick)
    {
        self.closeBtnClick(self);
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
