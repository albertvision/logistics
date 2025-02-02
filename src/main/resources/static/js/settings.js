document.addEventListener("DOMContentLoaded", function () {
    addService(); // Adds an initial service box on page load
});

function addService() {
    const servicesList = document.getElementById("services-list");

    // Create Service Box
    const serviceBox = document.createElement("div");
    serviceBox.classList.add("service-box");

    serviceBox.innerHTML = `
        <label>Service Type:</label>
        <select>
            <option value="Standard">Standard</option>
            <option value="Express">Express</option>
            <option value="Overnight">Overnight</option>
        </select>

        <label>Min Weight (g):</label>
        <input type="number" min="0" placeholder="Enter min weight">

        <label>Base Price (EUR):</label>
        <input type="number" min="0" step="0.01" placeholder="Enter base price">

        <label>Price (EUR) per Gram:</label>
        <input type="number" min="0" step="0.01" placeholder="Enter price per gram">
    `;

    servicesList.appendChild(serviceBox);
}

function removeService() {
    const servicesList = document.getElementById("services-list");
    const lastService = servicesList.lastElementChild; // Get the last added service

    if (lastService) {
        servicesList.removeChild(lastService); // Remove only the last service
    }
}