//
//  GGSpeedMatchView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGSpeedMatchView.h"
#import "GGMatchGameCell.h"
#import "GGMatchGameTypeCell.h"
#import "GGGameModel.h"
#import "UIImage+AFNetworking.h"
#import "UIImageView+AFNetworking.h"
static NSString *gameCellIden =  @"gameCellIden";
static NSString *typeCellIden  = @"typeCellIden";

@interface GGSpeedMatchView ()<UICollectionViewDelegate,
UICollectionViewDataSource>

@property (nonatomic,strong)UICollectionView *collectionView;

@property (nonatomic,strong)UICollectionView *typeCollectionView;

@property (nonatomic,strong)UIButton *sureButton;

@property (nonatomic,strong)NSMutableArray *dataArr;

@property (nonatomic,strong)NSMutableArray *typeDataArr;


@property (nonatomic,strong)GGGameModel *gameModel;

@property (nonatomic,strong)NSMutableArray *selectedGameTypeArr;

@property (nonatomic,strong)NSDictionary *selectAllDic;

@end


@implementation GGSpeedMatchView


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        [self addSubview:self.waitbackView];
        [self.waitbackView addSubview:self.activityIndicator];
        _selectAllDic = @{@"name":@"全部偏好",
                          @"objectId":@"0"
                          };
        self.selectedGameTypeArr = [NSMutableArray arrayWithCapacity:0];
        [self addSubview:self.backView];
        [self.backView addSubview:self.btnClose];
        [self.backView addSubview:self.lblStatus];
        self.lblStatus.text = @"全部游戏";
        self.lblStatus.textColor = [UIColor whiteColor];
       // UIColor *bgColor = [UIColor colorWithPatternImage: [UIImage imageNamed:@"bg_yijianzudui"]];
        UIColor *bgColor = GGHEXCOLOR(@"22272D");
        self.backView.backgroundColor = bgColor;
        
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 10;
        flowLayout.minimumLineSpacing = 10;
        flowLayout.itemSize =  CGSizeMake(70,85);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 60, self.frame.size.width, 85) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = YES;
        self.collectionView.showsHorizontalScrollIndicator = NO;
        [self.backView addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGMatchGameCell class] forCellWithReuseIdentifier:gameCellIden];
        
        UILabel *label = [SJUILabelFactory labelWithText:@"组队偏好:(可多选)" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:12]];
        label.frame = CGRectMake(15, CGRectGetMaxY(self.collectionView.frame) + 15, 300, 20);
        [self.backView addSubview:label];
        
        
        UICollectionViewFlowLayout *typeLayout = [[UICollectionViewFlowLayout alloc] init];
        typeLayout.minimumInteritemSpacing = 10;
        typeLayout.minimumLineSpacing = 10;
        typeLayout.itemSize =  CGSizeMake(80,30);
        [typeLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        self.typeCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(label.frame) + 10, self.frame.size.width, 30) collectionViewLayout:typeLayout];
        self.typeCollectionView.backgroundColor = [UIColor clearColor];
        self.typeCollectionView.delegate = self;
        self.typeCollectionView.dataSource = self;
        self.typeCollectionView.scrollEnabled = YES;
        self.typeCollectionView.showsHorizontalScrollIndicator = NO;
        [self.backView addSubview:self.typeCollectionView];
        //注册Cell
        [self.typeCollectionView registerClass:[GGMatchGameTypeCell class] forCellWithReuseIdentifier:typeCellIden];
        self.typeCollectionView.allowsMultipleSelection = YES;
        
        self.sureButton = [SJUIButtonFactory buttonWithTitle:@"开始组队" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(startMatch) tag:3];
        self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(self.self.typeCollectionView.frame) + 20, self.frame.size.width - 30, 45);
        self.sureButton.backgroundColor = RGBCOLOR(28, 181, 73);
        [self.sureButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.sureButton.layer.masksToBounds = YES;
        self.sureButton.layer.cornerRadius = 22.5;
        self.sureButton.titleLabel.font = [UIFont boldSystemFontOfSize:18];
        [self.backView addSubview:self.sureButton];
        
    }
    return self;
}

- (void)setGameData:(NSArray *)dataArr
{
    self.dataArr = [NSMutableArray arrayWithArray:dataArr];
    [self.collectionView reloadData];
    if (dataArr.count > 0)
    {
        
        if ([GGGameManger shareInstance].index)
        {
            [self showDefaultGame:[GGGameManger shareInstance].index - 1];
            [self showGameType:[GGGameManger shareInstance].index - 1];
        }
        else
        {
            [self showDefaultGame:0];
            [self showGameType:0];
        }
//        NSIndexPath *indexPaht = [NSIndexPath indexPathForRow:0 inSection:0];
//        [self.collectionView selectItemAtIndexPath:indexPaht animated:YES scrollPosition:UICollectionViewScrollPositionTop];
//        [self showGameType:0];
    }
}


- (void)showDefaultGame:(NSInteger)index
{
    NSIndexPath *indexPaht = [NSIndexPath indexPathForRow:index inSection:0];
    [self.collectionView selectItemAtIndexPath:indexPaht animated:YES scrollPosition:UICollectionViewScrollPositionCenteredHorizontally];
    [self.collectionView scrollToItemAtIndexPath:indexPaht atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally animated:YES];
}

- (void)showGameType:(NSInteger)index
{
    [self.selectedGameTypeArr removeAllObjects];
    GGGameModel *model = self.dataArr[index];
    self.gameModel = model;
    self.typeDataArr = [NSMutableArray arrayWithArray:model.taskType];
    [self.typeDataArr insertObject:_selectAllDic atIndex:0];
    [self.typeCollectionView reloadData];
    self.lblStatus.text = model.gameName;
}

- (void)startMatch
{
    //开始匹配
    if (self.selectedGameTypeArr.count == 0)
    {
        [XHToast showBottomWithText:@"请选择组队偏好"];
    }
    else
    {
        BOOL isAll = NO;
        for(NSDictionary *temp in self.selectedGameTypeArr)
        {
            if (![temp isKindOfClass:[AVObject class]])
            {
                isAll = YES;
            }
        }
        if (self.matchBtnClick)
        {
            self.matchBtnClick(self, self.gameModel, isAll?self.gameModel.taskType:self.selectedGameTypeArr);
        }
    }
}

#pragma mark - collectionView.Delegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    if (collectionView == self.collectionView)
    {
        return self.dataArr.count;
    }
    return self.typeDataArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        GGMatchGameCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:gameCellIden forIndexPath:indexPath];
        cell.layer.masksToBounds = YES;
        cell.layer.cornerRadius = 5;
        GGGameModel *model = self.dataArr[indexPath.row];
        [cell.iconImage setImageWithURL:[NSURL URLWithString:model.gameicon.url]];
        cell.titLabel.text = model.gameName;
        return cell;
    }
    GGMatchGameTypeCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:typeCellIden forIndexPath:indexPath];
    NSDictionary *temp = self.typeDataArr[indexPath.row];
    cell.titLabel.text = [temp objectForKey:@"name"];
    return cell;
}

// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        
        CGFloat width = 70;
        CGFloat height = 85;
        return  CGSizeMake(width,height);
        
    }
    CGFloat width = 80;
    CGFloat height = 30;
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
    
    return 10;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    if (collectionView == self.collectionView)
    {
        
        return 15;
    }
    return 15;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
    if (collectionView == self.collectionView)
    {
        [self showGameType:indexPath.row];
    }
    else
    {
        NSDictionary *dic = self.typeDataArr[indexPath.row];
        [self.selectedGameTypeArr addObject:dic];
    }
}


- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        
    }
    else
    {
        NSDictionary *dic = self.typeDataArr[indexPath.row];
        [self.selectedGameTypeArr removeObject:dic];
    }
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}



/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */

@end

