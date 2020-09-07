//
//  GGActivityView.h
//  GGameParty
//
//  Created by Victor on 2018/8/9.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GGActivityView : UIView
@property (nonatomic,strong)UICollectionView *collectionView;
@property (nonatomic, copy) void (^clickActivityBlock)(GGActivityView *editView,NSString *url);

@end
