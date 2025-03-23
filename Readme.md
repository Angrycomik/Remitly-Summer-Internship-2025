# Remitly Home Exercise Summer Internship 2025

## Project Description
This is a Spring Boot application made as a task for Remitly Summer Internship 2025. 
It is designed to process and store SWIFT code data for banks.

The data is stored in PostgreSQL and containerized using Docker.

## Running

### Requirements:
- **Docker**

### Installation:

1. Clone the repository:
```bash
git clone https://github.com/Angrycomik/Remitly-Summer-Internship-2025.git
cd Remitly-Summer-Internship-2025
```

2. Build and run the containers:
```bash
docker-compose up --build
```

3. The application can be accessed at:
```
http://localhost:8080
```

## Endpoints

### 1. Retrieve details of a single SWIFT code whether for a headquarters or branches.
**GET** `/v1/swift-codes/{swift-code}`

**Example response:**
```
{
    "address": "Example Address",
    "bankName": "Example Bank Name",
    "countryISO2": "AA",
    "countryName": "Country",
    "isHeadquarter": true,
    "swiftCode": "AAAABBCCXXX",
    "branches": [
        {
            "address": "Example Address",
            "bankName": "Example Bank Name",
            "countryISO2": "AA",
            "isHeadquarter": false,
            "swiftCode": "AAAABBCC001"
        },
        {
            "address": "Example Address",
            "bankName": "Example Bank Name",
            "countryISO2": "AA",
            "isHeadquarter": false,
            "swiftCode": "AAAABBCC002"
        }
    ]
}
```
### 2. Return all SWIFT codes with details for a specific country (both headquarters and branches).
**GET** `/v1/swift-codes/country/{countryISO2code}`

**Example response:**
```
{
    "countryISO2": "AA",
    "countryName": "Country",
    "swiftCodes": [
        {
            "address": "Example Address",
            "bankName": "Example Bank Name",
            "countryISO2": "AA",
            "isHeadquarter": true,
            "swiftCode": "AAAABBCCXXX"
        }
    ]
}
```


### 3. Adds new SWIFT code entries to the database for a specific country.
**POST** `/v1/swift-codes`

**Example request:**
```
{
"address": "Example Address",
"bankName": "Example Bank Name",
"countryISO2": "AA",
"countryName": "Country",
"isHeadquarter": true,
"swiftCode": "AAAABBCCXXX"
}
```
**Example response:**
```
{
    "message": "SWIFT code added successfully"
}
```
### 4. Deletes swift-code data if swiftCode matches the one in the database.
**DELETE** `/v1/swift-codes/{swift-code}`

**Example response:**
```
{
    "message": "SWIFT code deleted successfully"
}
```
## Testing

To run the tests, use the following commands:

1. Connect to the container:
```bash
docker exec -it remitly-app sh
```

2. Run the tests using Maven:
```bash
mvn test
```
It will run both unit and integration tests.

Additionally, the application has been tested manually using Postman.

## Additional Information

- The data is loaded from the **swift_codes.csv** file located in the **src/main/resources** directory into the PostgreSQL database after the application starts. 
- The file and database are stored inside the Docker container.


