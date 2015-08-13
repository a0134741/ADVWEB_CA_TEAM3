var locationstr = "http://10.10.2.93:8080/team3_setgame/api/main/getallgames";
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
    swal({title: "Are you sure to join the game?",
        text: "",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes!",
        closeOnConfirm: false}, function () {
        var email = sessionStorage.getItem("email");
        var locationstr = "http://10.10.2.93:8080/team3_setgame/api/game/"
                + gameId + "/" + email;
        //alert(locationstr);
        $.getJSON(locationstr)
                .done(function (result) {

                    setTimeout("window.location.href='game.html';", 1200);
                    sessionStorage.setItem("gameId", result.gameId);
                });
    });
}

