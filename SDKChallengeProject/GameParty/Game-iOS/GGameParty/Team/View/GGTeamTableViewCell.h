//
//  GGTeamTableViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/7/29.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
@class GGTeamModel;
@interface GGTeamTableViewCell : UITableViewCell

@property (nonatomic,strong)UIImageView *headImage;
@property (nonatomic,strong)UILabel *titLabel;
@property (nonatomic,strong)UILabel *gameNameLabel;
@property (nonatomic,strong)UILabel *infoLabel;
@property (nonatomic,strong)UIButton *operateButton;

@property (nonatomic,strong)UIView *shodwView;

- (void)setModel:(GGTeamModel *)model;

@end
