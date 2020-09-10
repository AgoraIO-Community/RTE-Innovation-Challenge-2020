export default {

  methods: {
    /**
    * @description: base64位图片转码文件流
    * @param {type}
    */

    base64toFile(dataurl, filename = 'file') {
      const arr = dataurl.split(',')

      const mime = arr[0].match(/:(.*?);/)[1]

      const suffix = mime.split('/')[1]

      const bstr = atob(arr[1])

      let n = bstr.length

      const u8arr = new Uint8Array(n)

      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }

      return new File([u8arr], `${filename}.${suffix}`, {

        type: mime

      })
    }
  }
}
