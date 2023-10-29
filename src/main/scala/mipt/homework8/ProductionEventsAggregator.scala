package mipt.homework8


import zio.*


trait ProductionEventsAggregator:
    // Метод принимает результат производтсва одного напитка и сохраняет его в общий список всех событий со всех фабрик
    def saveProductionEvent(productionEvent: ProductionEvent): UIO[Unit]

    // Получить список всех событий, которые были сохранены в агрегаторе
    def getAllEvents(): UIO[List[ProductionEvent]]


object ProductionEventsAggregator:

    private class Impl(state: Ref[List[ProductionEvent]]) extends ProductionEventsAggregator:
        override def saveProductionEvent(productionEvent: ProductionEvent): UIO[Unit] =
            state.update(_ :+ productionEvent)
        override def getAllEvents(): UIO[List[ProductionEvent]] =
            state.get
    

    // Метод для создания аггрегатора результатов производства.
    // Вызывать его нужно один раз в мейне программы, чтобы был всего один объект на всю программу.
    // Ref это аналог AtomicRef, только из ФП
    def makeAggregator: ZIO[Scope, Nothing, ProductionEventsAggregator] =
        Ref.make(List.empty[ProductionEvent]).map(new Impl(_))

