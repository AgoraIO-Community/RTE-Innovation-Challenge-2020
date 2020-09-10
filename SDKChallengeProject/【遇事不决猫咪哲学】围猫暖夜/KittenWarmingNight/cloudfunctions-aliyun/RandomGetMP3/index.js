'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const Mode=event.Mode;
	const index = event.index;
	const collection1 = db.collection('PublicData')
	const collection2 = db.collection(Mode)
	let res = await collection1.doc("5f5604f3183a450001a93051").get()
	let Mp3Num;
	if (index==0){
		Mp3Num = res.data[0]["CodingMode-num"]
	}
	if (index==1){
		Mp3Num = res.data[0]["LonelyMode-num"]
	}
	if (index==2){
		Mp3Num = res.data[0]["ConfuseMode-num"]
	}
	if (index==3){
		Mp3Num = res.data[0]["UpsetMode-num"]
	}
	if (index==4){
		Mp3Num = res.data[0]["HardWorkingMode-num"]
	}
	const chosenMp3ID = Math.floor(Math.random()*(Mp3Num-1+1)+1)
	console.log(chosenMp3ID)
	const ans = await collection2.where({
		"mp3-id": chosenMp3ID
	}).get()
	return {
		data:ans.data
	}
	return {
		code: 200,
		data: ans.data[0],
		msg: "随机抽取MP3成功"	
	}
};
