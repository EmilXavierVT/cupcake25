function increaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.textContent, 10); // Convert to number
    counter.textContent = x + 1;
}

function decreaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.textContent, 10); // Convert to number
    counter.textContent = x - 1;
}

