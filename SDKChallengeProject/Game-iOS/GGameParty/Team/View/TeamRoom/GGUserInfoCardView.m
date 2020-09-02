//
//  GGUserInfoCardView.m
//  GGameParty
//
//  Created by Victor on 2018/8/1.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserInfoCardView.h"
#import "GGUserGoodCell.h"
@interface GGUserInfoCardView()<UICollectionViewDelegate,
UICollectionViewDataSource>
{
    BOOL _canGood;
    NSString *_goodObjectId;
}

@property (nonatomic,strong)UIView *backView;

@property (nonatomic, strong) UIButton *btnClose;

@property (nonatomic, strong) UIButton *reporBtn;

@property (nonatomic,strong)UIImageView *headImageView;

@property (nonatomic,strong)UIImageView *sexImageView;

@property (nonatomic,strong)UILabel *ageLabel;

@property (nonatomic,strong)UILabel *nameLabel;
@property (nonatomic,strong)UILabel *noGoodLabel;

@property (nonatomic,strong)UICollectionView *collectionView;

@property (nonatomic,strong)UIButton *sendMsgBtn;

@property (nonatomic,strong)UIButton *followBtn;

@property (nonatomic,strong)UIButton *getOutBtn;

@property (nonatomic,strong)UIView *btn_line1;

@property (nonatomic,strong)UIView *btn_line2;

@property (nonatomic,strong)GGTeamModel *teamModel;

@property (nonatomic,strong)AVUser *user;

@property (nonatomic,strong)NSArray  *goodArr;


@property (strong, nonatomic) UIActivityIndicatorView *activityIndicator;

@property (nonatomic,strong)UIView *waitbackView;



@end


@implementation GGUserInfoCardView

- (instancetype)initWithFrame:(CGRect)frame user:(AVUser *)user teamdModel:(GGTeamModel *)teamModel
{
    self = [super initWithFrame:frame];
    if ( self )
    {
        self.teamModel = teamModel;
        self.user = user;
        self.tuandui = NO;
        self.zuijia = NO;
        self.taidu = NO;
        self.leyu = NO;
        _canGood = NO;
        self.waitbackView = [UIView new];
        [self addSubview:self.waitbackView];
        self.waitbackView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.waitbackView.layer.cornerRadius = 10;
        self.waitbackView.clipsToBounds = YES;
        self.waitbackView.backgroundColor = [UIColor whiteColor];
        
        self.activityIndicator = [[UIActivityIndicatorView alloc]initWithFrame:self.waitbackView.bounds];
        self.activityIndicator.backgroundColor = [UIColor whiteColor];
        self.activityIndicator.activityIndicatorViewStyle =  UIActivityIndicatorViewStyleWhiteLarge;
        [self.waitbackView addSubview:self.activityIndicator];
        self.activityIndicator.color = Main_Color;
        [self.activityIndicator startAnimating];
        
        self.backView = [UIView new];
        [self addSubview:self.backView];
        self.backView.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height - 10);
        self.backView.layer.cornerRadius = 10;
        self.backView.clipsToBounds = YES;
        self.backView.backgroundColor = [UIColor whiteColor];
        
        
        self.btnClose = [UIButton mm_buttonWithTarget:self action:@selector(actionClose)];
        [self.backView addSubview:self.btnClose];
        self.btnClose.frame = CGRectMake(self.frame.size.width - 50, 5, 40, 40);
        //  [self.btnClose setTitle:@"Close" forState:UIControlStateNormal];
        [self.btnClose setImage:[UIImage imageNamed:@"icon_guanbi"] forState:UIControlStateNormal];
        [self.btnClose setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        self.btnClose.titleLabel.font = [UIFont systemFontOfSize:14];
        
        self.reporBtn = [UIButton mm_buttonWithTarget:self action:@selector(reportUser)];
        [self.backView addSubview:self.reporBtn];
        self.reporBtn.frame = CGRectMake(10, 5, 40, 40);
       // [self.reporBtn setTitle:@"举报" forState:UIControlStateNormal];
        [self.reporBtn setTitleColor:RGBCOLOR(170, 170, 170) forState:UIControlStateNormal];
        self.reporBtn.titleLabel.font = [UIFont systemFontOfSize:14];
        
        CGFloat width = 60;
        CGFloat xx = (self.frame.size.width - 60)/2;
        self.headImageView = [[UIImageView alloc]initWithFrame:CGRectMake(xx, 20, width, width)];
        self.headImageView.image = [UIImage imageNamed:@"icon_linshi"];
        [self.backView addSubview:self.headImageView];
        self.headImageView.layer.masksToBounds = YES;
        self.headImageView.layer.cornerRadius = width/2;
        
        UIImageView *sex = [SJUIImageViewFactory imageViewWithImageName:@"icon_nv"];
        sex.frame = CGRectMake(CGRectGetMaxX(self.headImageView.frame) - 15, CGRectGetMaxY(self.headImageView.frame) - 15, 13, 13);
        [self.backView addSubview:sex];
        self.sexImageView = sex;
        
        
        UILabel *nameLabel = [SJUILabelFactory labelWithText:@"飞鸟新人" textColor:GGTitle_Color font:[UIFont boldSystemFontOfSize:16]];
        nameLabel.frame = CGRectMake(0, CGRectGetMaxY(self.headImageView.frame) + 5, self.frame.size.width, 25);
        nameLabel.textAlignment = NSTextAlignmentCenter;
        [self.backView addSubview:nameLabel];
        self.nameLabel = nameLabel;
        
        
        UILabel *ageLabel = [SJUILabelFactory labelWithText:@"22岁 · 六安市" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
        ageLabel.frame = CGRectMake(0, CGRectGetMaxY(self.nameLabel.frame), nameLabel.frame.size.width, 16);
        ageLabel.textAlignment = NSTextAlignmentCenter;
        [self.backView addSubview:ageLabel];
        self.ageLabel = ageLabel;
        
        
        UILabel *dddLabel = [SJUILabelFactory labelWithText:@"" textColor:RGBCOLOR(170,170,170) font:[UIFont systemFontOfSize:12]];
        dddLabel.frame = CGRectMake(0, CGRectGetMaxY(self.ageLabel.frame), nameLabel.frame.size.width, 16);
        dddLabel.textColor = [UIColor redColor];
        dddLabel.textAlignment = NSTextAlignmentCenter;
        [self.backView addSubview:dddLabel];
        self.noGoodLabel = dddLabel;
        
        
        UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
        flowLayout.minimumInteritemSpacing = 10;
        flowLayout.minimumLineSpacing = 10;
        flowLayout.itemSize =  CGSizeMake(60,65);
        [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
        //设置CollectionView的属性
        self.collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(ageLabel.frame) + 20, self.frame.size.width, 70) collectionViewLayout:flowLayout];
        self.collectionView.backgroundColor = [UIColor clearColor];
        self.collectionView.delegate = self;
        self.collectionView.dataSource = self;
        self.collectionView.scrollEnabled = NO;
        self.collectionView.allowsMultipleSelection = YES;
        [self.backView addSubview:self.collectionView];
        //注册Cell
        [self.collectionView registerClass:[GGUserGoodCell class] forCellWithReuseIdentifier:@"usergoodcell"];
        
        UIView *line = [[UIView alloc]init];
        line.backgroundColor = GGLine_Color;
        [self.backView addSubview:line];
        
        //队长----
        self.sendMsgBtn = [SJUIButtonFactory buttonWithTitle:@"发消息" titleColor:GGTitle_Color font:[UIFont systemFontOfSize:14] target:self sel:@selector(opertaBtnClick:)];
        self.sendMsgBtn.tag = 100;
        [self.backView addSubview:self.sendMsgBtn];
        
        
        self.btn_line1 = [SJUIViewFactory viewWithBackgroundColor:GGLine_Color];
        [self.backView addSubview:self.btn_line1];
        
        self.followBtn = [SJUIButtonFactory buttonWithTitle:@"关注Ta" titleColor:GGTitle_Color font:[UIFont systemFontOfSize:14] target:self sel:@selector(opertaBtnClick:)];
        self.followBtn.tag = 200;
        [self.backView addSubview:self.followBtn];
        
        self.btn_line2 = [SJUIViewFactory viewWithBackgroundColor:GGLine_Color];
        [self.backView addSubview:self.btn_line2];
        
        self.getOutBtn = [SJUIButtonFactory buttonWithTitle:@"踢出" titleColor:GGTitle_Color font:[UIFont systemFontOfSize:14] target:self sel:@selector(opertaBtnClick:)];
        [self.backView addSubview:self.getOutBtn];
        self.getOutBtn.tag = 300;
        
        CGFloat hh = 50;
        
        if([user.objectId isEqualToString:[AVUser currentUser].objectId])
        {
            //自己点自己头像
            self.sendMsgBtn.hidden = YES;
            self.followBtn.hidden = YES;
            self.getOutBtn.hidden = YES;
        }
        else if ([[AVUser currentUser].objectId isEqualToString:teamModel.publisher.objectId])
        {
            //队长点头像
            CGFloat ww = self.frame.size.width/3;
            
            line.frame = CGRectMake(0, self.frame.size.height - hh - 1, self.frame.size.width, 1);
            
            self.sendMsgBtn.frame = CGRectMake(0, self.frame.size.height - hh, ww, hh);
            self.btn_line1.frame = CGRectMake(CGRectGetMaxX(self.sendMsgBtn.frame) - 0.5, self.sendMsgBtn.frame.origin.y, 1, self.sendMsgBtn.frame.size.height);
            
            self.followBtn.frame = CGRectMake(CGRectGetMaxX(self.btn_line1.frame), self.frame.size.height - hh, ww, hh);
            self.btn_line2.frame = CGRectMake(CGRectGetMaxX(self.followBtn.frame) - 0.5, self.followBtn.frame.origin.y, 1, self.followBtn.frame.size.height);
            
            self.getOutBtn.frame = CGRectMake(CGRectGetMaxX(self.btn_line2.frame), self.frame.size.height - hh, ww, hh);
            [self loadData];
        }
        else
        {
            //普通用户点击头像
            line.frame = CGRectMake(0, self.frame.size.height - hh - 1, self.frame.size.width, 1);
            CGFloat ww = self.frame.size.width/2;
            self.sendMsgBtn.frame = CGRectMake(0, self.frame.size.height - hh, ww, hh);
            self.btn_line1.frame = CGRectMake(CGRectGetMaxX(self.sendMsgBtn.frame) - 0.5, self.sendMsgBtn.frame.origin.y, 1, self.sendMsgBtn.frame.size.height);
            
            self.followBtn.frame = CGRectMake(CGRectGetMaxX(self.btn_line1.frame), self.frame.size.height - hh, ww, hh);
            self.btn_line2.frame = CGRectMake(CGRectGetMaxX(self.followBtn.frame) - 0.5, self.followBtn.frame.origin.y, 1, self.followBtn.frame.size.height);

            [self loadData];
        }
        [self loadUserData];
        [self loadGoodData];
    }
    return self;
}

- (void)loadGoodData
{
    AVQuery *query = [AVQuery queryWithClassName:DB_User_Extra];
    [query whereKey:@"user" equalTo:self.user.objectId];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
                AVObject *obj = objects[0];
                _goodObjectId = obj.objectId;
                self.goodArr = @[[obj objectForKey:@"tuandui"],
                                 [obj objectForKey:@"zuijia"],
                                 [obj objectForKey:@"taidu"],
                                 [obj objectForKey:@"leyu"]];
            }
            else
            {
                self.goodArr = @[[NSNumber numberWithInteger:0],
                                [NSNumber numberWithInteger:0],
                                [NSNumber numberWithInteger:0],
                                 [NSNumber numberWithInteger:0]];
            }
            [self.collectionView reloadData];
        }
    }];
    
}

- (void)loadUserData
{
    self.backView.hidden = YES;
    AVQuery *query = [AVQuery queryWithClassName:DB_USER];
    [query whereKey:@"objectId" equalTo:self.user.objectId];
    
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            self.backView.hidden = NO;
            if (objects.count > 0)
            {
                self.user = objects[0];
                NSString *headUrl = [[self.user objectForKey:@"avatar"] objectForKey:@"url"];
                [self.headImageView setImageWithURL:[NSURL URLWithString:headUrl] placeholderImage:GGDefault_User_Head];
                
                self.nameLabel.text = self.user.username;
                
                NSNumber *sex = [self.user objectForKey:@"sex"];
                self.sexImageView.image = [UIImage imageNamed:[sex isEqual:@(YES)]?@"icon_nan":@"icon_nv"];
                
                NSInteger temp = [[self.user objectForKey:@"age"] integerValue];
                NSString *tempStr = [NSString stringWithFormat:@"%ld",(long)temp];
                NSString *info = [NSString stringWithFormat:@"%@岁 · %@", [tempStr isEqualToString:@"0"]?@"0":tempStr,[[self.user objectForKey:@"lastAddress"] length] == 0?@"":[self.user objectForKey:@"lastAddress"]];
                self.ageLabel.text = info;
                self.noGoodLabel.text = [[self.user objectForKey:@"warn"] length] == 0?@"":[self.user objectForKey:@"warn"];
            }
            else
            {
                self.backView.hidden = NO;
            }
        }
        else
        {
            self.backView.hidden = NO;
        }
    }];
}


- (void)loadData
{
    AVQuery *query = [AVUser followeeQuery:[AVUser currentUser].objectId];
    [query whereKey:@"followee" equalTo:self.user];
    [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count != 0)
            {
                [self.followBtn setTitle:@"取消关注" forState:UIControlStateNormal];
            }
        }
    }];
    
    [GGUserModel queryGoodRelation:self.user.objectId withBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
        if (!error)
        {
            if (objects.count > 0)
            {
                //不能点赞
                _canGood = NO;
            }else
            {
                _canGood = YES;
            }
        }
    }];
    
    
}

- (void)opertaBtnClick:(UIButton *)btn
{
    switch (btn.tag)
    {
        case 100:
            //发消息
            if (self.sendMegClick)
            {
                self.sendMegClick(self, self.user);
            }
            break;
        case 200:
            //关注他
            if ([btn.titleLabel.text isEqualToString:@"取消关注"])
            {
                [[AVUser currentUser] unfollow:self.user.objectId andCallback:^(BOOL succeeded, NSError *error) {
                    if (succeeded)
                    {
                        [btn setTitle:@"关注Ta" forState:UIControlStateNormal];
                    }
                }];
            }
            else
            {
                [[AVUser currentUser]follow:self.user.objectId andCallback:^(BOOL succeeded, NSError * _Nullable error) {
                    if (succeeded) {
                        [btn setTitle:@"取消关注" forState:UIControlStateNormal];
                        [GGUserModel postFollowMsg:self.user.objectId];
                    }
                    
                }];
            }

            break;
        case 300:
            //踢出这个team
            
            if (self.getOutRoomClick)
            {
                self.getOutRoomClick(self, self.user);
            }
            break;
            
        default:
            break;
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
    return 4;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identify = @"usergoodcell";
    GGUserGoodCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identify forIndexPath:indexPath];
  //  NSArray *titleArr = @[@"团队主力",@"最佳队友",@"态度友好",@"乐于助人"];
    NSArray *imgArr = @[@"icon_zhuli",@"icon_zuijia",@"icon_youhao",@"icon_zhuren"];
    if (self.goodArr.count == 4)
    {
        NSString *temp = [NSString stringWithFormat:@"%@",self.goodArr[indexPath.row]];
        cell.numLabel.text = temp;
        cell.imageView.image = [UIImage imageNamed:imgArr[indexPath.row]];
    }
    
    return cell;
}



// 定义每个UICollectionView的大小
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    CGFloat width = 62;
    CGFloat height = 65;
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
    CGFloat jianju = (self.frame.size.width - 62 * 4 - 20)/3;
    return jianju;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 10;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    // Medal *p = self.medals[indexPath.item];
     GGUserGoodCell *cell = (GGUserGoodCell *)[collectionView cellForItemAtIndexPath:indexPath];
    if (_canGood)
    {
        if (self.goodArr.count == 4)
        {
            NSInteger num = [cell.numLabel.text integerValue] + 1;
            NSString *temp = [NSString stringWithFormat:@"%ld",(long)num];
            cell.numLabel.text = temp;
            cell.goodImage.hidden = NO;
            [UIView animateWithDuration:0.3 animations:^{
                cell.goodImage.mj_y = cell.goodImage.frame.origin.y + 5;
            } completion:^(BOOL finished) {
                cell.goodImage.mj_y = cell.goodImage.frame.origin.y - 5;
            }];
        }
        if (indexPath.row == 0)
        {
            _tuandui = YES;
        }
        if (indexPath.row == 1)
        {
            _zuijia = YES;
        }
        if (indexPath.row == 2)
        {
            _taidu = YES;
        }
        if (indexPath.row == 3)
        {
            _leyu = YES;
        }
    }
    else
    {
        [XHToast showBottomWithText:@"一个用户一天只能点赞一次噢"];
    }
}

- (void)collectionView:(UICollectionView *)collectionView didDeselectItemAtIndexPath:(nonnull NSIndexPath *)indexPath
{
    GGUserGoodCell *cell = (GGUserGoodCell *)[collectionView cellForItemAtIndexPath:indexPath];
    if (_canGood)
    {
        if (self.goodArr.count == 4)
        {
            
            NSInteger num = [cell.numLabel.text integerValue] - 1;
            NSString *temp = [NSString stringWithFormat:@"%ld",(long)num];
            cell.numLabel.text = temp;
            cell.goodImage.hidden = YES;
        }
        if (indexPath.row == 0)
        {
            _tuandui = NO;
        }
        if (indexPath.row == 1)
        {
            _zuijia = NO;
        }
        if (indexPath.row == 2)
        {
            _taidu = NO;
        }
        if (indexPath.row == 3)
        {
            _leyu = NO;
        }
    }
}

- (BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}


#pragma makr - Method

- (void)reportUser
{
    
}

- (void)actionClose
{
    BOOL isMy = [[AVUser currentUser].objectId isEqualToString:self.user.objectId];
    if (isMy == NO && _canGood && (self.taidu != NO || self.zuijia != NO || self.tuandui != NO || self.leyu != NO))
    {
        AVObject *obj;
        if (_goodObjectId) {
            obj = [AVObject objectWithClassName:DB_User_Extra objectId:_goodObjectId];
        }
        else
        {
            obj = [AVObject objectWithClassName:DB_User_Extra];
        }
        
        [obj setObject:self.user.objectId forKey:@"user"];
        obj.fetchWhenSave = true;
        if (self.tuandui)
        {
            [obj incrementKey:@"tuandui" byAmount:@(1)];
        }
        if (self.zuijia)
        {
            [obj incrementKey:@"zuijia" byAmount:@(1)];
        }
        if (self.taidu)
        {
            [obj incrementKey:@"taidu" byAmount:@(1)];
        }
        if (self.leyu)
        {
            [obj incrementKey:@"leyu" byAmount:@(1)];
        }
        [obj saveInBackground];
        [GGUserModel userGoodRelation:self.user.objectId];
        
    }
    if (self.closeBtnClick)
    {
        self.closeBtnClick(self);
    }
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
