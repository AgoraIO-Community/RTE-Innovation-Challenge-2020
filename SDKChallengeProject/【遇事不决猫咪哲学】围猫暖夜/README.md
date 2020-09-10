# 围猫暖夜

by遇事不决, 猫咪哲学

---

## 项目简介

目前市面上的线上心理类程序多聚焦于心理疾病患者, 且多为 APP 和网页, 并没有关注到普通人在一些特定的时刻如深夜时突然产生的情绪和随开随用的需求.
<center><img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg?raw=true" alt = "示例图片1" width="60%"></center>


由此, 我们队通过 uniapp 开发了围猫暖夜这款微信小程序. 围猫暖夜涵盖了陌生人互相匹配的多人语音聊天、和专业咨询师沟通的私语空间、每日一签和语音漂流瓶等社交功能, 以声音作为交互的主要媒介, 页面色彩明丽, 插画生动可爱, 以猫咪为主题, 让使用者感到放松和舒适.

<img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/page1.jpg?raw=true" alt = "示例图片1" width="32%">
<img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/page2.jpg?raw=true" alt = "示例图片2" width="32%">
<img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/share.jpg?raw=true" alt = "示例图片3" width="32%">

另外还有彩蛋功能！为正在写代码的秃头（bushi）程序员提供安慰和鼓励！

## Demo 展示
[Demo 跳转](https://jbox.sjtu.edu.cn/l/r1KU5s)

## Web 端多人语音聊天——私语空间(H5)

受限于微信个人小程序的权限约束, 无法直接在小程序端进行开发或通过 web-view 进行直接跳转, 因此最终将私语空间功能发布在 Web 端. 小程序端生成房间号和超链接后会自动将内容复制到剪贴版, 便于用户通过外部浏览器进行快速访问. [私语空间跳转](https://wangshanyw.cn/KittenWarmingNight/SecretTalkingGarden/index.html)

<center><img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/html.png?raw=true" alt = "示例图片4" width="30%"></center>

## 配置
- `npm install`
- Agora AppID `/ChattingRoomWeb/pages/index/index.vue :51`
- yesapi 的 php 代理网址 `/KittenWarmingNight/lib/yes3.js:17`

## 项目使用的技术

- 多人语音聊天功能：Agora Web SDK(H5 端)
- 使用 UniCloud 和云函数进行较多的功能封装
- 基于 Uview-UI, 提供了很棒的用户交互界面

## 微信小程序二维码

9月10号已提交审核, 目前正在审核.
<img src="https://github.com/xjywq/picgo/blob/master/%E5%9B%B4%E7%8C%AB%E6%9A%96%E5%A4%9C/Code.jpg?raw=true" alt = "示例图片1" width="30%">

## 项目人员组成及负责工作

|  姓名  |           负责模块           |
| :----: | :--------------------------: |
| 张若涵 | 前端, 后端, 服务器, 文档撰写 |
| 丁健宇 |   前端, 外观实现, 文档撰写   |
