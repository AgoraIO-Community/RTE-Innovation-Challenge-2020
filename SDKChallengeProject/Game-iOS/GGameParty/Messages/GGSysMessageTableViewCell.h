//
//  GGSysMessageTableViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/10.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGSysMesModel.h"
@interface GGSysMessageTableViewCell : UITableViewCell

@property (nonatomic,strong)UIImageView *contentImage;
@property (nonatomic,strong)UIImageView *headImage;

@property (nonatomic,strong)UILabel *titLabel;

@property (nonatomic,strong)UILabel *descLabel;


- (void)setModel:(GGSysMesModel *)model;
@end
