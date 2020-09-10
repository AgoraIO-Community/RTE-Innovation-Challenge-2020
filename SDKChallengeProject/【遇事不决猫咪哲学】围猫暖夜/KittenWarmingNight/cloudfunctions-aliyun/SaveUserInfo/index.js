'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const openID = event.openID.toString()
	const collection = db.collection('uni-id-users')
	// console.log('event : ', event)
	const res = await collection.add({
		openID: openID,
		WhetherConsultant: 0
	})
	//返回数据给客户端
	return {
		code: 200,
		msg: "第一次存储用户信息成功",
		data: res
	}
};
