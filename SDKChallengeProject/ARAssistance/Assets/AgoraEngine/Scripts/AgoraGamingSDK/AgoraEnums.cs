
using System;

namespace agora_gaming_rtc
{
    #region some enum and struct types
    /** Video display mode. */
    public enum VIDEO_RENDER_MODE
    {
        //default Render Mode is rawData
        /** 100: (Default) RawData.*/
        RENDER_RAWDATA = 100,
        /** 101: OpenGLES 2. */
        REDNER_OPENGL_ES2 = 101,
        /** 102: Unity low level interface. */
        RENDER_UNITY_LOW_LEVEL_INTERFACE = 102,
    };

    /** Error code.
    */
    public enum ERROR_CODE
    {
        /** -7: The SDK is not initialized before calling this method. */
        ERROR_NOT_INIT_ENGINE = -7,
        /** 0: No error occurs. */
        ERROR_OK = 0,
        /** -2: An invalid parameter is used. For example, the specific channel name includes illegal characters. */
        ERROR_INVALID_ARGUMENT = -2,
        /** -100: No device is plugged.*/
        ERROR_NO_DEVICE_PLUGIN = -100,
    };

    /** Remote video stream types. */
    public enum REMOTE_VIDEO_STREAM_TYPE
    {
        /** 0: High-stream video. */
        REMOTE_VIDEO_STREAM_HIGH = 0,
        /** 1: Low-stream video. */
        REMOTE_VIDEO_STREAM_LOW = 1,
    };

    /** The state of the remote video. */
    public enum REMOTE_VIDEO_STATE 
    {
        /** 0: The remote video is in the default state, probably due to `REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3)`, `REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5)`, or `REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7)`.
        */
        REMOTE_VIDEO_STATE_STOPPED = 0,

        /** 1: The first remote video packet is received.
        */
        REMOTE_VIDEO_STATE_STARTING = 1,

        /** 2: The remote video stream is decoded and plays normally, probably due to `REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY(2)`, `REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4)`, `REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6)`, or `REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9)`.
        */
        REMOTE_VIDEO_STATE_DECODING = 2,

        /** 3: The remote video is frozen, probably due to `REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1)` or `REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8)`.
        */
        REMOTE_VIDEO_STATE_FROZEN = 3,

        /** 4: The remote video fails to start, probably due to `REMOTE_VIDEO_STATE_REASON_INTERNAL(0)`.
        */
        REMOTE_VIDEO_STATE_FAILED = 4
    };

    /** Reasons for a user being offline. */
    public enum USER_OFFLINE_REASON
    {
        /** 0: The user quits the call. */
        QUIT = 0,
        /** 1: The SDK times out and the user drops offline because no data packet is received within a certain period of time. If the user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline. */
        DROPPED = 1,
        /** 2: (Live broadcast only.) The client role switched from the host to the audience. */
        BECOME_AUDIENCE = 2,
    };
    /** Output log filter level. */
    public enum LOG_FILTER
    {
        /** 0: Do not output any log information. */
        OFF = 0,
        /** 0x80f: Output all log information. Set your log filter as debug if you want to get the most complete log file. */
        DEBUG = 0x80f,
        /** 0x0f: Output CRITICAL, ERROR, WARNING, and INFO level log information. We recommend setting your log filter as this level. */
        INFO = 0x0f,
        /** 0x0e: Outputs CRITICAL, ERROR, and WARNING level log information. */
        WARNING = 0x0e,
        /** 0x0c: Outputs CRITICAL and ERROR level log information. */
        ERROR = 0x0c,
        /** 0x08: Outputs CRITICAL level log information. */
        CRITICAL = 0x08,
    };

     /** The channel profile of the Agora IRtcEngine. */
    public enum CHANNEL_PROFILE
    {
        /** 0: (Default) The Communication profile. Use this profile in one-on-one calls or group calls, where all users can talk freely. */
        CHANNEL_PROFILE_COMMUNICATION = 0,
        /** 1: The Live-Broadcast profile. Users in a live-broadcast channel have a role as either broadcaster or audience. 
         * A broadcaster can both send and receive streams; an audience can only receive streams.
         */
        CHANNEL_PROFILE_LIVE_BROADCASTING = 1,
        /** 2: The Gaming profile. This profile uses a codec with a lower bitrate and consumes less power. Applies to the gaming scenario, where all game players can talk freely.*/
        CHANNEL_PROFILE_GAME = 2,
    };
    
    /** Client roles in a live broadcast. */
    public enum CLIENT_ROLE
    {
        /** 1: Host */
        BROADCASTER = 1,
        /** 2: Audience */
        AUDIENCE = 2,
    };

    /** Audio recording qualities. */
    public enum AUDIO_RECORDING_QUALITY_TYPE
    {
        /** 0: Low quality. The sample rate is 32 kHz, and the file size is around 1.2 MB after 10 minutes of recording. */
        AUDIO_RECORDING_QUALITY_LOW = 0,
        /** 1: Medium quality. The sample rate is 32 kHz, and the file size is around 2 MB after 10 minutes of recording. */
        AUDIO_RECORDING_QUALITY_MEDIUM = 1,
        /** 2: High quality. The sample rate is 32 kHz, and the file size is around 3.75 MB after 10 minutes of recording. */
        AUDIO_RECORDING_QUALITY_HIGH = 2,
    };
    /** Audio output routing. */
    public enum AUDIO_ROUTE
    {
        /** -1: Default.
        */
        AUDIO_ROUTE_DEFAULT = -1,
        /** 0: Headset.
        */
        AUDIO_ROUTE_HEADSET = 0,
        /** 1: Earpiece.
        */
        AUDIO_ROUTE_EARPIECE = 1,
        /** 2: Headset with no microphone.
        */
        AUDIO_ROUTE_HEADSET_NO_MIC = 2,
        /** 3: Speakerphone.
        */
        AUDIO_ROUTE_SPEAKERPHONE = 3,
        /** 4: Loudspeaker.
        */
        AUDIO_ROUTE_LOUDSPEAKER = 4,
        /** 5: Bluetooth headset.
        */
        AUDIO_ROUTE_BLUETOOTH = 5
    };

    /** Connection states. */
    public enum CONNECTION_STATE_TYPE
    {
        /** 1: The SDK is disconnected from Agora's edge server.
         * - This is the initial state before calling the {@link agora_gaming_rtc.IRtcEngine.JoinChannelByKey JoinChannelByKey} method.
         * - The SDK also enters this state when the application calls the {@link agora_gaming_rtc.IRtcEngine.LeaveChannel LeaveChannel} method.
         */
        CONNECTION_STATE_DISCONNECTED = 1,
  
        /** 2: The SDK is connecting to Agora's edge server.
         * - When the application calls the {@link agora_gaming_rtc.IRtcEngine.JoinChannelByKey JoinChannelByKey} method, the SDK starts to establish a connection to the specified channel, triggers the {@link agora_gaming_rtc.OnConnectionStateChangedHandler OnConnectionStateChangedHandler} callback, and switches to the `CONNECTION_STATE_CONNECTING(2)` state.
         * - When the SDK successfully joins the channel, it triggers the `OnConnectionStateChangedHandler` callback and switches to the `CONNECTION_STATE_CONNECTED(3)` state.
         * - After the SDK joins the channel and when it finishes initializing the media engine, the SDK triggers the {@link agora_gaming_rtc.OnJoinChannelSuccessHandler OnJoinChannelSuccessHandler} callback.
         */
        CONNECTION_STATE_CONNECTING = 2,
  
         /** 3: The SDK is connected to Agora's edge server and has joined a channel. You can now publish or subscribe to a media stream in the channel.
         * If the connection to the channel is lost because, for example, if the network is down or switched, the SDK automatically tries to reconnect and triggers:
         * - The {@link agora_gaming_rtc.OnConnectionInterruptedHandler OnConnectionInterruptedHandler} callback (deprecated).
         * - The {@link agora_gaming_rtc.OnConnectionStateChangedHandler OnConnectionStateChangedHandler} callback and switches to the `CONNECTION_STATE_RECONNECTING(4)` state.
         */
        CONNECTION_STATE_CONNECTED = 3,
  
         /** 4: The SDK keeps rejoining the channel after being disconnected from a joined channel because of network issues.
         * - If the SDK cannot rejoin the channel within 10 seconds after being disconnected from Agora's edge server, the SDK triggers the {@link agora_gaming_rtc.OnConnectionLostHandler OnConnectionLostHandler} callback, stays in the `CONNECTION_STATE_RECONNECTING(4)` state, and keeps rejoining the channel.
         * - If the SDK fails to rejoin the channel 20 minutes after being disconnected from Agora's edge server, the SDK triggers the {@link agora_gaming_rtc.OnConnectionStateChangedHandler OnConnectionStateChangedHandler} callback, switches to the `CONNECTION_STATE_FAILED(5)` state, and stops rejoining the channel.
         */
        CONNECTION_STATE_RECONNECTING = 4,
  
        /** 5: The SDK fails to connect to Agora's edge server or join the channel.
         * 
         * You must call the {@link agora_gaming_rtc.IRtcEngine.LeaveChannel LeaveChannel} method to leave this state, and call the {@link agora_gaming_rtc.IRtcEngine.JoinChannelByKey JoinChannelByKey} method again to rejoin the channel.
         * 
         * If the SDK is banned from joining the channel by Agora's edge server (through the RESTful API), the SDK triggers the {@link agora_gaming_rtc.OnConnectionBannedHandler OnConnectionBannedHandler} (deprecated) and {@link agora_gaming_rtc.OnConnectionStateChangedHandler OnConnectionStateChangedHandler} callbacks.
         */
        CONNECTION_STATE_FAILED = 5,
    };

    /** Reasons for a connection state change. */
    public enum CONNECTION_CHANGED_REASON_TYPE
    {
        /** 0: The SDK is connecting to Agora's edge server. */
        CONNECTION_CHANGED_CONNECTING = 0,
        /** 1: The SDK has joined the channel successfully. */
        CONNECTION_CHANGED_JOIN_SUCCESS = 1,
        /** 2: The connection between the SDK and Agora's edge server is interrupted. */
        CONNECTION_CHANGED_INTERRUPTED = 2,
        /** 3: The connection between the SDK and Agora's edge server is banned by Agora's edge server. */
        CONNECTION_CHANGED_BANNED_BY_SERVER = 3,
        /** 4: The SDK fails to join the channel for more than 20 minutes and stops reconnecting to the channel. */
        CONNECTION_CHANGED_JOIN_FAILED = 4,
        /** 5: The SDK has left the channel. */
        CONNECTION_CHANGED_LEAVE_CHANNEL = 5,
        /** 6: The connection failed since Appid is not valid. */
        CONNECTION_CHANGED_INVALID_APP_ID = 6,
        /** 7: The connection failed since channel name is not valid. */
        CONNECTION_CHANGED_INVALID_CHANNEL_NAME = 7,
        /** 8: The connection failed since token is not valid, possibly because:
         * - The App Certificate for the project is enabled in Dashboard, but you do not use Token when joining the channel. If you enable the App Certificate, you must use a token to join the channel.
         * - The `uid` that you specify in the {@link agora_gaming_rtc.IRtcEngine.JoinChannelByKey JoinChannelByKey} method is different from the `uid` that you pass for generating the token.
         */
        CONNECTION_CHANGED_INVALID_TOKEN = 8,
        /** 9: The connection failed since token is expired. */
        CONNECTION_CHANGED_TOKEN_EXPIRED = 9,
        /** 10: The connection is rejected by server. */
        CONNECTION_CHANGED_REJECTED_BY_SERVER = 10,
        /** 11: The connection changed to reconnecting since SDK has set a proxy server. */
        CONNECTION_CHANGED_SETTING_PROXY_SERVER = 11,
        /** 12: When SDK is in connection failed, the renew token operation will make it connecting. */
        CONNECTION_CHANGED_RENEW_TOKEN = 12,
        /** 13: The IP Address of SDK client has changed. i.e., Network type or IP/Port changed by network operator might change client IP address. */
        CONNECTION_CHANGED_CLIENT_IP_ADDRESS_CHANGED = 13,
        /** 14: Timeout for the keep-alive of the connection between the SDK and Agora's edge server. The connection state changes to `CONNECTION_STATE_RECONNECTING(4)`. */
        CONNECTION_CHANGED_KEEP_ALIVE_TIMEOUT = 14,
    };

    /** Stream fallback options. */
    public enum STREAM_FALLBACK_OPTIONS
    {
        /** 0: No fallback behavior for the local/remote video stream when the uplink/downlink network conditions are poor. The quality of the stream is not guaranteed. */
        STREAM_FALLBACK_OPTION_DISABLED = 0,
        /** 1: Under poor downlink network conditions, the remote video stream, to which you subscribe, falls back to the low-stream (low resolution and low bitrate) video. You can set this option only in the {@link agora_gaming_rtc.IRtcEngine.SetRemoteSubscribeFallbackOption SetRemoteSubscribeFallbackOption} method. Nothing happens when you set this in the {@link agora_gaming_rtc.IRtcEngine.SetLocalPublishFallbackOption SetLocalPublishFallbackOption} method. */
        STREAM_FALLBACK_OPTION_VIDEO_STREAM_LOW = 1,
        /** 2: Under poor uplink network conditions, the locally published video stream falls back to audio only.
         * 
         * Under poor downlink network conditions, the remote video stream, to which you subscribe, first falls back to the low-stream (low resolution and low bitrate) video; and then to an audio-only stream if the network conditions worsen.
         */
        STREAM_FALLBACK_OPTION_AUDIO_ONLY = 2,
    };

    /** Content hints for screen sharing.
    */
    public enum VideoContentHint
    {
        /** (Default) No content hint.
        */
        CONTENT_HINT_NONE = 0,
        /** Motion-intensive content. Choose this option if you prefer smoothness or when you are sharing a video clip, movie, or video game.
        */
        CONTENT_HINT_MOTION = 1,
        /** Motionless content. Choose this option if you prefer sharpness or when you are sharing a picture, PowerPoint slide, or text.
        */
        CONTENT_HINT_DETAILS = 2,
    };


    /** The reason of the remote video state change. */
    public enum REMOTE_VIDEO_STATE_REASON 
    {
        /** 0: Internal reasons.
        */
        REMOTE_VIDEO_STATE_REASON_INTERNAL = 0,

        /** 1: Network congestion.
        */
        REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION = 1,

        /** 2: Network recovery.
        */
        REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY = 2,

        /** 3: The local user stops receiving the remote video stream or disables the video module.
        */
        REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED = 3,

        /** 4: The local user resumes receiving the remote video stream or enables the video module.
        */
        REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED = 4,

        /** 5: The remote user stops sending the video stream or disables the video module.
        */
        REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED = 5,

        /** 6: The remote user resumes sending the video stream or enables the video module.
        */
        REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED = 6,

        /** 7: The remote user leaves the channel.
        */
        REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE = 7,

        /** 8: The remote media stream falls back to the audio-only stream due to poor network conditions.
        */
        REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK = 8,

        /** 9: The remote media stream switches back to the video stream after the network conditions improve.
        */
        REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY = 9

    };

    /** Local video state types.
    */
    public enum LOCAL_VIDEO_STREAM_STATE
    {
        /** 0: Initial state. */
        LOCAL_VIDEO_STREAM_STATE_STOPPED = 0,
        /** 1: The capturer starts successfully. */
        LOCAL_VIDEO_STREAM_STATE_CAPTURING = 1,
        /** 2: The first video frame is successfully encoded. */
        LOCAL_VIDEO_STREAM_STATE_ENCODING = 2,
        /** 3: The local video fails to start. */
        LOCAL_VIDEO_STREAM_STATE_FAILED = 3
    };

    /** Local video state error codes
    */
    public enum LOCAL_VIDEO_STREAM_ERROR 
    {
        /** 0: The local video is normal. */
        LOCAL_VIDEO_STREAM_ERROR_OK = 0,
        /** 1: No specified reason for the local video failure. */
        LOCAL_VIDEO_STREAM_ERROR_FAILURE = 1,
        /** 2: No permission to use the local video device. */
        LOCAL_VIDEO_STREAM_ERROR_DEVICE_NO_PERMISSION = 2,
        /** 3: The local video capturer is in use. */
        LOCAL_VIDEO_STREAM_ERROR_DEVICE_BUSY = 3,
        /** 4: The local video capture fails. Check whether the capturer is working properly. */
        LOCAL_VIDEO_STREAM_ERROR_CAPTURE_FAILURE = 4,
        /** 5: The local video encoding fails. */
        LOCAL_VIDEO_STREAM_ERROR_ENCODE_FAILURE = 5
    };

    /** Client roles in a live broadcast. */
    public enum CLIENT_ROLE_TYPE
    {
        /** 1: Host */
        CLIENT_ROLE_BROADCASTER = 1,
        /** 2: Audience */
        CLIENT_ROLE_AUDIENCE = 2,
    };

    /** Media device types. */
    public enum MEDIA_DEVICE_TYPE
    {
        /** -1: Unknown device type. */
        UNKNOWN_AUDIO_DEVICE = -1,
        /** 0: Audio playback device. */
        AUDIO_PLAYOUT_DEVICE = 0,
        /** 1: Audio recording device. */
        AUDIO_RECORDING_DEVICE = 1,
        /** 2: Video renderer. */
        VIDEO_RENDER_DEVICE = 2,
        /** 3: Video capturer. */
        VIDEO_CAPTURE_DEVICE = 3,
        /** 4: Application audio playback device. */
        AUDIO_APPLICATION_PLAYOUT_DEVICE = 4,
    };

    /** Use modes of the {@link agora_gaming_rtc.AudioRawDataManager.OnRecordAudioFrameHandler OnRecordAudioFrameHandler} callback. */
    public enum RAW_AUDIO_FRAME_OP_MODE_TYPE
    {
        /** 0: Read-only mode: Users only read the AudioFrame data without modifying anything. For example, when users acquire the data with the Agora SDK, then push the RTMP streams. */
        RAW_AUDIO_FRAME_OP_MODE_READ_ONLY = 0,
        /** 1: Write-only mode: Users replace the AudioFrame data with their own data and pass the data to the SDK for encoding. For example, when users acquire the data. */
        RAW_AUDIO_FRAME_OP_MODE_WRITE_ONLY = 1,
        /** 2: Read and write mode: Users read the data from AudioFrame, modify it, and then play it. For example, when users have their own sound-effect processing module and perform some voice pre-processing, such as a voice change. */
        RAW_AUDIO_FRAME_OP_MODE_READ_WRITE = 2,
    };

    /** Audio profiles.
     * 
     * Sets the sample rate, bitrate, encoding mode, and the number of channels.
     */
    public enum AUDIO_PROFILE_TYPE // sample rate, bit rate, mono/stereo, speech/music codec
    {
        /** 0: Default audio profile.
        * - In the Communication profile, the default value is `AUDIO_PROFILE_SPEECH_STANDARD(1)`;
        * - In the Live-broadcast profile, the default value is `AUDIO_PROFILE_MUSIC_STANDARD(2)`.
        */
        AUDIO_PROFILE_DEFAULT = 0, // use default settings
        /** 1: A sample rate of 32 kHz, audio encoding, mono, and a bitrate of up to 18 Kbps. */
        AUDIO_PROFILE_SPEECH_STANDARD = 1, // 32Khz, 18Kbps, mono, speech
        /** 2: A sample rate of 48 kHz, music encoding, mono, and a bitrate of up to 48 Kbps. */
        AUDIO_PROFILE_MUSIC_STANDARD = 2, // 48Khz, 48Kbps, mono, music
        /** 3: A sample rate of 48 kHz, music encoding, stereo, and a bitrate of up to 56 Kbps. */
        AUDIO_PROFILE_MUSIC_STANDARD_STEREO = 3, // 48Khz, 56Kbps, stereo, music
        /** 4: A sample rate of 48 kHz, music encoding, mono, and a bitrate of up to 128 Kbps. */
        AUDIO_PROFILE_MUSIC_HIGH_QUALITY = 4, // 48Khz, 128Kbps, mono, music
        /** 5: A sample rate of 48 kHz, music encoding, stereo, and a bitrate of up to 192 Kbps. */
        AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO = 5, // 48Khz, 192Kbps, stereo, music
        /** 6: A sample rate of 16 kHz, audio encoding, mono, and Acoustic Echo Cancellation (AES) enabled.  */
        AUDIO_PROFILE_IOT = 6,
        AUDIO_PROFILE_NUM = 7,
    };

    /** Audio application scenarios.*/
    public enum AUDIO_SCENARIO_TYPE // set a suitable scenario for your app type
    {
        /** 0: Default. */
        AUDIO_SCENARIO_DEFAULT = 0,
        /** 1: Entertainment scenario, supporting voice during gameplay. */
        AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT = 1,
        /** 2: Education scenario, prioritizing fluency and stability. */
        AUDIO_SCENARIO_EDUCATION = 2,
        /** 3: Live gaming scenario, enabling the gaming audio effects in the speaker mode in a live broadcast scenario. Choose this scenario for high-fidelity music playback. */
        AUDIO_SCENARIO_GAME_STREAMING = 3,
        /** 4: Showroom scenario, optimizing the audio quality with external professional equipment. */
        AUDIO_SCENARIO_SHOWROOM = 4,
        /** 5: Gaming scenario. */
        AUDIO_SCENARIO_CHATROOM_GAMING = 5,
        /** 6: Applicable to the IoT scenario. */
        AUDIO_SCENARIO_IOT = 6,
        AUDIO_SCENARIO_NUM = 7,
    };

    /** Video codec profile types. */
    public enum VIDEO_CODEC_PROFILE_TYPE
    {  
        /** 66: Baseline video codec profile. Generally used in video calls on mobile phones. */
        VIDEO_CODEC_PROFILE_BASELINE = 66,
        /** 77: Main video codec profile. Generally used in mainstream electronics such as MP4 players, portable video players, PSP, and iPads. */
        VIDEO_CODEC_PROFILE_MAIN = 77,
        /**  100: (Default) High video codec profile. Generally used in high-resolution broadcasts or television. */
        VIDEO_CODEC_PROFILE_HIGH = 100,
    };

    /** Audio-sample rates. */
    public enum AUDIO_SAMPLE_RATE_TYPE
    {
        /** 32000: 32 kHz */
        AUDIO_SAMPLE_RATE_32000 = 32000,
        /** 44100: 44.1 kHz */
        AUDIO_SAMPLE_RATE_44100 = 44100,
        /** 48000: 48 kHz */
        AUDIO_SAMPLE_RATE_48000 = 48000,
    };

    /** The states of the local user's audio mixing file. */
    public enum AUDIO_MIXING_STATE_TYPE
    {
        /** 710: The audio mixing file is playing.
        */
        AUDIO_MIXING_STATE_PLAYING = 710,
        /** 711: The audio mixing file pauses playing.
        */
        AUDIO_MIXING_STATE_PAUSED = 711,
        /** 713: The audio mixing file stops playing.
        */
        AUDIO_MIXING_STATE_STOPPED = 713,
        /** 714: An exception occurs when playing the audio mixing file. See #AUDIO_MIXING_ERROR_TYPE.
        */
        AUDIO_MIXING_STATE_FAILED = 714,
    };

    /** The error codes of the local user's audio mixing file.
    */
    public enum AUDIO_MIXING_ERROR_TYPE
    {
        /** 701: The SDK cannot open the audio mixing file.
        */
        AUDIO_MIXING_ERROR_CAN_NOT_OPEN = 701,
        /** 702: The SDK opens the audio mixing file too frequently.
        */
        AUDIO_MIXING_ERROR_TOO_FREQUENT_CALL = 702,
        /** 703: The opening of the audio mixing file is interrupted.
        */
        AUDIO_MIXING_ERROR_INTERRUPTED_EOF = 703,
        /** 0: The SDK can open the audio mixing file.
        */
        AUDIO_MIXING_ERROR_OK = 0,
    };

    /** States of the RTMP streaming.
    */
    public enum RTMP_STREAM_PUBLISH_STATE
    {
        /** 0: The RTMP streaming has not started or has ended.
        */
        RTMP_STREAM_PUBLISH_STATE_IDLE = 0,
        /** 1: The SDK is connecting to Agora's streaming server and the RTMP server. This state is triggered after you call the {@link agora_gaming_rtc.IRtcEngine.AddPublishStreamUrl AddPublishStreamUrl} method.
        */
        RTMP_STREAM_PUBLISH_STATE_CONNECTING = 1,
        /** 2: The RTMP streaming publishes. The SDK successfully publishes the RTMP streaming and returns this state.
        */
        RTMP_STREAM_PUBLISH_STATE_RUNNING = 2,
        /** 3: The RTMP streaming is recovering. When exceptions occur to the CDN, or the streaming is interrupted, the SDK tries to resume RTMP streaming and returns this state.
        * - If the SDK successfully resumes the streaming, `RTMP_STREAM_PUBLISH_STATE_RUNNING(2)` returns.
        * - If the streaming does not resume within 60 seconds or server errors occur, `RTMP_STREAM_PUBLISH_STATE_FAILURE(4)` returns. You can also reconnect to the server by calling the {@link agora_gaming_rtc.IRtcEngine.RemovePublishStreamUrl RemovePublishStreamUrl} and {@link agora_gaming_rtc.IRtcEngine.AddPublishStreamUrl AddPublishStreamUrl} methods.
        */
        RTMP_STREAM_PUBLISH_STATE_RECOVERING = 3,
        /** 4: The RTMP streaming fails. See the errCode parameter for the detailed error information. You can also call the {@link agora_gaming_rtc.IRtcEngine.AddPublishStreamUrl AddPublishStreamUrl} method to publish the RTMP streaming again.
        */
        RTMP_STREAM_PUBLISH_STATE_FAILURE = 4,
    };

    /** Error codes of the RTMP streaming.
    */
    public enum RTMP_STREAM_PUBLISH_ERROR
    {
        /** 0: The RTMP streaming publishes successfully. */
        RTMP_STREAM_PUBLISH_ERROR_OK = 0,
        /** 1: Invalid argument used. If, for example, you do not call the {@link agora_gaming_rtc.IRtcEngine.SetLiveTranscoding SetLiveTranscoding} method to configure the LiveTranscoding parameters before calling the addPublishStreamUrl method, the SDK returns this error. Check whether you set the parameters in the *setLiveTranscoding* method properly. */
        RTMP_STREAM_PUBLISH_ERROR_INVALID_ARGUMENT = 1,
        /** 2: The RTMP streaming is encrypted and cannot be published. */
        RTMP_STREAM_PUBLISH_ERROR_ENCRYPTED_STREAM_NOT_ALLOWED = 2,
        /** 3: Timeout for the RTMP streaming. Call the {@link agora_gaming_rtc.IRtcEngine.AddPublishStreamUrl AddPublishStreamUrl} method to publish the streaming again. */
        RTMP_STREAM_PUBLISH_ERROR_CONNECTION_TIMEOUT = 3,
        /** 4: An error occurs in Agora's streaming server. Call the {@link agora_gaming_rtc.IRtcEngine.AddPublishStreamUrl AddPublishStreamUrl} method to publish the streaming again. */
        RTMP_STREAM_PUBLISH_ERROR_INTERNAL_SERVER_ERROR = 4,
        /** 5: An error occurs in the RTMP server. */
        RTMP_STREAM_PUBLISH_ERROR_RTMP_SERVER_ERROR = 5,
        /** 6: The RTMP streaming publishes too frequently. */
        RTMP_STREAM_PUBLISH_ERROR_TOO_OFTEN = 6,
        /** 7: The host publishes more than 10 URLs. Delete the unnecessary URLs before adding new ones. */
        RTMP_STREAM_PUBLISH_ERROR_REACH_LIMIT = 7,
        /** 8: The host manipulates other hosts' URLs. Check your app logic. */
        RTMP_STREAM_PUBLISH_ERROR_NOT_AUTHORIZED = 8,
        /** 9: Agora's server fails to find the RTMP streaming. */
        RTMP_STREAM_PUBLISH_ERROR_STREAM_NOT_FOUND = 9,
        /** 10: The format of the RTMP streaming URL is not supported. Check whether the URL format is correct. */
        RTMP_STREAM_PUBLISH_ERROR_FORMAT_NOT_SUPPORTED = 10,
    };

    /** Network type. */
    public enum NETWORK_TYPE
    {
        /** -1: The network type is unknown. */
        NETWORK_TYPE_UNKNOWN = -1,
        /** 0: The SDK disconnects from the network. */
        NETWORK_TYPE_DISCONNECTED = 0,
        /** 1: The network type is LAN. */
        NETWORK_TYPE_LAN = 1,
        /** 2: The network type is Wi-Fi. */
        NETWORK_TYPE_WIFI = 2,
        /** 3: The network type is mobile 2G. */
        NETWORK_TYPE_MOBILE_2G = 3,
        /** 4: The network type is mobile 3G. */
        NETWORK_TYPE_MOBILE_3G = 4,
        /** 5: The network type is mobile 4G. */
        NETWORK_TYPE_MOBILE_4G = 5,
    };

    /** Local voice changer options. */
    public enum VOICE_CHANGER_PRESET 
    {
        /** 0: The original voice (no local voice change).
        */
        VOICE_CHANGER_OFF = 0, //Turn off the voice changer
        /** 1: An old man's voice.
        */
        VOICE_CHANGER_OLDMAN = 1,
        /** 2: A little boy's voice.
        */
        VOICE_CHANGER_BABYBOY = 2,
        /** 3: A little girl's voice.
        */
        VOICE_CHANGER_BABYGIRL = 3,
        /** 4: The voice of a growling bear.
        */
        VOICE_CHANGER_ZHUBAJIE = 4,
        /** 5: Ethereal vocal effects.
        */
        VOICE_CHANGER_ETHEREAL = 5,
        /** 6: Hulk's voice.
        */
        VOICE_CHANGER_HULK = 6
    };

    /** Local voice reverberation presets. */
    public enum AUDIO_REVERB_PRESET 
    {
        /** 0: The original voice (no local voice reverberation).
        */
        AUDIO_REVERB_OFF = 0, // Turn off audio reverb
        /** 1: Pop music.
        */
        AUDIO_REVERB_POPULAR = 1,
        /** 2: R&B.
        */
        AUDIO_REVERB_RNB = 2,
        /** 3: Rock music.
        */
        AUDIO_REVERB_ROCK = 3,
        /** 4: Hip-hop.
        */
        AUDIO_REVERB_HIPHOP = 4,
        /** 5: Pop concert.
        */
        AUDIO_REVERB_VOCAL_CONCERT = 5,
        /** 6: Karaoke.
        */
        AUDIO_REVERB_KTV = 6,
        /** 7: Recording studio.
        */
        AUDIO_REVERB_STUDIO = 7
    };

    /** Audio equalization band frequencies. */
    public enum AUDIO_EQUALIZATION_BAND_FREQUENCY
    {
        /** 0: 31 Hz */
        AUDIO_EQUALIZATION_BAND_31 = 0,
        /** 1: 62 Hz */
        AUDIO_EQUALIZATION_BAND_62 = 1,
        /** 2: 125 Hz */
        AUDIO_EQUALIZATION_BAND_125 = 2,
        /** 3: 250 Hz */
        AUDIO_EQUALIZATION_BAND_250 = 3,
        /** 4: 500 Hz */
        AUDIO_EQUALIZATION_BAND_500 = 4,
        /** 5: 1 kHz */
        AUDIO_EQUALIZATION_BAND_1K = 5,
        /** 6: 2 kHz */
        AUDIO_EQUALIZATION_BAND_2K = 6,
        /** 7: 4 kHz */
        AUDIO_EQUALIZATION_BAND_4K = 7,
        /** 8: 8 kHz */
        AUDIO_EQUALIZATION_BAND_8K = 8,
        /** 9: 16 kHz */
        AUDIO_EQUALIZATION_BAND_16K = 9,
    };

    /** Quality change of the local video in terms of target frame rate and target bit rate since last count.
    */
    public enum QUALITY_ADAPT_INDICATION 
    {
        /** The quality of the local video stays the same. */
        ADAPT_NONE = 0,
        /** The quality improves because the network bandwidth increases. */
        ADAPT_UP_BANDWIDTH = 1,
        /** The quality worsens because the network bandwidth decreases. */
        ADAPT_DOWN_BANDWIDTH = 2,
    };

    /** Audio reverberation types. */
    public enum AUDIO_REVERB_TYPE
    {
        /** 0: The level of the dry signal (db). The value is between -20 and 10. */
        AUDIO_REVERB_DRY_LEVEL = 0, // (dB, [-20,10]), the level of the dry signal
        /** 1: The level of the early reflection signal (wet signal) (dB). The value is between -20 and 10. */
        AUDIO_REVERB_WET_LEVEL = 1, // (dB, [-20,10]), the level of the early reflection signal (wet signal)
        /** 2: The room size of the reflection. The value is between 0 and 100. */
        AUDIO_REVERB_ROOM_SIZE = 2, // ([0,100]), the room size of the reflection
        /** 3: The length of the initial delay of the wet signal (ms). The value is between 0 and 200. */
        AUDIO_REVERB_WET_DELAY = 3, // (ms, [0,200]), the length of the initial delay of the wet signal in ms
        /** 4: The reverberation strength. The value is between 0 and 100. */
        AUDIO_REVERB_STRENGTH = 4, // ([0,100]), the strength of the reverberation
    };

    /** Audio codec profile types. The default value is LC_ACC. */
    public enum AUDIO_CODEC_PROFILE_TYPE
    {
        /** 0: LC-AAC, which is the low-complexity audio codec type. */
        AUDIO_CODEC_PROFILE_LC_AAC = 0,
        /** 1: HE-AAC, which is the high-efficiency audio codec type. */
        AUDIO_CODEC_PROFILE_HE_AAC = 1,
    };

    /** Video codec types */
    public enum VIDEO_CODEC_TYPE 
    {
        /** 1: Standard VP8. */
        VIDEO_CODEC_VP8 = 1,
        /** 2: Standard H264. */
        VIDEO_CODEC_H264 = 2,
        /** 3: Enhanced VP8. */
        VIDEO_CODEC_EVP = 3,
        /** 4: Enhanced H264. */
        VIDEO_CODEC_E264 = 4,
    };

    /** Statistics of the channel. */
    public struct RtcStats
    {
        /** Call duration (s), represented by an aggregate value. 
        */
        public uint duration;
        /** Total number of bytes transmitted, represented by an aggregate value.
        */
        public uint txBytes;
        /**Total number of bytes received, represented by an aggregate value.
        */
        public uint rxBytes;
        /** Total number of audio bytes sent (bytes), represented
        * by an aggregate value.
        */
        public uint txAudioBytes;
        /** Total number of video bytes sent (bytes), represented
        * by an aggregate value.
        */
        public uint txVideoBytes;
        /** Total number of audio bytes received (bytes) before
        * network countermeasures, represented by an aggregate value.
        */
        public uint rxAudioBytes;
        /** Total number of video bytes received (bytes),
        * represented by an aggregate value.
        */
        public uint rxVideoBytes;

        /** Transmission bitrate (Kbps), represented by an instantaneous value.
        */
        public uint txKBitRate;
        /** Receive bitrate (Kbps), represented by an instantaneous value.
        */
        public uint rxKBitRate;
        /** Audio receive bitrate (Kbps), represented by an instantaneous value.
        */
        public uint rxAudioKBitRate;
        /** Audio transmission bitrate (Kbps), represented by an instantaneous value.
        */
        public uint txAudioKBitRate;
        /** Video receive bitrate (Kbps), represented by an instantaneous value.
        */
        public uint rxVideoKBitRate;
        /** Video transmission bitrate (Kbps), represented by an instantaneous value.
        */
        public uint txVideoKBitRate;
        /** Client-server latency (ms)
        */
        public ushort lastmileDelay;
        /** The packet loss rate (%) from the local client to Agora's edge server,
        * before using the anti-packet-loss method.
        */
        public ushort txPacketLossRate;
        /** The packet loss rate (%) from Agora's edge server to the local client,
        * before using the anti-packet-loss method.
        */
        public ushort rxPacketLossRate;
        /** Number of users in the channel.
        * - Communication profile: The number of users in the channel.
        * - Live broadcast profile:
        *   -  If the local user is an audience: The number of users in the channel = The number of hosts in the channel + 1.
        *   -  If the user is a host: The number of users in the channel = The number of hosts in the channel.
        */
        public uint userCount;
        /** Application CPU usage (%).
        */
        public double cpuAppUsage;
        /** System CPU usage (%).
        */
        public double cpuTotalUsage;
    };
    /** Properties of the audio volume information.
     *
     * An array containing the user ID and volume information for each speaker.
     */
    public struct AudioVolumeInfo
    {
        /** User ID of the speaker. The `uid` of the local user is 0.
        */
        public uint uid;
        /** The volume of the speaker. The `volume` ranges between 0 (lowest volume) and 255 (highest volume).
        */
        public uint volume;
        /** Voice activity status of the local user.
        * - 0: The local user is not speaking.
        * - 1: The local user is speaking.
        * 
        * @note
        * - The `vad` parameter cannot report the voice activity status of the remote users. In the remote users' callback, `vad` = 0.
        * - Ensure that you set `report_vad`(true) in the {@link agora_gaming_rtc.IRtcEngine.EnableAudioVolumeIndication EnableAudioVolumeIndication} method to enable the voice activity detection of the local user.
        */
        public uint vad;
    };

    /** Statistics of the local video stream. */
    public struct LocalVideoStats
    {
        /** Bitrate (Kbps) sent in the reported interval, which does not include
        * the bitrate of the retransmission video after packet loss.
        */
        public int sentBitrate;
        /** Frame rate (fps) sent in the reported interval, which does not include
        * the frame rate of the retransmission video after packet loss.
        */
        public int sentFrameRate;
        /** The encoder output frame rate (fps) of the local video.
        */
        public int encoderOutputFrameRate;
        /** The render output frame rate (fps) of the local video.
        */
        public int rendererOutputFrameRate;
        /** The target bitrate (Kbps) of the current encoder. This value is estimated by the SDK based on the current network conditions.
        */
        public int targetBitrate;
        /** The target frame rate (fps) of the current encoder.
        */
        public int targetFrameRate;
        /** Quality change of the local video in terms of target frame rate and
        * target bit rate in this reported interval. See #QUALITY_ADAPT_INDICATION.
        */
        public QUALITY_ADAPT_INDICATION qualityAdaptIndication;
        /** The encoding bitrate (Kbps), which does not include the bitrate of the
        * re-transmission video after packet loss.
        */
        public int encodedBitrate;
        /** The width of the encoding frame (px).
        */
        public int encodedFrameWidth;
        /** The height of the encoding frame (px).
        */
        public int encodedFrameHeight;
        /** The value of the sent frames, represented by an aggregate value.
        */
        public int encodedFrameCount;
        /** The codec type of the local video:
        * - VIDEO_CODEC_VP8 = 1: VP8.
        * - VIDEO_CODEC_H264 = 2: (Default) H.264.
        */
        public VIDEO_CODEC_TYPE codecType;
    }; 

    /** Statistics of the remote video stream. */
    public struct RemoteVideoStats
    {
        /** User ID of the remote user sending the video streams. */
        public uint uid;
        /** **DEPRECATED** Time delay (ms). */
        public int delay;
        /** Width (pixels) of the video stream. */
        public int width;
        /** Height (pixels) of the video stream. */
        public int height;
        /** Bitrate (Kbps) received since the last count. */
        public int receivedBitrate;
        /** The decoder output frame rate (fps) of the remote video. */
        public int decoderOutputFrameRate;
        /** The render output frame rate (fps) of the remote video. */
        public int rendererOutputFrameRate;
        /** The remote video stream type, see #REMOTE_VIDEO_STREAM_TYPE.
        */
        public REMOTE_VIDEO_STREAM_TYPE rxStreamType;
        /** Packet loss rate (%) of the remote video stream after using the anti-packet-loss method. */
        public int packetLossRate;
        /** The total freeze time (ms) of the remote video stream after the remote user joins the channel.
         * In a video session where the frame rate is set to no less than 5 fps, video freeze occurs when the time interval between two adjacent renderable video frames is more than 500 ms.
         */
        public int totalFrozenTime;
        /** The total video freeze time as a percentage (%) of the total time when the video is available. */
        public int frozenRate;
    };

    public struct UserInfo 
    {
        public uint uid;
        public string userAccount;
    }

    /** Audio statistics of a remote user */
    public struct RemoteAudioStats
    {
        /** User ID of the remote user sending the audio streams. */
        public uint uid;
        /** Audio quality received by the user. */
        public int quality;
        /** Network delay (ms) from the sender to the receiver. */
        public int networkTransportDelay;
        /** Network delay (ms) from the receiver to the jitter buffer. */
        public int jitterBufferDelay;
        /** The audio frame loss rate in the reported interval. */
        public int audioLossRate;
        /** The number of channels. */
        public int numChannels;
        /** The sample rate (Hz) of the received audio stream in the reported interval. */
        public int receivedSampleRate;
        /** The average bitrate (Kbps) of the received audio stream in the reported interval. */
        public int receivedBitrate;
        /** The total freeze time (ms) of the remote audio stream after the remote user joins the channel. In a session, audio freeze occurs when the audio frame loss rate reaches 4%.
         * Agora uses 2 seconds as an audio piece unit to calculate the audio freeze time. The total audio freeze time = The audio freeze number &times; 2 seconds.
         */
        public int totalFrozenTime;
        /** The total audio freeze time as a percentage (%) of the total time when the audio is available. */
        public int frozenRate;
    };


    /** The options of the watermark image to be added. */
    public struct WatermarkOptions 
    {
        /** Sets whether or not the watermark image is visible in the local video preview: 
         * - true: The watermark image is visible in preview.
         * - false: The watermark image is not visible in preview. 
         */
        public bool visibleInPreview;
        /** The watermark position in the landscape mode. See Rectangle.
         * For detailed information on the landscape mode, see *Rotate the video*.
         */
        public Rectangle positionInLandscapeMode;
        /** The watermark position in the portrait mode. See Rectangle.
         * For detailed information on the portrait mode, see *Rotate the video*.
         */
        public Rectangle positionInPortraitMode;
    };

    /** Audio statistics of the local user. */
    public struct LocalAudioStats
    {
        /** The number of channels.
        */
        public int numChannels;
        /** The sample rate (Hz).
        */
        public int sentSampleRate;
        /** The average sending bitrate (Kbps).
        */
        public int sentBitrate;
    };

    /** Video encoder configurations. */
    public struct VideoEncoderConfiguration 
    {
        /** The video frame dimension used to specify the video quality and measured by the total number of pixels along a frame's width and height: VideoDimensions.
        */
        public VideoDimensions dimensions;
        /** The frame rate of the video: #FRAME_RATE.
         *
         * Note that we do not recommend setting this to a value greater than 30.
         */
        public FRAME_RATE frameRate;
        /** The minimum frame rate of the video. The default value is -1.
        */
        public int minFrameRate;
        /** The video encoding bitrate (Kbps).
         */
        public int bitrate;
        /** The minimum encoding bitrate (Kbps).
         * 
         * The SDK automatically adjusts the encoding bitrate to adapt to the network conditions. Using a value greater than the default value forces the video encoder to output high-quality images but may cause more packet loss and hence sacrifice the smoothness of the video transmission. That said, unless you have special requirements for image quality, Agora does not recommend changing this value.
         * 
         * @note This parameter applies only to the Live-broadcast profile.
         */
        public int minBitrate;
        /** The video orientation mode of the video: #ORIENTATION_MODE.
        */
        public ORIENTATION_MODE orientationMode;
        /** The video encoding degradation preference under limited bandwidth: #DEGRADATION_PREFERENCE.
        */
        public DEGRADATION_PREFERENCE degradationPreference;
    };

    /** Video dimensions. */
    public struct VideoDimensions 
    {
        /** Width (pixels) of the video. */
        public int width;
        /** Height (pixels) of the video. */
        public int height;
    };

    /** The video properties of the user displaying the video in the CDN live. Agora supports a maximum of 17 transcoding users in a CDN streaming channel. */
    public struct TranscodingUser
    {
        /** User ID of the user displaying the video in the CDN live.
        */
        public uint uid;

        /** Horizontal position from the top left corner of the video frame.
*/
        public int x;
        /** Vertical position from the top left corner of the video frame.
        */
        public int y;
        /** Width of the video frame. The default value is 360.
        */
        public int width;
        /** Height of the video frame. The default value is 640.
        */
        public int height;

        /** Layer position of the video frame. The value ranges between 0 and 100.
         * - 0: (Default) Lowest
         * - 100: Highest
         * 
         * @note
         * - If zOrder is beyond this range, the SDK reports `ERR_INVALID_ARGUMENT(2)`.
         * - As of v2.3, the SDK supports zOrder = 0.
         */
        public int zOrder;
        /**  Transparency of the video frame in CDN live. The value ranges between 0 and 1.0:
         * - 0: Completely transparent
         * - 1.0: (Default) Opaque
         */
        public double alpha;
        /** The audio channel of the sound. The default value is 0:
         * - 0: (Default) Supports dual channels at most, depending on the upstream of the broadcaster.
         * - 1: The audio stream of the broadcaster uses the FL audio channel. If the upstream of the broadcaster uses multiple audio channels, these channels will be mixed into mono first.
         * - 2: The audio stream of the broadcaster uses the FC audio channel. If the upstream of the broadcaster uses multiple audio channels, these channels will be mixed into mono first.
         * - 3: The audio stream of the broadcaster uses the FR audio channel. If the upstream of the broadcaster uses multiple audio channels, these channels will be mixed into mono first.
         * - 4: The audio stream of the broadcaster uses the BL audio channel. If the upstream of the broadcaster uses multiple audio channels, these channels will be mixed into mono first.
         * - 5: The audio stream of the broadcaster uses the BR audio channel. If the upstream of the broadcaster uses multiple audio channels, these channels will be mixed into mono first.
         * 
         * @note If your setting is not 0, you may need a specialized player.
         */
        public int audioChannel;
    };

    /** Image properties.
     * 
     * The properties of the watermark and background images.
     */
    public struct RtcImage
    {
        /** HTTP/HTTPS URL address of the image on the broadcasting video. The maximum length of this parameter is 1024 bytes. */
        public string url;
        /** Horizontal position of the image from the upper left of the broadcasting video. */
        public int x;
        /** Vertical position of the image from the upper left of the broadcasting video. */
        public int y;
        /** Width of the image on the broadcasting video. */
        public int width;
        /** Height of the image on the broadcasting video. */
        public int height;
    }

    /** A struct for managing CDN live audio/video transcoding settings. */
    public struct LiveTranscoding
    {
        /** Width of the video. The default value is 360. 
         * - If you push video streams to the CDN, set the value of width &times; height to at least 64 &times; 64 (px), or the SDK will adjust it to 64 &times; 64 (px).
         * - If you push audio streams to the CDN, set the value of width &times; height to 0 &times; 0 (px).
         */
        public int width;
        /** Height of the video. The default value is 640. 
         * - If you push video streams to the CDN, set the value of width &times; height to at least 64 &times; 64 (px), or the SDK will adjust it to 64 &times; 64 (px).
         * - If you push audio streams to the CDN, set the value of width &times; height to 0 &times; 0 (px).
         */
        public int height;
        /** Bitrate of the CDN live output video stream. The default value is 400 Kbps.
         * 
         * Set this parameter according to the Video Bitrate Table. If you set a bitrate beyond the proper range, the SDK automatically adapts it to a value within the range.
         */
        public int videoBitrate;
        /** Frame rate of the output video stream set for the CDN live broadcast. The default value is 15 fps, and the value range is (0,30].
         * 
         * @note Agora adjusts all values over 30 to 30.
         */
        public int videoFramerate;

        /** **DEPRECATED** Latency mode:
         * - true: Low latency with unassured quality.
         * - false: (Default) High latency with assured quality.
         */
        public bool lowLatency;

        /** Video GOP in frames. The default value is 30 fps.
        */
        public int videoGop;
        /** Self-defined video codec profile: #VIDEO_CODEC_PROFILE_TYPE.
         * 
         * @note If you set this parameter to other values, Agora adjusts it to the default value of 100.
         */
        public VIDEO_CODEC_PROFILE_TYPE videoCodecProfile;
        /** The background color in RGB hex value. Value only, do not include a #. For example, 0xFFB6C1 (light pink). The default value is 0x000000 (black).
         */
        public uint backgroundColor;
        /** The number of users in the live broadcast.
         */
        public uint userCount;
        /** TranscodingUser.
        */
        public TranscodingUser transcodingUsers;
        /** Reserved property. Extra user-defined information to send SEI for the H.264/H.265 video stream to the CDN live client. Maximum length: 4096 Bytes.
         *
         * For more information on SEI frame, see [SEI-related questions](https://docs.agora.io/en/faq/sei).
         */
        public string transcodingExtraInfo;

        /** **DEPRECATED** The metadata sent to the CDN live client defined by the RTMP or FLV metadata.
         */
        public string metadata;
        /** The watermark image added to the CDN live publishing stream.
         *
         * Ensure that the format of the image is PNG. Once a watermark image is added, the audience of the CDN live publishing stream can see the watermark image. See RtcImage.
         */
        public RtcImage watermark;
        /** The background image added to the CDN live publishing stream.
         * 
         * Once a background image is added, the audience of the CDN live publishing stream can see the background image. See RtcImage.
         */
        public RtcImage backgroundImage;
        /** Self-defined audio-sample rate: #AUDIO_SAMPLE_RATE_TYPE.
        */
        public AUDIO_SAMPLE_RATE_TYPE audioSampleRate;
        /** Bitrate of the CDN live audio output stream. The default value is 48 Kbps, and the highest value is 128.
         */
        public int audioBitrate;
        /** Agora's self-defined audio-channel types. We recommend choosing option 1 or 2. A special player is required if you choose option 3, 4, or 5:
         * - 1: (Default) Mono
         * - 2: Two-channel stereo
         * - 3: Three-channel stereo
         * - 4: Four-channel stereo
         * - 5: Five-channel stereo
         */
        public int audioChannels;
        /** Self-defined audio codec profile: #AUDIO_CODEC_PROFILE_TYPE.
         */
        public AUDIO_CODEC_PROFILE_TYPE audioCodecProfile;
    };

    /** Video frame rates. */
    public enum FRAME_RATE
    {
        /** 1: 1 fps */
        FRAME_RATE_FPS_1 = 1,
        /** 7: 7 fps */
        FRAME_RATE_FPS_7 = 7,
        /** 10: 10 fps */
        FRAME_RATE_FPS_10 = 10,
        /** 15: 15 fps */
        FRAME_RATE_FPS_15 = 15,
        /** 24: 24 fps */
        FRAME_RATE_FPS_24 = 24,
        /** 30: 30 fps */
        FRAME_RATE_FPS_30 = 30,
        /** 60: 60 fps (Windows and macOS only) */
        FRAME_RATE_FPS_60 = 60,
    };

    /** Video output orientation modes.
    */
    public enum ORIENTATION_MODE 
    {
        /** 0: (Default) Adaptive mode.
         * 
         * The video encoder adapts to the orientation mode of the video input device.
         * - If the width of the captured video from the SDK is greater than the height, the encoder sends the video in landscape mode. The encoder also sends the rotational information of the video, and the receiver uses the rotational information to rotate the received video.
         * - When you use a custom video source, the output video from the encoder inherits the orientation of the original video. If the original video is in portrait mode, the output video from the encoder is also in portrait mode. The encoder also sends the rotational information of the video to the receiver.
         */
        ORIENTATION_MODE_ADAPTIVE = 0,
        /** 1: Landscape mode.
         * 
         * The video encoder always sends the video in landscape mode. The video encoder rotates the original video before sending it and the rotational infomation is 0. This mode applies to scenarios involving CDN live streaming.
         */
        ORIENTATION_MODE_FIXED_LANDSCAPE = 1,
        /** 2: Portrait mode.
         * 
         * The video encoder always sends the video in portrait mode. The video encoder rotates the original video before sending it and the rotational infomation is 0. This mode applies to scenarios involving CDN live streaming.
         */
        ORIENTATION_MODE_FIXED_PORTRAIT = 2,
    };

    /** Video degradation preferences when the bandwidth is a constraint. */
    public enum DEGRADATION_PREFERENCE 
    {
        /** 0: (Default) Degrade the frame rate in order to maintain the video quality. */
        MAINTAIN_QUALITY = 0,
        /** 1: Degrade the video quality in order to maintain the frame rate. */
        MAINTAIN_FRAMERATE = 1,
        /** 2: (For future use) Maintain a balance between the frame rate and video quality. */
        MAINTAIN_BALANCED = 2,
    };

    /** The external video frame. */
    public struct ExternalVideoFrame
    {
        /** The video buffer type.
         */
        public enum VIDEO_BUFFER_TYPE
        {
            /** 1: The video buffer in the format of raw data.
             */
            VIDEO_BUFFER_RAW_DATA = 1,
        };

        /** The video pixel format.
         */
        public enum VIDEO_PIXEL_FORMAT
        {
            /** 0: The video pixel format is unknown.
             */
            VIDEO_PIXEL_UNKNOWN = 0,
            /** 1: The video pixel format is I420.
             */
            VIDEO_PIXEL_I420 = 1,
            /** 2: The video pixel format is BGRA.
             */
            VIDEO_PIXEL_BGRA = 2,
            /** 8: The video pixel format is NV12.
             */
            VIDEO_PIXEL_NV12 = 8,
            /** 16: The video pixel format is I422.
             */
            VIDEO_PIXEL_I422 = 16,
        };

        /** The buffer type. See #VIDEO_BUFFER_TYPE.
         */
        public VIDEO_BUFFER_TYPE type;
        /** The pixel format. See #VIDEO_PIXEL_FORMAT.
         */
        public VIDEO_PIXEL_FORMAT format;
        /** The video buffer.
         */
        public byte[] buffer;
        /** Line spacing of the incoming video frame, which must be in pixels instead of bytes. For textures, it is the width of the texture.
         */
        public int stride;
        /** Height of the incoming video frame.
         */
        public int height;
        /** [Raw data related parameter] The number of pixels trimmed from the left. The default value is 0.
         */
        public int cropLeft;
        /** [Raw data related parameter] The number of pixels trimmed from the top. The default value is 0.
         */
        public int cropTop;
        /** [Raw data related parameter] The number of pixels trimmed from the right. The default value is 0.
         */
        public int cropRight;
        /** [Raw data related parameter] The number of pixels trimmed from the bottom. The default value is 0.
         */
        public int cropBottom;
        /** [Raw data related parameter] The clockwise rotation of the video frame. You can set the rotation angle as 0, 90, 180, or 270. The default value is 0.
         */
        public int rotation;
        /** Timestamp of the incoming video frame (ms). An incorrect timestamp results in frame loss or unsynchronized audio and video.
         */
        public long timestamp;
    };

    /** The video frame type. */
    public enum VIDEO_FRAME_TYPE 
    {
        /** 0: YUV420. */
        FRAME_TYPE_YUV420 = 0,  //YUV 420 format
        /** 1: RGBA. */
        FRAME_TYPE_RGBA = 1, //RGBA
    };

    
    /** Video frame containing the Agora SDK's encoded video data. */
    public struct VideoFrame 
    {
        /** The video frame type: #VIDEO_FRAME_TYPE. */
        public VIDEO_FRAME_TYPE type;
        /** Width (pixel) of the video frame.*/
        public int width;  
        /** Height (pixel) of the video frame. */
        public int height;  
        /** Line span of the Y buffer within the video data. */
        public int yStride;  //stride of  data buffer
        /** The buffer of the RGBA data. */
        public byte[] buffer;  //rgba data buffer
        /** Set the rotation of this frame before rendering the video. Supports 0, 90, 180, 270 degrees clockwise.
         */
        public int rotation; // rotation of this frame (0, 90, 180, 270)
        /** The timestamp of the external audio frame. It is mandatory. You can use this parameter for the following purposes:
         * - Restore the order of the captured audio frame.
         * - Synchronize audio and video frames in video-related scenarios, including scenarios where external video sources are used.
         *
         * @note This timestamp is for rendering the video stream, and not for capturing the video stream.
         */
        public long renderTimeMs;
        /** Reserved parameter. */
        public int avsync_type;
    };

    /** The audio frame type. */
    public enum AUDIO_FRAME_TYPE 
    {
        /** 0: PCM16. */
        FRAME_TYPE_PCM16 = 0,  //PCM 16bit little endian
    };

    /** Definition of AudioFrame. */
    public struct AudioFrame 
    {
        /** The type of the audio frame. See #AUDIO_FRAME_TYPE
         */
        public AUDIO_FRAME_TYPE type;
        /** The number of samples per channel in the audio frame.
         */
        public int samples;  //number of samples in this frame
        /** The number of bytes per audio sample, which is usually 16-bit (2-byte).
         */
        public int bytesPerSample;  //number of bytes per sample: 2 for PCM16
        /** The number of audio channels.
         * - 1: Mono
         * - 2: Stereo (the data is interleaved)
         */
        public int channels;  //number of channels (data are interleaved if stereo)
        /** The sample rate.
         */
        public int samplesPerSec;  //sampling rate
        /** The data buffer of the audio frame. When the audio frame uses a stereo channel, the data buffer is interleaved. 
         * The size of the data buffer is as follows: `buffer` = `samples` × `channels` × `bytesPerSample`.
         */
        public byte[] buffer;  //data buffer
        /** The timestamp of the external audio frame. You can use this parameter for the following purposes:
         * - Restore the order of the captured audio frame.
         * - Synchronize audio and video frames in video-related scenarios, including where external video sources are used.
         */
        public long renderTimeMs;
        /** Reserved parameter.
         */
        public int avsync_type;
    };

    /** **DEPRECATED** Type of audio device.
    */
    public enum MEDIA_SOURCE_TYPE 
    {
        /** 0: Audio playback device.
        */
        AUDIO_PLAYOUT_SOURCE = 0,
        /** 1: Microphone.
         */
        AUDIO_RECORDING_SOURCE = 1,
    };

    /** States of the last-mile network probe test. */
    public enum LASTMILE_PROBE_RESULT_STATE 
    {
        /** 1: The last-mile network probe test is complete. */ 
        LASTMILE_PROBE_RESULT_COMPLETE = 1,   
        /** 2: The last-mile network probe test is incomplete and the bandwidth estimation is not available, probably due to limited test resources. */
        LASTMILE_PROBE_RESULT_INCOMPLETE_NO_BWE = 2,
        /** 3: The last-mile network probe test is not carried out, probably due to poor network conditions. */
        LASTMILE_PROBE_RESULT_UNAVAILABLE = 3  
    }; 

    /** The uplink or downlink last-mile network probe test result. */
    public struct LastmileProbeOneWayResult 
    {
        /** The packet loss rate (%). */  
        public uint packetLossRate;
        /** The network jitter (ms). */
        public uint jitter;
        /** The estimated available bandwidth (Kbps). */
        public uint availableBandwidth;
    };

    /** The uplink and downlink last-mile network probe test result. */
    public struct LastmileProbeResult 
    { 
        /** The state of the probe test. */  
        public LASTMILE_PROBE_RESULT_STATE state; 
        /** The uplink last-mile network probe test result. */
        public LastmileProbeOneWayResult uplinkReport; 
        /** The downlink last-mile network probe test result. */   
        public LastmileProbeOneWayResult downlinkReport;    
        /** The round-trip delay time (ms). */
        public uint rtt;
    }; 

    /** The camera direction: #CAMERA_DIRECTION. */
    public enum CAMERA_DIRECTION 
    {
        /** 0: The rear camera. */
        CAMERA_REAR = 0,
        /** 1: The front camera. */
        CAMERA_FRONT = 1,
    };

     /** Camera capturer configuration.
     */
    public struct CameraCapturerConfiguration
    {

        /** Camera capturer preference settings.See: #CAPTURER_OUTPUT_PREFERENCE. */
        public CAPTURER_OUTPUT_PREFERENCE preference;
        /** Camera direction settings (for Android/iOS only). See: #CAMERA_DIRECTION. */
        public CAMERA_DIRECTION cameraDirection ;
    };

     /** Camera capturer configuration.
 */ 
    public enum CAPTURER_OUTPUT_PREFERENCE
    {
        /** 0: (Default) self-adapts the camera output parameters to the system performance and network conditions to balance CPU consumption and video preview quality.
        */
        CAPTURER_OUTPUT_PREFERENCE_AUTO = 0,
        /** 1: Prioritizes the system performance. The SDK chooses the dimension and frame rate of the local camera capture closest to those set by {@link agora_gaming_rtc.IRtcEngine.SetVideoEncoderConfiguration SetVideoEncoderConfiguration}.
        */
        CAPTURER_OUTPUT_PREFERENCE_PERFORMANCE = 1,
        /** 2: Prioritizes the local preview quality. The SDK chooses higher camera output parameters to improve the local video preview quality. This option requires extra CPU and RAM usage for video pre-processing.
        */
        CAPTURER_OUTPUT_PREFERENCE_PREVIEW = 2,
    };
    

    /** Network quality types. */
    public enum QUALITY_TYPE
    {
        /** 0: The network quality is unknown. */
        QUALITY_UNKNOWN = 0,
        /**  1: The network quality is excellent. */
        QUALITY_EXCELLENT = 1,
        /** 2: The network quality is quite good, but the bitrate may be slightly lower than excellent. */
        QUALITY_GOOD = 2,
        /** 3: Users can feel the communication slightly impaired. */
        QUALITY_POOR = 3,
        /** 4: Users cannot communicate smoothly. */
        QUALITY_BAD = 4,
        /** 5: The network is so bad that users can barely communicate. */
        QUALITY_VBAD = 5,
        /** 6: The network is down and users cannot communicate at all. */
        QUALITY_DOWN = 6,
        /** 7: Users cannot detect the network quality. (Not in use.) */
        QUALITY_UNSUPPORTED = 7,
        /** 8: Detecting the network quality. */
        QUALITY_DETECTING = 8,
    };

    /** Media device states.
    */
    public enum MEDIA_DEVICE_STATE_TYPE
    {
    /** 1: The device is active.
    */
        MEDIA_DEVICE_STATE_ACTIVE = 1,
        /** 2: The device is disabled.
        */
        MEDIA_DEVICE_STATE_DISABLED = 2,
        /** 4: The device is not present.
        */
        MEDIA_DEVICE_STATE_NOT_PRESENT = 4,
        /** 8: The device is unplugged.
        */
        MEDIA_DEVICE_STATE_UNPLUGGED = 8
    };

    /** States of importing an external video stream in a live broadcast. */
    public enum INJECT_STREAM_STATUS
    {
        /** 0: The external video stream imported successfully. */
        INJECT_STREAM_STATUS_START_SUCCESS = 0,
        /** 1: The external video stream already exists. */
        INJECT_STREAM_STATUS_START_ALREADY_EXISTS = 1,
        /** 2: The external video stream to be imported is unauthorized. */
        INJECT_STREAM_STATUS_START_UNAUTHORIZED = 2,
        /** 3: Import external video stream timeout. */
        INJECT_STREAM_STATUS_START_TIMEDOUT = 3,
        /** 4: Import external video stream failed. */
        INJECT_STREAM_STATUS_START_FAILED = 4,
        /** 5: The external video stream stopped importing successfully. */
        INJECT_STREAM_STATUS_STOP_SUCCESS = 5,
        /** 6: No external video stream is found. */
        INJECT_STREAM_STATUS_STOP_NOT_FOUND = 6,
        /** 7: The external video stream to be stopped importing is unauthorized. */
        INJECT_STREAM_STATUS_STOP_UNAUTHORIZED = 7,
        /** 8: Stop importing external video stream timeout. */
        INJECT_STREAM_STATUS_STOP_TIMEDOUT = 8,
        /** 9: Stop importing external video stream failed. */
        INJECT_STREAM_STATUS_STOP_FAILED = 9,
        /** 10: The external video stream is corrupted. */
        INJECT_STREAM_STATUS_BROKEN = 10,
    };

    /** The priority of the remote user.
    */
    public enum PRIORITY_TYPE
    {
        /** 50: The user's priority is high.
        */
        PRIORITY_HIGH = 50,
        /** 100: (Default) The user's priority is normal.
        */
        PRIORITY_NORMAL = 100,
    };

    /** Configurations of the last-mile network probe test. */
    public struct LastmileProbeConfig 
    {
        /** Sets whether or not to test the uplink network. Some users, for example, the audience in a Live-broadcast channel, do not need such a test:
         * - true: test.
         * - false: do not test.
         */
        public bool probeUplink;
        /** Sets whether or not to test the downlink network:
         * - true: test.
         * - false: do not test. 
         */
        public bool probeDownlink;
        /** The expected maximum sending bitrate (Kbps) of the local user. The value ranges between 100 and 5000. We recommend setting this parameter according to the bitrate value set by {@link agora_gaming_rtc.IRtcEngine.SetVideoEncoderConfiguration SetVideoEncoderConfiguration}. */
        public uint expectedUplinkBitrate;
        /** The expected maximum receiving bitrate (Kbps) of the local user. The value ranges between 100 and 5000. */
        public uint expectedDownlinkBitrate;
    };

        /** **DEPRECATED** */
    public struct PublisherConfiguration 
    {
        /** Width of the CDN live output video stream. The default value is 360.
        */
        public int width;
        /** Height of the CDN live output video stream. The default value is 640.
        */
        public int height;
        /** Frame rate of the CDN live output video stream. The default value is 15 fps.
        */
        public int framerate;
        /** Bitrate of the CDN live output video stream. The default value is 500 Kbps.
        */
        public int bitrate;
        /** Default layout:
         * - 0: Tile horizontally
         * - 1: Layered windows
         * - 2: Tile vertically
         */
        public int defaultLayout;
        /** Video stream lifecycle of CDN live.
        */
        public int lifecycle;
        /** Whether or not the current user is the owner of the RTMP stream:
         * - true: (Default) Yes. The push-stream configuration takes effect.
         * - false: No. The push-stream configuration does not work.
         */
        public bool owner;
        /** Width of the injected stream. N/A. Set it as 0.
        */
        public int injectStreamWidth;
        /** Height of the injected stream. N/A. Set it as 0.
        */
        public int injectStreamHeight;
        /** URL address of the injected stream in the channel. N/A.
        */
        public string injectStreamUrl;
        /** Push-stream URL address for the picture-in-picture layout. The default value is NULL.
        */
        public string publishUrl;
        /** The push-stream URL address of the original stream that does not require picture-blending. The default value is NULL.
        */
        public string rawStreamUrl;
        /** Reserved field. The default value is NULL.
        */
        public string extraInfo;
    }

    /** Definition of Packet. */
    public struct Packet
	{
        /** Buffer address of the sent or received data.
         * @note Agora recommends that the value of buffer is more than 2048 bytes, otherwise, you may meet undefined behaviors such as a crash.
         */
		public IntPtr buffer;
        /** Buffer size of the sent or received data.
         */
		public IntPtr size;
	};

    /** Local audio state types.
    */
    public enum LOCAL_AUDIO_STREAM_STATE
    {
        /** 0: The local audio is in the initial state.
        */
        LOCAL_AUDIO_STREAM_STATE_STOPPED = 0,
        /** 1: The recording device starts successfully.
        */
        LOCAL_AUDIO_STREAM_STATE_RECORDING = 1,
        /** 2: The first audio frame encodes successfully.
        */
        LOCAL_AUDIO_STREAM_STATE_ENCODING = 2,
        /** 3: The local audio fails to start.
        */
        LOCAL_AUDIO_STREAM_STATE_FAILED = 3
    };

    /** Local audio state error codes.
    */
    public enum LOCAL_AUDIO_STREAM_ERROR
    {
        /** 0: The local audio is normal.
        */
        LOCAL_AUDIO_STREAM_ERROR_OK = 0,
        /** 1: No specified reason for the local audio failure.
        */
        LOCAL_AUDIO_STREAM_ERROR_FAILURE = 1,
        /** 2: No permission to use the local audio device.
        */
        LOCAL_AUDIO_STREAM_ERROR_DEVICE_NO_PERMISSION = 2,
        /** 3: The microphone is in use.
        */
        LOCAL_AUDIO_STREAM_ERROR_DEVICE_BUSY = 3,
        /** 4: The local audio recording fails. Check whether the recording device
        * is working properly.
        */
        LOCAL_AUDIO_STREAM_ERROR_RECORD_FAILURE = 4,
        /** 5: The local audio encoding fails.
        */
        LOCAL_AUDIO_STREAM_ERROR_ENCODE_FAILURE = 5
    };


    /** Remote audio states.
    */
    public enum REMOTE_AUDIO_STATE
    {
        /** 0: The remote audio is in the default state, probably due to
        * `REMOTE_AUDIO_REASON_LOCAL_MUTED(3)`,
        * `REMOTE_AUDIO_REASON_REMOTE_MUTED(5)`, or
        * `REMOTE_AUDIO_REASON_REMOTE_OFFLINE(7)`.
        */
        REMOTE_AUDIO_STATE_STOPPED = 0,  // Default state, audio is started or remote user disabled/muted audio stream
        /** 1: The first remote audio packet is received.
        */
        REMOTE_AUDIO_STATE_STARTING = 1,  // The first audio frame packet has been received
        /** 2: The remote audio stream is decoded and plays normally, probably
        * due to `REMOTE_AUDIO_REASON_NETWORK_RECOVERY(2)`,
        * `REMOTE_AUDIO_REASON_LOCAL_UNMUTED`(4)`, or
        * `REMOTE_AUDIO_REASON_REMOTE_UNMUTED(6)`.
        */
        REMOTE_AUDIO_STATE_DECODING = 2,  // The first remote audio frame has been decoded or fronzen state ends
        /** 3: The remote audio is frozen, probably due to
        * `REMOTE_AUDIO_REASON_NETWORK_CONGESTION(1)`.
        */
        REMOTE_AUDIO_STATE_FROZEN = 3,    // Remote audio is frozen, probably due to network issue
        /** 4: The remote audio fails to start, probably due to
        * `REMOTE_AUDIO_REASON_INTERNAL(0)`.
        */
        REMOTE_AUDIO_STATE_FAILED = 4,    // Remote audio play failed
    };

    /** Remote audio state reasons.
    */
    public enum REMOTE_AUDIO_STATE_REASON
    {
        /** 0: Internal reasons.
        */
        REMOTE_AUDIO_REASON_INTERNAL = 0,
        /** 1: Network congestion.
        */
        REMOTE_AUDIO_REASON_NETWORK_CONGESTION = 1,
        /** 2: Network recovery.
        */
        REMOTE_AUDIO_REASON_NETWORK_RECOVERY = 2,
        /** 3: The local user stops receiving the remote audio stream or
        * disables the audio module.
        */
        REMOTE_AUDIO_REASON_LOCAL_MUTED = 3,
        /** 4: The local user resumes receiving the remote audio stream or
        * enables the audio module.
        */
        REMOTE_AUDIO_REASON_LOCAL_UNMUTED = 4,
        /** 5: The remote user stops sending the audio stream or disables the
        * audio module.
        */
        REMOTE_AUDIO_REASON_REMOTE_MUTED = 5,
        /** 6: The remote user resumes sending the audio stream or enables the
        * audio module.
        */
        REMOTE_AUDIO_REASON_REMOTE_UNMUTED = 6,
        /** 7: The remote user leaves the channel.
        */
        REMOTE_AUDIO_REASON_REMOTE_OFFLINE = 7,
    };

    /** Image enhancement options. */
    public struct BeautyOptions {
        /** The contrast level, used with the @p lightening parameter.
        */
        public enum LIGHTENING_CONTRAST_LEVEL
        {
            /** Low contrast level. */
            LIGHTENING_CONTRAST_LOW = 0,
            /** (Default) Normal contrast level. */
            LIGHTENING_CONTRAST_NORMAL,
            /** High contrast level. */
            LIGHTENING_CONTRAST_HIGH
        };

        /** The contrast level, used with the `lightening` parameter.
        */
        public LIGHTENING_CONTRAST_LEVEL lighteningContrastLevel;

        /** The brightness level. The value ranges from 0.0 (original) to 1.0. */
        public float lighteningLevel;

        /** The sharpness level. The value ranges between 0 (original) and 1. This parameter is usually used to remove blemishes.
        */
        public float smoothnessLevel;

        /** The redness level. The value ranges between 0 (original) and 1. This parameter adjusts the red saturation level.
        */
        public float rednessLevel;
    }

    /** The relative location of the region to the screen or window. */
    public struct Rectangle
    {
        /** The horizontal offset from the top-left corner.
        */
        public int x;
        /** The vertical offset from the top-left corner.
        */
        public int y;
        /** The width of the region.
        */
        public int width;
        /** The height of the region.
        */
        public int height;
    };

    /** Screen sharing encoding parameters.
    */
    public struct ScreenCaptureParameters
    {
        /** The maximum encoding dimensions of the shared region in terms of width &times; height.
         * 
         * The default value is 1920 &times; 1080 pixels, that is, 2073600 pixels. Agora uses the value of this parameter to calculate the charges.
         * 
         * If the aspect ratio is different between the encoding dimensions and screen dimensions, Agora applies the following algorithms for encoding. Suppose the encoding dimensions are 1920 x 1080:
         * - If the value of the screen dimensions is lower than that of the encoding dimensions, for example, 1000 &times; 1000, the SDK uses 1000 &times; 1000 for encoding.
         * - If the value of the screen dimensions is higher than that of the encoding dimensions, for example, 2000 &times; 1500, the SDK uses the maximum value under 1920 &times; 1080 with the aspect ratio of the screen dimension (4:3) for encoding, that is, 1440 &times; 1080.
         */
        public VideoDimensions dimensions;
        /** The frame rate (fps) of the shared region.
         * 
         * The default value is 5. We do not recommend setting this to a value greater than 15.
         */
        public int frameRate;
        /** The bitrate (Kbps) of the shared region.
         * 
         * The default value is 0 (the SDK works out a bitrate according to the dimensions of the current screen).
         */
        public int bitrate;
        /** Sets whether or not to capture the mouse for screen sharing:
         * - true: (Default) Capture the mouse.
         * - false: Do not capture the mouse.
         */
        public bool captureMouseCursor;
    };

    /** Configuration of the imported live broadcast voice or video stream.
    */
    public struct InjectStreamConfig 
    {
        /** Width of the added stream in the live broadcast. The default value is 0 (same width as the original stream).
        */
        public int width;
        /** Height of the added stream in the live broadcast. The default value is 0 (same height as the original stream).
        */
        public int height;
        /** Video GOP of the added stream in the live broadcast in frames. The default value is 30 fps.
        */
        public int videoGop;
        /** Video frame rate of the added stream in the live broadcast. The default value is 15 fps.
        */
        public int videoFramerate;
        /** Video bitrate of the added stream in the live broadcast. The default value is 400 Kbps.
        *
        * @note The setting of the video bitrate is closely linked to the resolution. If the video bitrate you set is beyond a reasonable range, the SDK sets it within a reasonable range.
        */
        public int videoBitrate;
        /** Audio-sample rate of the added stream in the live broadcast: #AUDIO_SAMPLE_RATE_TYPE. The default value is 48000 Hz.
        *
        * @note We recommend setting the default value.
        */
        public AUDIO_SAMPLE_RATE_TYPE audioSampleRate;
        /** Audio bitrate of the added stream in the live broadcast. The default value is 48.
        *
        * @note We recommend setting the default value.
        */
        public int audioBitrate;
        /** Audio channels in the live broadcast.
        * - 1: (Default) Mono
        * - 2: Two-channel stereo
        *
        * @note We recommend setting the default value.
        */
        public int audioChannels;
    };

    /** Audio session restriction. */
    public enum AUDIO_SESSION_OPERATION_RESTRICTION 
    {
        /** No restriction, the SDK has full control of the audio session operations. */
        AUDIO_SESSION_OPERATION_RESTRICTION_NONE = 0,
        /** The SDK does not change the audio session category. */
        AUDIO_SESSION_OPERATION_RESTRICTION_SET_CATEGORY = 1,
        /** The SDK does not change any setting of the audio session (category, mode, categoryOptions). */
        AUDIO_SESSION_OPERATION_RESTRICTION_CONFIGURE_SESSION = 1 << 1,
        /** The SDK keeps the audio session active when leaving a channel. */
        AUDIO_SESSION_OPERATION_RESTRICTION_DEACTIVATE_SESSION = 1 << 2,
        /** The SDK does not configure the audio session anymore. */
        AUDIO_SESSION_OPERATION_RESTRICTION_ALL = 1 << 7,
    };

    /** The definition of ChannelMediaRelayConfiguration.
    */
    public struct ChannelMediaRelayConfiguration 
    {
        /** the information of the source channel: ChannelMediaInfo. It contains the following members:
         * - `channelName`: The name of the source channel. The default value is `NULL`, which means the SDK applies the name of the current channel.
         * - `uid`: ID of the broadcaster whose media stream you want to relay. The default value is 0, which means the SDK generates a random UID. You must set it as 0.
         * - `token`: The token for joining the source channel. It is generated with the `channelName` and `uid` you set in `srcInfo`.
         *   - If you have not enabled the App Certificate, set this parameter as the default value `NULL`, which means the SDK applies the App ID.
         *   - If you have enabled the App Certificate, you must use the `token` generated with the `channelName` and `uid`, and the `uid` must be set as 0.
         */
        public ChannelMediaInfo srcInfo;
        /** the information of the destination channel: ChannelMediaInfo. It contains the following members:
         * - `channelName`: The name of the destination channel.
         * - `uid`: ID of the broadcaster in the destination channel. The value ranges from 0 to (2<sup>32</sup>-1). To avoid UID conflicts, this `uid` must be different from any other UIDs in the destination channel. The default value is 0, which means the SDK generates a random UID.
         * - `token`: The token for joining the destination channel. It is generated with the `channelName` and `uid` you set in `destInfos`.
         *   - If you have not enabled the App Certificate, set this parameter as the default value `NULL`, which means the SDK applies the App ID.
         *   - If you have enabled the App Certificate, you must use the `token` generated with the `channelName` and `uid`.
         */
        public ChannelMediaInfo destInfos;
        /** The number of destination channels. The default value is 0, and the
        * value range is [0,4). Ensure that the value of this parameter
        * corresponds to the number of ChannelMediaInfo structs you define in
        * `destInfos`.
        */
        public int destCount;
    };

    /** The definition of ChannelMediaInfo.
    */
    public struct ChannelMediaInfo 
    {
        /** The channel name. 
        */
        public string channelName;
        /** The token that enables the user to join the channel.
        */
        public string token;
        /** The user ID.
        */
        public uint uid;
    };

    /** The event code in CHANNEL_MEDIA_RELAY_EVENT. */
    public enum CHANNEL_MEDIA_RELAY_EVENT 
    {
        /** 0: The user disconnects from the server due to poor network
        * connections.
        */
        RELAY_EVENT_NETWORK_DISCONNECTED = 0,
        /** 1: The network reconnects.
        */
        RELAY_EVENT_NETWORK_CONNECTED = 1,
        /** 2: The user joins the source channel.
        */
        RELAY_EVENT_PACKET_JOINED_SRC_CHANNEL = 2,
        /** 3: The user joins the destination channel.
        */
        RELAY_EVENT_PACKET_JOINED_DEST_CHANNEL = 3,
        /** 4: The SDK starts relaying the media stream to the destination channel.
        */
        RELAY_EVENT_PACKET_SENT_TO_DEST_CHANNEL = 4,
        /** 5: The server receives the video stream from the source channel.
        */
        RELAY_EVENT_PACKET_RECEIVED_VIDEO_FROM_SRC = 5,
        /** 6: The server receives the audio stream from the source channel.
        */
        RELAY_EVENT_PACKET_RECEIVED_AUDIO_FROM_SRC = 6,
        /** 7: The destination channel is updated.
        */
        RELAY_EVENT_PACKET_UPDATE_DEST_CHANNEL = 7,
        /** 8: The destination channel update fails due to internal reasons.
        */
        RELAY_EVENT_PACKET_UPDATE_DEST_CHANNEL_REFUSED = 8,
        /** 9: The destination channel does not change, which means that the
        * destination channel fails to be updated.
        */
        RELAY_EVENT_PACKET_UPDATE_DEST_CHANNEL_NOT_CHANGE = 9,
        /** 10: The destination channel name is NULL.
        */
        RELAY_EVENT_PACKET_UPDATE_DEST_CHANNEL_IS_NULL = 10,
        /** 11: The video profile is sent to the server.
        */
        RELAY_EVENT_VIDEO_PROFILE_UPDATE = 11,
    };

    /** The state code in CHANNEL_MEDIA_RELAY_STATE. */
    public enum CHANNEL_MEDIA_RELAY_STATE 
    {
        /** 0: The SDK is initializing.
        */
        RELAY_STATE_IDLE = 0,
        /** 1: The SDK tries to relay the media stream to the destination channel.
        */
        RELAY_STATE_CONNECTING = 1,
        /** 2: The SDK successfully relays the media stream to the destination
        * channel.
        */
        RELAY_STATE_RUNNING = 2,
        /** 3: A failure occurs. See the details in code.
        */
        RELAY_STATE_FAILURE = 3,
    };


    /** **DEPRECATED** Video profiles. */
    public enum VIDEO_PROFILE_TYPE
    {
        /** 0: 160 &times; 120, frame rate 15 fps, bitrate 65 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_120P = 0,
        /** 2: 120 &times; 120, frame rate 15 fps, bitrate 50 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_120P_3 = 2,
        /** 10: 320&times;180, frame rate 15 fps, bitrate 140 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_180P = 10,
        /** 12: 180 &times; 180, frame rate 15 fps, bitrate 100 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_180P_3 = 12,
        /** 13: 240 &times; 180, frame rate 15 fps, bitrate 120 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_180P_4 = 13,
        /** 20: 320 &times; 240, frame rate 15 fps, bitrate 200 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_240P = 20,
        /** 22: 240 &times; 240, frame rate 15 fps, bitrate 140 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_240P_3 = 22,
        /** 23: 424 &times; 240, frame rate 15 fps, bitrate 220 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_240P_4 = 23,
        /** 30: 640 &times; 360, frame rate 15 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P = 30,
        /** 32: 360 &times; 360, frame rate 15 fps, bitrate 260 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P_3 = 32,
        /** 33: 640 &times; 360, frame rate 30 fps, bitrate 600 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P_4 = 33,
        /** 35: 360 &times; 360, frame rate 30 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P_6 = 35,
        /** 36: 480 &times; 360, frame rate 15 fps, bitrate 320 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P_7 = 36,
        /** 37: 480 &times; 360, frame rate 30 fps, bitrate 490 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_360P_8 = 37,
        /** 38: 640 &times; 360, frame rate 15 fps, bitrate 800 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_LANDSCAPE_360P_9 = 38,
        /** 39: 640 &times; 360, frame rate 24 fps, bitrate 800 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_LANDSCAPE_360P_10 = 39,
        /** 100: 640 &times; 360, frame rate 24 fps, bitrate 1000 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_LANDSCAPE_360P_11 = 100,
        /** 40: 640 &times; 480, frame rate 15 fps, bitrate 500 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P = 40,
        /** 42: 480 &times; 480, frame rate 15 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_3 = 42,
        /** 43: 640 &times; 480, frame rate 30 fps, bitrate 750 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_4 = 43,
        /** 45: 480 &times; 480, frame rate 30 fps, bitrate 600 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_6 = 45,
        /** 47: 848 &times; 480, frame rate 15 fps, bitrate 610 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_8 = 47,
        /** 48: 848 &times; 480, frame rate 30 fps, bitrate 930 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_9 = 48,
        /** 49: 640 &times; 480, frame rate 10 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_480P_10 = 49,
        /** 50: 1280 &times; 720, frame rate 15 fps, bitrate 1130 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_720P = 50,
        /** 52: 1280 &times; 720, frame rate 30 fps, bitrate 1710 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_720P_3 = 52,
        /** 54: 960 &times; 720, frame rate 15 fps, bitrate 910 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_720P_5 = 54,
        /** 55: 960 &times; 720, frame rate 30 fps, bitrate 1380 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_720P_6 = 55,
        /** 60: 1920 &times; 1080, frame rate 15 fps, bitrate 2080 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_1080P = 60,
        /** 62: 1920 &times; 1080, frame rate 30 fps, bitrate 3150 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_1080P_3 = 62,
        /** 64: 1920 &times; 1080, frame rate 60 fps, bitrate 4780 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_1080P_5 = 64,
        /** 66: 2560 &times; 1440, frame rate 30 fps, bitrate 4850 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_1440P = 66,
        /** 67: 2560 &times; 1440, frame rate 60 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_1440P_2 = 67,
        /** 70: 3840 &times; 2160, frame rate 30 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_4K = 70,
        /** 72: 3840 &times; 2160, frame rate 60 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_LANDSCAPE_4K_3 = 72,
        /** 1000: 120 &times; 160, frame rate 15 fps, bitrate 65 Kbps. */
        VIDEO_PROFILE_PORTRAIT_120P = 1000,
        /** 1002: 120 &times; 120, frame rate 15 fps, bitrate 50 Kbps. */
        VIDEO_PROFILE_PORTRAIT_120P_3 = 1002,
        /** 1010: 180 &times; 320, frame rate 15 fps, bitrate 140 Kbps. */
        VIDEO_PROFILE_PORTRAIT_180P = 1010,
        /** 1012: 180 &times; 180, frame rate 15 fps, bitrate 100 Kbps. */
        VIDEO_PROFILE_PORTRAIT_180P_3 = 1012,
        /** 1013: 180 &times; 240, frame rate 15 fps, bitrate 120 Kbps. */
        VIDEO_PROFILE_PORTRAIT_180P_4 = 1013,
        /** 1020: 240 &times; 320, frame rate 15 fps, bitrate 200 Kbps. */
        VIDEO_PROFILE_PORTRAIT_240P = 1020,
        /** 1022: 240 &times; 240, frame rate 15 fps, bitrate 140 Kbps. */
        VIDEO_PROFILE_PORTRAIT_240P_3 = 1022,
        /** 1023: 240 &times; 424, frame rate 15 fps, bitrate 220 Kbps. */
        VIDEO_PROFILE_PORTRAIT_240P_4 = 1023,
        /** 1030: 360 &times; 640, frame rate 15 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P = 1030,
        /** 1032: 360 &times; 360, frame rate 15 fps, bitrate 260 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P_3 = 1032,
        /** 1033: 360 &times; 640, frame rate 30 fps, bitrate 600 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P_4 = 1033,
        /** 1035: 360 &times; 360, frame rate 30 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P_6 = 1035,
        /** 1036: 360 &times; 480, frame rate 15 fps, bitrate 320 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P_7 = 1036,
        /** 1037: 360 &times; 480, frame rate 30 fps, bitrate 490 Kbps. */
        VIDEO_PROFILE_PORTRAIT_360P_8 = 1037,
        /** 1038: 360 &times; 640, frame rate 15 fps, bitrate 800 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_PORTRAIT_360P_9 = 1038,
        /** 1039: 360 &times; 640, frame rate 24 fps, bitrate 800 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_PORTRAIT_360P_10 = 1039,
        /** 1100: 360 &times; 640, frame rate 24 fps, bitrate 1000 Kbps.
         * @note Live broadcast profile only.
         */
        VIDEO_PROFILE_PORTRAIT_360P_11 = 1100,
        /** 1040: 480 &times; 640, frame rate 15 fps, bitrate 500 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P = 1040,
        /** 1042: 480 &times; 480, frame rate 15 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_3 = 1042,
        /** 1043: 480 &times; 640, frame rate 30 fps, bitrate 750 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_4 = 1043,
        /** 1045: 480 &times; 480, frame rate 30 fps, bitrate 600 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_6 = 1045,
        /** 1047: 480 &times; 848, frame rate 15 fps, bitrate 610 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_8 = 1047,
        /** 1048: 480 &times; 848, frame rate 30 fps, bitrate 930 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_9 = 1048,
        /** 1049: 480 &times; 640, frame rate 10 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_PORTRAIT_480P_10 = 1049,
        /** 1050: 720 &times; 1280, frame rate 15 fps, bitrate 1130 Kbps. */
        VIDEO_PROFILE_PORTRAIT_720P = 1050,
        /** 1052: 720 &times; 1280, frame rate 30 fps, bitrate 1710 Kbps. */
        VIDEO_PROFILE_PORTRAIT_720P_3 = 1052,
        /** 1054: 720 &times; 960, frame rate 15 fps, bitrate 910 Kbps. */
        VIDEO_PROFILE_PORTRAIT_720P_5 = 1054,
        /** 1055: 720 &times; 960, frame rate 30 fps, bitrate 1380 Kbps. */
        VIDEO_PROFILE_PORTRAIT_720P_6 = 1055,
        /** 1060: 1080 &times; 1920, frame rate 15 fps, bitrate 2080 Kbps. */
        VIDEO_PROFILE_PORTRAIT_1080P = 1060,
        /** 1062: 1080 &times; 1920, frame rate 30 fps, bitrate 3150 Kbps. */
        VIDEO_PROFILE_PORTRAIT_1080P_3 = 1062,
        /** 1064: 1080 &times; 1920, frame rate 60 fps, bitrate 4780 Kbps. */
        VIDEO_PROFILE_PORTRAIT_1080P_5 = 1064,
        /** 1066: 1440 &times; 2560, frame rate 30 fps, bitrate 4850 Kbps. */
        VIDEO_PROFILE_PORTRAIT_1440P = 1066,
        /** 1067: 1440 &times; 2560, frame rate 60 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_PORTRAIT_1440P_2 = 1067,
        /** 1070: 2160 &times; 3840, frame rate 30 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_PORTRAIT_4K = 1070,
        /** 1072: 2160 &times; 3840, frame rate 60 fps, bitrate 6500 Kbps. */
        VIDEO_PROFILE_PORTRAIT_4K_3 = 1072,
        /** Default 640 &times; 360, frame rate 15 fps, bitrate 400 Kbps. */
        VIDEO_PROFILE_DEFAULT = VIDEO_PROFILE_LANDSCAPE_360P,
    };

    /** The definition of #CHANNEL_MEDIA_RELAY_ERROR. */
    public enum CHANNEL_MEDIA_RELAY_ERROR 
    {
        /** 0: The state is normal.
        */
        RELAY_OK = 0,
        /** 1: An error occurs in the server response.
        */
        RELAY_ERROR_SERVER_ERROR_RESPONSE = 1,
        /** 2: No server response. You can call the
        * {@link agora_gaming_rtc.IRtcEngine.LeaveChannel LeaveChannel} method to
        * leave the channel.
        */
        RELAY_ERROR_SERVER_NO_RESPONSE = 2,
        /** 3: The SDK fails to access the service, probably due to limited
        * resources of the server.
        */
        RELAY_ERROR_NO_RESOURCE_AVAILABLE = 3,
        /** 4: The server fails to join the source channel.
        */
        RELAY_ERROR_FAILED_JOIN_SRC = 4,
        /** 5: The server fails to join the destination channel.
        */
        RELAY_ERROR_FAILED_JOIN_DEST = 5,
        /** 6: The server fails to receive the data from the source channel.
        */
        RELAY_ERROR_FAILED_PACKET_RECEIVED_FROM_SRC = 6,
        /** 7: The source channel fails to transmit data.
        */
        RELAY_ERROR_FAILED_PACKET_SENT_TO_DEST = 7,
        /** 8: The SDK disconnects from the server due to poor network
        * connections. You can call the {@link agora_gaming_rtc.IRtcEngine.LeaveChannel LeaveChannel} method to leave the channel.
        */
        RELAY_ERROR_SERVER_CONNECTION_LOST = 8,
        /** 9: An internal error occurs in the server.
        */
        RELAY_ERROR_INTERNAL_ERROR = 9,
        /** 10: The token of the source channel has expired.
        */
        RELAY_ERROR_SRC_TOKEN_EXPIRED = 10,
        /** 11: The token of the destination channel has expired.
        */
        RELAY_ERROR_DEST_TOKEN_EXPIRED = 11,
    };

    /** Metadata type of the observer.
     * @note We only support video metadata for now.
     */
    public enum METADATA_TYPE
    {
        /** -1: The metadata type is unknown.
         */
        UNKNOWN_METADATA = -1,
        /** 0: The metadata type is video.
         */
        VIDEO_METADATA = 0,
    };

    /** The definition of Metadata. */
    public struct Metadata
    {
        /** The User ID.
         * - For the receiver: The ID of the user who sent the metadata.
         * - For the sender: Ignore it.
         */
        public uint uid;
        /** The buffer size of the sent or received metadata.
         */
        public uint size;
        /** The buffer address of the sent or received metadata.
         */
        public byte[] buffer;
        /** Time statmp of the frame following the metadata.
        */
        public long timeStampMs;
    };

    /** Video display settings of the VideoCanvas class.
    */
    public struct VideoCanvas
    {
        /** Video display window (view).
        */
        public int hwnd;
        /** The rendering mode of the video view. See RENDER_MODE_TYPE.
        */
        public RENDER_MODE_TYPE renderMode;
        /** The user ID. */
        public uint uid;
        public IntPtr priv; // private data (underlying video engine denotes it)
    };

    /** Video display modes. */
    public enum RENDER_MODE_TYPE
    {
        /** 1: Uniformly scale the video until it fills the visible boundaries (cropped). One dimension of the video may have clipped contents.
        */
        RENDER_MODE_HIDDEN = 1,
        /** 2: Uniformly scale the video until one of its dimension fits the boundary (zoomed to fit). Areas that are not filled due to disparity in the aspect ratio are filled with black.
        */
        RENDER_MODE_FIT = 2,
        /** **DEPRECATED** 3: This mode is deprecated.
        */
        RENDER_MODE_ADAPTIVE = 3,
    };

    /** Video mirror modes. */
    public enum VIDEO_MIRROR_MODE_TYPE
    {
        /** 0: (Default) The SDK enables the mirror mode.  */
        VIDEO_MIRROR_MODE_AUTO = 0,//determined by SDK
        /** 1: Enable mirror mode. */
        VIDEO_MIRROR_MODE_ENABLED = 1,//enabled mirror
        /** 2: Disable mirror mode. */
        VIDEO_MIRROR_MODE_DISABLED = 2,//disable mirror
    };
    #endregion some enum and struct types
}