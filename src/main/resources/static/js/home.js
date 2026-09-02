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