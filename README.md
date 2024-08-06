# 📈 Проект "Currency Exchange"

## 📝 Описание проекта
"Currency Exchange" - это REST API для работы с валютами и обменными курсами. Проект реализует CRUD операции для валют и обменных курсов, а также предоставляет возможность конвертации валют.

## 💻 Технологии
В проекте используются следующие технологии:

- ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) **Java** - основной язык программирования.
- ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white) **Maven** - инструмент для управления зависимостями и сборки проекта.
- ![Servlets](https://img.shields.io/badge/Servlets-4B8BBE?style=for-the-badge&logo=java&logoColor=white) **Java Servlets** - для обработки HTTP-запросов.
- ![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white) **SQLite** - база данных для хранения информации о валютах и курсах.
- ![Tomcat](https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black) **Apache Tomcat** - сервер для развертывания веб-приложений.
- ![JDBC](https://img.shields.io/badge/JDBC-4479A1?style=for-the-badge&logo=java&logoColor=white) **JDBC** - для взаимодействия с базой данных.
- ![JSON](https://img.shields.io/badge/JSON-000000?style=for-the-badge&logo=json&logoColor=white) **JSON** - формат обмена данными.

## 🚀 Установка и запуск
1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/ssss1131/Currency-Exchanger.git
    ```
2. Перейдите в директорию проекта:
    ```bash
    cd Currency-Exchanger
    ```
3. Соберите проект с помощью Maven:
    ```bash
    mvn clean install
    ```
4. Запустите Tomcat и разверните приложение.

## 📑 Endpoints

### ➕ Создание валюты
```http
POST /currencies
```
Параметры:
- code - код валюты (например, USD)
- name - название валюты (например, United States Dollar)
- sign - символ валюты (например, $)

### ➕ Добавление нового обменного курса
```http
POST /exchangeRates
```
Параметры:
- baseCurrencyCode - USD
- targetCurrencyCode - EUR
- rate - 0.99

### 🔄 Обновление обменного курса
```http
PATCH /exchangeRate/{baseCurrencyCode}{targetCurrencyCode}
```
Параметры:
- rate - 1.2

### 🔄 Обмен валюты
```http
GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
```

Остальные эндпоинты можно посмотреть в rest-api.http со всеми запросами для теста

## 🧪 Тестирование с помощью rest-api.http
Для удобного тестирования эндпоинтов вы можете использовать файл rest-api.http, в котором прописаны примеры запросов. 
Просто откройте этот файл в Intellij IDEA и выполните запросы.


