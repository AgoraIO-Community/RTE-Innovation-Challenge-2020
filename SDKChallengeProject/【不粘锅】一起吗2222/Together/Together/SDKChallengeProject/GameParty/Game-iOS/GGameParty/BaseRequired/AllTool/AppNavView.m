//
//  AppNavView.m
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "AppNavView.h"

#define Normal_Font [UIFont systemFontOfSize:18]
#define Normal_Color [UIColor blackColor]

#define UnNormal_Font [UIFont boldSystemFontOfSize:30]
#define UnNormal_Color [UIColor blackColor]

#import "NSString+SGPagingView.h"
#import "AUIAnimatableLabel.h"
#import "LCCKBadgeView.h"
@interface AppNavView()<UIScrollViewDelegate>
{
    BOOL _isNeedSearch;
}
@property (nonatomic,strong)NSArray *titleArr;
@property (nonatomic, strong) UIButton *searchBtn;

@end

@implementation AppNavView
- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray *)arr isNeedSearch:(BOOL)isNeedSearch
{
    _isNeedSearch = isNeedSearch;
    return [self initWithFrame:frame titleArr:arr];
}

- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray *)arr
{
    if (self = [self initWithFrame:frame])
    {
        self.backgroundColor = [UIColor whiteColor];
        self.searchBtn = [SJUIButtonFactory buttonWithImageName:@"icon_chuangjian"];
        self.searchBtn.frame = CGRectMake(kScreenW - 110, 10, 100, self.frame.size.height - 10);
        [self addSubview:self.searchBtn];
        self.searchBtn.imageView.contentMode = UIViewContentModeRight;
        [self.searchBtn addTarget:self action:@selector(goSearch) forControlEvents:UIControlEventTouchUpInside];
        self.searchBtn.hidden = !_isNeedSearch;
        
        self.titleArr = arr;
        if (self.titleArr.count > 0)
        {
            CGFloat width = [arr[0] SG_sizeWithFont:UnNormal_Font].width;
            for (int i = 0;i<arr.count;i++)
            {
                NSString *title = arr[i];
                AUIAnimatableLabel *label = [[AUIAnimatableLabel alloc]initWithFrame:CGRectMake(14 + width * i, 16, width, 35)];
                label.tag = 1+i;
                [self addSubview:label];
                label.textAlignment = NSTextAlignmentCenter;
                label.verticalTextAlignment = AUITextVerticalAlignmentBottom;
                label.text = [NSString stringWithFormat:@"%@",title];
                label.userInteractionEnabled = YES;
                UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(btnClick:)];
                [label addGestureRecognizer:tap];
                label.font = Normal_Font;
                if (i == 0)
                {
                    label.font = UnNormal_Font;
                }
                UILabel *redP = [[UILabel alloc]initWithFrame:CGRectMake(label.frame.origin.x +(width - 4)/2, self.frame.size.height - 2, 4, 2)];
                redP.backgroundColor = [UIColor redColor];
                redP.tag = 10+i;
                [self addSubview:redP];
                redP.hidden = YES;
            }
        }
    }
    return self;
}

- (void)goSearch
{
    if ([self.v_delegate respondsToSelector:@selector(shouldGoSearch)]) {
        [self.v_delegate shouldGoSearch];
    }
}




- (void)showRedPointIndex:(NSInteger)index
{
    UILabel *label = [self viewWithTag:index + 10];
    label.hidden = NO;
}

- (void)hiddenRedPointIndex:(NSInteger)index
{
    UILabel *label = [self viewWithTag:index + 10];
    label.hidden = YES;
}



- (void)navViewSwitchToIndex:(NSInteger)index
{
    [self btnClickWithIndex:index + 1];
}

- (void)btnClickWithIndex:(NSInteger)index
{
    AUIAnimatableLabel *label = [self viewWithTag:index];
    NSString *title = label.text;
    
    for (int i = 1; i < self.titleArr.count+1; i++)
    {
        AUIAnimatableLabel *normalLabel = [self viewWithTag:i];
        normalLabel.font = Normal_Font;
    }
    label.font = UnNormal_Font;
    
    if ([self.v_delegate respondsToSelector:@selector(clickTitle:index:)])
    {
        [self.v_delegate clickTitle:title index:label.tag - 1];
    }
}

- (void)btnClick:(UITapGestureRecognizer *)tap
{
    AUIAnimatableLabel *label = (AUIAnimatableLabel *)[tap view];
    NSString *title = label.text;
    for (int i = 1; i < self.titleArr.count+1; i++)
    {
        AUIAnimatableLabel *normalLabel = [self viewWithTag:i];
        normalLabel.font = Normal_Font;
    }
    label.font = UnNormal_Font;

    if ([self.v_delegate respondsToSelector:@selector(clickTitle:index:)])
    {
        [self.v_delegate clickTitle:title index:label.tag - 1];
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
