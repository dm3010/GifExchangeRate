# Тестовая задача
Сервис получения GIF с giphy.com в зависимости от изменения дневного курса валюты (с openexchangerate.com)

# Endpoints
`/get/{currency}`

где currency - 3-х буквенный код валюты (RUB, EUR, и т.д.)

пример: `http://127.0.0.1:8080/get/rub`

результатом запроса будет произвольная GIF с сервиса giphy.com,

в случае роста курса со вчерашнего дня по запросу _rich_,

в случае понижения - по запросу _broke_

в случае ошибки в теле ответа будет пусто, код ответа транслируется от сервисов


# Настройка

основные настройки в файле application.properties
```ini
server.port=8080

openexchangerates.key=d1cab48c796546b6a3491f5347e11517
openexchangerates.url=https://openexchangerates.org/api
openexchangerates.base=USD

giphy.key=hidyT0yooJt3jmgfSpg9CsPge7xEEZqb
giphy.url=http://api.giphy.com/v1/gifs/random
giphy.tag.up=rich
giphy.tag.down=broke
```
