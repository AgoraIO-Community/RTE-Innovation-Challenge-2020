//
//  GGShareAppView.h
//  GGameParty
//
//  Created by Victor on 2018/9/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGShareAppView : GGPopupView

@property (nonatomic, copy) void (^sharePlatFormClick)(GGShareAppView *view, NSInteger index);


@end
