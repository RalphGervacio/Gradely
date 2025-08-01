$(document).ready(function () {
    const $form = $('#loginForm');
    const $alertBox = $('#alertBox');
    $('#showPassword').on('change', function () {
        password.call(this);
    });
    $form.on('submit', function (e) {
        e.preventDefault();
        handleLogin();
    });
    
    function handleLogin() {
        const studentId = $('#studentId').val();
        const password = $('#password').val();
        const rememberMe = $('#rememberMe').is(':checked');

        $.ajax({
            url: '/auth/authenticate',
            method: 'POST',
            data: {studentId, password, rememberMe},
            success: handleResponse,
            error: handleError
        });
    }


    function handleResponse(res) {
        $alertBox.removeClass('d-none alert-success alert-danger');
        if (res.success) {
            $alertBox.addClass('alert-success').text(res.message);
            window.location.href = "/dashboard";
        } else {
            $alertBox.addClass('alert-danger').text(res.message);
        }
    }

    function handleError() {
        $alertBox
                .removeClass('d-none alert-success')
                .addClass('alert-danger')
                .text('An error occurred. Please try again.');
    }

    function password() {
        const passwordField = document.getElementById('password');
        passwordField.type = this.checked ? 'text' : 'password';
    }
});
