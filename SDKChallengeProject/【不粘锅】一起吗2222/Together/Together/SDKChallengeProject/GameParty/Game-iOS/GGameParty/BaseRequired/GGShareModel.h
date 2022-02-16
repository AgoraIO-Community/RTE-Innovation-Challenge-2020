//
//  GGShareModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/20.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGShareModel : NSObject

@property (nonatomic,strong)NSString *title;

@property (nonatomic,strong)NSString *descText;

@property (nonatomic,strong)NSArray *imageArray;
@property (nonatomic,strong)NSURL *shareUrl;

@end
