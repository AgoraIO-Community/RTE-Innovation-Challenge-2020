//
//  GGShareRoomView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface GGShareRoomView : GGPopupView
@property (nonatomic, copy) void (^sharePlatFormClick)(GGShareRoomView *view, NSInteger index);

@end
