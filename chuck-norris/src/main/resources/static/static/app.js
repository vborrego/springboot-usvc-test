$(document).ready(function () {
    console.log("App ready");
});

$(".button").on("click", function (event) {
    console.log("Button clicked");
/*
    $.get("chucknorris")
        .done(function (data) {
            //console.log(data.response);
            $(".joke").text(data.response);
        });*/
});