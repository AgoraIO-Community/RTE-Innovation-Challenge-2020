import HxToast from './toast/index.js'

const install = function(Vue) {
  Vue.component(HxToast.name, HxToast)

  Vue.prototype.$hx_toast = HxToast.instalToast
}
export default install
