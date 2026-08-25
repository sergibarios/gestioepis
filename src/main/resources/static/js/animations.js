document.addEventListener("DOMContentLoaded", () => {
    const actionCards = document.querySelectorAll('.action-card');

    actionCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(30px)';

        setTimeout(() => {
            card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 150 * (index + 1));
    });

    setTimeout(() => {
        actionCards.forEach(card => {
            card.style.transition = 'transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease';
        });
    }, 1000);
});