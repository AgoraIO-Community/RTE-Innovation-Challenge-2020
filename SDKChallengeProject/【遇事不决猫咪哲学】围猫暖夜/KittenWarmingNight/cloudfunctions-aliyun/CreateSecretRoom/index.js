'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('PublicData')
	var len = 10;
	len = len || 32;
	var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
	var maxPos = $chars.length;
	var pwd = '';
	for (var i = 0; i < len; i++) {
	　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
	}
	const res = await collection.doc("5f5604f3183a450001a93051").update({
		"Applied-room": pwd
	})
	const res1 = await collection.doc("5f5604f3183a450001a93051").get()
	console.log(JSON.stringify(res));
	return {
		code: 200,
		msg: "创建房间成功",
		data: res1.data[0]["Applied-room"]
	}
};
