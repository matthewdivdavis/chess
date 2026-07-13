# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Server Diagram]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdADZM9qBACu2AMQALADMABwATACcIDD+yPYAFmA6CD6GAEoo9kiqFnJIEGiYiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAEQDlGjAALYo43XjMOMANCu46gDu0ByLy2srKLPASAj7KwC+mMK1MJWs7FyUDRNTUDPzF4fjm6o7UD2SxW63Gx1O52B42ubE43FgD1uogaUCyOTAlAAFJlsrlKJkAI5pXIAShuNVE9yqsnkShU6ga9hQYAAqoNMe9PigyTTFMo1KoqUYdHUAGJITgwNmUXkwHSWGCcuZiHSo4AAaylgxgWyQYASisGXJgwAQao4CpQAA90RpeXSBfdERSVA1pVBeeSRConVVbi8YAozShgBaOhr0ABRK0qbAEQpeu5lB4lcwNQJOYIjCbzdTAJmLFaRqDeeqG6bKk3B0MK+Tq9DQsycTD2-nqX3Vb0oBpoHwIBCJykPVv01R1EBqjHujmDXk87QO9sPYx1BQcDhamXaQc+4cLttjichjEKHz6zHAM8JOct-ejoUrtcb0-6z1I3cPWHPMs49H4tR9lgX7wh2-plm8RrKssDQHKCl76h0ED1mg0ErFciaUB2qYYA04ROE42aTJBXwwDBIIrPBCSIchqEHNc6AcKYXi+AE0DsEysSinAkbSHACgwAAMhA2RFNhzDOtQAYtO03R9AY6gFGg2ZKvM6x-ACHDXGBQrAQGowqSgan6P8uwwk8IESVQyIwAgwkSpiQkiYSxJgGS76GHutIHoyzLTgZ85efey4iuKkrurK8rlh8yqYKqIaau6xocBAagwGgEDMNa6K3oFjrJn6LrdmlfYDu5oE1AGxalm6MxXtASAAF4oIx2n5fAyBpjAGZZmM4y5qo+YLDBVXQA0BlpRlMAAGa+JwjYMTlfJBZZog7igAA8yYjgKdThduZWeUtAprZU7njpOKAvgkmJ7fIAVHUuVTGGtQrbeoi2LoKwUnRUz3uZUb1jgNNBXZiV03oDlQrmQwA0IGV5voVHa6WW4Pbij5WSeBREViRZE-JR1ENqRaGNq1KYdThMB4QRvUGbR5HjITSHE-j9HNp43h+P4XgoOgsTxEkvP845vhYGJQpgQ0jTSJGAmRh0kY9L08mqIpIzM8hGEIlUKPS7L8uK8rqvq7VCEs0UGMra6NnCWLDl22ezlqK5L2HZ9DQgIUU1IFAsyg5r6D3Z9UMigA4syJoI9ugObSumQ2IUBixWqmpXfVDX5IUMCQMhRgpYK6WZTauQfQemNWTbvb9m7lmVSWo1SmbCQZ812tYZTYDpk4ACMhH9YNhbjCNZYsjM+qt3s7MtYVm2A6u0fyD988cCg3AnleYOL8Awfl8FdTSGvzKGPqhjp1AjVZ2gMBE0UgMV9Z1elUj7sHmXQVPSYB2fuZAZP5gVs65ln-uTKoYlcL4VGNPJiXMAiog3P4bAEpNQCXRDAMOyoNASyAdLMOCtlb2GVBrK8t924-zhFJPBsleiEPmMQ82WsrYFS7D5MAtC1AXhIRbNyL9qR3h2jAL2aAfZ+wDlw5Cu8P7CjqBHZggc778PUHHEUCdsBJxVKneGE8L6Z3jNfXO-NkqpSLjALKpd75tTOsVGutdmG1AaCPGqlFJ5kIpqULuXVe79wFIPYaDdR7jxbjo5q81myxzKH9WeZR55oyXv9Co88mRgAwbmTEZJjAA0UWOGA7DVCenfnlOx1lcmIy7MjX+ZYSnbgARUiuekVi5MLI0CY7CACS0hCw93CMEQIoItgJD1CgRKUElg-BSKAdUwyvijNBOwgAcsqUZlwYBdC0hVBEbjOo02zA0zBTSWnKnaZ07pvSVj9MGVMoa4wxkIAmZc-YPx5mLOucs1ZTZGKcxYv4DgAB2SITgUBOFiJGYIcBuJuHgBddBlZiid0lus6WbROgEKIc3W+2YnnzDWVjHSFTEUyRRXQtFFsMXKgWVimpFCdadkrkVI8cgUApJQJwhhQc3Z8NygyQR3tfb+03vIskBTHrSNkVHVlRQhWqGUXUVR6iU7xS0UEy+eic4W3zsYyaZisAWOtkVEBL8cEwEcU3ZxwSZ44rauAzxfdeoDwLH46qJrtGNRCdA9yc8skL1fPtQqmTOVjiSUyzEmLuSaB0H6h62T2H5MlfClhOTlQxpRptdytjHhUsZIm6pgC7EBjadIUwoD2ruIgbTUYuz5hHIaF0npMBoGfO5pYNetktgCyQIkMATb+wQFbQAKQgBKaF8xYjjJAOqWF7i432KaM0Fk1D2H0KoiSsY2BblNqgHACAtkoDrHzdipMus8UzrnYSlAi70UrrXZQTd27d2HOkGZKlU7rIACsB1oCDQK9lMhPXcuEbysR4rJGFJFZHeRphwnxzUGotAyc4oakVRnK+qq85GMLpqku2qskPyriVQtBrc1lmNWPU1LqPlFqtRmG1OYfH2qLP4pxzqmpT3ecvT1QiRGzCDfm4D6hQ4yMjkysNEaPYJvmJ6eDmomXqvQ8XbKkNTqFR7HhsCm0yhnWNQAIVDDAZ2pcxJqalhmMtfVaNDXo467TG49NgFCea2limWEgASCgMdN7oCYlXcAdd7moBkkk-ALd0Ab4RiKKp5MestM6Zs5S78hmlNe23TnULE7zDxaxnUYz3i8x0eHgxmAVndMoCJC7OzsbdWZvEz6spbU9ZiZQJ6HNNK81ZriesjuJbqb4R2b8K9G6gtQH2AAdRYK0xWvRNMCQUHAAA0o8+9xza31uYtzLw3m20drWwqRAIZYDAGwKuwgyHUvlEI-rOWCslZ9EiRaw9GamgG0u8rZ6TCaXWRlhdo212v68J-f687hsru9GehYz+AOnvfeExUK1NMoGsaAA)
