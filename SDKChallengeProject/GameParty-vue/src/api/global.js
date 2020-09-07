/**
 * 查询表名为tbName的表中全部数据
 * @param {Object} AV 第三方库
 * @param {string} tbName 表名称
 */
import Query from '@/leanCloud/ggQuery'
export function getList(AV) {
  return new Promise((resolve, reject) => {
    const query = Query.TeamListQuery()
    query.find().then(
      function(results) {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve(results)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
