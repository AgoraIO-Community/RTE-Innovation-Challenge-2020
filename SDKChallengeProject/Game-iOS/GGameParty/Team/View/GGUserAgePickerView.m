//
//  GGUserAgePickerView.m
//  GGameParty
//
//  Created by Victor on 2018/8/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGUserAgePickerView.h"
#import "QFDatePickerView.h"
@interface GGUserAgePickerView()<UIPickerViewDataSource, UIPickerViewDelegate>
@end

@implementation GGUserAgePickerView


- (instancetype)init {
    return [self initWithFrame:CGRectZero];
}

- (instancetype)initWithFrame:(CGRect)frame {
    
    if (self = [super initWithFrame:frame]) {
        
        self.lblStatus.text = @"选择出生年";
        self.btnClose.hidden = YES;
        
        
     
//        _pickerView= [[UIPickerView alloc] init];
//        _pickerView.frame = CGRectMake(0, 0, frame.size.width, 216);
//        _pickerView.dataSource = self;
//        _pickerView.delegate = self;
//        _pickerView.showsSelectionIndicator = YES;
//        [self addSubview:_pickerView];
        
    }
    return self;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
