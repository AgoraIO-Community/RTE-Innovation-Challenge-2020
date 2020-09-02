//
//  GGNoticeView.h
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZScrollLabel.h"
@interface GGNoticeView : UIView

@property (nonatomic,strong)UILabel *noticeLabel;

@property (nonatomic,strong)ZScrollLabel *contentLabel;

@property (nonatomic,strong)UIImageView *nabaImage;


@end
