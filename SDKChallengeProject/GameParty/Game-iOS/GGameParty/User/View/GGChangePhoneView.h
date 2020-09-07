//
//  GGChangePhoneView.h
//  GGameParty
//
//  Created by Victor on 2018/8/13.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGLoginView.h"

@interface GGChangePhoneView : GGLoginView
@property (nonatomic, copy) void (^successChangeBlock)(GGChangePhoneView *loginView,NSString *phone);

@end
