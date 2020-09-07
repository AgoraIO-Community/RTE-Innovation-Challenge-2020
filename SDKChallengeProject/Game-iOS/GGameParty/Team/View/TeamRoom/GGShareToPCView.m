//
//  GGShareToPCView.m
//  GGameParty
//
//  Created by Victor on 2018/9/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGShareToPCView.h"

@interface GGShareToPCView()


@property (nonatomic, strong) UIButton    *btnClose;

@property (nonatomic,strong)UIView *backView;

@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;

@property (nonatomic,strong)UIView *waitbackView;
@property (nonatomic,strong)UIButton  *sureButton;
@property (nonatomic,strong)NSString *url;

@end



@implementation GGShareToPCView
- (instancetype)initWithFrame:(CGRect)frame withUrl:(NSString *)url
{
    self.url = url;
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
        
        UIImageView *image = [SJUIImageViewFactory imageViewWithImageName:@"pic_phwpc" viewMode:UIViewContentModeCenter];
        image.frame = CGRectMake(0, 20, self.frame.size.width, 100);
        [self addSubview:image];
        
        UILabel *urlLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:13] textColor:GGTitle_Color alignment:NSTextAlignmentCenter];
        urlLabel.text = self.url;
        urlLabel.frame = CGRectMake(0, CGRectGetMaxY(image.frame), self.frame.size.width, 20);
        [self addSubview:urlLabel];
        
        
        UILabel *tipLabel = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:15] textColor:GGTitle_Color alignment:NSTextAlignmentCenter];
        tipLabel.text = @"已生成网页链接，请在电脑中打开链接";
        tipLabel.frame = CGRectMake(0, CGRectGetMaxY(urlLabel.frame)+10, self.frame.size.width, 20);
        [self addSubview:tipLabel];
        
        
        UILabel *tip2Label = [SJUILabelFactory labelWithFont:[UIFont systemFontOfSize:9] textColor:RGBCOLOR(119, 119, 119) alignment:NSTextAlignmentCenter];
        tip2Label.text = @"注意：电脑与手机端语音同步，关闭房间则全部都会关闭";
        tip2Label.frame = CGRectMake(0, CGRectGetMaxY(tipLabel.frame)+5, self.frame.size.width, 10);
        [self addSubview:tip2Label];
        
        self.sureButton = [SJUIButtonFactory buttonWithTitle:@"复制链接" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(sureSaveData) tag:3];
        self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(tip2Label.frame) + 20, self.frame.size.width - 30, 45);
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
    [self actionClose];

    if (self.sureBtnClick)
    {
        self.sureBtnClick(self, self.sureButton);
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
