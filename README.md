![poster](https://raw.githubusercontent.com/qaxperience/thumbnails/main/playwright-zombie.png)

## 🤘 About

Repository for the automated testing project of the Zombie Plus system! 

## 💻 Technologies
- Java 21
- Maven 3.9
- Selenium 4.21
- TestNG 7.10
- JavaFaker
- PostgreSQL
- Jackson
- WebDriver Manager
- Github Actions

## 💻 Prerequisites
These technologies are needed to be able to build the project correctly 
- Node.js
- NPM
- Java
- Maven


## 🤖 How to run

1. Clone the repository and install dependencies for application on folder **application/web** and **application/api**
```
npm install
```

2. Start the application locally. It is necessary to run this for Backend Layer and Frontend Layer
```
npm run dev
```

3. Install Java dependencies and run tests
```
mvn clean install
```

4. Run tests
```
mvn test
```

## Project architecture

This project uses Page Object Model Design pattern, which involves creating classes based on your application's pages. Each class is responsible for all methods related to its page.
It is implemented to run on Github Actions by triggering manually.


