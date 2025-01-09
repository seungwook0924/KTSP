
// 쿠키 저장 함수
function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = `${name}=${encodeURIComponent(value)};path=/;expires=${date.toUTCString()}`;
}

// 쿠키 가져오기 함수
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
    return null;
}

// 페이지 로드 시 실행
window.onload = function() {
    const savedId = getCookie("studentNumber");
    if (savedId) {
    document.getElementById("studentNumber").value = savedId;
    document.getElementById("remember").checked = true;
    }
};

// 폼 제출 이벤트 핸들러
document.getElementById("loginForm").addEventListener("submit", function(event) {
    const remember = document.getElementById("remember").checked;
    const studentNumber = document.getElementById("studentNumber").value;

    if (remember) {
    setCookie("studentNumber", studentNumber, 30); // 30일 동안 저장
    } else {
    setCookie("studentNumber", "", 0); // 쿠키 삭제
    }
});
