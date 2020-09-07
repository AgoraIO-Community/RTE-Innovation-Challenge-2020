//
//  GGShareAppView.m
//  GGameParty
//
//  Created by Victor on 2018/9/24.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGShareAppView.h"
#import "GGShareCollectionViewCell.h"

@interface GGShareAppView()<UICollectionViewDelegate,
UICollectionViewDataSource>

@property (nonatomic,strong)UICollectionView *collectionView;

@end


@implementation GGShareAppView



- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        [self addSubview:self.waitbackView];
        [self.waitbackView addSubview:self.activityIndicator];
        
        [self addSubview:self.backView];
        [self.backView addSubview:self.btnClose];
        [self.backView addSubview:self.lblStatus];
        self.lblStatus.text = @"分享飞聊";
        
        
//        UIView *cellView = [[UIView alloc]initWithFrame:CGRectMake(0, 50, self.frame.size.width, 50)];
//        [self.backView addSubview:cellView];
//        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(goTop)];
//        [cellView addGestureRecognizer:tap];
//
//        UIImageView *image = [SJUIImageViewFactory imageViewWithImageName:@"icon_dating"];
//        image.frame = CGRectMake(0, 0, 50, 50);
//        image.contentMode =  UIViewContentModeCenter;
//        [cellView addSubview:image];
//
//
//
//        UILabel *title = [SJUILabelFactory labelWithText:@"发送到组队大厅,呼唤伙伴加入" textColor:GGTitle_Color font:[UIFont systemFontOfSize:14]];
//        title.frame = CGRectMake(50, 0, self.frame.size.width - 50, 50);
//        [cellView addSubview:title];
//
//        UIImageView *rightArrow = [SJUIImageViewFactory imageViewWithImageName:@"icon_qianjhei"];
//        rightArrow.frame = CGRectMake(self.frame.size.width - 50, 0, 50, 50);
//        //        rightArrow.backgroundColor = [UIColor redColor];
//        rightArrow.contentMode =  UIViewContentModeCenter;
//        [cellView addSubview:rightArrow];
//
//
//        UIView *line = [[UIView alloc]initWithFrame:CGRectMake(10, CGRectGetMaxY(cellView.frame), self.frame.size.width - 20, 1)];
//        line.backgroundColor = GGLine_Color;
//        [self.backView addSubview:line];
        
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 10;
        flowLayout.minimumLineSpacing = 10;
        flowLayout.itemSize =  CGSizeMake(60,55);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 50+10, self.frame.size.width, 70) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = YES;
        self.collectionView.showsHorizontalScrollIndicator = NO;
        [self.backView addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGShareCollectionViewCell class] forCellWithReuseIdentifier:@"sharecell"];
    }
    return self;
}

- (void)goTop
{
    if (self.sharePlatFormClick)
    {
        self.sharePlatFormClick(self, 100);
    }
}

#pragma mark - collectionView.Delegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    //return self.dataArr.count;
    return 4;;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"sharecell";
    GGShareCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
    //    cell.backgroundColor = randomColor;
    NSArray *imageArr = @[
                          @"icon_QQshare",
                          @"icon_weixinshare",
                          @"icon_QQshare",
                          @"icon_pengyouq"
                          ];
    NSArray *titleArr = @[
                          @"QQ好友",
                          @"微信好友",
                          @"QQ空间",
                          @"朋友圈"
                          ];
    cell.iconImage.image = [UIImage imageNamed:imageArr[indexPath.row]];
    cell.titLabel.text = titleArr[indexPath.row];
    return cell;
}

// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat width = 60;
    CGFloat height = 55;
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
    //    CGFloat jianju = (self.frame.size.width - 60 * 4 - 30)/3;
    //    return jianju;
    return 10;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    CGFloat jianju = (self.frame.size.width - 60 * 4 - 30)/3;
    //    return jianju;
    return 15;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
    if (self.sharePlatFormClick)
    {
        self.sharePlatFormClick(self, indexPath.row);
    }
    
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}



@end
