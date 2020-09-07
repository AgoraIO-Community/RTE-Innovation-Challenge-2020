// function watch(ctx, obj) {
//   Object.keys(obj).forEach(key => {
//     defineReactive(ctx.data, key, ctx.data[key], function(value) {
//       obj[key].call(ctx, value)
//     })
//   })
// }

// function computed(ctx, obj) {
//   let keys = Object.keys(obj)
//   let dataKeys = Object.keys(ctx.data)
//   dataKeys.forEach(dataKey => {
//     defineReactive(ctx.data, dataKey, ctx.data[dataKey])
//   })
//   let firstComputedObj = keys.reduce((prev, next) => {
//     ctx.data.$target = function() {
//       ctx.setData({ [next]: obj[next].call(ctx) })
//     }
//     prev[next] = obj[next].call(ctx)
//     ctx.data.$target = null
//     return prev
//   }, {})
//   ctx.setData(firstComputedObj)
// }

// function defineReactive(data, key, val, fn) {
//   let subs = data['$' + key] || []
//   Object.defineProperty(data, key, {
//     configurable: true,
//     enumerable: true,
//     get: function() {
//       if (data.$target) {
//         subs.push(data.$target)
//         data['$' + key] = subs
//       }
//       return val
//     },
//     set: function(newVal) {
//       if (newVal === val) return
//       fn && fn(newVal)
//       if (subs.length) {
//         // 用 setTimeout 因为此时 this.data 还没更新
//         setTimeout(() => {
//           subs.forEach(sub => sub())
//         }, 0)
//       }
//       val = newVal
//     },
//   })
// }

// module.exports = { watch, computed }





/**
 * 检测函数的变化
 * data 当前上下文的data，key 键名，val 键值，fn 回调函数
 */
function defineReactive(data, key, val, watchFn, computedFn) {
  let realWatchFn = data['watchFn'];
  let realComputedFn = data['computedFn'];
  Object.defineProperty(data, key, {
    configurable: true,
    enumerable: true,
    get: function () {
      if (watchFn) {
        realWatchFn = watchFn;
        data['watchFn'] = realWatchFn;
      }
      if (computedFn) {
        realComputedFn = computedFn;
        data['computedFn'] = realComputedFn;
      }
      return val
    },
    set: function (newVal) {
      if (newVal === val) return
      // 如果新值和老值不相同则返回回调函数 fn
      realWatchFn && realWatchFn(newVal, val, key);
      val = newVal;
      if (realComputedFn && realComputedFn.length) {
        // 执行 computed的更新设置值
        setTimeout(() => {
          realComputedFn.forEach(sub => sub());
        })
      }
    },
  })
}

// vue watch 方法 监听值的变化
function watch(ctx, obj) {
  // obj是watch监听的一个一个对象集合 
  Object.keys(obj).forEach(key => {
    // console.log(key);
    defineReactive(ctx.data, key, ctx.data[key], function (newVal, oldVal, realKey) {
      // obj[key] 对应监听值的回调函数,key值判断当前是否是需要watch的字段
      realKey == key && obj[key].call(ctx, newVal, oldVal);
    })
  })
}

// computed 函数
function computed(ctx, obj) {
  // console.log(ctx.data)
  let computedKeys = Object.keys(obj)//computed 对象集合
  let computedFn = [];//computedFn存储computed计算操作
  let computedObj = computedKeys.reduce((total, next) => {
    computedFn.push(function () {
      ctx.setData({ [next]: obj[next].call(ctx) })
    })
    total[next] = obj[next].call(ctx);
    return total
  }, {})
  // 首次加载先设置一次
  ctx.setData(computedObj)
  // 绑定数据变化时，动态computed
  let dataKeys = Object.keys(ctx.data)
  dataKeys.forEach(dataKey => {
    defineReactive(ctx.data, dataKey, ctx.data[dataKey], false, computedFn)
  })
}

// 对外抛出 watch、computed 方法
module.exports = { watch, computed }