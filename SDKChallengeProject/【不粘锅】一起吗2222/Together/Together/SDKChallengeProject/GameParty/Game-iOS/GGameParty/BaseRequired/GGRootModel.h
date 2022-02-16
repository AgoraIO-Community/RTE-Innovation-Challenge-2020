//
//  GGRootModel.h
//  GGameParty
//
//  Created by Victor on 2018/7/21.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGRootModel : NSObject

- (id)initWithDic:(NSDictionary *)dic;

- (void)setValue:(id)value forUndefinedKey:(NSString *)key;
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;

+ (NSString*)convertToJSONData:(id)infoDict;


@end
