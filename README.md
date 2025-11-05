# HitachiExam

# Parking Management System

## Overview
A brief description of your Parking Management System, e.g.,  
"This system allows management of parking lots, vehicles, and parking availability. It supports user authentication, vehicle tracking, and reporting."

**Main Features:**
-   Authenticate using a username and password to generate token
-   Registering a parking lot
-   Registering a vehicle
-   Checking in a vehicle to a parking lot
-   Checking out a vehicle from a parking lot and display parking cost
-   Viewing current occupancy and availability of a parking lot
-   Viewing all vehicles currently parked in a lot
---

## Technology Stack
- **Backend:** Java 17, Spring Boot
- **Database:** H2 (for testing)
- **Authentication:** JWT
- **Other Libraries:** Lombok, ModelMapper, Spring Security

---

## Installation

1. **Clone the repository**
```bash
    git clone https://github.com/wrenflores/HitachiExam.git
    cd parking-management-system
```
2. **Setup Backend**
- Ensure Java 17 and Maven are installed.

3. **Setup Database**
- For testing, H2 in-memory database is used (no setup required).

## Running the Project

**Backend:**
```bash
    mvn spring-boot:run
```
**Backend**: http://localhost:8080
## Usage

**Authentication:**
```bash
    curl --location --request POST 'http://localhost:8080/v1/user/login' \
            --header 'Content-Type: application/json' \
            --header 'Cookie: JSESSIONID=4261AF8E42E735516132F48D98D82A47' \
            --data-raw '{
                "username": "smartpark",
                "password": "Hitachi@12345"
            }
            '
```
Use the returned JWT token for authorized endpoints:
Authorization: Bearer <JWT_TOKEN>

## Features

**Get All Parking Lots**
```bash
    curl --location --request GET 'http://localhost:8080/v1/parkinglot/allparkinglots?page=0&size=5&sort=location%2Casc' \
            --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8'
```

**Available Parking Lots**
```bash
    curl --location --request GET 'http://localhost:8080/v1/parkinglot/availableparkinglots/LOT-001' \
            --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8'
```

**Get Parking Lot**
```bash
    curl --location --request GET 'http://localhost:8080/v1/parkinglot/LOT-003' \
          --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8'
```

**Register Parking Lot**
```bash
    curl --location --request POST 'http://localhost:8080/v1/parkinglot/register' \
            --header 'Content-Type: application/json' \
            --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8' \
            --data '{
                "lotId": "LOT-003",
                "location": "Robinson Parking",
                "capacity": 1,
                "costPerMinute": 5
            }
            '
```

**Register Vehicle**
```bash
    curl --location --request POST 'http://localhost:8080/v1/vehicle/register' \
            --header 'Content-Type: application/json' \
            --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8' \
            --data '{
                "licensePlate": "VBG214",
                "ownerName": "Joselito",
                "type": "CAR"
            }
            '
```

**Check In Vehicle**
```bash
    curl --location --request PATCH 'http://localhost:8080/v1/vehicle/checkin/VBG214?lotId=LOT-001' \
          --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8'
```

**Check Out Vehicle**
```bash
    curl --location --request PATCH 'http://localhost:8080/v1/vehicle/checkout/VBG213' \
            --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzbWFydHBhcmsiLCJpYXQiOjE3NjIzNjk5MjAsImV4cCI6MTc2MjM3MzUyMH0.YGUQLac4yZGibcNCFRN5BxG6lAws2POtUVQaxk-7kD8' 
```

## Testing

**Register Vehicle**
```bash
    mvn test
```
Uses JUnit 5 and Mockito for service/repository testing.









