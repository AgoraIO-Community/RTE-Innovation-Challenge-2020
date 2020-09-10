'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('uni-id-users')
	const openID = event.openID.toString()
	const res = await collection.where({
		"openID": openID
	}).get()
	if (!res.data[0]) {
	 	return {
	 		code: 300,
	 		msg: "没有存储过该用户信息",
	 	}
	}
	else {
	 	return {
	 		code:200,
	 		msg: "已存在该用户信息"
	 	}
	}
};
