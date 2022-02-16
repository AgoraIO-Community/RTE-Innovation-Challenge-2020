//
//  GGEncryTool.h
//  GGameParty
//
//  Created by Victor on 2018/8/4.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GGEncryTool : NSObject

+ (NSString *)xorEncryptorDecrypt:(NSString *)string;
// 加密方法
+ (NSString*)encrypt:(NSString*)plainText;

// 解密方法
+ (NSString*)decrypt:(NSString*)encryptText;

@end
