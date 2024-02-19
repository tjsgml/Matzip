
const submitBtn = document.getElementById("submit-btn");
submitBtn.addEventListener("click", submitRestaurantInfo);

async function submitRestaurantInfo() {
    const placeName = nameInput.value;
    const address = addrInput.value;
    const detailAddress = detailAddrInput.value;
    const contact = contactInput.value;
    const lon = lngInput.value;
    const lat = latInput.value;

    const category = categorySelect.value;

    console.log(placeName + " placeName")
    console.log(address + " address")
    console.log(detailAddress + " detailAddress")
    console.log(contact + " contact")
    console.log(lon + " lon")
    console.log(lat + " lat")

    if (placeName.trim() === ''
        || address.trim() === ''
        || lon.trim() === ''
        || lat.trim() === ''
    ) {
        alert("지도에서 위치를 선택해주세요.")
        return;
    }

    const businessTimes = getBusinessTime();

    const data = {
        placeName,
        address,
        detailAddress,
        contact,
        lon, lat,
        category,
        businessTimes
    }

    console.log("data = {}" , data);

    const {data: result} = await axios.post("./restaurant", data);

    location.href = `./restaurant/${result}/menu`;
}