//
//  GGEncryTool.m
//  GGameParty
//
//  Created by Victor on 2018/8/4.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGEncryTool.h"
#import <CommonCrypto/CommonCryptor.h>
#import "GGBase64.h"

//秘钥
#define gkey            @"guyuyincomdevelopeeeeeee"
//向量
#define gIv             @"01234567"

#define xorKey             @"AAAAAAAAAAAAAAAAAAAAAAAAAA"
@implementation GGEncryTool
//20269uekc`

+ (NSString *)xorEncryptorDecrypt:(NSString *)string
{
//20269
    NSData* bytes = [string dataUsingEncoding:NSUTF8StringEncoding];

    Byte  *myByte = (Byte *)[bytes bytes];

    NSData* keyBytes = [xorKey dataUsingEncoding:NSUTF8StringEncoding];

    Byte  *keyByte = (Byte *)[keyBytes bytes];

    int keyIndex = 0;

    for (int x = 0; x < [bytes length]; x++)
    {
        myByte[x]  = myByte[x] ^ keyByte[keyIndex];

        if (++keyIndex == [keyBytes length])
        {
            keyIndex = 0;
        }
    }
    //可以直接返回NSData
    NSData *newData = [[NSData alloc] initWithBytes:myByte length:[bytes length]];
    NSString *aString = [[NSString alloc] initWithData:newData encoding:NSUTF8StringEncoding];
    return aString;
//    NSArray *arr = @[@"a",@"b",@"c",@"d",@"e",@"f",@"g",@"h",@"i",@"j",@"k",@"l",@"m",@"n",@"o",@"p",@"q",@"r",@"s",@"t",@"u",@"v",@"w",@"x",@"y",@"z",@"A",@"B",@"C",@"D",@"E",@"F",@"G",@"H",@"I",@"J",@"K",@"L",@"M",@"N",@"O",@"P",@"Q",@"R",@"S",@"T",@"U",@"V",@"W",@"X",@"Y",@"Z"];
//    NSString *randomString = [NSString stringWithFormat:@"%@%@",arr[arc4random()%arr.count],arr[arc4random()%arr.count]];
//    return [NSString stringWithFormat:@"%@%@",aString,randomString];
//
//    NSInteger length = xorKey.length;
//
//    // 将OC字符串转换为C字符串
//    const char *keys = [xorKey cStringUsingEncoding:NSASCIIStringEncoding];
//
//    unsigned char cKey[length];
//
//    memcpy(cKey, keys, length);
//
//    // 数据初始化，空间未分配 配合使用 appendBytes
//    NSMutableData *encryptData = [[NSMutableData alloc] initWithCapacity:length];
//
//    // 获取字节指针
//    NSData* data = [string dataUsingEncoding:NSUTF8StringEncoding];
//    const Byte *point = data.bytes;
//
//    for (int i = 0; i < string.length; i++) {
//        int l = i % length;                     // 算出当前位置字节，要和密钥的异或运算的密钥字节
//        char c = cKey[l];
//        Byte b = (Byte) ((point[i]) ^ c);       // 异或运算
////        NSLog(@"%c",b);
//        [encryptData appendBytes:&b length:1];  // 追加字节
//    }
//    NSString *aString = [[NSString alloc] initWithData:encryptData encoding:NSUTF8StringEncoding];
//    return aString;
}




// 加密方法
+ (NSString*)encrypt:(NSString*)text {
    
    const void *vplainText;
    
    NSData* data = [text dataUsingEncoding:NSUTF8StringEncoding];
    size_t plainTextBufferSize = [data length];
    vplainText = (const void *)[data bytes];
    
    CCCryptorStatus ccStatus;
    uint8_t *bufferPtr = NULL;
    size_t bufferPtrSize = 0;
    size_t movedBytes = 0;
    
    bufferPtrSize = (plainTextBufferSize + kCCBlockSize3DES) & ~(kCCBlockSize3DES - 1);
    bufferPtr = malloc( bufferPtrSize * sizeof(uint8_t));
    memset((void *)bufferPtr, 0x0, bufferPtrSize);
    
    const void *vkey = (const void *) [gkey UTF8String];
//    const void *vkey = (const void *) [[NSString stringWithFormat:@"%@%@%@",gkey,gkey,gkey] UTF8String];

    const void *vinitVec = (const void *) [gIv UTF8String];
    
    ccStatus = CCCrypt(kCCEncrypt,
                       kCCAlgorithm3DES,
                       kCCOptionPKCS7Padding,
                       vkey,
                       kCCKeySize3DES,
                       vinitVec,
                       vplainText,
                       plainTextBufferSize,
                       (void *)bufferPtr,
                       bufferPtrSize,
                       &movedBytes);
    
    NSData *myData = [NSData dataWithBytes:(const void *)bufferPtr length:(NSUInteger)movedBytes];
    NSString *result = [GGBase64 base64EncodedStringFrom:myData];
    return result;
}

// 解密方法
+ (NSString*)decrypt:(NSString*)encryptText {
    NSData *encryptData = [GGBase64 dataWithBase64EncodedString:encryptText];
    size_t plainTextBufferSize = [encryptData length];
    const void *vplainText = [encryptData bytes];
    
    CCCryptorStatus ccStatus;
    uint8_t *bufferPtr = NULL;
    size_t bufferPtrSize = 0;
    size_t movedBytes = 0;
    
    bufferPtrSize = (plainTextBufferSize + kCCBlockSize3DES) & ~(kCCBlockSize3DES - 1);
    bufferPtr = malloc( bufferPtrSize * sizeof(uint8_t));
    memset((void *)bufferPtr, 0x0, bufferPtrSize);
    
    const void *vkey = (const void *) [gkey UTF8String];
    
    const void *vinitVec = (const void *) [gIv UTF8String];
    
    ccStatus = CCCrypt(kCCDecrypt,
                       kCCAlgorithm3DES,
                       kCCOptionPKCS7Padding,
                       vkey,
                       kCCKeySize3DES,
                       vinitVec,
                       vplainText,
                       plainTextBufferSize,
                       (void *)bufferPtr,
                       bufferPtrSize,
                       &movedBytes);
    
    NSString *result = [[NSString alloc] initWithData:[NSData dataWithBytes:(const void *)bufferPtr
                                                                     length:(NSUInteger)movedBytes] encoding:NSUTF8StringEncoding] ;
    return result;
}




@end
