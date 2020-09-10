'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('PublicData')
	const res = await collection.doc("5f5604f3183a450001a93051").get()
	const res1 = await collection.doc("5f5604f3183a450001a93051").update({
		"current-anonymous-room": ''
	})
	console.log(JSON.stringify(res1));
	return {
		code: 200,
		msg: "获取房间号成功",
		data: res.data[0]["current-anonymous-room"]
	}
};
