# Requirements

### Features

- Focus on building a data-centric application. This means that the application's primary goal
  should be managing and processing data. Data-centric applications are often used in fields such as
  finance, healthcare, or scientific research, just to mention a few.
- The user interface and other features should be designed to support these data-centric operations.
- The application should manage its users and provide register/login functionalities.

### Tech

- The application's backend should be written in Java as a Spring Boot Web API.
- The data management should be handled with Spring Data JPA and a PostgreSQL database.
- The user interface should be written in React.

# Tasx - Business Task Manager Web App

### Features

- "userek"
- regisztráció cégként
- cégen belül szerepkörök, a céget regisztráló user az (első) admin
  - alkalmazott
  - szerkesztő
  - admin
  - root / superuser: a létrehozó

- projekt
  - az alkalmazott tud projektet létrehozni
  - a projekt létrehozója vagy az editor tudja szerkeszteni a projektet és a taskokat
  - határidő (tól-ig)
  - taskok (checklist)
    - a projekthez rendelt emberek + editor tudják létrehozni, szerkeszteni
    - ki csinálta
    - mi kell hozzá: eszközök
      - név, ár --> költség
    - határidő (tól-ig)
    - status: nincs elkezdve, elkezdve, siker, nem siker
  - progress taskokból
  - prioritás must have, nth
  - status: nincs elkezdve, elkezdve, siker, nem siker
  - kifizetve, nincs kifizetve

- alkalmazott:
  - aktivitás: min és mennyit dolgozott
  - mikor és mennyit dolgozik (szabadság, rész / teljes munkaidő stb)

- szerkesztő:
  - projektek taskok szerkesztése

- admin
  - userek managelése
  - jutalmazás, statisztika

- naptár integráció: google?