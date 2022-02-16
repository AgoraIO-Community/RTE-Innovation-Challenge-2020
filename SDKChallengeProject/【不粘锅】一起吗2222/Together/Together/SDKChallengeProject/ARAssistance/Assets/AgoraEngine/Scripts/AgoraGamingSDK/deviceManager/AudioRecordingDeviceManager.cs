using System.Runtime.InteropServices;
using AOT;

namespace agora_gaming_rtc
{
	public abstract class IAudioRecordingDeviceManager : IRtcEngineNative
	{
        
		public abstract bool CreateAAudioRecordingDeviceManager();

		public abstract int ReleaseAAudioRecordingDeviceManager();

	 	public abstract int GetAudioRecordingDeviceCount();

        public abstract int GetAudioRecordingDevice(int index, ref string audioRecordingDeviceName, ref string audioRecordingDeviceId);

        public abstract int SetAudioRecordingDevice(string deviceId);

 		public abstract int StartAudioRecordingDeviceTest(int indicationInterval);

 		public abstract int StopAudioRecordingDeviceTest();
        
		public abstract int GetCurrentRecordingDevice(ref string deviceId);
   
	    public abstract	int SetAudioRecordingDeviceVolume(int volume);

        public abstract int GetAudioRecordingDeviceVolume();

        public abstract int SetAudioRecordingDeviceMute(bool mute);

        public abstract bool IsAudioRecordingDeviceMute();
        
		public abstract int GetCurrentRecordingDeviceInfo(ref string deviceName, ref string deviceId);
	}

    /** The definition of AudioRecordingDeviceManager. */
	public sealed class AudioRecordingDeviceManager : IAudioRecordingDeviceManager
    {
		
		private IRtcEngine mEngine = null;
		private static AudioRecordingDeviceManager _audioRecordingDeviceManagerInstance;

		private AudioRecordingDeviceManager(IRtcEngine rtcEngine)
		{
			mEngine = rtcEngine;
		}

		public static AudioRecordingDeviceManager GetInstance(IRtcEngine rtcEngine)
		{
			if (_audioRecordingDeviceManagerInstance == null)
			{
				_audioRecordingDeviceManagerInstance = new AudioRecordingDeviceManager(rtcEngine);
			}
			return _audioRecordingDeviceManagerInstance;
		}

     	public static void ReleaseInstance()
		{
			_audioRecordingDeviceManagerInstance = null;
		}

		// used internally
		public void SetEngine (IRtcEngine engine)
		{
			mEngine = engine;
		}

        /** Create an AudioRecordingDeviceManager instance.
        *
        * @note Ensure that you call {@link agora_gaming_rtc.AudioRecordingDeviceManager.ReleaseAAudioRecordingDeviceManager ReleaseAAudioRecordingDeviceManager} to release this instance after calling this method.
        * 
        * @return 
        * - true: Success.
        * - false: Failure.
        */
		public override bool CreateAAudioRecordingDeviceManager()
		{
			if (mEngine == null)
				return false;

			return IRtcEngineNative.creatAAudioRecordingDeviceManager();
		}
        /** Release an AudioRecordingDeviceManager instance.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override int ReleaseAAudioRecordingDeviceManager()
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.releaseAAudioRecordingDeviceManager();
		}

        /** Retrieves the total number of the indexed audio recording devices in the system.
        * 
        * @return Total number of the indexed audio recording devices.
        */
	 	public override int GetAudioRecordingDeviceCount()
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.getAudioRecordingDeviceCount();
		}

        /** Retrieves the audio recording device associated with the index.
        *         
        * After calling this method, the SDK retrieves the device name and device ID according to the index.
        * 
        * @note Call {@link agora_gaming_rtc.AudioRecordingDeviceManager.GetAudioRecordingDeviceCount GetAudioRecordingDeviceCount} before this method.
        * 
        * @param index The index of the recording device in the system. The value of `index` is associated with the number of the recording device which is retrieved from `GetAudioRecordingDeviceCount`. For example, when the number of recording devices is 3, the value range of `index` is [0,2].
        * @param audioRecordingDeviceName The name of the recording device for the corresponding index.
        * @param audioRecordingDeviceId The ID of the recording device for the corresponding index.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override int GetAudioRecordingDevice(int index, ref string audioRecordingDeviceName, ref string audioRecordingDeviceId)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			if (index >= 0 && index < GetAudioRecordingDeviceCount())
			{
				System.IntPtr audioRecordingDeviceNamePtr = Marshal.AllocHGlobal(512);
				System.IntPtr audioRecordingDeviceIdPtr = Marshal.AllocHGlobal(512);
				int ret = IRtcEngineNative.getAudioRecordingDevice(index, audioRecordingDeviceNamePtr, audioRecordingDeviceIdPtr);
				audioRecordingDeviceName = Marshal.PtrToStringAnsi(audioRecordingDeviceNamePtr);
				audioRecordingDeviceId = Marshal.PtrToStringAnsi(audioRecordingDeviceIdPtr);
				Marshal.FreeHGlobal(audioRecordingDeviceNamePtr);
				Marshal.FreeHGlobal(audioRecordingDeviceIdPtr);
				return ret;
			}
			else
			{
				return (int)ERROR_CODE.ERROR_INVALID_ARGUMENT;
			}  
		}

        /** Retrieves the device ID of the current audio recording device.
        * 
        * @param deviceId The device ID of the current audio recording device.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override int GetCurrentRecordingDevice(ref string deviceId)
        {
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

            if (GetAudioRecordingDeviceCount() > 0)
			{
				System.IntPtr recordingDeviceId = Marshal.AllocHGlobal(512);
				int ret = getCurrentRecordingDevice(recordingDeviceId);
				deviceId = Marshal.PtrToStringAnsi(recordingDeviceId);
				Marshal.FreeHGlobal(recordingDeviceId);
				return ret;
			}
			else
			{
				return (int)ERROR_CODE.ERROR_NO_DEVICE_PLUGIN;
			}
        }
		
        /** Sets the volume of the current audio recording device.
        * 
        * @param volume The volume of the current audio recording device. The value ranges between 0 (lowest volume) and 255 (highest volume).
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override	int SetAudioRecordingDeviceVolume(int volume)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;
			
			return IRtcEngineNative.setAudioRecordingDeviceVolume(volume);
		}

        /** Retrieves the volume of the current audio recording device.
        * 
        * @return
        * - The volume of the current audio recording device, if this method call succeeds.
        * - < 0: Failure.
        */
        public override int GetAudioRecordingDeviceVolume()
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.getAudioRecordingDeviceVolume();
		}

        /** Sets whether to stop audio recording.
        * 
        * @param mute Sets whether to stop audio recording.
        * -true: Stops.
        * -false: Doesn't stop.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
        public override int SetAudioRecordingDeviceMute(bool mute)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.setAudioRecordingDeviceMute(mute);
		}

        /** Gets the status of the current audio recording device.
        * 
        * @return Whether the current audio recording device stops audio recording.
        * -true: Stops.
        * -false: Doesn't stop.
        */
        public override bool IsAudioRecordingDeviceMute()
		{
			if (mEngine == null)
				return false;

			return IRtcEngineNative.isAudioRecordingDeviceMute();
		}

        /** Sets the audio recording device using the device ID.
        * 
        * @note 
        * - Call {@link agora_gaming_rtc.AudioRecordingDeviceManager.GetAudioRecordingDevice GetAudioRecordingDevice} before this method.
        * - Plugging or unplugging the audio device does not change the device ID.
        * 
        * @param deviceId Device ID of the audio recording device, retrieved by calling `GetAudioRecordingDevice`.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override int SetAudioRecordingDevice(string deviceId)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.setAudioRecordingDevice(deviceId);
		}

        /** Starts the test of the current audio recording device.
        * 
        * This method tests whether the local audio devices are working properly. After calling this method, the microphone captures the local audio and plays it through the speaker. The {@link agora_gaming_rtc.OnVolumeIndicationHandler OnVolumeIndicationHandler} callback returns the local audio volume information at the set interval.
        * 
        * @note 
        * - Ensure that you call {@link agora_gaming_rtc.AudioRecordingDeviceManager.StopAudioRecordingDeviceTest StopAudioRecordingDeviceTest} after calling this method.
        * - This method tests the local audio devices and does not report the network conditions.
        * 
        * @param indicationInterval The time interval (ms) at which the `OnVolumeIndicationHandler` callback returns.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
 		public override int StartAudioRecordingDeviceTest(int indicationInterval)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.startAudioRecordingDeviceTest(indicationInterval);
		}

        /** Stops the test of the current audio recording device.
        * 
        * @note Ensure that you call this method to stop the test after calling {@link agora_gaming_rtc.AudioRecordingDeviceManager.StartAudioRecordingDeviceTest StartAudioRecordingDeviceTest}.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
 		public override int StopAudioRecordingDeviceTest()
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			return IRtcEngineNative.stopAudioRecordingDeviceTest();
		}

        /** Retrieves the device information of the current audio recording device.
        * 
        * @param deviceName The device name of the current audio recording device.
        * @param deviceId The device ID of the current audio recording device.
        * 
        * @return
        * - 0: Success.
        * - < 0: Failure.
        */
		public override int GetCurrentRecordingDeviceInfo(ref string deviceName, ref string deviceId)
		{
			if (mEngine == null)
				return (int)ERROR_CODE.ERROR_NOT_INIT_ENGINE;

			if (GetAudioRecordingDeviceCount() > 0)
			{
				System.IntPtr audioRecordingDeviceNamePtr = Marshal.AllocHGlobal(512);
				System.IntPtr audioRecordingDeviceIdPtr = Marshal.AllocHGlobal(512);
				int ret = IRtcEngineNative.getCurrentRecordingDeviceInfo(audioRecordingDeviceNamePtr, audioRecordingDeviceIdPtr);
				deviceName = Marshal.PtrToStringAnsi(audioRecordingDeviceNamePtr);
				deviceId = Marshal.PtrToStringAnsi(audioRecordingDeviceIdPtr);
				Marshal.FreeHGlobal(audioRecordingDeviceNamePtr);
				Marshal.FreeHGlobal(audioRecordingDeviceIdPtr);
				return ret;	
			}
			else
			{
				return (int)ERROR_CODE.ERROR_NO_DEVICE_PLUGIN;
			}
		}
	}
}