const functions = require('firebase-functions');
const admin = require('firebase-admin');
const firebase_tools = require('firebase-tools');

admin.initializeApp();

exports.deleteRoomInfo = functions
  .runWith({
    timeoutSeconds: 540,
    memory: '2GB'
  })
  .https.onCall((data) => {

    const path = data.path;
    console.log(`User has requested to delete path ${path}`);

    return firebase_tools.firestore
      .delete(path, {
        project: process.env.GCLOUD_PROJECT,
        recursive: true,
        yes: true
      })
      .then(() => {
        return {
          path: path
        };
      });
  });