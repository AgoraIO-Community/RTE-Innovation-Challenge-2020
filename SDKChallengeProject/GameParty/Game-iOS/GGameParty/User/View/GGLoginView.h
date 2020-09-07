//
//  GGLoginView.h
//  GGameParty
//
//  Created by Victor on 2018/7/26.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "MMPopupView.h"
#import "LRTextField.h"
@interface GGLoginView : UIView

@property (nonatomic, copy) void (^loginBtnClick)(GGLoginView *loginView, UIButton *button);

@property (nonatomic, copy) void (^closeBtnClick)(GGLoginView *loginView);


@property (nonatomic,strong)LRTextField *phoneTextField;

@property (nonatomic,strong)LRTextField *codeTextField;
@property (nonatomic,strong)UIView *backView;

@property (nonatomic,strong)UIView *waitbackView;

@property (nonatomic,strong)UILabel *titleLabel;

@property (nonatomic,strong)UIButton  *sendCodeBtn;

@property (nonatomic,strong)UIButton  *loginButton;

@property (nonatomic, strong) UILabel     *lblStatus;

@property (nonatomic, strong) UIButton    *btnClose;

@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;
- (void)login;
- (BOOL)valivaForm;
- (void)actionClose;
- (void)sendCode;
- (void)receiveCheckNumButton;

@end
