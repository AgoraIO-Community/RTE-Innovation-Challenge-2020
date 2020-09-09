export default function() {
    let counter = 0;
    let iscensor = true;
    let url = geturl(); // 获取刚加载脚本时的Url
    setTimeout(censor, 1000);
    go();

    function go() {
        counter++;
        if (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen") &&
            (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen") as HTMLElement).offsetHeight > 0) {
            if (document.querySelector("#bangumi_player")) {
                scrollTo(0, (document.querySelector("#bangumi_player") as HTMLElement).offsetTop);
                if ((document.querySelector(".bilibili-player-video-btn.bilibili-player-video-btn-widescreen") as HTMLElement).innerHTML.indexOf("退出宽屏") !== -1 ||
                    (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen") as HTMLElement).innerHTML.indexOf("退出网页全屏") !== -1 ||
                    (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-btn-fullscreen") as HTMLElement).innerHTML.indexOf("退出全屏") !== -1
                ) {
                    iscensor = true;
                    return;
                }
                setTimeout(function() {
                    (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen") as HTMLElement).click();
                    iscensor = false;
                }, 500);
            } else {
                if (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen:not(.closed)") !== null) {
                    setTimeout(function() {
                        (document.querySelector(".bilibili-player-video-btn.bilibili-player-video-web-fullscreen:not(.closed)") as HTMLElement).click();
                        iscensor = false;
                    }, 500);
                }
            }
        } else {
            if (counter > 30) {
                iscensor = false;
                return;
            }
            setTimeout(go, 300);
        }
    }

    function censor() {
        if (!iscensor && url !== geturl()) {
            counter = 0;
            go();
            url = geturl();
        }
        setTimeout(censor, 2000);
    }

    function geturl() {
        return window.location.href;
    }
}
