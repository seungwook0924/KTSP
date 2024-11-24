//게시글 수정에서 파일을 삭제하는 로직
document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-button");
    const boardId = document.getElementById("boardId").value; // Hidden input에서 boardId 가져오기

    deleteButtons.forEach(button => {
        button.addEventListener("click", function () {
            // 숨겨진 input에서 파일 이름 읽기
            const fileName = button.previousElementSibling.value;
            const deleteUrl = document.getElementById("deleteUrl").value;

            // 서버로 DELETE 요청 보내기
            fetch(`${deleteUrl}?fileName=${encodeURIComponent(fileName)}&boardId=${encodeURIComponent(boardId)}`, {
                method: "DELETE",
            })
                .then(response => {
                    if (response.ok) {
                        // 성공 시 UI에서 항목 제거
                        button.parentElement.remove();
                        console.log(`파일 삭제 성공: ${fileName}`);
                    } else {
                        console.error(`파일 삭제 실패: ${fileName}`);
                    }
                })
                .catch(error => console.error("삭제 요청 중 오류 발생:", error));
        });
    });
});

//파일 등록
document.getElementById("files").addEventListener("change", function () {
    const fileList = this.files;
    const selectedFilesElement = document.getElementById("selectedFiles");
    selectedFilesElement.innerHTML = ""; // 초기화

    if (fileList.length === 0) {
        const noFileItem = document.createElement("li");
        noFileItem.textContent = "선택된 파일 없음";
        selectedFilesElement.appendChild(noFileItem);
    } else {
        Array.from(fileList).forEach(file => {
            const listItem = document.createElement("li");
            listItem.textContent = file.name;
            selectedFilesElement.appendChild(listItem);
        });
    }
});

//파일 크기 초과 방지 로직
function checkFileSize(input) {
    const maxSize = 50 * 1024 * 1024; // 50MB
    let totalSize = 0; // 총 파일 크기 합계 변수

    for (const file of input.files) {
        totalSize += file.size; // 파일 크기 누적
        if (totalSize > maxSize) { // 총합이 최대 크기를 초과하면 경고
            alert(`선택된 파일들의 총 크기가 너무 큽니다. (파일 총합 최대 크기: 50MB)`);
            input.value = ""; // 입력 초기화
            break;
        }
    }
}

//제목의 글자수를 제한하는 로직
document.getElementById("title").addEventListener("input", function () {
    const titleInput = this.value;
    const warningElement = document.getElementById("titleWarning");

    if (titleInput.length > 20) {
        warningElement.style.display = "block"; // 경고 메시지 표시
        this.value = titleInput.slice(0, 20); // 글자수를 20자로 자름
    } else {
        warningElement.style.display = "none"; // 경고 메시지 숨김
    }
});

let isFormSubmitted = false;

// beforeunload 이벤트 핸들러
function beforeUnloadHandler(e) {
    if (!isFormSubmitted) {
        e.preventDefault();
        e.returnValue = ''; // 최신 브라우저를 위한 설정
    }
}

let isFormSubmitted = false;

// beforeunload 이벤트 핸들러
function beforeUnloadHandler(e) {
    if (!isFormSubmitted) {
        e.preventDefault();
        e.returnValue = ''; // 최신 브라우저를 위한 설정
    }
}

// 페이지를 떠날 때 경고 메시지 표시
window.addEventListener('beforeunload', beforeUnloadHandler);

// 폼 제출 시 이벤트 제거
document.querySelector('form').addEventListener('submit', function (event) {
    console.log("Form is being submitted...");
    isFormSubmitted = true;

    // beforeunload 이벤트 제거
    window.removeEventListener('beforeunload', beforeUnloadHandler);

    // 디버깅: 제출 이벤트 이후를 로그로 확인
    setTimeout(() => {
        console.log("Submit event completed. Listener removed.");
    }, 500);
});