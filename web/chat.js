var wsocket;
var serviceLocation = 'ws://localhost:8080/team3_setgame/wssocket/';
var $nickName;
var $message;
var $chatdetail;
var room = '';
$nickName = "dong";
function onMessageReceived(evt) {
    //var msg = JSON.parse(evt.data); // native API
    alert(evt.data);
}
function sendMessage() {
    var msg = '{"message":"' + $message.val() + '", "sender":"'
		+ $nickName + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
 
function connectToChatserver() {
    room = "abc";
    wsocket = new WebSocket(serviceLocation + room);
    wsocket.onmessage = onMessageReceived;
    
}
 
function leaveRoom() {
    wsocket.close();
    $chatdetail.empty();
    $('.chat-wrapper').hide();
    $('.chat-signin').show();
    $nickName.focus();
}

$(document).ready(function () {
   
    connectToChatserver();
    setTimeout(
        function () {
            wsocket.send('hello');
        }, 500);
    
});