const dropList = document.querySelectorAll(".drop-list select"),
fromCurrency = document.querySelector(".from select"),
toCurrency = document.querySelector(".to select"),
swapIcon = document.querySelector(".icon i"),
getButton = document.querySelector("form button");

for (let i = 0; i < dropList.length; i++) {
    for (currency_code in country_list) {
        let selected;

        if (i == 0) {
            selected = currency_code == "USD" ? "selected" : "";
        } else if (i == 1) {
            selected = currency_code == "NPR" ? "selected" : "";
        }
        let optionTag = `<option value="${currency_code}" ${selected}>${currency_code}</option>`
        dropList[i].insertAdjacentHTML("beforeend", optionTag);
    }
    dropList[i].addEventListener('change', (e) => {
        loadFlag(e.target);
    });
}

function loadFlag(element) {
    let countryCode = country_list[element.value];
    let img = element.parentElement.querySelector("img");
    img.src = `https://flagsapi.com/${countryCode}/flat/64.png`;
}

async function getExchangeRate() {
    const amount = document.querySelector(".amount input");
    let exchangeText = document.querySelector(".exchange-rate");
    let amountVal = amount.value;

    if (amountVal == "" || amountVal == "0") {
        amount.value = "1";
        amountVal = 1;
    }

    let data = { fromCurrency: `${fromCurrency.value}`, toCurrency: `${toCurrency.value}`, amount: amountVal};

    exchangeText.innerText = "Getting exchange rate...";
    let response = await fetch("http://localhost:8080/CURRENCY_CONVERTOR_WEBAPP/service", {
            method: 'POST',
            headers: {
                'content-type': 'application/json',
            },
            body: JSON.stringify(data),
        }
    );

    let responseData = await response.json();

    let totalExchangeRate = (responseData.toAmount).toFixed(2);

    exchangeText.innerText = `${amountVal} ${fromCurrency.value} = ${totalExchangeRate} ${toCurrency.value}`;
}

window.addEventListener('load', () =>{
    getExchangeRate();
});

getButton.addEventListener('click', (e) =>{
    e.preventDefault();
    getExchangeRate();
});

function swap() {
    let temp = toCurrency.value;
    toCurrency.value = fromCurrency.value;
    fromCurrency.value = temp;
}

swapIcon.addEventListener('click', () => {
    swap();
    loadFlag(fromCurrency);
    loadFlag(toCurrency);
    getExchangeRate();
});
