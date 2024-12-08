const container = document.querySelector('.container');
const search = document.querySelector('.search-box button');
const weatherBox = document.querySelector('.weather-box');
const weatherDetails = document.querySelector('.weather-details');
const error404 = document.querySelector('.not-found');

search.addEventListener('click',async () => {
    const city = document.querySelector('.search-box input').value;
    let data = {city: `${city}`};

    let response = await fetch("http://localhost:8080/WEATHER_WEBAPP/service",{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })

    let weatherData = await response.json();

    if (weatherData.code === 404) {
        container.style.height = '400px';
        weatherBox.style.display = 'none';
        weatherDetails.style.display = 'none';
        error404.style.display = 'block';
        error404.classList.add('fadeIn');
        return;
    }

    error404.style.display = 'none';
    error404.classList.remove('fadeIn');

    const image = document.querySelector('.weather-box img');
    const temperature = document.querySelector('.weather-box .temperature');
    const description = document.querySelector('.weather-box .description');
    const humidity = document.querySelector('.weather-details .humidity span');
    const wind = document.querySelector('.weather-details .wind span');

    switch(weatherData.weather) {
        case 'Clear':
            image.src = './image/clear.png';
            break;

        case 'Rain':
            image.src = './image/rain.png';
            break;

        case 'Snow':
            image.src = './image/snow.png';
            break;

        case 'Clouds':
            image.src = './image/cloud.png';
            break;

        case 'Haze':
            image.src = './image/mist.png';
            break;

        default:
            image.src = '';
    }

    temperature.innerHTML = `${weatherData.temperature}<span>°C</span>`;
    description.innerHTML = `${weatherData.description}`;
    humidity.innerHTML = `${weatherData.humidity}`;
    wind.innerHTML = `${weatherData.wind}`;

    weatherBox.style.display = '';
    weatherDetails.style.display = '';
    weatherBox.classList.add('fadeIn');
    weatherDetails.classList.add('fadeIn');
    container.style.height = '590px';
})