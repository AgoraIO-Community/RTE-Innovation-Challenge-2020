//
//  GGRoomHeadView.h
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGTeamModel.h"
@interface GGRoomHeadView : UIView

@property (nonatomic,strong)UILabel *titleLabel;

@property (nonatomic,strong)UILabel *gameLabel;

@property (nonatomic,strong)UILabel *infoLabel;

@property (nonatomic,strong)UIButton *closeButton;

@property (nonatomic,strong)UIImageView *lockView;

- (void)setDataWithTeamModel:(GGTeamModel *)model;

@end
