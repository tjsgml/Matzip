const businessTimes = document.querySelectorAll("div.business-time");

businessTimes.forEach(el => {
    const openTime = el.querySelector("input#open-time").value;
    const closeTime = el.querySelector("input#close-time").value;
    const isHoliday = el.querySelector("input#is-holiday").value;
    const alertError = el.querySelector("div#alert-error-comp");

    const inputStartHour = el.querySelector("input#input-start-hour");
    const inputStartMinute = el.querySelector("input#input-start-minute");
    const inputEndHour = el.querySelector("input#input-end-hour");
    const inputEndMinute = el.querySelector("input#input-end-minute");
    const isHolidayToUpdate = el.querySelector("input#is-holiday-to-update");

    inputStartHour.addEventListener("keyup", () => checkHourInputValid(inputStartHour, alertError));
    inputStartMinute.addEventListener("keyup", () => checkMinuteInputValid(inputStartMinute, alertError));
    inputEndHour.addEventListener("keyup", () => checkHourInputValid(inputEndHour, alertError));
    inputEndMinute.addEventListener("keyup", () => checkMinuteInputValid(inputEndMinute, alertError));

    if (isHoliday === "true") {
        isHolidayToUpdate.checked = true;
        return;
    }

    mkFormatHourAndMinute(openTime, inputStartHour, inputStartMinute);
    mkFormatHourAndMinute(closeTime, inputEndHour, inputEndMinute);
})

function checkHourInputValid(hourInput, alertError) {
    const hourValue = hourInput.value;
    const isValid = !isNaN(hourValue) && (parseInt(hourValue) >= 0 && parseInt(hourValue) < 24);

    if (!isValid && alertError) {
        alertError.innerHTML = ("0부터 23까지의 정수만 입력받을 수 있습니다.");
        hourInput.value = hourInput.value.slice(0, hourInput.value.length - 1);
    }
    console.log("isUnnvalid : " + isValid);

    return isValid;
}

function checkMinuteInputValid(minuteInput, alertError) {
    const minuteValue = minuteInput.value;
    const isValid = !isNaN(minuteValue) && (parseInt(minuteValue) >= 0 && parseInt(minuteValue) < 60);

    if (!isValid && alertError) {
        alertError.innerHTML = ("0부터 59까지의 정수만 입력받을 수 있습니다.");
        minuteInput.value = minuteInput.value.slice(0, minuteInput.value.length - 1);
    }

    console.log("isValid : " + isValid);

    return isValid;
}

function mkFormatHourAndMinute(time, hourEl, minuteEl) {
    const timeSplited = time.split(":");
    hourEl.value = timeSplited[0];
    minuteEl.value = timeSplited[1];
}

document.querySelector("button#save-change").addEventListener("click", updateBusinessHour);

async function updateBusinessHour() {
    const businessHours = [];
    let isValid = true;

    businessTimes.forEach(el => {
        const bhourId = el.querySelector("input#bhour-id")?.value;
        const bhourDay = el.querySelector("input#bhour-day")?.value;

        const inputStartHour = el.querySelector("input#input-start-hour");
        const inputStartMinute = el.querySelector("input#input-start-minute");
        const inputEndHour = el.querySelector("input#input-end-hour");
        const inputEndMinute = el.querySelector("input#input-end-minute");
        const isHoliday = el.querySelector("input#is-holiday-to-update").checked;

        let data = {
            bhourId: bhourId ?? null,
            day: bhourDay ?? null,
        }

        if (isHoliday) {
            data.isHoliday = isHoliday;
        } else {
            if (checkHourInputValid(inputStartHour) && checkHourInputValid(inputEndHour) && checkMinuteInputValid(inputStartMinute) && checkMinuteInputValid(inputEndMinute)) {
                data.isHoliday = isHoliday;
                data.startHour = inputStartHour.value;
                data.startMinute = inputStartMinute.value;
                data.endHour = inputEndHour.value;
                data.endMinute = inputEndMinute.value;
            } else {
                data = null;
            }
        }
        businessHours.push(data);
    })

    const {data, status} = await axios.patch(location.href + "/time",  businessHours);

    if (status === 200) {
        location.reload();
    } else {
        alert("시간 변경에 문제가 발생했습니다. 다시 시도해주세요")
    }
}
