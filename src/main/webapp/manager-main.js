"use strict";

const BASE_URL = "/Project_1/ERS";
let allTabs = document.querySelectorAll(".tab");
let managerName = document.getElementById("manager-name");
let logout = document.getElementById("logout");
let viewAllEmployees = document.getElementById("all-employees");
let pendingTable = document.getElementById("pending-table");
let resolvedTable = document.getElementById("resolved-table");
let manageRequestsForm = document.getElementById("manage-requests-form");
let manageRequestsStatus = document.getElementById("manage-requests-status");
let approveRequest = document.getElementById("approve-request");
let denyRequest = document.getElementById("deny-request");
let employeeReimbursementsId = document.getElementById("employee-reimbursements-id");
let employeeReimbursements = document.getElementById("employee-reimbursements-submit");
let employeeReims = document.getElementById("employee-reims");

for (let i = 0; i < allTabs.length; i++) {
    allTabs[i].addEventListener('click', function (event) {
        document.querySelector('.active').classList.remove('active');
        this.classList.add('active');

        let allContent = document.querySelectorAll("#content div");
        for (let i = 0; i < allContent.length; i++) {
            allContent[i].classList.add('hidden');
        }

        let id = this.dataset.content;
        document.querySelector(id).classList.remove('hidden');
        let idContent = document.querySelector(id).children;
        for (let i = 0; i < idContent.length; i++) {
            idContent[i].classList.remove('hidden');
        }
    })
}

let employeesJsonPromise =
    fetch(`${BASE_URL}/view-employees`, {
        method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .catch(console.error);

window.addEventListener("load", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/employee-view-info`, {
            method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((employeeNameJson) => {
        managerName.innerText = "";
        managerName.innerText = employeeNameJson["firstName"];
    })
    .catch(console.error)
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
});

allTabs[1].addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/view-employees`, {
        method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((employeesJson) => {
        viewAllEmployees.innerHTML = "";
        for (let i = 0; i < employeesJson.length; i++) {
            if (employeesJson[i]["companyPosition"] === "employee") {
                let newTr = document.createElement("tr");
                let id = document.createElement("td");
                let firstName = document.createElement("td");
                let lastName = document.createElement("td");
                let email = document.createElement("td");
                let address = document.createElement("td");
                id.innerText = employeesJson[i]["id"];
                firstName.innerText = employeesJson[i]["firstName"];
                lastName.innerText = employeesJson[i]["lastName"];
                email.innerText = employeesJson[i]["email"];
                address.innerText = employeesJson[i]["address"];
                viewAllEmployees.appendChild(newTr);
                newTr.appendChild(id);
                newTr.appendChild(firstName);
                newTr.appendChild(lastName);
                newTr.appendChild(email);
                newTr.appendChild(address);
            }
        }
    })
    .catch(console.log)
})

allTabs[2].addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/reimbursement`, {
            method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((reimbursementsJson) => {
        pendingTable.innerHTML = "";
        for (let reimbursement in reimbursementsJson) {
            if (reimbursementsJson[reimbursement].status === "PENDING") {
                let tableRow = document.createElement("tr");
                let id = document.createElement("td");
                let employeeId = document.createElement("td");
                let firstName = document.createElement("td");
                let lastName = document.createElement("td");
                let email = document.createElement("td");
                let dateSubmitted = document.createElement("td");
                let amount = document.createElement("td");
                let image = document.createElement("td");

                id.innerText = reimbursementsJson[reimbursement].id;
                employeeId.innerText = reimbursementsJson[reimbursement].employeeId;
                Promise.resolve(employeesJsonPromise).then((json) => {
                    firstName.innerText = json[employeeId.innerText].firstName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    lastName.innerText = json[employeeId.innerText].lastName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    email.innerText = json[employeeId.innerText].email
                });
                dateSubmitted.innerText = new Date(reimbursementsJson[reimbursement].sqlDate).toLocaleDateString();
                amount.innerText = reimbursementsJson[reimbursement].amount;
                image.innerText = reimbursementsJson[reimbursement].employeeId;

                pendingTable.appendChild(tableRow);
                tableRow.appendChild(id);
                tableRow.appendChild(employeeId);
                tableRow.appendChild(firstName);
                tableRow.appendChild(lastName);
                tableRow.appendChild(email);
                tableRow.appendChild(dateSubmitted);
                tableRow.appendChild(amount);
                tableRow.appendChild(image);
            }
        }
    })
    .catch(console.error)
});

allTabs[3].addEventListener("click", (event) => {
    event.preventDefault();
    fetch(`${BASE_URL}/reimbursement`, {
            method: "GET"
    })
    .then((response) => {
        return response.json();
    })
    .then((reimbursementsJson) => {
        resolvedTable.innerHTML = "";
        for (let reimbursement in reimbursementsJson) {
            if (reimbursementsJson[reimbursement].status !== "PENDING") {
                let tableRow = document.createElement("tr");
                let id = document.createElement("td");
                let firstName = document.createElement("td");
                let lastName = document.createElement("td");
                let email = document.createElement("td");
                let dateSubmitted = document.createElement("td");
                let amount = document.createElement("td");
                let status = document.createElement("td");
                let approvingManager = document.createElement("td");
                let image = document.createElement("td");

                let employeeId = reimbursementsJson[reimbursement].employeeId;
                id.innerText = reimbursementsJson[reimbursement].id;
                Promise.resolve(employeesJsonPromise).then((json) => {
                    firstName.innerText = json[employeeId].firstName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    lastName.innerText = json[employeeId].lastName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    email.innerText = json[employeeId].email
                });
                dateSubmitted.innerText = new Date(reimbursementsJson[reimbursement].sqlDate).toLocaleDateString();
                amount.innerText = reimbursementsJson[reimbursement].amount;
                status.innerText = reimbursementsJson[reimbursement].status;
                approvingManager.innerText = reimbursementsJson[reimbursement].approvingManager;
                image.innerText = reimbursementsJson[reimbursement].employeeId;

                resolvedTable.appendChild(tableRow);
                tableRow.appendChild(id);
                tableRow.appendChild(firstName);
                tableRow.appendChild(lastName);
                tableRow.appendChild(email);
                tableRow.appendChild(dateSubmitted);
                tableRow.appendChild(amount);
                tableRow.appendChild(status);
                tableRow.appendChild(approvingManager);
                tableRow.appendChild(image);
            }
        }
    })
    .catch(console.error)
});

employeeReimbursements.addEventListener("click", (event) => {
    event.preventDefault();
    employeeReims.innerHTML = "";
    fetch(`${BASE_URL}/employee-reimbursements`, {
        method: "POST",
        body: JSON.stringify(employeeReimId(employeeReimbursementsId))
    })
    .then((response) => {
        return response.json();
    })
    .then ((employeesReimbursementsJson) => {
        for (let reimbursement in employeesReimbursementsJson) {
            if (employeesReimbursementsJson[reimbursement].status === "PENDING") {
                let tableRow = document.createElement("tr");
                let id = document.createElement("td");
                let firstName = document.createElement("td");
                let lastName = document.createElement("td");
                let email = document.createElement("td");
                let dateSubmitted = document.createElement("td");
                let amount = document.createElement("td");
                let image = document.createElement("td");

                let employeeId = employeesReimbursementsJson[reimbursement].employeeId;
                id.innerText = employeesReimbursementsJson[reimbursement].id;
                Promise.resolve(employeesJsonPromise).then((json) => {
                    firstName.innerText = json[employeeId].firstName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    lastName.innerText = json[employeeId].lastName
                });
                Promise.resolve(employeesJsonPromise).then((json) => {
                    email.innerText = json[employeeId].email
                });
                dateSubmitted.innerText = new Date(employeesReimbursementsJson[reimbursement].sqlDate).toLocaleDateString();
                amount.innerText = employeesReimbursementsJson[reimbursement].amount;
                image.innerText = employeesReimbursementsJson[reimbursement].employeeId;

                employeeReims.appendChild(tableRow);
                tableRow.appendChild(id);
                tableRow.appendChild(firstName);
                tableRow.appendChild(lastName);
                tableRow.appendChild(email);
                tableRow.appendChild(dateSubmitted);
                tableRow.appendChild(amount);
                tableRow.appendChild(image);
            }
        }
    })
    .catch(console.error)
})

approveRequest.addEventListener("click", (event) => {
    event.preventDefault();
    manageRequestsStatus.innerHTML = "";
    fetch(`${BASE_URL}/update-reimbursement`, {
        method: "POST",
        body: JSON.stringify(approveReim(manageRequestsForm))
    })
    .then((response) => {
        if (response.status >= 200 && response.status < 300) {
            manageRequestsStatus.innerHTML = "<p>Reimbursement has been successfully approved</p>";
            manageRequestsStatus.style.color = "red";
            manageRequestsStatus.style.fontWeight = "bold";
        } else {
            manageRequestsStatus.innerHTML = "<p>Failed to approve reimbursement</p>";
            manageRequestsStatus.style.color = "red";
            manageRequestsStatus.style.fontWeight = "bold";
        }
    })
    .catch((error) => {
        manageRequestsStatus.innerHTML = "<p>Failed to update reimbursements. Please try again</p>";
        manageRequestsStatus.style.color = "red";
        manageRequestsStatus.style.fontWeight = "bold";
    })
});

denyRequest.addEventListener("click", (event) => {
    event.preventDefault();
    manageRequestsStatus.innerHTML = "";
    fetch(`${BASE_URL}/update-reimbursement`, {
            method: "POST",
            body: JSON.stringify(denyReim(manageRequestsForm))
        })
        .then((response) => {
            if (response.status >= 200 && response.status < 300) {
                manageRequestsStatus.innerHTML = "<p>Reimbursement has been successfully denied</p>";
                manageRequestsStatus.style.color = "red";
                manageRequestsStatus.style.fontWeight = "bold";
            } else {
                manageRequestsStatus.innerHTML = "<p>Failed to deny reimbursement</p>";
                manageRequestsStatus.style.color = "red";
                manageRequestsStatus.style.fontWeight = "bold";
            }
        })
        .catch((error) => {
            manageRequestsStatus.innerHTML = "<p>Failed to update reimbursements. Please try again</p>";
            manageRequestsStatus.style.color = "red";
            manageRequestsStatus.style.fontWeight = "bold";
        })
});

let approveReim = (form) => {
    let updatedReimInfo = {};
    updatedReimInfo.id = form.reimbursementId.value;
    updatedReimInfo.status = "APPROVED";
    return updatedReimInfo;
}

let denyReim = (form) => {
    let updatedReimInfo = {};
    updatedReimInfo.id = form.reimbursementId.value;
    updatedReimInfo.status = "DENIED";
    return updatedReimInfo;
}

let employeeReimId = (form) => {
    let employeeId = {};
    employeeId.id = form.employeeId.value;
    return employeeId;
}