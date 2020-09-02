//
//  GGChangePhoneView.m
//  GGameParty
//
//  Created by Victor on 2018/8/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGChangePhoneView.h"

@implementation GGChangePhoneView

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
        self.activityIndicator.color = Main_Color;
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
        self.lblStatus.font = [UIFont boldSystemFontOfSize:28];
        self.lblStatus.text = @"修改手机号";
        self.lblStatus.textAlignment = NSTextAlignmentLeft;
        
        
        
        self.phoneTextField = [[LRTextField alloc] initWithFrame:CGRectMake(22, 60, self.frame.size.width - 44, 35) labelHeight:15];
        self.phoneTextField.font = [UIFont systemFontOfSize:16];
        //        self.phoneTextField.placeholderInactiveColor =  HEXCOLOR(@"#cccccc");
        self.phoneTextField.placeholder = @"输入手机号码";
        self.phoneTextField.format = @"###-####-####";
        self.phoneTextField.borderStyle = UITextBorderStyleNone;
        self.phoneTextField.keyboardType = UIKeyboardTypeNumberPad;
        [self.backView addSubview:self.phoneTextField];
        [self.phoneTextField setValidationBlock:^NSDictionary *(LRTextField *textField, NSString *text) {
           
            if ([[text stringByReplacingOccurrencesOfString:@"-" withString:@""] length] != 11)
            {
//                textField.placeholder = @"输入手机号码";
//                textField.placeholderInactiveColor = [[UIColor grayColor] colorWithAlphaComponent:0.7];
//
                textField.placeholderInactiveColor = [UIColor redColor];
                textField.placeholder = @"手机号码格式不正确";
                
                return @{};
            }
            else
            {
                textField.placeholderInactiveColor = [UIColor grayColor];
                textField.placeholder = @"可用的手机号";
            }
            
            return @{};
        }];
        
        UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(self.phoneTextField.frame.origin.x, CGRectGetMaxY(self.phoneTextField.frame), self.phoneTextField.frame.size.width, 1)];
        line1.backgroundColor = RGBCOLOR(238, 238, 238);
        [self.backView addSubview:line1];
        
        
        self.codeTextField = [[LRTextField alloc] initWithFrame: CGRectMake(self.phoneTextField.frame.origin.x, CGRectGetMaxY(self.phoneTextField.frame) + 27, self.phoneTextField.frame.size.width - 100, 35) labelHeight:15];
        self.codeTextField.placeholder = @"输入验证码";
        self.codeTextField.keyboardType = UIKeyboardTypeNumberPad;
        self.codeTextField.borderStyle = UITextBorderStyleNone;
        self.codeTextField.hintTextColor = RGBCOLOR(239,77,77);
        [self.backView addSubview:self.codeTextField];
        
        
        UIView *line2 = [[UIView alloc]initWithFrame:CGRectMake(self.codeTextField.frame.origin.x, CGRectGetMaxY(self.codeTextField.frame), self.codeTextField.frame.size.width - 20, 1)];
        line2.backgroundColor = RGBCOLOR(238, 238, 238);
        [self.backView addSubview:line2];
        
        self.sendCodeBtn = [SJUIButtonFactory buttonWithTitle:@"发送验证码" titleColor:RGBCOLOR(0,153,241) backgroundColor:[UIColor whiteColor] imageName:@"" target:self sel:@selector(sendCode) tag:2];
        
        self.sendCodeBtn.frame = CGRectMake(CGRectGetMaxX(self.codeTextField.frame), self.codeTextField.frame.origin.y, 100, 35);
        self.sendCodeBtn.titleLabel.font = [UIFont systemFontOfSize:14];
        self.sendCodeBtn.layer.borderColor = [[UIColor colorWithRed:0.0f/255.0f green:153.0f/255.0f blue:241.0f/255.0f alpha:1.0f] CGColor];
        self.sendCodeBtn.layer.borderWidth = 1;
        self.sendCodeBtn.layer.masksToBounds = YES;
        self.sendCodeBtn.layer.cornerRadius = 17.5;
        
        [self.backView addSubview:self.sendCodeBtn];
        
        
        self.loginButton = [SJUIButtonFactory buttonWithTitle:@"确认修改" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(login) tag:3];
        self.loginButton.frame = CGRectMake(15, CGRectGetMaxY(line2.frame) + 20, self.frame.size.width - 30, 45);
        self.loginButton.backgroundColor = RGBCOLOR(28, 181, 73);
        [self.loginButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.loginButton.layer.masksToBounds = YES;
        self.loginButton.layer.cornerRadius = 22.5;
        self.loginButton.titleLabel.font = [UIFont boldSystemFontOfSize:18];
        [self.backView addSubview:self.loginButton];
        
        
        // [self.phoneTextField becomeFirstResponder];
        
    }
    
    return self;
}
- (void)sendCode
{
    
    NSString *phone = [self.phoneTextField.text stringByReplacingOccurrencesOfString:@"-" withString:@""];
    if (phone.length == 11)
    {
        AVQuery *query = [AVQuery queryWithClassName:DB_USER];
        [query whereKey:@"mobilePhoneNumber" equalTo:phone];
        [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            if (!error) {
                if (objects.count == 0) {
                    AVShortMessageRequestOptions *options = [[AVShortMessageRequestOptions alloc] init];
                    options.TTL = 10;                      // 验证码有效时间为 10 分钟
                    options.applicationName = @"飞鸟语音";  // 应用名称
                    options.operation = @"修改账户手机号";        // 操作名称
                    [AVSMS requestShortMessageForPhoneNumber:phone
                                                     options:options
                                                    callback:^(BOOL succeeded, NSError * _Nullable error) {
                                                        if (succeeded) {
                                                            /* 请求成功 */
                                                            [self receiveCheckNumButton];
                                                            
                                                            
                                                        } else {
                                                            /* 请求失败 */
                                                            if (error.code == 602)
                                                            {
                                                                [XHToast showCenterWithText:@"服务器出现错误,稍后再试"];
                                                            }
                                                            if (error.code == 601)
                                                            {
                                                                [XHToast showCenterWithText:@"今日此号码短信已用尽,请明日再试"];
                                                            }
                                                            
                                                        }
                                                    }];
                }
                else
                {
                    self.phoneTextField.placeholder = @"此号码已被占用,请更换";
                }
            }
        }];
        
        
        
    }
}

- (void)login
{
    if ([self valivaForm])
    {
        NSString *phoneNumber = [self.phoneTextField.text stringByReplacingOccurrencesOfString:@"-" withString:@""];
        NSString *smscode = self.codeTextField.text;
        [self.phoneTextField resignFirstResponder];
        [self.codeTextField resignFirstResponder];
        [self.backView setHidden:YES];
        [AVOSCloud verifySmsCode:smscode mobilePhoneNumber:phoneNumber callback:^(BOOL succeeded, NSError *error) {
            if(succeeded)
            {
                AVUser *user = [AVUser currentUser];
                user.mobilePhoneNumber = phoneNumber;
                [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                    [self.backView setHidden:NO];
                    [self actionClose];
                    if (succeeded)
                    {
                        if (self.successChangeBlock)
                        {
                            self.successChangeBlock(self, phoneNumber);
                        }
                    }
                    else
                    {
                        [XHToast showCenterWithText:@"网络错误,请重试"];
                    }
                }];
            }
            else
            {
                 [self.backView setHidden:NO];
                if (error.code == 603)
                {
                    //验证码错误
                    //                self.codeTextField.hintText = @"验证码错误";
                    self.codeTextField.placeholder = @"验证码错误";
                    self.codeTextField.placeholderInactiveColor = [UIColor redColor];
                }
                else
                {
                    [XHToast showCenterWithText:@"貌似出现了些错误,一会儿再来试试看"];
                    
                }
            }
        }];
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

