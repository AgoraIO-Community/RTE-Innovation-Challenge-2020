# Emoji Battle
<p align="center">
  <img width="100px" height="100px" src="https://raw.githubusercontent.com/underwindfall/blogAssets/master/match/icon.png">
</p>

## 项目展示

| ![](https://raw.githubusercontent.com/underwindfall/blogAssets/master/match/%E6%88%AA%E5%B1%8F2020-09-03%2000.53.33.png) | ![](https://raw.githubusercontent.com/underwindfall/blogAssets/master/match/%E6%88%AA%E5%B1%8F2020-09-03%2001.33.44.png) | ![](https://raw.githubusercontent.com/underwindfall/blogAssets/master/match/%E6%88%AA%E5%B1%8F2020-09-03%2000.56.46.png) | ![](https://raw.githubusercontent.com/underwindfall/blogAssets/master/match/%E6%88%AA%E5%B1%8F2020-09-03%2000.56.12.png) |
| :-------------------: | :------------------: | :-----------------: | :------------------: |
|         首页          |        创建/加入房间        |        比赛中         |       比赛结果       |

## Live Video
[Live video](https://github.com/underwindfall/blogAssets/raw/master/match/1599088412675314.mp4)
  
## 什么是Emoji Battle
声网2020秋季开发者大赛参赛作品, 这是一款基于`RTC 视频开发`和`机器学习`人脸表情的娱乐性项目。

## 如何体验
- 需要在`string-config.xml`下填写正确的`appId` 与 `token`

```xml
<string name="appId"></string>
<string name="token"></string>
```

## APK
[APK](app-debug.apk)

## 玩法

### 创建/加入房间
通过点击首页的开始游戏进入创建房间界面，当房间内人数有两人便可进行**battle**.

### 比赛
当房间内开始比赛时，每隔一段时间，系统会提出一个表情，玩家需尽力模仿这个表情进行比赛。当比赛结束时，系统会统计玩家各自模仿出多少个表情从而决出胜负。


## 技术栈
| SDK | 描述|
|---|---|
|声网SDK|实现视频通话|
|Goolge-ML-FACE|实现人脸表情识别|
|Firebase Firestore |实现数据的即时交换|


## 团队
团队名：**invictus void**

| 成员 | 分工 | 联系方式 |
|---|---|--- |
| underwindfall | 负责项目开发 | yangqifan02@gmail.com  |