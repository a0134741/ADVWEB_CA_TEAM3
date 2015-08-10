var choose = 0;
var position = new Array();
var k = 0;
var wsocket;
var serviceLocation = 'ws://172.23.28.196:8080/team3_setgame/wssocket/';
var gameId;
var $message;
var email;
function connectToChatserver($) {
    wsocket = new WebSocket(serviceLocation + gameId);
    wsocket.onmessage = onMessageReceived;
    alert(serviceLocation + gameId);
}
function onMessageReceived(evt) {
    if (evt.data == "Not_Set"||evt.data == "NOT_FOUND")
    {
        alert(evt.data);
    }
    else
    {
        var msg = JSON.parse(evt.data); // native API
        alert(evt.data);
        if (msg.type == 0)
        {
            showMessage(msg);
        }
        else {
            loadimage(evt.data);
        }
    }
}
function sendChatMessage() {
    var msg = '{"type":0,"message":"' + $message.val() + '", "sender":"'
            + email + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
function sendGameMessage() {
    var msg = '{"type":1,"position1":' + position[0]
            + ', "position2":' + position[1]
            + ', "position3":' + position[2]
            + ', "email":"' + email
            + '", "gameId":' + gameId + '}';
    wsocket.send(msg);
}
function loadimage(data) {
    var msg = JSON.parse(data);
    for (var i = 0; i <= 11; i++) {
        var cardname = msg.table[i].number.toString() +
                msg.table[i].shading.toString() +
                msg.table[i].color.toString() +
                msg.table[i].shape.toString();
        var imgId = "#img" + i;
        var imgurl = "../images/cards/" + cardname + ".gif";
        jQuery(imgId).attr("src", imgurl);
    }
}
function showMessage(msg) {
    if (msg.sender.toString() == email)
    {
        var $messageLine = $('<div class="message right"><img src="img/1_copy.jpg"/>\n\
                        <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    else
    {
        var $messageLine = $('<div class="message"><img src="img/1_copy.jpg"/>\n\
                            <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    $('#chatdetail').append($messageLine);
    document.getElementById("msg_end").scrollIntoView();
}
function showchat() {
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

jQuery(document).ready(function ($) {
    showchat();
    gameId = sessionStorage.getItem("gameId");
    email = sessionStorage.getItem("email");

    //alert(gameId);
    $("#selectable li").css("height", $("#selectable li").css("width"));
    $("#selectable li").on("click", function () {
        if (choose < 3)
        {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                choose = choose - 1;
            }
            else {
                $(this).addClass("selected");
                choose = choose + 1;
            }
        }
        else {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                choose = choose - 1;
            }
            else {
                alert(">3");
            }
        }
    });
    $("#gamesubmit").click(function ()
    {
        if (choose = 3) {
            $("#selectable li").each(function () {
                if ($(this).hasClass("selected")) {
                    position[k] = $(this).attr("id");
                    k = k + 1;
                    $(this).removeClass("selected");
                }
            });
            alert(position);
            sendGameMessage();
            k = 0;
            choose = 0;
            position.splice(0, position.length);
        }
        else {
            alert("No enough 3 cards");
        }
    });
    $('#send').click(function () {
        $message = $('#message');
        sendChatMessage();
    });
    $("#message").keyup(function () {
        if (event.keyCode == 13) {
            $message = $('#message');
            sendChatMessage();
        }
    });
    connectToChatserver();
});