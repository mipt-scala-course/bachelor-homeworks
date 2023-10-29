package mipt
package homework8

import zio.*
import Homeworks.*
import ProductionEvent.*
import ProductionFailure.*

/**
  * Класс фабрики, которая приняв заказ, готова начать его исполнять
  */
trait ProductionFactory:
    def startOrder(content: Map[String, Int], maxDuration: Duration = Duration.Infinity): ZIO[Any, ProductionFailure, Map[String, Int]]


object ProductionFactory:

    case class ProductionFactoryConfig(
        factoryName: String,
        unitProductionMs: Long,
        neglectProbability: Double,
        repairTreshold: Int,
        repairProbability: Double,
        repairTimeMs: Long
    )


    def apply(
        config: ProductionFactoryConfig,
        eventAggregator: ProductionEventsAggregator
    ): ProductionFactory =
        Impl(config, eventAggregator) 

  
    private class Impl(
        config: ProductionFactoryConfig,
        eventAggregator: ProductionEventsAggregator
    ) extends ProductionFactory:

        private val neglection = ZIO.fail(BeverageProductionNeglect(config.factoryName))
        private val brokening  = ZIO.fail(BeverageTechnicalError(config.factoryName))
        private val productionDestructed = ZIO.fail(ReparingTreshold(config.factoryName))


        override def startOrder(content: Map[String, Int], maxDuration: Duration = Duration.Infinity) =
            task"""
                Реализуйте метод, который долен выполнять заказ.
                По одному выбирайте напитки из ключей заказа и производите по одному в течение unitProductionMs.
                При производстве напитка может произойти одно из следующих событий:
                    * Завершение производства через unitProductionMs от начала - отправляем BeverageProduced аггрегатору и можно приступать к производству следующего напитка. Если напитков в заказе больше нет, тогда производство успешно зваершается.
                    * Поломка оборудования - отправляем BeverageTechnicalError аггрегатору и если не достигнут repairTreshold, то производство встаёт на паузу на repairTimeMs, отправляем событие BeverageProductionRepair аггрегатору. Иначе завершаем производство с ошибкой ReparingTreshold.
                    * После начала ремонта оборудования через repairTimeMs возобновляем производство и отправляем аггрегатору BeverageProductionRepaired.
                    * Сотрудник выпил напиток - отправляем аггрегатору BeverageProductionNeglect, производство единицы напитка начинаем заново.
                    * Если на производство напитков установлен таймаут, то необходимо следить за непревышением этого таймаута. При превышении отправляем аггрегатору BeverageProductionTimeout и завершаем производство с ошибкой ProductionTimeout
               По завершении производства должен вернуться список успешно завершенных напитков (очевино, что аналогичный исходному)
            """ (8, 2)


