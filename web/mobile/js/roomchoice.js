var locationstr = "http://" + window.location.host + "/team3_setgame/api/main/getallgames";
$(document).ready(function(){

});
$(document).ready(function () {
    getallgame();
});
function getallgame() {
    $.getJSON(locationstr)
            .done(function (result) {
                $("#gamelist").empty();
                var game = result.gamesArray;
                for (var i in game) {
                    var liappend="<li onclick='enterroom("+game[i].gameId+")'>"+
                                 "<i class='fa fa-home'></i>"+
                                 game[i].title+
                                 "</li>";
                    $("#gamelist").append(
                            liappend
                            );
                }
            });

}
function enterroom(gameId){
    $.session.set("gameId",gameId);
    window.location.href="game.html";
}

