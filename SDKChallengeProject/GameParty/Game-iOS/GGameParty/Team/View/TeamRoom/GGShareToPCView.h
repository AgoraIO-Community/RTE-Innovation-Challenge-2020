//
//  GGShareToPCView.h
//  GGameParty
//
//  Created by Victor on 2018/9/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGShareToPCView : UIView
@property (nonatomic, copy) void (^sureBtnClick)(GGShareToPCView *view, UIButton *button);

@property (nonatomic, copy) void (^closeBtnClick)(GGShareToPCView *view);

- (instancetype)initWithFrame:(CGRect)frame withUrl:(NSString *)url;

@end
