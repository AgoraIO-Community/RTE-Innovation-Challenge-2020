const vscode = require('vscode');
const fs = require('fs');
const path = require('path');
const util = require('./util');
const yesapi = require('./view/lib/yes4.js');

/**
 * @param {*} context
 * @param {*} templatePath
 */
function getWebViewContent(context, templatePath) {
    const resourcePath =  path.join(context.extensionPath, templatePath);
    const dirPath = path.dirname(resourcePath);
    let html = fs.readFileSync(resourcePath, 'utf-8');
    html = html.replace(/(<link.+?href="|<script.+?src="|<img.+?src=")(.+?)"/g, (m, $1, $2) => {
        return $1 + vscode.Uri.file(path.resolve(dirPath, $2)).with({ scheme: 'vscode-resource' }).toString() + '"';
    });
    return html;
}


const messageHandler = {
    stopSync() {
        vscode.window.showInformationMessage("回调执行");
    },
    initedSuccess() {
        vscode.window.showInformationMessage("成功开启同步！");
    },
    danmu(global,message) {
        vscode.window.showInformationMessage(global.text1213);
    },
    newRoom(global,context) {
        context.workspaceState.update("channelS",global.rid);
        console.log(context.workspaceState.get("channelS"));
    }
};

function goWeb(panel, cmd, data) {
    console.log(arguments);
    panel.webview.postMessage({cmd, data: data});
    console.log(panel.webview.postMessage({cmd, data: data}))
}


var throttling = (fn, wait) => {
	let timer;
	let context, args;
 
	let run = () => {
		timer=setTimeout(()=>{
			fn.apply(context,args);
			clearTimeout(timer);
			timer=null;
		},wait);
	}
 
	return function () {
		context=this;
		args=arguments;
		if(!timer){
			console.log("throttle, set");
			run();
		}else{
			console.log("throttle, ignore");
		}
	}
 
}

module.exports = function(context) {
    context.subscriptions.push(vscode.commands.registerCommand('puluter.codesync', async function (uri) {
        //创建webview
        const panel = vscode.window.createWebviewPanel(
            'testWebview',
            "同步中~",
            vscode.ViewColumn.One,
            {
                enableScripts: true,
                retainContextWhenHidden: true,
            }
        );
        //注册修改事件
        vscode.workspace.onDidChangeTextDocument((e)=>{
            let {contentChanges, document} = e;
            throttling(()=>{
                console.log(document);
                goWeb(panel, "updateTxt", {text: document.getText()})
            },300)();
          },this);


        let id;
        console.log(context.workspaceState.get("channelS"));
        if(context.workspaceState.get("channelS") === undefined) {
            id = -1;
        } else {
            id = context.workspaceState.get("channelS");
        }

        //注册webview回调事件
        await panel.webview.onDidReceiveMessage(message => {
            if (messageHandler[message.cmd]) {
                messageHandler[message.cmd](message,context);
            } else {
                util.showError(`未找到名为 ${message.cmd} 回调方法!`);
            }
        }, undefined, context.subscriptions);
        //载入webview内容
        setTimeout(()=>{
        goWeb(panel, "initUid", {uid: id})
        },3000);
        panel.webview.html = await getWebViewContent(context, '/src/view/index.html');
    }));
};  
