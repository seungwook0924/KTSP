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
window.checkFileSize = function(input) {
    const maxSize = 50 * 1024 * 1024; // 50MB
    let totalSize = 0;

    for (const file of input.files) {
        totalSize += file.size;
        if (totalSize > maxSize) {
            alert(`선택된 파일들의 총 크기가 너무 큽니다. (파일 총합 최대 크기: 50MB)`);
            input.value = ""; // 입력 초기화
            break;
        }
    }
};

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
