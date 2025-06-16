# Multithreaded messaging server

## ABOUT
This is a <ins>console version</ins> of a simple chat that allows you to exchange messages with other clients connected to the server.

## How to start it?

Since Java requires the presence of the main class Main, for ease of launch there is the possibility to initialize the server or client classes using command line arguments.
> [!WARNING]
> **PLEASE NOTE** that you need to generate a .class file before running. To do this, simply run in the terminal `make`

<details>
  <summary><b>Starting the server</b></summary>

  ```
  make
  java Main server
  ```

</details>

<details>
  <summary><b>Starting the client</b></summary>

  ```
  make
  java Main client
  ```
</details>

> [!NOTE]
> There is also an interactive class selection in the absence of command line parameters.
> ```
> $ make && java Main
> 
> Select startup mode
> 1 - Server
> 2 - Client
> Number:
> ```

# Want to contribute?
It's not hard. Just fork and then send a pull request. The project is really young and weak and therefore needs your help :)

All the basic rules for participating in the development of the project are written in [CONTRIBUTING](CONTRIBUTING.md).
