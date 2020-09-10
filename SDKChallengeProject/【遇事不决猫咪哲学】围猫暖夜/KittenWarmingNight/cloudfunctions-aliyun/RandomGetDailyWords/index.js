'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection1 = db.collection('PublicData')
	const collection2 = db.collection('LotteryWords')
	let res = await collection1.doc("5f5604f3183a450001a93051").get()
	const wordsNum = res.data[0]["num-of-words"]
	const chosenWordID = Math.floor(Math.random()*(wordsNum-1+1)+1)
	const ans = await collection2.where({
		"words-id": chosenWordID
	}).get()
	return {
		code: 200,
		data: ans.data[0],
		msg: "随机抽取信息成功"	
	}
};
