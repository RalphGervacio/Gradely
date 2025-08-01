$(document).ready(function () {
// Global Toast
    window.showToast = function (message, type = 'success') {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: type,
            title: message,
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            background: '#f8fafc',
            color: '#2a2a2a'
        });
    };

});