/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-15 10:34:41
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-08-30 15:08:57
 */
/**
 * 获取游戏列表
 * @param {Object} AV 第三方库
 * @param {string} phoneNumber 手机号
 * @param {string} code  验证码
 */
export function getGames(AV, phoneNumber, code) {
  return new Promise((resolve, reject) => {
    // 用户登录接口
    AV.User.logInWithMobilePhoneSmsCode(phoneNumber, code).then(
      function(success) {
        resolve(success.attributes)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
