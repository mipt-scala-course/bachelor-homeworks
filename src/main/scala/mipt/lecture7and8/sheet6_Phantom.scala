package mipt.lecture7and8


import zio.ZIO

import scala.util.NotGiven


object sheet6_Phantom extends App:


    object Example:

        sealed trait Flower
        case object Carnation extends Flower
        case object Chamomile extends Flower
        case object Knapweed  extends Flower
        case object Rose      extends Flower
        case object Sunflower extends Flower
        case object Tulip     extends Flower

        trait Ikebana:
            def content: Set[Flower]


        object Girl:
            def recieveIkebana(ikebana: Ikebana) =
                if (ikebana.content(Rose) || ikebana.content.size > 1)
                    "Get a smile back"
                else
                    throw new IllegalStateException



        object CompileTimeChecked:
            trait ValidIkebana

            class IkebanaTyped[C](val content: Set[Flower]) extends Ikebana:
                def addFlower[F <: Flower](f: F): IkebanaTyped[ValidIkebana] =
                    new IkebanaTyped[ValidIkebana](content + f)

            object IkebanaTyped:
                def apply[F <: Flower](f: F): IkebanaTyped[Nothing] =
                    new IkebanaTyped[Nothing](Set(f))
                def apply(f: Rose.type): IkebanaTyped[ValidIkebana] =
                    new IkebanaTyped[ValidIkebana](Set(f))


            // gift ikebana function

            def giftIkebana(ikebana: IkebanaTyped[ValidIkebana]) =
                Girl.recieveIkebana(ikebana)


            // Checks
            val ikebana1 = IkebanaTyped(Knapweed)
            val ikebana2 = IkebanaTyped(Rose)
            val ikebana3 = ikebana1.addFlower(Rose)
            val ikebana4 = ikebana1.addFlower(Tulip)
            val ikebana5 = ikebana1.addFlower(Knapweed)

            // giftIkebana(ikebana1)
            giftIkebana(ikebana2)
            giftIkebana(ikebana3)
            giftIkebana(ikebana4)
            giftIkebana(ikebana5)








        object CompileTimeCheckedWithEvidance:
            trait ValidIkebana


            class IkebanaTyped[T, C](val content: Set[Flower]) extends Ikebana:
                def addFlower[F <: Flower](f: F)(implicit ev: NotGiven[F <:< T]): IkebanaTyped[T with F, ValidIkebana] =
                    new IkebanaTyped[T with F, ValidIkebana](content + f)

            object IkebanaTyped:
                def apply[F <: Flower](f: F): IkebanaTyped[F, Nothing] =
                    new IkebanaTyped[F, Nothing](Set(f))
                def apply[F <: Rose.type](f: F): IkebanaTyped[F, ValidIkebana] =
                    new IkebanaTyped[F, ValidIkebana](Set(f))




            // gift ikebana function

            def giftIkebana(ikebana: IkebanaTyped[_, ValidIkebana]) =
                Girl.recieveIkebana(ikebana)



            // Checks
            val ikebana1 = IkebanaTyped(Knapweed)
            val ikebana2 = IkebanaTyped(Rose)
            val ikebana3 = ikebana1.addFlower(Rose)
            val ikebana4 = ikebana1.addFlower(Tulip)
            // val ikebana5 = ikebana1.addFlower(Knapweed)

            // giftIkebana(ikebana1)
            giftIkebana(ikebana2)
            giftIkebana(ikebana3)
            giftIkebana(ikebana4)





    object Effects:



        case class Effect[R, +E, +A](value: () => Either[E, A], services: Map[String, Any] = Map.empty)


        object Effect:

            def succeed[A](a: => A): Effect[Any, Nothing, A] =
                Effect(() => Right(a))

            def failed[E](e: => E): Effect[Any, E, Nothing] =
                Effect(() => Left(e))



        extension [R, E, A] (effect: Effect[R, E, A])

            def provide[R1](service: R1): Effect[R with R1, E, A] =
                effect.copy(services = effect.services + (service.getClass().toString() -> service))

            def service[R1 <: R](c: Class[R1]): R1 =
                effect.services.get(c.toString()).get.asInstanceOf[R1]

                
            def flatMap[B](f: A => Effect[R, E, B]): Effect[R, E, B] =
                Effect( () =>
                    effect.value() match
                        case Right(value) => f(value).value()
                        case error        => error.asInstanceOf
                )

            def map[B](f: A => B): Effect[R, E, B] =
                Effect( () =>
                    effect.value() match
                        case Right(value) => Right(f(value))
                        case error        => error.asInstanceOf
                )




        object SomeService:
            def doSomeWork(value: Int): String = s"String with $value"

        object AnotherService:
            def doSomeWork(value: Int): String = s"Something wrong with $value"



            
        val initial: Effect[Any, Nothing, Int] =
            Effect.succeed(10)

        val provided: Effect[SomeService.type, Nothing, Int] =
            initial.provide(SomeService)


        val result = 
            for
                value   <- provided
                service  = provided.service(classOf[SomeService.type])
            yield service.doSomeWork(value)

    end Effects
    


    println(Effects.result.value())







    object DependencyControlled:


        // будем хранить "метки" имеющихся сервисов
        enum SvcTag[A]:
            case IntTag extends SvcTag[Int]
            case BoolTag extends SvcTag[Boolean]
            case Service[Svc](name: String) extends SvcTag[Svc]


        type SvcComplex = SvcTag[Int] & SvcTag[Boolean]


        // хранить метки будем в специальном типе-обёртке
        trait HasTag[A]:
            def tag: SvcTag[A]

        object HasTag:
            def apply[A](using H: HasTag[A]): HasTag[A] = H
            def svc[Svc](name: String): HasTag[Svc] = new HasTag[Svc]:
                def tag: SvcTag[Svc] =
                    SvcTag.Service(name)



        // "копилка" для меток
        case class Env[+R](map: Map[SvcTag[_], Any]):

            def get[A >: R: HasTag]: A =
                map.getOrElse(HasTag[A].tag, throw new Exception("No such tag")).asInstanceOf[A]

            def add[A: HasTag](value: A): Env[A & R] =
                Env[A & R](map + (HasTag[A].tag -> value))

                
        object Env:
            val empty: Env[Any] = Env[Any](Map())


        // Эффект
        case class Effect[-R, +E, +A](f: Env[R] => Either[E, A])

        // Конструкторы
        object Effect:

            def pure[A](a: => A): Effect[Any, Nothing, A] =
                Effect(_ => Right(a))

            def raise[E](e: => E): Effect[Any, E, Nothing] =
                Effect(_ => Left(e))

            def service[S: HasTag]: Effect[S, Nothing, S] =
                Effect(env => Right(env.get[S]))


        // Операторы
        extension [R, E, A] (effect: Effect[R, E, A])

            def flatMap[R2 <: R, B](g: A => Effect[R2, E, B]): Effect[R2, E, B] =
                Effect(
                    (env: Env[R2]) =>
                        effect.f(env) match
                            case Right(a) => g(a).f(env)
                            case e => e.asInstanceOf
                )

            def map[B](g: A => B): Effect[R, E, B] =
                Effect[R, E, B](
                    (env: Env[R]) => 
                        effect.f(env) match
                            case Right(a) => Right(g(a))
                            case e => e.asInstanceOf
                )


            def provide[R2 >: R, S: HasTag](value: S)(using ev: (S & R2) =:= R): Effect[R2, E, A] =
                Effect[R2, E, A](e => effect.f(ev.liftCo(e.add(value))))



        // Examples

        // Services
        trait InputSvc[In]:
            def read: Effect[Any, Nothing, In]

        trait OutputSvc[Out]:
            def write(data: Out): Effect[Any, Nothing, Unit]


        object StringInput:
            given HasTag[InputSvc[String]] = HasTag.svc("InputSvc")
            def apply: InputSvc[String] =
                new:
                    override def read: Effect[Any, Nothing, String] =
                        Effect.pure(scala.io.StdIn.readLine)

        object StringOutput:
            given HasTag[OutputSvc[String]] = HasTag.svc("OutputSvc")
            def apply: OutputSvc[String] =
                new:
                    override def write(data: String): Effect[Any, Nothing, Unit] =
                        Effect.pure(println(data))
                

        // Business Domain & Logic
        import StringInput.given
        import StringOutput.given


        case class User(name: String)

        trait AuthService:
            def authenticate: Effect[InputSvc[String] & OutputSvc[String], Throwable, User]

        object AuthService:
            
            class Impl extends AuthService:
                override def authenticate: Effect[InputSvc[String] & OutputSvc[String], Throwable, User] =
                    for
                        input   <- Effect.service[InputSvc[String]]
                        output  <- Effect.service[OutputSvc[String]]
                        _       <- output.write("Hello! What is your name?")
                        name    <- input.read
                        _       <- output.write("Really?")
                        confirm <- input.read
                        user    <- 
                            if (confirm.compareToIgnoreCase("yes") == 0)
                                Effect.pure(User(name))
                            else
                                Effect.raise(new Exception("Inadequate"))
                    yield user

        val program =
            new AuthService.Impl()
                .authenticate
                .provide[InputSvc[String], OutputSvc[String]](StringOutput.apply)
                .provide[Any, InputSvc[String]](StringInput.apply)
                
    end DependencyControlled
    import DependencyControlled.*



    println("*"*16)
    println("DependencyControlled:")
    println()
    println(program.f(Env.empty))
    println()
    println(program.f(Env.empty))
    println() 






