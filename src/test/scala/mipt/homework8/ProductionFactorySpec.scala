package mipt.homework8

import zio.*
import zio.test.*
import zio.test.Assertion.*

import ProductionItem.*
import ProductionEvent.*
import ProductionFailure.*


object ProductionFactorySpec extends ZIOSpecDefault:

    private def makeNamedIdealFactory(name: String, ea: ProductionEventsAggregator) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 0.0, Int.MaxValue, 0.0, 1), ea)

    private def makeNamedBrokableFactory(name: String, ea: ProductionEventsAggregator, treshold: Int = Int.MaxValue) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 0.0, treshold, 1.0, 1), ea)

    private def makeNamedNeglectableFactory(name: String, ea: ProductionEventsAggregator) =
        ProductionFactory(ProductionFactory.ProductionFactoryConfig(name, 1, 0.5, Int.MaxValue, 0.0, 1), ea)

    private val simpleSource =
        Map(Sayany.value -> 1, Kvas.value -> 1, Compot.value -> 1, RosTish.value -> 1)

    override def spec =
        suite("Factory must")(
            test("produce all goods for ideal condifions") {
                for
                    ea <- ProductionEventsAggregator.makeAggregator
                    pf <- makeNamedIdealFactory("Ideal factory", ea).startOrder(simpleSource).fork
                    _  <- TestClock.adjust(5.second)
                    rl <- pf.join
                    events <- ea.getAllEvents()
                yield 
                    assert( rl == simpleSource )( isTrue ) && 
                    assert( events )( isNonEmpty ) && 
                    assert( events )( contains(BeverageProduced("Ideal factory", RosTish.value)) )
            },
            test("fail on production timed out") {
                ProductionEventsAggregator.makeAggregator.flatMap:
                    ea =>
                        (
                            for
                                pf <- makeNamedIdealFactory("Timed factory", ea).startOrder(simpleSource, 2.millisecond).fork
                                _  <- TestClock.adjust(5.second)
                                rl <- pf.join
                            yield 
                                assert( rl )( isEmpty )                        
                        ).catchAll(
                            err =>
                                for events <- ea.getAllEvents() yield
                                    assert( err == ProductionTimeout("Timed factory") )( isTrue ) &&
                                    assert( events )( contains(BeverageProductionTimeout("Timed factory")) )
                        )
            },
            test("fail on production timed out if repair present") {
                ProductionEventsAggregator.makeAggregator.flatMap:
                    ea =>                        
                        (
                            for
                                pf <- makeNamedBrokableFactory("Brokable timed factory", ea).startOrder(simpleSource, 7.millisecond).fork
                                _  <- TestClock.adjust(5.second)
                                rl <- pf.join
                            yield assert( rl )( isEmpty )
                        ).catchAll(
                            err =>
                                for events <- ea.getAllEvents() yield 
                                    assert( err == ProductionTimeout("Brokable timed factory") )( isTrue ) &&
                                    assert( events )( contains(BeverageTechnicalError("Brokable timed factory")) ) &&
                                    assert( events )( contains(BeverageProductionRepair("Brokable timed factory", 1)) ) &&
                                    assert( events )( contains(BeverageProductionRepaired("Brokable timed factory")) ) &&
                                    assert( events )( contains(BeverageProductionTimeout("Brokable timed factory")) )
                        )
            },
            test("fail on production timed out if neglection present") {
                ProductionEventsAggregator.makeAggregator.flatMap:
                    ea =>                        
                        (
                            for
                                _  <- TestRandom.feedDoubles(0, 0.9, 0, 0.9, 0)
                                ea <- ProductionEventsAggregator.makeAggregator
                                pf <- makeNamedNeglectableFactory("Neglectable timed factory", ea).startOrder(simpleSource, 5.millisecond).fork
                                _  <- TestClock.adjust(5.second)
                                rl <- pf.join
                            yield assert( rl )( isEmpty )
                        ).catchAll(
                            err =>
                                for events <- ea.getAllEvents() yield 
                                    println(events.isEmpty)
                                    println(events.size)
                                    assert( err == ProductionTimeout("Neglectable timed factory") )( isTrue )
                        )
            },
            test("fail on repair treshold reached") {
                ProductionEventsAggregator.makeAggregator.flatMap:
                    ea =>                        
                        (
                            for
                                pf <- makeNamedBrokableFactory("Broken totally factory", ea, 3).startOrder(simpleSource).fork
                                _  <- TestClock.adjust(5.second)
                                rl <- pf.join
                            yield assert( rl )( isEmpty )
                        ).catchAll(
                            err =>
                                for events <- ea.getAllEvents() yield 
                                    assert( err == ReparingTreshold("Broken totally factory") )( isTrue ) &&
                                    assert( events )( contains(BeverageTechnicalError("Broken totally factory")) ) &&
                                    assert( events )( contains(BeverageProductionRepair("Broken totally factory", 1)) )
                        )
            }
        )

