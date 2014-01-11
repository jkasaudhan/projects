navigator.getMedia = (navigator.getUserMedia ||
                       navigator.webkitGetUserMedia ||
                       navigator.mozGetUserMedia ||
                       navigator.msGetUserMedia);

navigator.getMedia(
// constraints
   {
   video: true,
   audio: true
},
// successCallback
onSuccess,
// errorCallback
onError
);

   function onSuccess(localMediaStream) {
      
       var video = document.querySelector('video');
       video.src = window.URL.createObjectURL(localMediaStream);
       video.onloadedmetadata = function (e) {
           // Do something with the video here.
       };
       //get audio stream of machine
       var audioContext = new AudioContext();
       var mediaStreamSource = audioContext.createMediaStreamSource(localMediaStream);
       mediaStreamSource.connect(audioContext.destination);
       //capture video image and draws in canvas.
       var photoButton = document.getElementById('takePhoto');
       photoButton.addEventListener('click', takePhoto, false);

       //implementing video conferencing

       //var peerConnection = new mozRTCPeerConnection({"TURN localhost:80":onSignal});
       var peerConnection = new mozRTCPeerConnection({ "iceServers": [{ "url": "stun:stun.services.mozilla.com"}] });

       peerConnection.onicecandidate = onicecandidate;
       peerConnection.onaddstream = onaddstream;
       peerConnection.addstream = localMediaStream;

       //create offer
       peerConnection.createOffer(function (sessionDescription) {
           alert("create offer");
           peerConnection.setLocalDescription(sessionDescription);
           alert(sessionDescription);
           POST-Offer-SDP-For-Other-Peer(sessionDescription.sdp, sessionDescription.type);
           setRemoteDescription(sessionDescription);
       },
       function () {
           alert("error creating offer.");

       }, { 'mandatory': { 'OfferToReceiveAudio': true, 'OfferToReceiveVideo': true} });

       //ser remore description
       function setRemoteDescription(sessionDescription) {
           peerConnection.setRemoteDescription(new mozRTCSessionDescription(sessionDescription.sdp),
                                            function () {
                                                alert("setting remote description.");
                                                // if we received an offer, we need to answer
                                                if (peerConnection.remoteDescription.type == "offer")
                                                    peerConnection.createAnswer(localDescCreated, logError);
                                            },
                                            function () { alert("error setting remote descriiption."); });
                                        }
        function onicecandidate(event) {
            alert("onicecandidate");
            if (!peerConnection || !event || !event.candidate) return;
            var candidate = event.candidate;
            alert("candidate:" + candidate);
            POST-ICE-to-other-Peer(candidate.candidate, candidate.sdpMLineIndex);

        }
           peerConnection.createAnswer(function (sessionDescription) {
           peerConnection.setLocalDescription(sessionDescription);

           POST-answer-SDP-back-to-Offerer(sessionDescription.sdp, sessionDescription.type);
           setRemoteAnswer(sessionDescription);
       },
        function () {
            alert("error creating answer");
        },
        { 'mandatory': { 'OfferToReceiveAudio': true, 'OfferToReceiveVideo': true} });

        function setRemoteAnswer(sessionDescription) {
            peerConnection.setRemoteDescription(new mozRTCSessionDescription(sessionDescription));
        }
        
       peerConnection.addIceCandidate(new mozRTCIceCandidate({
           sdpMLineIndex: candidate.sdpMLineIndex,
           candidate: candidate.candidate
       }));
   }

   function onError(err) {
       alert("error");
       console.log("The following error occured: " + err);
   }

   function takePhoto() {
       alert("taking photos");
       var photo = document.getElementById('photo');
       var context = photo.getContext("2d");
       var video = document.querySelector('video');
       photo.width = video.clientWidth;
       photo.height = video.clientHeight;
       context.drawImage(video,0,0,photo.width,photo.height);
   }

   function onSignal(message) {
       alert("onSignal.");
       sendSignalling(message);
   }

   function onRemoteStreamAdded(event) {
       alert("onRemoteStreamAdded.");
       var remote = document.getElementById('remoteVideo');
       remote.src = window.URL.createObjectURL(event.stream);
   }



   function onaddstream(event) {
       alert("onaddstream");
       if (!event) return;
       var remote_video = document.getElementById('remoteVideo');
       remote_video.src = window.URL.createObjectURL(event.stream);
       // remote_video.mozSrcObject  = event.stream;
       waitUntilRemoteStreamStartsFlowing();
   }

   function waitUntilRemoteStreamStartsFlowing() {
       if (!(remote_video.readyState <= HTMLMediaElement.HAVE_CURRENT_DATA
        || remote_video.paused || remote_video.currentTime <= 0)) {
           // remote stream started flowing!
       }
       else setTimeout(waitUntilRemoteStreamStartsFlowing, 50);
   }

   function addICECandidate(candidate) {
   
    }