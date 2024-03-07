const updateBtnToNonExpose = document.querySelector("button#btn-to-none-expose");
const updateBtnToExpose = document.querySelector("button#btn-to-expose");

updateBtnToExpose.addEventListener("click", () => changeExpose("Y"));
updateBtnToNonExpose.addEventListener("click", () => changeExpose("N"));

async function changeExpose(expose) {
    const checkedHashtags = getAllCheckedHashtags();

    if (checkedHashtags.length === 0) {
        alert(`먼저 ${expose === "Y" ? "노출할" : "노출 안할"} 해시태그들을 선택해주세요.`);
        return;
    }

    const tags = document.querySelectorAll("input.select-tag");

    const query = checkedHashtags.map(el => `tagId=${el}`).join("&");
    const {status} = await axios.patch(`../hashtag/expose/${expose}?${query}`);
    checkedHashtags.forEach(el => {
        const aa = document.querySelector(`td#expose-td-${el}`);
        aa.innerHTML = expose;
    });

    changeAllChecked(tags, false);
}