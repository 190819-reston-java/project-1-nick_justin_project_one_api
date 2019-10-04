"use strict";

const BASE_URL = "/Project_1/ERS/manager-verification";
let managerLoginSubmit = document.getElementById("login-submit");
let managerLoginForm = document.getElementById("manager-login-form");
let validationFail = document.getElementById("validation-fail");


managerLoginSubmit.addEventListener("click", (event) => {
    event.preventDefault();
    validationFail.innerHTML = "";
    fetch(BASE_URL, {
        method: "POST",
        body: JSON.stringify(createLoginInfo(managerLoginForm))
    })
    .then((response) => {
        if (response.status >= 200 && response.status < 300) {
            document.location.href = 'manager-main.html';
        } else if (response.status === 403) {
            validationFail.innerHTML = "<p>Not a manager. You do not have access to this content.</p>";
            validationFail.style.color = "red";
            validationFail.style.fontWeight = "bold";
        } else {
            validationFail.innerHTML = "<p>Incorrect username or password</p>";
            validationFail.style.color = "red";
            validationFail.style.fontWeight = "bold";
        }
    })
    .catch((error) => {
        validationFail.innerHTML = "<p>Failed to login. Please try again</p>";
        validationFail.style.color = "red";
        validationFail.style.fontWeight = "bold";
    })
});

let createLoginInfo = (form) => {
    let loginInfo = {};
    loginInfo.username = form.username.value;
    loginInfo.password = form.password.value;
    return loginInfo;
};