"use strict";

// BASE_URL is gonna change depending on project name
const BASE_URL = "/Project_1/ERS/employee-verification";

let employeeLoginForm = document.getElementById("employee-login-form");
let employeeLoginSubmit = document.getElementById("login-submit");
let validationFail = document.getElementById("validation-fail");

employeeLoginSubmit.addEventListener("click", (event)=>{
    event.preventDefault();
    validationFail.innerHTML = "";
    fetch(BASE_URL, {
        method: "POST",
        body: JSON.stringify(createLoginInfo(employeeLoginForm))
    })
    .then((response)=>{
        if (response.status >= 200 && response.status < 300) {
            document.location.href = 'employee-main.html';
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

let createLoginInfo = (form)=>{
    let loginInfo = {};
    loginInfo.username = form.username.value;
    loginInfo.password = form.password.value;
    return loginInfo;
}