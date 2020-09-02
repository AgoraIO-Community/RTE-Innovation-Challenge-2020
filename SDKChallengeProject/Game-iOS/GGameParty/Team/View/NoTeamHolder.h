//
//  NoTeamHolder.h
//  GGameParty
//
//  Created by Victor on 2018/9/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef void(^ReloadButtonClickBlock)() ;

@interface NoTeamHolder : UIView

- (instancetype)initWithFrame:(CGRect)frame
                  reloadBlock:(ReloadButtonClickBlock)reloadBlock;

- (void)changeTitle:(NSString *)title;

- (void)showInView:(UIView *)viewWillShow ;
- (void)dismiss ;
@end
