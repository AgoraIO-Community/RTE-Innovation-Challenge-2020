// 填入 appID
const APPID = "4235bf0f9bdf414da06eb627f34af208";

if(APPID === ""){
  wx.showToast({
    title: `请在config.js中提供正确的appid`,
    icon: 'none',
    duration: 5000
  });
}

module.exports = {
  APPID: APPID
}