//
//  GGSetSteamIDView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGSetSteamIDView : UIView
@property (nonatomic, copy) void (^sureBtnClick)(GGSetSteamIDView *view, UIButton *button);

@property (nonatomic, copy) void (^closeBtnClick)(GGSetSteamIDView *view);
- (instancetype)initWithFrame:(CGRect)frame withSteamId:(NSString *)steamID;

@property (nonatomic, strong) UIButton    *btnClose;

@property (nonatomic,strong)UIView *backView;

@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;

@property (nonatomic,strong)UIView *waitbackView;

@property (nonatomic, strong) UILabel     *lblStatus;

@property (nonatomic, strong) UILabel     *tipsLabel;

@property (nonatomic,strong)UITextField *titleTextField;

@property (nonatomic,strong)UIButton  *sureButton;

@property (nonatomic,strong)NSString   *steamId;


- (void)sureSaveData;

- (void)actionClose;

@end
