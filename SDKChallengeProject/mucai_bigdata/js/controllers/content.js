/// <reference path="../../libs/agora-rtm-sdk.d.ts" />
var vm = new Vue({
    el: '#app',
    data() {
        return {
            login_status: false,
            rtm_uid: "",
            rtm_client: null,
            rtm_channel: null,
            rtm_message: "",
            rtm_messages: []
        }
    },
    created() {
        var vm = this;
        const client = AgoraRTM.createInstance('98c6e628417f44aaaed07fa7e6a23a01');

        client.on('ConnectionStateChanged', (newState, reason) => {
            console.log('on connection state changed to ' + newState + ' reason: ' + reason);
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
                this.rtm_channel = this.rtm_client.createChannel('1');
                this.rtm_channel.join().then(() => {
                    /* 加入频道成功的处理逻辑 */
                    console.log('加入频道1')

                }).catch(error => {
                    /* 加入频道失败的处理逻辑 */

                });
                this.rtm_channel.on('ChannelMessage', ({ text }, senderId) => { // text 为收到的频道消息文本，senderId 为发送方的 User ID
                    /* 收到频道消息的处理逻辑 */
                    this.addMsgLog(senderId, text, timestamp = new Date().getTime());
                });
            }).catch(err => {
                console.log('AgoraRTM client login failure', err);
            });
        },
        sendMsg() {
            this.rtm_channel.sendMessage({ text: this.rtm_message }).then(() => {
                /* 频道消息发送成功的处理逻辑 */
                console.log(`发送成功${this.rtm_message}`)
                this.addMsgLog(this.rtm_uid, this.rtm_message, timestamp = new Date().getTime());
                this.rtm_message = "";
            }).catch(error => {
                /* 频道消息发送失败的处理逻辑 */
                this.rtm_message = "";
                console.log(error)
            });

        },
        addMsgLog(senderId, text, timestamp = new Date().getTime()) {
            let message = { senderId: senderId, text: text, timestamp: timestamp }
            this.rtm_messages.push(message);
            console.log(this.rtm_messages)
        }

    },
    watch: {

    },
    destroyed() {
        this.rtm_channel.leave();
        this.rtm_client.logout();
    }
});