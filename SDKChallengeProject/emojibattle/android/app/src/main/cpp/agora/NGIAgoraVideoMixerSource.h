//
//  Agora SDK
//
//  Copyright (c) 2018 Agora.io. All rights reserved.
//

#pragma once  // NOLINT(build/header_guard)

#include "AgoraBase.h"
#include "AgoraRefPtr.h"
#include "IAgoraService.h"

namespace agora {
namespace rtc {

class IVideoTrack;

struct MixerLayoutConfig {
  uint32_t top;
  uint32_t left;
  uint32_t width;
  uint32_t height;
  int32_t zOrder;
  float alpha;
};

/**
 * The IVideoMixerSource class abstracts a multi-in-multi-out video source which receives video
 * streams from multiple local or remote video tracks and generate mixed video stream in user defined output
 * format. When only one video track is added to the mixer, it simply forwards the incoming video frames
 * to its sinks.
 */
class IVideoMixerSource : public RefCountInterface {
public:
  /**
   * Add a video track for mixing.
   * @param track The instance of the video track that you want mixer to receive its video stream.
   */
  virtual void addVideoTrack(agora_refptr<IVideoTrack> track) = 0;
  /**
   * Remove the video track.
   * @param track The instance of the video track that you want to remove from the mixer.
   */
  virtual void removeVideoTrack(agora_refptr<IVideoTrack> track) = 0;
  /**
   * Configure the layout of video frames comming from a specific track (indicated by uid)
   * on the mixer canvas.
   * @param uid The instance of the video track that you want to remove from the mixer.
   * @param config The layout configuration
   */
  virtual void setStreamLayout(user_id_t uid, const MixerLayoutConfig& config) = 0;
  /**
   * Add a image source to the mixer with its layout configuration on the mixer canvas.
   * @param url The URL of the image source.
   * @param config The layout configuration
   */
  virtual void setImageSource(const char* url, const MixerLayoutConfig& config) = 0;
  /**
   * Remove the user layout on the mixer canvas
   * 
   * @param uid The uid of the stream.
   */
  virtual void delStreamLayout(user_id_t uid) = 0;
  /**
   * Refresh the user layout on the mixer canvas
   * @param uid The uid of the stream.
   */
  virtual void refresh(user_id_t uid) = 0;
  /**
   * Set the mixer canvas background to override the default configuration
   * @param width width of the canvas
   * @param height height of the canvas
   * @param fps fps of the mixed video stream
   * @param color_argb mixer canvas background color in argb
   */
  virtual void setBackground(uint32_t width, uint32_t height, int fps, uint32_t color_argb) = 0;
  /**
   * Set the mixer canvas background to override the default configuration
   * @param width width of the canvas
   * @param height height of the canvas
   * @param fps fps of the mixed video stream
   * @param url URL of the canvas background image
   */
  virtual void setBackground(uint32_t width, uint32_t height, int fps, const char* url) = 0;
  /**
   * Set the rotation of the mixed video stream
   * @param rotation:0:none, 1:90°, 2:180°, 3:270° 
   */
  virtual void setRotation(uint8_t rotation) = 0;
};

} //namespace rtc
} // namespace agora
