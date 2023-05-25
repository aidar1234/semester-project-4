function addTransportFavorite() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
           alert('Объявление было добалено в избранное');
        } else if (this.readyState === 4 && this.status === 406) {
            alert('Объявление уже в избранном');
        }
    };
    xhttp.open("GET", "/profile/favorite/transport/add/" + id, true);
    xhttp.send();
}

function removeTransportFavorite() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert('Объявление было удалено, вернитесь к списку объявлений');
        }
    };
    xhttp.open("GET", "/profile/favorite/transport/remove/" + id, true);
    xhttp.send();
}

function addElectronicsFavorite() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert('Объявление было добалено в избранное');
        } else if (this.readyState === 4 && this.status === 406) {
            alert('Объявление уже в избранном');
        }
    };
    xhttp.open("GET", "/profile/favorite/electronics/add/" + id, true);
    xhttp.send();
}

function removeElectronicsFavorite() {
    id = document.getElementById('id').value;
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert('Объявление было удалено, вернитесь к списку объявлений');
        }
    };
    xhttp.open("GET", "/profile/favorite/electronics/remove/" + id, true);
    xhttp.send();
}

