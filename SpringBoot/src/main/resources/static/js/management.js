function confirmReject() {
    const confirmed = confirm("정말로 거절하시겠습니까?");
    if (confirmed) {
        document.getElementById("reject").submit();
    }
}

function confirmExpulsion() {
    const confirmed = confirm("정말로 추방하시겠습니까?");
    if (confirmed) {
        document.getElementById("expulsion").submit();
    }
}