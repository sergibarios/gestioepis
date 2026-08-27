document.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    if (params.get('open') === 'add') {
        const modalEl = document.querySelector('[data-quick-action="add"]');
        if (modalEl && window.bootstrap) {
            new bootstrap.Modal(modalEl).show();
        }
        params.delete('open');
        const newQuery = params.toString();
        const newUrl = window.location.pathname + (newQuery ? '?' + newQuery : '') + window.location.hash;
        window.history.replaceState({}, '', newUrl);
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('form.delete-form').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!confirm('Segur que vols eliminar aquest element? Aquesta acció no es pot desfer.')) {
                event.preventDefault();
            }
        });
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const toastEl = document.getElementById('appToast');
    if (toastEl && window.bootstrap) {
        new bootstrap.Toast(toastEl, { delay: 3500 }).show();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.btn').forEach(function (btn) {
        btn.addEventListener('click', function (event) {
            const rect = btn.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = event.clientX - rect.left - size / 2;
            const y = event.clientY - rect.top - size / 2;

            const ripple = document.createElement('span');
            ripple.className = 'btn-ripple';
            ripple.style.width = size + 'px';
            ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';

            btn.appendChild(ripple);
            ripple.addEventListener('animationend', function () {
                ripple.remove();
            });
        });
    });
});