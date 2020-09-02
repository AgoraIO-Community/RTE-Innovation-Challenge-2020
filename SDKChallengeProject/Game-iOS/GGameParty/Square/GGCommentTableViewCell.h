//
//  GGCommentTableViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/8/14.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SquareCommentModel.h"
@interface GGCommentTableViewCell : UITableViewCell

@property (nonatomic, copy) void (^goUserInfoVCBlock)(NSString *userId);
@property (nonatomic, copy) void (^giveGoodBlock)(BOOL isGood,NSInteger index);


@property (nonatomic,strong)UIButton *headImagBtn;
@property (nonatomic,strong)UILabel *nameLabel;
@property (nonatomic,strong)UILabel *descLabel;

@property (nonatomic,strong)UIButton *goodButton;

@property (nonatomic,strong)SquareCommentModel *commentModel;
@property (nonatomic,assign)NSInteger index;

+ (CGFloat)calcuHeithWith:(SquareCommentModel *)model;

- (void)setModel:(SquareCommentModel *)model index:(NSInteger)index;
@end
