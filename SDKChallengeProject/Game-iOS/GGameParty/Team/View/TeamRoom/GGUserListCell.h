//
//  GGUserListCell.h
//  GGameParty
//
//  Created by Victor on 2018/7/31.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
@class GGUserListModel;
@interface GGUserListCell : UICollectionViewCell

@property (nonatomic,strong)UIImageView *avatarImage;

@property (nonatomic,strong)UILabel  *nameLabel;

@property (nonatomic,strong)UIImageView  *noMacImage;

@property (nonatomic,strong)UIImageView  *offLineView;

@property (nonatomic,strong)UIImageView  *isSpeakImage;


- (void)setUserListModel:(GGUserListModel *)model;


@end
