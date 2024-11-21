// 비밀번호 입력 시 유효성 검사
document.getElementById("password").addEventListener("input", function (event) {
    const password = event.target.value;
    const passwordError = document.getElementById("passwordError");

    // 비밀번호 유효성 검사 정규식
    const validPasswordPattern = /^[a-zA-Z0-9!@#$%^&*(),.?":{}|<>~`[\]\\_+=-]*$/;

    if (!validPasswordPattern.test(password)) {
        passwordError.style.display = "block";
        passwordError.textContent = "비밀번호는 영문자, 숫자 또는 특수문자만 입력할 수 있습니다.";
        event.target.value = password.replace(/[^a-zA-Z0-9!@#$%^&*(),.?":{}|<>~`[\]\\_+=-]/g, ""); // 유효하지 않은 문자 제거
    } else {
        passwordError.style.display = "none";
    }
});

// 비밀번호 확인 필드 검증
document.getElementById("registerForm").addEventListener("submit", function (event) {
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const passwordError = document.getElementById("passwordError");

    // 비밀번호와 비밀번호 확인 값이 일치하지 않는 경우
    if (password !== confirmPassword) {
        event.preventDefault(); // 폼 제출 중단
        passwordError.style.display = "block";
        passwordError.textContent = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    }
});

// DOM 요소 가져오기
var emailVerifyBtn = document.getElementById("emailVerifyBtn");
var emailModal = document.getElementById("emailModal");
var closeModalBtn = document.getElementsByClassName("close")[0];
var emailError = document.getElementById("emailError");
var emailSuccess = document.getElementById("emailSuccess");
var emailSending = document.getElementById("emailSending");
var emailVerified = false; // 이메일 인증 여부
var email; // 사용자가 입력한 이메일

// 이메일 인증 버튼 클릭 시
emailVerifyBtn.onclick = function ()
{
    email = document.getElementById("emailId").value; // 폼에서 입력한 이메일 가져오기
    // 한글 입력 방지 정규식
    const invalidPattern = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g;

    // 한글이 포함된 경우
    if (invalidPattern.test(email))
    {
        alert("이메일에는 한글을 포함할 수 없습니다."); // 경고 메시지
        return; // 함수 종료
    }

    document.getElementById("emailVerifyBtn").disabled = true; //버튼 클릭 비활성화


    // 이메일 검증 (빈 값 체크)
    if (email)
    {
        email += "@kangwon.ac.kr";
        emailSending.style.display = "block";
        emailError.style.display = "none";

        // 이메일 인증 요청
        fetch("/api/email/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: email, // 이메일 주소를 JSON으로 전달
            }),
        })
            .then((response) => {
                if (response.ok) {
                    // 인증 코드 발송 성공 시 모달 표시
                    emailModal.style.display = "block"; // 인증 모달 띄우기
                    emailError.style.display = "none"; // 에러 메시지 숨기기
                } else {
                    // 서버 응답이 실패일 경우
                    emailError.style.display = "block";
                    emailSending.style.display = "none";
                    emailError.textContent = "인증 코드를 보낼 수 없습니다. 다시 시도해주세요.";
                    document.getElementById("emailVerifyBtn").disabled = false; //버튼 클릭 활성화
                }
            })
            .catch((error) => {
                // 요청 실패 시 에러 메시지 표시
                emailError.style.display = "block";
                emailSending.style.display = "none";
                emailError.textContent = "요청 중 문제가 발생했습니다. 다시 시도해주세요.";
                document.getElementById("emailVerifyBtn").disabled = false; //버튼 클릭 활성화
            });
    }
    else
    {
        // 이메일이 입력되지 않은 경우
        emailError.style.display = "block";
        emailError.textContent = "이메일을 입력해주세요.";
        document.getElementById("emailVerifyBtn").disabled = false; //버튼 클릭 활성화
        alert("이메일을 입력해주세요.");
    }
};

// 모달 닫기 버튼 클릭 시
closeModalBtn.onclick = function ()
{
    emailModal.style.display = "none";
    document.getElementById("emailId").value = "";
    document.getElementById("emailVerifyBtn").disabled = false; //버튼 클릭 활성화
};

document.getElementById("emailId").addEventListener("input", function () {
    const emailValue = this.value;
    document.getElementById("hiddenEmailId").value = emailValue; // 값 복사
});

// 인증번호 확인 버튼 클릭 시
document.getElementById("verifyCodeBtn").onclick = function ()
{
    document.getElementById("hiddenEmailId").value = emailId.value; // 값 복사
    var verificationCode = document.getElementById("verificationCode").value;

    // 인증번호를 서버로 보내 확인
    fetch("/api/email/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            email: email, // 이메일 주소
            verifyCode: verificationCode, // 입력한 인증 코드
        }),
    })
        .then((response) => {
            if (response.ok) {
                return response.json(); // 성공 시 JSON 파싱
            } else {
                throw new Error("인증 실패");
            }
        })
        .then((data) => {
            if (data.status === "success") {
                // 인증 성공 시
                emailVerified = true; // 이메일 인증 완료
                emailSuccess.style.display = "block"; // 인증 완료 메시지 표시
                emailModal.style.display = "none"; // 모달 닫기
                emailSending.style.display = "none";
                document.getElementById("modalError").style.display = "none";

                // 이메일 입력 필드를 비활성화
                document.getElementById("emailId").disabled = true;
                document.getElementById("emailVerifyBtn").disabled = true;
            } else {
                // 인증 실패 시
                emailSending.style.display = "none";
                document.getElementById("modalError").style.display = "block";
                document.getElementById("modalError").textContent = "인증 코드가 일치하지 않습니다.";
            }
        })
        .catch((error) => {
            // 요청 실패 시 에러 메시지 표시
            console.error("Error verifying email:", error);
            document.getElementById("modalError").style.display = "block";
            emailSending.style.display = "none";
            document.getElementById("emailVerifyBtn").disabled = false; //버튼 클릭 활성화
            document.getElementById("modalError").textContent = "인증 확인 중 문제가 발생했습니다. 다시 시도해주세요.";
        });
};

// 회원가입 폼 제출 시 이메일 인증 여부 체크
document.getElementById("registerForm").onsubmit = function (event)
{
    if (!emailVerified)
    {
        event.preventDefault(); // 이메일 인증이 완료되지 않으면 폼 제출을 막음
        alert("이메일 인증을 먼저 완료해주세요.");
    }
};