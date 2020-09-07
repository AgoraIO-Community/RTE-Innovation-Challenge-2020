import Vue from 'vue'
import Toast from './toast.vue'

Toast.instalToast = function(options) {
  if (options === undefined || options === null) {
    options = {
      message: ''
    }
  } else if (typeof options === 'string' || typeof options === 'number') {
    options = {
      message: options
    }
  }
  var ToastObj = Vue.extend(Toast)

  var component = new ToastObj({
    data: options
  }).$mount()
  document.querySelector('body').appendChild(component.$el)
}

export default Toast
