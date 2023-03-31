# README #

Desafio técnico para professor na Ada

## Tecnologias envolvidas

* Java 17
* Spring boot 3.5
* JWT (auth0)

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