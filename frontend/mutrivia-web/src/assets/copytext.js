function copytext() {
    let copyText = document.querySelector(".copy-text");
    let input = copyText.querySelector("input.text");
    input.select();
    input.setSelectionRange(0, 99999);
    // it can only be used in HTTPS
    // navigator.clipboard.writeText(input.value);
    input.select();
    document.execCommand("copy");
    copyText.classList.add("active");
    window.getSelection().removeAllRanges();
    setTimeout(function(){
        copyText.classList.remove("active");
        console.log("copied!");
    },2500);
}
