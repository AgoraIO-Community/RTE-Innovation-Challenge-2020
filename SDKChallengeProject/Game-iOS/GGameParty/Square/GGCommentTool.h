//
//  GGCommentTool.h
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
@class SquareModel;
@interface GGCommentTool : UIView

@property (nonatomic, copy) void (^giveGoodBlock)(BOOL isGood);

@property (nonatomic, copy) void (^operateBtnClickBlock)(NSInteger btnTag);


@property (nonatomic,strong)UIButton *goodButton;

@property (nonatomic,strong)UIButton *commentButton;

@property (nonatomic,strong)UIButton *shareButton;

@property (nonatomic, strong) SquareModel *squareModel;

- (void)setModel:(SquareModel *)model;


@end
