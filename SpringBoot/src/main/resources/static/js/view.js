function confirmDelete() {
    const confirmed = confirm("정말로 삭제하시겠습니까?");
    if (confirmed) {
        document.getElementById("deleteForm").submit();
    }
}

// 팀 지원 로직
document.addEventListener("DOMContentLoaded", function () {
    const joinButton = document.getElementById("join"); // 지원 버튼
    const joinModal = document.getElementById("joinModal"); // 모달 창
    const closeModalBtn = document.getElementById("closeModalBtn"); // 모달 닫기 버튼
    const submitButton = document.getElementById("submit"); // 모달 내 확인 버튼
    const contentInput = document.getElementById("content"); // 입력 필드
    const charCount = document.getElementById("charCount"); // 글자 수 카운터
    const modalError = document.getElementById("modalError"); // 에러 메시지

    const boardId = document.getElementById("boardId").value; // 숨겨진 필드에서 boardId 가져옴

    // 지원 버튼 클릭 시 모달 열기
    joinButton.onclick = function () {
        joinModal.style.display = "block";
    };

    // 모달 닫기 버튼 클릭 시
    closeModalBtn.onclick = function () {
        joinModal.style.display = "none";
    };

    // 입력 글자 수 업데이트
    contentInput.oninput = function () {
        const currentLength = contentInput.value.length;
        charCount.textContent = `${currentLength}/255`;
    };

    // 제출 버튼 클릭 시
    submitButton.onclick = function () {
        const content = contentInput.value.trim();

        // 유효성 검사: 내용이 비어있으면 에러 메시지 표시
        if (!content)
        {
            modalError.style.display = "block";
            return;
        }

        // 에러 메시지 숨기기
        modalError.style.display = "none";
        const requestUrl = `/team/join/${boardId}`;

        // 서버로 데이터 전송
        fetch(requestUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ content }),
        })
            .then((response) => {
                return response.text().then((message) => {
                    alert(message); // 서버에서 반환된 메시지를 알림으로 표시
                    joinModal.style.display = "none";

                    if (response.ok)
                    {
                        joinModal.style.display = "none"; // 성공 시 모달 닫기
                    }
                });
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("서버와 통신 중 오류가 발생했습니다.");
            });
    };
});

// 모집 마감 로직
function closeBoard() {
    // 히든 필드에서 boardId 값 가져오기
    const boardId = document.getElementById("boardId").value;

    if (!boardId)
    {
        alert("게시글 ID를 찾을 수 없습니다.");
        return;
    }

    if (!confirm("정말로 모집을 마감하시겠습니까?")) return;

    fetch(`/team/close/${boardId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => {
            if (!response.ok) return alert("오류가 발생했습니다. 나중에 다시 시도해주세요."); // 서버에서 오류 응답을 받은 경우
            return response.text(); // 성공 메시지 반환
        })
        .then(message => {
            alert(message); // 성공 메시지 출력
            location.reload(); // 페이지 새로고침으로 상태 업데이트
        })
        .catch(error => {
            alert(`오류: ${error.message}`); // 오류 메시지 출력
        });
}