# CSV Import Script

This script reads observation data from a CSV file and sends each row as an HTTP POST request to a Spring Boot backend.

## Requirements
- Node.js installed
- Backend running on `http://localhost:8080`
- Endpoint available: `POST /api/observations`

## Installation
Install dependencies:
bash
npm install

## Run Script
bash
node index.js