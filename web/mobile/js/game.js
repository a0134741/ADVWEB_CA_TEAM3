var choose = 0;
var position = new Array();
var k = 0;
var wsocket;
var serviceLocation = 'ws://localhost:8080/team3_setgame/wssocket/';
var gameId = '1';
var email = "a0134741@u.nus.edu";

function connectToChatserver($) {
    wsocket = new WebSocket(serviceLocation + gameId);
    wsocket.onmessage = onMessageReceived;
    alert(serviceLocation + gameId);
}
function onMessageReceived(evt) {
    //var msg = JSON.parse(evt.data); // native API
    alert(evt.data);
    loadimage(evt.data);
}
function sendChatMessage() {
    var msg = '{"message":"' + $message.val() + '", "sender":"'
            + $nickName + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
function sendGameMessage() {
    var msg = '{"type":1,"position1":' + position[0]
            + ', "position2":' + position[1]
            + ', "position3":' + position[2]
            + ', "email":"' + email
            + '", "gameId":' + gameId + '}';
    alert(msg);
    wsocket.send(msg);
}
function loadimage(data) {
    var msg = JSON.parse(data);
    for (i = 0; i <= 11; i++) {
        var cardname = msg.table[i].number.toString() +
                msg.table[i].shading.toString() +
                msg.table[i].color.toString() +
                msg.table[i].shape.toString();
        var imgId = "#img" + i;
        var imgurl = "../images/cards/" + cardname + ".gif";
        jQuery(imgId).attr("src", imgurl);
    }
}

jQuery(document).ready(function($){
    gameId = sessionStorage.getItem("gameId");
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
    connectToChatserver($);
});