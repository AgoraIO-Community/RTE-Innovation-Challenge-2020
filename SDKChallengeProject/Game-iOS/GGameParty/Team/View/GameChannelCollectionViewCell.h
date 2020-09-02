//
//  GameChannelCollectionViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGGameModel.h"
@interface GameChannelCollectionViewCell : UICollectionViewCell

@property (nonatomic,strong)UIImageView *imageView;

@property (nonatomic,strong)UIImageView *headImage;


@property (nonatomic,strong)UILabel *titLabel;

@property (nonatomic,strong)UILabel *numLabel;

@property (nonatomic,strong)UIButton *joinButton;
- (void)setGameModel:(GGGameModel * )model;


@end
