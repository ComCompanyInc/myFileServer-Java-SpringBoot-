const SIGN_IN = "/signIn";

/*
 * {
        "login": "fff123",
        "password": "123",
        "role": {
            "name": "ADMIN"
        }
    }
 */
//Массив json для передачи на сервер
userData = {
    role: {
        name: "USER"
    }
};

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    
    registrationButton = document.getElementById("buttonReg");
    
    registrationButton.addEventListener('click', function() {
        //alert('Кнопка нажата через addEventListener!');
        //console.log('Событие обработано');

        loginField = document.getElementById("login");
        passwordField = document.getElementById("password");

        //задаем в обьект значения из полей
        userData.login = loginField.value;
        userData.password = passwordField.value;
        
        console.log("DDDATA-> " + JSON.stringify(userData));
    
        putDataToServer(userData);
    });
});

function putDataToServer(data) {
    // Отправка на сервер
    fetch(Config.API_BASE + SIGN_IN, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.text()/*json()*/) //выбираем ответ в виде текста от сервера, а не json
    .then(data => {
        console.log('Успех:', data);
        document.getElementById("result").textContent = data;
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}

