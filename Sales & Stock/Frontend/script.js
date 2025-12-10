document.addEventListener("DOMContentLoaded", () => {
    // nav-Buttons:
    const homeBtn = document.getElementById("homeBtn");
    const salesBtn = document.getElementById("salesBtn");
    const inventoryBtn = document.getElementById("inventoryBtn");
    const productInfoBtn = document.getElementById("productInfoBtn");

    const homeSignupPage = document.getElementById("homeSignupPage");
    const homeDefaultPage = document.getElementById("homeDefaultPage");
    const inventoryTab = document.getElementById("inventoryTab");
    const salesTab = document.getElementById("salesTab");
    const productInfoTab = document.getElementById("productInfoTab");
    let currentHomePage = homeSignupPage;

    // changes==================
    const bellIconBtn = document.getElementById("bellIcon");
    const notificationBox = document.getElementById("notificationBox");
    const allTabBtns = [homeBtn, salesBtn, inventoryBtn, productInfoBtn];

    // home signup =================================================
    const submitAuthBtn = document.getElementById("submit");
    const AuthUsernameInp = document.getElementById("username");
    const AuthPswdInp = document.getElementById("password");

    // message box===================
    // const messageBox = document.getElementById("messageBox");
    const messagePara = document.getElementById("messagePara");

    // All content pages/tabs:
    // inventory operations
    const addProdBtn = document.getElementById("addProdBtn");
    const removeProdBtn = document.getElementById("removeProdBtn");
    const updateProdBtn = document.getElementById("updateProdBtn");

    const productNameInp = document.getElementById("prodName");
    const quantityInp = document.getElementById("prodQuantity");
    const priceInp = document.getElementById("prodPrice");

    // sales input fields
    const slsproductNameInp = document.getElementById("slsproductNameInp");
    const slsquantityInp = document.getElementById("slsquantityInp");
    const slspriceInp = document.getElementById("slspriceInp");

    const soldBtn = document.getElementById("soldBtn");
    const viewRecordsBtn = document.getElementById("viewRecordsBtn");

    // Product Info Tab
    const searchProdNameInp = document.getElementById("searchProdName");
    const searchProductBtn = document.getElementById("searchProductBtn");

    const divProdName = document.getElementById("divProdName");
    const divProdprice = document.getElementById("divProdprice");
    const divProdquantity = document.getElementById("divProdquantity");
    const divProdTime = document.getElementById("divProdTime");
    const divProdDate = document.getElementById("divProdDate");

    const displayUsername = document.getElementById("displayUsername");



    // storing data
    let userCredentials = [];
    let inventoryTabProdData = [];
    let salesTabProdData = [];

    const BASE_URL = "http://localhost:8081";

    // Array of all pages
    const allPages = [
        homeSignupPage,
        homeDefaultPage,
        inventoryTab,
        salesTab,
        productInfoTab,
    ];

    // =======================================================
    // =======================================================
    submitAuthBtn.addEventListener("click",  (e) => {
    e.preventDefault();

    const username = AuthUsernameInp.value.trim();
    const password = AuthPswdInp.value.trim();

    if (username === "" || password === "") {
        alert("Username and Password cannot be empty!");
        return;
    }

    const isAuthenticated = authenticateUserCredential([username, password]);

    if (isAuthenticated) {
        displayUsername.textContent = "@"+ username;
        showMessage("Login Successful");
        currentHomePage = homeDefaultPage;

        allTabBtns.forEach((btn) => {
            btn.disabled = false; // unlock tabs
        });

        showPage(currentHomePage);

    } else {
        showMessage("Invalid Credentials");
    }

    // Clear inputs
    AuthUsernameInp.value = "";
    AuthPswdInp.value = "";
});


    bellIconBtn.addEventListener("click", () => {
        bellIconBtn.classList.toggle("red");
        notificationBox.classList.toggle("hidden");
    });

    // const productNames = ["ParleG", "Apple", "Banana", "Mango"];

    async function renderNotificationAlerts() {
        const products = await getAlertableProducts();
        notificationBox.innerHTML = "";
        products.forEach((product) => {
            const newAlertItem = document.createElement("p");
            newAlertItem.classList.add("list-item");
            newAlertItem.innerHTML = `<span>${product}</span> is less than 50 in stock!`;

            notificationBox.appendChild(newAlertItem);
        });
    }

    renderNotificationAlerts();
    highlightClickedBtn(homeBtn);

    // Show default page
    showPage(currentHomePage);
    allTabBtns.forEach((btn) => {
        btn.disabled = true;
    });

    // Navigation control
    homeBtn.addEventListener("click", () => showPage(currentHomePage));
    salesBtn.addEventListener("click", () => showPage(salesTab));
    inventoryBtn.addEventListener("click", () => showPage(inventoryTab));
    productInfoBtn.addEventListener("click", () => showPage(productInfoTab));

    // INVENTORY wale EVENTS
    addProdBtn.addEventListener("click", () => {
        if (!productNameInp.value || !quantityInp.value || !priceInp.value) {
            alert("Fill all product fields");
            return;
        }

        inventoryTabProdData = {
            name: productNameInp.value.trim(),
            quantity: Number(quantityInp.value),
            price: Number(priceInp.value),
        };

        addProductToInventory(inventoryTabProdData);
        productNameInp.value = quantityInp.value = priceInp.value = "";
        showMessage("Products added successfully!");
        renderNotificationAlerts();
    });

    removeProdBtn.addEventListener("click", () => {
        if (!productNameInp.value) {
            alert("Product name required");
            return;
        }

        inventoryTabProdData = {
            name: productNameInp.value.trim(),
        };

        removeProductFromInventory(inventoryTabProdData);
        productNameInp.value = quantityInp.value = priceInp.value = "";
        showMessage("Products removed!");
        renderNotificationAlerts();
    });

    updateProdBtn.addEventListener("click", () => {
        if (!productNameInp.value) {
            alert("Product name required");
            return;
        }

        inventoryTabProdData = {
            name: productNameInp.value.trim(),
            quantity: Number(quantityInp.value),
            price: Number(priceInp.value),
        };

        updateProductToInventory(inventoryTabProdData);
        productNameInp.value = quantityInp.value = priceInp.value = "";
        showMessage("Product inventory updated successfully!");
        renderNotificationAlerts();
    });

    // SALES EVENTS
    soldBtn.addEventListener("click", () => {
        if (!slsproductNameInp.value || !slsquantityInp.value) {
            alert("Fill product name and quantity");
            return;
        }

        salesTabProdData = {
            name: slsproductNameInp.value.trim(),
            quantity: Number(slsquantityInp.value),
            price: slspriceInp.value ? Number(slspriceInp.value) : null,
        };

        modifyInventoryDB(salesTabProdData);
        slsproductNameInp.value = slsquantityInp.value = slspriceInp.value = "";
        showMessage("Product recorded in the sale book!");
        renderNotificationAlerts();
    });

    viewRecordsBtn.addEventListener("click", () => {
        openRecordsFolder();
        renderNotificationAlerts();
    });

    // SEARCH PRODUCT
    searchProductBtn.addEventListener("click", async () => {
        if (!searchProdNameInp.value) {
            alert("Please enter product name");
            return;
        }

        const prodToSearch = searchProdNameInp.value.trim();

        const fetchedProdDetails = await fetchProdDetailsFromDB(prodToSearch);
        console.log(fetchedProdDetails);
        if (!fetchedProdDetails) {
            alert("Product not found");
            return;
        }
        divProdName.textContent = fetchedProdDetails[0];
        divProdprice.textContent = fetchedProdDetails[1];
        divProdquantity.textContent = fetchedProdDetails[2];
        divProdTime.textContent = fetchedProdDetails[3];
        divProdDate.textContent = fetchedProdDetails[4];
    });

    // Page switching ke liye
    function showPage(targetPage) {
        allPages.forEach((page) => page.classList.add("hidden"));
        if (targetPage) {
            targetPage.classList.remove("hidden");
            // changes================
            switch (targetPage.id) {
                case "homeSignupPage":
                    highlightClickedBtn(homeBtn);
                    break;
                case "homeDefaultPage":
                    highlightClickedBtn(homeBtn);
                    break;
                case "inventoryTab":
                    highlightClickedBtn(inventoryBtn);
                    break;
                case "salesTab":
                    highlightClickedBtn(salesBtn);
                    break;
                case "productInfoTab":
                    highlightClickedBtn(productInfoBtn);
                    break;
                default:
                    break;
            }
            // =====================
        }
    }

    function openRecordsFolder() {
        window.location.href = "http://localhost:8081/inventory/download";
    }

    function addProductToInventory(prod) {
        const body = {
            product: prod.name.toLowerCase(),
            price: prod.price,
            quantity: prod.quantity,
        };

        fetch(`${BASE_URL}/inventory`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        })
            .then((res) => res.json())
            .then((data) => {
                showMessage("Product Added Successfully");
                console.log(data);
            })
            .catch((err) => console.error("Add Error:", err));
    }

    function removeProductFromInventory(prod) {
        fetch(`${BASE_URL}/inventory/${prod.name.toLowerCase()}`, {
            method: "DELETE",
        })
            .then((res) => res.text())
            .then((msg) => {
                showMessage(msg);
                console.log(msg);
            })
            .catch((err) => console.error("Delete Error:", err));
    }

    function authenticateUserCredential([username, password]) {
    return (username == "admin" && password == "admin123" )
        || ( username == "rishab" && password == "Rishab@123")
    }

    // async function authenticateUserCredential([username, password]) {

//     try {
//         const response = await fetch(`${BASE_URL}/auth/login`, {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({
//                 username: username.toLowerCase(),
//                 password: password
//             })
//         });

//         if (!response.ok) {
//             return false;
//         }

//         alert("Login successful");
//         return true;

//     } catch (error) {
//         console.error("Login Error:", error);
//         return false;
//     }
// }

    function updateProductToInventory(prod) {
        const body = {
            product: prod.name.toLowerCase(),
            price: prod.price,
            quantity: prod.quantity,
        };

        fetch(`${BASE_URL}/inventory/${prod.name.toLowerCase()}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        })
            .then((res) => res.json())
            .then((data) => {
                showMessage("Product Updated Successfully");
                console.log(data);
            })
            .catch((err) => console.error("Update Error:", err));
    }

    function modifyInventoryDB(sale) {
        const productName = sale.name.toLowerCase();

        const body = {
            product: productName,
            quantity: sale.quantity,
            price: sale.price || 0,
        };

        fetch(`${BASE_URL}/inventory/${productName}/${sale.quantity}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        })
            .then((res) => res.json())
            .then((data) => {
                showMessage("Inventory Updated After Sale");
                console.log(data);
            })
            .catch((err) => console.error("Sale Update Error:", err));
    }

    async function fetchProdDetailsFromDB(prodName) {
        try {
            const res = await fetch(
                `${BASE_URL}/inventory/${prodName.toLowerCase()}`
            );

            if (!res.ok) return null;

            const data = await res.json();

            return [
                data.product,
                data.price,
                data.quantity,
                data.time,
                data.date,
            ];
        } catch (err) {
            console.error("Search Error:", err);
            return null;
        }
    }

    async function getAlertableProducts() {
        try {
            const response = await fetch(
                "http://localhost:8081/inventory/alerts"
            );

            if (!response.ok) {
                alert("Failed to fetch alerts");
                return;
            }

            const alerts = await response.json();

            if (!alerts || alerts.length === 0) {
                alert("No alerts found");
                return;
            }

            return alerts;
        } catch (error) {
            console.error("Error fetching alerts:", error);
            alert("Something went wrong while fetching alerts.");
        }
    }

    function showMessage(message) {
        if (message != "") {
            messagePara.textContent = `${message}`;
        }
    }

    function highlightClickedBtn(clickedBtn) {
        allTabBtns.forEach((tabBtn) => {
            tabBtn.style.backgroundColor = "#fff";
            tabBtn.style.color = "#000";
        });
        clickedBtn.style.backgroundColor = "green";
        clickedBtn.style.color = "#fff";
    }


});
