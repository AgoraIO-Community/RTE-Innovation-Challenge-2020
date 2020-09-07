//
//  GGUserAgePickerView.h
//  GGameParty
//
//  Created by Victor on 2018/8/5.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGPopupView.h"

@interface GGUserAgePickerView : GGPopupView

@property (nonatomic, strong, readonly) UIPickerView *pickerView;
@property (nonatomic, strong, readonly) UILabel *titleLabel;
@property (nonatomic, strong, readonly) UIButton *saveButton;
@property (nonatomic, strong, readonly) UIButton *cancelButton;

@property (nonatomic, copy) void (^saveClickedBlock)(GGUserAgePickerView *pickerView);
@property (nonatomic, copy) void (^cancelClickedBlock)(GGUserAgePickerView *pickerView);

@property (nonatomic, strong, readonly) NSString *selectedTimeString;
@property (nonatomic, assign, readonly) NSInteger selectedTimestamp;

@end
