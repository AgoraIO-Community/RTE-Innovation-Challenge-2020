//
//  GGAppTool.h
//  GGameParty
//
//  Created by Victor on 2018/7/29.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGAppTool : NSObject

+ (NSString *)getCurrentIpAddress;

+ (void)getMobId:(NSDictionary *)customParams path:(NSString *)path source:(NSString *)source result:(void (^)(NSString *mobid))result;

+ (BOOL)userCanJoinGame:(NSString *)gameId;


+ (NSString *) compareCurrentTime:(NSDate *)date;

+ (BOOL)validateMobile:(NSString *)mobile;

+ (UIImage *)imageWithColor:(UIColor *)color size:(CGSize)size;

-(NSDate*) getDateWithDateString:(NSString*) dateString;
+ (CGSize)sizeWithText:(NSString *)text font:(UIFont *)font maxSize:(CGSize)maxSize;

/**
 生成房间名

 @return 房间名
 */
+ (NSString *)getRandomRoomTitle;
+ (NSDate *)getCurrentDayTime;

/**
 压缩图片

 @param source_image 原始图片
 @param maxSize 最大尺寸
 @return 
 */
+ (NSData *)resetSizeOfImageDataMethodTwo:(UIImage *)source_image maxSize:(int)maxSize;

@end
