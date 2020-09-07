//
//  GGRefreshHeader.m
//  GGameParty
//
//  Created by Victor on 2018/8/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRefreshHeader.h"

@implementation GGRefreshHeader
+ (GGRefreshHeader *)ggrefreshHeaderBlock:(MJRefreshComponentRefreshingBlock)refreshingBlock
{
    NSMutableArray *refresharr = [NSMutableArray arrayWithCapacity:0];
    for(int i = 0;i<60;i++)
    {
        NSString *str = [NSString stringWithFormat:@"icon77_000%d",i];
        UIImage *image = [UIImage imageNamed:str];
        if (image) {
            [refresharr addObject:image];
        }
    }
    NSArray *arr = @[refresharr[0]];
    MJRefreshGifHeader *header = [MJRefreshGifHeader headerWithRefreshingBlock:refreshingBlock];
    [header setImages:arr forState:MJRefreshStateIdle];
    [header setImages:refresharr forState:MJRefreshStateRefreshing];
    header.lastUpdatedTimeLabel.hidden = YES;
    [header setTitle:@"下拉刷新" forState:MJRefreshStateIdle];
    [header setTitle:@"下拉刷新" forState:MJRefreshStatePulling];
    [header setTitle:@"刷新中" forState:MJRefreshStateRefreshing];
    return (GGRefreshHeader *)header;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
