
const submitBtn = document.getElementById("submit-btn");
submitBtn.addEventListener("click", submitRestaurantInfo);

async function submitRestaurantInfo() {
    const placeName = nameInput.value;
    const address = addrInput.value;
    const detailAddress = detailAddrInput.value;
    const contact = contactInput.value;
    const lng = lngInput.value;
    const lat = latInput.value;

    console.log(placeName + " placeName")
    console.log(address + " address")
    console.log(detailAddress + " detailAddress")
    console.log(contact + " contact")
    console.log(lng + " lng")
    console.log(lat + " lat")

    if (placeName.trim() === ''
        || address.trim() === ''
        || lng.trim() === ''
        || lat.trim() === ''
    ) {
        alert("지도에서 위치를 선택해주세요.")
        return;
    }

    const businessTime = getBusinessTime();

    const data = {
        placeName,
        address,
        detailAddress,
        contact,
        lng, lat,
        businessTime
    }

    console.log("data = {}" , data);

    const {data: result} = await axios.post("./restaurant", data);

    location.href = `./restaurant/${result}/menu`;
}