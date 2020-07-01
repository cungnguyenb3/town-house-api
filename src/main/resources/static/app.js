var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).title, JSON.parse(greeting.body).content);
        });
    });
}

function sendName() {
    stompClient.send("/app/notification", {}, JSON.stringify({ 'title': $("#title").val() , 'name': $("#name").val() }));
}

function showGreeting(title, message) {
    $("#greetings").append("<tr><td>" + title + "</td> <td>" + message + "</td></tr>");
}

$( document ).ready(function() {
    console.log("hi")
    connect();
});

$(function () {

    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#send").click(function () {
        sendName();
    });
});

