//
//  GGQGameTableViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGQGameTableViewCell : UITableViewCell
@property (nonatomic,strong)UIImageView *headImage;

@property (nonatomic,strong)UILabel *nameLabel;

@property (nonatomic,strong)UIButton *goBtn;

@property (nonatomic,strong)UILabel *infoLabel;
- (void)setModel:(GGGameModel *)model;
@end
