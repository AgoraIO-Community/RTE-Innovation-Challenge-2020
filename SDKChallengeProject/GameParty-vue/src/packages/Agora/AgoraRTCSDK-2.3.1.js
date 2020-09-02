!(function(e, t) {
  typeof exports === 'object' && typeof module === 'object'
    ? (module.exports = t())
    : typeof define === 'function' && define.amd
      ? define('AgoraRTC', [], t)
      : typeof exports === 'object'
        ? (exports.AgoraRTC = t())
        : (e.AgoraRTC = t())
})(this, function() {
  return (function(e) {
    function t(i) {
      if (n[i]) return n[i].exports
      var r = (n[i] = { i: i, l: !1, exports: {} })
      return e[i].call(r.exports, r, r.exports, t), (r.l = !0), r.exports
    }
    var n = {}
    return (
      (t.m = e),
      (t.c = n),
      (t.d = function(e, n, i) {
        t.o(e, n) || Object.defineProperty(e, n, { configurable: !1, enumerable: !0, get: i })
      }),
      (t.n = function(e) {
        var n =
          e && e.__esModule
            ? function() {
                return e.default
              }
            : function() {
                return e
              }
        return t.d(n, 'a', n), n
      }),
      (t.o = function(e, t) {
        return Object.prototype.hasOwnProperty.call(e, t)
      }),
      (t.p = ''),
      t((t.s = 14))
    )
  })([
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(15),
        r = (function() {
          var e,
            t,
            n,
            r,
            o,
            a,
            s = 0
          return (
            (e = function(e) {
              e > 4 ? (e = 4) : e < 0 && (e = 0), (s = e)
            }),
            (t = function() {
              var e = arguments[0],
                t = arguments
              if (!(e < s)) {
              {
switch (e) {
                case 0:
                  ;(t[0] = (0, i.getTimestamp)() + ' Agora-SDK [DEBUG]:'), console.log.apply(console, t)
                  break
                case 1:
                  ;(t[0] = (0, i.getTimestamp)() + ' Agora-SDK [INFO]:'), console.log.apply(console, t)
                  break
                case 2:
                  ;(t[0] = (0, i.getTimestamp)() + ' Agora-SDK [WARN]:'), console.warn.apply(console, t)
                  break
                case 3:
                  ;(t[0] = (0, i.getTimestamp)() + ' Agora-SDK [ERROR]:'), console.error.apply(console, t)
                  break
                default:
                  return (t[0] = (0, i.getTimestamp)() + ' Agora-SDK [DEFAULT]:'), void console.log.apply(console, t)
              }
              }
            }),
            (n = function() {
              for (var e = [0], n = 0; n < arguments.length; n++) e.push(arguments[n])
              t.apply(this, e)
            }),
            (r = function() {
              for (var e = [1], n = 0; n < arguments.length; n++) e.push(arguments[n])
              t.apply(this, e)
            }),
            (o = function() {
              for (var e = [2], n = 0; n < arguments.length; n++) e.push(arguments[n])
              t.apply(this, e)
            }),
            (a = function() {
              for (var e = [3], n = 0; n < arguments.length; n++) e.push(arguments[n])
              t.apply(this, e)
            }),
            { DEBUG: 0, INFO: 1, WARNING: 2, ERROR: 3, NONE: 4, setLogLevel: e, log: t, debug: n, info: r, warning: o, error: a }
          )
        })()
      t.default = r
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = ['webcs-1.agora.io'],
        r = ['webcs-2.agora.io', 'webcs-3.agora.io', 'webcs-4.agora.io'],
        o = {
          '90p_1': [160, 90],
          '120p_1': [160, 120],
          '120p_3': [120, 120],
          '120p_4': [212, 120],
          '180p_1': [320, 180],
          '180p_3': [180, 180],
          '180p_4': [240, 180],
          '240p_1': [320, 240],
          '240p_3': [240, 240],
          '240p_4': [424, 240],
          '360p_1': [640, 360],
          '360p_3': [360, 360],
          '360p_4': [640, 360],
          '360p_6': [360, 360],
          '360p_7': [480, 360],
          '360p_8': [480, 360],
          '360p_9': [640, 360],
          '360p_10': [640, 360],
          '360p_11': [640, 360],
          '480p_1': [640, 480],
          '480p_2': [640, 480],
          '480p_3': [480, 480],
          '480p_4': [640, 480],
          '480p_6': [480, 480],
          '480p_8': [848, 480],
          '480p_9': [848, 480],
          '480p_10': [640, 480],
          '720p_1': [1280, 720],
          '720p_2': [1280, 720],
          '720p_3': [1280, 720],
          '720p_5': [960, 720],
          '720p_6': [960, 720],
          '1080p_1': [1920, 1080],
          '1080p_2': [1920, 1080],
          '1080p_3': [1920, 1080],
          '1080p_5': [1920, 1080],
          '1440p_1': [2560, 1440],
          '1440p_2': [2560, 1440],
          '4k_1': [3840, 2160],
          '4k_3': [3840, 2160]
        }
      ;(t.GIT_VERSION = 'release_20180410_01-130-g3fbf70f'),
        (t.VERSION = '2.3.1'),
        (t.WEBCS_DOMAIN = i),
        (t.WEBCS_DOMAIN_BACKUP_LIST = r),
        (t.EVENT_REPORT_DOMAIN = 'webcollector-1.agora.io'),
        (t.EVENT_REPORT_BACKUP_DOMAIN = 'webcollector-2.agora.io'),
        (t.WEBCS_BACKUP_CONNECT_TIMEOUT = 6e3),
        (t.HTTP_CONNECT_TIMEOUT = 5e3),
        (t.SUPPORT_RESOLUTION_LIST = o)
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function() {
          var e = m()
          return e.name && e.name === 'Chrome'
        },
        r = function() {
          var e = m()
          return e.name && e.name === 'Safari'
        },
        o = function() {
          var e = m()
          return e.name && e.name === 'Firefox'
        },
        a = function() {
          var e = m()
          return e.name && e.name === 'OPR'
        },
        s = function() {
          var e = m()
          return e.name && e.name === 'QQBrowser'
        },
        d = function() {
          var e = m()
          return e.name && e.name === 'MicroMessenger'
        },
        c = function() {
          var e = p()
          return e === 'Linux' || e === 'Mac OS X' || e === 'Mac OS' || e.indexOf('Windows') !== -1
        },
        u = function() {
          var e = p()
          return e === 'Android' || e === 'iOS'
        },
        l = function() {
          return m().version
        },
        p = function() {
          return m().os
        },
        f = function() {
          var e,
            t = navigator.userAgent,
            n = t.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || []
          n[1] === 'Chrome' && (e = t.match(/(OPR(?=\/))\/?(\d+)/i)) != null && (n = e),
            'Safari' === n[1] && (e = t.match(/version\/(\d+)/i)) != null && (n[2] = e[1]),
            ~t.toLowerCase().indexOf('qqbrowser') && (e = t.match(/(qqbrowser(?=\/))\/?(\d+)/i)) != null && (n = e),
            ~t.toLowerCase().indexOf('micromessenger') && (e = t.match(/(micromessenger(?=\/))\/?(\d+)/i)) != null && (n = e),
            ~t.toLowerCase().indexOf('edge') && (e = t.match(/(edge(?=\/))\/?(\d+)/i)) != null && (n = e),
            ~t.toLowerCase().indexOf('trident') && (e = /\brv[ :]+(\d+)/g.exec(t) || []) != null && (n = [null, 'IE', e[1]])
          var i = void 0,
            r = [
              { s: 'Windows 10', r: /(Windows 10.0|Windows NT 10.0)/ },
              { s: 'Windows 8.1', r: /(Windows 8.1|Windows NT 6.3)/ },
              { s: 'Windows 8', r: /(Windows 8|Windows NT 6.2)/ },
              { s: 'Windows 7', r: /(Windows 7|Windows NT 6.1)/ },
              { s: 'Windows Vista', r: /Windows NT 6.0/ },
              { s: 'Windows Server 2003', r: /Windows NT 5.2/ },
              { s: 'Windows XP', r: /(Windows NT 5.1|Windows XP)/ },
              { s: 'Windows 2000', r: /(Windows NT 5.0|Windows 2000)/ },
              { s: 'Windows ME', r: /(Win 9x 4.90|Windows ME)/ },
              { s: 'Windows 98', r: /(Windows 98|Win98)/ },
              { s: 'Windows 95', r: /(Windows 95|Win95|Windows_95)/ },
              { s: 'Windows NT 4.0', r: /(Windows NT 4.0|WinNT4.0|WinNT|Windows NT)/ },
              { s: 'Windows CE', r: /Windows CE/ },
              { s: 'Windows 3.11', r: /Win16/ },
              { s: 'Android', r: /Android/ },
              { s: 'Open BSD', r: /OpenBSD/ },
              { s: 'Sun OS', r: /SunOS/ },
              { s: 'Linux', r: /(Linux|X11)/ },
              { s: 'iOS', r: /(iPhone|iPad|iPod)/ },
              { s: 'Mac OS X', r: /Mac OS X/ },
              { s: 'Mac OS', r: /(MacPPC|MacIntel|Mac_PowerPC|Macintosh)/ },
              { s: 'QNX', r: /QNX/ },
              { s: 'UNIX', r: /UNIX/ },
              { s: 'BeOS', r: /BeOS/ },
              { s: 'OS/2', r: /OS\/2/ },
              { s: 'Search Bot', r: /(nuhk|Googlebot|Yammybot|Openbot|Slurp|MSNBot|Ask Jeeves\/Teoma|ia_archiver)/ }
            ]
          for (var o in r) {
            var a = r[o]
            if (a.r.test(navigator.userAgent)) {
              i = a.s
              break
            }
          }
          return { name: n[1], version: n[2], os: i }
        },
        m = (function() {
          var e = f()
          return function() {
            return e
          }
        })()
      ;(t.getBrowserInfo = m),
        (t.getBrowserVersion = l),
        (t.getBrowserOS = p),
        (t.isChrome = i),
        (t.isSafari = r),
        (t.isFireFox = o),
        (t.isOpera = a),
        (t.isQQBrowser = s),
        (t.isWeChatBrowser = d),
        (t.isSupportedPC = c),
        (t.isSupportedMobile = u)
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function() {
          var e = {}
          return (
            (e.dispatcher = {}),
            (e.dispatcher.eventListeners = {}),
            (e.addEventListener = function(t, n) {
              void 0 === e.dispatcher.eventListeners[t] && (e.dispatcher.eventListeners[t] = []), e.dispatcher.eventListeners[t].push(n)
            }),
            (e.on = e.addEventListener),
            (e.removeEventListener = function(t, n) {
              var i
              ;(i = e.dispatcher.eventListeners[t].indexOf(n)) !== -1 && e.dispatcher.eventListeners[t].splice(i, 1)
            }),
            (e.dispatchEvent = function(t) {
              var n
              for (n in e.dispatcher.eventListeners[t.type]) {
              { e.dispatcher.eventListeners[t.type].hasOwnProperty(n) && typeof e.dispatcher.eventListeners[t.type][n] == 'function' && e.dispatcher.eventListeners[t.type][n](t) }
            }),
            (e.dispatchSocketEvent = function(t) {
              var n
              for (n in e.dispatcher.eventListeners[t.type]) {
              { e.dispatcher.eventListeners[t.type].hasOwnProperty(n) && typeof e.dispatcher.eventListeners[t.type][n] == 'function' && e.dispatcher.eventListeners[t.type][n](t.msg) }
            }),
            e
          )
        },
        r = function(e) {
          var t = {}
          return (t.type = e.type), t
        },
        o = function(e) {
          var t = r(e)
          return (t.stream = e.stream), (t.reason = e.reason), (t.msg = e.msg), t
        },
        a = function(e) {
          var t = r(e)
          return (t.uid = e.uid), (t.attr = e.attr), (t.stream = e.stream), t
        },
        s = function(e) {
          var t = r(e)
          return (t.msg = e.msg), t
        },
        d = function(e) {
          var t = r(e)
          return (t.url = e.url), (t.reason = e.reason), t
        }
      ;(t.EventDispatcher = i), (t.StreamEvent = o), (t.ClientEvent = a), (t.MediaEvent = s), (t.LiveStreamingEvent = d)
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return new Promise(function(t, n) {
          o(e, t, n)
        })
      }
      var r =
          typeof Symbol === 'function' && typeof Symbol.iterator === 'symbol'
            ? function(e) {
                return typeof e
              }
            : function(e) {
                return e && typeof Symbol === 'function' && e.constructor === Symbol && e !== Symbol.prototype ? 'symbol' : typeof e
              },
        o = null,
        a = null,
        s = null,
        d = null,
        c = null,
        u = null,
        l = null,
        p = {
          log: function() {},
          extractVersion: function(e, t, n) {
            var i = e.match(t)
            return i && i.length >= n && parseInt(i[n])
          }
        }
      if (
        ((typeof window == 'undefined' ? 'undefined' : r(window)) === 'object' &&
          (!window.HTMLMediaElement ||
            'srcObject' in window.HTMLMediaElement.prototype ||
            Object.defineProperty(window.HTMLMediaElement.prototype, 'srcObject', {
              get: function() {
                return 'mozSrcObject' in this ? this.mozSrcObject : this._srcObject
              },
              set: function(e) {
                'mozSrcObject' in this ? (this.mozSrcObject = e) : ((this._srcObject = e), (this.src = URL.createObjectURL(e)))
              }
            }),
          (o = window.navigator && window.navigator.getUserMedia)),
        (a = function(e, t) {
          e.srcObject = t
        }),
        (s = function(e, t) {
          e.srcObject = t.srcObject
        }),
        typeof window !== 'undefined' && window.navigator)
      ) {
      {
if (navigator.mozGetUserMedia && window.mozRTCPeerConnection) {
        if (
          (p.log('This appears to be Firefox'),
          (d = 'firefox'),
          (c = p.extractVersion(navigator.userAgent, /Firefox\/([0-9]+)\./, 1)),
          (u = 31),
          (l = function(e, t) {
            if (c < 38 && e && e.iceServers) {
              for (var n = [], i = 0; i < e.iceServers.length; i++) {
                var r = e.iceServers[i]
                if (r.hasOwnProperty('urls'))
                  for (var o = 0; o < r.urls.length; o++) {
                    var a = { url: r.urls[o] }
                    0 === r.urls[o].indexOf('turn') && ((a.username = r.username), (a.credential = r.credential)), n.push(a)
                  }
                else n.push(e.iceServers[i])
              }
              e.iceServers = n
            }
            return new mozRTCPeerConnection(e, t)
          }),
          window.RTCSessionDescription || (window.RTCSessionDescription = mozRTCSessionDescription),
          window.RTCIceCandidate || (window.RTCIceCandidate = mozRTCIceCandidate),
          (o = function(e, t, n) {
            var i = function(e) {
              if ((void 0 === e ? 'undefined' : r(e)) !== 'object' || e.require) return e
              var t = []
              return (
                Object.keys(e).forEach(function(n) {
                  if (n !== 'require' && n !== 'advanced' && n !== 'mediaSource') {
                    var i = (e[n] = r(e[n]) === 'object' ? e[n] : { ideal: e[n] })
                    if (
                      ((void 0 === i.min && void 0 === i.max && void 0 === i.exact) || t.push(n),
                      void 0 !== i.exact && (typeof i.exact == 'number' ? (i.min = i.max = i.exact) : (e[n] = i.exact), delete i.exact),
                      void 0 !== i.ideal)
                    ) {
                      e.advanced = e.advanced || []
                      var o = {}
                      'number' === typeof i.ideal ? (o[n] = { min: i.ideal, max: i.ideal }) : (o[n] = i.ideal), e.advanced.push(o), delete i.ideal, Object.keys(i).length || delete e[n]
                    }
                  }
                }),
                t.length && (e.require = t),
                e
              )
            }
            return (
              c < 38 && (p.log('spec: ' + JSON.stringify(e)), e.audio && (e.audio = i(e.audio)), e.video && (e.video = i(e.video)), p.log('ff37: ' + JSON.stringify(e))),
              navigator.mozGetUserMedia(e, t, n)
            )
          }),
          (navigator.getUserMedia = o),
          navigator.mediaDevices || (navigator.mediaDevices = { getUserMedia: i, addEventListener: function() {}, removeEventListener: function() {} }),
          (navigator.mediaDevices.enumerateDevices =
              navigator.mediaDevices.enumerateDevices ||
              function() {
                return new Promise(function(e) {
                  e([{ kind: 'audioinput', deviceId: 'default', label: '', groupId: '' }, { kind: 'videoinput', deviceId: 'default', label: '', groupId: '' }])
                })
              }),
          c < 41)
        ) {
          var f = navigator.mediaDevices.enumerateDevices.bind(navigator.mediaDevices)
          navigator.mediaDevices.enumerateDevices = function() {
            return f().then(void 0, function(e) {
              if (e.name === 'NotFoundError') return []
              throw e
            })
          }
        }
      } else if (navigator.webkitGetUserMedia && window.webkitRTCPeerConnection) {
        p.log('This appears to be Chrome'),
        (d = 'chrome'),
        (c = p.extractVersion(navigator.userAgent, /Chrom(e|ium)\/([0-9]+)\./, 2)),
        (u = 38),
        (l = function(e, t) {
          e && e.iceTransportPolicy && (e.iceTransports = e.iceTransportPolicy)
          var n = new webkitRTCPeerConnection(e, t),
            i = n.getStats.bind(n)
          return (
            (n.getStats = function(e, t, n) {
              var r = this,
                o = arguments
              if (arguments.length > 0 && typeof e == 'function') return i(e, t)
              var a = function(e) {
                var t = {}
                return (
                  e.result().forEach(function(e) {
                    var n = { id: e.id, timestamp: e.timestamp, type: e.type }
                    e.names().forEach(function(t) {
                      n[t] = e.stat(t)
                    }),
                    (t[n.id] = n)
                  }),
                  t
                )
              }
              if (arguments.length >= 2) {
                var s = function(e) {
                  o[1](a(e))
                }
                return i.apply(this, [s, arguments[0]])
              }
              return new Promise(function(t, n) {
                1 === o.length && e === null
                  ? i.apply(r, [
                    function(e) {
                      t.apply(null, [a(e)])
                    },
                    n
                  ])
                  : i.apply(r, [t, n])
              })
            }),
            n
          )
        }),
        ['createOffer', 'createAnswer'].forEach(function(e) {
          var t = webkitRTCPeerConnection.prototype[e]
          webkitRTCPeerConnection.prototype[e] = function() {
            var e = this
            if (arguments.length < 1 || (arguments.length === 1 && r(arguments[0]) === 'object')) {
              var n = arguments.length === 1 ? arguments[0] : void 0
              return new Promise(function(i, r) {
                t.apply(e, [i, r, n])
              })
            }
            return t.apply(this, arguments)
          }
        }),
        ['setLocalDescription', 'setRemoteDescription', 'addIceCandidate'].forEach(function(e) {
          var t = webkitRTCPeerConnection.prototype[e]
          webkitRTCPeerConnection.prototype[e] = function() {
            var e = arguments,
              n = this
            return new Promise(function(i, r) {
              t.apply(n, [
                e[0],
                function() {
                  i(), e.length >= 2 && e[1].apply(null, [])
                },
                function(t) {
                  r(t), e.length >= 3 && e[2].apply(null, [t])
                }
              ])
            })
          }
        })
        var m = function(e) {
          if ((void 0 === e ? 'undefined' : r(e)) !== 'object' || e.mandatory || e.optional) return e
          var t = {}
          return (
            Object.keys(e).forEach(function(n) {
              if (n !== 'require' && n !== 'advanced' && n !== 'mediaSource') {
                var i = r(e[n]) === 'object' ? e[n] : { ideal: e[n] }
                void 0 !== i.exact && typeof i.exact == 'number' && (i.min = i.max = i.exact)
                var o = function(e, t) {
                  return e ? e + t.charAt(0).toUpperCase() + t.slice(1) : t === 'deviceId' ? 'sourceId' : t
                }
                if (void 0 !== i.ideal) {
                  t.optional = t.optional || []
                  var a = {}
                  'number' === typeof i.ideal
                    ? ((a[o('min', n)] = i.ideal), t.optional.push(a), (a = {}), (a[o('max', n)] = i.ideal), t.optional.push(a))
                    : ((a[o('', n)] = i.ideal), t.optional.push(a))
                }
                void 0 !== i.exact && typeof i.exact != 'number'
                  ? ((t.mandatory = t.mandatory || {}), (t.mandatory[o('', n)] = i.exact))
                  : ['min', 'max'].forEach(function(e) {
                    void 0 !== i[e] && ((t.mandatory = t.mandatory || {}), (t.mandatory[o(e, n)] = i[e]))
                  })
              }
            }),
            e.advanced && (t.optional = (t.optional || []).concat(e.advanced)),
            t
          )
        }
        if (
          ((o = function(e, t, n) {
            return e.audio && (e.audio = m(e.audio)), e.video && (e.video = m(e.video)), p.log('chrome: ' + JSON.stringify(e)), navigator.webkitGetUserMedia(e, t, n)
          }),
          (navigator.getUserMedia = o),
          navigator.mediaDevices ||
              (navigator.mediaDevices = {
                getUserMedia: i,
                enumerateDevices: function() {
                  return new Promise(function(e) {
                    var t = { audio: 'audioinput', video: 'videoinput' }
                    return MediaStreamTrack.getSources(function(n) {
                      e(
                        n.map(function(e) {
                          return { label: e.label, kind: t[e.kind], deviceId: e.id, groupId: '' }
                        })
                      )
                    })
                  })
                }
              }),
          navigator.mediaDevices.getUserMedia)
        ) {
          var g = navigator.mediaDevices.getUserMedia.bind(navigator.mediaDevices)
          navigator.mediaDevices.getUserMedia = function(e) {
            return p.log('spec:   ' + JSON.stringify(e)), (e.audio = m(e.audio)), (e.video = m(e.video)), p.log('chrome: ' + JSON.stringify(e)), g(e)
          }
        } else
          navigator.mediaDevices.getUserMedia = function(e) {
            return i(e)
          }
        void 0 === navigator.mediaDevices.addEventListener &&
            (navigator.mediaDevices.addEventListener = function() {
              p.log('Dummy mediaDevices.addEventListener called.')
            }),
        void 0 === navigator.mediaDevices.removeEventListener &&
              (navigator.mediaDevices.removeEventListener = function() {
                p.log('Dummy mediaDevices.removeEventListener called.')
              }),
        (a = function(e, t) {
          c >= 43 ? (e.srcObject = t) : void 0 !== e.src ? (e.src = URL.createObjectURL(t)) : p.log('Error attaching stream to element.')
        }),
        (s = function(e, t) {
          c >= 43 ? (e.srcObject = t.srcObject) : (e.src = t.src)
        })
      } else
        navigator.mediaDevices && navigator.userAgent.match(/Edge\/(\d+).(\d+)$/)
          ? (p.log('This appears to be Edge'), (d = 'edge'), (c = p.extractVersion(navigator.userAgent, /Edge\/(\d+).(\d+)$/, 2)), (u = 12))
          : p.log('Browser does not appear to be WebRTC-capable') } else p.log('This does not appear to be a browser'), (d = 'not a browser')
      var v = {}
      try {
        Object.defineProperty(v, 'version', {
          set: function(e) {
            c = e
          }
        })
      } catch (e) {}
      var _
      l ? (_ = l) : typeof window !== 'undefined' && (_ = window.RTCPeerConnection),
        (e.exports = {
          RTCPeerConnection: _,
          getUserMedia: o,
          attachMediaStream: a,
          reattachMediaStream: s,
          webrtcDetectedBrowser: d,
          webrtcDetectedVersion: c,
          webrtcMinimumVersion: u,
          webrtcTesting: v,
          webrtcUtils: p
        })
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.SUBSCRIBE = t.PUBLISH = t.JOIN_GATEWAY = t.JOIN_CHOOSE_SERVER = t.SESSION_INIT = t.report = void 0)
      var i = n(1),
        r = (function(e) {
          if (e && e.__esModule) return e
          var t = {}
          if (e != null) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n])
          return (t.default = e), t
        })(i),
        o = n(0),
        a = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(o),
        s = n(10),
        d = { type: null, sid: null, lts: null, succ: null, cname: null, uid: null, peerid: null, cid: null, elaps: null, extend: null, vid: 0 },
        c = (function() {
          var e = {}
          return (
            (e.list = {}),
            (e.url = (0, s.shouldUseHttps)() ? 'https://' + r.EVENT_REPORT_DOMAIN + ':6443/events/report' : 'http://' + r.EVENT_REPORT_DOMAIN + ':6080/events/report'),
            (e.urlBackup = (0, s.shouldUseHttps)() ? 'https://' + r.EVENT_REPORT_BACKUP_DOMAIN + ':6443/events/report' : 'http://' + r.EVENT_REPORT_BACKUP_DOMAIN + ':6080/events/report'),
            (e.setProxyServer = function(t) {
              t &&
                ((e.url = (0, s.shouldUseHttps)()
                  ? 'https://' + t + '/rs/?h=' + r.EVENT_REPORT_DOMAIN + '&p=6443&d=events/report'
                  : 'http://' + t + '/rs/?h=' + r.EVENT_REPORT_DOMAIN + '&p=6080&d=events/report'),
                (e.urlBackup = (0, s.shouldUseHttps)()
                  ? 'https://' + t + '/rs/?h=' + r.EVENT_REPORT_BACKUP_DOMAIN + '&p=6443&d=events/report'
                  : 'http://' + t + '/rs/?h=' + r.EVENT_REPORT_BACKUP_DOMAIN + '&p=6080&d=events/report'),
                a.default.debug('reportProxyServerURL: ' + e.url),
                a.default.debug('reportProxyServerBackupURL: ' + e.urlBackup))
            }),
            (e.sessionInit = function(t, n) {
              n.sid = t
              var i = Object.assign(d, n)
              e.list[t] || (i.startTime = +new Date())
              var o = Object.assign({}, i)
              delete i.appid,
                delete i.mode,
                (e.list[t] = i),
                (o.extend = null),
                (o.ver = r.VERSION),
                (o.type = 'session_init'),
                (o.browser = navigator.userAgent),
                (o.lts = +new Date()),
                (o.elaps = o.lts - o.startTime),
                e.send(o)
            }),
            (e.joinChooseServer = function(t, n, i) {
              var r
              ;(r = i ? Object.assign(e.list[t], n) : Object.assign({}, e.list[t], n)), (r.type = 'join_choose_server')
              var o = +new Date()
              ;(r.ev_elaps = o - r.lts), (r.elaps = o - r.startTime), (r.lts = o), (r.serverList = JSON.stringify(r.serverList)), e.send(r)
            }),
            (e.joinGateway = function(t, n) {
              var i = Object.assign(e.list[t], n)
              i.type = 'join_gateway'
              var r = +new Date()
              ;(i.ev_elaps = r - i.lts), (i.elaps = r - i.startTime), (i.lts = r), e.send(i)
            }),
            (e.publish = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              i.type = 'publish'
              var r = +new Date()
              ;(i.ev_elaps = r - i.lts), (i.elaps = r - i.startTime), (i.lts = r), e.send(i)
            }),
            (e.subscribe = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              i.type = 'subscribe'
              var r = +new Date()
              ;(i.ev_elaps = r - i.lts), (i.elaps = r - i.startTime), (i.lts = r), e.send(i)
            }),
            (e.firstRemoteFrame = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              ;(i.type = 'first_remote_frame'), (i.lts = +new Date()), (i.elaps = i.lts - i.startTime), e.send(i)
            }),
            (e.streamSwitch = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              ;(i.type = 'stream_switch'), (i.isdual = n.isdual), (i.lts = +new Date()), (i.elaps = i.lts - i.startTime), e.send(i)
            }),
            (e.audioSendingStopped = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              ;(i.type = 'audio_sending_stopped'), (i.lts = +new Date()), (i.elaps = i.lts - i.startTime), e.send(i)
            }),
            (e.videoSendingStopped = function(t, n) {
              var i = Object.assign({}, e.list[t], n)
              ;(i.type = 'video_sending_stopped'), (i.lts = +new Date()), (i.elaps = i.lts - i.startTime), e.send(i)
            }),
            (e.send = function(t) {
              try {
                ;(0, s.post)(
                  e.url,
                  t,
                  null,
                  function(n) {
                    ;(0, s.post)(e.urlBackup, t, null, function(e) {}, { timeout: 1e4 })
                  },
                  { timeout: 1e4 }
                )
              } catch (e) {}
            }),
            e
          )
        })()
      ;(t.report = c), (t.SESSION_INIT = 'session_init'), (t.JOIN_CHOOSE_SERVER = 'join_choose_server'), (t.JOIN_GATEWAY = 'join_gateway'), (t.PUBLISH = 'publish'), (t.SUBSCRIBE = 'subscribe')
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      function r() {
        return (0, l.default)()
          .replace(/-/g, '')
          .toUpperCase()
      }
      Object.defineProperty(t, '__esModule', { value: !0 }),
        (t.random = t.safeCall = t.is32Uint = t.vsResHack = t.audioLevelHelper = t.generateSessionId = t.isLiveTranscodingValid = t.checkSystemRequirements = void 0)
      var o =
          typeof Symbol === 'function' && typeof Symbol.iterator === 'symbol'
            ? function(e) {
                return typeof e
              }
            : function(e) {
                return e && typeof Symbol === 'function' && e.constructor === Symbol && e !== Symbol.prototype ? 'symbol' : typeof e
              },
        a = n(2),
        s = (function(e) {
          if (e && e.__esModule) return e
          var t = {}
          if (e != null) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n])
          return (t.default = e), t
        })(a),
        d = n(0),
        c = i(d),
        u = n(16),
        l = i(u),
        p = function() {
          var e = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection,
            t = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.msGetUserMedia || navigator.mozGetUserMedia || (navigator.mediaDevices && navigator.mediaDevices.getUserMedia),
            n = window.WebSocket,
            i = !!e && !!t && !!n,
            r = !1
          return (
            c.default.debug(s.getBrowserInfo(), 'isAPISupport:' + i),
            s.isChrome() && s.getBrowserVersion() >= 58 && s.getBrowserOS() !== 'iOS' && (r = !0),
            s.isFireFox() && s.getBrowserVersion() >= 56 && (r = !0),
            s.isOpera() && s.getBrowserVersion() >= 45 && (r = !0),
            s.isSafari() && s.getBrowserVersion() >= 11 && (r = !0),
            (s.isWeChatBrowser() || s.isQQBrowser()) && s.getBrowserOS() !== 'iOS' && (r = !0),
            s.isSupportedPC() || s.isSupportedMobile() || (r = !1),
            i && r
          )
        },
        f = function() {
          var e = arguments[0]
          if (typeof e === 'function') {
            var t = Array.prototype.slice.call(arguments, 1)
            e.apply(null, t)
          }
        },
        m = window.AudioContext || window.webkitAudioContext,
        g = m ? new m() : null,
        v = function(e) {
          if (g) {
          {
return (
            (this.audioContext = g),
            (this.sourceNode = e.otWebkitAudioSource || this.audioContext.createMediaStreamSource(e)),
            (this.analyser = this.audioContext.createAnalyser()),
            (this.timeDomainData = new Uint8Array(this.analyser.frequencyBinCount)),
            this.sourceNode.connect(this.analyser),
            (this.getAudioLevel = function() {
              if (this.analyser) {
                this.analyser.getByteTimeDomainData(this.timeDomainData)
                for (var e = 0, t = 0; t < this.timeDomainData.length; t++) e = Math.max(e, Math.abs(this.timeDomainData[t] - 128))
                return e / 128
              }
              return c.default.warning("can't find analyser in audioLevelHelper"), 0
            }),
            this
          )
}
        },
        _ = function(e, t, n) {
          try {
            var i = document.createElement('video')
            i.setAttribute('autoplay', ''),
              i.setAttribute('muted', ''),
              i.setAttribute('playsinline', ''),
              i.setAttribute('style', 'position: absolute; top: 0; left: 0; width:1px; high:1px;'),
              document.body.appendChild(i),
              i.addEventListener('playing', function(e) {
                s.isFireFox() ? i.videoWidth && (t(i.videoWidth, i.videoHeight), document.body.removeChild(i)) : (t(i.videoWidth, i.videoHeight), document.body.removeChild(i))
              }),
              (i.srcObject = e)
          } catch (e) {
            n(e)
          }
        },
        S = function(e) {
          return !!(e >= 0 && e <= 4294967295)
        },
        E = function(e) {
          var t = ['lowLatency', 'userConfigExtraInfo', 'transcodingUsers']
          for (var n in e) {
          {
if (
            (n === 'lowLatency' && typeof e[n] != 'boolean') ||
              (n === 'userConfigExtraInfo' && o(e[n]) !== 'object') ||
              (n === 'transcodingUsers' && !h(e[n])) ||
              (!~t.indexOf(n) && typeof e[n] != 'number')
          )
            throw new Error('Param [' + n + '] is inVaild')
          }
          return !0
        },
        h = function(e) {
          for (var t = 0; t < e.length; t++) for (var n in e[t]) if (typeof e[t][n] !== 'number') throw new Error('Param user[' + t + '] - [' + n + '] is inVaild')
          return !0
        },
        I = function(e) {
          isNaN(e) && (e = 1e3)
          var t = +new Date()
          t = (9301 * t + 49297) % 233280
          var n = t / 233280
          return Math.ceil(n * e)
        }
      ;(t.checkSystemRequirements = p), (t.isLiveTranscodingValid = E), (t.generateSessionId = r), (t.audioLevelHelper = v), (t.vsResHack = _), (t.is32Uint = S), (t.safeCall = f), (t.random = I)
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = {
          FAILED: 'FAILED',
          INVALID_KEY: 'INVALID_KEY',
          INVALID_CLIENT_MODE: 'INVALID_CLIENT_MODE',
          INVALID_CLIENT_CODEC: 'INVALID_CLIENT_CODEC',
          CLIENT_MODE_CODEC_MISMATCH: 'CLIENT_MODE_CODEC_MISMATCH',
          INVALID_OPERATION: 'INVALID_OPERATION',
          INVALID_PARAMETER: 'INVALID_PARAMETER',
          INVALID_LOCAL_STREAM: 'INVALID_LOCAL_STREAM',
          INVALID_REMOTE_STREAM: 'INVALID_REMOTE_STREAM',
          INVALID_DYNAMIC_KEY: 'INVALID_DYNAMIC_KEY',
          DYNAMIC_KEY_TIMEOUT: 'DYNAMIC_KEY_TIMEOUT',
          NO_VOCS_AVAILABLE: 'NO_VOCS_AVAILABLE',
          NO_VOS_AVAILABLE: 'ERR_NO_VOS_AVAILABLE',
          JOIN_CHANNEL_TIMEOUT: 'ERR_JOIN_CHANNEL_TIMEOUT',
          NO_AVAILABLE_CHANNEL: 'NO_AVAILABLE_CHANNEL',
          LOOKUP_CHANNEL_TIMEOUT: 'LOOKUP_CHANNEL_TIMEOUT',
          LOOKUP_CHANNEL_REJECTED: 'LOOKUP_CHANNEL_REJECTED',
          OPEN_CHANNEL_TIMEOUT: 'OPEN_CHANNEL_TIMEOUT',
          OPEN_CHANNEL_REJECTED: 'OPEN_CHANNEL_REJECTED',
          REQUEST_DEFERRED: 'REQUEST_DEFERRED',
          STREAM_ALREADY_PUBLISHED: 'STREAM_ALREADY_PUBLISHED',
          STREAM_NOT_YET_PUBLISHED: 'STREAM_NOT_YET_PUBLISHED',
          JOIN_TOO_FREQUENT: 'JOIN_TOO_FREQUENT',
          SOCKET_ERROR: 'SOCKET_ERROR',
          SOCKET_DISCONNECTED: 'SOCKET_DISCONNECTED',
          PEERCONNECTION_FAILED: 'PEERCONNECTION_FAILED',
          CONNECT_GATEWAY_ERROR: 'CONNECT_GATEWAY_ERROR',
          SERVICE_NOT_AVAILABLE: 'SERVICE_NOT_AVAILABLE',
          JOIN_CHANNEL_FAILED: 'JOIN_CHANNEL_FAILED',
          PUBLISH_STREAM_FAILED: 'PUBLISH_STREAM_FAILED',
          UNPUBLISH_STREAM_FAILED: 'UNPUBLISH_STREAM_FAILED',
          SUBSCRIBE_STREAM_FAILED: 'SUBSCRIBE_STREAM_FAILED',
          UNSUBSCRIBE_STREAM_FAILED: 'UNSUBSCRIBE_STREAM_FAILED',
          NO_SUCH_REMOTE_STREAM: 'NO_SUCH_REMOTE_STREAM',
          ERR_FAILED: '1',
          ERR_INVALID_VENDOR_KEY: '101',
          ERR_INVALID_CHANNEL_NAME: '102',
          WARN_NO_AVAILABLE_CHANNEL: '103',
          WARN_LOOKUP_CHANNEL_TIMEOUT: '104',
          WARN_LOOKUP_CHANNEL_REJECTED: '105',
          WARN_OPEN_CHANNEL_TIMEOUT: '106',
          WARN_OPEN_CHANNEL_REJECTED: '107',
          WARN_REQUEST_DEFERRED: '108',
          ERR_DYNAMIC_KEY_TIMEOUT: '109',
          ERR_INVALID_DYNAMIC_KEY: '110',
          ERR_NO_VOCS_AVAILABLE: '2000',
          ERR_NO_VOS_AVAILABLE: '2001',
          ERR_JOIN_CHANNEL_TIMEOUT: '2002',
          IOS_NOT_SUPPORT: 'IOS_NOT_SUPPORT',
          WECHAT_NOT_SUPPORT: 'WECHAT_NOT_SUPPORT',
          SHARING_SCREEN_NOT_SUPPORT: 'SHARING_SCREEN_NOT_SUPPORT',
          STILL_ON_PUBLISHING: 'STILL_ON_PUBLISHING',
          LOW_STREAM_ALREADY_PUBLISHED: 'LOW_STREAM_ALREADY_PUBLISHED',
          LOW_STREAM_NOT_YET_PUBLISHED: 'LOW_STREAM_ALREADY_PUBLISHED',
          HIGH_STREAM_NOT_VIDEO_TRACE: 'HIGH_STREAM_NOT_VIDEO_TRACE',
          NOT_FIND_DEVICE_BY_LABEL: 'NOT_FIND_DEVICE_BY_LABEL',
          ENABLE_DUALSTREAM_FAILED: 'ENABLE_DUALSTREAM_FAILED',
          DISABLE_DUALSTREAM_FAILED: 'DISABLE_DUALSTREAM_FAILED',
          ELECTRON_NOT_SUPPORT_SHARING_SCREEN: 'ELECTRON_NOT_SUPPORT_SHARING_SCREEN'
        },
        r = {
          2000: 'ERR_NO_VOCS_AVAILABLE',
          2001: 'ERR_NO_VOS_AVAILABLE',
          2002: 'ERR_JOIN_CHANNEL_TIMEOUT',
          2003: 'WARN_REPEAT_JOIN',
          2004: 'ERR_JOIN_BY_MULTI_IP',
          101: 'ERR_INVALID_VENDOR_KEY',
          102: 'ERR_INVALID_CHANNEL_NAME',
          103: 'WARN_NO_AVAILABLE_CHANNEL',
          104: 'WARN_LOOKUP_CHANNEL_TIMEOUT',
          105: 'WARN_LOOKUP_CHANNEL_REJECTED',
          106: 'WARN_OPEN_CHANNEL_TIMEOUT',
          107: 'WARN_OPEN_CHANNEL_REJECTED',
          108: 'WARN_REQUEST_DEFERRED',
          109: 'ERR_DYNAMIC_KEY_TIMEOUT',
          110: 'ERR_NO_AUTHORIZED',
          111: 'ERR_VOM_SERVICE_UNAVAILABLE',
          112: 'ERR_NO_CHANNEL_AVAILABLE_CODE',
          113: 'ERR_TOO_MANY_USERS',
          114: 'ERR_MASTER_VOCS_UNAVAILABLE',
          115: 'ERR_INTERNAL_ERROR',
          116: 'ERR_NO_ACTIVE_STATUS',
          117: 'ERR_INVALID_UID',
          118: 'ERR_DYNAMIC_KEY_EXPIRED',
          119: 'ERR_STATIC_USE_DYANMIC_KE',
          120: 'ERR_DYNAMIC_USE_STATIC_KE',
          2: 'K_TIMESTAMP_EXPIRED',
          3: 'K_CHANNEL_PERMISSION_INVALID',
          4: 'K_CERTIFICATE_INVALID',
          5: 'K_CHANNEL_NAME_EMPTY',
          6: 'K_CHANNEL_NOT_FOUND',
          7: 'K_TICKET_INVALID',
          8: 'K_CHANNEL_CONFLICTED',
          9: 'K_SERVICE_NOT_READY',
          10: 'K_SERVICE_TOO_HEAVY',
          14: 'K_UID_BANNED',
          15: 'K_IP_BANNED',
          16: 'K_CHANNEL_BANNED'
        },
        o = [
          'ERR_NO_VOCS_AVAILABLE',
          'ERR_NO_VOS_AVAILABLE',
          'ERR_JOIN_CHANNEL_TIMEOUT',
          'WARN_LOOKUP_CHANNEL_TIMEOUT',
          'WARN_OPEN_CHANNEL_TIMEOUT',
          'ERR_VOM_SERVICE_UNAVAILABLE',
          'ERR_TOO_MANY_USERS',
          'ERR_MASTER_VOCS_UNAVAILABLE',
          'ERR_INTERNAL_ERROR'
        ],
        a = ['service unavailable', 'too many users', 'timeout code', 'canceled code']
      ;(t.default = i), (t.GatewayErrorCode = r), (t.JOIN_GS_TRYNEXT_LIST = o), (t.JOIN_CS_RETRY_LIST = a)
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.getDevices = t.createStream = t.Stream = void 0)
      var r =
          typeof Symbol === 'function' && typeof Symbol.iterator === 'symbol'
            ? function(e) {
                return typeof e
              }
            : function(e) {
                return e && typeof Symbol === 'function' && e.constructor === Symbol && e !== Symbol.prototype ? 'symbol' : typeof e
              },
        o = n(20),
        a = i(o),
        s = n(9),
        d = n(3),
        c = n(0),
        u = i(c),
        l = n(2),
        p = n(29),
        f = n(6),
        m = n(5),
        g = function(e) {
          function t() {
            return window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./) !== null && window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1] <= 35
          }
          function n(t, n) {
            return { width: { ideal: t }, height: { ideal: n }, deviceId: e.cameraId ? { exact: e.cameraId } : void 0 }
          }
          var i = (0, d.EventDispatcher)()
          if (
            ((i.params = Object.assign({}, e)),
            (i.stream = e.stream),
            (i.aux_stream = void 0),
            (i.url = e.url),
            (i.onClose = void 0),
            (i.local = !1),
            (i.video = !!e.video),
            (i.audio = !!e.audio),
            (i.screen = !!e.screen),
            (i.screenAttributes = { width: 1920, height: 1080, maxFr: 5, minFr: 1 }),
            (i.videoSize = e.videoSize),
            (i.player = void 0),
            (i.audioLevelHelper = null),
            (e.attributes = e.attributes || {}),
            (i.videoEnabled = !0),
            (i.audioEnabled = !0),
            (i.lowStream = null),
            (i.videoWidth = 0),
            (i.videoHeight = 0),
            (i.streamId = null),
            (i.streamId = e.streamID),
            (i.mirror = !1 !== e.mirror),
            !(void 0 === i.videoSize || (i.videoSize instanceof Array && i.videoSize.length === 4)))
          ) {
          { throw Error('Invalid Video Size')
          }(i.videoSize = [640, 480, 640, 480]), (void 0 !== e.local && !0 !== e.local) || (i.local = !0), (i.initialized = !i.local)
          var o = {
            true: !0,
            unspecified: !0,
            '90p_1': n(160, 90),
            '120p_1': n(160, 120),
            '120p_3': n(120, 120),
            '120p_4': n(212, 120),
            '180p_1': n(320, 180),
            '180p_3': n(180, 180),
            '180p_4': n(240, 180),
            '240p_1': n(320, 240),
            '240p_3': n(240, 240),
            '240p_4': n(424, 240),
            '360p_1': n(640, 360),
            '360p_3': n(360, 360),
            '360p_4': n(640, 360),
            '360p_6': n(360, 360),
            '360p_7': n(480, 360),
            '360p_8': n(480, 360),
            '360p_9': n(640, 360),
            '360p_10': n(640, 360),
            '360p_11': n(640, 360),
            '480p_1': n(640, 480),
            '480p_2': n(640, 480),
            '480p_3': n(480, 480),
            '480p_4': n(640, 480),
            '480p_6': n(480, 480),
            '480p_8': n(848, 480),
            '480p_9': n(848, 480),
            '480p_10': n(640, 480),
            '720p_1': n(1280, 720),
            '720p_2': n(1280, 720),
            '720p_3': n(1280, 720),
            '720p_5': n(960, 720),
            '720p_6': n(960, 720),
            '1080p_1': n(1920, 1080),
            '1080p_2': n(1920, 1080),
            '1080p_3': n(1920, 1080),
            '1080p_5': n(1920, 1080),
            '1440p_1': n(2560, 1440),
            '1440p_2': n(2560, 1440),
            '4k_1': n(3840, 2160),
            '4k_3': n(3840, 2160)
          }
          return (
            (i.unmuteAudio = void 0),
            (i.muteAudio = void 0),
            (i.unmuteVideo = void 0),
            (i.muteVideo = void 0),
            (i.setVideoResolution = function(t) {
              return (t += ''), void 0 !== o[t] && ((e.video = o[t]), (e.attributes = e.attributes || {}), (e.attributes.resolution = t), !0)
            }),
            (i.setVideoFrameRate = function(t) {
              return (
                !(0, l.isFireFox)() &&
                ((void 0 === t ? 'undefined' : r(t)) === 'object' &&
                  t instanceof Array &&
                  t.length > 1 &&
                  ((e.attributes = e.attributes || {}), (e.attributes.minFrameRate = t[0]), (e.attributes.maxFrameRate = t[1]), !0))
              )
            }),
            (i.setVideoBitRate = function(t) {
              return (
                (void 0 === t ? 'undefined' : r(t)) === 'object' &&
                t instanceof Array &&
                t.length > 1 &&
                ((e.attributes = e.attributes || {}), (e.attributes.minVideoBW = t[0]), (e.attributes.maxVideoBW = t[1]), !0)
              )
            }),
            (i.setScreenProfile = function(e) {
              if (typeof e === 'string' && i.screen) {
                switch (e) {
                  case '480p_1':
                    ;(i.screenAttributes.width = 640), (i.screenAttributes.height = 480), (i.screenAttributes.maxFr = 5), (i.screenAttributes.minFr = 1)
                    break
                  case '480p_2':
                    ;(i.screenAttributes.width = 640), (i.screenAttributes.height = 480), (i.screenAttributes.maxFr = 30), (i.screenAttributes.minFr = 25)
                    break
                  case '720p_1':
                    ;(i.screenAttributes.width = 1280), (i.screenAttributes.height = 720), (i.screenAttributes.maxFr = 5), (i.screenAttributes.minFr = 1)
                    break
                  case '720p_2':
                    ;(i.screenAttributes.width = 1280), (i.screenAttributes.height = 720), (i.screenAttributes.maxFr = 30), (i.screenAttributes.minFr = 25)
                    break
                  case '1080p_1':
                    ;(i.screenAttributes.width = 1920), (i.screenAttributes.height = 1080), (i.screenAttributes.maxFr = 5), (i.screenAttributes.minFr = 1)
                    break
                  case '1080p_2':
                    ;(i.screenAttributes.width = 1920), (i.screenAttributes.height = 1080), (i.screenAttributes.maxFr = 30), (i.screenAttributes.minFr = 25)
                }
                return !0
              }
              return !1
            }),
            (i.setVideoProfileCustom = function(e) {
              i.setVideoResolution(e[0]), i.setVideoFrameRate([e[1], e[1]]), i.setVideoBitRate([e[2], e[2]])
            }),
            (i.setVideoProfileCustomPlus = function(t) {
              console.log(t),
                (e.video = n(t.width, t.height)),
                (e.attributes = e.attributes || {}),
                (e.attributes.resolution = t.width + 'x' + t.height),
                i.setVideoFrameRate([t.framerate, t.framerate]),
                i.setVideoBitRate([t.bitrate, t.bitrate])
            }),
            (i.setVideoProfile = function(e) {
              if (((i.profile = e), typeof e === 'string' && i.video)) {
                switch (e) {
                  case '120p':
                  case '120P':
                  case '120p_1':
                  case '120P_1':
                    i.setVideoResolution('120p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 65])
                    break
                  case '120p_3':
                  case '120P_3':
                    i.setVideoResolution('120p_3'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 50])
                    break
                  case '180p':
                  case '180P':
                  case '180p_1':
                  case '180P_1':
                    i.setVideoResolution('180p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 140])
                    break
                  case '180p_3':
                  case '180P_3':
                    i.setVideoResolution('180p_3'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 100])
                    break
                  case '180p_4':
                  case '180P_4':
                    i.setVideoResolution('180p_4'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 120])
                    break
                  case '240p':
                  case '240P':
                  case '240p_1':
                  case '240P_1':
                    i.setVideoResolution('240p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 200])
                    break
                  case '240p_3':
                  case '240P_3':
                    i.setVideoResolution('240p_3'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 140])
                    break
                  case '240p_4':
                  case '240P_4':
                    i.setVideoResolution('240p_4'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([10, 220])
                    break
                  case '360p':
                  case '360P':
                  case '360p_1':
                  case '360P_1':
                    i.setVideoResolution('360p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 400])
                    break
                  case '360p_3':
                  case '360P_3':
                    i.setVideoResolution('360p_3'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 260])
                    break
                  case '360p_4':
                  case '360P_4':
                    i.setVideoResolution('360p_4'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 600])
                    break
                  case '360p_6':
                  case '360P_6':
                    i.setVideoResolution('360p_6'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 400])
                    break
                  case '360p_7':
                  case '360P_7':
                    i.setVideoResolution('360p_7'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 320])
                    break
                  case '360p_8':
                  case '360P_8':
                    i.setVideoResolution('360p_8'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 490])
                    break
                  case '360p_9':
                  case '360P_9':
                    i.setVideoResolution('360p_9'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 800])
                    break
                  case '360p_10':
                  case '360P_10':
                    i.setVideoResolution('360p_10'), i.setVideoFrameRate([24, 24]), i.setVideoBitRate([20, 800])
                    break
                  case '360p_11':
                  case '360P_11':
                    i.setVideoResolution('360p_11'), i.setVideoFrameRate([24, 24]), i.setVideoBitRate([20, 1e3])
                    break
                  case '480p':
                  case '480P':
                  case '480p_1':
                  case '480P_1':
                    i.setVideoResolution('480p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 500])
                    break
                  case '480p_2':
                  case '480P_2':
                    i.setVideoResolution('480p_2'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 1e3])
                    break
                  case '480p_3':
                  case '480P_3':
                    i.setVideoResolution('480p_3'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 400])
                    break
                  case '480p_4':
                  case '480P_4':
                    i.setVideoResolution('480p_4'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 750])
                    break
                  case '480p_6':
                  case '480P_6':
                    i.setVideoResolution('480p_6'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 600])
                    break
                  case '480p_8':
                  case '480P_8':
                    i.setVideoResolution('480p_8'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 610])
                    break
                  case '480p_9':
                  case '480P_9':
                    i.setVideoResolution('480p_9'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([20, 930])
                    break
                  case '480p_10':
                  case '480P_10':
                    i.setVideoResolution('480p_10'), i.setVideoFrameRate([10, 10]), i.setVideoBitRate([20, 400])
                    break
                  case '720p':
                  case '720P':
                  case '720p_1':
                  case '720P_1':
                    i.setVideoResolution('720p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([30, 1130])
                    break
                  case '720p_2':
                  case '720P_2':
                    i.setVideoResolution('720p_2'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([30, 2e3])
                    break
                  case '720p_3':
                  case '720P_3':
                    i.setVideoResolution('720p_3'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([30, 1710])
                    break
                  case '720p_5':
                  case '720P_5':
                    i.setVideoResolution('720p_5'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([30, 910])
                    break
                  case '720p_6':
                  case '720P_6':
                    i.setVideoResolution('720p_6'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([30, 1380])
                    break
                  case '1080p':
                  case '1080P':
                  case '1080p_1':
                  case '1080P_1':
                    i.setVideoResolution('1080p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([50, 2080])
                    break
                  case '1080p_2':
                  case '1080P_2':
                    i.setVideoResolution('1080p_2'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([50, 3e3])
                    break
                  case '1080p_3':
                  case '1080P_3':
                    i.setVideoResolution('1080p_3'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([50, 3150])
                    break
                  case '1080p_5':
                  case '1080P_5':
                    i.setVideoResolution('1080p_5'), i.setVideoFrameRate([60, 60]), i.setVideoBitRate([50, 4780])
                    break
                  case '1440p':
                  case '1440P':
                  case '1440p_1':
                  case '1440P_1':
                    i.setVideoResolution('1440p_1'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([50, 4850])
                    break
                  case '1440p_2':
                  case '1440P_2':
                    i.setVideoResolution('1440p_2'), i.setVideoFrameRate([60, 60]), i.setVideoBitRate([50, 7350])
                    break
                  case '4k':
                  case '4K':
                  case '4k_1':
                  case '4K_1':
                    i.setVideoResolution('4k_1'), i.setVideoFrameRate([30, 30]), i.setVideoBitRate([50, 8910])
                    break
                  case '4k_3':
                  case '4K_3':
                    i.setVideoResolution('4k_3'), i.setVideoFrameRate([60, 60]), i.setVideoBitRate([50, 13500])
                    break
                  default:
                    i.setVideoResolution('480p_1'), i.setVideoFrameRate([15, 15]), i.setVideoBitRate([20, 500])
                }
                return !0
              }
              return !1
            }),
            (i.getId = function() {
              return i.streamId
            }),
            (i.getAttributes = function() {
              return e.screen ? i.screenAttributes : e.attributes
            }),
            (i.hasAudio = function() {
              return i.audio
            }),
            (i.hasVideo = function() {
              return i.video
            }),
            (i.hasScreen = function() {
              return i.screen
            }),
            (i.isVideoOn = function() {
              return (i.hasVideo() || i.hasScreen()) && i.videoEnabled
            }),
            (i.isAudioOn = function() {
              return i.hasAudio() && i.audioEnabled
            }),
            (i.init = function(n, o) {
              var a = (new Date().getTime(), arguments[2])
              if ((void 0 === a && (a = 2), !0 === i.initialized)) return void (typeof o === 'function' && o({ type: 'warning', msg: 'STREAM_ALREADY_INITIALIZED' }))
              if (!0 !== i.local) return void (typeof o === 'function' && o({ type: 'warning', msg: 'STREAM_NOT_LOCAL' }))
              try {
                if ((e.audio || e.video || e.screen) && void 0 === e.url) {
                  u.default.debug('Requested access to local media')
                  var d = e.video
                  if (e.screen) var c = { video: d, audio: e.audio, screen: !0, data: !0, extensionId: e.extensionId, attributes: i.screenAttributes, fake: e.fake, mediaSource: e.mediaSource }
                  else {
                    var c = { video: d, audio: e.audio, fake: e.fake }
                    if (!t()) {
                      var p = 30,
                        m = 30
                      if (
                        (void 0 !== e.attributes.minFrameRate && (p = e.attributes.minFrameRate),
                        void 0 !== e.attributes.maxFrameRate && (m = e.attributes.maxFrameRate),
                        !0 === c.audio && ((c.audio = !e.microphoneId || { deviceId: { exact: e.microphoneId } }), i.audioProcessing))
                      ) {
                        var g = {}
                        void 0 !== i.audioProcessing.AGC &&
                          ((0, l.isFireFox)()
                            ? (g.autoGainControl = i.audioProcessing.AGC)
                            : (0, l.isChrome)() && ((g.googAutoGainControl = i.audioProcessing.AGC), (g.googAutoGainControl2 = i.audioProcessing.AGC))),
                          0 !== Object.keys(g).length && (!0 === c.audio ? (c.audio = { mandatory: g }) : (c.audio = Object.assign(c.audio, g)))
                      }
                      !0 === c.video
                        ? ((c.video = { width: { ideal: i.videoSize[0] }, height: { ideal: i.videoSize[1] }, frameRate: { ideal: p, max: m } }), i.setVideoBitRate([500, 500]))
                        : r(c.video) === 'object' && (c.video.frameRate = { ideal: p, max: m })
                    }
                  }
                  u.default.debug(c)
                  var v = Object.assign({}, c)
                  if (
                    ((0, s.GetUserMedia)(
                      v,
                      function(t) {
                        var r = t.getVideoTracks().length > 0,
                          a = t.getAudioTracks().length > 0
                        return v.video && !r && v.audio && !a
                          ? (u.default.error('Media access: NO_CAMERA_MIC_PERMISSION'), o && o('NO_CAMERA_MIC_PERMISSION'))
                          : v.video && !r
                            ? (u.default.error('Media access: NO_CAMERA_PERMISSION'), o && o('NO_CAMERA_PERMISSION'))
                            : v.screen && !r
                              ? (u.default.error('Media access: NO_SCREEN_PERMISSION'), o && o('NO_SCREEN_PERMISSION'))
                              : v.audio && !a
                                ? (u.default.error('Media access: NO_MIC_PERMISSION'), o && o('NO_MIC_PERMISSION'))
                                : (u.default.debug('User has granted access to local media'),
                                  i.dispatchEvent({ type: 'accessAllowed' }),
                                  (i.stream = t),
                                  (i.initialized = !0),
                                  n && n(),
                                  i.hasVideo() &&
                                    (0, f.vsResHack)(
                                      t,
                                      function(e, t) {
                                        ;(i.videoWidth = e), (i.videoHeight = t)
                                      },
                                      function(e) {
                                        u.default.warning('vsResHack failed:' + e)
                                      }
                                    ),
                                  void (
                                    e.screen &&
                                    (0, l.isChrome)() &&
                                    i.stream &&
                                    i.stream.getVideoTracks()[0] &&
                                    (i.stream.getVideoTracks()[0].onended = function() {
                                      i.dispatchEvent({ type: 'stopScreenSharing' })
                                    })
                                  ))
                      },
                      function(e) {
                        var t = { type: 'error', msg: e.name || e }
                        switch (t.msg) {
                          case 'Starting video failed':
                          case 'TrackStartError':
                            if (((i.videoSize = void 0), a > 0)) {
                            {
return void setTimeout(function() {
                              i.init(n, o, a - 1)
                            }, 1)
                            }
                            t.msg = 'MEDIA_OPTION_INVALID'
                            break
                          case 'DevicesNotFoundError':
                            t.msg = 'DEVICES_NOT_FOUND'
                            break
                          case 'NotSupportedError':
                            t.msg = 'NOT_SUPPORTED'
                            break
                          case 'PermissionDeniedError':
                            ;(t.msg = 'PERMISSION_DENIED'), i.dispatchEvent({ type: 'accessDenied' })
                            break
                          case 'PERMISSION_DENIED':
                            i.dispatchEvent({ type: 'accessDenied' })
                            break
                          case 'InvalidStateError':
                            ;(t.msg = 'PERMISSION_DENIED'), i.dispatchEvent({ type: 'accessDenied' })
                            break
                          case 'NotAllowedError':
                            i.dispatchEvent({ type: 'accessDenied' })
                            break
                          case 'ConstraintNotSatisfiedError':
                            t.msg = 'CONSTRAINT_NOT_SATISFIED'
                            break
                          default:
                            t.msg || (t.msg = 'UNDEFINED')
                        }
                        u.default.error('Media access:', t.msg), typeof o === 'function' && o(t)
                      }
                    ),
                    e.screen && e.audio)
                  ) {
                    var _ = { video: !1, audio: c.audio }
                    u.default.debug(_),
                      (0, s.GetUserMedia)(
                        _,
                        function(e) {
                          u.default.info('User has granted access to auxiliary local media.'), i.dispatchEvent({ type: 'accessAllowed' }), (i.aux_stream = e)
                        },
                        function(e) {
                          var t = { type: 'error', msg: e.name || e }
                          switch (t.msg) {
                            case 'Starting video failed':
                            case 'TrackStartError':
                              if (((i.videoSize = void 0), a > 0))
                                {return void setTimeout(function() {
                                i.init(n, o, a - 1)
                              }, 1)}
                              t.msg = 'MEDIA_OPTION_INVALID'
                              break
                            case 'DevicesNotFoundError':
                              t.msg = 'DEVICES_NOT_FOUND'
                              break
                            case 'NotSupportedError':
                              t.msg = 'NOT_SUPPORTED'
                              break
                            case 'PermissionDeniedError':
                            case 'InvalidStateError':
                              ;(t.msg = 'PERMISSION_DENIED'), i.dispatchEvent({ type: 'accessDenied' })
                              break
                            case 'PERMISSION_DENIED':
                            case 'NotAllowedError':
                              i.dispatchEvent({ type: 'accessDenied' })
                              break
                            case 'ConstraintNotSatisfiedError':
                              t.msg = 'CONSTRAINT_NOT_SATISFIED'
                              break
                            default:
                              t.msg || (t.msg = 'UNDEFINED')
                          }
                          u.default.error('Media access:', t.msg), typeof o === 'function' && o(t)
                        }
                      )
                  }
                } else typeof o === 'function' && o({ type: 'warning', msg: 'STREAM_HAS_NO_MEDIA_ATTRIBUTES' })
              } catch (e) {
                u.default.error('Stream init:', e), typeof o === 'function' && o({ type: 'error', msg: e.message || e })
              }
            }),
            (i.close = function() {
              if ((u.default.debug('Close stream with id', i.streamId), void 0 !== i.stream)) {
                var e = i.stream.getTracks()
                for (var t in e) e.hasOwnProperty(t) && e[t].stop()
                i.stream = void 0
              }
              ;(i.initialized = !1), (i.unmuteAudio = void 0), (i.muteAudio = void 0), (i.unmuteVideo = void 0), (i.muteVideo = void 0), i.lowStream && i.lowStream.close()
            }),
            (i.enableAudio = function() {
              return (
                u.default.debug('Enable audio stream with id', i.streamId),
                !(!i.hasAudio() || !i.initialized || void 0 === i.stream || !0 === i.stream.getAudioTracks()[0].enabled) &&
                  (void 0 !== i.unmuteAudio && i.unmuteAudio(), (i.audioEnabled = !0), (i.stream.getAudioTracks()[0].enabled = !0), !0)
              )
            }),
            (i.disableAudio = function() {
              return (
                u.default.debug('Disable audio stream with id', i.streamId),
                !!(i.hasAudio() && i.initialized && void 0 !== i.stream && i.stream.getAudioTracks()[0].enabled) &&
                  (void 0 !== i.muteAudio && i.muteAudio(),
                  (i.audioEnabled = !1),
                  (i.stream.getAudioTracks()[0].enabled = !1),
                  i.sid && m.report.audioSendingStopped(i.sid, { succ: !0, reason: 'muteAudio' }),
                  !0)
              )
            }),
            (i.enableVideo = function() {
              return (
                u.default.debug('Enable video stream with id', i.streamId),
                !(!i.initialized || void 0 === i.stream || !i.stream.getVideoTracks().length || !0 === i.stream.getVideoTracks()[0].enabled) &&
                  (void 0 !== i.unmuteVideo && i.unmuteVideo(), (i.videoEnabled = !0), (i.stream.getVideoTracks()[0].enabled = !0), i.lowStream && i.lowStream.enableVideo(), !0)
              )
            }),
            (i.disableVideo = function() {
              return (
                u.default.debug('Disable video stream with id', i.streamId),
                !!(i.initialized && void 0 !== i.stream && i.stream.getVideoTracks().length && i.stream.getVideoTracks()[0].enabled) &&
                  (void 0 !== i.muteVideo && i.muteVideo(),
                  (i.videoEnabled = !1),
                  (i.stream.getVideoTracks()[0].enabled = !1),
                  i.lowStream && i.lowStream.disableVideo(),
                  i.sid && m.report.videoSendingStopped(i.sid, { succ: !0, reason: 'muteVideo' }),
                  !0)
              )
            }),
            (i.play = function(e, t) {
              ;(i.showing = !1),
                !i.local || i.video || i.screen
                  ? void 0 !== e && ((i.player = new a.default({ id: i.getId(), stream: i, elementID: e, options: void 0, url: t })), (i.showing = !0))
                  : i.hasAudio() && ((i.player = new a.default({ id: i.getId(), stream: i, elementID: e, options: void 0, url: t })), (i.showing = !0))
            }),
            (i.stop = function() {
              u.default.debug('Stop stream player with id', i.streamId), void 0 !== i.player && i.player.destroy()
            }),
            (i.getStats = function(e) {
              i.pc && i.pc.getStats
                ? i.pc.getStats(function(t) {
                    if (i.pc.isSubscriber) {
                      var n = (0, p.subscribeStatsFilter)(t)
                      ;(0, l.isFireFox)() && ((n.videoReceivedResolutionHeight = i.videoHeight + ''), (n.videoReceivedResolutionWidth = i.videoWidth + '')), e && e((0, p.subscribeStatsFilter)(t))
                    } else {
                      var n = (0, p.publishStatsFilter)(t)
                      ;((0, l.isSafari)() || (0, l.isFireFox)()) && ((n.videoSendResolutionHeight = i.videoHeight + ''), (n.videoSendResolutionWidth = i.videoWidth + '')),
                        ((0, l.isSafari)() || (0, l.isFireFox)()) &&
                          i.uplinkStats &&
                          ((n.videoSendBandwidth = i.uplinkStats.uplink_available_bandwidth + ''), (n.videoSendPacketsLost = i.uplinkStats.uplink_cumulative_lost + '')),
                        e && e(n)
                    }
                  })
                : u.default.warning('use getStats after peerConnection established')
            }),
            (i.getAudioLevel = function() {
              return i.audioLevelHelper
                ? i.audioLevelHelper.getAudioLevel()
                : i.stream
                  ? i.stream.getAudioTracks().length !== 0
                    ? ((i.audioLevelHelper = new f.audioLevelHelper(i.stream)), i.audioLevelHelper.getAudioLevel())
                    : void u.default.warning("can't get audioLevel beacuse no audio trace in stream")
                  : (u.default.warning("can't get audioLevel beacuse no stream exist"), 0)
            }),
            i
          )
        },
        v = function(e) {
          return u.default.debug('Create stream'), g(e)
        },
        _ = function(e, t) {
          navigator.mediaDevices && navigator.mediaDevices.enumerateDevices
            ? navigator.mediaDevices
                .enumerateDevices()
                .then(function(t) {
                  return e(t)
                })
                .catch(function(e) {
                  return t && t(e.name + ': ' + e.message)
                })
            : (u.default.warning('enumerateDevices() not supported.'), t && t('enumerateDevices() not supported'))
        }
      ;(t.Stream = g), (t.createStream = v), (t.getDevices = _)
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.GetUserMedia = t.Connection = void 0)
      var r = n(23),
        o = i(r),
        a = n(24),
        s = i(a),
        d = n(25),
        c = i(d),
        u = n(26),
        l = i(u),
        p = n(27),
        f = i(p),
        m = n(0),
        g = i(m),
        v = n(28),
        _ = i(v),
        S = n(7),
        E = 103,
        h = function(e) {
          var t = {}
          if (((e.session_id = E += 1), typeof window !== 'undefined' && window.navigator)) {
          {
if (window.navigator.userAgent.match('Firefox') !== null) (t.browser = 'mozilla'), (t = (0, f.default)(e))
          else if (window.navigator.userAgent.indexOf('Safari') > -1 && navigator.userAgent.indexOf('Chrome') === -1) g.default.debug('Safari'), (t = (0, c.default)(e)), (t.browser = 'safari')
          else if (window.navigator.userAgent.indexOf('MSIE ')) (t = (0, s.default)(e)), (t.browser = 'ie')
          else if (window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1] >= 26) (t = (0, s.default)(e)), (t.browser = 'chrome-stable')
          else {
            if (!(window.navigator.userAgent.toLowerCase().indexOf('chrome') >= 40)) throw ((t.browser = 'none'), 'WebRTC stack not available')
            ;(t = (0, o.default)(e)), (t.browser = 'chrome-canary')
          }
} else g.default.error('Publish/subscribe video/audio streams not supported yet'), (t = (0, l.default)(e))
          return t
        },
        I = function(e, t, n) {
          if ((({}.config = e), (navigator.getMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia), e.screen)) {
            var i = null
            try {
              i = window.require('electron')
            } catch (e) {}
            if (i) return void (i.desktopCapturer ? new _.default(e, t, n).shareScreen(i) : n(S.ErrorCodes.ELECTRON_NOT_SUPPORT_SHARING_SCREEN))
            if ((g.default.debug('Screen access requested'), window.navigator.userAgent.match('Firefox') !== null)) {
              if (!~['screen', 'window', 'application'].indexOf(e.mediaSource)) return n && n('Invalid mediaSource, mediaSource should be one of [screen, window, application]')
              var r = {}
              void 0 != e.video.mandatory ? ((r.video = e.video), (r.video.mediaSource = e.mediaSource)) : (r = { video: { mediaSource: e.mediaSource } }), navigator.getMedia(r, t, n)
            } else if (window.navigator.userAgent.match('Chrome') !== null) {
              if (window.navigator.appVersion.match(/Chrome\/([\w\W]*?)\./)[1] < 34) return void n({ code: 'This browser does not support screen sharing' })
              var o = 'okeephmleflklcdebijnponpabbmmgeo'
              e.extensionId && (g.default.debug('extensionId supplied, using ' + e.extensionId), (o = e.extensionId)), g.default.debug('Screen access on chrome stable, looking for extension')
              try {
                chrome.runtime.sendMessage(o, { getStream: !0 }, function(i) {
                  if (void 0 === i) return g.default.debug('Access to screen denied'), void n({ code: 'Access to screen denied' })
                  var o = i.streamId,
                    a = e.attributes.width,
                    s = e.attributes.height,
                    d = e.attributes.maxFr,
                    c = e.attributes.minFr
                  ;(r = { video: { mandatory: { chromeMediaSource: 'desktop', chromeMediaSourceId: o, maxHeight: s, maxWidth: a, maxFrameRate: d, minFrameRate: c } } }), navigator.getMedia(r, t, n)
                })
              } catch (e) {
                g.default.debug('AgoraRTC screensharing plugin is not accessible')
                var a = { code: 'no_plugin_present' }
                return void n(a)
              }
            } else g.default.debug('This browser does not support screenSharing')
          } else {
          { window.navigator.userAgent.indexOf('Safari') > -1 && navigator.userAgent.indexOf('Chrome') === -1
            ? navigator.mediaDevices
              .getUserMedia(e)
              .then(t)
              .catch(n)
            : typeof navigator != 'undefined' && navigator.getMedia
              ? navigator.getMedia(e, t, n)
              : g.default.error('Video/audio streams not supported yet')
}
        }
      ;(t.Connection = h), (t.GetUserMedia = I)
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.shouldUseHttps = t.post = void 0)
      var i = n(1),
        r = (function(e) {
          if (e && e.__esModule) return e
          var t = {}
          if (e != null) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n])
          return (t.default = e), t
        })(i),
        o = function(e, t, n, i) {
          var o = new XMLHttpRequest()
          ;(o.timeout = t.timeout || r.HTTP_CONNECT_TIMEOUT),
            o.open('POST', e, !0),
            o.setRequestHeader('Content-type', 'application/json; charset=utf-8'),
            (o.onload = function(e) {
              n && n(o.responseText)
            }),
            (o.onerror = function(t) {
              i && i(t, e)
            }),
            (o.ontimeout = function(t) {
              i && i(t, e)
            }),
            o.send(JSON.stringify(t))
        },
        a = function() {
          return document.location.protocol == 'https:'
        }
      ;(t.post = o), (t.shouldUseHttps = a)
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = ['live', 'rtc', 'web', 'interop', 'h264_interop', 'web-only'],
        r = ['vp8', 'h264'],
        o = ['aes-128-xts', 'aes-256-xts', 'aes-128-ecb']
      ;(t.AUDIO_SAMPLE_RATE_32000 = 32e3),
        (t.AUDIO_SAMPLE_RATE_44100 = 44100),
        (t.AUDIO_SAMPLE_RATE_48000 = 48e3),
        (t.VIDEO_CODEC_PROFILE_BASELINE = 66),
        (t.VIDEO_CODEC_PROFILE_MAIN = 77),
        (t.VIDEO_CODEC_PROFILE_HIGH = 100),
        (t.REMOTE_VIDEO_STREAM_HIGH = 0),
        (t.REMOTE_VIDEO_STREAM_LOW = 1),
        (t.REMOTE_VIDEO_STREAM_MEDIUM = 2),
        (t.CLIENT_MODE_LIVE = 'live'),
        (t.CLIENT_MODE_RTC = 'rtc'),
        (t.CLIENT_MODE_WEB = 'web'),
        (t.CLIENT_MODE_INTEROP = 'interop'),
        (t.CLIENT_MODE_H264_INTEROP = 'h264_interop'),
        (t.CLIENT_MODE_WEBONLY = 'web-only'),
        (t.CLIENT_MODES = i),
        (t.CLIENT_CODEC_VP8 = 'vp8'),
        (t.CLIENT_CODEC_H264 = 'h264'),
        (t.CLIENT_CODECS = r),
        (t.ENCRYPTION_MODES = o),
        (t.ENCRYPTION_MODE_AES128XTS = 'aes-128-xts'),
        (t.ENCRYPTION_MODE_AES256XTS = 'aes-256-xts'),
        (t.ENCRYPTION_MODE_AES128ECB = 'aes-128-ecb'),
        (t.ENCRYPTION_MODE_NONE = 'none')
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function(e) {
        e && e.apply(this, [].slice.call(arguments, 1))
      }
      t.default = i
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.getGatewayList = void 0)
      var r = n(1),
        o = (function(e) {
          if (e && e.__esModule) return e
          var t = {}
          if (e != null) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n])
          return (t.default = e), t
        })(r),
        a = n(0),
        s = i(a),
        d = n(10),
        c = n(5),
        u = n(7),
        l = (i(u),
        function(e, t, n, i) {
          var r = new Date().getTime(),
            o = { key: t.appId, cname: t.cname, uid: t.uid, multi_ip: t.multiIP || {} }
          ;(0, d.post)(
            e,
            o,
            function(o) {
              var a = JSON.parse(o),
                s = a.error
              void 0 != s ? (c.report.joinChooseServer(t.sid, { lts: r, succ: !1, csAddr: e, serverList: null, ec: s }), i('Get server node failed [' + s + ']', e, s)) : n(a, e)
            },
            function(e, n) {
              e.type === 'timeout'
                ? (c.report.joinChooseServer(t.sid, { lts: r, succ: !1, csAddr: n, serverList: null, ec: 'timeout' }), i('Connect choose server timeout', n))
                : c.report.joinChooseServer(t.sid, { lts: r, succ: !1, csAddr: n, serverList: null, ec: 'server_wrong' })
            }
          )
        }),
        p = function(e, t, n) {
          for (
            var i = new Date().getTime(),
              r = !1,
              a = !0,
              p = function(n, o) {
                if (r) c.report.joinChooseServer(e.sid, { lts: i, succ: !0, csAddr: o, serverList: n.gateway_addr, cid: n.cid + '', uid: n.uid + '', ec: null }, !1)
                else {
                  if ((clearTimeout(_), (r = !0), s.default.debug('Get gateway address:', n.gateway_addr), e.proxyServer)) {
                    for (var a = n.gateway_addr, d = 0; d < a.length; d++) {
                      var u = a[d].split(':')
                      n.gateway_addr[d] = e.proxyServer + '/ws/?h=' + u[0] + '&p=' + u[1]
                    }
                    s.default.debug('Get gateway address:', n.gateway_addr)
                  }
                  t(n), c.report.joinChooseServer(e.sid, { lts: i, succ: !0, csAddr: o, serverList: n.gateway_addr, cid: n.cid + '', uid: n.uid + '', ec: null }, !0)
                }
              },
              f = function(e, t, i) {
                a && (s.default.error(e, t, i), i && !u.JOIN_CS_RETRY_LIST.includes(i) && ((a = !1), n(i)))
              },
              m = o.WEBCS_DOMAIN,
              g = 0;
            g < m.length;
            ++g
          ) {
            var v
            ;(v = e.proxyServer
              ? (0, d.shouldUseHttps)()
                ? 'https://' + e.proxyServer + '/cs/?h=' + m[g] + '&p=5668&d=getwebgw/jsonp'
                : 'http://' + e.proxyServer + '/cs/?h=' + m[g] + '&p=5669&d=getwebgw/jsonp'
              : (0, d.shouldUseHttps)()
                ? 'https://' + m[g] + ':5668/getwebgw/jsonp'
                : 'http://' + m[g] + ':5669/getwebgw/jsonp'),
              s.default.debug('Connect to backup_choose_server: ' + v),
              l(v, e, p, f)
          }
          var _ = setTimeout(function() {
            if (!r) {
            { for (var t = o.WEBCS_DOMAIN_BACKUP_LIST, n = 0; n < t.length; ++n) {
              var i
                ;(i = e.proxyServer
                ? (0, d.shouldUseHttps)()
                  ? 'https://' + e.proxyServer + '/cs/?h=' + t[n] + '&p=5668&d=getwebgw/jsonp'
                  : 'http://' + e.proxyServer + '/cs/?h=' + t[n] + '&p=5669&d=getwebgw/jsonp'
                : (0, d.shouldUseHttps)()
                  ? 'https://' + t[n] + ':5668/getwebgw/jsonp'
                  : 'http://' + t[n] + ':5669/getwebgw/jsonp'),
              s.default.debug('Connect to backup_choose_server: ' + i),
              l(i, e, p, f)
            }
            }
          }, 1e3)
          setTimeout(function() {
            !r && a && n()
          }, o.WEBCS_BACKUP_CONNECT_TIMEOUT)
        },
        f = function(e, t, n) {
          var i = !1,
            r = null,
            o = 1
          !(function n() {
            i ||
              p(
                e,
                function(e) {
                  ;(i = !0), clearTimeout(r), t(e)
                },
                function(e) {
                  if (e) return void s.default.info('Join failed: ' + e)
                  s.default.debug('Request gateway list will be restart in ' + o + 's'),
                    (r = setTimeout(function() {
                      n()
                    }, 1e3 * o)),
                    (o = o >= 3600 ? 3600 : 2 * o)
                }
              )
          })()
        }
      t.getGatewayList = f
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(1),
        r = n(0),
        o = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(r),
        a = n(6),
        s = n(8),
        d = n(11),
        c = n(30)
      t.default = {
        TranscodingUser: c.TranscodingUser,
        LiveTranscoding: c.LiveTranscoding,
        createClient: c.createClient,
        createStream: s.createStream,
        Logger: o.default,
        getDevices: s.getDevices,
        checkSystemRequirements: a.checkSystemRequirements,
        VERSION: i.VERSION,
        AUDIO_SAMPLE_RATE_32000: d.AUDIO_SAMPLE_RATE_32000,
        AUDIO_SAMPLE_RATE_44100: d.AUDIO_SAMPLE_RATE_44100,
        AUDIO_SAMPLE_RATE_48000: d.AUDIO_SAMPLE_RATE_48000,
        VIDEO_CODEC_PROFILE_BASELINE: d.VIDEO_CODEC_PROFILE_BASELINE,
        VIDEO_CODEC_PROFILE_MAIN: d.VIDEO_CODEC_PROFILE_MAIN,
        VIDEO_CODEC_PROFILE_HIGH: d.VIDEO_CODEC_PROFILE_HIGH,
        REMOTE_VIDEO_STREAM_HIGH: d.REMOTE_VIDEO_STREAM_HIGH,
        REMOTE_VIDEO_STREAM_LOW: d.REMOTE_VIDEO_STREAM_LOW,
        REMOTE_VIDEO_STREAM_MEDIUM: d.REMOTE_VIDEO_STREAM_MEDIUM
      }
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function() {
        var e = new Date()
        return e.toTimeString().split(' ')[0] + ':' + e.getMilliseconds()
      }
      t.getTimestamp = i
    },
    function(e, t, n) {
      function i(e, t, n) {
        var i = (t && n) || 0
        typeof e === 'string' && ((t = e == 'binary' ? new Array(16) : null), (e = null)), (e = e || {})
        var a = e.random || (e.rng || r)()
        if (((a[6] = (15 & a[6]) | 64), (a[8] = (63 & a[8]) | 128), t)) for (var s = 0; s < 16; ++s) t[i + s] = a[s]
        return t || o(a)
      }
      var r = n(17),
        o = n(19)
      e.exports = i
    },
    function(e, t, n) {
      ;(function(t) {
        var n,
          i = t.crypto || t.msCrypto
        if (i && i.getRandomValues) {
          var r = new Uint8Array(16)
          n = function() {
            return i.getRandomValues(r), r
          }
        }
        if (!n) {
          var o = new Array(16)
          n = function() {
            for (var e, t = 0; t < 16; t++) (3 & t) == 0 && (e = 4294967296 * Math.random()), (o[t] = (e >>> ((3 & t) << 3)) & 255)
            return o
          }
        }
        e.exports = n
      }.call(t, n(18)))
    },
    function(e, t) {
      var n
      n = (function() {
        return this
      })()
      try {
        n = n || Function('return this')() || (0, eval)('this')
      } catch (e) {
        typeof window === 'object' && (n = window)
      }
      e.exports = n
    },
    function(e, t) {
      function n(e, t) {
        var n = t || 0,
          r = i
        return (
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]] +
          '-' +
          r[e[n++]] +
          r[e[n++]] +
          '-' +
          r[e[n++]] +
          r[e[n++]] +
          '-' +
          r[e[n++]] +
          r[e[n++]] +
          '-' +
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]] +
          r[e[n++]]
        )
      }
      for (var i = [], r = 0; r < 256; ++r) i[r] = (r + 256).toString(16).substr(1)
      e.exports = n
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 })
      var r = n(21),
        o = i(r),
        a = n(22),
        s = i(a),
        d = n(0),
        c = i(d),
        u = n(4),
        l = function(e) {
          var t = (0, o.default)({})
          if (
            ((t.id = e.id),
            (t.url = e.url),
            (t.stream = e.stream.stream),
            (t.elementID = e.elementID),
            (t.destroy = function() {
              ;(t.video.srcObject = null), (t.audio.srcObject = null), t.video.pause(), delete t.resizer, document.getElementById(t.div.id) && t.parentNode.removeChild(t.div)
            }),
            (t.resize = function() {
              var n = t.container.offsetWidth,
                i = t.container.offsetHeight
              e.stream.screen
                ? 0.75 * n < i
                  ? ((t.video.style.width = n + 'px'), (t.video.style.height = 0.75 * n + 'px'), (t.video.style.top = -((0.75 * n) / 2 - i / 2) + 'px'), (t.video.style.left = '0px'))
                  : ((t.video.style.height = i + 'px'), (t.video.style.width = (4 / 3) * i + 'px'), (t.video.style.left = -(((4 / 3) * i) / 2 - n / 2) + 'px'), (t.video.style.top = '0px'))
                : (n === t.containerWidth && i === t.containerHeight) ||
                  (0.75 * n > i
                    ? ((t.video.style.width = n + 'px'), (t.video.style.height = 0.75 * n + 'px'), (t.video.style.top = -((0.75 * n) / 2 - i / 2) + 'px'), (t.video.style.left = '0px'))
                    : ((t.video.style.height = i + 'px'), (t.video.style.width = (4 / 3) * i + 'px'), (t.video.style.left = -(((4 / 3) * i) / 2 - n / 2) + 'px'), (t.video.style.top = '0px'))),
                (t.containerWidth = n),
                (t.containerHeight = i)
            }),
            (t.div = document.createElement('div')),
            t.div.setAttribute('id', 'player_' + t.id),
            e.stream.video
              ? t.div.setAttribute('style', 'width: 100%; height: 100%; position: relative; background-color: black; overflow: hidden;')
              : t.div.setAttribute('style', 'width: 100%; height: 100%; position: relative; overflow: hidden;'),
            (t.video = document.createElement('video')),
            t.video.setAttribute('id', 'video' + t.id),
            e.stream.local && !e.stream.screen
              ? e.stream.mirror
                ? t.video.setAttribute('style', 'width: 100%; height: 100%; position: absolute; transform: rotateY(180deg);')
                : t.video.setAttribute('style', 'width: 100%; height: 100%; position: absolute; ')
              : e.stream.video
                ? (t.video.setAttribute('style', 'width: 100%; height: 100%; position: absolute;'),
                  window.navigator.userAgent.indexOf('Safari') > -1 && navigator.userAgent.indexOf('Chrome') === -1 && t.video.setAttribute('controls', ''))
                : e.stream.screen
                  ? t.video.setAttribute('style', 'width: 100%; height: 100%; position: absolute;')
                  : t.video.setAttribute('style', 'width: 100%; height: 100%; position: absolute; display: none;'),
            t.video.setAttribute('autoplay', ''),
            t.video.setAttribute('muted', ''),
            t.video.setAttribute('playsinline', ''),
            e.stream.local && ((t.video.volume = 0), t.video.setAttribute('muted', '')),
            (t.audio = document.createElement('audio')),
            t.audio.setAttribute('id', 'audio' + t.id),
            t.audio.setAttribute('autoplay', ''),
            e.stream.local && ((t.audio.volume = 0), t.audio.setAttribute('muted', '')),
            void 0 !== t.elementID
              ? (document.getElementById(t.elementID).appendChild(t.div), (t.container = document.getElementById(t.elementID)))
              : (document.body.appendChild(t.div), (t.container = document.body)),
            (t.parentNode = t.div.parentNode),
            t.video.addEventListener('playing', function(e) {
              !(function e() {
                if (t.video.videoWidth * t.video.videoHeight > 4) return void c.default.debug('video dimensions:', t.video.videoWidth, t.video.videoHeight)
                setTimeout(e, 50)
              })()
            }),
            (t.containerWidth = 0),
            (t.containerHeight = 0),
            (t.resizer = new s.default(t.container, t.resize)),
            t.resize(),
            e.stream.hasVideo() || e.stream.hasScreen())
          ) {
          { t.div.appendChild(t.video), t.div.appendChild(t.audio), (0, u.attachMediaStream)(t.video, e.stream.stream), (0, u.attachMediaStream)(t.audio, e.stream.stream) } else if ((!e.stream.local && t.video.removeAttribute('muted'), t.div.appendChild(t.video), window.MediaStream)) {
            var n = new MediaStream(e.stream.stream.getAudioTracks())
            t.video.srcObject = n
          } else t.video.srcObject = e.stream.stream
          return t
        }
      t.default = l
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(3),
        r = function(e) {
          var t = (0, i.EventDispatcher)(e)
          return (t.url = '.'), t
        }
      t.default = r
    },
    function(e, t, n) {
      var i, r
      !(function(o, a) {
        ;(i = a), void 0 !== (r = typeof i === 'function' ? i.call(t, n, t, e) : i) && (e.exports = r)
      })(0, function() {
        function e(e, t) {
          var n = Object.prototype.toString.call(e),
            i =
              n === '[object Array]' ||
              n === '[object NodeList]' ||
              n === '[object HTMLCollection]' ||
              n === '[object Object]' ||
              (typeof jQuery !== 'undefined' && e instanceof jQuery) ||
              (typeof Elements !== 'undefined' && e instanceof Elements),
            r = 0,
            o = e.length
          if (i) for (; r < o; r++) t(e[r])
          else t(e)
        }
        if (typeof window === 'undefined') return null
        var t =
            window.requestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            function(e) {
              return window.setTimeout(e, 20)
            },
          n = function(i, r) {
            function o() {
              var e = []
              this.add = function(t) {
                e.push(t)
              }
              var t, n
              ;(this.call = function() {
                for (t = 0, n = e.length; t < n; t++) e[t].call()
              }),
                (this.remove = function(i) {
                  var r = []
                  for (t = 0, n = e.length; t < n; t++) e[t] !== i && r.push(e[t])
                  e = r
                }),
                (this.length = function() {
                  return e.length
                })
            }
            function a(e, t) {
              return e.currentStyle ? e.currentStyle[t] : window.getComputedStyle ? window.getComputedStyle(e, null).getPropertyValue(t) : e.style[t]
            }
            function s(e, n) {
              if (e.resizedAttached) {
                if (e.resizedAttached) return void e.resizedAttached.add(n)
              } else (e.resizedAttached = new o()), e.resizedAttached.add(n)
              ;(e.resizeSensor = document.createElement('div')), (e.resizeSensor.className = 'resize-sensor')
              var i = 'position: absolute; left: 0; top: 0; right: 0; bottom: 0; overflow: hidden; z-index: -1; visibility: hidden;',
                r = 'position: absolute; left: 0; top: 0; transition: 0s;'
              ;(e.resizeSensor.style.cssText = i),
                (e.resizeSensor.innerHTML =
                  '<div class="resize-sensor-expand" style="' +
                  i +
                  '"><div style="' +
                  r +
                  '"></div></div><div class="resize-sensor-shrink" style="' +
                  i +
                  '"><div style="' +
                  r +
                  ' width: 200%; height: 200%"></div></div>'),
                e.appendChild(e.resizeSensor),
                'static' == a(e, 'position') && (e.style.position = 'relative')
              var s,
                d,
                c,
                u,
                l = e.resizeSensor.childNodes[0],
                p = l.childNodes[0],
                f = e.resizeSensor.childNodes[1],
                m = e.offsetWidth,
                g = e.offsetHeight,
                v = function() {
                  ;(p.style.width = '100000px'), (p.style.height = '100000px'), (l.scrollLeft = 1e5), (l.scrollTop = 1e5), (f.scrollLeft = 1e5), (f.scrollTop = 1e5)
                }
              v()
              var _ = function() {
                  ;(d = 0), s && ((m = c), (g = u), e.resizedAttached && e.resizedAttached.call())
                },
                S = function() {
                  ;(c = e.offsetWidth), (u = e.offsetHeight), (s = c != m || u != g), s && !d && (d = t(_)), v()
                },
                E = function(e, t, n) {
                  e.attachEvent ? e.attachEvent('on' + t, n) : e.addEventListener(t, n)
                }
              E(l, 'scroll', S), E(f, 'scroll', S)
            }
            e(i, function(e) {
              s(e, r)
            }),
              (this.detach = function(e) {
                n.detach(i, e)
              })
          }
        return (
          (n.detach = function(t, n) {
            e(t, function(e) {
              ;(e.resizedAttached && typeof n === 'function' && (e.resizedAttached.remove(n), e.resizedAttached.length())) ||
                (e.resizeSensor && (e.contains(e.resizeSensor) && e.removeChild(e.resizeSensor), delete e.resizeSensor, delete e.resizedAttached))
            })
          }),
          n
        )
      })
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(0),
        r = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(i),
        o = (n(4),
        function(e) {
          var t = {},
            n = webkitRTCPeerConnection
          ;(t.pc_config = { iceServers: [] }),
            (t.con = { optional: [{ DtlsSrtpKeyAgreement: !0 }] }),
            e.iceServers instanceof Array
              ? (t.pc_config.iceServers = e.iceServers)
              : (e.stunServerUrl &&
                  (e.stunServerUrl instanceof Array
                    ? e.stunServerUrl.map(function(e) {
                        'string' === typeof e && e !== '' && t.pc_config.iceServers.push({ url: e })
                      })
                    : typeof e.stunServerUrl === 'string' && e.stunServerUrl !== '' && t.pc_config.iceServers.push({ url: e.stunServerUrl })),
                e.turnServer &&
                  (e.turnServer instanceof Array
                    ? e.turnServer.map(function(e) {
                        'string' === typeof e.url && e.url !== '' && t.pc_config.iceServers.push({ username: e.username, credential: e.password, url: e.url })
                      })
                    : typeof e.turnServer.url === 'string' &&
                      e.turnServer.url !== '' &&
                      t.pc_config.iceServers.push({ username: e.turnServer.username, credential: e.turnServer.password, url: e.turnServer.url }))),
            void 0 === e.audio && (e.audio = !0),
            void 0 === e.video && (e.video = !0),
            (t.mediaConstraints = { mandatory: { OfferToReceiveVideo: e.video, OfferToReceiveAudio: e.audio } }),
            (t.roapSessionId = 103),
            (t.peerConnection = new n(t.pc_config, t.con)),
            (t.peerConnection.onicecandidate = function(e) {
              e.candidate
                ? (t.iceCandidateCount += 1)
                : (r.default.debug('PeerConnection State: ' + t.peerConnection.iceGatheringState),
                  void 0 === t.ices && (t.ices = 0),
                  (t.ices = t.ices + 1),
                  t.ices >= 1 && t.moreIceComing && ((t.moreIceComing = !1), t.markActionNeeded()))
            })
          var i = function(t) {
            var n, i
            return (
              e.minVideoBW &&
                e.maxVideoBW &&
                ((n = t.match(/m=video.*\r\n/)),
                (i = n[0] + 'b=AS:' + e.maxVideoBW + '\r\n'),
                (t = t.replace(n[0], i)),
                r.default.debug('Set Video Bitrate - min:' + e.minVideoBW + ' max:' + e.maxVideoBW)),
              e.maxAudioBW && ((n = t.match(/m=audio.*\r\n/)), (i = n[0] + 'b=AS:' + e.maxAudioBW + '\r\n'), (t = t.replace(n[0], i))),
              t
            )
          }
          return (
            (t.processSignalingMessage = function(e) {
              var n,
                r = JSON.parse(e)
              ;(t.incomingMessage = r),
                'new' === t.state
                  ? r.messageType === 'OFFER'
                    ? ((n = { sdp: r.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)), (t.state = 'offer-received'), t.markActionNeeded())
                    : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state)
                  : t.state === 'offer-sent'
                    ? r.messageType === 'ANSWER'
                      ? ((n = { sdp: r.sdp, type: 'answer' }), (n.sdp = i(n.sdp)), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)), t.sendOK(), (t.state = 'established'))
                      : r.messageType === 'pr-answer'
                        ? ((n = { sdp: r.sdp, type: 'pr-answer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)))
                        : r.messageType === 'offer'
                          ? t.error('Not written yet')
                          : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state)
                    : t.state === 'established' &&
                      (r.messageType === 'OFFER'
                        ? ((n = { sdp: r.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)), (t.state = 'offer-received'), t.markActionNeeded())
                        : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state))
            }),
            (t.addStream = function(e) {
              t.peerConnection.addStream(e), t.markActionNeeded()
            }),
            (t.removeStream = function() {
              t.markActionNeeded()
            }),
            (t.close = function() {
              ;(t.state = 'closed'), t.peerConnection.close()
            }),
            (t.markActionNeeded = function() {
              ;(t.actionNeeded = !0),
                t.doLater(function() {
                  t.onstablestate()
                })
            }),
            (t.doLater = function(e) {
              window.setTimeout(e, 1)
            }),
            (t.onstablestate = function() {
              var e
              if (t.actionNeeded) {
                if (t.state === 'new' || t.state === 'established') {
                {
t.peerConnection.createOffer(
                  function(e) {
                    if (((e.sdp = i(e.sdp)), r.default.debug('Changed', e.sdp), e.sdp !== t.prevOffer))
                      return t.peerConnection.setLocalDescription(e), (t.state = 'preparing-offer'), void t.markActionNeeded()
                    r.default.debug('Not sending a new offer')
                  },
                  function(e) {
                    r.default.debug('peer connection create offer failed ', e)
                  },
                  t.mediaConstraints
                ) } else if (t.state === 'preparing-offer') {
                  if (t.moreIceComing) return
                  ;(t.prevOffer = t.peerConnection.localDescription.sdp), t.sendMessage('OFFER', t.prevOffer), (t.state = 'offer-sent')
                } else if (t.state === 'offer-received') {
                { t.peerConnection.createAnswer(
                  function(e) {
                    if ((t.peerConnection.setLocalDescription(e), (t.state = 'offer-received-preparing-answer'), t.iceStarted)) return void t.markActionNeeded()
                    var n = new Date()
                    r.default.debug(n.getTime() + ': Starting ICE in responder'), (t.iceStarted = !0)
                  },
                  function(e) {
                    r.default.debug('peer connection create answer failed ', e)
                  },
                  t.mediaConstraints
                ) } else if (t.state === 'offer-received-preparing-answer') {
                  if (t.moreIceComing) return
                  ;(e = t.peerConnection.localDescription.sdp), t.sendMessage('ANSWER', e), (t.state = 'established')
                } else t.error('Dazed and confused in state ' + t.state + ', stopping here')
                t.actionNeeded = !1
              }
            }),
            (t.sendOK = function() {
              t.sendMessage('OK')
            }),
            (t.sendMessage = function(e, n) {
              var i = {}
              ;(i.messageType = e),
                (i.sdp = n),
                e === 'OFFER'
                  ? ((i.offererSessionId = t.sessionId), (i.answererSessionId = t.otherSessionId), (i.seq = t.sequenceNumber += 1), (i.tiebreaker = Math.floor(429496723 * Math.random() + 1)))
                  : ((i.offererSessionId = t.incomingMessage.offererSessionId), (i.answererSessionId = t.sessionId), (i.seq = t.incomingMessage.seq)),
                t.onsignalingmessage(JSON.stringify(i))
            }),
            (t.error = function(e) {
              throw 'Error in RoapOnJsep: ' + e
            }),
            (t.sessionId = t.roapSessionId += 1),
            (t.sequenceNumber = 0),
            (t.actionNeeded = !1),
            (t.iceStarted = !1),
            (t.moreIceComing = !0),
            (t.iceCandidateCount = 0),
            (t.onsignalingmessage = e.callback),
            (t.peerConnection.onopen = function() {
              t.onopen && t.onopen()
            }),
            (t.peerConnection.onaddstream = function(e) {
              t.onaddstream && t.onaddstream(e)
            }),
            (t.peerConnection.onremovestream = function(e) {
              t.onremovestream && t.onremovestream(e)
            }),
            (t.peerConnection.oniceconnectionstatechange = function(e) {
              t.oniceconnectionstatechange && t.oniceconnectionstatechange(e.currentTarget.iceConnectionState)
            }),
            (t.onaddstream = null),
            (t.onremovestream = null),
            (t.state = 'new'),
            t.markActionNeeded(),
            t
          )
        })
      t.default = o
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(0),
        r = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(i),
        o = n(4),
        a = function(e) {
          var t = {},
            n = o.RTCPeerConnection
          ;(t.isSubscriber = e.isSubscriber),
            (t.pc_config = { iceServers: [{ url: 'stun:webcs.agora.io:3478' }] }),
            (t.con = { optional: [{ DtlsSrtpKeyAgreement: !0 }] }),
            e.iceServers instanceof Array
              ? (t.pc_config.iceServers = e.iceServers)
              : (e.stunServerUrl &&
                  (e.stunServerUrl instanceof Array
                    ? e.stunServerUrl.map(function(e) {
                        'string' === typeof e && e !== '' && t.pc_config.iceServers.push({ url: e })
                      })
                    : typeof e.stunServerUrl === 'string' && e.stunServerUrl !== '' && t.pc_config.iceServers.push({ url: e.stunServerUrl })),
                e.turnServer &&
                  (e.turnServer instanceof Array
                    ? e.turnServer.map(function(e) {
                        typeof e.url === 'string' && e.url !== '' && t.pc_config.iceServers.push({ username: e.username, credential: e.password, url: e.url })
                      })
                    : typeof e.turnServer.url === 'string' &&
                      e.turnServer.url !== '' &&
                      (t.pc_config.iceServers.push({
                        username: e.turnServer.username,
                        credential: e.turnServer.credential,
                        credentialType: 'password',
                        urls: 'turn:' + e.turnServer.url + ':' + e.turnServer.udpport + '?transport=udp'
                      }),
                      typeof e.turnServer.tcpport === 'string' &&
                        e.turnServer.tcpport !== '' &&
                        t.pc_config.iceServers.push({
                          username: e.turnServer.username,
                          credential: e.turnServer.credential,
                          credentialType: 'password',
                          urls: 'turn:' + e.turnServer.url + ':' + e.turnServer.tcpport + '?transport=tcp'
                        }),
                      !0 === e.turnServer.forceturn && (t.pc_config.iceTransportPolicy = 'relay')))),
            void 0 === e.audio && (e.audio = !0),
            void 0 === e.video && (e.video = !0),
            (t.mediaConstraints = { mandatory: { OfferToReceiveVideo: e.video, OfferToReceiveAudio: e.audio } }),
            (t.roapSessionId = 103),
            (t.peerConnection = new n(t.pc_config, t.con)),
            (t.peerConnection.onicecandidate = function(e) {
              var n, i, o, a
              ;(n = t.peerConnection.localDescription.sdp),
                (i = n.match(/a=candidate:.+typ\ssrflx.+\r\n/)),
                (o = n.match(/a=candidate:.+typ\shost.+\r\n/)),
                (a = n.match(/a=candidate:.+typ\srelay.+\r\n/)),
                t.iceCandidateCount === 0 &&
                  (t.timeout = setTimeout(function() {
                    t.moreIceComing && ((t.moreIceComing = !1), t.markActionNeeded())
                  }, 1e3)),
                (i === null && o === null && a === null) ||
                  void 0 !== t.ice ||
                  (r.default.debug('srflx candidate : ' + i + ' relay candidate: ' + a + ' host candidate : ' + o), clearTimeout(t.timeout), (t.ice = 0), (t.moreIceComing = !1), t.markActionNeeded()),
                (t.iceCandidateCount = t.iceCandidateCount + 1)
            }),
            r.default.debug('Created webkitRTCPeerConnnection with config "' + JSON.stringify(t.pc_config) + '".')
          var i = function(t) {
              return e.screen && (t = t.replace('a=x-google-flag:conference\r\n', '')), t
            },
            a = function(t) {
              var n, i
              return (
                e.minVideoBW &&
                  e.maxVideoBW &&
                  ((n = t.match(/m=video.*\r\n/)),
                  (i = n[0] + 'b=AS:' + e.maxVideoBW + '\r\n'),
                  (t = t.replace(n[0], i)),
                  r.default.debug('Set Video Bitrate - min:' + e.minVideoBW + ' max:' + e.maxVideoBW)),
                e.maxAudioBW && ((n = t.match(/m=audio.*\r\n/)), (i = n[0] + 'b=AS:' + e.maxAudioBW + '\r\n'), (t = t.replace(n[0], i))),
                t
              )
            }
          return (
            (t.processSignalingMessage = function(e) {
              var n,
                r = JSON.parse(e)
              ;(t.incomingMessage = r),
                'new' === t.state
                  ? r.messageType === 'OFFER'
                    ? ((n = { sdp: r.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)), (t.state = 'offer-received'), t.markActionNeeded())
                    : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state)
                  : t.state === 'offer-sent'
                    ? r.messageType === 'ANSWER'
                      ? ((n = { sdp: r.sdp, type: 'answer' }),
                        (n.sdp = i(n.sdp)),
                        (n.sdp = a(n.sdp)),
                        t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)),
                        t.sendOK(),
                        (t.state = 'established'))
                      : r.messageType === 'pr-answer'
                        ? ((n = { sdp: r.sdp, type: 'pr-answer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)))
                        : r.messageType === 'offer'
                          ? t.error('Not written yet')
                          : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state)
                    : t.state === 'established' &&
                      (r.messageType === 'OFFER'
                        ? ((n = { sdp: r.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(n)), (t.state = 'offer-received'), t.markActionNeeded())
                        : t.error('Illegal message for this state: ' + r.messageType + ' in state ' + t.state))
            }),
            (t.getStatsRate = function(e) {
              t.getStats(function(t) {
                e(t)
              })
            }),
            (t.getStats = function(e) {
              t.peerConnection.getStats(null, function(n) {
                var i = [],
                  r = [],
                  o = null
                Object.keys(n).forEach(function(e) {
                  var t = n[e]
                  r.push(t), (t.type !== 'ssrc' && t.type !== 'VideoBwe') || ((o = t.timestamp), i.push(t))
                }),
                  i.push({ id: 'time', startTime: t.connectedTime, timestamp: o || new Date() }),
                  e(i, r)
              })
            }),
            (t.addStream = function(e) {
              t.peerConnection.addStream(e), t.markActionNeeded()
            }),
            (t.removeStream = function() {
              t.markActionNeeded()
            }),
            (t.close = function() {
              ;(t.state = 'closed'), t.peerConnection.close()
            }),
            (t.markActionNeeded = function() {
              ;(t.actionNeeded = !0),
                t.doLater(function() {
                  t.onstablestate()
                })
            }),
            (t.doLater = function(e) {
              window.setTimeout(e, 1)
            }),
            (t.onstablestate = function() {
              var e
              if (t.actionNeeded) {
                if (t.state === 'new' || t.state === 'established') {
                { t.peerConnection.createOffer(
                  function(e) {
                    if (((e.sdp = a(e.sdp)), e.sdp !== t.prevOffer)) return t.peerConnection.setLocalDescription(e), (t.state = 'preparing-offer'), void t.markActionNeeded()
                    r.default.debug('Not sending a new offer')
                  },
                  function(e) {
                    r.default.debug('peer connection create offer failed ', e)
                  },
                  t.mediaConstraints
                ) } else if (t.state === 'preparing-offer') {
                  if (t.moreIceComing) return
                  ;(t.prevOffer = t.peerConnection.localDescription.sdp),
                    (t.prevOffer = t.prevOffer.replace(/a=candidate:.+typ\shost.+\r\n/g, 'a=candidate:2243255435 1 udp 2122194687 192.168.0.1 30000 typ host generation 0 network-id 1\r\n')),
                    t.sendMessage('OFFER', t.prevOffer),
                    (t.state = 'offer-sent')
                } else if (t.state === 'offer-received') {
                { t.peerConnection.createAnswer(
                  function(e) {
                    if ((t.peerConnection.setLocalDescription(e), (t.state = 'offer-received-preparing-answer'), t.iceStarted)) return void t.markActionNeeded()
                    var n = new Date()
                    r.default.debug(n.getTime() + ': Starting ICE in responder'), (t.iceStarted = !0)
                  },
                  function(e) {
                    r.default.debug('peer connection create answer failed ', e)
                  },
                  t.mediaConstraints
                )
} else if (t.state === 'offer-received-preparing-answer') {
                  if (t.moreIceComing) return
                  ;(e = t.peerConnection.localDescription.sdp), t.sendMessage('ANSWER', e), (t.state = 'established')
                } else t.error('Dazed and confused in state ' + t.state + ', stopping here')
                t.actionNeeded = !1
              }
            }),
            (t.sendOK = function() {
              t.sendMessage('OK')
            }),
            (t.sendMessage = function(e, n) {
              var i = {}
              ;(i.messageType = e),
                (i.sdp = n),
                e === 'OFFER'
                  ? ((i.offererSessionId = t.sessionId), (i.answererSessionId = t.otherSessionId), (i.seq = t.sequenceNumber += 1), (i.tiebreaker = Math.floor(429496723 * Math.random() + 1)))
                  : ((i.offererSessionId = t.incomingMessage.offererSessionId), (i.answererSessionId = t.sessionId), (i.seq = t.incomingMessage.seq)),
                t.onsignalingmessage(JSON.stringify(i))
            }),
            (t.error = function(e) {
              throw 'Error in RoapOnJsep: ' + e
            }),
            (t.sessionId = t.roapSessionId += 1),
            (t.sequenceNumber = 0),
            (t.actionNeeded = !1),
            (t.iceStarted = !1),
            (t.moreIceComing = !0),
            (t.iceCandidateCount = 0),
            (t.onsignalingmessage = e.callback),
            (t.peerConnection.ontrack = function(e) {
              t.onaddstream && (t.onaddstream(e, 'ontrack'), (t.peerConnection.onaddstream = null))
            }),
            (t.peerConnection.onaddstream = function(e) {
              t.onaddstream && (t.onaddstream(e, 'onaddstream'), (t.peerConnection.ontrack = null))
            }),
            (t.peerConnection.onremovestream = function(e) {
              t.onremovestream && t.onremovestream(e)
            }),
            (t.peerConnection.oniceconnectionstatechange = function(e) {
              e.currentTarget.iceConnectionState === 'connected' && (t.connectedTime = new Date()), t.oniceconnectionstatechange && t.oniceconnectionstatechange(e.currentTarget.iceConnectionState)
            }),
            (t.onaddstream = null),
            (t.onremovestream = null),
            (t.state = 'new'),
            t.markActionNeeded(),
            t
          )
        }
      t.default = a
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(0),
        r = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(i),
        o = n(4),
        a = function(e) {
          var t = {}
          o.RTCPeerConnection
          ;(t.isSubscriber = e.isSubscriber),
            (t.pc_config = { iceServers: [{ urls: ['stun:72.251.224.27:3478', 'stun:stun.l.google.com:19302'] }], bundlePolicy: 'max-bundle' }),
            (t.con = { optional: [{ DtlsSrtpKeyAgreement: !0 }] }),
            e.iceServers instanceof Array
              ? (t.pc_config.iceServers = e.iceServers)
              : (e.stunServerUrl &&
                  (e.stunServerUrl instanceof Array
                    ? e.stunServerUrl.map(function(e) {
                        'string' === typeof e && e !== '' && t.pc_config.iceServers.push({ url: e })
                      })
                    : typeof e.stunServerUrl === 'string' && e.stunServerUrl !== '' && t.pc_config.iceServers.push({ url: e.stunServerUrl })),
                e.turnServer &&
                  (e.turnServer instanceof Array
                    ? e.turnServer.map(function(e) {
                        typeof e.url === 'string' && e.url !== '' && t.pc_config.iceServers.push({ username: e.username, credential: e.password, url: e.url })
                      })
                    : typeof e.turnServer.url === 'string' &&
                      e.turnServer.url !== '' &&
                      (t.pc_config.iceServers.push({
                        username: e.turnServer.username,
                        credential: e.turnServer.credential,
                        credentialType: 'password',
                        urls: ['turn:' + e.turnServer.url + ':' + e.turnServer.udpport + '?transport=udp']
                      }),
                      typeof e.turnServer.tcpport === 'string' &&
                        e.turnServer.tcpport !== '' &&
                        t.pc_config.iceServers.push({
                          username: e.turnServer.username,
                          credential: e.turnServer.credential,
                          credentialType: 'password',
                          urls: ['turn:' + e.turnServer.url + ':' + e.turnServer.tcpport + '?transport=tcp']
                        }),
                      !0 === e.turnServer.forceturn && (t.pc_config.iceTransportPolicy = 'relay')))),
            void 0 === e.audio && (e.audio = !0),
            void 0 === e.video && (e.video = !0),
            (t.mediaConstraints = { mandatory: { OfferToReceiveVideo: e.video, OfferToReceiveAudio: e.audio } }),
            (t.roapSessionId = 103),
            (t.peerConnection = new o.RTCPeerConnection(t.pc_config)),
            r.default.debug('safari Created RTCPeerConnnection with config "' + JSON.stringify(t.pc_config) + '".'),
            (t.peerConnection.onicecandidate = function(e) {
              var n, i, o, a
              ;(n = t.peerConnection.localDescription.sdp),
                (i = n.match(/a=candidate:.+typ\ssrflx.+\r\n/)),
                (o = n.match(/a=candidate:.+typ\shost.+\r\n/)),
                (a = n.match(/a=candidate:.+typ\srelay.+\r\n/)),
                t.iceCandidateCount === 0 &&
                  (t.timeout = setTimeout(function() {
                    t.moreIceComing && ((t.moreIceComing = !1), t.markActionNeeded())
                  }, 1e3)),
                (i === null && o === null && a === null) ||
                  void 0 !== t.ice ||
                  (r.default.debug('srflx candidate : ' + i + ' relay candidate: ' + a + ' host candidate : ' + o), clearTimeout(t.timeout), (t.ice = 0), (t.moreIceComing = !1), t.markActionNeeded()),
                (t.iceCandidateCount = t.iceCandidateCount + 1)
            })
          var n = function(t) {
              return e.screen && (t = t.replace('a=x-google-flag:conference\r\n', '')), t
            },
            i = function(t) {
              var n, i
              return (
                e.minVideoBW &&
                  e.maxVideoBW &&
                  ((n = t.match(/m=video.*\r\n/)),
                  (i = n[0] + 'b=AS:' + e.maxVideoBW + '\r\n'),
                  (t = t.replace(n[0], i)),
                  r.default.debug('Set Video Bitrate - min:' + e.minVideoBW + ' max:' + e.maxVideoBW)),
                e.maxAudioBW && ((n = t.match(/m=audio.*\r\n/)), (i = n[0] + 'b=AS:' + e.maxAudioBW + '\r\n'), (t = t.replace(n[0], i))),
                t
              )
            }
          t.processSignalingMessage = function(e) {
            var r,
              o = JSON.parse(e)
            ;(t.incomingMessage = o),
              'new' === t.state
                ? o.messageType === 'OFFER'
                  ? ((r = { sdp: o.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(r)), (t.state = 'offer-received'), t.markActionNeeded())
                  : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state)
                : t.state === 'offer-sent'
                  ? o.messageType === 'ANSWER'
                    ? ((r = { sdp: o.sdp, type: 'answer' }),
                      (r.sdp = n(r.sdp)),
                      (r.sdp = i(r.sdp)),
                      (r.sdp = r.sdp.replace(/a=x-google-flag:conference\r\n/g, '')),
                      t.peerConnection.setRemoteDescription(new RTCSessionDescription(r)),
                      t.sendOK(),
                      (t.state = 'established'))
                    : o.messageType === 'pr-answer'
                      ? ((r = { sdp: o.sdp, type: 'pr-answer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(r)))
                      : o.messageType === 'offer'
                        ? t.error('Not written yet')
                        : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state)
                  : t.state === 'established' &&
                    (o.messageType === 'OFFER'
                      ? ((r = { sdp: o.sdp, type: 'offer' }), t.peerConnection.setRemoteDescription(new RTCSessionDescription(r)), (t.state = 'offer-received'), t.markActionNeeded())
                      : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state))
          }
          var a = {
              id: '',
              type: '',
              mediaType: '',
              googCodecName: 'opus',
              aecDivergentFilterFraction: '0',
              audioInputLevel: '0',
              bytesSent: '0',
              packetsSent: '0',
              googEchoCancellationReturnLoss: '0',
              googEchoCancellationReturnLossEnhancement: '0'
            },
            s = {
              id: '',
              type: '',
              mediaType: '',
              googCodecName: e.codec === 'h264' ? 'H264' : 'VP8',
              bytesSent: '0',
              packetsLost: '0',
              packetsSent: '0',
              googAdaptationChanges: '0',
              googAvgEncodeMs: '0',
              googEncodeUsagePercent: '0',
              googFirsReceived: '0',
              googFrameHeightSent: '0',
              googFrameHeightInput: '0',
              googFrameRateInput: '0',
              googFrameRateSent: '0',
              googFrameWidthSent: '0',
              googFrameWidthInput: '0',
              googNacksReceived: '0',
              googPlisReceived: '0',
              googRtt: '0',
              googFramesEncoded: '0'
            },
            d = {
              id: '',
              type: '',
              mediaType: '',
              audioOutputLevel: '0',
              bytesReceived: '0',
              packetsLost: '0',
              packetsReceived: '0',
              googAccelerateRate: '0',
              googCurrentDelayMs: '0',
              googDecodingCNG: '0',
              googDecodingCTN: '0',
              googDecodingCTSG: '0',
              googDecodingNormal: '0',
              googDecodingPLC: '0',
              googDecodingPLCCNG: '0',
              googExpandRate: '0',
              googJitterBufferMs: '0',
              googJitterReceived: '0',
              googPreemptiveExpandRate: '0',
              googPreferredJitterBufferMs: '0',
              googSecondaryDecodedRate: '0',
              googSpeechExpandRate: '0'
            },
            c = {
              id: '',
              type: '',
              mediaType: '',
              googTargetDelayMs: '0',
              packetsLost: '0',
              googDecodeMs: '0',
              googMaxDecodeMs: '0',
              googRenderDelayMs: '0',
              googFrameWidthReceived: '0',
              googFrameHeightReceived: '0',
              googFrameRateReceived: '0',
              googFrameRateDecoded: '0',
              googFrameRateOutput: '0',
              googFramesDecoded: '0',
              googFrameReceived: '0',
              googJitterBufferMs: '0',
              googCurrentDelayMs: '0',
              googMinPlayoutDelayMs: '0',
              googNacksSent: '0',
              googPlisSent: '0',
              googFirsSent: '0',
              bytesReceived: '0',
              packetsReceived: '0'
            },
            u = {
              id: 'bweforvideo',
              type: 'VideoBwe',
              googAvailableSendBandwidth: '0',
              googAvailableReceiveBandwidth: '0',
              googActualEncBitrate: '0',
              googRetransmitBitrate: '0',
              googTargetEncBitrate: '0',
              googBucketDelay: '0',
              googTransmitBitrate: '0'
            },
            l = 0,
            p = 0,
            f = 0
          return (
            (t.getStatsRate = function(e) {
              t.getStats(function(t) {
                t.forEach(function(e) {
                  e.type === 'outbound-rtp' && e.mediaType === 'video' && e.googFramesEncoded && ((e.googFrameRateSent = ((e.googFramesEncoded - l) / 3).toString()), (l = e.googFramesEncoded)),
                    'inbound-rtp' === e.type &&
                      e.id.indexOf('55543') != -1 &&
                      (e.googFrameRateReceived && ((e.googFrameRateReceived = ((e.googFrameReceived - f) / 3).toString()), (f = e.googFrameReceived)),
                      e.googFrameRateDecoded && ((e.googFrameRateDecoded = ((e.googFramesDecoded - p) / 3).toString()), (p = e.googFramesDecoded)))
                }),
                  e(t)
              })
            }),
            (t.getStats = function(e) {
              var n = []
              t.peerConnection
                .getStats()
                .then(function(i) {
                  i.forEach(function(e) {
                    n.push(e),
                      'outbound-rtp' === e.type &&
                        e.mediaType === 'audio' &&
                        ((a.id = e.id),
                        (a.type = e.type),
                        (a.mediaType = e.mediaType),
                        (a.bytesSent = e.bytesSent ? e.bytesSent + '' : '0'),
                        (a.packetsSent = e.packetsSent ? e.packetsSent + '' : '0')),
                      e.type === 'outbound-rtp' &&
                        e.mediaType === 'video' &&
                        ((s.id = e.id),
                        (s.type = e.type),
                        (s.mediaType = e.mediaType),
                        (s.bytesSent = e.bytesSent ? e.bytesSent + '' : '0'),
                        (s.packetsSent = e.packetsSent ? e.packetsSent + '' : '0'),
                        (s.googPlisReceived = e.pliCount ? e.pliCount + '' : '0'),
                        (s.googNacksReceived = e.nackCount ? e.nackCount + '' : '0'),
                        (s.googFirsReceived = e.firCount ? e.firCount + '' : '0'),
                        (s.googFramesEncoded = e.framesEncoded ? e.framesEncoded + '' : '0')),
                      'inbound-rtp' === e.type &&
                        e.id.indexOf('44444') != -1 &&
                        ((d.id = e.id),
                        (d.type = e.type),
                        (d.mediaType = 'audio'),
                        (d.packetsReceived = e.packetsReceived ? e.packetsReceived + '' : '0'),
                        (d.bytesReceived = e.bytesReceived ? e.bytesReceived + '' : '0'),
                        (d.packetsLost = e.packetsLost ? e.packetsLost + '' : '0'),
                        (d.packetsReceived = e.packetsReceived ? e.packetsReceived + '' : '0'),
                        (d.googJitterReceived = e.jitter ? e.jitter + '' : '0')),
                      e.type === 'inbound-rtp' &&
                        e.id.indexOf('55543') != -1 &&
                        ((c.id = e.id),
                        (c.type = e.type),
                        (c.mediaType = 'video'),
                        (c.packetsReceived = e.packetsReceived ? e.packetsReceived + '' : '0'),
                        (c.bytesReceived = e.bytesReceived ? e.bytesReceived + '' : '0'),
                        (c.packetsLost = e.packetsLost ? e.packetsLost + '' : '0'),
                        (c.googJitterBufferMs = e.jitter ? e.jitter + '' : '0'),
                        (c.googNacksSent = e.nackCount ? e.nackCount + '' : '0'),
                        (c.googPlisSent = e.pliCount ? e.pliCount + '' : '0'),
                        (c.googFirsSent = e.firCount ? e.firCount + '' : '0')),
                      'track' === e.type &&
                        e.id.indexOf('55543') != -1 &&
                        ((c.googFrameWidthReceived = e.frameWidth ? e.frameWidth + '' : '0'),
                        (c.googFrameHeightReceived = e.frameHeight ? e.frameHeight + '' : '0'),
                        (c.googFrameReceived = e.framesReceived ? e.framesReceived + '' : '0'),
                        (c.googFramesDecoded = e.framesDecoded ? e.framesDecoded + '' : '0')),
                      'track' === e.type && e.id.indexOf('44444') != -1 && ((d.audioOutputLevel = e.audioLevel + ''), (a.audioInputLevel = e.audioLevel + '')),
                      e.type === 'candidate-pair' &&
                        (e.availableIncomingBitrate == 0 ? (u.googAvailableSendBandwidth = e.availableOutgoingBitrate + '') : (u.googAvailableReceiveBandwidth = e.availableIncomingBitrate + ''))
                  })
                  var r = [u, a, s, d, c]
                  r.push({ id: 'time', startTime: t.connectedTime, timestamp: new Date() }), e(r, n)
                })
                .catch(function(e) {
                  console.error(e)
                })
            }),
            (t.addStream = function(e) {
              window.navigator.userAgent.indexOf('Safari') > -1 && navigator.userAgent.indexOf('Chrome') === -1
                ? e.getTracks().forEach(function(n) {
                    return t.peerConnection.addTrack(n, e)
                  })
                : t.peerConnection.addStream(e),
                t.markActionNeeded()
            }),
            (t.removeStream = function() {
              t.markActionNeeded()
            }),
            (t.close = function() {
              ;(t.state = 'closed'), t.peerConnection.close()
            }),
            (t.markActionNeeded = function() {
              ;(t.actionNeeded = !0),
                t.doLater(function() {
                  t.onstablestate()
                })
            }),
            (t.doLater = function(e) {
              window.setTimeout(e, 1)
            }),
            (t.onstablestate = function() {
              var n
              if (t.actionNeeded) {
                if (t.state === 'new' || t.state === 'established') {
                  if (e.isSubscriber && window.navigator.userAgent.indexOf('Safari') > -1 && navigator.userAgent.indexOf('Chrome') === -1) {
                    var o = t.peerConnection.addTransceiver('audio'),
                      a = t.peerConnection.addTransceiver('video')
                    o.setDirection('recvonly'), a.setDirection('recvonly')
                  }
                  t.peerConnection
                    .createOffer(t.mediaConstraints)
                    .then(function(n) {
                      if (((n.sdp = i(n.sdp)), e.isSubscriber || (n.sdp = n.sdp.replace(/a=extmap:4 urn:3gpp:video-orientation\r\n/g, '')), n.sdp !== t.prevOffer)) {
                      { return t.peerConnection.setLocalDescription(n), (t.state = 'preparing-offer'), void t.markActionNeeded() }
                      r.default.debug('Not sending a new offer')
                    })
                    .catch(function(e) {
                      r.default.debug('peer connection create offer failed ', e)
                    })
                } else if (t.state === 'preparing-offer') {
                  if (t.moreIceComing) return
                  ;(t.prevOffer = t.peerConnection.localDescription.sdp),
                    (t.prevOffer = t.prevOffer.replace(/a=candidate:.+typ\shost.+\r\n/g, 'a=candidate:2243255435 1 udp 2122194687 192.168.0.1 30000 typ host generation 0 network-id 1\r\n')),
                    t.sendMessage('OFFER', t.prevOffer),
                    (t.state = 'offer-sent')
                } else if (t.state === 'offer-received') {
                {
t.peerConnection.createAnswer(
                  function(e) {
                    if ((t.peerConnection.setLocalDescription(e), (t.state = 'offer-received-preparing-answer'), t.iceStarted)) return void t.markActionNeeded()
                    var n = new Date()
                    r.default.debug(n.getTime() + ': Starting ICE in responder'), (t.iceStarted = !0)
                  },
                  function(e) {
                    r.default.debug('peer connection create answer failed ', e)
                  },
                  t.mediaConstraints
                )
} else if (t.state === 'offer-received-preparing-answer') {
                  if (t.moreIceComing) return
                  ;(n = t.peerConnection.localDescription.sdp), t.sendMessage('ANSWER', n), (t.state = 'established')
                } else t.error('Dazed and confused in state ' + t.state + ', stopping here')
                t.actionNeeded = !1
              }
            }),
            (t.sendOK = function() {
              t.sendMessage('OK')
            }),
            (t.sendMessage = function(e, n) {
              var i = {}
              ;(i.messageType = e),
                (i.sdp = n),
                e === 'OFFER'
                  ? ((i.offererSessionId = t.sessionId), (i.answererSessionId = t.otherSessionId), (i.seq = t.sequenceNumber += 1), (i.tiebreaker = Math.floor(429496723 * Math.random() + 1)))
                  : ((i.offererSessionId = t.incomingMessage.offererSessionId), (i.answererSessionId = t.sessionId), (i.seq = t.incomingMessage.seq)),
                t.onsignalingmessage(JSON.stringify(i))
            }),
            (t.error = function(e) {
              throw 'Error in RoapOnJsep: ' + e
            }),
            (t.sessionId = t.roapSessionId += 1),
            (t.sequenceNumber = 0),
            (t.actionNeeded = !1),
            (t.iceStarted = !1),
            (t.moreIceComing = !0),
            (t.iceCandidateCount = 0),
            (t.onsignalingmessage = e.callback),
            (t.peerConnection.ontrack = function(e) {
              t.onaddstream && t.onaddstream(e, 'ontrack')
            }),
            (t.peerConnection.onremovestream = function(e) {
              t.onremovestream && t.onremovestream(e)
            }),
            (t.peerConnection.oniceconnectionstatechange = function(e) {
              e.currentTarget.iceConnectionState === 'connected' && (t.connectedTime = new Date()), t.oniceconnectionstatechange && t.oniceconnectionstatechange(e.currentTarget.iceConnectionState)
            }),
            (t.onaddstream = null),
            (t.onremovestream = null),
            (t.state = 'new'),
            t.markActionNeeded(),
            t
          )
        }
      t.default = a
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function() {
        var e = {}
        return (e.addStream = function() {}), e
      }
      t.default = i
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(0),
        r = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(i),
        o = n(4),
        a = function(e) {
          var t = {},
            n = (mozRTCPeerConnection, mozRTCSessionDescription),
            i = !1
          ;(t.isSubscriber = e.isSubscriber),
            (t.pc_config = { iceServers: [] }),
            e.iceServers instanceof Array
              ? e.iceServers.map(function(e) {
                  0 === e.url.indexOf('stun:') && t.pc_config.iceServers.push({ url: e.url })
                })
              : (e.stunServerUrl &&
                  (e.stunServerUrl instanceof Array
                    ? e.stunServerUrl.map(function(e) {
                        'string' === typeof e && e !== '' && t.pc_config.iceServers.push({ url: e })
                      })
                    : typeof e.stunServerUrl === 'string' && e.stunServerUrl !== '' && t.pc_config.iceServers.push({ url: e.stunServerUrl })),
                e.turnServer &&
                  typeof e.turnServer.url === 'string' &&
                  e.turnServer.url !== '' &&
                  (t.pc_config.iceServers.push({
                    username: e.turnServer.username,
                    credential: e.turnServer.credential,
                    credentialType: 'password',
                    urls: 'turn:' + e.turnServer.url + ':' + e.turnServer.udpport + '?transport=udp'
                  }),
                  typeof e.turnServer.tcpport === 'string' &&
                    e.turnServer.tcpport !== '' &&
                    t.pc_config.iceServers.push({
                      username: e.turnServer.username,
                      credential: e.turnServer.credential,
                      credentialType: 'password',
                      urls: 'turn:' + e.turnServer.url + ':' + e.turnServer.tcpport + '?transport=tcp'
                    }),
                  !0 === e.turnServer.forceturn && (t.pc_config.iceTransportPolicy = 'relay'))),
            void 0 === e.audio && (e.audio = !0),
            void 0 === e.video && (e.video = !0),
            (t.mediaConstraints = { offerToReceiveAudio: e.audio, offerToReceiveVideo: e.video, mozDontOfferDataChannel: !0 }),
            (t.roapSessionId = 103),
            (t.peerConnection = new o.RTCPeerConnection(t.pc_config)),
            r.default.debug('safari Created RTCPeerConnnection with config "' + JSON.stringify(t.pc_config) + '".'),
            (t.peerConnection.onicecandidate = function(e) {
              var n, i, o, a
              ;(n = t.peerConnection.localDescription.sdp),
                (i = n.match(/a=candidate:.+typ\ssrflx.+\r\n/)),
                (o = n.match(/a=candidate:.+typ\shost.+\r\n/)),
                (a = n.match(/a=candidate:.+typ\srelay.+\r\n/)),
                t.iceCandidateCount === 0 &&
                  (t.timeout = setTimeout(function() {
                    t.moreIceComing && ((t.moreIceComing = !1), t.markActionNeeded())
                  }, 1e3)),
                (i === null && o === null && a === null) ||
                  void 0 !== t.ice ||
                  (r.default.debug('srflx candidate : ' + i + ' relay candidate: ' + a + ' host candidate : ' + o), clearTimeout(t.timeout), (t.ice = 0), (t.moreIceComing = !1), t.markActionNeeded()),
                (t.iceCandidateCount = t.iceCandidateCount + 1)
            }),
            (t.checkMLineReverseInSDP = function(e) {
              return !(!~e.indexOf('m=audio') || !~e.indexOf('m=video')) && e.indexOf('m=audio') > e.indexOf('m=video')
            }),
            (t.reverseMLineInSDP = function(e) {
              var t = e.split('m=audio'),
                n = t[1].split('m=video'),
                i = 'm=video' + n[1],
                r = 'm=audio' + n[0]
              return (e = t[0] + i + r)
            }),
            (t.processSignalingMessage = function(e) {
              var i,
                o = JSON.parse(e)
              ;(t.incomingMessage = o),
                'new' === t.state
                  ? o.messageType === 'OFFER'
                    ? ((o.sdp = l(o.sdp)),
                      (i = { sdp: o.sdp, type: 'offer' }),
                      t.peerConnection.setRemoteDescription(
                        new n(i),
                        function() {
                          r.default.debug('setRemoteDescription succeeded')
                        },
                        function(e) {
                          r.default.info('setRemoteDescription failed: ' + e.name)
                        }
                      ),
                      (t.state = 'offer-received'),
                      t.markActionNeeded())
                    : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state)
                  : t.state === 'offer-sent'
                    ? o.messageType === 'ANSWER'
                      ? ((o.sdp = l(o.sdp)),
                        (o.sdp = o.sdp.replace(/ generation 0/g, '')),
                        (o.sdp = o.sdp.replace(/ udp /g, ' UDP ')),
                        (o.sdp = o.sdp.replace(/a=group:BUNDLE audio video/, 'a=group:BUNDLE sdparta_0 sdparta_1')),
                        (o.sdp = o.sdp.replace(/a=mid:audio/, 'a=mid:sdparta_0')),
                        (o.sdp = o.sdp.replace(/a=mid:video/, 'a=mid:sdparta_1')),
                        t.isMLineReverseInSDP && (o.sdp = t.reverseMLineInSDP(o.sdp)),
                        (i = { sdp: o.sdp, type: 'answer' }),
                        t.peerConnection.setRemoteDescription(
                          new n(i),
                          function() {
                            r.default.debug('setRemoteDescription succeeded')
                          },
                          function(e) {
                            r.default.info('setRemoteDescription failed: ' + e)
                          }
                        ),
                        t.sendOK(),
                        (t.state = 'established'))
                      : o.messageType === 'pr-answer'
                        ? ((i = { sdp: o.sdp, type: 'pr-answer' }),
                          t.peerConnection.setRemoteDescription(
                            new n(i),
                            function() {
                              r.default.debug('setRemoteDescription succeeded')
                            },
                            function(e) {
                              r.default.info('setRemoteDescription failed: ' + e.name)
                            }
                          ))
                        : o.messageType === 'offer'
                          ? t.error('Not written yet')
                          : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state)
                    : t.state === 'established' &&
                      (o.messageType === 'OFFER'
                        ? ((i = { sdp: o.sdp, type: 'offer' }),
                          t.peerConnection.setRemoteDescription(
                            new n(i),
                            function() {
                              r.default.debug('setRemoteDescription succeeded')
                            },
                            function(e) {
                              r.default.info('setRemoteDescription failed: ' + e.name)
                            }
                          ),
                          (t.state = 'offer-received'),
                          t.markActionNeeded())
                        : t.error('Illegal message for this state: ' + o.messageType + ' in state ' + t.state))
            })
          var a = {
              id: '',
              type: '',
              mediaType: 'opus',
              googCodecName: 'opus',
              aecDivergentFilterFraction: '0',
              audioInputLevel: '0',
              bytesSent: '0',
              packetsSent: '0',
              googEchoCancellationReturnLoss: '0',
              googEchoCancellationReturnLossEnhancement: '0'
            },
            s = {
              id: '',
              type: '',
              mediaType: '',
              googCodecName: e.codec === 'h264' ? 'H264' : 'VP8',
              bytesSent: '0',
              packetsLost: '0',
              packetsSent: '0',
              googAdaptationChanges: '0',
              googAvgEncodeMs: '0',
              googEncodeUsagePercent: '0',
              googFirsReceived: '0',
              googFrameHeightSent: '0',
              googFrameHeightInput: '0',
              googFrameRateInput: '0',
              googFrameRateSent: '0',
              googFrameWidthSent: '0',
              googFrameWidthInput: '0',
              googNacksReceived: '0',
              googPlisReceived: '0',
              googRtt: '0'
            },
            d = {
              id: '',
              type: '',
              mediaType: '',
              audioOutputLevel: '0',
              bytesReceived: '0',
              packetsLost: '0',
              packetsReceived: '0',
              googAccelerateRate: '0',
              googCurrentDelayMs: '0',
              googDecodingCNG: '0',
              googDecodingCTN: '0',
              googDecodingCTSG: '0',
              googDecodingNormal: '0',
              googDecodingPLC: '0',
              googDecodingPLCCNG: '0',
              googExpandRate: '0',
              googJitterBufferMs: '0',
              googJitterReceived: '0',
              googPreemptiveExpandRate: '0',
              googPreferredJitterBufferMs: '0',
              googSecondaryDecodedRate: '0',
              googSpeechExpandRate: '0'
            },
            c = {
              id: '',
              type: '',
              mediaType: '',
              googTargetDelayMs: '0',
              packetsLost: '0',
              googDecodeMs: '0',
              googMaxDecodeMs: '0',
              googRenderDelayMs: '0',
              googFrameWidthReceived: '0',
              googFrameHeightReceived: '0',
              googFrameRateReceived: '0',
              googFrameRateDecoded: '0',
              googFrameRateOutput: '0',
              googJitterBufferMs: '0',
              googCurrentDelayMs: '0',
              googMinPlayoutDelayMs: '0',
              googNacksSent: '0',
              googPlisSent: '0',
              googFirsSent: '0',
              bytesReceived: '0',
              packetsReceived: '0',
              googFramesDecoded: '0'
            },
            u = 0
          ;(t.getStatsRate = function(e) {
            t.getStats(function(t) {
              t.forEach(function(e) {
                e.type === 'inboundrtp' && e.mediaType === 'video' && e.googFrameRateDecoded && ((e.googFrameRateDecoded = ((e.googFramesDecoded - u) / 3).toString()), (u = e.googFramesDecoded))
              }),
                e(t)
            })
          }),
            (t.getStats = function(e) {
              t.peerConnection.getStats().then(
                function(n) {
                  var i = []
                  Object.keys(n).forEach(function(e) {
                    var t = n[e]
                    i.push(t),
                      'outboundrtp' === t.type &&
                        t.mediaType === 'video' &&
                        ((s.id = t.id),
                        (s.type = t.type),
                        (s.mediaType = t.mediaType),
                        (s.bytesSent = t.bytesSent ? t.bytesSent + '' : '0'),
                        (s.packetsSent = t.packetsSent ? t.packetsSent + '' : '0'),
                        (s.googPlisReceived = t.pliCount ? t.pliCount + '' : '0'),
                        (s.googNacksReceived = t.nackCount ? t.nackCount + '' : '0'),
                        (s.googFirsReceived = t.firCount ? t.firCount + '' : '0'),
                        (s.googFrameRateSent = t.framerateMean ? t.framerateMean + '' : '0')),
                      t.type === 'outboundrtp' &&
                        t.mediaType === 'audio' &&
                        ((a.id = t.id),
                        (a.type = t.type),
                        (a.mediaType = t.mediaType),
                        (a.bytesSent = t.bytesSent ? t.bytesSent + '' : '0'),
                        (a.packetsSent = t.packetsSent ? t.packetsSent + '' : '0')),
                      'inboundrtp' !== t.type ||
                        t.mediaType !== 'audio' ||
                        t.isRemote ||
                        ((d.id = t.id),
                        (d.type = t.type),
                        (d.mediaType = t.mediaType),
                        (d.bytesReceived = t.bytesReceived ? t.bytesReceived + '' : '0'),
                        (d.packetsLost = t.packetsLost ? t.packetsLost + '' : '0'),
                        (d.packetsReceived = t.packetsReceived ? t.packetsReceived + '' : '0'),
                        (d.googJitterReceived = t.jitter ? t.jitter + '' : '0')),
                      'inboundrtp' !== t.type ||
                        t.mediaType !== 'video' ||
                        t.isRemote ||
                        ((c.id = t.id),
                        (c.type = t.type),
                        (c.mediaType = t.mediaType),
                        (c.bytesReceived = t.bytesReceived ? t.bytesReceived + '' : '0'),
                        (c.googFrameRateReceived = t.framerateMean ? t.framerateMean + '' : '0'),
                        (c.googFramesDecoded = t.framesDecoded ? t.framesDecoded + '' : '0'),
                        (c.packetsLost = t.packetsLost ? t.packetsLost + '' : '0'),
                        (c.packetsReceived = t.packetsReceived ? t.packetsReceived + '' : '0'),
                        (c.googJitterBufferMs = t.jitter ? t.jitter + '' : '0'),
                        (c.googNacksSent = t.nackCount ? t.nackCount + '' : '0'),
                        (c.googPlisSent = t.pliCount ? t.pliCount + '' : '0'),
                        (c.googFirsSent = t.firCount ? t.firCount + '' : '0')),
                      -1 !== t.id.indexOf('outbound_rtcp_video') && (s.packetsLost = t.packetsLost ? t.packetsLost + '' : '0')
                  })
                  var r = [s, a, d, c]
                  r.push({ id: 'time', startTime: t.connectedTime, timestamp: new Date() }), e(r, i)
                },
                function(e) {
                  r.default.error(e)
                }
              )
            }),
            (t.addStream = function(e) {
              ;(i = !0), t.peerConnection.addStream(e), t.markActionNeeded()
            }),
            (t.removeStream = function() {
              t.markActionNeeded()
            }),
            (t.close = function() {
              ;(t.state = 'closed'), t.peerConnection.close()
            }),
            (t.markActionNeeded = function() {
              ;(t.actionNeeded = !0),
                t.doLater(function() {
                  t.onstablestate()
                })
            }),
            (t.doLater = function(e) {
              window.setTimeout(e, 1)
            }),
            (t.onstablestate = function() {
              if (t.actionNeeded) {
                if (t.state === 'new' || t.state === 'established') {
                  i && (t.mediaConstraints = void 0),
                    (function() {
                      t.peerConnection.createOffer(
                        function(e) {
                          if (
                            ((e.sdp = l(e.sdp)),
                            (e.sdp = e.sdp.replace(
                              /a=extmap:1 http:\/\/www.webrtc.org\/experiments\/rtp-hdrext\/abs-send-time/,
                              'a=extmap:3 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time'
                            )),
                            e.sdp !== t.prevOffer)
                          )
                            {return t.peerConnection.setLocalDescription(e), (t.isMLineReverseInSDP = t.checkMLineReverseInSDP(e.sdp)), (t.state = 'preparing-offer'), void t.markActionNeeded()}
                          r.default.debug('Not sending a new offer')
                        },
                        function(e) {
                          r.default.debug('Ups! create offer failed ', e)
                        },
                        t.mediaConstraints
                      )
                    })()
                } else if (t.state === 'preparing-offer') {
                  if (t.moreIceComing) return
                  ;(t.prevOffer = t.peerConnection.localDescription.sdp),
                    (t.prevOffer = t.prevOffer.replace(/a=candidate:.+typ\shost.+\r\n/g, 'a=candidate:2243255435 1 udp 2122194687 192.168.0.1 30000 typ host generation 0 network-id 1\r\n')),
                    t.sendMessage('OFFER', t.prevOffer),
                    (t.state = 'offer-sent')
                } else if (t.state === 'offer-received')
                  {t.peerConnection.createAnswer(
                  function(e) {
                    if ((t.peerConnection.setLocalDescription(e), (t.state = 'offer-received-preparing-answer'), t.iceStarted)) return void t.markActionNeeded()
                    var n = new Date()
                    r.default.debug(n.getTime() + ': Starting ICE in responder'), (t.iceStarted = !0)
                  },
                  function() {
                    r.default.debug('Ups! Something went wrong')
                  }
                )}
                else if (t.state === 'offer-received-preparing-answer') {
                  if (t.moreIceComing) return
                  var e = t.peerConnection.localDescription.sdp
                  t.sendMessage('ANSWER', e), (t.state = 'established')
                } else t.error('Dazed and confused in state ' + t.state + ', stopping here')
                t.actionNeeded = !1
              }
            }),
            (t.sendOK = function() {
              t.sendMessage('OK')
            }),
            (t.sendMessage = function(e, n) {
              var i = {}
              ;(i.messageType = e),
                (i.sdp = n),
                'OFFER' === e
                  ? ((i.offererSessionId = t.sessionId), (i.answererSessionId = t.otherSessionId), (i.seq = t.sequenceNumber += 1), (i.tiebreaker = Math.floor(429496723 * Math.random() + 1)))
                  : ((i.offererSessionId = t.incomingMessage.offererSessionId), (i.answererSessionId = t.sessionId), (i.seq = t.incomingMessage.seq)),
                t.onsignalingmessage(JSON.stringify(i))
            }),
            (t.error = function(e) {
              throw 'Error in RoapOnJsep: ' + e
            }),
            (t.sessionId = t.roapSessionId += 1),
            (t.sequenceNumber = 0),
            (t.actionNeeded = !1),
            (t.iceStarted = !1),
            (t.moreIceComing = !0),
            (t.iceCandidateCount = 0),
            (t.onsignalingmessage = e.callback),
            (t.peerConnection.ontrack = function(e) {
              t.onaddstream && t.onaddstream(e, 'ontrack')
            }),
            (t.peerConnection.onremovestream = function(e) {
              t.onremovestream && t.onremovestream(e)
            }),
            (t.peerConnection.oniceconnectionstatechange = function(e) {
              e.currentTarget.iceConnectionState === 'connected' && (t.connectedTime = new Date()), t.oniceconnectionstatechange && t.oniceconnectionstatechange(e.currentTarget.iceConnectionState)
            })
          var l = function(t) {
            if (e.video && e.maxVideoBW) {
              var n = t.match(/m=video.*\r\n/)
              if ((n == null && (n = t.match(/m=video.*\n/)), n && n.length > 0)) {
                var i = n[0] + 'b=TIAS:' + 1e3 * e.maxVideoBW + '\r\n'
                t = t.replace(n[0], i)
              }
            }
            if (e.audio && e.maxAudioBW) {
              var n = t.match(/m=audio.*\r\n/)
              if ((n == null && (n = t.match(/m=audio.*\n/)), n && n.length > 0)) {
                var i = n[0] + 'b=TIAS:' + 1e3 * e.maxAudioBW + '\r\n'
                t = t.replace(n[0], i)
              }
            }
            return t
          }
          return (t.onaddstream = null), (t.onremovestream = null), (t.state = 'new'), t.markActionNeeded(), t
        }
      t.default = a
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function(e, t, n) {
        var i = {}
        return (
          (i.callback = t),
          (i.config = e),
          (i.error = n),
          (i.selectSource = function(e, t, n) {
            var i = document.createElement('div')
            ;(i.innerText = 'share screen'),
              i.setAttribute('style', 'text-align: center; height: 25px; line-height: 25px; border-radius: 4px 4px 0 0; background: #D4D2D4; border-bottom:  solid 1px #B9B8B9;')
            var r = document.createElement('div')
            r.setAttribute('style', 'width: 100%; height: 500px; padding: 15px 25px ; box-sizing: border-box;')
            var o = document.createElement('div')
            ;(o.innerText = "Agora Web Screensharing wants to share the contents of your screen with webdemo.agorabeckon.com. Choose what you'd like to share."),
              o.setAttribute('style', 'height: 12%;')
            var a = document.createElement('div')
            a.setAttribute(
              'style',
              'width: 100%; height: 80%; background: #FFF; border:  solid 1px #CBCBCB; display: flex; flex-wrap: wrap; justify-content: space-around; overflow-y: scroll; padding: 0 15px; box-sizing: border-box;'
            )
            var s = document.createElement('div')
            s.setAttribute('style', 'text-align: right; padding: 16px 0;')
            var d = document.createElement('button')
            ;(d.innerHTML = 'cancel'),
              d.setAttribute('style', 'width: 85px;'),
              (d.onclick = function() {
                document.body.removeChild(c), n && n('NotAllowedError')
              }),
              s.appendChild(d),
              r.appendChild(o),
              r.appendChild(a),
              r.appendChild(s)
            var c = document.createElement('div')
            c.setAttribute(
              'style',
              'position: absolute; z-index: 99999999; top: 50%; left: 50%; width: 620px; height: 525px; background: #ECECEC; border-radius: 4px; -webkit-transform: translate(-50%,-50%); transform: translate(-50%,-50%);'
            ),
              c.appendChild(i),
              c.appendChild(r),
              document.body.appendChild(c),
              e.map(function(e) {
                if (e.id) {
                  var n = document.createElement('div')
                  n.setAttribute('style', 'width: 30%; height: 160px; padding: 20px 0; text-align: center;box-sizing: content-box;'),
                    (n.innerHTML =
                      '<div style="height: 120px; display: table-cell; vertical-align: middle;"><img style="width: 100%; background: #333333; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.2);" src=' +
                      e.thumbnail.toDataURL() +
                      ' /></div><span style="\theight: 40px; line-height: 40px; display: inline-block; width: 70%; word-break: keep-all; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">' +
                      e.name +
                      '</span>'),
                    (n.onclick = function() {
                      document.body.removeChild(c), t && t(e.id)
                    }),
                    a.appendChild(n)
                }
              })
          }),
          (i.getShareScreenStream = function(e) {
            var t = i.config.attributes.width,
              n = i.config.attributes.height,
              r = i.config.attributes.maxFr,
              o = i.config.attributes.minFr,
              a = { audio: !1, video: { mandatory: { chromeMediaSource: 'desktop', chromeMediaSourceId: e, maxHeight: n, maxWidth: t, maxFrameRate: r, minFrameRate: o } } }
            navigator.webkitGetUserMedia(a, i.callback, i.error)
          }),
          (i.shareScreen = function(e) {
            try {
              var t = window.require('electron_share_screen_ui')
            } catch (e) {}
            t
              ? t(function(e) {
                  i.getShareScreenStream(e)
                }, i.error)
              : e.desktopCapturer.getSources({ types: ['window', 'screen'] }, function(e, t) {
                  if (e) throw e
                  i.selectSource(
                    t,
                    function(e) {
                      i.getShareScreenStream(e)
                    },
                    i.error
                  )
                })
          }),
          i
        )
      }
      t.default = i
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = function(e) {
          var t = {
            audioSendBytes: '0',
            audioSendPackets: '0',
            videoSendBytes: '0',
            videoSendPackets: '0',
            videoSendPacketsLost: '0',
            videoSendFrameRate: '0',
            videoSendBandwidth: '0',
            videoSendResolutionWidth: '0',
            videoSendResolutionHeight: '0',
            audioCodecName: '',
            videoCodecName: '',
            timestamp: '',
            startTime: '',
            duration: '0'
          }
          return (
            e.forEach(function(e) {
              e.type === 'VideoBwe'
                ? (t.videoSendBandwidth = e.googAvailableSendBandwidth)
                : e.id.indexOf('send') !== -1 || e.id.indexOf('outbound_rtp') !== -1 || e.id.indexOf('OutboundRTP') !== -1
                  ? e.mediaType === 'audio'
                    ? ((t.audioSendBytes = e.bytesSent), (t.audioSendPackets = e.packetsSent), (t.audioCodecName = e.googCodecName))
                    : ((t.videoSendBytes = e.bytesSent),
                      (t.videoSendPackets = e.packetsSent),
                      (t.videoSendPacketsLost = e.packetsLost),
                      (t.videoSendFrameRate = e.googFrameRateSent),
                      (t.videoSendResolutionWidth = e.googFrameWidthSent),
                      (t.videoSendResolutionHeight = e.googFrameHeightSent),
                      (t.videoCodecName = e.googCodecName))
                  : e.id === 'time' && ((t.timestamp = e.timestamp), (t.startTime = e.startTime))
            }),
            t.timestamp instanceof Date && t.startTime instanceof Date && (t.duration = Math.floor((t.timestamp.getTime() - t.startTime.getTime()) / 1e3) + ''),
            t
          )
        },
        r = function(e) {
          var t = {
            audioReceiveBytes: '0',
            audioReceivePackets: '0',
            audioReceivePacketsLost: '0',
            videoReceiveBytes: '0',
            videoReceivePackets: '0',
            videoReceivePacketsLost: '0',
            videoReceiveFrameRate: '0',
            videoReceiveDecodeFrameRate: '0',
            videoReceiveBandwidth: '0',
            videoReceivedResolutionWidth: '0',
            videoReceivedResolutionHeight: '0',
            timestamp: '',
            startTime: '',
            duration: '0'
          }
          return (
            e.forEach(function(e) {
              e.type === 'VideoBwe'
                ? (t.videoReceiveBandwidth = e.googAvailableReceiveBandwidth)
                : e.id.indexOf('recv') !== -1 || e.id.indexOf('inbound_rtp') !== -1 || e.id.indexOf('InboundRTP') !== -1
                  ? e.mediaType === 'audio'
                    ? ((t.audioReceiveBytes = e.bytesReceived), (t.audioReceivePackets = e.packetsReceived), (t.audioReceivePacketsLost = e.packetsLost))
                    : ((t.videoReceiveBytes = e.bytesReceived),
                      (t.videoReceivePacketsLost = e.packetsLost),
                      (t.videoReceivePackets = e.packetsReceived),
                      (t.videoReceiveFrameRate = e.googFrameRateReceived),
                      (t.videoReceiveDecodeFrameRate = e.googFrameRateDecoded),
                      (t.videoReceivedResolutionWidth = e.googFrameWidthReceived),
                      (t.videoReceivedResolutionHeight = e.googFrameHeightReceived))
                  : e.id === 'time' && ((t.timestamp = e.timestamp), (t.startTime = e.startTime))
            }),
            t.timestamp instanceof Date && t.startTime instanceof Date && (t.duration = Math.floor((t.timestamp.getTime() - t.startTime.getTime()) / 1e3) + ''),
            t
          )
        }
      ;(t.publishStatsFilter = i), (t.subscribeStatsFilter = r)
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.LiveTranscoding = t.TranscodingUser = t.createLiveClient = t.createRtcClient = t.createClient = void 0)
      var r = n(31),
        o = i(r),
        a = n(0),
        s = i(a),
        d = n(7),
        c = i(d),
        u = n(6),
        l = n(13),
        p = n(5),
        f = n(2),
        m = n(35),
        g = n(8),
        v = n(1),
        _ = (function(e) {
          if (e && e.__esModule) return e
          var t = {}
          if (e != null) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n])
          return (t.default = e), t
        })(v),
        S = n(11),
        E = function(e) {
          var t = {}
          ;(t.key = void 0),
            (t.highStream = null),
            (t.lowStream = null),
            (t.lowStreamParameter = null),
            (t.isDualStream = !1),
            (t.highStreamState = 2),
            (t.lowStreamState = 2),
            (t.proxyServer = null),
            (t.turnServer = {})
          var e = Object.assign({}, e)
          return (
            (t.aespassword = null),
            (t.aesmode = S.ENCRYPTION_MODE_NONE),
            (t.setLowStreamParameter = function(e) {
              t.lowStreamParameter = e
            }),
            (t._getVideoCameraIdByLabel = function(e, t, n) {
              ;(0, g.getDevices)(function(i) {
                var r = !0,
                  o = !1,
                  a = void 0
                try {
                  for (var s, d = i[Symbol.iterator](); !(r = (s = d.next()).done); r = !0) {
                    var u = s.value
                    if (u.label === e && u.kind === 'videoinput') return t && t(u.deviceId)
                  }
                } catch (e) {
                  ;(o = !0), (a = e)
                } finally {
                  try {
                    !r && d.return && d.return()
                  } finally {
                    if (o) throw a
                  }
                }
                return n && n(c.default.NOT_FIND_DEVICE_BY_LABEL)
              }, n)
            }),
            (t.init = function(t, n, i) {
              if (typeof t !== 'string') throw new Error('Param appId should be string')
              s.default.info('Initializing AgoraRTC client'), (e.appId = t), (e.sessionId = (0, u.generateSessionId)()), n && n()
            }),
            (t.setTurnServer = function(e, n, i) {
              var r = e.turnServerURL,
                o = e.username,
                a = e.password,
                d = e.udpport,
                c = e.forceturn,
                u = e.tcpport
              if (!r) throw new Error('Do not set the turnServerURL parameter as empty')
              if (!o) throw new Error('Do not set the username parameter as empty')
              if (!a) throw new Error('Do not set the password parameter as empty')
              if (!d) throw new Error('Do not set the udpport parameter as empty')
              ;(t.turnServer.url = r),
                (t.turnServer.udpport = d),
                (t.turnServer.username = o),
                (t.turnServer.credential = a),
                (t.turnServer.forceturn = c || !1),
                u && ((t.turnServer.tcpport = u), s.default.info('Set turnserver tcpurl.' + t.turnServer.url + ':' + t.turnServer.tcpport)),
                s.default.info('Set turnserver udpurl.' + t.turnServer.url + ':' + t.turnServer.udpport + ',username:' + t.turnServer.uername + ',password:' + t.turnServer.credential)
            }),
            (t.setProxyServer = function(e) {
              if (!e) throw new Error('Do not set the proxyServer parameter as empty')
              ;(t.proxyServer = e), p.report.setProxyServer(e)
            }),
            (t.setEncryptionSecret = function(e) {
              if (!e || typeof e !== 'string') throw new Error('Invalid secret')
              t.aespassword = e
            }),
            (t.setEncryptionMode = function(e) {
              if (typeof e !== 'string') throw new Error('Invalid encryptionMode')
              S.ENCRYPTION_MODES.includes(e) || (e = S.ENCRYPTION_MODE_NONE), (t.aesmode = e)
            }),
            (t.configPublisher = function(e) {
              t.gatewayClient.configPublisher(e)
            }),
            (t.enableDualStream = function(n, i) {
              return (0, f.getBrowserOS)() === 'iOS'
                ? (p.report.streamSwitch(e.sessionId, { lts: new Date().getTime(), isdual: !0, succ: !1 }), i && i(c.default.IOS_NOT_SUPPORT))
                : (0, f.isWeChatBrowser)()
                  ? (p.report.streamSwitch(e.sessionId, { lts: new Date().getTime(), isdual: !0, succ: !1 }), i && i(c.default.WECHAT_NOT_SUPPORT))
                  : (p.report.streamSwitch(e.sessionId, { lts: new Date().getTime(), isdual: !0, succ: !0 }),
                    (t.isDualStream = !0),
                    void (t.highStreamState === 0
                      ? t._publishLowStream(n, function(e) {
                          s.default.warning(e), i && i(c.default.ENABLE_DUALSTREAM_FAILED)
                        })
                      : t.highStreamState === 1
                        ? i && i(c.default.STILL_ON_PUBLISHING)
                        : n && n()))
            }),
            (t.disableDualStream = function(n, i) {
              p.report.streamSwitch(e.sessionId, { lts: new Date().getTime(), isdual: !1, succ: !0 }),
                (t.isDualStream = !1),
                0 === t.highStreamState
                  ? t._unpublishLowStream(n, function(e) {
                      s.default.warning(e), i && i(c.default.DISABLE_DUALSTREAM_FAILED)
                    })
                  : t.highStreamState === 1
                    ? i && i(c.default.STILL_ON_PUBLISHING)
                    : n && n()
            }),
            (t._createLowStream = function(e, n) {
              if (!t.highStream || !t.highStream.stream) return void (n && n(c.default.HIGH_STREAM_NOT_VIDEO_TRACE))
              var i = Object.assign({}, t.highStream.params)
              if (((i.streamID += 1), (i.audio = !1), !i.video)) return void (n && n(c.default.HIGH_STREAM_NOT_VIDEO_TRACE))
              var r = t.highStream.stream.getVideoTracks()[0]
              if (!r) return void (n && n(c.default.HIGH_STREAM_NOT_VIDEO_TRACE))
              t._getVideoCameraIdByLabel(
                r.label,
                function(o) {
                  i.cameraId = o
                  var a = new g.Stream(i)
                  if (((a.streamId = t.highStream.getId() + 1), t.lowStreamParameter)) {
                    var d = Object.assign({}, t.lowStreamParameter)
                    if (!d.width || !d.height) {
                      var c = (0, m.simMap)(t.highStream.profile),
                        u = _.SUPPORT_RESOLUTION_LIST[c[0]]
                      ;(d.width = u[0]), (d.height = u[1])
                    }
                    if (((d.framerate = d.framerate || 5), (d.bitrate = d.bitrate || 50), (0, f.isSafari)() || (0, f.isOpera)())) {
                      s.default.debug('Shimming lowStreamParameter')
                      var u = _.SUPPORT_RESOLUTION_LIST[t.highStream.profile]
                      ;(d.width = u[0]), (d.height = u[1])
                    }
                    a.setVideoProfileCustomPlus(d)
                  } else a.setVideoProfileCustom((0, m.simMap)(t.highStream.profile))
                  a.init(function() {
                    ;(t.highStream.lowStream = a), r.enabled || a.disableVideo(), e && e(a)
                  }, n)
                },
                n
              )
            }),
            (t._getLowStream = function(e, n) {
              t.lowStream
                ? e(t.lowStream)
                : t._createLowStream(function(n) {
                    ;(t.lowStream = n), e(t.lowStream)
                  }, n)
            }),
            (t._publishLowStream = function(e, n) {
              return t.lowStreamState !== 2
                ? n && n(c.default.LOW_STREAM_ALREADY_PUBLISHED)
                : t.highStream && t.highStream.hasScreen()
                  ? n && n(c.default.SHARING_SCREEN_NOT_SUPPORT)
                  : void t._getLowStream(function(i) {
                      ;(t.lowStreamState = 1),
                        t.gatewayClient.publish(
                          i,
                          function() {
                            ;(t.lowStreamState = 0), e && e()
                          },
                          function(e) {
                            s.default.debug('publish low stream failed'), n && n(e)
                          }
                        )
                    }, n)
            }),
            (t._unpublishLowStream = function(e, n) {
              if (t.lowStreamState !== 0) return n && n(c.default.LOW_STREAM_NOT_YET_PUBLISHED)
              t.lowStream &&
                (t.gatewayClient.unpublish(
                  t.lowStream,
                  function() {},
                  function(e) {
                    s.default.debug('unpublish low stream failed'), n && n(e)
                  }
                ),
                t.lowStream.close(),
                (t.lowStream = null),
                (t.lowStreamState = 2),
                e && e())
            }),
            (t.join = function(n, i, r, o, a) {
              if (n && typeof n !== 'string') return s.default.warning('Param channelKey should be string'), a && a(c.default.INVALID_PARAMETER)
              if (typeof i !== 'string') return s.default.warning('Param channel should be string'), a && a(c.default.INVALID_PARAMETER)
              if (r && (typeof r !== 'number' || !(0, u.is32Uint)(r))) return s.default.warning('Param uid should be number'), a && a(c.default.INVALID_PARAMETER)
              ;(t.highStream = null), (t.lowStream = null), (t.lowStreamParameter = null), (t.isDualStream = !1), (t.highStreamState = 2), (t.lowStreamState = 2)
              var d = { appId: e.appId, sid: e.sessionId, cname: i, uid: r, turnServer: t.turnServer, proxyServer: t.proxyServer }
              t.aespassword && t.aesmode !== S.ENCRYPTION_MODE_NONE && Object.assign(d, { aespassword: t.aespassword, aesmode: t.aesmode }),
                p.report.sessionInit(e.sessionId, { lts: new Date().getTime(), cname: i, appid: e.appId, mode: e.mode, succ: !0 }),
                (t.onSuccess = o),
                (t.onFailure = a),
                (t.channel = i),
                (0, l.getGatewayList)(
                  d,
                  function(r) {
                    s.default.info('Joining channel: ' + i),
                      (t.key = n || e.appId),
                      (d.cid = r.cid),
                      (d.uid = r.uid),
                      (d.uni_lbs_ip = r.uni_lbs_ip),
                      (d.gatewayAddr = r.gateway_addr),
                      (t.joinInfo = d),
                      t.gatewayClient.join(
                        d,
                        t.key,
                        function(e) {
                          s.default.info('Join channal ' + i + ' success'), o && o(e)
                        },
                        a
                      )
                  },
                  a
                )
            }),
            (t.renewChannelKey = function(e, n, i) {
              void 0 === t.key
                ? (s.default.error('renewChannelKey should not be called before user join'), (0, u.safeCall)(i, c.default.INVALID_OPERATION))
                : ((t.key = e), (t.gatewayClient.key = e), t.gatewayClient.rejoin(), (0, u.safeCall)(n))
            }),
            (t.leave = function(e, n) {
              s.default.info('Leaving channel'), t.gatewayClient.leave(e, n)
            }),
            (t._publish = function(n, i, r) {
              if (t.highStreamState !== 2) return s.default.warning("Can't publish stream when stream already publish", n.getId()), r && r(c.default.STREAM_ALREADY_PUBLISHED)
              s.default.info('Publishing stream, uid: ', n.getId()),
                (t.highStream = n),
                (t.highStreamState = 1),
                (t.highStream.streamId = t.joinInfo.uid),
                t.gatewayClient.publish(
                  n,
                  function() {
                    ;(n.sid = e.sessionId),
                      (t.highStreamState = 0),
                      s.default.info('Publish success, uid:', n.getId()),
                      t.isDualStream
                        ? t._publishLowStream(
                            function() {
                              i && i()
                            },
                            function() {
                              i && i()
                            }
                          )
                        : i && i()
                  },
                  r
                )
            }),
            (t._unpublish = function(e, n, i) {
              if (t.highStreamState !== 0) return s.default.warning("Can't unpublish stream when stream not publish"), i && i(c.default.STREAM_NOT_YET_PUBLISHED)
              s.default.info('Unpublish stream, uid: ', e.getId()),
                t.isDualStream && t.lowStream
                  ? (t._unpublishLowStream(null, i), t.gatewayClient.unpublish(e, null, i), (t.highStreamState = 2), s.default.info('Unpublish stream success, uid:', e.getId()))
                  : (t.gatewayClient.unpublish(e, null, i), (t.highStreamState = 2), s.default.info('Unpublish stream success, uid:', e.getId())),
                n && n()
            }),
            (t.publish = function(e, n) {
              if (t.highStreamState !== 2) return void (n && n(c.default.STREAM_ALREADY_PUBLISHED))
              t._publish(e, null, n)
            }),
            (t.unpublish = function(e, n) {
              if (t.highStreamState !== 0) return void (n && n(c.default.STREAM_NOT_YET_PUBLISHED))
              t._unpublish(e, null, n)
            }),
            (t.subscribe = function(e, n) {
              s.default.info('Subscribe stream, uid: ', e.getId()), t.gatewayClient.subscribe(e, null, n)
            }),
            (t.unsubscribe = function(e, n) {
              s.default.info('Unsubscribe stream, uid: ', e.getId()), t.gatewayClient.unsubscribe(e, null, n)
            }),
            (t.setRemoteVideoStreamType = function(e, n) {
              t.gatewayClient.setRemoteVideoStreamType(e, n)
            }),
            (t.startLiveStreaming = function(e, n) {
              t.gatewayClient.startLiveStreaming(e, n)
            }),
            (t.stopLiveStreaming = function(e) {
              t.gatewayClient.stopLiveStreaming(e)
            }),
            (t.setLiveTranscoding = function(e) {
              Object.assign(I, e), t.gatewayClient.setLiveTranscoding(I)
            }),
            (t.gatewayClient = (0, o.default)(e)),
            (t.on = t.gatewayClient.on),
            e && e.turnServer && t.setTurnServer(e.turnServer),
            e && e.proxyServer && t.setProxyServer(e.proxyServer),
            t.on('onMultiIP', function(e) {
              t.gatewayClient.closeGateway(),
                (t.gatewayClient.socket = void 0),
                (t.gatewayClient.hasChangeBGPAddress = !0),
                (t.joinInfo.multiIP = e.arg.option),
                (0, l.getGatewayList)(
                  t.joinInfo,
                  function(e) {
                    s.default.info('Joining channel: ' + t.channel),
                      (t.joinInfo.cid = e.cid),
                      (t.joinInfo.uid = e.uid),
                      (t.joinInfo.uni_lbs_ip = e.uni_lbs_ip),
                      (t.joinInfo.gatewayAddr = e.gateway_addr),
                      t.gatewayClient.join(
                        t.joinInfo,
                        t.key,
                        function(e) {
                          s.default.info('Join channal ' + t.channel + ' success'), t.onSuccess && t.onSuccess(e)
                        },
                        t.onFailure
                      )
                  },
                  t.onFailure
                )
            }),
            t.on('rejoin', function() {
              var e = t.highStreamState === 2 ? 2 : 0
              t.highStream &&
                e === 0 &&
                (s.default.info('publish after rejoin'),
                (t.highStreamState = 2),
                (t.lowStreamState = 2),
                t.publish(t.highStream, function(e) {
                  e && s.default.info(e)
                }))
            }),
            t.on('pubP2PLost', function(e) {
              s.default.debug('Start reconnect local peerConnection :', t.highStream.getId()),
                t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-start', id: t.highStream.getId() }),
                1 === t.highStreamState && ((t.highStreamState = 0), (t.lowStreamState = 0)),
                t._unpublish(
                  t.highStream,
                  function() {
                    t._publish(
                      t.highStream,
                      function() {
                        s.default.debug('Reconnect local peerConnection success:', t.highStream.getId()), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: t.highStream.getId() })
                      },
                      function(e) {
                        s.default.debug('Reconnect local peerConnection failed:' + e), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: t.highStream.getId() })
                      }
                    )
                  },
                  function(e) {
                    s.default.debug('Reconnect local peerConnection failed:' + e), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: t.highStream.getId() })
                  }
                )
            }),
            t.on('subP2PLost', function(e) {
              s.default.debug('Start reconnect remote peerConnection :', e.stream.getId()),
                t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-start', id: e.stream.getId() }),
                t.gatewayClient.unsubscribe(
                  e.stream,
                  function() {
                    t.gatewayClient.subscribe(
                      e.stream,
                      function() {
                        s.default.debug('Reconnect remote peerConnection success:', e.stream.getId()), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: e.stream.getId() })
                      },
                      function(n) {
                        s.default.debug('Reconnect remote peerConnection failed:' + n), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: e.stream.getId() })
                      }
                    )
                  },
                  function(n) {
                    s.default.debug('Reconnect remote peerConnection failed:' + n), t.gatewayClient.dispatchEvent({ type: 'stream-reconnect-end', id: e.stream.getId() })
                  }
                )
            }),
            t
          )
        },
        h = { uid: 0, x: 0, y: 0, width: 0, height: 0, zOrder: 0, alpha: 1 },
        I = {
          width: 640,
          height: 360,
          videoBitrate: 400,
          videoFramerate: 15,
          lowLatency: !1,
          audioSampleRate: S.AUDIO_SAMPLE_RATE_48000,
          audioBitrate: 48,
          audioChannels: 1,
          videoGop: 30,
          videoCodecProfile: S.VIDEO_CODEC_PROFILE_HIGH,
          userCount: 0,
          userConfigExtraInfo: {},
          backgroundColor: 0,
          transcodingUsers: []
        },
        R = function(e) {
          switch (e) {
            case S.CLIENT_MODE_H264_INTEROP:
              return S.CLIENT_CODEC_H264
            default:
              return S.CLIENT_CODEC_VP8
          }
        },
        b = function(e) {
          return S.CLIENT_MODES.includes(e.mode)
            ? S.CLIENT_CODECS.includes(e.codec)
              ? e.mode == S.CLIENT_MODE_H264_INTEROP && e.codec !== S.CLIENT_CODEC_H264 && c.default.CLIENT_MODE_CODEC_MISMATCH
              : c.default.INVALID_CLIENT_CODEC
            : c.default.INVALID_CLIENT_MODE
        },
        y = function(e) {
          switch (e.mode) {
            case S.CLIENT_MODE_INTEROP:
            case S.CLIENT_MODE_H264_INTEROP:
              e.mode = S.CLIENT_MODE_LIVE
              break
            case S.CLIENT_MODE_WEBONLY:
              e.mode = S.CLIENT_MODE_RTC
          }
        },
        A = function(e) {
          ;(e = Object.assign({}, e || {})), e.codec || (e.codec = R(e.mode))
          var t = b(e)
          if (t) throw (s.default.error('Invalid parameter setting MODE : ' + e.mode + ' CODEC : ' + e.codec + ' ERROR ' + t), new Error(t))
          return s.default.info('Creating client , MODE : ' + e.mode + ' CODEC : ' + e.codec), y(e), E(e)
        },
        C = function() {
          return s.default.info('Creating client , MODE : rtc'), s.default.warning('createRtcClient is deprecated.'), E({ mode: 'rtc' })
        },
        O = function(e) {
          var t = 'host'
          return e && e.role === 'audience' && (t = e.role), s.default.info('Creating client , MODE : live'), s.default.warning('createLiveClient is deprecated.'), E({ mode: 'live', role: t })
        }
      ;(t.createClient = A), (t.createRtcClient = C), (t.createLiveClient = O), (t.TranscodingUser = h), (t.LiveTranscoding = I)
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 })
      var r =
          typeof Symbol === 'function' && typeof Symbol.iterator === 'symbol'
            ? function(e) {
                return typeof e
              }
            : function(e) {
                return e && typeof Symbol === 'function' && e.constructor === Symbol && e !== Symbol.prototype ? 'symbol' : typeof e
              },
        o = n(8),
        a = n(32),
        s = i(a),
        d = n(9),
        c = n(3),
        u = n(0),
        l = i(u),
        p = n(7),
        f = i(p),
        m = n(12),
        g = i(m),
        v = n(13),
        _ = n(1),
        S = n(5),
        E = n(6),
        h = n(2),
        I = n(34),
        R = i(I),
        b = function(e) {
          var t = !1,
            n = function() {
              return { _type: 'ping' }
            },
            i = function() {
              return {
                _type: 'join1',
                message: {
                  appId: e.appId,
                  key: M.key,
                  channel: M.joinInfo.cname,
                  uid: M.uid,
                  version: _.VERSION,
                  browser: navigator.userAgent,
                  mode: e.mode,
                  codec: e.codec,
                  role: e.role,
                  config: M.config
                }
              }
            },
            a = function() {
              return { _type: 'leave' }
            },
            u = function(e) {
              return { _type: 'control', message: e }
            },
            m = function(e) {
              var t = e
              return e.uni_lbs_ip && (t = Object.assign(e, { wanip: e.uni_lbs_ip, hasChange: M.hasChangeBGPAddress })), { _type: 'token', message: t }
            },
            I = function(e) {
              return { _type: 'unpublish', message: e }
            },
            b = function(e) {
              return { _type: 'unsubscribe', message: e }
            },
            y = function(e, t) {
              return { _type: 'switchVideoStream', message: { id: e, type: t } }
            },
            A = function(e, t, n) {
              return { _type: 'publish', options: e, sdp: t, p2pid: n }
            },
            C = function(e) {
              return { _type: 'publish_stats', options: { stats: e }, sdp: null }
            },
            O = function(e) {
              return { _type: 'publish_stats_low', options: { stats: e }, sdp: null }
            },
            T = function(e, t, n) {
              return { _type: 'subscribe', options: e, sdp: t, p2pid: n }
            },
            w = function(e, t) {
              return { _type: 'subscribe_stats', options: { id: e, stats: t }, sdp: null }
            },
            N = function(e, t) {
              return { _type: 'start_live_streaming', message: { url: e, transcodingEnabled: t } }
            },
            D = function(e) {
              return { _type: 'stop_live_streaming', message: { url: e } }
            },
            L = function(e) {
              return { _type: 'set_live_transcoding', message: { transcoding: e } }
            },
            M = (0, c.EventDispatcher)(e)
          ;(M.socket = void 0),
            (M.state = 0),
            (M.mode = e.mode),
            (M.role = e.role),
            (M.codec = e.codec),
            (M.config = {}),
            (M.timers = {}),
            (M.timer_counter = {}),
            (M.localStreams = {}),
            (M.remoteStreams = {}),
            (M.attemps = 1),
            (M.p2p_attemps = 1),
            (M.audioLevel = {}),
            (M.activeSpeaker = void 0),
            (M.reconnectMode = 'retry'),
            (M.rejoinAttempt = 0),
            (M.hasChangeBGPAddress = !1),
            (M.p2ps = new Map()),
            (M.firstFrameTimer = new Map()),
            (M.liveStreams = new Map()),
            (M.remoteStreamsInChannel = new Set())
          var P = g.default
          ;(M.p2pCounter = (0, E.random)(1e5)),
            (M.generateP2PId = function() {
              return ++M.p2pCounter
            }),
            (M.remoteVideoStreamTypes = { REMOTE_VIDEO_STREAM_HIGH: 0, REMOTE_VIDEO_STREAM_LOW: 1, REMOTE_VIDEO_STREAM_MEDIUM: 2 }),
            (M.configPublisher = function(e) {
              M.config = e
            }),
            (M.join = function(e, t, r, o) {
              var a = new Date().getTime(),
                s = e.uid
              return M.state !== 0
                ? (l.default.error('Server already in connecting/connected state'),
                  o && o(f.default.INVALID_OPERATION),
                  void S.report.joinGateway(e.sid, { lts: a, succ: !1, ec: f.default.INVALID_OPERATION, addr: null }))
                : s !== null && void 0 !== s && parseInt(s) !== s
                  ? (l.default.error('Input uid is invalid'), o && o(f.default.INVALID_PARAMETER), void S.report.joinGateway(e.sid, { lts: a, succ: !1, ec: f.default.INVALID_PARAMETER, addr: null }))
                  : ((M.joinInfo = Object.assign({}, e)),
                    (M.uid = s),
                    (M.key = t),
                    (M.state = 1),
                    void W(
                      e,
                      function(t) {
                        ;(M.state = 2),
                          l.default.debug('Connected to gateway server'),
                          (M.pingTimer = setInterval(function() {
                            G(n(), function() {}, function(e) {})
                          }, 3e3)),
                          G(
                            i(),
                            function(t) {
                              S.report.joinGateway(e.sid, { lts: a, succ: !0, ec: null, vid: t.vid, addr: M.socket.getURL() }), (M.rejoinAttempt = 0), r && r(M.uid)
                            },
                            function(t) {
                              l.default.error('User join failed [' + t + ']'),
                                R.default[t] && M.rejoinAttempt < 4 ? M._doWithAction(R.default[t], r, o) : o && o(t),
                                S.report.joinGateway(e.sid, { lts: a, succ: !1, ec: t, addr: M.socket.getURL() })
                            }
                          )
                      },
                      function(t) {
                        l.default.error('User join failed [' + t + ']'), o && o(t), S.report.joinGateway(e.sid, { lts: a, succ: !1, ec: t, addr: M.socket.getURL() })
                      }
                    ))
            }),
            (M.leave = function(e, t) {
              if (M.state != 2) return void P(e)
              clearInterval(M.pingTimer)
              for (var n in M.timers) M.timers.hasOwnProperty(n) && clearInterval(M.timers[n])
              G(
                a(),
                function(t) {
                  M.socket.close(), (M.socket = void 0), l.default.info('Leave channal success'), e && e(t)
                },
                t
              )
              for (var n in M.localStreams)
                if (M.localStreams.hasOwnProperty(n)) {
                  var i = M.localStreams[n]
                  delete M.localStreams[n], void 0 !== i.pc && (i.pc.close(), (i.pc = void 0))
                }
              K(), (M.state = 0)
            }),
            (M.publish = function(e, t, n) {
              var i = new Date().getTime()
              if (((e.publishLTS = i), (void 0 === e ? 'undefined' : r(e)) !== 'object' || e === null))
                {return l.default.error('Invalid local stream'), n && n(f.default.INVALID_LOCAL_STREAM), void S.report.publish(M.joinInfo.sid, { lts: i, succ: !1, ec: f.default.INVALID_LOCAL_STREAM })}
              if (e.stream === null && void 0 === e.url)
                return (
                  l.default.error('Invalid local media stream'), n && n(f.default.INVALID_LOCAL_STREAM), void S.report.publish(M.joinInfo.sid, { lts: i, succ: !1, ec: f.default.INVALID_LOCAL_STREAM })
                )
              if (M.state !== 2)
                return l.default.error('User is not in the session'), n && n(f.default.INVALID_OPERATION), void S.report.publish(M.joinInfo.sid, { lts: i, succ: !1, ec: f.default.INVALID_OPERATION })
              var o = e.getAttributes() || {}
              if (e.local && void 0 === M.localStreams[e.getId()] && (e.hasAudio() || e.hasVideo() || e.hasScreen())) {
                var a = M.generateP2PId()
                M.p2ps.set(a, e),
                  (e.p2pId = a),
                  void 0 !== e.url
                    ? z(A({ state: 'url', audio: e.hasAudio(), video: e.hasVideo(), attributes: e.getAttributes(), mode: M.mode }, e.url), function(t, n) {
                        'success' === t
                          ? ((e.getUserId = function() {
                              return n
                            }),
                            (M.localStreams[n] = e),
                            (e.onClose = function() {
                              M.unpublish(e)
                            }))
                          : l.default.error('Publish local stream failed', t)
                      })
                    : ((M.localStreams[e.getId()] = e),
                      (e.pc = (0, d.Connection)({
                        callback: function(r) {
                          l.default.debug('SDP exchange in publish : send offer --  ', JSON.parse(r)),
                            z(
                              A(
                                {
                                  state: 'offer',
                                  id: e.getId(),
                                  audio: e.hasAudio(),
                                  video: e.hasVideo() || e.hasScreen(),
                                  attributes: e.getAttributes(),
                                  mode: M.mode,
                                  codec: M.codec,
                                  p2pid: a,
                                  turnip: M.joinInfo.turnServer.url,
                                  turnport: Number(M.joinInfo.turnServer.udpport),
                                  turnusername: M.joinInfo.turnServer.username,
                                  turnpassword: M.joinInfo.turnServer.credential
                                },
                                r
                              ),
                              function(o, s) {
                                if (o === 'error')
                                  return (
                                    l.default.error('Publish local stream failed'),
                                    n && n(f.default.PUBLISH_STREAM_FAILED),
                                    void S.report.publish(M.joinInfo.sid, { lts: i, succ: !1, localSDP: r, ec: f.default.PUBLISH_STREAM_FAILED })
                                  )
                                ;(e.pc.onsignalingmessage = function(t) {
                                  ;(e.pc.onsignalingmessage = function() {}),
                                    z(A({ state: 'ok', id: e.getId(), audio: e.hasAudio(), video: e.hasVideo(), screen: e.hasScreen(), attributes: e.getAttributes(), mode: M.mode }, t)),
                                    (e.getUserId = function() {
                                      return s.id
                                    }),
                                    l.default.info('Local stream published with uid', s.id),
                                    (e.onClose = function() {
                                      M.unpublish(e)
                                    }),
                                    (e.unmuteAudio = function() {
                                      G(u({ action: 'audio-out-on', streamId: e.getId() }), function() {}, function() {})
                                    }),
                                    (e.unmuteVideo = function() {
                                      G(u({ action: 'video-out-on', streamId: e.getId() }), function() {}, function() {})
                                    }),
                                    (e.muteAudio = function() {
                                      G(u({ action: 'audio-out-off', streamId: e.getId() }), function() {}, function() {})
                                    }),
                                    (e.muteVideo = function() {
                                      G(u({ action: 'video-out-off', streamId: e.getId() }), function() {}, function() {})
                                    }),
                                    e.getId() === e.getUserId() &&
                                      (e.isAudioOn() || (e.hasAudio() && (l.default.debug('local stream audio mute'), e.muteAudio())),
                                      e.isVideoOn() || ((e.hasVideo() || e.hasScreen()) && (l.default.debug('local stream video mute'), e.muteVideo())))
                                }),
                                  (e.pc.oniceconnectionstatechange = function(r) {
                                    'failed' === r
                                      ? (void 0 != M.timers[e.getId()] && clearInterval(M.timers[e.getId()]),
                                        l.default.error('Publisher connection is lost -- streamId: ' + e.getId() + ' p2pId: ' + a),
                                        M.p2ps.delete(a),
                                        l.default.debug('publish p2p failed: ', M.p2ps),
                                        n && n(f.default.PEERCONNECTION_FAILED),
                                        M.dispatchEvent((0, c.ClientEvent)({ type: 'pubP2PLost', stream: e })),
                                        S.report.publish(M.joinInfo.sid, { lts: i, succ: !1, ec: f.default.PEERCONNECTION_FAILED }))
                                      : r === 'connected' && (l.default.debug('publish p2p connected: ', M.p2ps), t && t(), S.report.publish(M.joinInfo.sid, { lts: i, succ: !0, ec: null }))
                                  }),
                                  l.default.debug('SDP exchange in publish : receive answer --  ', JSON.parse(o)),
                                  e.pc.processSignalingMessage(o)
                              }
                            )
                        },
                        audio: e.hasAudio(),
                        video: e.hasVideo(),
                        screen: e.hasScreen(),
                        isSubscriber: !1,
                        stunServerUrl: M.stunServerUrl,
                        turnServer: M.joinInfo.turnServer,
                        maxAudioBW: o.maxAudioBW,
                        minVideoBW: o.minVideoBW,
                        maxVideoBW: o.maxVideoBW,
                        mode: M.mode,
                        codec: M.codec
                      })),
                      e.pc.addStream(e.stream),
                      l.default.debug('PeerConnection add stream :', e.stream),
                      (M.timers[e.getId()] = setInterval(function() {
                        e &&
                          e.pc &&
                          e.pc.getStats &&
                          e.pc.getStatsRate(function(t) {
                            t.forEach(function(t) {
                              ;(t.id.indexOf('outbound_rtp') === -1 && t.id.indexOf('OutboundRTP') === -1) ||
                                t.mediaType !== 'video' ||
                                ((t.googFrameWidthSent = e.videoWidth + ''), (t.googFrameHeightSent = e.videoHeight + '')),
                                e.getUserId && (e.getId() == e.getUserId() ? G(C(t), null, null) : G(O(t), null, null))
                            })
                          })
                      }, 3e3)),
                      void 0 !== e.aux_stream && (e.pc.addStream(e.aux_stream), l.default.debug('PeerConnection add stream :', e.aux_stream)))
              }
            }),
            (M.unpublish = function(e, t, n) {
              return (void 0 === e ? 'undefined' : r(e)) !== 'object' || e === null
                ? (l.default.error('Invalid local stream'), void P(n, f.default.INVALID_LOCAL_STREAM))
                : M.state !== 2
                  ? (l.default.error('User not in the session'), void P(n, f.default.INVALID_OPERATION))
                  : (void 0 != M.timers[e.getId()] && clearInterval(M.timers[e.getId()]),
                    void (void 0 !== M.socket
                      ? e.local && void 0 !== M.localStreams[e.getId()]
                        ? (delete M.localStreams[e.getId()],
                          G(I(e.getUserId())),
                          (e.hasAudio() || e.hasVideo() || e.hasScreen()) && void 0 === e.url && void 0 !== e.pc && (e.pc.close(), (e.pc = void 0)),
                          (e.onClose = void 0),
                          (e.unmuteAudio = void 0),
                          (e.muteAudio = void 0),
                          (e.unmuteVideo = void 0),
                          (e.muteVideo = void 0),
                          M.p2ps.delete(e.p2pId),
                          t && t())
                        : (l.default.error('Invalid local stream'), P(n, f.default.INVALID_LOCAL_STREAM))
                      : (l.default.error('User not in the session'), P(n, f.default.INVALID_OPERATION))))
            }),
            (M.subscribe = function(e, t, n) {
              var i = new Date().getTime()
              if (((e.subscribeLTS = i), (void 0 === e ? 'undefined' : r(e)) !== 'object' || e === null))
                return (
                  l.default.error('Invalid remote stream'),
                  n && n(f.default.INVALID_REMOTE_STREAM),
                  void S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.INVALID_REMOTE_STREAM })
                )
              if (M.state !== 2)
                return (
                  l.default.error('User is not in the session'),
                  n && n(f.default.INVALID_OPERATION),
                  void S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.INVALID_OPERATION })
                )
              if (!e.local && M.remoteStreams.hasOwnProperty(e.getId()))
                if (e.hasAudio() || e.hasVideo() || e.hasScreen()) {
                  var o = M.generateP2PId()
                  M.p2ps.set(o, e),
                    (e.p2pId = o),
                    (e.pc = (0, d.Connection)({
                      callback: function(t) {
                        l.default.debug('SDP exchange in subscribe : send offer --  ', JSON.parse(t)),
                          z(
                            T(
                              {
                                streamId: e.getId(),
                                audio: !0,
                                video: !0,
                                mode: M.mode,
                                codec: M.codec,
                                p2pid: o,
                                turnip: M.joinInfo.turnServer.url,
                                turnport: Number(M.joinInfo.turnServer.udpport),
                                turnusername: M.joinInfo.turnServer.username,
                                turnpassword: M.joinInfo.turnServer.credential
                              },
                              t
                            ),
                            function(t) {
                              if (t === 'error')
                                return (
                                  l.default.error('Subscribe remote stream failed, closing stream ', e.getId()),
                                  e.close(),
                                  n && n(f.default.SUBSCRIBE_STREAM_FAILED),
                                  void S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.SUBSCRIBE_STREAM_FAILED })
                                )
                              l.default.debug('SDP exchange in subscribe : receive answer --  ', JSON.parse(t)), e.pc.processSignalingMessage(t)
                            }
                          )
                      },
                      nop2p: !0,
                      audio: !0,
                      video: !0,
                      screen: e.hasScreen(),
                      isSubscriber: !0,
                      stunServerUrl: M.stunServerUrl,
                      turnServer: M.joinInfo.turnServer
                    })),
                    (e.pc.onaddstream = function(t, n) {
                      if ((n === 'ontrack' && t.track.kind === 'video') || n === 'onaddstream') {
                        if (
                          (l.default.info('Remote stream subscribed with uid ', e.getId()),
                          (M.remoteStreams[e.getId()].stream = n === 'onaddstream' ? t.stream : t.streams[0]),
                          M.remoteStreams[e.getId()].hasVideo())
                        ) {
                          if ((0, h.isFireFox)()) {
                            var i = M.remoteStreams[e.getId()].stream
                            ;(0, E.vsResHack)(
                              i,
                              function(t, n) {
                                ;(e.videoWidth = t), (e.videoHeight = n)
                              },
                              function(e) {
                                return l.default.warning('vsResHack failed:' + e)
                              }
                            )
                          }
                        } else M.remoteStreams[e.getId()].disableVideo()
                        var r = (0, c.StreamEvent)({ type: 'stream-subscribed', stream: M.remoteStreams[e.getId()] })
                        M.dispatchEvent(r)
                      }
                      ;(e.unmuteAudio = function() {
                        G(u({ action: 'audio-in-on', streamId: e.getId() }), function() {}, function() {})
                      }),
                        (e.muteAudio = function() {
                          G(u({ action: 'audio-in-off', streamId: e.getId() }), function() {}, function() {})
                        }),
                        (e.unmuteVideo = function() {
                          G(u({ action: 'video-in-on', streamId: e.getId() }), function() {}, function() {})
                        }),
                        (e.muteVideo = function() {
                          G(u({ action: 'video-in-off', streamId: e.getId() }), function() {}, function() {})
                        })
                    }),
                    (M.timers[e.getId()] = setInterval(function() {
                      e &&
                        e.pc &&
                        e.pc.getStats &&
                        e.pc.getStatsRate(function(t) {
                          t.forEach(function(t) {
                            t.id.indexOf('inbound_rtp') !== -1 && t.mediaType === 'video' && ((t.googFrameWidthReceived = e.videoWidth + ''), (t.googFrameHeightReceived = e.videoHeight + '')),
                              z(w(e.getId(), t), null, null)
                          })
                        })
                    }, 3e3)),
                    (M.audioLevel[e.getId()] = 0),
                    (M.timers[e.getId() + 'audio'] = setInterval(function() {
                      e &&
                        e.pc &&
                        e.pc.getStats &&
                        e.pc.getStats(function(t) {
                          t.forEach(function(t) {
                            if (t.mediaType === 'audio') {
                              if (t.audioOutputLevel > 5e3) {
                                M.audioLevel[e.getId()] < 20 && (M.audioLevel[e.getId()] += 1)
                                for (var n in M.audioLevel) parseInt(n) !== e.getId() && M.audioLevel[n] > 0 && (M.audioLevel[n] -= 1)
                              }
                              var i = Object.keys(M.audioLevel),
                                r = i.sort(function(e, t) {
                                  return M.audioLevel[t] - M.audioLevel[e]
                                })
                              if (M.activeSpeaker !== r[0]) {
                                var o = (0, c.ClientEvent)({ type: 'active-speaker', uid: r[0] })
                                M.dispatchEvent(o), (M.activeSpeaker = r[0]), l.default.debug('Update active speaker:' + M.activeSpeaker)
                              }
                            }
                          })
                        })
                    }, 50)),
                    (e.pc.oniceconnectionstatechange = function(r) {
                      'failed' === r
                        ? (void 0 != M.timers[e.getId()] && (clearInterval(M.timers[e.getId()]), clearInterval(M.timers[e.getId()] + 'audio')),
                          l.default.error('Subscriber connection is lost -- streamId: ' + e.getId() + ' p2pId: ' + o),
                          M.p2ps.delete(o),
                          l.default.debug('subscribe p2p failed: ', M.p2ps),
                          n && n(f.default.PEERCONNECTION_FAILED),
                          M.remoteStreams[e.getId()] && M.dispatchEvent((0, c.ClientEvent)({ type: 'subP2PLost', stream: e })),
                          S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.PEERCONNECTION_FAILED }))
                        : r === 'connected' &&
                          (t && t(),
                          l.default.debug('subscribe p2p connected: ', M.p2ps),
                          S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !0, peerid: e.getId() + '', ec: null }),
                          M.firstFrameTimer.set(
                            e.getId(),
                            setInterval(function() {
                              e.pc
                                ? e.pc.getStats(function(t) {
                                    t.forEach(function(t) {
                                      ;(t.id.indexOf('recv') === -1 && t.id.indexOf('inbound_rtp') === -1 && t.id.indexOf('InboundRTP') === -1) ||
                                        (t.mediaType === 'video' &&
                                          (t.framesDecoded > 0 || t.googFramesDecoded > 0) &&
                                          (clearInterval(M.firstFrameTimer.get(e.getId())),
                                          M.firstFrameTimer.delete(e.getId()),
                                          S.report.firstRemoteFrame(M.joinInfo.sid, {
                                            lts: new Date().getTime(),
                                            peerid: e.getId() + '',
                                            succ: !0,
                                            width: +t.googFrameWidthReceived,
                                            height: +t.googFrameHeightReceived
                                          })))
                                    })
                                  })
                                : (clearInterval(M.firstFrameTimer.get(e.getId())), M.firstFrameTimer.delete(e.getId()))
                            }, 100)
                          ))
                    })
                } else
                  l.default.error('Invalid remote stream'),
                    n && n(f.default.INVALID_REMOTE_STREAM),
                    S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.INVALID_REMOTE_STREAM })
              else
                {l.default.error('No such remote stream'),
              n && n(f.default.NO_SUCH_REMOTE_STREAM),
              S.report.subscribe(M.joinInfo.sid, { lts: i, succ: !1, peerid: e.getId() + '', ec: f.default.INVALID_REMOTE_STREAM })}
            }),
            (M.unsubscribe = function(e, t, n) {
              return (void 0 === e ? 'undefined' : r(e)) !== 'object' || e === null
                ? (l.default.error('Invalid remote stream'), void P(n, f.default.INVALID_REMOTE_STREAM))
                : M.state !== 2
                  ? (l.default.error('User is not in the session'), void P(n, f.default.INVALID_OPERATION))
                  : (void 0 != M.timers[e.getId()] && (clearInterval(M.timers[e.getId()]), clearInterval(M.timers[e.getId()] + 'audio')),
                    void 0 != M.audioLevel[e.getId()] && delete M.audioLevel[e.getId()],
                    void 0 != M.timer_counter[e.getId()] && delete M.timer_counter[e.getId()],
                    M.remoteStreams.hasOwnProperty(e.getId())
                      ? M.socket
                        ? e.local
                          ? (l.default.error('Invalid remote stream'), void P(n, f.default.INVALID_REMOTE_STREAM))
                          : (e.close(),
                            void G(
                              b(e.getId()),
                              function(i) {
                                if (i === 'error') return l.default.error('Unsubscribe remote stream failed', e.getId()), void P(n, f.default.UNSUBSCRIBE_STREAM_FAILED)
                                void 0 !== e.pc && (e.pc.close(), (e.pc = void 0)),
                                  (e.onClose = void 0),
                                  (e.unmuteAudio = void 0),
                                  (e.muteAudio = void 0),
                                  (e.unmuteVideo = void 0),
                                  (e.muteVideo = void 0),
                                  M.p2ps.delete(e.p2pId),
                                  l.default.info('Unsubscribe stream success'),
                                  M.dispatchEvent((0, c.ClientEvent)({ type: 'stream-removed', uid: e.getId(), stream: e })),
                                  t && t()
                              },
                              n
                            ))
                        : (l.default.error('User is not in the session'), void P(n, f.default.INVALID_OPERATION))
                      : void P(n, f.default.NO_SUCH_REMOTE_STREAM))
            }),
            (M.setRemoteVideoStreamType = function(e, t) {
              if ((l.default.debug('Switching remote video stream ' + e.getId() + ' to ' + t), (void 0 === e ? 'undefined' : r(e)) !== 'object' || e === null))
                {return void l.default.error('Invalid remote stream')}
              if (M.state !== 2) return void l.default.error('User is not in the session')
              if (!e.local) {
                switch (t) {
                  case M.remoteVideoStreamTypes.REMOTE_VIDEO_STREAM_HIGH:
                  case M.remoteVideoStreamTypes.REMOTE_VIDEO_STREAM_LOW:
                  case M.remoteVideoStreamTypes.REMOTE_VIDEO_STREAM_MEDIUM:
                    break
                  default:
                    return
                }
                G(y(e.getId(), t), null, null)
              }
            }),
            (M.startLiveStreaming = function(e, t) {
              if ((M.liveStreams.set(e, t), l.default.debug('Start live streaming ' + e + ' transcodingEnabled ' + t), M.state !== 2)) return void l.default.error('User is not in the session')
              G(N(e, t), null, null)
            }),
            (M.stopLiveStreaming = function(e) {
              if ((l.default.debug('Stop live streaming ' + e), M.state !== 2)) return void l.default.error('User is not in the session')
              M.liveStreams.delete(e), G(D(e), null, null)
            }),
            (M.setLiveTranscoding = function(e) {
              if ((0, E.isLiveTranscodingValid)(e)) {
                if (((M.transcoding = e), l.default.debug('Set live transcoding ', e), M.state !== 2)) return void l.default.error('User is not in the session')
                G(L(e), null, null)
              }
            }),
            (M.closeGateway = function() {
              l.default.debug('close gateway'), (M.state = 0), M.socket.close(), B()
            })
          var k = function(e) {
              return 1e3 * Math.min(30, Math.pow(2, e) - 1)
            },
            V = function(e, t) {
              l.default.debug('Connect next gateway'), (M.state = 0), M.socket.close(), B(), (M.reconnectMode = 'tryNext'), j(e, t)
            },
            x = function(e, t) {
              l.default.debug('Reconnect gateway'), (M.state = 0), M.socket.close(), B(), (M.reconnectMode = 'retry'), j(e, t)
            },
            U = function() {
              l.default.debug('quit gateway'), (M.state = 0), M.socket.close(), B()
            },
            F = function() {
              l.default.debug('Reconnect gateway'), (M.state = 0), M.socket.close(), B(), (M.reconnectMode = 'recover'), j()
            },
            B = function() {
              for (var e in M.timers) M.timers.hasOwnProperty(e) && clearInterval(M.timers[e])
              for (var e in M.remoteStreams) {
              {
if (M.remoteStreams.hasOwnProperty(e)) {
                var t = M.remoteStreams[e],
                  n = (0, c.ClientEvent)({ type: 'stream-removed', uid: t.getId(), stream: t })
                M.dispatchEvent(n)
              } }
              M.p2ps.clear(), K(), J(), clearInterval(M.pingTimer)
            }
          M.rejoin = function() {
            M.state !== 0 && (M.state = 0), M.socket && (clearInterval(M.pingTimer), M.socket.close(), (M.socket = void 0)), j()
          }
          var j = function(e, t) {
              ;(e =
                e ||
                function(e) {
                  l.default.info('User ' + e + ' is re-joined to ' + M.joinInfo.cname),
                    M.dispatchEvent((0, c.ClientEvent)({ type: 'rejoin' })),
                    M.liveStreams &&
                      M.liveStreams.size &&
                      M.liveStreams.forEach(function(e, t) {
                        e && M.setLiveTranscoding(M.transcoding), M.startLiveStreaming(t, e)
                      })
                }),
                (t =
                  t ||
                  function(e) {
                    l.default.error('Re-join to channel failed [' + e + ']'), M.dispatchEvent((0, c.StreamEvent)({ type: 'error', reason: e }))
                  }),
                M.key ? (++M.rejoinAttempt, M.join(M.joinInfo, M.key, e, t)) : l.default.error('Connection recover failed [Invalid channel key]')
            },
            H = function(e) {
              M.socket = (0, s.default)(e, { sid: M.joinInfo.sid })
            },
            W = function(e, n, i) {
              ;(M.onConnect = n),
                void 0 !== M.socket
                  ? M.reconnectMode === 'retry'
                    ? (l.default.debug('Retry current gateway'), M.socket.reconnect())
                    : M.reconnectMode === 'tryNext'
                      ? (l.default.debug('Try next gateway'), M.socket.connectNext())
                      : M.reconnectMode === 'recover' &&
                        (l.default.debug('Recover gateway'),
                        l.default.debug('Try to reconnect choose server and get gateway list again '),
                        (0, v.getGatewayList)(M.joinInfo, function(e) {
                          M.socket.replaceHost(e.gateway_addr)
                        }))
                  : (H(e.gatewayAddr),
                    M.socket.on('onUplinkStats', function(e) {
                      M.localStreams[M.uid] && (M.localStreams[M.uid].uplinkStats = e)
                    }),
                    M.socket.on('connect', function() {
                      ;(M.attemps = 1), G(m(e), M.onConnect, i)
                    }),
                    M.socket.on('recover', function() {
                      ;(M.state = 0),
                        l.default.debug('Try to reconnect choose server and get gateway list again '),
                        (0, v.getGatewayList)(M.joinInfo, function(e) {
                          M.socket.replaceHost(e.gateway_addr)
                        })
                    }),
                    M.socket.on('disconnect', function(e) {
                      if (M.state !== 0) {
                        M.state = 0
                        var n = (0, c.StreamEvent)({ type: 'error', reason: f.default.SOCKET_DISCONNECTED })
                        if ((M.dispatchEvent(n), M.p2ps.size === 0 ? (M.reconnectMode = 'tryNext') : (M.reconnectMode = 'retry'), B(), t != 1)) {
                          var i = k(M.attemps)
                          l.default.error('Disconnect from server [' + e + '], attempt to recover [#' + M.attemps + '] after ' + i / 1e3 + ' seconds')
                          setTimeout(function() {
                            M.attemps++, j()
                          }, i)
                        }
                      }
                    }),
                    M.socket.on('onAddAudioStream', function(e) {
                      if ((l.default.info('Newly added audio stream with uid', e.id), M.remoteStreamsInChannel.has(e.id) || M.remoteStreamsInChannel.add(e.id), void 0 === M.remoteStreams[e.id])) {
                        var t = (0, o.Stream)({ streamID: e.id, local: !1, audio: e.audio, video: e.video, screen: e.screen, attributes: e.attributes })
                        M.remoteStreams[e.id] = t
                        var n = (0, c.StreamEvent)({ type: 'stream-added', stream: t })
                        M.dispatchEvent(n)
                      }
                    }),
                    M.socket.on('onAddVideoStream', function(e) {
                      if ((l.default.info('Newly added remote stream with uid', e.id), M.remoteStreamsInChannel.has(e.id) || M.remoteStreamsInChannel.add(e.id), void 0 === M.remoteStreams[e.id])) {
                        var t = (0, o.Stream)({ streamID: e.id, local: !1, audio: e.audio, video: e.video, screen: e.screen, attributes: e.attributes })
                        M.remoteStreams[e.id] = t
                        var n = (0, c.StreamEvent)({ type: 'stream-added', stream: t })
                        M.dispatchEvent(n)
                      } else if (void 0 !== M.remoteStreams[e.id].stream) {
                        ;(M.remoteStreams[e.id].video = !0), M.remoteStreams[e.id].enableVideo(), l.default.info('Stream changed: enable video ' + e.id)
                        var i = M.remoteStreams[e.id],
                          r = i.player.elementID
                        i.stop(), i.play(r)
                      } else {
                        var t = (0, o.Stream)({ streamID: e.id, local: !1, audio: !0, video: !0, screen: !1, attributes: e.attributes })
                        ;(M.remoteStreams[e.id] = t), l.default.info('Stream changed: modify video ' + e.id)
                      }
                    }),
                    M.socket.on('onRemoveStream', function(e) {
                      M.remoteStreamsInChannel.has(e.id) && M.remoteStreamsInChannel.delete(e.id)
                      var t = M.remoteStreams[e.id]
                      if (!t) return void console.log('ERROR stream ', e.id, ' not found onRemoveStream ', e)
                      delete M.remoteStreams[e.id]
                      var n = (0, c.StreamEvent)({ type: 'stream-removed', stream: t })
                      M.dispatchEvent(n), t.close(), void 0 !== t.pc && (t.pc.close(), (t.pc = void 0), M.p2ps.delete(t.p2pId))
                    }),
                    M.socket.on('onPublishStream', function(e) {
                      var t = M.localStreams[e.id],
                        n = (0, c.StreamEvent)({ type: 'stream-published', stream: t })
                      M.dispatchEvent(n)
                    }),
                    M.socket.on('mute_audio', function(e) {
                      l.default.info('rcv peer mute audio:' + e.peerid)
                      var t = (0, c.ClientEvent)({ type: 'mute-audio', uid: e.peerid })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('unmute_audio', function(e) {
                      l.default.info('rcv peer unmute audio:' + e.peerid)
                      var t = (0, c.ClientEvent)({ type: 'unmute-audio', uid: e.peerid })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('mute_video', function(e) {
                      l.default.info('rcv peer mute video:' + e.peerid)
                      var t = (0, c.ClientEvent)({ type: 'mute-video', uid: e.peerid })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('unmute_video', function(e) {
                      l.default.info('rcv peer unmute video:' + e.peerid)
                      var t = (0, c.ClientEvent)({ type: 'unmute-video', uid: e.peerid })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('user_banned', function(e) {
                      l.default.info('user banned uid:' + e.id + 'error:' + e.errcode)
                      var n = (0, c.ClientEvent)({ type: 'client-banned', uid: e.id, attr: e.errcode })
                      M.dispatchEvent(n), (t = !0), leave()
                    }),
                    M.socket.on('onP2PLost', function(e) {
                      if ((l.default.debug('p2plost:', e, 'p2ps:', M.p2ps), e.event === 'publish')) {
                        var t = M.localStreams[e.uid]
                        t && S.report.publish(M.joinInfo.sid, { lts: t.publishLTS, succ: !1, ec: 'DTLS failed' })
                      }
                      if (e.event === 'subscribe') {
                        var n = M.remoteStreams[e.uid]
                        n && S.report.subscribe(M.joinInfo.sid, { lts: n.subscribeLTS, succ: !1, peerid: e.uid + '', ec: 'DTLS failed' })
                      }
                      l.default.debug('p2plost:', e.p2pid)
                      var i = M.p2ps.get(e.p2pid)
                      i &&
                        (M.p2ps.delete(e.p2pid),
                        i.local
                          ? M.dispatchEvent((0, c.ClientEvent)({ type: 'pubP2PLost', stream: i }))
                          : M.remoteStreams[i.getId()] && M.dispatchEvent((0, c.ClientEvent)({ type: 'subP2PLost', stream: i })))
                    }),
                    (M._doWithAction = function(e, t, n) {
                      e === 'tryNext' ? V(t, n) : e === 'retry' ? x(t, n) : e === 'quit' ? U() : e === 'recover' && F()
                    }),
                    M.socket.on('notification', function(e) {
                      if ((l.default.debug('Receive notification: ', e), p.GatewayErrorCode[e.code] === 'ERR_JOIN_BY_MULTI_IP')) return M.dispatchEvent({ type: 'onMultiIP', arg: e })
                      e.detail ? M._doWithAction(R.default[p.GatewayErrorCode[e.code]]) : e.action && M._doWithAction(e.action)
                    }),
                    M.socket.on('onPeerLeave', function(e) {
                      var t = (0, c.ClientEvent)({ type: 'peer-leave', uid: e.id })
                      if (
                        (M.remoteStreamsInChannel.has(e.id) && M.remoteStreamsInChannel.delete(e.id),
                        M.remoteStreams.hasOwnProperty(e.id) && (t.stream = M.remoteStreams[e.id]),
                        M.dispatchEvent(t),
                        M.remoteStreams.hasOwnProperty(e.id))
                      ) {
                        l.default.info('closing stream on peer leave', e.id)
                        var n = M.remoteStreams[e.id]
                        n.close(), delete M.remoteStreams[e.id], void 0 !== n.pc && (n.pc.close(), (n.pc = void 0), M.p2ps.delete(n.p2pId))
                      }
                      M.timers.hasOwnProperty(e.id) && (clearInterval(M.timers[e.id]), delete M.timers[e.id]),
                        void 0 != M.audioLevel[e.id] && delete M.audioLevel[e.id],
                        void 0 != M.timer_counter[e.id] && delete M.timer_counter[e.id]
                    }),
                    M.socket.on('onUplinkStats', function(e) {}),
                    M.socket.on('liveStreamingStarted', function(e) {
                      var t = (0, c.LiveStreamingEvent)({ type: 'liveStreamingStarted', url: e.url })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('liveStreamingFailed', function(e) {
                      var t = (0, c.LiveStreamingEvent)({ type: 'liveStreamingFailed', url: e.url })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('liveStreamingStopped', function(e) {
                      var t = (0, c.LiveStreamingEvent)({ type: 'liveStreamingStopped', url: e.url })
                      M.dispatchEvent(t)
                    }),
                    M.socket.on('liveTranscodingUpdated', function(e) {
                      var t = (0, c.LiveStreamingEvent)({ type: 'liveTranscodingUpdated', reason: e.reason })
                      M.dispatchEvent(t)
                    }))
            },
            G = function(e, t, n) {
              if (void 0 === M.socket) return l.default.error('No socket available'), void P(n, f.default.INVALID_OPERATION)
              try {
                M.socket.emitSimpleMessage(e, function(e, i) {
                  e === 'success' ? typeof t === 'function' && t(i) : typeof n === 'function' && n(p.GatewayErrorCode[i] || i)
                })
              } catch (t) {
                l.default.error('Socket emit message failed' + JSON.stringify(e)), l.default.error(t), P(n, f.default.SOCKET_ERROR)
              }
            },
            z = function(e, t) {
              if (void 0 === M.socket) return void l.default.error('Error in sendSimpleSdp [socket not ready]')
              try {
                M.socket.emitSimpleMessage(e, function(e, n) {
                  t && t(e, n)
                })
              } catch (e) {
                l.default.error('Error in sendSimpleSdp [' + e + ']')
              }
            },
            J = function() {
              for (var e in M.localStreams) {
              {
if (void 0 !== M.localStreams[e]) {
                var t = M.localStreams[e]
                delete M.localStreams[e], void 0 !== t.pc && (t.pc.close(), (t.pc = void 0))
              }
              }
            },
            K = function() {
              M.remoteStreamsInChannel.clear()
              for (var e in M.remoteStreams) {
              { if (M.remoteStreams.hasOwnProperty(e)) {
                var t = M.remoteStreams[e]
                t.stop(), t.close(), delete M.remoteStreams[e], void 0 !== t.pc && (t.pc.close(), (t.pc = void 0))
              }
              }
            }
          return M
        }
      t.default = b
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = n(33),
        r = (function(e) {
          return e && e.__esModule ? e : { default: e }
        })(i),
        o = n(3),
        a = function(e, t) {
          var n = {}
          return (
            (n.connect = function() {
              ;(t.host = e),
                (n.signal = (0, r.default)(t)),
                (n.on = n.signal.on),
                (n.dispatchEvent = n.signal.dispatchEvent),
                n.signal.on('onopen', function(e) {
                  ;(n.signal.onEvent = function(e) {
                    n.dispatchEvent((0, o.MediaEvent)({ type: e.event, msg: e }))
                  }),
                    n.dispatchEvent((0, o.MediaEvent)({ type: 'connect', msg: e }))
                }),
                n.signal.on('onError', function(e) {
                  var t = e.msg
                  onError(t.code, 'error')
                })
            }),
            (n.disconnect = function() {
              n.signal.disconnect()
            }),
            (n.close = function() {
              n.signal.close()
            }),
            (n.getURL = function() {
              return n.signal.getURL()
            }),
            (n.reconnect = function() {
              n.signal.reconnect()
            }),
            (n.connectNext = function() {
              n.signal.connectNext()
            }),
            (n.replaceHost = function(e) {
              n.signal.replaceHost(e)
            }),
            (n.emitSimpleMessage = function(e, t) {
              n.signal.sendSignalCommand(e, t)
            }),
            n.connect(),
            n
          )
        }
      t.default = a
    },
    function(e, t, n) {
      'use strict'
      function i(e) {
        return e && e.__esModule ? e : { default: e }
      }
      Object.defineProperty(t, '__esModule', { value: !0 })
      var r = n(3),
        o = n(12),
        a = i(o),
        s = n(0),
        d = i(s),
        c = n(5),
        u = function(e) {
          var t = (0, r.EventDispatcher)(e)
          return (
            (t.needReconnect = !0),
            (t.isTimeout = !1),
            (t.isInit = !0),
            (t.hostIndex = 0),
            (t.requestID = 0),
            e.host instanceof Array ? (t.host = e.host) : (t.host = [e.host]),
            (t.getURL = function() {
              return t.connection.url
            }),
            (t.reconnect = function() {
              ;(t.isInit = !0), t.creatConnection()
            }),
            (t.connectNext = function() {
              ;(t.isInit = !0),
                ++t.hostIndex,
                d.default.debug('Gateway length:' + t.host.length + ' current index:' + t.hostIndex),
                t.hostIndex >= t.host.length ? t.dispatchEvent((0, r.MediaEvent)({ type: 'recover' })) : t.creatConnection()
            }),
            (t.replaceHost = function(e) {
              ;(t.host = e || t.host), (t.hostIndex = 0), t.creatConnection()
            }),
            (t.creatConnection = function() {
              d.default.debug('start connect:' + t.host[t.hostIndex]),
                (t.lts = new Date().getTime()),
                (t.connection = new WebSocket('wss://' + t.host[t.hostIndex])),
                (t.connection.onopen = function(e) {
                  d.default.debug('websockect opened'),
                    (t.needReconnect = !0),
                    (t.isTimeout = !1),
                    (t.isInit = !1),
                    clearTimeout(t.timeoutCheck),
                    t.dispatchEvent((0, r.MediaEvent)({ type: 'onopen', event: e, socket: t }))
                }),
                (t.connection.onmessage = function(e) {
                  var n = JSON.parse(e.data)
                  n.hasOwnProperty('_id')
                    ? t.dispatchEvent((0, r.MediaEvent)({ type: n._id, msg: n }))
                    : n.hasOwnProperty('_type') && t.dispatchSocketEvent((0, r.MediaEvent)({ type: n._type, msg: n.message }))
                }),
                (t.connection.onclose = function(n) {
                  t.needReconnect
                    ? t.isTimeout || t.isInit
                      ? (d.default.debug('websockect connect timeout'), c.report.joinGateway(e.sid, { lts: t.lts, succ: !1, ec: 'timeout', addr: t.connection.url }), t.connectNext())
                      : t.dispatchEvent((0, r.MediaEvent)({ type: 'disconnect', event: n }))
                    : (d.default.debug('websockect closeed'),
                      (0, a.default)(e.onFailure, n),
                      clearTimeout(t.timeoutCheck),
                      t.dispatchEvent((0, r.MediaEvent)({ type: 'close', event: n })),
                      (t.connection.onopen = void 0),
                      (t.connection.onclose = void 0),
                      (t.connection.onerror = void 0),
                      (t.connection.onmessage = void 0),
                      (t.connection = void 0))
                }),
                (t.connection.onerror = function(e) {})
              setTimeout(function() {
                t.connection && t.connection.readyState != WebSocket.OPEN && ((t.isTimeout = !0), t.connection.close())
              }, 5e3)
            }),
            t.creatConnection(),
            (t.sendMessage = function(e, n) {
              t.connection && t.connection.readyState == WebSocket.OPEN ? t.connection.send(JSON.stringify(e)) : n({ error: 'Gateway not connected' })
            }),
            (t.disconnect = function() {
              ;(t.needReconnect = !0), t.connection.close()
            }),
            (t.close = function() {
              ;(t.needReconnect = !1), (t.connection.onclose = void 0), t.connection.close()
            }),
            (t.sendSignalCommand = function(e, n) {
              ;(e._id = '_request_' + t.requestID),
                (t.requestID += 1),
                e._type !== 'publish_stats' &&
                  e._type !== 'subscribe_stats' &&
                  e._type !== 'publish_stats_low' &&
                  t.on(e._id, function(i) {
                    i.msg && n && n(i.msg._result, i.msg.message), delete t.dispatcher.eventListeners[e._id]
                  }),
                t.sendMessage(e, function(e) {
                  ;(e.reason = 'NOT_CONNECTED'), n && n(e.reason, e)
                })
            }),
            t
          )
        }
      t.default = u
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 })
      var i = {
        ERR_NO_VOCS_AVAILABLE: 'tryNext',
        ERR_NO_VOS_AVAILABLE: 'tryNext',
        ERR_JOIN_CHANNEL_TIMEOUT: 'tryNext',
        WARN_REPEAT_JOIN: 'quit',
        ERR_JOIN_BY_MULTI_IP: 'recover',
        WARN_LOOKUP_CHANNEL_TIMEOUT: 'tryNext',
        WARN_OPEN_CHANNEL_TIMEOUT: 'tryNext',
        ERR_VOM_SERVICE_UNAVAILABLE: 'tryNext',
        ERR_TOO_MANY_USERS: 'tryNext',
        ERR_MASTER_VOCS_UNAVAILABLE: 'tryNext',
        ERR_INTERNAL_ERROR: 'tryNext',
        notification_test_recover: 'recover',
        notification_test_tryNext: 'tryNext',
        notification_test_retry: 'retry'
      }
      t.default = i
    },
    function(e, t, n) {
      'use strict'
      Object.defineProperty(t, '__esModule', { value: !0 }), (t.simMap = void 0)
      var i = n(2),
        r = function(e) {
          var t
          switch (e) {
            case '120p':
            case '120p_1':
              t = ['120p_1', '120p_1', '120p_1']
              break
            case '120p_3':
              t = ['120p_3', '120p_3', '120p_3']
              break
            case '180p':
            case '180p_1':
              t = ['90p_1', '90p_1', '180p_1']
              break
            case '180p_3':
              t = ['120p_3', '120p_3', '180p_3']
              break
            case '180p_4':
              t = ['120p_1', '120p_1', '180p_4']
              break
            case '240p':
            case '240p_1':
              t = ['120p_1', '120p_1', '240p_1']
              break
            case '240p_3':
              t = ['120p_3', '120p_3', '240p_3']
              break
            case '240p_4':
              t = ['120p_4', '120p_4', '240p_4']
              break
            case '360p':
            case '360p_1':
            case '360p_4':
            case '360p_9':
            case '360p_10':
            case '360p_11':
              t = ['90p_1', '90p_1', '360p_1']
              break
            case '360p_3':
            case '360p_6':
              t = ['120p_3', '120p_3', '360p_3']
              break
            case '360p_7':
            case '360p_8':
              t = ['120p_1', '120p_1', '360p_7']
              break
            case '480p':
            case '480p_1':
            case '480p_2':
            case '480p_4':
            case '480p_10':
              t = ['120p_1', '120p_1', '480p_1']
              break
            case '480p_3':
            case '480p_6':
              t = ['120p_3', '120p_3', '480p_3']
              break
            case '480p_8':
            case '480p_9':
              t = ['120p_4', '120p_4', '480p_8']
              break
            case '720p':
            case '720p_1':
            case '720p_2':
            case '720p_3':
              t = ['90p_1', '90p_1', '720p_1']
              break
            case '720p_5':
            case '720p_6':
              t = ['120p_1', '120p_1', '720p_5']
              break
            case '1080p':
            case '1080p_1':
            case '1080p_2':
            case '1080p_3':
            case '1080p_5':
              t = ['90p_1', '90p_1', '1080p_1']
              break
            case '1440p':
            case '1440p_1':
            case '1440p_2':
              t = ['90p_1', '90p_1', '1440p_1']
              break
            case '4k':
            case '4k_1':
            case '4k_3':
              t = ['90p_1', '90p_1', '4k_1']
              break
            default:
              t = ['120p_1', '120p_1', '360p_7']
          }
          return (0, i.isOpera)() ? [e, 15, 50] : (0, i.isFireFox)() ? [t[1], 15, 100] : (0, i.isSafari)() ? [t[2], 15, 50] : [t[0], 15, 50]
        }
      t.simMap = r
    }
  ]).default
})
