/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-15 10:34:36
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-10 23:43:53
 */
/**
 * 用户手机号验证码登录
 * @param {Object} AV 第三方库
 * @param {string} phoneNumber 手机号
 * @param {string} code  验证码
 */
export function login(AV, phoneNumber, code) {
  return new Promise((resolve, reject) => {
    // 用户登录接口 login
    AV.User.signUpOrlogInWithMobilePhone(phoneNumber, code).then(
      function(success) {
        resolve(success.toJSON())
      },
      function(error) {
        reject(error)
      }
    )
  })
}
