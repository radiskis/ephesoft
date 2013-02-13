function doOverlay(x0, x1, y0, y1, zoomFactor) {
	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.className = 'square';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}
function doOverlayById(overlayDivId, x0, x1, y0, y1, zoomFactor) {
	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.setAttribute('id', overlayDivId);
	d.className = 'square';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForKey(x0, x1, y0, y1, zoomFactor) {

	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydivkey');
	d.className = 'secondSquare';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForValue(x0, x1, y0, y1, zoomFactor) {

	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydivvalue');
	d.className = 'thirdSquare';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function doOverlayForRow(x0, x1, y0, y1, zoomFactor) {

	posx = x1;
	posy = y1;
	initx = x0;
	inity = y0;
	d = document.createElement('div');
	d.setAttribute('name', 'overlaydiv');
	d.className = 'squareRow';
	d.style.left = initx + 'px';
	d.style.top = inity + 'px';
	var width = (posx - initx) * zoomFactor;
	var height = (posy - inity) * zoomFactor;
	d.style.width = Math.abs(width) + 'px';
	d.style.height = Math.abs(height) + 'px';
	document.getElementsByTagName('body')[0].appendChild(d);
}

function removeOverlayById(overlayDivId) {
	var di = document.getElementById(overlayDivId);
	if (di) {
		di.parentNode.removeChild(di);
	}
}

function removeOverlay() {
	/*
	 * var di = document.getElementsByName('overlaydiv'); i = 0; if (di) { if
	 * (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 */

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydiv') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydivkey') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	var inputs = document.getElementsByTagName('div');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs.item(i).getAttribute('name') == 'overlaydivvalue') {
			document.getElementsByTagName('body')[0].removeChild(inputs[i]);
		}

	}

	/*
	 * var di = document.getElementsByName('overlaydivkey'); i = 0; if (di) { if
	 * (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 * var di = document.getElementsByName('overlaydivvalue'); i = 0; if (di) {
	 * if (di.length > 0) { while (i < di.length) {
	 * document.getElementsByTagName('body')[0].removeChild(di[i]); i++; } } }
	 */
}

function getViewPortHeight() {
	viewportheight = window.innerHeight;
	return viewportheight;
}

function getViewPortHeightForIE() {
	viewportheight = document.documentElement.clientHeight;
	return viewportheight;
}

function getViewPortWidthForIE() {
	viewportwidth = document.documentElement.clientWidth;
	return viewportwidth;
}

function loginSubmit() {
	document.getElementById("loginForm").submit();
}

function getViewPortWidth() {
	return window.innerWidth || document.documentElement.clientWidth
			|| document.body.clientWidth;
}
window.onbeforeunload = function() {
	onCloseWindow();
	return;
}

var onmessage = function(e) {
	var document = window.top.document;
	var element;
	if (e.data == "Save") {
		element = document.getElementById("okButtonElement");
	} else if (e.data == "Cancel") {
		element = document.getElementById("closeButtonElement");
	}
	if (element != null) {
		element.click();
	}
}

if (typeof window.addEventListener != 'undefined') {
	window.addEventListener('message', onmessage, false);
} else if (typeof window.attachEvent != 'undefined') {
	window.attachEvent('onmessage', onmessage);
}

function performScannerAction(action) {
	window.document.webScannerApplet.init(action);
}

function setScannerProperties(keys, values, delimiter) {
	window.document.webScannerApplet.setScannerProperties(keys, values,
			delimiter);
}

function setImageDeleted() {
	window.document.webScannerApplet.onImageDelete();
}
