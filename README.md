# Multithreaded messaging server

## ABOUT
This is a <ins>console version</ins> of a simple chat that allows you to exchange messages with other clients connected to the server.

### Main features:
- ✅ **Separated input and output message streams**
- ✅ **Sending messages in JSON format**
- ✅ **Save an unfinished message and move it to the next line with the ability to edit**

## How to start it?

Since Java requires the presence of the main class Main, for ease of launch there is the possibility to initialize the server or client classes using **command line arguments**.

> [!IMPORTANT]
> Make sure you have Maven installed, which is required to build this project. (See [Dependencies](#dependencies))

<details>
  <summary><b>Starting the server</b></summary>
  
  ```
  make
  java -jar target/multithreaded-server.jar Server
  ```
</details>

<details>
  <summary><b>Starting the client</b></summary>

  ```
  make
  java -jar target/multithreaded-server.jar Client
  ```
</details>

> [!NOTE]
> There is also an interactive class selection in the absence of command line parameters.
> ```
> $ make && java -jar target/multithreaded-server.jar
> 
> Select startup mode
> 1 - Server
> 2 - Client
> Number:
> ```

## Dependencies
- **OpenJDK** (*tested on 17.0.15*)
- **Maven** (*tested on 3.8.7*)
- **Gson** (*tested on 2.13.1*)
- **JLine** (*tested on 3.30.2*)

## Want to contribute?
It's not hard. Just fork and then send a pull request. The project is really young and weak and therefore needs your help :)

All the basic rules for participating in the development of the project are written in [CONTRIBUTING](CONTRIBUTING.md).
