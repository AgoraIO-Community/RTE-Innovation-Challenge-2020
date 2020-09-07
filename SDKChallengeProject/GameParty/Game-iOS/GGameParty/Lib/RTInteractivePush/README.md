# RTInteractivePush

[![CI Status](http://img.shields.io/travis/rickytan/RTInteractivePush.svg?style=flat)](https://travis-ci.org/rickytan/RTInteractivePush)
[![Version](https://img.shields.io/cocoapods/v/RTInteractivePush.svg?style=flat)](http://cocoapods.org/pods/RTInteractivePush)
[![License](https://img.shields.io/cocoapods/l/RTInteractivePush.svg?style=flat)](http://cocoapods.org/pods/RTInteractivePush)
[![Platform](https://img.shields.io/cocoapods/p/RTInteractivePush.svg?style=flat)](http://cocoapods.org/pods/RTInteractivePush)

Enjoy the interactive poping feature of `UINavigationController`? But where is the missing interactive pushing? Here it is.

## Example

![ScreenCap](https://user-images.githubusercontent.com/1250207/27914013-7a103aba-6294-11e7-949d-a3fb340e86ce.gif)

```objective-c
- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.rt_enableInteractivePush = YES;
    ...
}

- (nullable __kindof UIViewController *)rt_nextSiblingController
{
    return [[YourNewViewController alloc] init];
}
```

## Requirements
- iOS 7 +
- Xcode 8 +

## Installation

InteractivePush is available through [CocoaPods](http://cocoapods.org). To install
it, simply add the following line to your Podfile:

```ruby
pod "RTInteractivePush"
```

## Author

Ricky Tan, ricky.tan.xin@gmail.com

## License

InteractivePush is available under the MIT license. See the LICENSE file for more info.
