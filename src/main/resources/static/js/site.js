document.addEventListener("DOMContentLoaded", function () {
    /** LOGIN FORM FUNCTIONALITY **/

    const loginForm = document.getElementById("login-form");
    const loginMessage = document.getElementById("login-message");

    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Спира презареждането на страницата

            const emailInput = document.getElementById("username").value;
            const passwordInput = document.getElementById("password").value;
            const userType = document.querySelector('input[name="user-type"]:checked').value;

            // Валидация на имейла (трябва да съдържа "@")
           // if (!emailInput.includes("@")) {
            //    loginMessage.textContent = "Моля, въведете валиден имейл адрес!";
             //   loginMessage.style.color = "red";
              //  return;
           //  }

            // Валидация на паролата (минимум 6 символа)
            if (passwordInput.length < 6) {
                loginMessage.textContent = "Паролата трябва да съдържа поне 6 символа!";
                loginMessage.style.color = "red";
                return;
            }

            // Успешен вход
            loginMessage.textContent = `Успешно влязохте като ${userType.toUpperCase()}!`;
            loginMessage.style.color = "green";
        });
    }

    /** MODAL (POP-UP) LOGIN FUNCTIONALITY **/
    const modal = document.getElementById("login-modal");
    const btn = document.getElementById("login-btn");
    const closeBtn = document.querySelector(".close");
    const modalLoginForm = document.getElementById("modal-login-form");
    const modalLoginMessage = document.getElementById("modal-login-message");

    if (btn && modal) {
        btn.addEventListener("click", () => {
            modal.style.display = "block";
        });

        closeBtn.addEventListener("click", () => {
            modal.style.display = "none";
        });

        window.addEventListener("click", (event) => {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        });
    }

    if (modalLoginForm) {
        modalLoginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const emailInput = document.getElementById("modal-email").value;
            const passwordInput = document.getElementById("modal-password").value;
            const userType = document.querySelector('input[name="modal-user-type"]:checked').value;

            if (!emailInput.includes("@")) {
                modalLoginMessage.textContent = "Please enter a valid email address!";
                modalLoginMessage.style.color = "red";
                return;
            }

            if (passwordInput.length < 6) {
                modalLoginMessage.textContent = "The password needs to have at least 6 symbols!";
                modalLoginMessage.style.color = "red";
                return;
            }

            modalLoginMessage.textContent = `Successfully logged in as ${userType.toUpperCase()}!`;
            modalLoginMessage.style.color = "green";

            console.log(`Email: ${emailInput}, Password: ${passwordInput}, Logged as: ${userType}`);
        });
    }

    /** VIEW SERVICES BUTTON FUNCTIONALITY **/
    const viewServicesButton = document.getElementById("viewServices");
    const servicesSection = document.getElementById("services");

    if (viewServicesButton && servicesSection) {
        viewServicesButton.addEventListener("click", function (event) {
            event.preventDefault(); // Предотвратява презареждането на страницата
            servicesSection.scrollIntoView({ behavior: "smooth", block: "start" });
        });
    }
});
// Function to filter shipments based on status
function filterShipments() {
    // Get the selected filter value
    let filterValue = document.getElementById("shipmentFilter").value;

    // Get the table and rows
    let table = document.getElementById("shipment-table");
    let rows = table.getElementsByTagName("tr");

    // Loop through all rows (except the header)
    for (let i = 1; i < rows.length; i++) {
        let statusCell = rows[i].getElementsByTagName("td")[2]; // Third column (Shipment Status)

        // Show row if it matches the filter, otherwise hide it
        if (filterValue === "all" || statusCell.innerText === filterValue) {
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }
}
function createShipment() {
    alert('Create Shipment button clicked!');
    // Add your logic for creating a shipment here
}