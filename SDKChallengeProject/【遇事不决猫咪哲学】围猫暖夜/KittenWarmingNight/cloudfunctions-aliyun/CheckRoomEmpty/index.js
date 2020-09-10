'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('PublicData')
	const res = await collection.doc("5f5604f3183a450001a93051").get()
	if (!res.data[0]["current-anonymous-room"]){
		return {
			code:300,
			msg: "目前房间为空"
		}
	}else {
		return {
			code: 400,
			msg: "目前房间有人"
		}
	}
};
