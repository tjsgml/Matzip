document.querySelector("button#excel-down-btn").addEventListener("click", async () => {
    location.href = location.origin + `/admin/excel/${resource}?${mkQueryString(query)}`;
})