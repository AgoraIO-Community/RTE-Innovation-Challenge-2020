//
//  GGContinueFindView.h
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGContinueFindView : UIView

@property (nonatomic, copy) void (^stopFindClick)(GGContinueFindView *view);


@property (nonatomic, copy) void (^continueFindClick)(GGContinueFindView *view);

@property (nonatomic, copy) void (^createRoomClick)(GGContinueFindView *view);

@end
