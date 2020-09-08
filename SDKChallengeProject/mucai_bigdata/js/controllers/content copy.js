/// <reference path="../../libs/agora-rtm-sdk.d.ts" />
var vm = new Vue({
    el: '#app',
    data() {
        return {

            login_status: false,
            rtm_uid: "",
            rtm_client: null,
            rtm_message: "",
            navList: [],
            tabContents: [],
            num: 0
        }
    },
    created() {
        var vm = this;
        const client = AgoraRTM.createInstance('98c6e628417f44aaaed07fa7e6a23a01');

        client.on('ConnectionStateChanged', (newState, reason) => {
            console.log('on connection state changed to ' + newState + ' reason: ' + reason);
        });
        client.on('MessageFromPeer', ({ text }, peerId) => { // text 为消息文本，peerId 是消息发送方 User ID
            /* 收到点对点消息的处理逻辑 */
            console.log(peerId, text)
            if (this.navList.includes(peerId)) {
                this.tabContents.map(v => {
                    if (v.rtm_uid == peerId) v.content.push(text);
                })
            } else {
                this.navList.push(peerId);
                let data = { rtm_uid: peerId, content: [] }
                data.content.push(text);
                this.tabContents.push(data);
            }

            console.log(this.tabContents)
            console.log(this.navList)
        });
        this.rtm_client = client;
    },
    mounted() {
        var vm = this;
        console.log(AgoraRTM)

    },
    methods: {
        login() {
            this.rtm_client.login({ token: '', uid: this.rtm_uid }).then(() => {
                console.log('AgoraRTM client login success');
                this.login_status = true;
            }).catch(err => {
                console.log('AgoraRTM client login failure', err);
            });
        },
        sendMsg() {
            this.rtm_client.sendMessageToPeer({ text: this.rtm_message }, // 符合 RtmMessage 接口的参数对象
                'kefu', // 远端用户 ID
            ).then(sendResult => {
                this.rtm_message = "";
                if (sendResult.hasPeerReceived) {
                    console.log(sendResult)
                        /* 远端用户收到消息的处理逻辑 */
                } else {
                    /* 服务器已接收、但远端用户不可达的处理逻辑 */
                    console.log(sendResult)
                }
            }).catch(error => {
                /* 发送失败的处理逻辑 */
                console.log(error)
                this.rtm_message = "";
            });


        },
        tab(index) {
            this.num = index
        }

    },
    watch: {

    },
    destroyed() {
        client.logout();
    }
});