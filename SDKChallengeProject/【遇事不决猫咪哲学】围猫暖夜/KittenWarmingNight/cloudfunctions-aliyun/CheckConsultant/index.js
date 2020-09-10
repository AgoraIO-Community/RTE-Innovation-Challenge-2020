'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('uni-id-users')
	const id = event.id.toString()
	const res = await collection.where({
		openID: id
	}).get()
	if (!res.data[0]["WhetherConsultant"]){
		return {
			code: 300,
			msg: "不是咨询师"
		}
	}else{
		return {
			code: 200,
			msg: "是咨询师"
		}
	}
};
