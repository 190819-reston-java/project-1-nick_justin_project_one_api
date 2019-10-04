"use strict";

const BASE_URL = "/Project_1/ERS";
const REIM_URL = `${BASE_URL}/reimbursement`;
let allTabs = document.querySelectorAll(".tab");
let employeeId;
let employeeInfo = document.getElementById("employee-info");
let employeeName = document.getElementById("employee-name");
let updateEmployeeInfo = document.getElementById("update-employee-info");
let updateInfoSubmit = document.getElementById("update-info-submit");
let updateStatus = document.getElementById("update-status");
let createReim = document.getElementById("create-reimbursement");
let pendingDisplay = document.getElementById("pending-display");
let resolvedDisplay = document.getElementById("resolved-display");
let logout = document.getElementById("logout");
let createReimSubmit = document.getElementById("create-reim-submit");
let fileField = document.querySelector('input[type="file"]');

for (let i = 0; i < allTabs.length; i++) {
    allTabs[i].addEventListener('click', function (event) {
        document.querySelector('.active').classList.remove('active');
        this.classList.add('active');

        let allContent = document.querySelectorAll("#content div");
        for (let i = 0; i < allContent.length; i++) {
            allContent[i].classList.add('hidden');
        };

        let id = this.dataset.content;
        document.querySelector(id).classList.remove('hidden');
        let idContent = document.querySelector(id).children;
        for (let i = 0; i < idContent.length; i++) {
            idContent[i].classList.remove('hidden');
        };
    })
};

window.addEventListener("load", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/employee-view-info`, {
        method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((employeeNameJson) => {
        employeeName.innerText = "";
        employeeName.innerText = employeeNameJson["firstName"];
        employeeId = employeeNameJson["id"];
    })
    .catch(console.error)
});

allTabs[2].addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${REIM_URL}/${employeeId}`, {
        method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((reimbursementsJson) => {
        pendingDisplay.innerHTML = "";
        for (let i = 0; i < reimbursementsJson.length; i++) {
            if (reimbursementsJson[i]["status"] === "PENDING") {
                let newTr = document.createElement("tr");
                let dateRequested = document.createElement("td");
                let amount = document.createElement("td");
                let status = document.createElement("td");
                dateRequested.innerText = new Date(reimbursementsJson[i]["sqlDate"]).toLocaleDateString();
                amount.innerText = reimbursementsJson[i]["amount"];
                status.innerText = reimbursementsJson[i]["status"];
                pendingDisplay.appendChild(newTr);
                newTr.appendChild(dateRequested);
                newTr.appendChild(amount);
                newTr.appendChild(status);
            }
        }
    })
    .catch(console.log)
});

allTabs[3].addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${REIM_URL}/${employeeId}`, {
        method: "GET" 
    })
    .then((response) => {
        return response.json();
    })
    .then((reimbursementsJson) => {
        resolvedDisplay.innerHTML = "";
        for (let i = 0; i < reimbursementsJson.length; i++) {
            if (reimbursementsJson[i]["status"] !== "PENDING") {
                let newTr = document.createElement("tr");
                let dateRequested = document.createElement("td");
                let amount = document.createElement("td");
                let status = document.createElement("td");
                dateRequested.innerText = new Date(reimbursementsJson[i]["sqlDate"]).toLocaleDateString();
                amount.innerText = reimbursementsJson[i]["amount"];
                status.innerText = reimbursementsJson[i]["status"];
                resolvedDisplay.appendChild(newTr);
                newTr.appendChild(dateRequested);
                newTr.appendChild(amount);
                newTr.appendChild(status);
            }
        }
    })
    .catch(console.log)
});

createReimSubmit.addEventListener("click", (event) => {
    event.preventDefault();
    let formData = new FormData();
    formData.append("receipt", fileField.files[0]);
    formData.append("amount", createReim.amount.value);
    fetch(`${BASE_URL}/upload-receipt`, {
        method: "POST",
        body: formData
    })
    .then((response) => {
        console.log(response);
    })
    .catch(console.error)
});

allTabs[4].addEventListener("click", (event) => {
    event.preventDefault();
    updateStatus.innerHTML = "";
    fetch(`${BASE_URL}/employee-view-info`, {
        method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((employeeInfoJson) => {
        employeeInfo.innerHTML = "";
        let firstName = document.createElement("td");
        let lastName = document.createElement("td");
        let email = document.createElement("td");
        let address = document.createElement("td");
        firstName.innerText = employeeInfoJson["firstName"];
        lastName.innerText = employeeInfoJson["lastName"];
        email.innerText = employeeInfoJson["email"];
        address.innerText = employeeInfoJson["address"];
        employeeInfo.appendChild(firstName);
        employeeInfo.appendChild(lastName);
        employeeInfo.appendChild(email);
        employeeInfo.appendChild(address);
        employeeInfo.addEventListener("click", (event) => {
            updateEmployeeInfo.hidden = false;
            updateEmployeeInfo.firstname.value = employeeInfoJson["firstName"];
            updateEmployeeInfo.lastname.value = employeeInfoJson["lastName"];
            updateEmployeeInfo.email.value = employeeInfoJson["email"];
            updateEmployeeInfo.address.value = employeeInfoJson["address"];
        });
    })
    .catch(console.error)
});

updateInfoSubmit.addEventListener("click", (event) => {
    event.preventDefault();
    updateStatus.innerHTML = "";
    fetch(`${BASE_URL}/employee-update-info`, {
        method: "POST",
        body: JSON.stringify(updateInfo(updateEmployeeInfo))
    })
    .then((response) => {
        if (response.status >= 200 && response.status < 300) {
            updateStatus.innerHTML = "<p>Information has successfully updated. Please refresh</p>";
            updateStatus.style.color = "red";
            updateStatus.style.fontWeight = "bold";
        } else {
            updateStatus.innerHTML = "<p>Failed to update information</p>";
            updateStatus.style.color = "red";
            updateStatus.style.fontWeight = "bold";
        }
        updateEmployeeInfo.hidden = true;
    })
    .catch((error) => {
    updateStatus.innerHTML = "<p>Failed to update information. Please try again</p>";
    updateStatus.style.color = "red";
    updateStatus.style.fontWeight = "bold";
    })
});

logout.addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/logout`, {
        method: "GET"
    })
    .then((response) => {
        if (response.status >= 200 && response.status < 300) {
            document.location.href = "index.html";
        } else {
            alert("Failed to logout. Please try again.");
        }
    })
    .catch(console.log(error))
})

let updateInfo = (form) => {
    let newInfo = {};
    newInfo.firstName = form.firstname.value;
    newInfo.lastName = form.lastname.value;
    newInfo.email = form.email.value;
    newInfo.address = form.address.value;
    return newInfo;
};

let reimFromForm = (form) => {
     let reim = {};
     // Set to zero to allow default value
     reim.id = 0;
     // Get employee id from session info
     reim.employeeId = employeeId;
     reim.amount = form["reimbursement-amount"].value;
     reim.sqlDate = Date.now();
     reim.status = "PENDING";
     reim.approvingManager = null;
     return reim;
};