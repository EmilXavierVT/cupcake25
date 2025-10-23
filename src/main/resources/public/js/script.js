let amountOfCupcake = 1;


function increaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.value, 10); // Convert to number
    counter.value = x + 1;
    amountOfCupcake = x + 1;
}

function decreaseOne() {
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.value, 10); // Convert to number
    counter.value = x - 1;
    amountOfCupcake = x - 1;
}

function resetAmountOfCupcakes() {
    amountOfCupcake = 1;
    var counter = document.getElementById("counter_number");
    var x = parseInt(counter.value, 10); // Convert to number
    counter.value = 1;

}

function addToOrder() {
    fetch("/add-to-order", {
        method: "POST"

    })
        .then(response => {
            if (response.ok) {
                alert("Session attribute set or activated!");
            } else {
                alert("Failed to activate session attribute.");
            }
        });
}

// this makes us call the function at the event click
document.getElementById("activate-session-btn").addEventListener("click", addToOrder);
