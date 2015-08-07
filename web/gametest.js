var wsocket;
var serviceLocation = 'ws://localhost:8080/team3_setgame/wssocket/';
var gameId = '';
function connectToChatserver() {
    gameId=$.session.get("gameId");
    wsocket = new WebSocket(serviceLocation+gameId);
    wsocket.onmessage = onMessageReceived;
    
}
function onMessageReceived(evt) {
    //var msg = JSON.parse(evt.data); // native API
    alert(evt.data);
    loadimage(evt.data);
}
function sendMessage() {
    var msg = '{"message":"' + $message.val() + '", "sender":"'
		+ $nickName + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
function loadimage(data){
    var msg = JSON.parse(data);
    
    for(i=0;i<=11;i++){
        var cardname=msg.table[i].number.toString()+
                     msg.table[i].shading.toString()+
                     msg.table[i].color.toString()+
                     msg.table[i].shape.toString();
        var imgId="#img"+i;
        var imgurl="images/cards/"+cardname+".gif";
        //alert(imgurl);
        $(imgId).attr("src",imgurl);
    }
}
$(document).ready(function () {
    //This is used to size the the card squareness
    connectToChatserver();
    var a=$.session.get("gameId");
    alert(a);
});
