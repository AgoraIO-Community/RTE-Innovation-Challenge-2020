'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection1 = db.collection('PublicData')
	const collection2 = db.collection('LotteryWords')
	let res = await collection1.doc("5f5604f3183a450001a93051").get()
	const wordsNum = res.data[0]["num-of-words"]
	let splitList = []
	let content = event.content
	const reg = /[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/;
	for (var i=0 ; i<content.length;i+=7){
	    if (reg.test(content[i+7])) {
	        splitList.push(content.substr(i,8))
	        i += 1
	    }
	    else {
	        splitList.push(content.substr(i,7))
	    }
	}
	const res2 = await collection1.doc("5f5604f3183a450001a93051").update({
		"num-of-words": wordsNum+1
	})
	const ans = await collection2.add({//
		"content": content,
		"upload-user-id": event.userID,
		"words-id": wordsNum+1,
		"splitList": splitList
	})
	console.log(event.userID)
	return {
		code: 200,
		msg: "上传签文成功",
		data: ans
	}
};
