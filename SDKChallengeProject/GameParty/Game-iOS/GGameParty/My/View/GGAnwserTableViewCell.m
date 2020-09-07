//
//  GGAnwserTableViewCell.m
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAnwserTableViewCell.h"

@implementation GGAnwserTableViewCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        self.contentView.layer.borderWidth = 1;
        self.contentView.layer.borderColor = GGLine_Color.CGColor;
    }
    return self;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}
- (void)setFrame:(CGRect)frame
{
    // 更改x、宽度
    frame.origin.x = 15;
    frame.size.width -= 15 * 2;
    
    // 更改顶部间距、每个cell之间的间距
    frame.origin.y += 15;;
    frame.size.height -= 15;
    
    [super setFrame:frame];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
