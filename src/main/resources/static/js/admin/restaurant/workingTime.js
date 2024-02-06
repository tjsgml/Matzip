function getBusinessTime() {
    const businessDiv = document.querySelectorAll("div.business-time");
    const businessTime = {};

    businessDiv.forEach(el => {
        const dayValue = (el.querySelector("input#day-value").value.toLowerCase());
        const isHoliday = el.querySelector("input#is-holiday").checked;
        const startHour = el.querySelector("input#input-start-hour").value;
        const startMinute = el.querySelector("input#input-start-minute").value;
        const endHour = el.querySelector("input#input-end-hour").value;
        const endMinute = el.querySelector("input#input-end-minute").value;

        if (!isHoliday && (!startHour || !startMinute || !endMinute || !endHour)) {
            businessTime[dayValue] = null;
        } else {
            businessTime[dayValue] = {
                isHoliday,
                startHour,
                startMinute,
                endHour,
                endMinute
            }
        }
    })

    return businessTime;
}

