export default function() {
    const userAgent: string = navigator.userAgent.toLowerCase();
    if (userAgent.indexOf(" electron/") > -1) {
        const remote = require("electron").remote; // 修改默认对话框，修复electron弹出默认对话框后页面失去焦点的bug
        window.alert = function(str: string) {
            const options = {
                type: "info",
                buttons: ["确定"],
                defaultId: 0,
                cancelId: 0,
                detail: str,
                message: ""
            };
            remote.dialog.showMessageBoxSync(options);
        };
        window.confirm = function(str?: string | undefined) {
            if (typeof str === "string") {
                const options = {
                    type: "info",
                    buttons: ["确认", "取消"],
                    defaultId: 0,
                    cancelId: 1,
                    detail: "",
                    message: str
                };
                const flag = remote.dialog.showMessageBoxSync(options);
                console.log(flag);
                if (flag === 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        };
    }
}
