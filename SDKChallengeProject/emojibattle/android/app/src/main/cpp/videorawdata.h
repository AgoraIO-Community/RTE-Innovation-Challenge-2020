#include <jni.h>

#ifndef _Included_com_qifan_emojibattle_sdk_VideoRawData
#define _Included_com_qifan_emojibattle_sdk_VideoRawData
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_qifan_emojibattle_sdk_VideoRawData_setCallback
        (JNIEnv *, jobject, jobject);

JNIEXPORT void JNICALL
Java_com_qifan_emojibattle_sdk_VideoRawData_setVideoCaptureByteBuffer
        (JNIEnv *, jobject, jobject);

JNIEXPORT void JNICALL Java_com_qifan_emojibattle_sdk_VideoRawData_releasePoint
        (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif