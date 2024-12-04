
(function ($) {
    "use strict";
    let usernameElement = $("#username"),
        username = getUsernameFromCookie(),
        chat_users_list = $("#chat-users-list"),
        chat_person = $("#chat-person").clone(true),
        chat_right_side = $("#chat-right-side"),
        chat_textarea_input = $("#chat-textarea-input"),
        send_btn = $("#send-btn"),
        chat_messages_list = $("#chat-messages-list"),
        chat_message_me = $("#chat-message-me").clone(true),
        chat_message_him = $("#chat-message-him").clone(true),
        chat_person_name = $("#chat-right-side #chat-person-name"),
        logout_btn = $("#logout-btn"),
        myAvatar_path = getMyAvatar(),
        socket = new SockJS('/chat'),  // Connect to the WebSocket endpoint
        stompClient = Stomp.over(socket);
    if(!username)
    {
        location.assign("/login");
    }
    stompClient.connect({}, function(frame) {
        stompClient.subscribe("/users/push-all-users",onAllUsersMessageReceived);
        stompClient.subscribe(`/users/user-messages/${username}`,pushMessageToList);
        stompClient.subscribe(`/users/${username}`,onChatMessageReceived);
        stompClient.send("/controller/connect-user",{},JSON.stringify({username}));
    });

    chat_users_list.on("click",".person",showPersonMessages);
    send_btn.on("click",sendMessage);
    logout_btn.on("click",logout);

    function onAllUsersMessageReceived(msg){
        let users = JSON.parse(msg.body);
        chat_users_list.children().remove();
        users.forEach(user=>{
        if(user.username!= username){
            chat_person.find(".fullname").text(user.fullname);
            chat_person.find(".username").text(user.username);
            if(user.status){
                chat_person.find(".status").removeClass("offline");
                chat_person.find(".status").addClass("online");
            }
            chat_person.find(".user img").attr("src",getRandomAvatarPath());
            chat_person.attr("id",user.username);
            chat_users_list.append(chat_person.clone());
            chat_person.attr("id","chat-person");
            chat_person.find(".status").removeClass("online");
            chat_person.find(".status").addClass("offline");
        }
        });
    }

     function pushMessageToList(m){
            m =  JSON.parse(m.body);
            if(m){
                chat_messages_list.children().remove();
                 m.forEach(ms=>{
                     if(ms.sender?.username == username){
                         chat_message_me.removeClass("hidden");
                         chat_message_me.find(".chat-name").text("You");
                         chat_message_me.find(".chat-text").text(ms.content);
                         chat_message_me.find(".chat-hour span.hour").text(timeAgo(ms.sentAt));
                         chat_message_me.find(".chat-avatar img").attr("src",myAvatar_path);
                         chat_messages_list.append(chat_message_me.clone(true));
                         chat_message_me.addClass("hidden");
                     }
                     else{
                         chat_message_him.removeClass("hidden");
                         chat_message_him.find(".chat-name").text(ms.sender.fullname);
                         chat_message_him.find(".chat-text").text(ms.content);
                         chat_message_him.find(".chat-hour span.hour").text(timeAgo(ms.sentAt));
                         chat_message_him.find(".chat-avatar img").attr("src",$(`#${ms.sender.username}`).find(".user img").attr("src"));
                         chat_messages_list.append(chat_message_him.clone(true));
                         chat_message_me.addClass("hidden");
                     }
                 });
            }
           chat_messages_list.animate({scrollTop:`${$('.chat-msg').length * ($('.chat-msg').height() + parseInt($('.chat-msg').css('margin-bottom'),10))}px`});
     }
    function onChatMessageReceived(msg){
        msg =  JSON.parse(msg.body);
        if(msg.receiver.username==username){
            if( msg.sender.username == chat_person_name.find("span.name").attr("data-username")){
                chat_message_me.removeClass("hidden");
                chat_message_him.find(".chat-name").text(chat_person_name.find("span.name").text());
                chat_message_him.find(".chat-text").text(msg.content);
                chat_message_him.find(".chat-hour span.hour").text(timeAgo(msg.sentAt));
                chat_message_him.find("#chat-avatar img").attr("src",$(`#${msg.sender.username}`).find(".user img").attr("src"));
                chat_messages_list.append(chat_message_him.clone(true));
                chat_message_me.addClass("hidden");
               chat_messages_list.animate({scrollTop:`${$('.chat-msg').length * ($('.chat-msg').height() + parseInt($('.chat-msg').css('margin-bottom'),10))}px`});
           }else{
              $(`#${msg.sender.username}`).find(".name-time .has-messages").removeClass('hidden');
           }
       }
    }
     function showPersonMessages(e){
        $(this).find(".name-time .has-messages").addClass('hidden');
        chat_person_name.find("span.name").text($(this).find(".fullname").text());
        chat_person_name.find("span.name").attr("data-username",$(this).find(".username").text());
        chat_right_side.removeClass("hidden");
        setChatMessagesListHeight();
        stompClient.send("/controller/user-messages",{},JSON.stringify({
          from:username,
          to:$(this).find(".username").text()
        }));
    }
    function sendMessage(e){
        let receiverUsername = chat_person_name.find("span.name").attr("data-username");
        let content = chat_textarea_input.val().trim();
        chat_right_side.removeClass("hidden");
        stompClient.send("/controller/send-message",{},JSON.stringify({
           username,
           from:username,
           to:receiverUsername,
           content
       }));
        chat_message_me.removeClass("hidden");
        chat_message_me.find(".chat-name").text("You");
        chat_message_me.find(".chat-text").text(content);
        chat_message_me.find(".chat-hour span.hour").text('now');
        chat_message_me.find(".chat-avatar img").attr("src",myAvatar_path);
        chat_messages_list.append(chat_message_me.clone(true));
        chat_message_me.addClass("hidden");
        chat_textarea_input.val('');
       chat_messages_list.animate({scrollTop:`${$('.chat-msg').length * ($('.chat-msg').height() + parseInt($('.chat-msg').css('margin-bottom'),10))}px`});
    }

    function getRandomIntInRange(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    function getRandomAvatarPath(){
        return `images/avatars/avatar${getRandomIntInRange(1,8)}.png`;
    }

    // Helper function to format the difference
    function timeAgo(dateArray) {
        // Convert array to Date object (month is 0-indexed in JavaScript)
        const date = new Date(dateArray[0], dateArray[1]-1, dateArray[2], dateArray[3], dateArray[4], dateArray[5]);
        // Get the current time
        const now = Date.now();

        // Calculate the difference in milliseconds
        const difference = now - date.getTime();

        // Create a RelativeTimeFormat object
        const rtf = new Intl.RelativeTimeFormat('en', { numeric: 'auto' });
        const seconds = Math.floor(difference / 1000);
        const minutes = Math.floor(difference / 60000);
        const hours = Math.floor(difference / 3600000);
        const days = Math.floor(difference / 86400000);
        const months = Math.floor(difference / 2592000000);
        const years = Math.floor(difference / 31536000000);

        if (years > 0) return rtf.format(-years, 'year');
        if (months > 0) return rtf.format(-months, 'month');
        if (days > 0) return rtf.format(-days, 'day');
        if (hours > 0) return rtf.format(-hours, 'hour');
        if (minutes > 0) return rtf.format(-minutes, 'minute');
        return rtf.format(-seconds, 'second');
    }
    function getUsernameFromCookie() {
        let keyValCookies = {};
        let cookies = document.cookie;
        if(cookies){
            cookies.trim().split(";").forEach(cookieSlice=>{
                let kv = cookieSlice.trim().split('=');
                keyValCookies[kv[0]] = kv[1];
            });
        }
        return keyValCookies['username'];
    }
    function setAvatarToMe(){
        if(!sessionStorage.getItem("avatar"))
            sessionStorage.setItem("avatar",getRandomAvatarPath());
    }
     function getMyAvatar(){
        if(!sessionStorage.getItem("avatar"))
           setAvatarToMe();
        return sessionStorage.getItem("avatar");
     }
     function setChatMessagesListHeight(){
         let windowHeight = $(window).height();
         let height = windowHeight - parseInt($('body').css('margin-top'),10)
                      - $('.page-title').height() - $('#chat-person-name').height()
                      - parseInt($('.chat-container').css('padding-top'),10)
                      - parseInt($('.chat-container').css('padding-bottom'),10)
                      - parseInt($('#chat-input-container').css('margin-top'),10)
                      - $('#chat-input-container').height();
         chat_messages_list.css('height',`${height}px`);
     }
     function logout(){
        stompClient.disconnect(function() {
            document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";            console.log("logging out")
            location.assign("/login");
        });
     }
})(jQuery);