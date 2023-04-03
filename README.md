# README #

Desafio técnico para professor na Ada Tech

## Tecnologias envolvidas

* Java 17
* Spring boot 3.5
* JWT (auth0)

## Documentação

* O projeto está documentado com Swagger `/api/public/swagger-ui/index.html`

## Inicialização

#### Durante a inicialização o projeto adiciona 3 usuários

* username: `izuku_midoriya` password: `izuku_midoriya`
* username: `katsuki_bakugo` password: `katsuki_bakugo`
* username: `shoto_todoroki` password: `shoto_todoroki`

## Autenticação

* (POST) http://localhost:8080/api/public/v1/user/auth
* O token será retornano em um header chamado Authorization
* ex: Authorization: Bearer {token}

```
{
    "username": "izuku_midoriya",
    "password": "izuku_midoriya"
}
```

## Como jogar

* Você deve se autenticar em `<POST> /api/public/v1/user/auth`
* Para iniciar um novo Quiz (Quiz se encerra após 3 erros) `<POST> /api/v1/quiz`
* Para responder um Quiz `<POST> /api/v1/quiz/{id}`
* Para encerrar um Quiz (antes dos 3 erros) `<POST> /api/v1/quiz/{id}/close`
* Para consultar o ranking `<GET> /api/v1/quiz/ranking`