package mipt
package homework8

import zio.*
import Homeworks.*
import ProductionEvent.*
import ProductionFailure.*
import ProductionItem.*


object ProductionDomain extends ZIOAppDefault:

    private def makeNamedIdealFactory(name: String, ea: ProductionEventsAggregator) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 0.0, Int.MaxValue, 0.0, 1), ea)

    private def makeNamedBrokableFactory(name: String, ea: ProductionEventsAggregator, treshold: Int = Int.MaxValue) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 0.0, treshold, 1.0, 1), ea)

    private def makeNamedNeglectableFactory(name: String, ea: ProductionEventsAggregator) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 1.0, Int.MaxValue, 0.0, 1), ea)

    private val simpleSource =
        Map(Sayany.value -> 1, Kvas.value -> 2, Compot.value -> 3, RosTish.value -> 5)

    
    def production(order: Map[String, Int], eventAggregator: ProductionEventsAggregator, timeout: Duration = Duration.Infinity): ZIO[Any, Nothing, Unit] =
        task"""
            Реализуйте алгоритм, который передаёт заранее сформированный список заказов заранее заданным фабрикам.
            Работа фабрик должна осуществляться параллельно. Используйте файберы.
            Завершение производства на одной из фабрик должно останавливать и отменять производство на всех остальных фабриках.
        """ (8, 1)



    override def run: ZIO[Any, Any, Any] =
        ZIO.scoped:
            for 
                ea      <- ProductionEventsAggregator.makeAggregator
                result  <- production(simpleSource, ea, 25.millisecond)
                events  <- ea.getAllEvents()
                _       <- ZIO.foreach(events)(evt => Console.printLine(evt.toString()))
            yield ()
