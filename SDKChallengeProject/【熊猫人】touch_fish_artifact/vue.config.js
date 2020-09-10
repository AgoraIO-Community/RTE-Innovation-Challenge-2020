const path = require("path");
const os = require("os");

const config = {
    configureWebpack: {
        module: {
            rules: [{
                test: /\.node$/,
                loader: 'native-ext-loader',
                options: {
                    rewritePath: os.platform() === 'win32' ? './resources' : '../Resources'
                }
            }]
        }
    },
    chainWebpack: config => {
        config.entry('app').clear().add('./render/main.ts');
        if (process.env.NODE_ENV !== 'production') {
            config.externals({
                "agora-electron-sdk": "commonjs2 agora-electron-sdk",
            });
        }
        config.resolve.extensions.add('.node');
        config.resolve.alias.set("@", path.join(path.resolve(), "/render"));
    },
    pluginOptions: {
        electronBuilder: {
            nodeIntegration: true,
            // Use this to change the entrypoint of your app's main process
            mainProcessFile: 'main/main.ts',
            builderOptions: {
                // options placed here will be merged with default configuration and passed to electron-builder
                appId: "com.jhpy.fish",
                productName: "Touch Fish Artifact",
                directories: {
                    app: "./dist_electron/bundled",
                    output: "dist_electron"
                },
                asar: true,
                mac: {
                    target: [
                        "dmg"
                    ],
                    hardenedRuntime: false,
                    extendInfo: {
                        NSMicrophoneUsageDescription: "请允许我们访问您的麦克风",
                        NSCameraUsageDescription: "请允许我们访问您的摄像头"
                    },
                },
                win: {
                    target: [{
                        "target": "nsis",
                        "arch": [
                            "ia32",
                            // "x64"
                        ]
                    }]
                },
                nsis: {
                    oneClick: false, // 是否一键安装
                    allowElevation: true, // 允许请求提升。 如果为false，则用户必须使用提升的权限重新启动安装程序。
                    allowToChangeInstallationDirectory: true, // 允许修改安装目录
                    createDesktopShortcut: true, // 创建桌面图标
                },
                dmg: {
                    window: {
                        width: 540,
                        height: 380
                    },
                    contents: [{
                            x: 410,
                            y: 180,
                            type: "link",
                            path: "/Applications"
                        },
                        {
                            x: 130,
                            y: 180,
                            type: "file"
                        }
                    ],
                    iconSize: 128
                }
            }
        }
    }
}

if (process.env.NODE_ENV === 'production') {
    config.configureWebpack.module.rules[0].options = {
        rewritePath: os.platform() === 'win32' ? './resources' : '../Resources'
    };
    config.pluginOptions.electronBuilder.builderOptions.mac.extraFiles = [{
        "from": "node_modules/agora-electron-sdk/build/Release",
        "to": './Resources'
    }];
    config.pluginOptions.electronBuilder.builderOptions.win.extraFiles = [{
        "from": "node_modules/agora-electron-sdk/build/Release",
        "to": './resources'
    }];
}

module.exports = config;