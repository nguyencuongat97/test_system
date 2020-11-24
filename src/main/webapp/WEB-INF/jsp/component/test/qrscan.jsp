<video autoplay></video>
<button id="reset">Reset</button>
<button id="stop" >Stop</button>

<script src="/assets/js/plugins/qrscan/qcode-decoder.min.js"></script>
<script type="text/javascript">

(function () {
'use strict';

var qr = QCodeDecoder();

if (!(qr.isCanvasSupported() && qr.hasGetUserMedia())) {
  alert('Your browser doesn\'t match the required specs.');
  throw new Error('Canvas and getUserMedia are required');
}

var video = document.querySelector('video');
var reset = document.querySelector('#reset');
var stop = document.querySelector('#stop');


function resultHandler (err, result) {
  alert(err.message);
  alert(result);
  if (err)
    return alert(err.message);

  alert(result);
}

// prepare a canvas element that will receive
// the image to decode, sets the callback for
// the result and then prepares the
// videoElement to send its source to the
// decoder.

qr.decodeFromCamera(video, resultHandler);


// attach some event handlers to reset and
// stop whenever we want.

reset.onclick = function () {
  qr.decodeFromCamera(video, resultHandler);
};

stop.onclick = function () {
  qr.stop();
};

})();

</script>