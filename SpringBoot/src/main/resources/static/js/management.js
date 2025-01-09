function confirmReject(button) {
    const confirmed = confirm(`정말로 거절하시겠습니까?`);
    if (confirmed) {
        button.closest("form").submit(); // 버튼의 부모 폼을 찾아 제출
    }
}

function confirmExpulsion(button) {
    const confirmed = confirm(`정말로 추방하시겠습니까?`);
    if (confirmed) {
        button.closest("form").submit(); // 버튼의 부모 폼을 찾아 제출
    }
}