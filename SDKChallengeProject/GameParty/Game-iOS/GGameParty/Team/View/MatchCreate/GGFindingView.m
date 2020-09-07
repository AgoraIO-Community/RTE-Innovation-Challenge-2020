//
//  GGFindingView.m
//  GGameParty
//
//  Created by Victor on 2018/8/2.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGFindingView.h"

@interface GGFindingView()
{
    dispatch_source_t _timer;

}
@property (nonatomic, assign) int timeout;



@end

@implementation GGFindingView


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame])
    {
//        UIColor *bgColor = [UIColor colorWithPatternImage: [UIImage imageNamed:@"bg_xunzhaozhong"]];
        UIColor *bgColor = GGHEXCOLOR(@"22272D");
        self.backgroundColor = bgColor;
        
        self.titLabel = [SJUILabelFactory labelWithText:@"正在为你寻找队友...15s" textColor:[UIColor whiteColor] font:[UIFont systemFontOfSize:16]];
        self.titLabel.frame = CGRectMake(15, 15, self.frame.size.width - 85, 25);
        [self addSubview:self.titLabel];
        
        UIView *circle = [[UIView alloc]initWithFrame:CGRectMake(15, CGRectGetMaxY(self.titLabel.frame) + 6, 3, 3)];
        circle.backgroundColor = RGBCOLOR(204, 204, 204);
        [self addSubview:circle];
        
        
        self.currentOnlineLabel = [SJUILabelFactory labelWithText:@"超时未找到房间将自动创建" textColor:RGBCOLOR(204, 204, 204) font:[UIFont systemFontOfSize:11]];
        self.currentOnlineLabel.frame = CGRectMake(20, CGRectGetMaxY(self.titLabel.frame), self.frame.size.width - 85, 15);
        [self addSubview:self.currentOnlineLabel];
        
        self.stopBtn = [SJUIButtonFactory buttonWithTitle:@"停止" titleColor:[UIColor whiteColor] font:[UIFont boldSystemFontOfSize:12] target:self sel:@selector(stopBtnClick)];
        self.stopBtn.frame = CGRectMake(self.frame.size.width - 75, 19, 60, 30);
        self.stopBtn.backgroundColor = Main_Color;
        self.stopBtn.layer.masksToBounds = YES;
        self.stopBtn.layer.cornerRadius = 15;
        [self addSubview:self.stopBtn];
        
        [self startTime];
        
    }
    return self;
}

- (void)continueFind
{
    [self startTime];
}

/** 开启倒计时 */
- (void)startTime {
    
    _timeout = 15;
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    
    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    
    dispatch_source_set_event_handler(_timer, ^{
        
        //每秒查询一次是否有房间
        
         if(_timeout < 1 ){// 倒计时结束//10s
            // 关闭定时器
            dispatch_source_cancel(_timer);
            
            dispatch_async(dispatch_get_main_queue(), ^{
                if (self.findedFailedBlock) {
                    self.findedFailedBlock(self);
                }
            });
            
        }else{
            
            // 显示倒计时结果
            
            NSString *strTime = [NSString stringWithFormat:@"正在为你寻找队友...%ds", _timeout];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                
                self.titLabel.text = strTime;
            });
            
            _timeout--;
        }
    });
    
    // 开启定时器
    dispatch_resume(_timer);
    
}


- (void)stopBtnClick
{
    if(_timer){
        dispatch_source_cancel(_timer);
        _timer = nil;
    }
    self.stopFindBlock(self);
    
}

- (void)stopTimer
{
    if(_timer){
        dispatch_source_cancel(_timer);
        _timer = nil;
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
