//
//  GGSquareTableViewCell.h
//  GGameParty
//
//  Created by Victor on 2018/7/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SquareModel.h"
#import "GGCommentTool.h"
@class GGSquareTableViewCell;

@protocol GGVideoListTableViewCellDelegate <NSObject>

@optional
- (void)clickedPlayOnTabCell:(GGSquareTableViewCell *_Nullable)cell;

@end


@interface GGSquareTableViewCell : UITableViewCell
@property (nonatomic, strong, nullable) SquareModel *model;

@property (nonatomic, weak, nullable) id<GGVideoListTableViewCellDelegate> delegate;

@property (nonatomic, strong) UIImageView * _Nullable coverImageView;

@property (nonatomic, strong) UIButton *playButton;

@property (nonatomic,strong)GGCommentTool *commentTool;
+ (CGFloat)heightWithVideo:(SquareModel *_Nullable)video;

@end
