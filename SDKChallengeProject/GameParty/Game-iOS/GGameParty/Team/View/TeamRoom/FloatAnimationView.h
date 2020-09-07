//
//  FloatAnimationView.h
//  GGameParty
//
//  Created by Victor on 2018/9/16.
//  Copyright © 2018年 Victor. All rights reserved.
//
#define ColorWithAlpha(r,g,b,a) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:a]

//typedef NS_ENUM(NSInteger, AnimationType) {
//    AnimationTypeWithBackground,
//    AnimationTypeWithoutBackground
//};

#import "RippleAnimationView.h"

@interface FloatAnimationView : UIView
/**
 设置扩散倍数。默认1.423倍
 */
@property (nonatomic, assign) CGFloat multiple;

- (instancetype)initWithFrame:(CGRect)frame animationType:(AnimationType)animationType;

@end
