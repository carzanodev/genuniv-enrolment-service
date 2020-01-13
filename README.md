# Overview
The enrolment service is the system of which students enroll to offerings provided by the colleges.

This service uses [genuniv-common-service](https://github.com/carzanodev/genuniv-common-service) as its chassis.

# Relations Diagram
![enrolment](./.assets/genuniv-enrolment-service.png)

## Request to Other Services
The enrolment service requests data from the following services via its APIs:
1. [general-info-service](https://github.com/carzanodev/genuniv-general-info-service)
    * School Year
    * School Period
    * Schedule
2. [college-service](https://github.com/carzanodev/genuniv-college-service)
    * Offering
2. [personal-records-service](https://github.com/carzanodev/genuniv-personal-records-service)
    * Faculty
    * Student
    * Staff
    
The retrieval of the data are done via a dedicated thread that periodically requests from the APIs. The data are also being cached afterwards.

# Access Endpoints
1. `/api/v1/enrolment`

## Enrolment API
The enrolment API does not follow the usual usage pattern of other genuniv-service's APIs. Most of its operations have to necessarily target a student.

For example, we POST an enrolment via:
```json
curl -H 'Content-Type: application/json' -d @data.json -X POST http://localhost:18000/api/v1/enrolment

(data.json)
{
    "student_id": 1,
    "enrolments": [
        {
            "offering_id": 1
        },
        {
            "offering_id": 2
        },
        {
            "offering_id": 3
        }
    ]
}
```

We obtain a student's list of enrolments via:
```json
curl localhost:18000/api/v1/enrolment?student_id=2

Result:
{
    "response": {
        "student_enrolments": [
            {
                "student_id": 1,
                "enrolments": [
                    {
                        "id": 1,
                        "offering_id": 1
                    },
                    {
                        "id": 2,
                        "offering_id": 2
                    },
                    {
                        "id": 3,
                        "offering_id": 3
                    }
                ]
            }
        ]
    },
    "meta": {
        "message": "Successfully retrieved data with id = 1!",
        "timestamp": "2020-01-11T03:18:19.188+0000"
    }
}
```

# Atomic Counter
In order to make sure that enrolments from possibly multiple instances of enrolment-service sync-up correctly with their maximum capacity, the atomic integer of Redis is used. The atomic integer has synchronized increment/decrement within the Redis server, making it perfect for handling the job of managing the capacity of an offering from multiple enrolment-service enrolment.

In order to check for the capacity of an offering in Redis, we use the key prefix `EC#`, followed by the offering ID.

Example:
```shell script
127.0.0.1:6379> get EC#1
"1"
```