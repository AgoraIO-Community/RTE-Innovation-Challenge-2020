//
//  GGRoomEditView.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YYTextView.h"
#import "GGTeamModel.h"
@interface GGRoomEditView : UIView
@property (nonatomic, copy) void (^sureBtnClick)(GGRoomEditView *editView, UIButton *button,GGTeamModel *teamModel);

@property (nonatomic, copy) void (^closeBtnClick)(GGRoomEditView *editView);


@property (nonatomic,strong)UITextField *titleTextField;

@property (nonatomic,strong)YYTextView *descTextView;
- (instancetype)initWithFrame:(CGRect)frame teamModel:(GGTeamModel *)teamModel;
@end
