# Scala/ZIO Random User Generator Client

Scala 3 client implementation for the [Random User Generator API](https://randomuser.me/), built using [ZIO](https://zio.dev/) and [Sttp](https://sttp.softwaremill.com/en/stable/).

## Getting Started

Add the following to your build.sbt:

```scala
libraryDependencies += "io.github.juliano" % "random-user_3" % "0.1.3"
```

## Usage

Here's a basic example of how to use the client:

```scala
import io.github.juliano.randomuser.*
import zio.*

object Example extends ZIOAppDefault:
  def run = 
    val program = for
      content <- Client.fetch()
      _ <- Console.printLine(s"Random user: ${content.results.head}")
      _ <- Console.printLine(s"API info: ${content.info}")
    yield ()

    program.provide(Client.default)
```


## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Random User Generator](https://randomuser.me/) for providing the API!
