//
//  GGMessageViewController.m
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGMessageViewController.h"
#import "GGConversionListViewController.h"
#import "GGSysMessageViewController.h"
#import "AppNavView.h"
@interface GGMessageViewController ()<
SGPageTitleViewDelegate, SGPageContentCollectionViewDelegate,GGNavBarDelegate>
@property (nonatomic, strong) SGPageTitleView *pageTitleView;
@property (nonatomic, strong) SGPageContentCollectionView *pageContentCollectionView;
@property (nonatomic,strong)AppNavView *navView;

@end

@implementation GGMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBar.hidden = YES;
    
    [self setupPageView];
    
    self.navView = [[AppNavView alloc]initWithFrame:CGRectMake(0, 20, kScreenW, 61)titleArr:@[@"聊天",@"系统"]];
    self.navView.v_delegate = self;
  //  [self.view addSubview:self.navView];
}


- (void)clickTitle:(NSString *)title index:(NSInteger)index
{
    
}

- (void)setupPageView
{
    CGFloat statusHeight = CGRectGetHeight([UIApplication sharedApplication].statusBarFrame);
    CGFloat pageTitleViewY = 0;
    if (statusHeight == 20.0) {
        pageTitleViewY = 20;
    } else {
        pageTitleViewY = 44;
    }
    
    NSArray *titleArr = @[@"聊天", @"系统消息"];
    SGPageTitleViewConfigure *configure = [SGPageTitleViewConfigure pageTitleViewConfigure];
    //    configure.showIndicator = NO;
    configure.titleTextZoom = YES;
    configure.titleTextZoomAdditionalPointSize = 0.3;
    configure.titleAdditionalWidth = 40;
    configure.titleFont = [UIFont systemFontOfSize:22];
    configure.titleSelectedFont = [UIFont boldSystemFontOfSize:30];
    configure.titleColor = [UIColor blackColor];
    configure.titleSelectedColor = [UIColor blackColor];
    configure.titleGradientEffect = YES;
    configure.showIndicator = NO;
    configure.showBottomSeparator = NO;
    
    /// pageTitleView
    self.pageTitleView = [SGPageTitleView pageTitleViewWithFrame:CGRectMake(0, pageTitleViewY, self.view.frame.size.width * 0.8, 60) delegate:self titleNames:titleArr configure:configure];
    [self.view addSubview:_pageTitleView];
    
    GGConversionListViewController *oneVC = [[GGConversionListViewController alloc] init];
    GGSysMessageViewController *twoVC = [[GGSysMessageViewController alloc] init];
    
    NSArray *childArr = @[oneVC, twoVC];
    /// pageContentCollectionView
    CGFloat ContentCollectionViewHeight = self.view.frame.size.height - CGRectGetMaxY(_pageTitleView.frame);
    self.pageContentCollectionView = [[SGPageContentCollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_pageTitleView.frame), self.view.frame.size.width, ContentCollectionViewHeight) parentVC:self childVCs:childArr];
    _pageContentCollectionView.delegatePageContentCollectionView = self;
    [self.view addSubview:_pageContentCollectionView];
}

- (void)pageTitleView:(SGPageTitleView *)pageTitleView selectedIndex:(NSInteger)selectedIndex
{
    [self.pageContentCollectionView setPageContentCollectionViewCurrentIndex:selectedIndex];
}

- (void)pageContentCollectionView:(SGPageContentCollectionView *)pageContentCollectionView progress:(CGFloat)progress originalIndex:(NSInteger)originalIndex targetIndex:(NSInteger)targetIndex
{
    [self.pageTitleView setPageTitleViewWithProgress:progress originalIndex:originalIndex targetIndex:targetIndex];
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
