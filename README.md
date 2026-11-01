\## Kafka Setup



This project uses Apache Kafka for asynchronous communication between microservices.



\### Prerequisites

\- Java 21

\- Apache Kafka 4.3.1



\### Installation

1\. Download Apache Kafka 4.3.1 from the \[official Apache Kafka website](https://kafka.apache.org/downloads).

2\. Extract Kafka to a directory outside the SchoolManagementSystem project.

3\. Configure Kafka using its KRaft configuration.

4\. Start the Kafka broker.

5\. Ensure the broker is accessible at `localhost:9092`.



\### Application Configuration



Configure the following environment variable:



`KAFKA\_BOOTSTRAP\_SERVERS=localhost:9092`



\### Important

Kafka installation files, local broker data and logs are not included in this repository. Each developer must install and configure Kafka separately.

