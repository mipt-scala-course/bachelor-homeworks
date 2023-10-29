package mipt.homework8



enum ProductionEvent(factoryName: String):

    // Напиток произведен
    case BeverageProduced(factoryName: String, bewerageName: String) 
        extends ProductionEvent(factoryName)

    // На заводе произошла одна техническая ошибка
    case BeverageTechnicalError(factoryName: String) 
        extends ProductionEvent(factoryName)

    // Производится починка оборудования
    case BeverageProductionRepair(factoryName: String, repairTime: Long) 
        extends ProductionEvent(factoryName)

    // Починка завершена
    case BeverageProductionRepaired(factoryName: String) 
        extends ProductionEvent(factoryName)

    // Работник Баханов выпил напитки
    case BeverageProductionNeglect(factoryName: String) 
        extends ProductionEvent(factoryName)

    // Достигнут лимит по времени производства напитков на одной из фабрик
    case BeverageProductionTimeout(factoryName: String) 
        extends ProductionEvent(factoryName)

