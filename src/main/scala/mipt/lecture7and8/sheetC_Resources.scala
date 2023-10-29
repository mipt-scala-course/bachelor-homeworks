package mipt.lecture7and8


import java.io.{BufferedReader, FileReader, IOException}
import scala.util.Using
import zio.ZIO.Acquire
import zio.ZIO.Release



object ScalaUsing extends App:




    val content1 =
        Using(new BufferedReader(new FileReader("file1.txt"))):
            reader =>
                reader.readLine()

    println(content1)












import cats.effect.{IO, IOApp, Resource}

object CatsBracket extends IOApp.Simple:


    def openFile(fileName: String) =
        new BufferedReader(new FileReader(fileName))

    def readFile(fileReader: BufferedReader) =
        fileReader.readLine()

    def closeFile(fileReader: BufferedReader) =
        fileReader.close()


    def sourceIO(fileName: String): IO[BufferedReader] =
        IO(openFile(fileName))




    // def bracket[A, B](acquire: F[A])(use: A => F[B])(release: A => F[Unit]): F[B]

    val succseed: IO[String] =
        IO.bracketFull(
            _ => sourceIO("file1.txt")
        )(
            file => IO(readFile(file))
        )(
            (file, _) => IO(closeFile(file))
        )




    val failed: IO[String] =
        IO(openFile("file2.txt"))
            .bracket(reader => IO.pure(reader))(f => IO(closeFile(f)))
            .map(file => readFile(file))






    // val run =
    //     sourceIO("file1.txt").bracket { file1 =>
    //         sourceIO("file2.txt").bracket { file2 =>
    //             IO.println(s"${readFile(file1)}; ${readFile(file2)}")
    //         }(file2 => IO(closeFile(file2)))
    //     }(file1 => IO(closeFile(file1)))









    val makeResource1: Resource[IO, BufferedReader] =
        Resource.make(sourceIO("file1.txt"))(reader => IO(closeFile(reader)))

    val makeResource2: Resource[IO, BufferedReader] =
        Resource.make(sourceIO("file2.txt"))(reader => IO(closeFile(reader)))

    val makeResource: Resource[IO, BufferedReader] =
        Resource.fromAutoCloseable(sourceIO("file2.txt"))


    val run =
        makeResource1
            .use(file => makeResource2.use(file2 =>  IO(readFile(file))))
            .flatMap(IO.println)







import zio.{Exit, ExitCode, Scope, Task, UIO, ZIO, ZIOAppDefault}

object ZioScope extends ZIOAppDefault:

    def openFile(fileName: String): Task[BufferedReader] =
        ZIO.attemptBlocking(new BufferedReader(new FileReader(fileName)))

    def closeFile(fileReader: BufferedReader): UIO[Unit] =
        ZIO.attemptBlocking(fileReader.close()).catchAll(_ => ZIO.unit)

    def readFile(fileReader: BufferedReader): Task[String] =
        ZIO.attemptBlocking(fileReader.readLine())





    // ZIO 1.x:

    trait ZManaged[-R, +E, +A]{
        def use[R1, E1, B](f: A => ZIO[R1, E1, B]): ZIO[R1, E1, B]
    }

    object ZManaged {
        def aquireReleaseWith[R, E, A](
            aquire: ZIO[R, E, A]
        )(
            release: A => ZIO[R, Nothing, Any]
        ): ZManaged[R, E, A] = ???
    }





    // ZIO 2.x:

    val resource: Task[String] =
        ZIO.acquireReleaseWith(
            openFile("file2.txt")
        )(
            file => closeFile(file)
        )(
            file => readFile(file)
        )











    val aquire: Acquire[Any, Throwable, BufferedReader]  = ZIO.acquireReleaseWith(openFile("file2.txt"))
    val release: Release[Any, Throwable, BufferedReader] = aquire(file => closeFile(file))
    val usage: ZIO[Any, Throwable, String]   = release(file => readFile(file))





    // Introducing Scope

    trait MyScope:
        def addFinalizer(finalizer: Exit[Any, Any] => UIO[Any]): UIO[Unit]
        def close(exit: Exit[Any, Any]): UIO[Any]

    object MyScope:
        val make: UIO[MyScope] = ???


    def fileResource(fileName: String): ZIO[Scope, Throwable, BufferedReader] =
        ???





    // Live example

    lazy val fileReaderLifecicle = 
        ZIO.scoped:
            for
                file1 <- fileResourceImpl("file1.txt")
                scope <- ZIO.service[Scope]
                _ <- scope.addFinalizer(zio.Console.printLine("finalize this").catchAll(_ => ZIO.unit))
                file2 <- fileResourceImpl("file2.txt")
                content1 <- readFile(file1)
                content2 <- readFile(file2)
            yield s"$content1; $content2"


    def fileResourceImpl(fileName: String): ZIO[Scope, Throwable, BufferedReader] =
        ZIO.acquireRelease(openFile(fileName))(file => closeFile(file))


    //
    override def run: ZIO[Scope, IOException, ExitCode] =
        fileReaderLifecicle
            .flatMap(string => zio.Console.printLine(string))
            .exitCode



    // val zxc = ZIO.acquireReleaseInterruptible(
    //     openFile(fileName)
    // )(
    //     ZIO.unit
    // )





