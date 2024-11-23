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