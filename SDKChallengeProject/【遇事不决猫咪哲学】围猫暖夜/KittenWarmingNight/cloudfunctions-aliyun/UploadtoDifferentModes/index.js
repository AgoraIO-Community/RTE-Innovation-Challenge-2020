'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const Mode=event.Mode;
	const fileID=event.fileID;
	const userID=event.userID;
	const index=event.index;
	const collection1 = db.collection('PublicData')
	const collection2 = db.collection(Mode)
	console.log('Getcollections success')
	let res = await collection1.doc("5f5604f3183a450001a93051").get()
	let Num;
	if (index==0){
		Num = res.data[0]["CodingMode-num"]
		const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
			"CodingMode-num": Num+1
		})
	}
	if (index==1){
		Num = res.data[0]["LonelyMode-num"]
		const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
			"LonelyMode-num": Num+1
		})
	}
	if (index==2){
		Num = res.data[0]["UpsetMode-num"]
		const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
			"UpsetMode-num": Num+1
		})
	}
	if (index==3){
		Num = res.data[0]["ConfuseMode-num"]
		const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
			"ConfuseMode-num": Num+1
		})
	}
	if (index==4){
		Num = res.data[0]["HardWorkingMode-num"]
		const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
			"HardWorkingMode-num": Num+1
		})
	}
	const ans = await collection2.add({
		"src": fileID,
		"upload-user-id": userID,
		"mp3-id": Num+1
	})
	return{
		code: 200,
		msg: "存储MP3的链接成功",
		data: ans
	}
};
