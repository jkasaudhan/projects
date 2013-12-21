navigator.getUserMedia || (navigator.getUserMedia = navigator.mozGetUserMedia || navigator.webkitGetUserMedia || navigator.msGetUserMedia);


if(navigator.getUserMedia){
	
	navigator.getUserMedia({video:true,audio:true},onSuccess,onError);

}else{
	alert("getUserMedia is not supported in this browser");
}

function onSuccess(stream){
	

        var video = document.getElementById('webcam');
        videoSource = window.URL.createObjectURL(stream);
        video.src = videoSource;
	video.autoplay = true;
	var pc = new PeerConnection();

}

function onError(){
	alert("browser does not support user mendia");
}

var serverConfig = "STUN 173.194.70.126:19302";
   var createId = function () { return Math.random().toString(16).substr(2); };
   var sessionId = location.hash = location.hash.substr(1) || createId();
   var userId = createId();
   var peers = {};
   var localStream;
 
   function sendSignalingMessage(peerUserId, message, callback) {
      var xhr = new XMLHttpRequest();
      xhr.open("POST", "/ctos/" + sessionId + "/" + userId + "/" + peerUserId);
      xhr.setRequestHeader("Content-Type", "text/plain");
      xhr.send(message);
      xhr.onreadystatechange = function () {
         if (xhr.readyState == 4 && callback instanceof Function)
            callback(xhr.status == 204);
      };
   };
 
   window.onload = function () {
      setTimeout(start, 500);
   };
 
   function start() {
      var PeerConn =  self.webkitDeprecatedPeerConnection || self.webkitPeerConnection;
      document.body.innerHTML = "<p>Waiting for others to join.. @" + sessionId +
            "<p>Send this link to the other participants: " + location.href;
 
      var options = {
         "audio": "yes please",
         "video": "ok then",
         "toString": function () {
            return "audio, video user";
         }
      };
      navigator.webkitGetUserMedia(options, function (stream) {
         localStream = stream;
         for (var pname in peers)
            if (peers[pname].conn)
               peers[pname].conn.addStream(localStream);
      });
 
      var es = new EventSource("/stoc/" + sessionId + "/" + userId);
      es.addEventListener("join", function (evt) {
         var peerUserId = evt.data;
         var peer = peers[peerUserId] = {};
         peer.sendQueue = [];
 
         function createPeerConnection() {
            peer.conn = new PeerConn(serverConfig, function (message) {
               function sendNextMessage(msg) {
                  sendSignalingMessage(peerUserId, msg, function () {
                     if (peer.sendQueue.length > 0)
                        sendNextMessage(peer.sendQueue.splice(0, 1));
                  });
               }
               if (!peer.sendQueue.length)
                  sendNextMessage(message);
               else
                  peer.sendQueue.append(message);
            });
 
            if (localStream)
               peer.conn.addStream(localStream);
 
            peer.conn.onaddstream = function (evt) {
               peer.video = document.createElement("video");
               peer.video.autoplay = peer.video.controls = true;
               peer.video.src = window.URL.createObjectURL(evt.stream);
               document.body.appendChild(peer.video);
            };
 
            peer.conn.onclose = function () {
               delete document.body.removeChild(peer.video);
               delete peers[peerUserId];
            };
         };
 
         if (peerUserId > userId)
            createPeerConnection();
 
         es.addEventListener("user-" + peerUserId, function (evt) {
            if (!peer.conn)
               createPeerConnection();
            peer.conn.processSignalingMessage(evt.data);
         }, false);
 
      }, false);
 
      es.addEventListener("leave", function (evt) {
         var peer = peers[evt.data];
         if (peer && peer.conn && peer.conn.readyState != 3)
            peer.conn.close();
      }, false);
   };
 
   window.onhashchange = function (evt) {
      window.location.reload();
   };