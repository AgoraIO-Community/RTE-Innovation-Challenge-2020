'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('Feedbacks')
	//console.log('event : ', event)
	const res = await collection.add({
		"userID": event.id,
		"content": event.content,
		"feedbackImages": event.feedbackImages
	})
	return{
		code: 200,
		msg: "反馈成功",
		data: res
	}
};
