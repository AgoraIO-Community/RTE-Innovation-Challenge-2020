import bilibiliAutoWebFullScreen from "@/utils/bilibili-auto-webfullscreen";
import tencentVideoAutoWebFullScreen from "@/utils/tencentvideo-auto-webfullscreen";

const config: any = [{
    reg: new RegExp(/(http)(s)?(:\/\/)(www\.bilibili.com)/g),
    code: bilibiliAutoWebFullScreen
}, {
    reg: new RegExp(/(http)(s)?(:\/\/)(v\.qq.com)/g),
    code: tencentVideoAutoWebFullScreen
}];

export default handleCode(config);

function handleCode(arr: any): WebviewCodeExecute[] {
    for (const item of arr) {
        item.code = `(${item.code})();`;
    }
    return arr;
}

interface WebviewCodeExecute {
    reg: RegExp;
    code: string;
}
