<img src="http://lorempixel.com/600/300/abstract/5" width="600" height="300" />

<div class="content">

    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent bibendum, lorem vel tincidunt imperdiet, nibh elit laoreet felis, a bibendum nisl tortor non orci. Donec pretium fermentum felis, quis aliquet est rutrum ut. Integer quis massa ut lacus
        viverra pharetra in eu lacus. Aliquam tempus odio adipiscing diam pellentesque rhoncus. Curabitur a bibendum est. Mauris vehicula cursus risus id luctus. Curabitur accumsan venenatis nibh, non egestas ipsum vulputate ac. Vivamus consectetur dolor
        sit amet enim aliquet eu scelerisque ipsum hendrerit. Donec lobortis suscipit vestibulum. Nullam luctus pellentesque risus in ullamcorper. Nam neque nunc, mattis vitae ornare ut, feugiat a erat. Ut tempus iaculis augue vel pellentesque.</p>

    <p>Vestibulum nunc massa, gravida quis porta nec, feugiat id metus. Nunc ac arcu dolor, quis vestibulum leo. Cras viverra mollis ipsum, non rhoncus lectus aliquam et. Morbi faucibus purus sit amet lacus aliquet elementum. Donec sit amet posuere enim.
        Cras in eros id tortor fringilla ultricies. Mauris faucibus ullamcorper velit, pulvinar varius odio eleifend eu. Quisque id odio metus. Morbi adipiscing ultricies posuere. Pellentesque elementum porttitor eros in molestie. Maecenas ut leo quis
        nisi tempor tincidunt.</p>

    <img src="http://lorempixel.com/600/300/abstract/3" width="600" height="300" />

    <p>Donec nunc ligula, vulputate quis mollis eu, interdum quis libero. Donec nulla ante, facilisis sit amet vulputate at, tincidunt a velit. Maecenas vestibulum, nulla sed tincidunt viverra, lorem turpis aliquam urna, ut pretium orci purus consequat augue.
        Etiam a enim orci, vitae pulvinar odio. In elit urna, tincidunt a pellentesque et, scelerisque at nibh. Sed nec est sapien. Aliquam ullamcorper eros eu quam ultrices vel faucibus eros interdum. Etiam mattis eleifend sapien, eu iaculis massa feugiat
        sed. Aliquam erat volutpat. Vivamus facilisis ultricies eros, a pretium purus mollis id. Sed dapibus elit ut neque rutrum dignissim. Nulla eros nisl, venenatis quis rhoncus sit amet, molestie nec nisl. Pellentesque vel neque sapien, et sagittis
        nulla.</p>

    <p>Aliquam eu iaculis mauris. Etiam arcu lectus, imperdiet sit amet volutpat vitae, commodo sed nibh. Fusce faucibus tempor cursus. Donec ut ligula tortor. Maecenas malesuada, diam vitae pharetra dictum, erat ante iaculis risus, sed sollicitudin nisl
        nisl vel metus. Nulla libero augue, convallis nec luctus id, iaculis nec urna. Cras vitae mi ut augue ultricies tempus.</p>

    <p>Vestibulum euismod vehicula sollicitudin. Duis nibh justo, rhoncus ac ullamcorper nec, rutrum sit amet leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum varius posuere nisi sed aliquet. Etiam
        dolor nisi, placerat vel congue tempus, posuere sed erat. Phasellus turpis eros, molestie sed consequat in, pretium at erat. In sed faucibus metus. Vestibulum fermentum libero nec eros fermentum dapibus. Duis nec libero eros. Pellentesque magna
        ligula, sagittis dictum fringilla at, posuere porttitor sem. Donec aliquam, ipsum quis pulvinar dapibus, augue sem viverra sapien, nec tempus odio nulla ac metus. Nunc ut augue mi, nec consequat urna.</p>

    <img src="http://lorempixel.com/600/300/abstract/2" width="600" height="300" />

    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent bibendum, lorem vel tincidunt imperdiet, nibh elit laoreet felis, a bibendum nisl tortor non orci. Donec pretium fermentum felis, quis aliquet est rutrum ut. Integer quis massa ut lacus
        viverra pharetra in eu lacus. Aliquam tempus odio adipiscing diam pellentesque rhoncus. Curabitur a bibendum est. Mauris vehicula cursus risus id luctus. Curabitur accumsan venenatis nibh, non egestas ipsum vulputate ac. Vivamus consectetur dolor
        sit amet enim aliquet eu scelerisque ipsum hendrerit. Donec lobortis suscipit vestibulum. Nullam luctus pellentesque risus in ullamcorper. Nam neque nunc, mattis vitae ornare ut, feugiat a erat. Ut tempus iaculis augue vel pellentesque.</p>

    <p>Vestibulum nunc massa, gravida quis porta nec, feugiat id metus. Nunc ac arcu dolor, quis vestibulum leo. Cras viverra mollis ipsum, non rhoncus lectus aliquam et. Morbi faucibus purus sit amet lacus aliquet elementum. Donec sit amet posuere enim.
        Cras in eros id tortor fringilla ultricies. Mauris faucibus ullamcorper velit, pulvinar varius odio eleifend eu. Quisque id odio metus. Morbi adipiscing ultricies posuere. Pellentesque elementum porttitor eros in molestie. Maecenas ut leo quis
        nisi tempor tincidunt.</p>

</div>

<style>
    .slider {
        position: fixed;
        top: 10px;
        left: 10px;
        min-width: 20px;
        max-width: 60px;
        box-shadow: 0 2px 13px rgba(0, 0, 0, 0.3);
        cursor: -webkit-grab;
        cursor: grab;
        opacity: 0.5;
        transition: opacity 800ms ease-in-out 200ms;
        z-index: 999;
    }
    
    .slider:hover {
        opacity: 1;
        transition-delay: 0ms;
    }
    
    .slider__size {
        position: relative;
        z-index: 3;
    }
    
    .slider__controller {
        width: 100%;
        padding-top: 100%;
        position: absolute;
        top: 0;
        left: 0;
        -webkit-transform-origin: 0 0;
        transform-origin: 0 0;
        margin: -3px;
        border-radius: 10px;
        border: solid 3px black;
    }
    
    .slider__content {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: -1;
        -webkit-transform-origin: 0 0;
        transform-origin: 0 0;
    }
    /* Presentational Styles */
    
    html {
        font-size: 52.6%;
    }
    
    body {
        font: 1.6rem/1.6 sans-serif;
        background: white;
        background-repeat: repeat-x;
        background-size: cover;
        max-width: 100%;
    }
    
    .content {
        max-width: 26em;
        padding: 2em;
        margin: auto;
    }
    
    p {
        margin: 0 0 2em;
    }
    
    img {
        max-width: 100%;
        height: auto;
        margin-bottom: 2em;
    }
    
    img {
        display: block;
        position: relative;
        left: -50vw;
        margin-left: 50%;
        width: 100vw;
        max-width: none;
    }
</style>

<script>
    console.clear();

    (function() {

        var doc = document,
            docEl = document.documentElement,
            body = doc.body,
            win = window;

        var slider = doc.createElement('div'),
            sliderSize = doc.createElement('div'),
            controller = doc.createElement('div'),
            sliderContent = doc.createElement('iframe'),
            scale = 0.1,
            realScale = scale;

        slider.className = 'slider';
        sliderSize.className = 'slider__size';
        controller.className = 'slider__controller';

        sliderContent.className += ' slider__content';
        sliderContent.style.transformOrigin = '0 0';

        slider.appendChild(sliderSize);
        slider.appendChild(controller);
        slider.appendChild(sliderContent);
        body.appendChild(slider);

        var html = doc.documentElement.outerHTML
            .replace(/<script([\s\S]*?)>([\s\S]*?)<\/script>/gim, ''); // Remove all scripts

        var script = '<script>var slider=document.querySelector(".slider"); slider.parentNode.removeChild(slider);<' + '/script>';
        /*function checkChildren(node){ if ( node.nodeValue ) { node.nodeValue = node.nodeValue.replace(/[a-z0-9]/gi,"\u2592"); return; } for ( var i = 0; i < node.childNodes.length; i++) { checkChildren(node.childNodes[i]); } } /* checkChildren(body); */

        html = html.replace('</body>', script + '</body>');

        // Must be appended to body to work.
        var iframeDoc = sliderContent.contentWindow.document;

        iframeDoc.open();
        iframeDoc.write(html);
        iframeDoc.close();


        ////////////////////////////////////////

        function getDimensions() {
            var bodyWidth = body.clientWidth,
                bodyRatio = body.clientHeight / bodyWidth,
                winRatio = win.innerHeight / win.innerWidth;

            slider.style.width = (scale * 100) + '%';

            // Calculate the actual scale in case a max-width/min-width is set.
            realScale = slider.clientWidth / bodyWidth;

            sliderSize.style.paddingTop = (bodyRatio * 100) + '%';
            controller.style.paddingTop = (winRatio * 100) + '%';

            sliderContent.style.transform = 'scale(' + realScale + ')';
            sliderContent.style.width = (100 / realScale) + '%';
            sliderContent.style.height = (100 / realScale) + '%';
        }

        getDimensions();
        win.addEventListener('resize', getDimensions);
        win.addEventListener('load', getDimensions);

        ////////////////////////////////////////
        // Track Scroll

        function trackScroll() {
            controller.style.transform = 'translate(' +
                ((win.pageXOffset * realScale)) + 'px, ' +
                ((win.pageYOffset * realScale)) + 'px)';
        }

        win.addEventListener('scroll', trackScroll);
        //body.addEventListener('scroll', trackScroll);


        ////////////////////////////////////////
        // Click & Drag Events

        var mouseY = 0,
            mouseX = 0,
            mouseDown = false;

        function pointerDown(e) {
            e.preventDefault();
            mouseDown = true;
            mouseX = e.touches ? e.touches[0].clientX : e.clientX;
            mouseY = e.touches ? e.touches[0].clientY : e.clientY;

            var offsetX = ((mouseX - slider.offsetLeft) - (controller.clientWidth / 2)) / realScale;
            var offsetY = ((mouseY - slider.offsetTop) - (controller.clientHeight / 2)) / realScale;

            win.scrollTo(offsetX, offsetY);
        }
        slider.addEventListener('mousedown', pointerDown);
        slider.addEventListener('touchdown', pointerDown);

        function pointerMove(e) {
            if (mouseDown) {
                e.preventDefault();

                var x = e.touches ? e.touches[0].clientX : e.clientX,
                    y = e.touches ? e.touches[0].clientY : e.clientY;

                win.scrollBy((x - mouseX) / realScale, ((y - mouseY) / realScale));
                mouseX = x;
                mouseY = y;
            }
        }
        win.addEventListener('mousemove', pointerMove);
        win.addEventListener('touchmove', pointerMove);

        function pointerReset(e) {
            mouseDown = false;
        }
        win.addEventListener('mouseup', pointerReset);
        win.addEventListener('touchend', pointerReset);

        function pointerLeave(e) {
            if (e.target === body) {
                mouseDown = false;
            }
        }
        body.addEventListener('mouseleave', pointerLeave);
        body.addEventListener('touchleave', pointerLeave);

    }())
</script>