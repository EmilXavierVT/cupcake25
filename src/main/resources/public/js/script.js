window.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.setAttribute('data-theme', 'dark');
    }
});


let amountOfCupcake = 1;


function increaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.value, 10); // Convert to number
    counter.value = x + 1;
    amountOfCupcake = x + 1;
}

function decreaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.value, 10); //
    if(x>1) {// Convert to number
        counter.value = x - 1;
        amountOfCupcake = x - 1;
    }
}



//Changes image on product-page
document.addEventListener("DOMContentLoaded", function () {
    const bottomSelect = document.getElementById("bottomSelect");
    const icingSelect = document.getElementById("icingSelect");

    const bottomImg = document.getElementById("bottomImage");
    const icingImg = document.getElementById("icingImage");

    const icingPreview = document.getElementById("preview_icing");
    const bottomPreview = document.getElementById("preview_bottom");

    function updateImage(select, img, previewImg) {
        const selectedOption = select.options[select.selectedIndex];
        const imageUrl = selectedOption.getAttribute("data-image");
        if (imageUrl) {
            img.src = imageUrl;
            previewImg.src = imageUrl;
        }else {
            console.log("no image path ");
        }
        if(!img){
            console.log("no image in html");
        }
        if (!selectedOption) {
            console.log("no selected option");
        }
        console.log(
            "selectedOption: ",
            selectedOption,
            "selectedOption.value: ",
            selectedOption.value,
            "selectedOption.getAttribute('data-image'): ",
            selectedOption.getAttribute("data-image")
        )
    }

    bottomSelect.addEventListener("change", () => updateImage(bottomSelect, bottomImg, bottomPreview));
    icingSelect.addEventListener("change", () => updateImage(icingSelect, icingImg, icingPreview));

});

function toggleDarkMode() {
    const body = document.body;
    const isDark = body.getAttribute('data-theme') === 'dark';
    if (isDark) {
        body.removeAttribute('data-theme');
        localStorage.setItem('theme', 'light');
    } else {
        body.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark');
    }


}