const submitBtn = document.getElementById("submit-btn");
submitBtn.addEventListener("click", submitRestaurantInfo);

async function submitRestaurantInfo() {

    const confirmToUpdate = confirm(`가게 정보를 수정하시겠습니까?
    ** 되돌릴 수 없습니다.
    `)

    if (!confirmToUpdate) {
        return;
    }


    const restaurantId = restaurantIdInput.value;
    const placeName = nameInput.value;
    const address = addrInput.value;
    const detailAddress = detailAddrInput.value;
    const contact = contactInput.value;
    const lon = lngInput.value;
    const lat = latInput.value;
    const category = categorySelect.value;

    if (placeName.trim() === ''
        || address.trim() === ''
        || lon.trim() === ''
        || lat.trim() === ''
    ) {
        alert("지도에서 위치를 선택해주세요.")
        return;
    }

    const data = {
        restaurantId,
        placeName,
        address,
        detailAddress,
        contact,
        lon, lat,
        category
    }

    console.log("data = {}", data);

}