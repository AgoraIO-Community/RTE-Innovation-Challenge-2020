//
//  GGRoomEditView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRoomEditView.h"
#import "YYTextView.h"
#import "GGTeamModel.h"
@interface GGRoomEditView()<UITextFieldDelegate,YYTextViewDelegate>
@property (nonatomic, strong) UIButton    *btnClose;

@property (nonatomic,strong)UIView *backView;

@property (nonatomic, strong) UILabel     *lblStatus;

@property (nonatomic, strong) UILabel     *titleNumLabel;

@property (nonatomic, strong) UILabel     *descNumLabel;

@property (nonatomic,strong)UIButton  *sureButton;

@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;

@property (nonatomic,strong)UIView *waitbackView;

@property (nonatomic,strong)GGTeamModel *teamModel;

@end

@implementation GGRoomEditView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (instancetype)init
{
    return [self initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame teamModel:(GGTeamModel *)teamModel
{
    self.teamModel = teamModel;
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
        self.lblStatus.text = @"房间设置";
        self.lblStatus.textAlignment = NSTextAlignmentLeft;
        
        
        UILabel *title = [SJUILabelFactory labelWithText:@"标题" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:14]];
        title.frame = CGRectMake(self.lblStatus.frame.origin.x, CGRectGetMaxY(self.lblStatus.frame) + 20, 35, 20);
        [self.backView addSubview:title];
        
        self.titleTextField = [[UITextField alloc]initWithFrame:CGRectMake(CGRectGetMaxX(title.frame), title.frame.origin.y, self.frame.size.width - CGRectGetMaxX(title.frame) - 20, 20)];
        self.titleTextField.placeholder = @"设置房间标题";
        [self.backView addSubview:self.titleTextField];
        self.titleTextField.font = [UIFont systemFontOfSize:14];
//        [self.titleTextField setValue:[UIFont systemFontOfSize:14.f weight:UIFontWeightThin] forKeyPath:@"_placeholderLabel.font"];
        
        self.titleTextField.attributedPlaceholder = [[NSAttributedString alloc] initWithString:@"设置房间标题"attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:14.f weight:UIFontWeightThin] }];

        
        self.titleTextField.text = self.teamModel.title;
        [self.titleTextField addTarget:self action:@selector(textFieldEditChanged:) forControlEvents:UIControlEventEditingChanged];
        


        NSString *tttNum = [NSString stringWithFormat:@"%lu/24",(unsigned long)self.teamModel.title.length];
        self.titleNumLabel = [SJUILabelFactory labelWithText:tttNum textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:14]];
        self.titleNumLabel.frame = CGRectMake(CGRectGetMaxX(self.titleTextField.frame) - 40, self.titleTextField.frame.origin.y, 40, self.titleTextField.frame.size.height);
        self.titleNumLabel.textAlignment = NSTextAlignmentRight;
        [self.backView addSubview:self.titleNumLabel];
        
        
        UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(self.titleTextField.frame.origin.x, CGRectGetMaxY(self.titleTextField.frame)+2, self.titleTextField.frame.size.width, 1)];
        line1.backgroundColor = RGBCOLOR(238, 238, 238);
        [self.backView addSubview:line1];
        
        UILabel *gonggao = [SJUILabelFactory labelWithText:@"公告" textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:14]];
        gonggao.frame = CGRectMake(self.lblStatus.frame.origin.x, CGRectGetMaxY(line1.frame) + 20, 35, 20);
        [self.backView addSubview:gonggao];
      
        self.descTextView = [[YYTextView alloc]initWithFrame:CGRectMake(self.titleTextField.frame.origin.x, gonggao.frame.origin.y, self.titleTextField.frame.size.width, 70)];
        self.descTextView.backgroundColor = RGBCOLOR(248,248,248);
        self.descTextView.font = [UIFont systemFontOfSize:14];
        self.descTextView.placeholderText = @"设置公告内容";
        self.descTextView.layer.masksToBounds = YES;
        self.descTextView.layer.cornerRadius = 6;
        [self.backView addSubview:self.descTextView];
        self.descTextView.delegate = self;
        self.descTextView.text = self.teamModel.desc;
        
        //[self.descTextView addTarget:self action:@selector(textViewEditChanged:) forControlEvents:UIControlEventEditingChanged];

        
        NSString *dddNum = [NSString stringWithFormat:@"%lu/48",(unsigned long)self.teamModel.desc.length];
        self.descNumLabel = [SJUILabelFactory labelWithText:dddNum textColor:RGBCOLOR(170, 170, 170) font:[UIFont systemFontOfSize:14]];
        self.descNumLabel.frame = CGRectMake(CGRectGetMaxX(self.titleTextField.frame) - 40, CGRectGetMaxY(self.descTextView.frame) - self.titleTextField.frame.size.height , 40, self.titleTextField.frame.size.height);
        self.descNumLabel.textAlignment = NSTextAlignmentRight;
        [self.backView addSubview:self.descNumLabel];
        
        
        self.sureButton = [SJUIButtonFactory buttonWithTitle:@"确定" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(sureSaveData) tag:3];
        self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(self.descTextView.frame) + 10, self.frame.size.width - 30, 45);
        self.sureButton.backgroundColor = RGBCOLOR(28, 181, 73);
        [self.sureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.sureButton.layer.masksToBounds = YES;
        self.sureButton.layer.cornerRadius = 22.5;
        self.sureButton.titleLabel.font = [UIFont boldSystemFontOfSize:18];
        [self.backView addSubview:self.sureButton];
    }
    
    return self;
}

- (void)textFieldEditChanged:(UITextField *)textField
{
    
    NSString *tttNum = [NSString stringWithFormat:@"%lu/24",(unsigned long)textField.text.length];
    self.titleNumLabel.text = tttNum;
    if (textField.text.length > 24 || textField.text.length == 0)
    {
        self.titleNumLabel.textColor  = [UIColor redColor];
    }else
    {
        self.titleNumLabel.textColor  = RGBCOLOR(170, 170, 170);
    }
}

- (void)textViewDidChange:(YYTextView *)textView
{
    NSString *tttNum = [NSString stringWithFormat:@"%lu/48",(unsigned long)self.descTextView.text.length];
    self.descNumLabel.text = tttNum;
    if (textView.text.length > 48)
    {
        self.descNumLabel.textColor  = [UIColor redColor];
    }else
    {
        self.descNumLabel.textColor  = RGBCOLOR(170, 170, 170);
    }
}

- (void)resigText
{
    [self.titleTextField resignFirstResponder];
    [self.descTextView resignFirstResponder];
}

- (void)sureSaveData
{
    
    NSString *title = self.titleTextField.text;
    NSString *desc = self.descTextView.text;
    if (([title isEqualToString:self.teamModel.title] && [desc isEqualToString:self.teamModel.desc]))
    {
        [self resigText];
        [self actionClose];
    }
    else if (title.length == 0 || title.length > 24 || desc.length > 48)
    {
        
    }
    else
    {
        [self resigText];
        AVObject *obj = [AVObject objectWithClassName:DB_TEAM objectId:self.teamModel.objectId];
        [obj setObject:title forKey:@"title"];
        [obj setObject:desc forKey:@"desc"];
        [self.backView setHidden:YES];
        [obj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
            [self.backView setHidden:NO];
            if (succeeded)
            {
                [self actionClose];
                if (self.sureBtnClick)
                {
                    self.teamModel.title = title;
                    self.teamModel.desc = desc;
                    self.sureBtnClick(self, self.sureButton, self.teamModel);
                }
            }
            else
            {
                [XHToast showCenterWithText:@"服务器出现错误,稍后再试"];
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


@end
