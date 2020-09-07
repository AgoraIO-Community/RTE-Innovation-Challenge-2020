//
//  GGUserGoodCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSInteger, AnimationType) {
    AnimationTypeWithBackground,
    AnimationTypeWithoutBackground
};


@interface GGUserGoodCell : UICollectionViewCell

@property (nonatomic,strong)UIImageView *imageView;

@property (nonatomic,strong)UIImageView *goodImage;

@property (nonatomic,strong)UILabel *numLabel;

@property (nonatomic,strong)UILabel *titLabel;

@end
