var locationstr = "http://192.168.1.8:8080/team3_setgame/api/main/getallgames";
$(document).ready(function () {
    getallgame();
});
function getallgame() {
    $.getJSON(locationstr)
            .done(function (result) {
                $("#gamelist").empty();
                var game = result.gamesArray;
                for (var i in game) {
                    var liappend = "<li onclick='enterroom(" + game[i].gameId + ")'>" +
                            "<i class='fa fa-home'></i>" +
                            game[i].title +
                            "</li>";
                    $("#gamelist").append(
                            liappend
                            );
                }
            });
}
function enterroom(gameId) {
    var email = sessionStorage.getItem("email");
    var locationstr = "http://192.168.1.8:8080/team3_setgame/api/game/"
            + gameId + "/" + email;
    alert(locationstr);
    $.getJSON(locationstr)
            .done(function (result) {
                Lobibox.notify('success', {
                    sound: false,
                    msg: 'Join Game Successful! Welcome!'
                });
                $('form').fadeOut(500);
                $('.wrapper').addClass('form-success');
                //$.session.set("email", email);
                setTimeout("window.location.href='game.html';", 1200);
                sessionStorage.setItem("gameId", result.gameId);
            })
            .fail(function () {
                Lobibox.notify('error', {
                    width: $(window).width(),
                    msg: 'Join game Fail! Please Try Again!'
                });
            });

}

