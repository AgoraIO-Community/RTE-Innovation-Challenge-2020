//
//  GGSetUserNameView.m
//  GGameParty
//
//  Created by Victor on 2018/8/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSetUserNameView.h"

@implementation GGSetUserNameView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)sureSaveData
{
    
    NSString *title = self.titleTextField.text;
    if (title.length == 0)
    {
        
    }
    if ( title.length > 20) {
        [XHToast showBottomWithText:@"用户名不能超过20个字"];
    }
    else
    {
        [self.titleTextField resignFirstResponder];
        [self.backView setHidden:YES];
        AVUser *user = [AVUser currentUser];
        [user setObject:self.titleTextField.text forKey:@"username"];
        [user saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
            [self.backView setHidden:NO];
            if (succeeded)
            {
                [self actionClose];
                if (self.sureBtnClick)
                {
                    self.sureBtnClick(self, self.sureButton);
                }
                else
                {
                    [XHToast showCenterWithText:@"服务器出现错误,稍后再试"];
                }
            }
            if (error.code == 202)
            {
                [XHToast showBottomWithText:@"用户名已被占用"];
            }
        }];
    }
}

@end
