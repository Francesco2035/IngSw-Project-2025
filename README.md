
# Galaxy Truckers board game




<img src="https://github.com/PietroPoggi/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/blob/quasi_main_3/Galaxy_Trucker/src/main/resources/galaxy-trucker_sx_SITO___optimized_600_600%20(1).jpg" width=192px height=192 px align="right" />









Galaxy Truckers is the final test of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2024/2025).

**Teacher** Gianpaolo Cugola

**Final Score**:

## Project specification
The objective was to digitize and reproduce in Java the board game *Galaxy Truckers*, made by Czech Games Edition. 

The full game (Italian distribution) can be found at this [link](https://www.craniocreations.it/prodotto/galaxy-trucker)

In this repository the following is included:
* UML diagram custom made for the project by the team;
* UML diagram generated directly from the code with automatic tools;
* rule compliant working game final implementation;
* source code for the implementation;
* source code for the unity test of such implementation;


<!--

## About the project


| **[Installation][installation-link]**     | **[Compiling][compiling-link]**     |    **[Running][running-link]**       | **[Javadocs][javadocs]** | **[Troubleshooting][troubleshooting-link]**
|-------------------------------------|-------------------------------------|-------------------------------------|-------------------------------------|-------------------------------------|
| [![i1][installation-image]][installation-link] | [![i2][compiling-image]][compiling-link] | [![i4][running-image]][running-link] | [![i3][javadocs-image]][javadocs] | [![i5][troubleshooting-image]][troubleshooting-link]
-->



## Initialization

The *system requirements* for running the project are the following:
* Linux, MacOS or Windows OS with an active terminal.
* Java SE JDK 23.0.2 (OracleJDK or OpenJDK) or above.
* Maven version 4.0.0 or above.

#### WARNING:
to achieve the best exceperience on Windows, usage of cmd.exe as a terminal is not adviced


Copy the repository on your machine and start a terminal




## Running

To run the project you will need to:

* Navigate to the following directory: out/artifacts/Galaxy_Trucker_jar
* Execute the following command in the terminal to start the server: java "-Djava.rmi.server.hostname=[your_ip_address]" -jar '.\Galaxy_Trucker-1.0-SNAPSHOT.jar' server
* And for the client: java "-Djava.rmi.server.hostname=[your_ip_address]" -jar '.\Galaxy_Trucker-1.0-SNAPSHOT.jar' client
* The next step for the client is following the instruction on screen to reach the game homepage


# UML diagrams

In this [directory](https://github.com/PietroPoggi/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/tree/main/Galaxy_Trucker/deliverables/UML) are listed all the various requested UML diagrams.


## Javadocs

You can find the generated documentation at this [link](https://github.com/PietroPoggi/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/tree/main/Galaxy_Trucker/deliverables/javadoc).


## Project connection protocol

[Here](https://github.com/PietroPoggi/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/blob/main/Galaxy_Trucker/deliverables/UML/connection%20protocol.pdf)'s the document for the connection protocol of the project. 



## Implemented Functionalities

| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Basic rules | ✅ |
| Complete rules | ✅ |
| RMI |✅ |
| TCP-IP |✅ |
| GUI | ✅ |
| TUI |✅ |
| Trial flight | ✅ |
| Multiple games | ✅|
| Disconnection resilience | ✅|
| Persistence |⛔|

#### Legend
⛔ Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;✅ Implemented


## Test cases
Tests of model and controller reach a classe coverage level of 100%.

**Coverage criteria: code lines.**

| Package | Line Coverage |
|:-----------------------|:------------------------------------:|
| Controller  |614/735 (83%)
| Model | 2817/3337 (84%)


See the complete report [here](https://github.com/PietroPoggi/ing-sw-2025-Poggi-Paludetti-Passolunghi-Rausa/blob/main/Galaxy_Trucker/deliverables/Coverage.png).

## The team

* [Federico Paludetti](https://github.com/PaluFede)
* [Riccardo Passolunghi](https://github.com/passo-polimi)
* [Pietro Poggi](https://github.com/PietroPoggi)
* [Francesco Rausa](https://github.com/Francesco2035)


## Software used

* Intellij IDEA Ultimate - main IDE
* Astah - UML diagrams
* Plantuml - sequence diagrams
* JUnit - Java tests


## Copyright
All rights reserved to their respective partners






