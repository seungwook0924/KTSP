// 슬라이더 기능 구현
let currentSlide = 0;
const slides = document.querySelectorAll(".banner-slide");
const totalSlides = slides.length;

function showSlide(index) {
    // 모든 슬라이드를 숨기고 현재 슬라이드만 표시
    slides.forEach(slide => slide.classList.remove("active-slide"));
    slides[index].classList.add("active-slide");
}

function nextSlide() {
    currentSlide = (currentSlide + 1) % totalSlides;
    showSlide(currentSlide);
}

// 5초마다 슬라이드 전환
setInterval(nextSlide, 5000);