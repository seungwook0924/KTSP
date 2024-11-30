// 댓글 작성 폼 처리
document.addEventListener("DOMContentLoaded", function () {
    const commentForm = document.querySelector(".comment-form");
    const contentInput = commentForm.querySelector("input[name='content']");
    const boardIdInput = commentForm.querySelector("input[type='hidden'][name='boardId']");

    commentForm.addEventListener("submit", function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const content = contentInput.value.trim();
        const boardId = boardIdInput.value;

        // 댓글 내용이 비어있는지 확인
        if (!content) {
            alert("댓글 내용을 입력해주세요.");
            return;
        }

        // 서버에 데이터 전송
        fetch("/comment/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                content: content,
                boardId: boardId,
            }),
        })
            .then((response) => {
                if (response.ok) window.location.reload(); // 페이지 새로고침
                else
                {
                    return response.text().then((text) => {
                        throw new Error(text);
                    });
                }
            })
            .catch((error) => {
                console.error("Error:", error.message);
                alert("댓글 작성에 실패했습니다. 다시 시도해주세요.");
            });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const replyForms = document.querySelectorAll(".reply-form");

    replyForms.forEach((form) => {
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const boardId = form.querySelector("input[name='boardId']").value;
            const parentCommentId = form.querySelector("input[name='parentCommentId']").value;
            const content = form.querySelector("input[name='content']").value.trim();

            if (!content) {
                alert("대댓글 내용을 입력해주세요.");
                return;
            }

            fetch("/comment/addReply", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    boardId: boardId,
                    parentCommentId: parentCommentId,
                    content: content,
                }),
            })
                .then((response) => {
                    if (response.ok) {
                        window.location.reload(); // 페이지 새로고침
                    } else {
                        return response.text().then((text) => {
                            throw new Error(text);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error:", error.message);
                    alert("대댓글 작성에 실패했습니다. 다시 시도해주세요.");
                });
        });
    });
});

// 대댓글 버튼 누를시
document.addEventListener("DOMContentLoaded", function () {
    const replyLinks = document.querySelectorAll(".toggle-reply-form-link");

    replyLinks.forEach((link) => {
        link.addEventListener("click", function () {
            const commentDiv = link.closest(".comment");
            const replyForm = commentDiv.querySelector(".reply-form");

            // 토글 로직
            if (replyForm.style.display === "none" || !replyForm.style.display) {
                replyForm.style.display = "flex";
            } else {
                replyForm.style.display = "none";
            }
        });
    });
});

// 댓글 삭제
document.addEventListener("DOMContentLoaded", function () {
    const commentsList = document.querySelector(".comments-list");

    // 댓글 삭제 이벤트 리스너
    if (commentsList) {
        commentsList.addEventListener("click", function (event) {
            const deleteButton = event.target.closest(".delete-comment-button"); // 삭제 버튼 탐색
            if (!deleteButton) return;

            // 데이터 가져오기
            const commentDiv = deleteButton.closest(".comment");
            const commentIdInput = commentDiv.querySelector("input[name='commentId']");
            const boardIdInput = commentDiv.querySelector("input[name='boardId']");

            if (!commentIdInput || !boardIdInput) {
                alert("필요한 데이터를 찾을 수 없습니다.");
                return;
            }

            const commentId = commentIdInput.value;
            const boardId = boardIdInput.value;

            // 삭제 확인
            if (!confirm("정말 댓글을 삭제하시겠습니까?")) return;

            // 서버 요청
            fetch("/comment/delete", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    commentId: commentId,
                    boardId: boardId,
                }),
            })
                .then((response) => {
                    if (response.ok) {
                        window.location.reload(); // 페이지 새로고침
                    } else {
                        return response.text().then((text) => {
                            throw new Error(text);
                        });
                    }
                })
                .catch((error) => {
                    console.error("Error:", error.message);
                    alert("댓글 삭제에 실패했습니다. 다시 시도해주세요.");
                });
        });
    }
});