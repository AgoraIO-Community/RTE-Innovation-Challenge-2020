//
//  GGCreateRoomView.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGCreateRoomView.h"
#import "GGSpeedMatchView.h"
#import "GGMatchGameCell.h"
#import "GGMatchGameTypeCell.h"
#import "GGGameModel.h"
#import "UIImage+AFNetworking.h"
#import "UIImageView+AFNetworking.h"
#import "GGSpeedCreateCell.h"
#import "GGSpeedCreateTypeCell.h"
#import "GGGameTypeModel.h"
static NSString *gameCellIden =  @"gameCellIden";
static NSString *typeCellIden  = @"typeCellIden";

static NSString *pNumCellIden  = @"pNumCellIden";

@interface GGCreateRoomView ()<UICollectionViewDelegate,
UICollectionViewDataSource>

@property (nonatomic,strong)UICollectionView *collectionView;

@property (nonatomic,strong)UICollectionView *typeCollectionView;

@property (nonatomic,strong)UICollectionView *pNumcollectionView;//人数的选择

@property (nonatomic,strong)UIButton *sureButton;

@property (nonatomic,strong)NSMutableArray *dataArr;

@property (nonatomic,strong)NSMutableArray *typeDataArr;

@property (nonatomic,strong)GGGameModel *gameModel;

@property (nonatomic,strong)GGGameTypeModel *gameTypeModel;

@property (nonatomic,assign)NSInteger maxnum;

@end



@implementation GGCreateRoomView

- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        [self addSubview:self.waitbackView];
        [self.waitbackView addSubview:self.activityIndicator];
       
        [self addSubview:self.backView];
        [self.backView addSubview:self.btnClose];
        [self.backView addSubview:self.lblStatus];
        
        self.lblStatus.text = @"全部游戏";
        self.lblStatus.textColor = GGTitle_Color;
        //UIColor *bgColor = [UIColor colorWithPatternImage: [UIImage imageNamed:@"bg_yijianzudui"]];
        self.backView.backgroundColor = [UIColor whiteColor];
        
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 10;
        flowLayout.minimumLineSpacing = 10;
        flowLayout.itemSize =  CGSizeMake(113,50);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 60, self.frame.size.width, 50) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = YES;
        self.collectionView.showsHorizontalScrollIndicator = NO;
        [self.backView addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGSpeedCreateCell class] forCellWithReuseIdentifier:gameCellIden];
        
        UILabel *label = [SJUILabelFactory labelWithText:@"房间类型:" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
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
        [self.typeCollectionView registerClass:[GGSpeedCreateTypeCell class] forCellWithReuseIdentifier:typeCellIden];
        
        
        UILabel *label2 = [SJUILabelFactory labelWithText:@"房间人数:" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
        label2.frame = CGRectMake(15, CGRectGetMaxY(self.typeCollectionView.frame) + 15, 300, 20);
        [self.backView addSubview:label2];
        
        
        UICollectionViewFlowLayout *pNumLayout = [[UICollectionViewFlowLayout alloc] init];
        pNumLayout.minimumInteritemSpacing = 10;
        pNumLayout.minimumLineSpacing = 10;
        pNumLayout.itemSize =  CGSizeMake(80,30);
        [pNumLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
        //设置CollectionView的属性
        self.pNumcollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(label2.frame) + 10, self.frame.size.width, 30) collectionViewLayout:pNumLayout];
        self.pNumcollectionView.backgroundColor = [UIColor clearColor];
        self.pNumcollectionView.delegate = self;
        self.pNumcollectionView.dataSource = self;
        self.pNumcollectionView.scrollEnabled = YES;
        self.pNumcollectionView.showsHorizontalScrollIndicator = NO;
        [self.backView addSubview:self.pNumcollectionView];
        //注册Cell
        [self.pNumcollectionView registerClass:[GGSpeedCreateTypeCell class] forCellWithReuseIdentifier:pNumCellIden];
        
 
        
        self.sureButton = [SJUIButtonFactory buttonWithTitle:@"立即创建" titleColor:[UIColor whiteColor] backgroundColor:RGBCOLOR(28, 181, 73) imageName:@"" target:self sel:@selector(startMatch) tag:3];
        self.sureButton.frame = CGRectMake(15, CGRectGetMaxY(self.self.pNumcollectionView.frame) + 15, self.frame.size.width - 30, 45);
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
    self.gameTypeModel = nil;
    self.maxnum = 4;//每次选择游戏的时候都先重置数据
    
    GGGameModel *model = self.dataArr[index];
    self.gameModel = model;
    self.typeDataArr = [NSMutableArray arrayWithArray:model.taskType];
    [self.typeCollectionView reloadData];
    self.lblStatus.text = model.gameName;
    
    if (self.typeDataArr.count > 0)
    {
        NSIndexPath *indexPaht = [NSIndexPath indexPathForRow:0 inSection:0];
        [self.typeCollectionView scrollToItemAtIndexPath:indexPaht atScrollPosition:UICollectionViewScrollPositionTop animated:YES];
        [self.typeCollectionView selectItemAtIndexPath:indexPaht animated:YES scrollPosition:UICollectionViewScrollPositionTop];
        [self showMaxDefaultNum:0];
    }
    
}

- (void)showMaxDefaultNum:(NSInteger)row
{
    NSDictionary *dic = self.typeDataArr[row];
    
    GGGameTypeModel *model =  [GGGameTypeModel new];
    model.objectId = dic[@"objectId"];
    model.name = dic[@"name"];
    model.maxnum = [dic objectForKey:@"maxnum"];
    self.gameTypeModel = model;
    
    NSInteger num = [[dic objectForKey:@"maxnum"] integerValue];
    NSInteger index = 0;
    if (num == 2)
    {
        index = 0;
    }
    if (num == 4)
    {
        index = 1;
    }
    if (num == 5)
    {
        index = 2;
    }
    if (num == 6)
    {
        index = 3;
    }
    if (num == 8)
    {
        index = 4;
    }
    NSIndexPath *indexPaht = [NSIndexPath indexPathForRow:index inSection:0];
    [self.pNumcollectionView scrollToItemAtIndexPath:indexPaht atScrollPosition:UICollectionViewScrollPositionTop animated:YES];
    [self.pNumcollectionView selectItemAtIndexPath:indexPaht animated:YES scrollPosition:UICollectionViewScrollPositionTop];
}




- (void)startMatch
{
    if (self.gameModel==nil || self.gameTypeModel==nil || _maxnum == 0)
    {
         [XHToast showBottomWithText:@"请完善房间设置"];
    }
    else
    {
        if (self.createBtnClick)
        {
            self.createBtnClick(self, self.gameModel, self.gameTypeModel, self.maxnum);
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
    if (collectionView == self.pNumcollectionView)
    {
        return 5;
    }
    return self.typeDataArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        GGSpeedCreateCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:gameCellIden forIndexPath:indexPath];
        cell.layer.masksToBounds = YES;
        cell.layer.cornerRadius = 5;
        GGGameModel *model = self.dataArr[indexPath.row];
        [cell.iconImage setImageWithURL:[NSURL URLWithString:model.gameicon.url]];
        cell.titLabel.text = model.gameName;
        return cell;
    }
    
    if (collectionView == self.pNumcollectionView)
    {
        GGSpeedCreateTypeCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:pNumCellIden forIndexPath:indexPath];
        cell.titLabel.text = @[@"2人",@"4人",@"5人",@"6人",@"8人"][indexPath.row];
        return cell;
    }
    GGSpeedCreateTypeCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:typeCellIden forIndexPath:indexPath];
    NSDictionary *temp = self.typeDataArr[indexPath.row];
    cell.titLabel.text = [temp objectForKey:@"name"];
    return cell;
}

// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        
        CGFloat width = 113;
        CGFloat height = 49;
        return  CGSizeMake(width,height);
        
    }
    CGFloat width = 80;
    CGFloat height = 30;
    return  CGSizeMake(width,height);
    
}

// 定义整个CollectionViewCell与整个View的间距
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    
    return UIEdgeInsetsMake(0, 10,0, 10);//（上、左、下、右）
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
        
        return 10;
    }
    return 10;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
    if (collectionView == self.collectionView)
    {
        [self showGameType:indexPath.row];
        
    }
    if (collectionView == self.pNumcollectionView)
    {
        if (indexPath.row == 0)
        {
            self.maxnum = 2;
        }
        if (indexPath.row == 1)
        {
            self.maxnum = 4;
        }
        if (indexPath.row == 2)
        {
            self.maxnum = 5;
        }
        if (indexPath.row == 3)
        {
            self.maxnum = 6;
        }
        if (indexPath.row == 4)
        {
            self.maxnum = 8;
        }
    }
    if (collectionView == self.typeCollectionView)
    {
        
        [self showMaxDefaultNum:indexPath.row];
    }
}

- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(NSIndexPath *)indexPath
{
    if (collectionView == self.collectionView)
    {
        
    }
    if (collectionView == self.pNumcollectionView)
    {
       
    }
    else
    {
       // NSDictionary *dic = self.typeDataArr[indexPath.row];
      
    }
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}



@end
