const axios = require("axios");

// url: "https://ap-web-1.agora.io/api/v1"
async function _requestOneAP(url, appId, cname, uid = null, serviceOptions = {serviceType: 0, packetUri: 44, flag: 4}){
    var start = Date.now();
    const body = JSON.stringify({
        flag: serviceOptions.flag,
        ts: start,
        key: "<agora access key>",
        cname: cname || "abcabc",
        uid: uid || 0,
        opid: 133,
        detail: {
            5: `{"vocs_ip":["1.2.3.4"],"vos_ip":["5.6.7.8"]}`
        }
    });
    const options ={
        url: url,
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Packet-Service-Type': serviceOptions.serviceType,
            'X-Packet-URI': serviceOptions.packetUri
        },
        data: body
    };
    let obj;
    try {
        const resp = await axios(options);
        obj = typeof resp.data === "object" ? resp.data : JSON.parse(resp.data);
    }catch(e) {
        console.error(url, body);
        throw e;
    }

    if (obj && obj.res){
        if (obj.res.code === 0 && obj.res.addresses && obj.res.addresses.length){
            return {
                url: url,
                data: obj,
                elapse: Date.now() - start
            };
        }else{
            throw {
                url: url,
                message: "ERROR_" + obj.res.code,
                data: obj,
                elapse: Date.now() - start
            };
        }
    }else{
        throw {
            url: url,
            message: "MALFORMED",
            data: obj,
            elapse: Date.now() - start
        };
    }
}

function _getOneWorkingGateway_Parallel(appId, cname, uid){
    var apAddresses = [
        "https://ap-web-1.agora.io/api/v1",
        "https://ap-web-2.agoraio.cn/api/v1",
        "https://ap-web-3.agora.io/api/v1",
        "https://ap-web-4.agoraio.cn/api/v1",
    ];
    return new Promise((resolve, reject)=>{
        var failedResp = [];
        var isResolved = false;
        var wsClients = [];
        function handleWsOpen(wsUrl, apRes, start){
            var ws = this;
            wsClients.forEach((client)=>{
                if (client !== ws){
                    client.onerror = function(err){};
                    client.close();
                }
            });
            var data = {
                ws: ws,
                wsElapse: Date.now() - start,
                elapse: (Date.now() - start + apRes.elapse),
                apRes: apRes
            };
            isResolved = true;
            resolve(data);
        }
        function handleApResponseSuccess(data){
            if (!isResolved){
                data.data.res.addresses.forEach((addressObj, index)=>{
                    let wsUrl = `wss://webrtc-${addressObj.ip.replace(/\./g, "-")}.${index % 2 ? "agora.io" : "agoraio.cn"}:${addressObj.port}`;
                    let wsClient = new WebSocket(wsUrl);
                    wsClient.ip = addressObj.ip;
                    wsClient.port = addressObj.port;
                    wsClient.addEventListener("open", handleWsOpen.bind(wsClient, wsUrl, data, Date.now()));
                    wsClients.push(wsClient);
                });
            }
        }
        function handleApResponseError(resp){
            failedResp.push(resp);
            if (failedResp.length === apAddresses.length){
                reject(failedResp);
            }
        }
        apAddresses.forEach((apAddress)=>{
            const promise = _requestOneAP(apAddress, appId, cname, uid);
            promise.then(handleApResponseSuccess);
            promise.catch(handleApResponseError);
        });
    });
}

async function _getOneWorkingGateway_Serial(appId, cname, uid){
    var apAddresses = [
        "https://ap-web-1.agora.io/api/v1",
        "https://ap-web-2.agoraio.cn/api/v1",
        "https://ap-web-3.agora.io/api/v1",
        "https://ap-web-4.agoraio.cn/api/v1",
    ];
    let errors = [];
    let index = 0;
    let start = Date.now();
    for (let i = 0; i < apAddresses.length; i++){
        try{
            const apRes = await _requestOneAP(apAddresses[i], appId, cname, uid);
            for(let j = 0; j < apRes.data.res.addresses.length; j++){
                index++;
                let addressObj = apRes.data.res.addresses[j];
                let wsUrl = `wss://webrtc-${addressObj.ip.replace(/\./g, "-")}.${index % 2 ? "agora.io" : "agoraio.cn"}:${addressObj.port}`;
                try{
                    const wsConnectResult = await new Promise((resolve, reject)=>{
                        let isResolved = false;
                        const start = Date.now();
                        let wsClient = new WebSocket(wsUrl);
                        wsClient.ip = addressObj.ip;
                        wsClient.port = addressObj.port;
                        wsClient.addEventListener("open", ()=>{
                            if (!isResolved){
                                isResolved = true;
                                var data = {
                                    ws: wsClient,
                                    wsElapse: Date.now() - start,
                                    apRes: apRes
                                };
                                resolve(data);
                            }
                        });
                        wsClient.addEventListener("error", (err)=>{
                            if (!isResolved){
                                isResolved = true;
                                reject({
                                    err: err,
                                    wsUrl: wsUrl,
                                    wsElapse: Date.now() - start,
                                });
                            }
                        });
                    });
                    wsConnectResult.elapse = Date.now() - start;
                    return wsConnectResult;
                }catch(e){
                    errors.push(e);
                }
            }
        }catch(e){
            errors.push(e);
        }
    }
    throw errors;
}

const getOneWorkingGateway = _getOneWorkingGateway_Serial;

module.exports = {
    _requestOneAP,
    _getOneWorkingGateway_Parallel,
    _getOneWorkingGateway_Serial,
    getOneWorkingGateway,
};
