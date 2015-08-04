var wsocket;
var serviceLocation = 'ws://' + '172.23.204.102:8080' + '/testchat-war/chat2/';
var $nickName;
var $message;
var $chatdetail;
var room = '';
$nickName = "dong";
function onMessageReceived(evt) {
    var msg = JSON.parse(evt.data); // native API
    if(msg.sender.toString()==$nickName)
    {
        var $messageLine=$('<div class="message right"><img src="img/1_copy.jpg"/>\n\
                        <div class="bubble">'+msg.message+
                        '<div class="corner"></div><span>'+
                        "now"+'</span></div></div>');
    }
    else
    {
        var $messageLine=$('<div class="message"><img src="img/1_copy.jpg"/>\n\
                            <div class="bubble">'+msg.message+
                            '<div class="corner"></div><span>'+
                            "now"+'</span></div></div>');
    }
    $chatdetail.append($messageLine);
    document.getElementById("msg_end").scrollIntoView();
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
function showchat(){
    var preloadbg = document.createElement('img');
    var clone = $(this).find('img').eq(0).clone();
    setTimeout(function () {
	$('#profile p').addClass('animate');
	$('#profile').addClass('animate');
    }, 100);
    setTimeout(function () {
	$('#chat-messages').addClass('animate');
	$('.cx, .cy').addClass('s1');
        setTimeout(function () {
	    $('.cx, .cy').addClass('s2');
	}, 100);
	    setTimeout(function () {
	        $('.cx, .cy').addClass('s3');
	    }, 200);
    }, 150);
    $('.floatingImg').animate({
                                'width': '68px',
                                'left': '108px',
                                'top': '20px'
    }, 200);
    $('.message').not('.right').find('img').attr('src', $(clone).attr('src'));
    $('#chatview').fadeIn();
}

$(document).ready(function () {
    showchat();
    $('#sendmessage input').focus(function () {
	if ($(this).val() === "Send message...") {
	    $(this).val('');
	}
    });
    
    $message = $('#message');
    $chatdetail = $('#chatdetail');
    connectToChatserver();
    //$message.focus();
    /*
    $('#do-chat').submit(function(evt) {
	evt.preventDefault();
	sendMessage()
    });
    */
   $('#send').click(function(){
       $message = $('#message');
       sendMessage();
   });
   $("#message").keyup(function(){ if(event.keyCode == 13){
       $message = $('#message');
       sendMessage();
    } });
});