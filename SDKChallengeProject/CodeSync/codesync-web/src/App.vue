<template>
<div>
  <div>
	<div id="join">
		<div id="header">
		<h1>CodeSync</h1>
		<p>轻松将您的代码从VSCode同步分享至网页端<br />
		<a>下载插件 即刻体验</a></p> 
		</div>

		<form id="signup-form">
      <input type="roomNumber" name="id" id="roomNumber" placeholder="房间号码" />
      <select type="langType" name="ty" id="langType" placeholder="语言高亮类型" >
        <option value ="java">Java</option>
        <option value ="c++">C++</option>
        <option value="python">Python</option>
        <option value="js">Javascript</option>
        <option value="html">HTML</option>
        <option value="css">CSS</option>
      </select>
      <input type="submit" value="进入" />
		</form>

		<div id="footer">
		<ul class="copyright">
		<li>&copy; 2020 CodeSync.</li>
		</ul>
		</div>
  </div>
  </div>
  <div :class="classOfPage">
    <div class="header">
      <div class="fileinfo">
        {{timeS}}
      </div>
    </div>
    <div class="sidebar"></div>
    <div class="codearea line-numbers">
      <pre
        style="background-color: #1e1e1e; font-size: 15px"
      ><code :class="classOfLang" v-text="html"></code></pre>
    </div>
    <div class="barrage">
      <div class="b-bot">
      <input
        v-model="barrageMsg"
        class="b-send"
        type="text"
        placeholder="完善信息后可以参与弹幕互动"
        value=""
      />
      <button v-on:click="sendBarrage()" class="b-sendbtn">发送</button>
      </div>
      <div class="b-content">
        <div v-for="info in barrage" :key="info.id">
          <div class="b-info">
            <img class="b-avatar" :src="avatar" />
            <div class="b-text">
              <div class="b-username">{{ info.username }}</div>
              <div class="b-message">{{ info.message }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom">CodeSync @ Copyright 2020.</div>
  </div>
</div>

</template>

<script>

import Prism from "./assets/prism";
import avatar from "./assets/default-avatar.png";
import AgoraRTM from "agora-rtm-sdk";
import "./assets/prism.css";
import yesapi from "./lib/yes3";

function getQueryString(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}
const rtm = {
  client: null,
  channel: null,
};
let options = {
  appId: "",
  channel: "demo_channel_name",
  token: null,
  uid: "testing",
  channelID: null,
};
async function initRtm(uqid,_this) {
  uqid = await yesapi.cs.queryRoom(uqid);
  rtm.client = AgoraRTM.createInstance(options.appId);
  let uid = options.uid;
  await rtm.client.login({
    token: null,
    uid: uid + Math.round(Math.random() * 10000).toString(),
  });
  let id = uqid;
  rtm.channel = rtm.client.createChannel(id);
  options.channelID = id;
  await rtm.channel.join();

  rtm.channel.on("ChannelMessage", ({ text }) => {
    _this.msgList[1] = text;
    let defCode = _this.msgList[1].substring(0, 6);
    if ((_this.msgList[1] != _this.msgList[0]) && (defCode == "[BARR]")) {
      _this.barrage.push({
        id: 1,
        username: "游客",
        message: _this.msgList[1].slice(6),
      });
    } else if ((_this.msgList[1] != _this.msgList[0]) && (defCode == "[CODE]")) {
      _this.html = _this.msgList[1].slice(6);
      setTimeout(() => {
        Prism.highlightAll();
      }, 500);
    }
    _this.msgList[0] = _this.msgList[1];    
  });
}
async function sendRtmMsg(msg) {
  await rtm.channel.sendMessage({ text: msg });
}

export default {
  data() {
    return {
      timeS:"15:55:53",
      classOfLang: "language-java",
      classOfPage: "container",
      barrage: [],
      barrageMsg: "",
      avatar: avatar,
      msgChecker: "",
      msgList: ["", "", ""],
      html: `import Loading from 'CodeSync' //Codesync 正在加载`,
    };
  },
  methods: {
    sendBarrage: function () {
      sendRtmMsg("[BARR]" + this.barrageMsg);
      if (this.barrageMsg != this.msgList[3]) {
        this.barrage.push({
          id: 1,
          username: "游客",
          message: this.barrageMsg,
        });
      }
      this.msgList[3] = this.barrageMsg;
      //this.barrage.push({id:1, username:"游客", message:"老师讲的真好。"})
    },
  },
  mounted() {
    if (getQueryString("id")) {
      this.classOfLang = `language-${getQueryString("ty")}`;
      this.classOfPage = "container"
    } else {
      this.classOfPage = "container invisible"
    }
    initRtm(getQueryString("id"),this);
    Prism.highlightAll();
    let _this = this;
    setInterval(() => {
      _this.timeS = new Date().toTimeString().slice(0,8);
    }, 1000);


    (function() {
      var	$body = document.querySelector('#join');
      // console.log($body)
          !function(){function t(t){this.el=t;for(var n=t.className.replace(/^\s+|\s+$/g,"").split(/\s+/),i=0;i<n.length;i++)e.call(this,n[i])}function n(t,n,i){Object.defineProperty?Object.defineProperty(t,n,{get:i}):t.__defineGetter__(n,i)}if(!("undefined"==typeof window.Element||"classList"in document.documentElement)){var i=Array.prototype,e=i.push,s=i.splice,o=i.join;t.prototype={add:function(t){this.contains(t)||(e.call(this,t),this.el.className=this.toString())},contains:function(t){return-1!=this.el.className.indexOf(t)},item:function(t){return this[t]||null},remove:function(t){if(this.contains(t)){for(var n=0;n<this.length&&this[n]!=t;n++);s.call(this,n,1),this.el.className=this.toString()}},toString:function(){return o.call(this," ")},toggle:function(t){return this.contains(t)?this.remove(t):this.add(t),this.contains(t)}},window.DOMTokenList=t,n(Element.prototype,"classList",function(){return new t(this)})}}();
          window.canUse=function(p){if(!window._canUse)window._canUse=document.createElement("div");var e=window._canUse.style,up=p.charAt(0).toUpperCase()+p.slice(1);return p in e||"Moz"+up in e||"Webkit"+up in e||"O"+up in e||"ms"+up in e};
          (function(){if("addEventListener"in window)return;window.addEventListener=function(type,f){window.attachEvent("on"+type,f)}})();
      
        window.addEventListener('load', function() {
          window.setTimeout(function() {
            $body.classList.remove('is-preload');
          }, 100);
        });

		(function() {
				var settings = {
						images: {
							'https://cdn.jovel.net/code3.png': 'center'
						},
						delay: 6000
				};
				var	pos = 0, lastPos = 0,
					$wrapper, $bgs = [], $bg,
					k;
				$wrapper = document.createElement('div');
					$wrapper.id = 'bg';
					$body.appendChild($wrapper);
				for (k in settings.images) {
						$bg = document.createElement('div');
							$bg.style.backgroundImage = 'url("' + k + '")';
							$bg.style.backgroundPosition = settings.images[k];
							$wrapper.appendChild($bg);
						$bgs.push($bg);
				}
				$bgs[pos].classList.add('visible');
				$bgs[pos].classList.add('top');
        if ($bgs.length == 1
				)
					return;
				window.setInterval(function() {
					lastPos = pos;
					pos++;
						if (pos >= $bgs.length)
							pos = 0;
						$bgs[lastPos].classList.remove('top');
						$bgs[pos].classList.add('visible');
						$bgs[pos].classList.add('top');
						window.setTimeout(function() {
							$bgs[lastPos].classList.remove('visible');
						}, settings.delay / 2);
				}, settings.delay);
      })();
    })();
  },
  computed: {
    timeStr: () => {
      return new Date().toTimeString().slice(0,8);
    },
  },
};
</script>

<style scoped lang="scss">
@import "./css/main.css";
.invisible {
  display: none;
}
.container {
  background-color: #1e1e1e;
  top: 0;
  left: 0px;
  width: 100%;
  height: 100%;
  position: fixed;
}
.header {
  background-color: #2d2d2d;
  top: 0;
  left: 70px;
  width: 100%;
  height: 50px;
  position: absolute;
  z-index: 10;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
}
.barrage {
  background-color: #252526;
  top: 0;
  right: 0;
  width: 350px;
  height: 100%;
  position: absolute;
  z-index: 1;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column-reverse;
}
.codearea {
  background-color: #1e1e1e;
  margin-left: 80px;
  margin-top: 60px;
  width: calc(100% - 440px);
  height: calc(100% - 75px);
  overflow: auto;
}

@media screen and (max-width: 1000px) {

  .codearea {
    background-color: #1e1e1e;
    margin-left: 0px;
    margin-top: 50px;
    width: 100%;
    height: calc(100% - 275px);
    overflow: auto;
  }
  .barrage {
    background-color: #252526;
    bottom: 25px;
    right: 0;
    top: auto;
    height: 210px;
    width: 100%;
    position: fixed;
    z-index: 5;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    display: flex;
    flex-direction: column-reverse;
    justify-content: flex-start;
    .b-content {
      margin-bottom: 15px;
      height: 150px;
      overflow: auto;
    }
    .b-bot {
      margin-bottom: 15px;
      // position: absolute;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      .b-send {
        bottom: 0px;
        left: auto;
        height: 35px;
        width: calc(100% - 80px);
        border-radius: 5px;
        background-color: #383a42;
        position: relative;
        color: white;
        outline-color: invert;
        outline-style: none;
        outline-width: 0px;
        border: none;
        padding-left: 10px;
        padding-right: 10px;
      }
      .b-sendbtn {
        bottom: 0px;
        right: auto;
        height: 37px;
        width: 60px;
        border-radius: 5px;
        background-color: #0079d0;
        position: relative;
        color: white;
        outline-color: invert;
        outline-style: none;
        outline-width: 0px;
        border: none;
      }
    }
  }
  .header {
    background-color: #2d2d2d;
    top: 0;
    width: 100%;
    height: 50px;
    position: absolute;
    z-index: 10;
    left: 0;
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
  }
  .sidebar {
    display: none;
  }
}
.fileinfo {
  background-color: #1e1e1e;
  top: 0;
  width: 200px;
  color: #ccc;
  line-height: 50px;
  font-size: 20px;
  padding-left: 20px;
  height: 50px;
  font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace;
}
.sidebar {
  background-color: #333333;
  top: 0;
  left: 0;
  width: 70px;
  height: 100%;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.5);
  position: absolute;
}
.bottom {
  background-color: #0079d0;
  bottom: 0;
  left: 0px;
  width: 100%;
  height: 25px;
  color: white;
  font-size: 12px;
  line-height: 25px;
  padding-left: 10px;
  position: absolute;
  z-index: 20;
}
.b-avatar {
  border-radius: 10px;
  width: 20px;
  height: 20px;
}
.b-send {
  bottom: 40px;
  left: 15px;
  height: 35px;
  width: 250px;
  border-radius: 5px;
  background-color: #383a42;
  position: absolute;
  color: white;
  outline-color: invert;
  outline-style: none;
  outline-width: 0px;
  border: none;
  padding-left: 10px;
  padding-right: 10px;
  font-size: 13px;
}
.b-sendbtn {
  bottom: 40px;
  right: 10px;
  height: 35px;
  width: 60px;
  border-radius: 5px;
  background-color: #0079d0;
  position: absolute;
  color: white;
  outline-color: invert;
  outline-style: none;
  outline-width: 0px;
  border: none;
}
.b-content {
  margin-bottom: 75px;
}
.b-info {
  margin: 20px;
  color: white;
  display: -webkit-box;
}
.b-username {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #97989a;
  letter-spacing: 0;
}
.b-message {
  font-family: PingFangSC-Regular;
  font-size: 13px;
  color: #fefffe;
  letter-spacing: 0;
}
.b-avatar {
  width: 36px;
  height: 36px;
  border-radius: 100%;
}
.b-text {
  font-size: 12px;
  margin-left: 12px;
  line-height: 20px;
  width: 100%;
  position: relative;
}
</style>
