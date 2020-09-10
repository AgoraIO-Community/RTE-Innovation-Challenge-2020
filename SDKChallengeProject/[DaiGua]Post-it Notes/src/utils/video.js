{
    joinHandle() {
        console.log('join')
        // e.preventDefault()
        var params = this.serializeformData() // Data is feteched and serilized from the form element.
        if (this.validator(params, this.fields)) {
          this.join(this.rtc, params)
        }
      },
      // This publishes the video feed to Agora
      publishHandle(e) {
        console.log('publish')
        e.preventDefault()
        var params = this.serializeformData()
        if (this.validator(params, this.fields)) {
          this.publish(this.rtc)
        }
      },
      // Unpublishes the video feed from Agora
      unpublishHandle(e) {
        console.log('unpublish')
        e.preventDefault()
        var params = this.serializeformData()
        if (this.validator(params, this.fields)) {
          this.unpublish(this.rtc)
        }
      },
      // Leeaves the chanenl if someone clicks the leave button
      leaveHandle(e) {
        console.log('leave')
        e.preventDefault()
        var params = this.serializeformData()
        if (this.validator(params, this.fields)) {
          this.leave(this.rtc)
        }
      },
      validator(formData, fields) {
        var keys = Object.keys(formData)
        for (const key of keys) {
          if (fields.indexOf(key) !== -1) {
            if (!formData[key]) {
              this.$message.error('Please Enter ' + key)
              return false
            }
          }
        }
        return true
      },
      serializeformData() {
        const formData = []
        formData.push({ name: 'appID', value: this.appID })
        formData.push({ name: 'token', value: this.token })
        formData.push({ name: 'uid', value: this.uid })
        formData.push({ name: 'appIcameraIdD', value: this.appIcameraIdD })
        formData.push({ name: 'channel', value: this.channel })
        formData.push({ name: 'microphoneId', value: this.microphoneId })
        formData.push({ name: 'appcameraResolutionID', value: this.appcameraResolutionID })
        formData.push({ name: 'codec', value: this.codec })
        formData.push({ name: 'mode', value: this.mode })
  
        // formData.append('channel', this.channel)
        // formData.append('token', this.token)
        // formData.append('uid', this.uid)
        // formData.append('appIcameraIdD', this.cameraId)
        // formData.append('microphoneId', this.microphoneId)
        // formData.append('appcameraResolutionID', this.cameraResolution)
        console.log('formdata---------', formData, this.appID, this.channel)
        const obj = {}
        for (const item of formData) {
          const key = item.name
          const val = item.value
          obj[key] = val
        }
        return obj
      },
      addView(id, show) {
        console.log('---------------addView(id, show)', id, show)
        this.idlist.push(id)
  
        // if (!$('#' + id)[0]) {
        //   console.log('#' + id)
        //   // $('<div/>', {
        //   //   id: 'remote_video_panel_' + id,
        //   //   class: 'video-view'
        //   // }).appendTo('#video')
        // }
      },
      removeView(id) {
        if (this.$refs['#remote_video_panel_' + id][0]) {
          // this.$refs['#remote_video_panel_' + id].remove()
          this.idlist.splice(this.idlist.indexOf(id), 1)
        }
      },

      getDevices(next) {
        this.AgoraRTC.getDevices(function(items) {
          items.filter(function(item) {
            return ['audioinput', 'videoinput'].indexOf(item.kind) !== -1
          })
            .map(function(item) {
              return {
                name: item.label,
                value: item.deviceId,
                kind: item.kind
              }
            })
          var videos = []
          var audios = []
          for (var i = 0; i < items.length; i++) {
            const item = items[i]
            if (item.kind === 'videoinput') {
              let name = item.label
              const value = item.deviceId
              if (!name) {
                name = 'camera-' + videos.length
              }
              videos.push({
                name: name,
                value: value,
                kind: item.kind
              })
            }
            if (item.kind === 'audioinput') {
              let name = item.label
              const value = item.deviceId
              if (!name) {
                name = 'microphone-' + audios.length
              }
              audios.push({
                name: name,
                value: value,
                kind: item.kind
              })
            }
          }
          next({ videos: videos, audios: audios })
        })
      },
      handleEvents(rtc) {
        // Occurs when an error message is reported and requires error handling.
        rtc.client.on('error', (err) => {
          console.log(err)
        })
        // Occurs when the peer user leaves the channel; for example, the peer user calls Client.leave.
        rtc.client.on('peer-leave', function(evt) {
          const id = evt.uid
  
          const streams = rtc.remoteStreams.filter(e => id !== e.getId())
          const peerStream = rtc.remoteStreams.find(e => id === e.getId())
          if (peerStream && peerStream.isPlaying()) {
            peerStream.stop()
          }
          rtc.remoteStreams = streams
          if (id !== rtc.params.uid) {
            this.removeView(id)
          }
          console.log('peer leave')
          console.log('peer-leave', id)
        })
        const _this = this
        // Occurs when the local stream is published.
        rtc.client.on('stream-published', function(evt) {
          _this.$message.success('stream published success')
          console.log('stream-published')
        })
        // Occurs when the remote stream is added 有远端流加入时订阅该流.
        rtc.client.on('stream-added', function(evt) {
          var remoteStream = evt.stream
          var id = remoteStream.getId()
          console.log('stream-added uid: ' + id)
          if (id !== rtc.params.uid) {
            rtc.client.subscribe(remoteStream, function(err) {
              console.log('stream subscribe failed', err)
            })
          }
          console.log('stream-added remote-uid: ', id)
        })
        // Occurs when a user subscribes to a remote stream 订阅成功后播放远端流.
        rtc.client.on('stream-subscribed', function(evt) {
          const remoteStream = evt.stream
          const id = remoteStream.getId()
          rtc.remoteStreams.push(remoteStream)
          _this.addView(id)
          console.log('remote_video_ + id-------', 'remote_video_' + id)
          remoteStream.play('remote_video_') // 播放流
          // Toast.info('stream-subscribed remote-uid: ' + id)
          console.log('stream-subscribed remote-uid: ', id)
        })
        // Occurs when the remote stream is removed; for example, a peer user calls Client.unpublish
        //  当远端流被移除时（例如远端用户调用了 Stream.unpublish）， 停止播放该流并移除它的画面。.
        rtc.client.on('stream-removed', function(evt) {
          const remoteStream = evt.stream
          const id = remoteStream.getId()
          console.log('stream-removed uid: ' + id)
          if (remoteStream.isPlaying()) {
            remoteStream.stop()
          }
          rtc.remoteStreams = rtc.remoteStreams.filter(function(stream) {
            return stream.getId() !== id
          })
          this.removeView(id)
          console.log('stream-removed remote-uid: ', id)
        })
        rtc.client.on('onTokenPrivilegeWillExpire', function() {
          // After requesting a new token
          // rtc.client.renewToken(token);
          // Toast.info('onTokenPrivilegeWillExpire')
          console.log('onTokenPrivilegeWillExpire')
        })
        rtc.client.on('onTokenPrivilegeDidExpire', function() {
          // After requesting a new token
          // client.renewToken(token);
          // Toast.info('onTokenPrivilegeDidExpire')
          console.log('onTokenPrivilegeDidExpire')
        })
      },
      /**
        * rtc: rtc object
        * option: {
        *  mode: string, "live" | "rtc"
        *  codec: string, "h264" | "vp8"
        *  appID: string
        *  channel: string, channel name
        *  uid: number
        *  token; string,
        * }
       **/
      join(rtc, option) {
        if (rtc.joined) {
          console.log('Your already joined')
          return
        }
  
        /**
         * A class defining the properties of the config parameter in the createClient method.
         * Note:
         *    Ensure that you do not leave mode and codec as empty.
         *    Ensure that you set these properties before calling Client.join.
         *  You could find more detail here. https://docs.agora.io/en/Video/API%20Reference/web/interfaces/agorartc.clientconfig.html
        **/
        console.log('----------this.AgoraRTC', this.AgoraRTC)
        rtc.client = this.AgoraRTC.createClient({ mode: option.mode, codec: option.codec })
  
        rtc.params = option
  
        // handle AgoraRTC client event
        this.handleEvents(rtc)
        const _this = this
        // init client
        rtc.client.init(option.appID, function() {
          console.log('init success')
          // The value of role can be "host" or "audience". 可以设置角色为主持人还是观众
          rtc.client.setClientRole('audience')
          /**
           * Joins an AgoraRTC Channel
           * This method joins an AgoraRTC channel.
           * Parameters
           * tokenOrKey: string | null
           *    Low security requirements: Pass null as the parameter value.
           *    High security requirements: Pass the string of the Token or Channel Key as the parameter value. See Use Security Keys for details.
           *  channel: string
           *    A string that provides a unique channel name for the Agora session. The length must be within 64 bytes. Supported character scopes:
           *    26 lowercase English letters a-z
           *    26 uppercase English letters A-Z
           *    10 numbers 0-9
           *    Space
           *    "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", "{", "}", "|", "~", ","
           *  uid: number | null
           *    The user ID, an integer. Ensure this ID is unique. If you set the uid to null, the server assigns one and returns it in the onSuccess callback.
           *   Note:
           *      All users in the same channel should have the same type (number or string) of uid.
           *      If you use a number as the user ID, it should be a 32-bit unsigned integer with a value ranging from 0 to (232-1).
          **/
          rtc.client.join(option.token ? option.token : null, option.channel, option.uid ? +option.uid : null, function(uid) {
            _this.$message.success('join channel: ' + option.channel + ' success, uid: ' + uid)
            console.log('join channel: ' + option.channel + ' success, uid: ' + uid)
            rtc.joined = true
  
            rtc.params.uid = uid
  
            // create local stream
            console.log('----------this.AgoraRTC', _this.AgoraRTC)
            rtc.localStream = _this.AgoraRTC.createStream({
              streamID: rtc.params.uid,
              audio: true,
              video: true,
              screen: false,
              microphoneId: option.microphoneId,
              cameraId: option.cameraId
            })
  
            // initialize local stream. Callback function executed after intitialization is done
            rtc.localStream.init(function() {
              console.log('init local stream success')
              // play stream with html element id "local_stream"
              rtc.localStream.play('local_stream')
  
              // publish local stream 观众禁止发布本地流
              // _this.publish(rtc)
            }, function(err) {
              // Toast.error('stream init failed, please open console see more detail')
              console.error('init local stream failed ', err)
            })
          }, function(err) {
            // Toast.error('client join failed, please open console see more detail')
            console.error('client join failed', err)
          })
        }, (err) => {
          // Toast.error('client init failed, please open console see more detail')
          console.error(err)
        })
      },
      publish(rtc) {
        if (!rtc.client) {
          console.log('Please Join Room First')
          return
        }
        if (rtc.published) {
          console.log('Your already published')
          return
        }
        var oldState = rtc.published
  
        // publish localStream 调用 Client.publish 方法，发布本地流
        rtc.client.publish(rtc.localStream, function(err) {
          rtc.published = oldState
          console.log('publish failed')
          // Toast.error('publish failed')
          console.error(err)
        })
        this.$message.success('publish')
        console.log('publish')
        rtc.published = true
      },
      unpublish(rtc) {
        if (!rtc.client) {
          console.log('Please Join Room First')
          return
        }
        if (!rtc.published) {
          console.log("Your didn't publish")
          return
        }
        var oldState = rtc.published
        rtc.client.unpublish(rtc.localStream, function(err) {
          rtc.published = oldState
          console.log('unpublish failed')
          // Toast.error('unpublish failed')
          console.error(err)
        })
        console.log('unpublish')
        rtc.published = false
      },
      leave(rtc) {
        if (!rtc.client) {
          console.log('Please Join First!')
          return
        }
        if (!rtc.joined) {
          console.log('You are not in channel')
          return
        }
        /**
         * Leaves an AgoraRTC Channel
         * This method enables a user to leave a channel.
         **/
        rtc.client.leave(function() {
          // stop stream
          if (rtc.localStream.isPlaying()) {
            rtc.localStream.stop()
          }
          // close stream
          rtc.localStream.close()
          for (let i = 0; i < rtc.remoteStreams.length; i++) {
            var stream = rtc.remoteStreams.shift()
            var id = stream.getId()
            if (stream.isPlaying()) {
              stream.stop()
            }
            this.removeView(id)
          }
          rtc.localStream = null
          rtc.remoteStreams = []
          rtc.client = null
          console.log('client leaves channel success')
          rtc.published = false
          rtc.joined = false
          console.log('leave success')
        }, function(err) {
          console.log('channel leave failed')
          console.log('leave success')
          console.error(err)
        })
      }
    }