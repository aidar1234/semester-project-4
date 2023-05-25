function onPhoneCheckboxClick(checkbox) {
    if (checkbox.checked) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                document.getElementById("phone").value = this.responseText;
            }
        };
        xhttp.open("GET", "/profile/phone", true);
        xhttp.send();
    }
}

function onLocalityCheckboxClick(checkbox) {
    if (checkbox.checked) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                document.getElementById("locality").value = this.responseText;
            }
        };
        xhttp.open("GET", "/profile/locality", true);
        xhttp.send();
    }
}
