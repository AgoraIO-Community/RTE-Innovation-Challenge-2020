//
//  GGAppTool.m
//  GGameParty
//
//  Created by Victor on 2018/7/29.
//  Copyright © 2018年 Victor. All rights reserved.
//

#import "GGAppTool.h"

@implementation GGAppTool
+ (NSString *)getCurrentIpAddress
{
    NSError *error;
    
        NSURL *ipURL = [NSURL URLWithString:@"http://pv.sohu.com/cityjson?ie=utf-8"];
    
        NSMutableString *ip = [NSMutableString stringWithContentsOfURL:ipURL encoding:NSUTF8StringEncoding error:&error];
    
        //判断返回字符串是否为所需数据
    
        if ([ip hasPrefix:@"var returnCitySN = "]) {
        
                //对字符串进行处理，然后进行json解析
        
                //删除字符串多余字符串
        
                NSRange range = NSMakeRange(0, 19);
        
                [ip deleteCharactersInRange:range];
        
                NSString * nowIp =[ip substringToIndex:ip.length-1];
        
                //将字符串转换成二进制进行Json解析
        
                NSData * data = [nowIp dataUsingEncoding:NSUTF8StringEncoding];
        
               NSDictionary * dict = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
        
                NSLog(@"%@",dict);
        
                return dict[@"cip"] ? dict[@"cip"] : @"";
            }
      return @"";
}


+ (void)getMobId:(NSDictionary *)customParams path:(NSString *)path source:(NSString *)source result:(void (^)(NSString *mobid))result
{
    MLSDKScene *scene = [[MLSDKScene alloc] initWithMLSDKPath:path source:source params:customParams];
    [MobLink getMobId:scene result:result];
}

+ (BOOL)userCanJoinGame:(NSString *)gameId
{
    NSArray *arr =  [GGUserStandarDefault objectForKey:GGGAME_DATA_KEY];
    for(NSDictionary *obj in arr)
    {
        
        if ([gameId isEqualToString:obj[@"gameId"]])
        {//找到此游戏
            BOOL isNeedQ = [[obj objectForKey:@"isNeedQ"] isEqualToString:@"1"];
            if (isNeedQ)
            {
                //需要答题
                NSArray *anwserArr = [[AVUser currentUser] objectForKey:@"allowGame"];
                
                if ([anwserArr containsObject:gameId])
                {
                    return NO;
                    break;
                }
                else
                {
                    return YES;
                    break;
                }

            }
            else
            {
                return NO;
                break;
            }
        }
    }
    return NO;
}

+ (NSString *) compareCurrentTime:(NSDate *)date
{
    
    //把字符串转为NSdate
//    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //[dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];
    //NSDate *timeDate = [dateFormatter dateFromString:str];
    NSDate *timeDate = date;
    //得到与当前时间差
    NSTimeInterval time = fabs([[NSDate date] timeIntervalSinceDate:timeDate]);
    
    NSString *returnString = @"";
    if(time < 60)
        returnString = @"刚刚";
    else if(time >=60 && time < 3600)
        returnString = [NSString stringWithFormat:@"%.0f分钟前",time/60];
    else if(time >= 3600 && time < 3600 * 24)
        returnString = [NSString stringWithFormat:@"%.0f小时前",time/(60 * 60)];
    else if(time >= 3600 * 24 && time < 3600 * 24 * 30)
        returnString = [NSString stringWithFormat:@"%.0f天前",time/(60 * 60 * 24)];
    else if(time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12)
        returnString = [NSString stringWithFormat:@"%.0f月前",time/(60 * 60 * 24 * 30)];
    else if(time >= 3600 * 24 * 30 * 12)
        returnString = [NSString stringWithFormat:@"%.0f年前",time/(60 * 60 * 24 * 30 * 12)];
    return  returnString;
}

+ (BOOL)validateMobile:(NSString *)mobile
{
    // 130-139  150-153,155-159  180-189  145,147  170,171,173,176,177,178
    NSString *phoneRegex = @"^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[57])|(17[013678]))\\d{8}$";
    NSPredicate *phoneTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",phoneRegex];
    return [phoneTest evaluateWithObject:mobile];
}

+ (CGSize)sizeWithText:(NSString *)text font:(UIFont *)font maxSize:(CGSize)maxSize

{
    
    NSDictionary *attrs = @{NSFontAttributeName : font};
    
    CGSize textSize2 = [text boundingRectWithSize:maxSize
                                                  options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin
                                               attributes:attrs
                                                  context:nil].size;
    
    
  //  return [text boundingRectWithSize:maxSize options:NSStringDrawingUsesLineFragmentOrigin attributes:attrs context:nil].size;
    return textSize2;
}



+ (UIImage *)imageWithColor:(UIColor *)color size:(CGSize)size
{
    
    if (!color || size.width <= 0 || size.height <= 0) return nil;
    
    CGRect rect = CGRectMake(0.0f, 0.0f, size.width, size.height);
    
    UIGraphicsBeginImageContextWithOptions(rect.size, NO, 0);
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGContextSetFillColorWithColor(context, color.CGColor);
    
    CGContextFillRect(context, rect);
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return image;
    
}

-(NSDate*) getDateWithDateString:(NSString*) dateString
{
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *date = [dateFormat dateFromString:dateString];
    return date;
}


+ (NSString *)getRandomRoomTitle
{
    
//    房间缺人，速度进来。
//    快来和我一起组队吧！
//    游戏不等人，速进。
//    带你游戏带你飞
//    求大佬带我玩
//    带萌新玩游戏
    NSArray *arr = @[@"房间缺人，速度进来",
                     @"快来和我一起组队吧",
                     @"游戏不等人，速进",
                     @"带你游戏带你飞",
                     @"求大佬带我玩",
                     @"带萌新玩游戏"];
    
    //return arr[arc4random()%6];
    return [NSString stringWithFormat:@"%@的语音房间",[AVUser currentUser].username];
}

+ (NSDate *)getCurrentDayTime
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateTime = [formatter stringFromDate:[NSDate date]];
    NSDate *destDate= [formatter dateFromString:dateTime];
    return destDate;
}


+ (NSData *)resetSizeOfImageData:(UIImage *)source_image maxSize:(int)maxSize{
    //先调整分辨率
    CGSize  newSize = CGSizeMake(source_image.size.width, source_image.size.height);
    CGFloat tempHeight = newSize.height / 1024;
    CGFloat tempWidth  = newSize.width / 1024;
    if (tempWidth>1.0 && tempWidth>tempHeight) {
        newSize = CGSizeMake(source_image.size.width / tempWidth, source_image.size.height / tempWidth);
    }else if (tempHeight > 1.0 && tempWidth < tempHeight){
        newSize = CGSizeMake(source_image.size.width / tempHeight,source_image.size.height / tempHeight);
    }
    UIGraphicsBeginImageContext(newSize);
    [source_image drawAsPatternInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage * newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    //先判断当前质量是否满足要求，不满足再进行压缩
    NSData * finallImageData = UIImageJPEGRepresentation(newImage, 1.0);
    int sizeOriginKB = (int)finallImageData.length/1024;
    
    NSLog(@"1===sizeOriginKB===%i",sizeOriginKB);
    
    if (sizeOriginKB<=maxSize) {
        return finallImageData;
    }
    //保存压缩系数
    NSMutableArray * compressionQualityArr = [NSMutableArray array];
    CGFloat avg = 1.0/250;
    CGFloat value = avg;
    for (int i = 250; i>=1; i--) {
        value = i* avg;
        [compressionQualityArr addObject:@(value)];
    }
    //调整大小
    //说明：压缩系数数组compressionQualityArr是从大到小存储。
    //思路：折半计算，如果中间压缩系数仍然降不到目标值maxSize，则从后半部分开始寻找压缩系数；反之从前半部分寻找压缩系数
    finallImageData = UIImageJPEGRepresentation(newImage, [compressionQualityArr[125] floatValue]);
    if (finallImageData.length/1024>maxSize) {
        //拿到最初的大小
        finallImageData = UIImageJPEGRepresentation(newImage, 1.0);
        //从后半部分开始
        for (int i=126; i<250; i++) {
            
            int sizeOriginKB = (int)finallImageData.length/1024;
            NSLog(@"2===sizeOriginKB==%i",sizeOriginKB);
            if (sizeOriginKB>maxSize) {
                finallImageData = UIImageJPEGRepresentation(newImage, [compressionQualityArr[i] floatValue]);
            }else
            {
                break;
            }
        }
    }else{
        //拿到最初的大小
        finallImageData = UIImageJPEGRepresentation(newImage, 1.0);
        for (int i = 0; i <125; i++) {
            int sizeOriginKB = (int)finallImageData.length/1024;
            NSLog(@"3===sizeOriginKB==%i",sizeOriginKB);
            if (sizeOriginKB>maxSize) {
                finallImageData = UIImageJPEGRepresentation(newImage, [compressionQualityArr[i] floatValue]);
            }else
            {
                break;
            }
        }
    }
    return finallImageData;
    
}

+ (NSData *)resetSizeOfImageDataMethodTwo:(UIImage *)source_image maxSize:(int)maxSize
{
    
    //先判断当前质量是否满足要求，不满足再进行压缩
    NSData * finallImageData = UIImageJPEGRepresentation(source_image, 1.0);
    int sizeOriginKB = (int)finallImageData.length/1024;
    
    NSLog(@"1===sizeOriginKB===%i",sizeOriginKB);
    
    if (sizeOriginKB<=maxSize) {
        return finallImageData;
    }
    //先调整分辨率
    CGSize defaultSize = CGSizeMake(1024, 1024);
    UIImage *newImage = [GGAppTool newSizeImage:defaultSize source_image:source_image];
    finallImageData = UIImageJPEGRepresentation(newImage, 1.0);
    //保存压缩系数
    NSMutableArray * compressionQualityArr = [NSMutableArray array];
    CGFloat avg = 1.0/250;
    CGFloat value = avg;
    for (int i = 250; i>=1; i--) {
        value = i* avg;
        [compressionQualityArr addObject:@(value)];
    }
    /*
     调整大小
     说明：压缩系数数组compressionQualityArr是从大到小存储。
     */
    //思路：使用二分法搜索
    finallImageData = [GGAppTool halfFuntion:compressionQualityArr image:newImage sourceData:finallImageData maxSize:maxSize];
    //如果还是未能压缩到指定大小，则进行降分辨率
    while (finallImageData.length==0) {
        //每次降100分辨率
        if (defaultSize.width-100 <= 0 || defaultSize.height-100 <= 0){
            break;
        }
        defaultSize = CGSizeMake(defaultSize.width-100, defaultSize.height-100);
        UIImage * image = [GGAppTool newSizeImage:defaultSize source_image:[UIImage imageWithData:UIImageJPEGRepresentation(newImage, [compressionQualityArr.lastObject floatValue])]];
        finallImageData = [GGAppTool halfFuntion:compressionQualityArr image:image sourceData:UIImageJPEGRepresentation(image,1.0) maxSize:maxSize];
    }
    int sizeOriginKB2 = (int)finallImageData.length/1024;
    NSLog(@"2===sizeOriginKB===%i",sizeOriginKB2);
    return finallImageData;
    
}

+ (UIImage *)newSizeImage:(CGSize )size source_image:(UIImage *)source_image
{
    CGSize newSize = CGSizeMake(source_image.size.width, source_image.size.height);
    CGFloat tempHeight = newSize.height / size.height;
    CGFloat tempWidth = newSize.width / size.width;
    if (tempWidth > 1.0 && tempWidth > tempHeight) {
        newSize =  CGSizeMake(source_image.size.width / tempWidth, source_image.size.height / tempWidth);
        
    } else if (tempHeight > 1.0 && tempWidth < tempHeight){
        newSize = CGSizeMake(source_image.size.width / tempHeight,  source_image.size.height / tempHeight)  ;
    }
    UIGraphicsBeginImageContext(newSize);
    [source_image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage * newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}

// MARK: - 二分法
+ (NSData *)halfFuntion:(NSArray *)arr image:(UIImage *)image sourceData:(NSData *)finallImageData maxSize:(int)maxSize
{
    NSData * tempFinallImageData = finallImageData;
    NSData * tempData = [NSData new];
    int start = 0;
    int end = (int)arr.count-1;
    int index = 0;
    int difference = INT_MAX;
    while (start<=end) {
        index = start + (end - start)/2;
        tempFinallImageData = UIImageJPEGRepresentation(image, [arr[index] floatValue]);
        int sizeOrigin = (int)tempFinallImageData.length;
        int sizeOriginKB = sizeOrigin / 1024;
        NSLog(@"sizeOriginKB====%i",sizeOriginKB);
        if (sizeOriginKB > maxSize) {
            start = index + 1;
        } else if (sizeOriginKB < maxSize) {
            if (maxSize-sizeOriginKB < difference){
                difference = maxSize-sizeOriginKB;
                tempData = tempFinallImageData;
            }
            end = index - 1;
        } else {
            break;
        }
    }
    return  tempData;
}


@end
