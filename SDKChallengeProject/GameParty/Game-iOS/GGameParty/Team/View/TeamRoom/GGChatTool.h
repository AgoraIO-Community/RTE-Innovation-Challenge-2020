//
//  GGChatTool.h
//  GGameParty
//
//  Created by Victor on 2018/7/30.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol GGChatToolDelegate <NSObject>

@optional
- (void)clickTool:(NSInteger)index button:(UIButton *)button;

- (void)longPressSendSteamID;

@end
@interface GGChatTool : UIView

@property (nonatomic,strong)UIButton *messageBtn;

@property (nonatomic,strong)UIButton *hornSwitchBtn;

@property (nonatomic,strong)UIButton *sendIDBtn;

@property (nonatomic,strong)UIButton *macSwitchBtn;
@property (nonatomic,assign)id<GGChatToolDelegate>v_delegate;
//@property (nonatomic, strong) Waver * waver;


@end
