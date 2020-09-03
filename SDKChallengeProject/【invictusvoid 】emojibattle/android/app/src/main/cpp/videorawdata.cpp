#include <jni.h>
#include <android/log.h>
#include <cstring>
#include "agora/IAgoraMediaEngine.h"

#include "agora/IAgoraRtcEngine.h"
#include <string.h>
#include "videorawdata.h"
#include "agora/VMUtil.h"

#include <map>

using namespace std;

jobject gCallBack = nullptr;
jclass gCallbackClass = nullptr;
jmethodID captureVideoMethodId = nullptr;
jmethodID renderVideoMethodId = nullptr;
void *_javaDirectPlayBufferCapture = nullptr;
map<int, void *> decodeBufferMap;

static JavaVM *gJVM = nullptr;

/**Listener to get video frame*/
class AgoraVideoFrameObserver : public agora::media::IVideoFrameObserver {

public:
    AgoraVideoFrameObserver() {

    }

    ~AgoraVideoFrameObserver() {

    }

    void
    getVideoFrame(VideoFrame &videoFrame, _jmethodID *jmethodID, void *_byteBufferObject,
                  unsigned int uid) {
        if (_byteBufferObject == nullptr) {
            return;
        }

        int width = videoFrame.width;
        int height = videoFrame.height;
        size_t widthAndHeight = (size_t) videoFrame.yStride * height;
        size_t length = widthAndHeight * 3 / 2;

        AttachThreadScoped ats(gJVM);
        JNIEnv *env = ats.env();

        memcpy(_byteBufferObject, videoFrame.yBuffer, widthAndHeight);
        memcpy((uint8_t *) _byteBufferObject + widthAndHeight, videoFrame.uBuffer,
               widthAndHeight / 4);
        memcpy((uint8_t *) _byteBufferObject + widthAndHeight * 5 / 4, videoFrame.vBuffer,
               widthAndHeight / 4);

        if (uid == 0) {
            env->CallVoidMethod(gCallBack, jmethodID, videoFrame.type, width, height, length,
                                videoFrame.yStride, videoFrame.uStride,
                                videoFrame.vStride, videoFrame.rotation,
                                videoFrame.renderTimeMs);
        } else {
            env->CallVoidMethod(gCallBack, jmethodID, uid, videoFrame.type, width, height,
                                length,
                                videoFrame.yStride, videoFrame.uStride,
                                videoFrame.vStride, videoFrame.rotation,
                                videoFrame.renderTimeMs);
        }
    }

    void writebackVideoFrame(VideoFrame &videoFrame, void *byteBuffer) {
        if (byteBuffer == nullptr) {
            return;
        }

        int width = videoFrame.width;
        int height = videoFrame.height;
        size_t widthAndHeight = (size_t) videoFrame.yStride * height;

        memcpy(videoFrame.yBuffer, byteBuffer, widthAndHeight);
        memcpy(videoFrame.uBuffer, (uint8_t *) byteBuffer + widthAndHeight, widthAndHeight / 4);
        memcpy(videoFrame.vBuffer, (uint8_t *) byteBuffer + widthAndHeight * 5 / 4,
               widthAndHeight / 4);
    }

public:
    /**Occurs each time the SDK receives a video frame captured by the local camera.
     * After you successfully register the video frame observer, the SDK triggers this callback each
     *  time a video frame is received. In this callback, you can get the video data captured by the
     *  local camera. You can then pre-process the data according to your scenarios.
     * After pre-processing, you can send the processed video data back to the SDK by setting the
     *  videoFrame parameter in this callback.
     * @param videoFrame
     * @return
     *   Whether or not to ignore the current video frame if the pre-processing fails:
     *     true: Do not ignore.
     *     false: Ignore the current video frame, and do not send it back to the SDK.
     * PS:
     *   This callback does not support sending processed RGBA video data back to the SDK.*/
    virtual bool onCaptureVideoFrame(VideoFrame &videoFrame) override {
        getVideoFrame(videoFrame, captureVideoMethodId, _javaDirectPlayBufferCapture, 0);
//        __android_log_print(ANDROID_LOG_DEBUG, "AgoraVideoFrameObserver", "onCaptureVideoFrame");
        writebackVideoFrame(videoFrame, _javaDirectPlayBufferCapture);
        return true;
    }

    /**Occurs each time the SDK receives a video frame sent by the remote user.
     * After you successfully register the video frame observer and isMultipleChannelFrameWanted
     *  return false, the SDK triggers this callback each time a video frame is received. In this
     *  callback, you can get the video data sent by the remote user. You can then post-process the
     *  data according to your scenarios.
     * After post-processing, you can send the processed data back to the SDK by setting the videoFrame
     *  parameter in this callback.
     * @param uid ID of the remote user who sends the current video frame.
     * @param videoFrame
     * @return
     *   Whether or not to ignore the current video frame if the post-processing fails:
     *    true: Do not ignore.
     *    false: Ignore the current video frame, and do not send it back to the SDK.
     * PS:
     *   This callback does not support sending processed RGBA video data back to the SDK.*/
    virtual bool onRenderVideoFrame(unsigned int uid, VideoFrame &videoFrame) override {
        __android_log_print(ANDROID_LOG_DEBUG, "AgoraVideoFrameObserver", "onRenderVideoFrame");
        map<int, void *>::iterator it_find;
        it_find = decodeBufferMap.find(uid);

        if (it_find != decodeBufferMap.end()) {
            if (it_find->second != nullptr) {
                getVideoFrame(videoFrame, renderVideoMethodId, it_find->second, uid);
                writebackVideoFrame(videoFrame, it_find->second);
            }
        }
        return true;
    }

};

static AgoraVideoFrameObserver s_videoFrameObserver;
static agora::rtc::IRtcEngine *rtcEngine = nullptr;

#ifdef __cplusplus
extern "C" {
#endif

int __attribute__((visibility("default")))
loadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine *engine) {
    __android_log_print(ANDROID_LOG_DEBUG, "apm-video-raw-data", "plugin loadAgoraRtcEnginePlugin");
    rtcEngine = engine;
    return 0;
}

void __attribute__((visibility("default")))
unloadAgoraRtcEnginePlugin(agora::rtc::IRtcEngine *engine) {
    __android_log_print(ANDROID_LOG_DEBUG, "apm-video-raw-data", "unloadAgoraRtcEnginePlugin");

    rtcEngine = nullptr;
}

JNIEXPORT void JNICALL Java_com_qifan_emojibattle_internal_sdk_VideoRawData_setCallback
        (JNIEnv *env, jobject, jobject callback) {
    if (!rtcEngine) {
        __android_log_print(ANDROID_LOG_INFO, "ldh", "register null");
        return;
    }

    env->GetJavaVM(&gJVM);

    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    mediaEngine.queryInterface(rtcEngine, agora::INTERFACE_ID_TYPE::AGORA_IID_MEDIA_ENGINE);
    if (mediaEngine) {
/**Registers a video frame observer object.
 * You need to implement the IVideoFrameObserver class in this method, and register callbacks
 *  according to your scenarios.
 * After you successfully register the video frame observer, the SDK triggers the registered
 *  callbacks each time a video frame is received.
 * @param observer Video frame observer object instance. If NULL is passed in, the registration is canceled.
 * @return
 *   0: Success.
 *   < 0: Failure.
 * PS:
 *   When handling the video data returned in the callbacks, pay attention to the changes in
 *    the width and height parameters, which may be adapted under the following circumstances:
 *   When the network condition deteriorates, the video resolution decreases incrementally.
 *   If the user adjusts the video profile, the resolution of the video returned in the callbacks also changes.*/
        int code = mediaEngine->registerVideoFrameObserver(&s_videoFrameObserver);
    }

    if (gCallBack == nullptr) {
        gCallBack = env->NewGlobalRef(callback);
        gCallbackClass = env->GetObjectClass(gCallBack);

/**Get the MethodId of each callback function through the callback object.
 * Pass the data back to the java layer through these methods*/
        captureVideoMethodId = env->GetMethodID(gCallbackClass, "onCaptureVideoFrame",
                                                "(IIIIIIIIJ)V");
        __android_log_print(ANDROID_LOG_DEBUG, "setCallback", "setCallback done successfully");
    }
}

JNIEXPORT void JNICALL
Java_com_qifan_emojibattle_internal_sdk_VideoRawData_setVideoCaptureByteBuffer
        (JNIEnv *env, jobject, jobject bytebuffer) {
    _javaDirectPlayBufferCapture = env->GetDirectBufferAddress(bytebuffer);
}

JNIEXPORT void JNICALL Java_com_qifan_emojibattle_internal_sdk_VideoRawData_releasePoint
        (JNIEnv *env, jobject) {
    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    mediaEngine.queryInterface(rtcEngine, agora::INTERFACE_ID_TYPE::AGORA_IID_MEDIA_ENGINE);

    if (mediaEngine) {
/**Release video and audio observers*/
        mediaEngine->registerVideoFrameObserver(NULL);
    }

    if (gCallBack != nullptr) {
        env->DeleteGlobalRef(gCallBack);
        gCallBack = nullptr;
    }
    gCallbackClass = nullptr;

    captureVideoMethodId = nullptr;
    renderVideoMethodId = nullptr;

    _javaDirectPlayBufferCapture = nullptr;

    decodeBufferMap.clear();
}

#ifdef __cplusplus
}
#endif
