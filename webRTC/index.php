
<!DOCTYPE html>
<html>
<head>
<title>WebRTC Test App</title>

<meta http-equiv="X-UA-Compatible" content="chrome=1"/>


<link rel="stylesheet" href="css/main.css" />

<script type="text/javascript" src="js/main.js"></script>

</head>
<body>

<div id="container" ondblclick="enterFullScreen()">
  <div id="card">
    <div id="local">
      <video id="localVideo" autoplay="autoplay" muted="true"/>
    </div>
    <div id="remote">
      <video id="remoteVideo" autoplay="autoplay">
      </video>
      <div id="mini">
        <video id="miniVideo" autoplay="autoplay" muted="true"/>
      </div>

    </div>

</div>
<div id="controls">
		<canvas id="photo"></canvas>
		<input type="button" id="takePhoto" value="Capture"/>
	  </div>
  </div>
</body>
<footer id="status">

</footer>
<div id="infoDiv"></div>
</html>