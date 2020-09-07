//
//  GGQuestionViewController.h
//  GGameParty
//
//  Created by Victor on 2018/8/7.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGRootViewController.h"



@interface GGQuestionViewController : GGRootViewController



@property (nonatomic, copy) void (^anwserCompleteBlock)(GGQuestionViewController *vc,NSString *gameId,BOOL isSucccesed);
@property (nonatomic, strong) NSString  *gameId;

@end
