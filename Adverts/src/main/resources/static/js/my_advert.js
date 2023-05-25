function removeTransportAdvert() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert('Объявление было удалено, вернитесь к списку объявлений');
        }
    };
    xhttp.open("GET", "/profile/adverts/transport/remove/" + id, true);
    xhttp.send();
}

function removeElectronicsAdvert() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert('Объявление было удалено, вернитесь к списку объявлений');
        }
    };
    xhttp.open("GET", "/profile/adverts/electronics/remove/" + id, true);
    xhttp.send();
}
