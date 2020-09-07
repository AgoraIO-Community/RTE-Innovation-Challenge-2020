var map = null
var tmpDown // 导出的二进制对象

const App = {
  searchNum: 1,
  pageNum: 1,
  local: null,
  searchResult: [],
  result: null,
  queryParams: {},
  excelNo: 1,
  searchListIndex: 0,
  queryList: [],
  init() {
    this.initLearnCloud()
    // this.initMap()
    this.initQueryParams()
    this.eventBind()
    this.AMapSearch()
  },
  initQueryParams() {
    const areas = getParameterByName('area') ? getParameterByName('area').split(',') : []
    const keywords = getParameterByName('keywords').split(',')
    let city = getParameterByName('city')
    if (city.substring(city.length - 1, city.length) !== '市' && city.substring(city.length - 1, city.length) !== '区') {
      city = city + '市'
    }
    this.queryParams = {
      keywords: keywords,
      city: city,
      areas: areas
    }
    for (let i = 0; i < areas.length; i++) {
      const area = city + areas[i]
      const item = {
        keywords: keywords,
        location: area
      }
      this.queryList.push(item)
    }
    // this.search()
  },
  initMap() {
    // 百度地图API功能
    map = new BMap.Map('mapContainer')

    map.centerAndZoom(new BMap.Point(116.404, 39.915), 11)
  },
  eventBind() {
    const submitBtn = document.getElementById('J_submit')
    submitBtn.onclick = (e) => {
      const btnText = e.target.innerText
      if (btnText.indexOf('取消采集') > -1) {
        wx.miniProgram.navigateTo({ url: `/pages/index/index` })
      } else {
        this.downloadExl()
      }
    }
  },
  initLearnCloud() {
    const APP_ID = 'PnYXLEcY3p4igmG1XY7jacKa-gzGzoHsz'
    const APP_KEY = 'CCnEj86sW6l4z3qqBYz2TdJ8'
    AV.init({
      appId: APP_ID,
      appKey: APP_KEY
    })
  },
  // 页面展示
  previewData(number) {
    const content = document.getElementById('J_content')
    if (this.searchNum > 8) {
      content.firstElementChild.remove()
    }
    const html = content.innerHTML +
                  `
                  <div class="content-item flex-col">
                    <span>正在进行第${this.searchNum}次采集</span>
                    <span>第${this.searchNum}次采集到${number}条数据</span>
                  </div>
                  `

    content.innerHTML = html

    this.searchNum++
    this.pageNum++
  },
  // 根据参数搜索
  search() {
    const searchInfo = this.queryList[this.searchListIndex]
    var bdary = new BMap.Boundary()
    bdary.get('合肥市蜀山区', function(rs) { // 获取行政区域
      var count = rs.boundaries.length // 行政区域的点有多少个
      if (count === 0) {
        return
      }
      // console.log(count)
    })
    this.localSearch = new BMap.LocalSearch(map, {
      renderOptions: { map: map },
      onSearchComplete: (result) => {
        // console.log(result)
        this.searchCallback()
      }
    })
    this.localSearch.setLocation('合肥市')
    this.localSearch.setPageCapacity(50)

    this.localSearch.search(searchInfo.keywords)
    // this.localSearch.gotoPage(this.searchNum)
  },
  // 百度API search回调
  searchCallback() {
    const result = this.localSearch.getResults() || []
    // console.log(result[0].getNumPois())
    let num = 0
    let maxPage = 1
    for (let i = 0; i < result.length; i++) {
      const element = result[i]
      maxPage = maxPage > element.getNumPages() ? maxPage : element.getNumPages()
      const dataResult = this.formatCollectionData(element.Ar)
      this.searchResult = [...this.searchResult, ...dataResult]
      num = num + dataResult.length
    }
    // console.log(this.searchResult)
    this.previewData(num)
    document.getElementById('J_submit').innerText = '正在跳转...'
    if (this.pageNum <= maxPage) {
      setTimeout(() => {
        this.localSearch.gotoPage(this.pageNum)
      }, 600)
    } else if (this.pageNum > maxPage && this.searchListIndex + 1 < this.queryList.length) {
      this.searchListIndex++
      this.pageNum = 1
      this.search()
    } else if (this.searchListIndex >= this.queryList.length) {
      this.downloadExl()
    }
  },
  getPoint() {
    const queryParams = this.queryParams

    if (queryParams.area !== '') {
      for (let i = 0; i < queryParams.area.length; i++) {
        const temp = queryParams.city + queryParams.area[i]
        var myGeo = new BMap.Geocoder()
        // 将地址解析结果显示在地图上，并调整地图视野
        myGeo.getPoint(temp, (point) => {
          // console.log(point)
        },
        queryParams.city)
      }
    }
    // 创建地址解析器实例
  },
  // 查询参数处理
  initAMapParams() {
    const { keywords, areas, city } = this.queryParams
    const queryList = []
    for (let i = 0; i < keywords.length; i++) {
      if (areas.length > 0) {
        for (let j = 0; j < areas.length; j++) {
          queryList.push({
            keyword: keywords[i],
            city: areas[j]
          })
        }
      } else {
        queryList.push({
          keyword: keywords[i],
          city: city
        })
      }
    }

    return queryList
  },
  // 高德地图查询
  AMapSearch() {
    const queryList = this.initAMapParams()
    const len = queryList.length
    let queryIndex = 1
    let city = queryList[0].city
    let keyword = queryList[0].keyword

    let placeSearch = null

    const search = () => {
      placeSearch.search(keyword, (status, result) => {
        if (status === 'no_data' && queryIndex !== len) {
          return
        } else if (status === 'no_data' && queryIndex === len) {
          this.downloadExl()
          return
        }
        const { pageIndex, pois, count, pageSize } = result.poiList
        // 页面预览数据采集进度
        const dataResult = this.formatCollectionDataAMap(pois, city)
        this.searchResult = [...this.searchResult, ...dataResult]
        this.previewData(dataResult.length)

        // 是否为最后一页数据
        if (pageIndex < Math.ceil(count / pageSize)) {
          placeSearch.setPageIndex(pageIndex + 1)
          setTimeout(() => {
            search()
          }, 500)
        } else {
          // 进行下一次查询
          if (queryIndex < len) {
            city = queryList[queryIndex].city
            placeSearch.setCity(city)
            placeSearch.setPageIndex(1)
            keyword = queryList[queryIndex].keyword
            queryIndex++
            setTimeout(() => {
              search()
            }, 500)
          } else {
            // 全部关键词 区域搜索完毕
            setTimeout(() => {
              this.downloadExl()
            }, 500)
          }
        }
      })
    }

    AMap.service(['AMap.PlaceSearch'], function() {
      placeSearch = new AMap.PlaceSearch({
        city: city
      })
      placeSearch.setPageSize(50)
      search()
    })
  },
  // 格式化结果数据
  formatCollectionData(data, city) {
    const json = []
    for (let i = 0; i < data.length; i++) {
      const element = data[i]
      const jsonItem = {
        '序号': this.excelNo,
        '城市': element.city,
        '名称': element.title,
        '联系电话': element.phoneNumber
      }
      json.push(jsonItem)
      this.excelNo++
    }
    return json
  },
  // 格式化结果数据
  formatCollectionDataAMap(data, city) {
    const json = []
    for (let i = 0; i < data.length; i++) {
      const element = data[i]
      const jsonItem = {
        '序号': this.excelNo,
        '城市': city,
        '名称': element.name,
        '联系电话': element.tel
      }
      json.push(jsonItem)
      this.excelNo++
    }
    // console.log(json)

    return json
  },
  // 生成excel文件保存到 leanCould 中
  downloadExl() {
    console.log('>>>>download')
    const json = this.searchResult
    var tmpdata = json[0]
    json.unshift({})
    var keyMap = [] // 获取keys
    // keyMap =Object.keys(json[0]);
    for (var k in tmpdata) {
      keyMap.push(k)
      json[0][k] = k
    }
    var tmpdata = []// 用来保存转换好的json
    json.map((v, i) => keyMap.map((k, j) => Object.assign({}, {
      v: v[k],
      position: (j > 25 ? getCharCol(j) : String.fromCharCode(65 + j)) + (i + 1)
    }))).reduce((prev, next) => prev.concat(next)).forEach((v, i) => tmpdata[v.position] = {
      v: v.v
    })
    var outputPos = Object.keys(tmpdata) // 设置区域,比如表格从A1到D10
    var tmpWB = {
      SheetNames: ['mySheet'], // 保存的表标题
      Sheets: {
        'mySheet': Object.assign({},
          tmpdata, // 内容
          {
            '!ref': outputPos[0] + ':' + outputPos[outputPos.length - 1] // 设置填充区域
          })
      }
    }
    const type = 'xlsx'
    tmpDown = new Blob([s2ab(XLSX.write(tmpWB,
      { bookType: (type == undefined ? 'xlsx' : type), bookSST: false, type: 'binary' }// 这里的数据是用来定义导出的格式类型
    ))], {
      type: ''
    })
    // 创建二进制对象写入转换好的字节流
    var byteArrayFile = new AV.File('数据采集.xlsx', tmpDown)

    byteArrayFile.save().then((res) => {
      const url = res.attributes.url
      wx.miniProgram.navigateBack({ delta: 1 })
      // wx.miniProgram.navigateTo({ url: `/pages/dataList/dataList?url=${url}&webview=${false}` })
      wx.miniProgram.postMessage({
        data: {
          url,
          length: json.length,
          json: json.splice(0, 50)
        }
      })
    })
  }

}

window.onload = function() {
  App.init()
}

function s2ab(s) { // 字符串转字符流
  var buf = new ArrayBuffer(s.length)
  var div = new Uint8Array(buf)
  for (var i = 0; i != s.length; ++i) div[i] = s.charCodeAt(i) & 0xFF
  return buf
}
// 将指定的自然数转换为26进制表示。映射关系：[0-25] -> [A-Z]。
function getCharCol(n) {
  let s = ''

  let m = 0
  while (n > 0) {
    m = n % 26 + 1
    s = String.fromCharCode(m + 64) + s
    n = (n - m) / 26
  }
  return s
}
function getParameterByName(name, url) {
  if (!url) url = window.location.href
  name = name.replace(/[\[\]]/g, '\\$&')
  var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)')

  var results = regex.exec(url)
  if (!results) return ''
  if (!results[2]) return null
  return decodeURIComponent(results[2].replace(/\+/g, ' '))
}

