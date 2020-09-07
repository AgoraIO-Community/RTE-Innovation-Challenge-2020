//
//  UINavigationController+InteractivePush.h
//  Pods
//
//  Created by Ricky on 2017/1/19.
//
//

#import <UIKit/UIKit.h>

IB_DESIGNABLE
@interface UINavigationController (RTInteractivePush)
@property (nonatomic, assign, getter=rt_isInteractivePushEnabled) IBInspectable BOOL rt_enableInteractivePush;
@property (nonatomic, readonly, nullable) UIPanGestureRecognizer *rt_interactivePushGestureRecognizer;
@end


@interface UIViewController (RTInteractivePush)

- (nullable __kindof UIViewController *)rt_nextSiblingController;

@end
