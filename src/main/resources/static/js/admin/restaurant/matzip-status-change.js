document.querySelectorAll("button.close-restaurant").forEach(el => {
    const restaurantId = el.parentElement.parentElement.getAttribute("data-id");
    el.addEventListener("click", () => setClosureStatusRestaurantById(restaurantId));
})

async function setClosureStatusRestaurantById(id) {
    const continueSet = confirm("폐업 상태로 전환하시겠습니까?");
    if (continueSet) {
        const resp = await axios.put(`./${id}/closure`);
        location.reload();
    }
}

document.querySelectorAll("button.confirm-restaurant").forEach(el => {
    const restaurantId = el.parentElement.parentElement.getAttribute("data-id");
    el.addEventListener("click", () => confirmRestaurant(restaurantId));
})

async function confirmRestaurant(id) {
    const continueSet = confirm("레스토랑 등록을 승인하시겠습니까??");
    if (continueSet) {
        const resp = await axios.put(`./${id}/open`);
        location.reload();
    }
}

document.querySelectorAll("button.delete-restaurant").forEach(el => {
    const restaurantId = el.parentElement.parentElement.getAttribute("data-id");
    el.addEventListener("click", () => deleteRestaurant(restaurantId));
})

async function deleteRestaurant(id) {
    const continueSet = confirm(`레스토랑 정보를 삭제하시겠습니까?
    ** 되돌릴 수 없습니다.`);
    if (continueSet) {
        const {status} = await axios.delete(`./${id}`);
        console.log(status);

        if (status === 204) location.reload();
    }
}