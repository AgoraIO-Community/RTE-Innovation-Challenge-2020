//
//  GGActivityView.m
//  GGameParty
//
//  Created by Victor on 2018/8/9.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGActivityView.h"
#import "GGActivityCollectionViewCell.h"
@interface GGActivityView()<UICollectionViewDelegate,
UICollectionViewDataSource>
@property (nonatomic,strong)NSMutableArray *dataArr;

@end

static NSString *iden = @"activityCell";
@implementation GGActivityView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        
        self.dataArr = [NSMutableArray arrayWithCapacity:0];
        
        CGFloat hh = (kScreenW - 45)/3;
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 15;
        flowLayout.minimumLineSpacing = 15;
        flowLayout.itemSize =  CGSizeMake(kScreenW - 15 - 30,hh - 15);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 15, self.frame.size.width, hh) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.showsHorizontalScrollIndicator = NO;
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = YES;
        //self.collectionView.pagingEnabled = YES;
        [self addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGActivityCollectionViewCell class] forCellWithReuseIdentifier:iden];
        
        [self loadData];
    }
    return self;
}

- (void)loadData
{
    AVQuery *query = [AVQuery queryWithClassName:DB_ACTIVITY];
    [query whereKey:@"status" equalTo:@"1"];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error) {
            self.dataArr = [NSMutableArray  arrayWithArray:objects];
            [self.collectionView reloadData];
            if (self.dataArr.count > 0) {
                [GGNotificationCenter postNotificationName:@"HAVE_ACTIVITY" object:nil];
            }
        }
    }];
}

#pragma mark - collectionView.Delegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.dataArr.count;
    
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    GGActivityCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:iden forIndexPath:indexPath];
    cell.layer.cornerRadius = 10;
    AVObject *obj = self.dataArr[indexPath.row];
    AVFile *file = [obj objectForKey:@"image"];
    [cell.imageView setImageWithURL:[NSURL URLWithString:file.url]];
    return cell;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    AVObject *obj = self.dataArr[indexPath.row];
    if (self.clickActivityBlock) {
        self.clickActivityBlock(self, [obj objectForKey:@"url"]);
    }
}


// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat width = kScreenW - 15 - 30;
    CGFloat height = width/3;
    return  CGSizeMake(width,height);
}

// 定义整个CollectionViewCell与整个View的间距
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(0, 15,0, 15);//（上、左、下、右）
}


//  定义每个UICollectionView的横向间距
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section
{
    return 15;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}



/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
