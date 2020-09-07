//
//  Base64.h
//  GGameParty
//
//  Created by Victor on 2018/7/28.
//  Copyright © 2018年 Victor. All rights reserved.
//

//#import <Foundation/Foundation.h>

//@interface Base64 : NSObject
extern size_t EstimateBas64EncodedDataSize(size_t inDataSize);
extern size_t EstimateBas64DecodedDataSize(size_t inDataSize);

extern bool Base64EncodeData(const void *inInputData, size_t inInputDataSize, char *outOutputData, size_t *ioOutputDataSize, BOOL wrapped);
extern bool Base64DecodeData(const void *inInputData, size_t inInputDataSize, void *ioOutputData, size_t *ioOutputDataSize);
//@end
