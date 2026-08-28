# GestioEPIs

Internal web app prototype for managing the company's Personal Protective Equipment (PPE) inventory: clothing and gear assigned to workers, by location.

## Features

- **Inventory** — full list of items (clothing, sizes, brand, location), filterable by location.
- **Purchases and orders** — supplier order tracking, with associated delivery notes.
- **Handovers** — track which items were given to which worker and when.
- **Home dashboard** — quick overview of activity (stats and latest movements).
- **Settings** — catalog management (categories, subcategories, locations).

## Tech Stack

- Java 25 + Spring Boot 4.1 (Spring Data JPA, Thymeleaf)
- Bootstrap 5
- H2 (local file-based database) + Flyway for migrations

## Running locally (development)

Requires Java 25. The included Gradle Wrapper means you don't need Gradle installed:

```bash
./gradlew bootRun
```

The app is then available at `http://localhost:8090`.

## Running it on a user's computer (desktop mode, no install required)

To distribute the app to an end user without them needing to install Java or anything else, there's a script that generates a portable folder (bundling the app with its own Java runtime):

```bash
.\scripts\build-portable.ps1
```

This creates `dist\GestioEPIs`, ready to zip and share. The user just needs to unzip it and double-click `Iniciar-GestioEPIs.bat`; the browser opens automatically. Data is stored in `%USERPROFILE%\GestioEPIs`, outside the app folder, so newer versions can be dropped in without losing it.

## Project structure

- `controllers/` — web routes and page logic.
- `models/` — JPA entities (`ClothingItem`, `Person`, `PurchaseOrder`, `Handover`, `Category`...).
- `repositories/` — data access (Spring Data JPA).
- `services/` — business logic (e.g. file storage).
- `resources/templates/` — Thymeleaf views.
- `resources/db/migration/` — database migrations (Flyway).
