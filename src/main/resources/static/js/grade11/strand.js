$(document).ready(function () {
    $('.strand-card').on('click', function () {
        const strand = $(this).data('strand');
        showToast(`You selected: ${strand}`, 'info');

        setTimeout(() => {
            window.location.href = `/students/strand?name=${encodeURIComponent(strand)}`;
        }, 1500);
    });
});
