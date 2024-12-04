
(function ($) {
    "use strict";

    /*==================================================================
    [ Validate ]*/
    let submit_btn = $(".submit_btn"),
        register_page= $("#register_page"),
        login_page = $("#login_page"),
        login_username_input = $("#login_page .username_input"),
        signup_username_input = $("#register_page .username_input"),
        fullname_input = $(".fullname_input"),
        login_password_input = $("#login_page .password_input"),
        signup_password_input = $("#register_page .password_input"),
        confirm_password_input = $(".confirm_password_input"),
        login_now_btn = $("#login-now-btn"),
        signup_now_btn = $("#signup-now-btn"),
        input_alert_container = $(".input-alert-container"),
        input_alert = $(".input-alert"),
        input_alert_copy = $(".input-alert").first().clone(true),
        signup_login_change = $(".signup-login-change");
    $('#register_page .validate-form').on('submit',function(e){
      validateSignupFormInputs();
      if(isInputEmpty(fullname_input)||
         isInputEmpty(signup_username_input)||
         isInputEmpty(signup_password_input)||
         signup_password_input.val() != confirm_password_input.val()
      ){
         e.preventDefault();
         return;
      }
     });
    $('#login_page .validate-form').on('submit',function(e){
        validateLoginFormInputs();
         if(isInputEmpty(login_username_input)||
            isInputEmpty(login_password_input))
         {
             e.preventDefault();
             return;
         }
     });

    function validateSignupFormInputs(){
         input_alert_container.children().remove();
         checkIfInputEmptyAndRaiseAlert(fullname_input);
         checkIfInputEmptyAndRaiseAlert(signup_username_input);
         checkIfInputEmptyAndRaiseAlert(signup_password_input);
         checkIfPasswordIsMatchedAndRaiseAlert(confirm_password_input);
    }
    function validateLoginFormInputs(){
        input_alert_container.children().remove();
        checkIfInputEmptyAndRaiseAlert(login_username_input);
        checkIfInputEmptyAndRaiseAlert(login_password_input);
    }
    function checkIfInputEmptyAndRaiseAlert(input){
        input_alert_copy.text("This filed can't be empty.");
        if (isInputEmpty(input)){
            input.parent().siblings(".input-alert-container").append(input_alert_copy.clone());
            return true;
        }
        return false;
    }
    function isInputEmpty(input){
        return input.val().trim().length < 1;
    }
     function checkIfPasswordIsMatchedAndRaiseAlert(input){
            input_alert_copy.text("Password confirmation doesn't match Password.");
            if(signup_password_input.val() != input.val())
                input.parent().siblings(".input-alert-container").append(input_alert_copy.clone());
     }

})(jQuery);