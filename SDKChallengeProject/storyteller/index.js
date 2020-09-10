const { app, BrowserWindow, ipcMain } = require("electron");
const Store = require("electron-store");

const store = new Store({
  name: process.env.STORYTELLER_STORAGE_KEY || undefined,
});
ipcMain.handle("getStoreValue", (event, key) => {
  return store.get(key);
});
ipcMain.handle("setStoreValue", (event, key, value) => {
  return store.set(key, value);
});

function createWindow() {
  const win = new BrowserWindow({
    width: 1440,
    height: 720,
    webPreferences: {
      nodeIntegration: true,
      webviewTag: true,
    },
  });

  win.loadFile("./ui/public/index.html");
}

app.whenReady().then(createWindow);
