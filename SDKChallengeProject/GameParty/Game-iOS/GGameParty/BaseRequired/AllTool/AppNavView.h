//
//  AppNavView.h
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>




@protocol GGNavBarDelegate <NSObject>

@optional
- (void)clickTitle:(NSString *)title index:(NSInteger)index;

- (void)shouldGoSearch;

@end





@interface AppNavView : UIView

- (void)navViewSwitchToIndex:(NSInteger)index;

- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray *)arr;
- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray *)arr isNeedSearch:(BOOL)isNeedSearch;
@property (nonatomic,assign)id<GGNavBarDelegate>v_delegate;

@property (nonatomic,strong)UIScrollView *scrollView;

- (void)hiddenRedPointIndex:(NSInteger)index;
- (void)showRedPointIndex:(NSInteger)index;

@end
