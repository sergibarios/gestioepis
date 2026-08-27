document.addEventListener("DOMContentLoaded", () => {
    const header = document.querySelector(".navbar");

    window.addEventListener("scroll", () => {
        if (window.scrollY > 50) {
            header.classList.add("navbar-scrolled");
        } else {
            header.classList.remove("navbar-scrolled");
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    document.querySelectorAll('.stat-value').forEach((el) => {
        const target = parseInt(el.textContent.trim(), 10);
        if (isNaN(target) || prefersReducedMotion) return;

        const duration = 800;
        const startTime = performance.now();

        function tick(now) {
            const progress = Math.min((now - startTime) / duration, 1);
            const eased = 1 - Math.pow(1 - progress, 3);
            el.textContent = Math.round(eased * target);
            if (progress < 1) {
                requestAnimationFrame(tick);
            } else {
                el.textContent = target;
            }
        }

        requestAnimationFrame(tick);
    });
});