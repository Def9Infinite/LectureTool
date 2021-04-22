Client: ![Client coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/organisation/repository-template/badges/master/coverage.svg?job=client-test)
Server: ![Server coverage](https://gitlab.ewi.tudelft.nl/cse1105/2020-2021/organisation/repository-template/badges/master/coverage.svg?job=server-test)


## Description of project
This project aims to improve the efficiency of remote learning under current environment. The main purpose of this project is to create a room to assist professors and students to ask questions in the same way as normal time. It also includes additional functionality, like lecture-speed feedback and polls.


## How to run it
1. Install Newest version of Docker from https://www.docker.com/
2. Run "create_database_in_docker.sh" under tooling directory.
3. Start up server by running server/src/main/java/nl/tudelft/oopp/project/DemoApplication.java.
4. To start up the software, navigate and run client/drc/main/java/nl/tudelft/oopp/project/MainApp.java

## How to test it
- Both the client and server have automatic tests, located in their respective `tests` directories.
- Extensive manual testing procedures are described in ManualTesting.md

## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://eu.ui-avatars.com/api/?name=MB&length=4&size=50&color=DDD&background=777&font-size=0.325) | Mirko Boon | m.s.boon@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=NK&length=4&size=50&color=DDD&background=777&font-size=0.325) | Nathan Klumpenaar | M.J.N.klumpenaar@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=YL&length=4&size=50&color=DDD&background=777&font-size=0.325) | Yifei Lu | Y.lu-30@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=ML&length=4&size=50&color=DDD&background=777&font-size=0.325) | Mihai Lates | M.Lates@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=DD&length=4&size=50&color=DDD&background=777&font-size=0.325) | Dragomir Drashkov | D.D.Drashkov@student.tudelft.nl |

## Copyright / License 

This work is licensed under a
[Creative Commons Attribution-ShareAlike 4.0 International License][cc-by-sa].

[![CC BY-SA 4.0][cc-by-sa-image]][cc-by-sa]

[cc-by-sa]: http://creativecommons.org/licenses/by-sa/4.0/
[cc-by-sa-image]: https://licensebuttons.net/l/by-sa/4.0/88x31.png
[cc-by-sa-shield]: https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg
