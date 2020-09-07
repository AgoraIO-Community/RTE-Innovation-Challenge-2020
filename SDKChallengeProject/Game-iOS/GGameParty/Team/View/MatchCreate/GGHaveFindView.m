//
//  GGHaveFindView.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGHaveFindView.h"

@interface GGHaveFindView ()
@property(nonatomic,strong)UILabel *timeLabel;
@property(nonatomic,strong)UILabel *titLabel;
@property (nonatomic, assign) int timeout;

@end

@implementation GGHaveFindView
- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
        UIView *back = [[UIView alloc]initWithFrame:self.bounds];
        back.backgroundColor = [UIColor whiteColor];
        back.layer.cornerRadius = 10;
        back.layer.masksToBounds = YES;
        [self addSubview:back];
        
        UILabel *tit = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, self.frame.size.width, 25)];
        tit.text = @"已为你找到符合要求的房间";
        tit.font = [UIFont boldSystemFontOfSize:16];
        tit.textAlignment = NSTextAlignmentCenter;
        [self addSubview:tit];
        self.titLabel = tit;
        
        UILabel *desc = [[UILabel alloc]initWithFrame:CGRectMake(0, CGRectGetMaxY(tit.frame) + 5, self.frame.size.width, 18)];
        desc.font = [UIFont systemFontOfSize:12];
        desc.textAlignment = NSTextAlignmentCenter;
        desc.textColor = Main_Color;
        [self addSubview:desc];
        self.timeLabel = desc;
        
        [self startTime];
    }
    return self;
}

- (void)changeTitle:(NSString *)title
{
    self.titLabel.text = title;
}


/** 开启倒计时 */
- (void)startTime {
    
    _timeout = 3;
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    dispatch_source_t _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    
    dispatch_source_set_event_handler(_timer, ^{
        
        //每秒查询一次是否有房间
        
        if(_timeout < 1){// 倒计时结束
            // 关闭定时器
            dispatch_source_cancel(_timer);
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                if (self.closeBtnClick) {
                    self.closeBtnClick(self);
                }
            });
            
        }else{
            
            
            
            NSString *strTime = [NSString stringWithFormat:@"正在进入...%ds", _timeout];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                self.timeLabel.text = strTime;

            });
            
            _timeout --;
        }
    });
    
    // 开启定时器
    dispatch_resume(_timer);
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
