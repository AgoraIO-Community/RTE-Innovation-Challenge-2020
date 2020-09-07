var msgList = document.getElementById("msg_list");
var boxMsgText = document.getElementById("msg_text");
const client = AgoraRTM.createInstance('98c6e628417f44aaaed07fa7e6a23a01');
client.on('ConnectionStateChanged', (newState, reason) => {
    console.log('on connection state changed to ' + newState + ' reason: ' + reason);
});
client.on('MessageFromPeer', ({ text }, peerId) => { // text 为消息文本，peerId 是消息发送方 User ID
    /* 收到点对点消息的处理逻辑 */
    console.log(peerId, text)
    var html = '<li>' +
        '<div class="im-chat-user">' +
        '<img src="/static/images/kefu.jpg"/>' +
        '<cite>客服' + peerId + '</cite>' +
        '</div>' +
        '<div class="im-chat-text">' + text + '</div>' +
        '</li>';

    //追加内容
    msgList.insertAdjacentHTML('beforeEnd', html);
});

function createRandomId() {
    return (Math.random() * 10000000).toString(16).substr(0, 4) + '-' + (new Date()).getTime() + '-' + Math.random().toString().substr(2, 5);
}



var uid = createRandomId();

client.login({ token: '', uid: uid }).then(() => {
    console.log('AgoraRTM client login success');

}).catch(err => {
    console.log('AgoraRTM client login failure', err);
});


mui.init();
(function($) {

    //初始化图片浏览插件
    var imageViewer = new $.ImageViewer('.msg-content-image', {
        dbl: false
    });

    var slider = document.getElementById('Gallery');
    var group = slider.querySelector('.mui-slider-group');
    var items = mui('.mui-slider-item', group);
    //克隆第一个节点
    var first = items[0].cloneNode(true);
    first.classList.add('mui-slider-item-duplicate');
    //克隆最后一个节点
    var last = items[items.length - 1].cloneNode(true);
    last.classList.add('mui-slider-item-duplicate');

})(mui);

$(function() {
    //输入框事件绑定
    $("#msg_text").on({
        "keydown": function(e) {
            var that = this;
            if (e.ctrlKey && e.keyCode == 13) {
                sendMsg(that.val(), 0);
            }
        },
        "input propertychange": function(e) {
            showSendBtn();
        },
        "focus": function() {
            if ($(".more-content").css('display') != 'none') {
                $(".more-content").hide();
                $("footer").css("bottom", "0");
                $(".im-chat-main").css("padding-bottom", "50px");
                document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
            }
        }
    });

    //语音按钮事件
    $(".footer-left i.chat-icon-voice").on("tap", function(e) {
        var than = $(this);

        $(".face-content").hide();
        $(".more-content").hide();
        $("footer").css("bottom", "0");
        $(".im-chat-main").css("padding-bottom", "50px");
        $(".footer-right i:first").attr("class", "mui-icon chat-icon chat-icon-face");
        if (than.hasClass("chat-icon-keyboard")) {
            $("#msg_text").show();
            $("footer").css("padding-right", "65px");
            $(".footer-right").css("width", "100px");
            $(".footer-right").css("right", "1px");
            $(".footer-center").css("padding-right", "25px");
            $(".footer-center").css("padding-left", "3px");
            $(".footer-right i:first").show();
            $(".footer-center .voice-btn").hide();
            than.attr("class", "mui-icon chat-icon chat-icon-voice");
            setTimeout(function() {
                $("#msg_text").focus();
            }, 10);
        } else {
            $("#msg_text").hide();
            $("footer").css("padding-right", "55px");
            $(".footer-right").css("width", "initial");
            $(".footer-right").css("right", "5px");
            $(".footer-center").css("padding-right", "0");
            $(".footer-center").css("padding-left", "10px");
            $(".footer-right i:first").hide();
            $(".footer-center .voice-btn").show();
            than.attr("class", "mui-icon chat-icon chat-icon-keyboard");
        }

        document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
    });

    //表情按钮事件
    $(".footer-right i.chat-icon-face").on("tap", function(e) {
        var than = $(this);
        var footer = $("footer");

        //隐藏语音输入
        if ($(".footer-left i:first").hasClass("chat-icon-keyboard")) {
            $(".footer-center .voice-btn").hide();
            $(".footer-left i:first").attr("class", "mui-icon chat-icon chat-icon-voice");
            $("#msg_text").show();
            $("footer").css("padding-right", "65px");
            $(".footer-right").css("width", "100px");
            $(".footer-right").css("right", "1px");
            $(".footer-center").css("padding-right", "25px");
            $(".footer-center").css("padding-left", "3px");
            $(".footer-right i:first").show();
        }

        if (than.hasClass("chat-icon-keyboard")) {
            $(".face-content").hide();
            than.attr("class", "mui-icon chat-icon chat-icon-face");
            footer.css("bottom", "0");
            $(".im-chat-main").css("padding-bottom", "50px");
            setTimeout(function() {
                $("#msg_text").focus();
            }, 10);
        } else {
            $(".more-content").hide();
            $(".face-content").show();
            than.attr("class", "mui-icon chat-icon chat-icon-keyboard");
            footer.css("bottom", $(".face-content").height());
            $(".im-chat-main").css("padding-bottom", $(".face-content").height() + 50);
        }

        document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
    });

    //绑定表情点击事件
    $(".face-content").find('.face-list>li').on('tap', function() {
        focusInsert(boxMsgText, 'face' + this.title + ' ');
        showSendBtn();
    });

    //更多按钮事件
    $(".footer-right i.chat-icon-add-more").on("tap", function(e) {
        var than = $(this);
        var footer = $("footer");

        //隐藏语音输入
        if ($(".footer-left i:first").hasClass("chat-icon-keyboard")) {
            $(".footer-center .voice-btn").hide();
            $(".footer-left i:first").attr("class", "mui-icon chat-icon chat-icon-voice");
            $("#msg_text").show();
            $("footer").css("padding-right", "65px");
            $(".footer-right").css("width", "100px");
            $(".footer-right").css("right", "1px");
            $(".footer-center").css("padding-right", "25px");
            $(".footer-center").css("padding-left", "3px");
            $(".footer-right i:first").show();
        }

        //为了美观把更多的高度设置成表情一样
        $(".more-content").height($(".face-content").height());

        //表情是展开的则隐藏
        if (than.prev().hasClass("chat-icon-keyboard")) {
            than.prev().attr("class", "mui-icon chat-icon chat-icon-face");
        }

        if ($(".more-content").css('display') != 'none') {
            $(".more-content").hide();
            footer.css("bottom", "0");
            $(".im-chat-main").css("padding-bottom", "50px");
        } else {
            $(".face-content").hide();
            $(".more-content").show();
            footer.css("bottom", $(".more-content").height());
            $(".im-chat-main").css("padding-bottom", $(".more-content").height() + 50);
        }

        document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
    });

    //录音按钮事件绑定
    $(".footer-center .voice-btn").on("tap", function() {
        mui.toast("该功能正在开发中，敬请期待");
    });

    //点击消息列表，关闭键盘
    $("#msg_list").on('tap', function(event) {
        if (!focus) {
            boxMsgText.blur();
        }
        //表情是展开的则隐藏
        if ($(".footer-right i:first").hasClass("chat-icon-keyboard")) {
            $(".footer-right i:first").attr("class", "mui-icon chat-icon chat-icon-face");
        }
        $(".face-content").hide();
        $(".more-content").hide();
        $("footer").css("bottom", "0");
        $(".im-chat-main").css("padding-bottom", "50px");

        document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
    });

    //发送按钮事件
    $("#sendMessage").on("tap", function() {

        var html = '<li class="im-chat-mine">' +
            '<div class="im-chat-user">' +
            '<img src="/static/images/guest.png"/>' +
            '<cite>guest</cite>' +
            '</div>' +
            '<div class="im-chat-text">' + imContent($("#msg_text").val() || '&nbsp;') + '</div>' +
            '</li>';

        //追加内容
        msgList.insertAdjacentHTML('beforeEnd', html);
        console.log(imContent($("#msg_text").val() || '&nbsp;'))
        client.sendMessageToPeer({ text: imContent($("#msg_text").val() || '&nbsp;') }, // 符合 RtmMessage 接口的参数对象
            'kefu', // 远端用户 ID
        ).then(sendResult => {
            if (sendResult.hasPeerReceived) {
                console.log(sendResult)
                    /* 远端用户收到消息的处理逻辑 */
            } else {
                /* 服务器已接收、但远端用户不可达的处理逻辑 */
                console.log(sendResult)
                var html = '<li>' +
                    '<div class="im-chat-user">' +
                    '<img src="/static/images/kefu.jpg"/>' +
                    '<cite>客服</cite>' +
                    '</div>' +
                    '<div class="im-chat-text">' + '-----客服不在线-----' + '</div>' +
                    '</li>';

                //追加内容
                msgList.insertAdjacentHTML('beforeEnd', html);

            }
        }).catch(error => {
            /* 发送失败的处理逻辑 */

            console.log(error)
        });


        //清空
        $("#msg_text").val('')
        showSendBtn();

        //滚动条到最底部
        document.getElementById("msg_list").scrollTop = msgList.scrollHeight + msgList.offsetHeight;
    });
});

//显示或隐藏发送按钮
function showSendBtn() {
    //处理是否显示发送消息按钮
    if ($("#msg_text").val()) {
        $("#sendMessage").removeClass("mui-hidden");
        $(".footer-right i.chat-icon-add-more").addClass("mui-hidden");
    } else {
        $("#sendMessage").addClass("mui-hidden");
        $(".footer-right i.chat-icon-add-more").removeClass("mui-hidden");
    }
}

//在焦点处插入内容
function focusInsert(obj, str) {
    var result, val = obj.value;
    obj.focus();
    if (document.selection) { //ie
        result = document.selection.createRange();
        document.selection.empty();
        result.text = str;
    } else {
        result = [
            val.substring(0, obj.selectionStart),
            str,
            val.substr(obj.selectionEnd)
        ];
        obj.focus();
        obj.value = result.join('');
    }
}

//转换内容
function imContent(content) {
    //支持的html标签
    var html = function(end) {
        return new RegExp('\\n*\\[' + (end || '') + '(pre|div|p|table|thead|th|tbody|tr|td|ul|li|ol|li|dl|dt|dd|h2|h3|h4|h5)([\\s\\S]*?)\\]\\n*', 'g');
    };
    var facesDOM = $(".mui-slider-item ul li");
    var faces = {};
    for (i = 0; i < facesDOM.length; i++) {
        faces[facesDOM[i].title] = facesDOM[i].getElementsByTagName("img")[0].src;
    }
    content = (content || '').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')
        .replace(/face\[([^\s\[\]]+?)\]/g, function(face) { //转义表情
            var alt = face.replace(/^face/g, '');
            return '<img alt="' + alt + '" title="' + alt + '" src="' + faces[alt] + '">';
        }).replace(/a\([\s\S]+?\)\[[\s\S]*?\]/g, function(str) { //转义链接
            var href = (str.match(/a\(([\s\S]+?)\)\[/) || [])[1];
            var text = (str.match(/\)\[([\s\S]*?)\]/) || [])[1];
            if (!href)
                return str;
            return '<a href="' + href + '" target="_blank">' + (text || href) + '</a>';
        }).replace(html(), '\<$1 $2\>').replace(html('/'), '\</$1\>') //转移代码
        .replace(/\n/g, '<br>') //转义换行 
    return content;
}
/**
 * 获取当前时间
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate +
        " " + date.getHours() + seperator2 + date.getMinutes() +
        seperator2 + date.getSeconds();
    return currentdate;
}