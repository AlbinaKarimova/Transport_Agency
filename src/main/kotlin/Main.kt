import java.math.RoundingMode
import java.text.DecimalFormat
interface city{
    var type : String // Тип города
    var weather : Int // Погода: непригодная для полета = 3 и 2, для автомобиля = 3, для поезда - любая пригодна
    var highways : Boolean // Наличие скоростной магистрали
    var name: String // Имя города
}
class big_city(_type : String, _weather : Int, _highways : Boolean, _name: String) : city{
    override var type: String = _type
    override var weather: Int = _weather
    override var highways: Boolean = _highways
    override var name: String = _name
    //var big_city_number : Int = 2
}
class middle_city(_type : String, _weather : Int, _highways : Boolean, _name: String) : city{
    override var type: String = _type
    override var weather: Int = _weather
    override var highways: Boolean = _highways
    override var name: String = _name
    //var middle_city_number = 4
}

interface transport{
    var type : String // Тип транспорта
    var price : Double // Стоимость единицы вeса на единицу пути
    var speed : Double // Скорость доставки
    var weidht : Double // Масса груза
    var accident_rate : Double // Аварийность транспорта (вероятность)
    fun cargo_time(dist: Int, customer: client): Double
    fun cargo_price(dist: Int, customer: client): Double
}
class Auto(var distance : Int) : transport{
    override var type : String = "auto"
    override var speed: Double = 50.0
    override var accident_rate: Double = 15.8 // увеличить при наличии магистрали на пути
    override var price: Double = 12.5 //* weidht * distance
    override var weidht: Double = 0.0
        set(value) {
            if (value > 1.0 && value <= 3949.0)
                field = value
            else
                field = 0.0
        }
        get() = field

    override fun cargo_time(dist: Int, customer: client): Double{
        // Проверяем, есть ли на пути скоростные магистрали
        if (customer.startCity.highways == true || customer.finishCity.highways == true){
            speed *= 2.2 // увеличиваем скорость на заданный коэффициент
        }
        var time = dist.toDouble() / speed
        return time
    }

    override fun cargo_price(dist: Int, customer: client): Double {
        weidht = customer.cargo_weight
        if (weidht != 0.0) {
            price = (price * dist.toDouble()) / weidht
            return price
        }
        else{
            return 0.0
        }
    }
}
class Train(var distance : Int) : transport{
    override var type: String = "train"
    override var speed: Double = 80.0
    override var accident_rate: Double = 6.1 // уменьшить при наличии магистрали на пути
    override var price: Double = 27.5 //* weidht * distance
    override var weidht: Double = 0.0
        set(value) {
            if (value > 4000.0 && value <= 69500.0)
                field = value
            else
                field = 0.0
        }
        get() = field

    override fun cargo_time(dist: Int, customer: client): Double{
        // Проверяем, есть ли на пути скоростные магистрали
        if (customer.startCity.highways == true || customer.finishCity.highways == true){
            speed *= 2.5 // увеличиваем скорость на заданный коэффициент
        }
        var time = dist.toDouble() / speed
        return time
    }

    override fun cargo_price(dist: Int, customer: client): Double {
        weidht = customer.cargo_weight
        if (weidht != 0.0) {
            price = (price * dist.toDouble()) / weidht
            return price
        }
        else{
            return 0.0
        }
    }
}
class Avia(var distance : Int) : transport{
    override var type : String = "avia"
    override var speed: Double = 800.0
    override var accident_rate: Double = 0.1
    override var price: Double = 3450.0 //* weidht * distance
    override var weidht: Double = 0.0
        set(value) {
            if (value > 69500.0 && value <= 120000.0)
                field = value
            else
                field = 0.0
        }
        get() = field

    override fun cargo_time(dist: Int, customer: client): Double{
        var time = dist.toDouble() / speed
        return time
    }

    override fun cargo_price(dist: Int, customer: client): Double {
        weidht = customer.cargo_weight
        // надо учесть что вес может быть равен 0
        if (weidht != 0.0) {
            price = (price * dist.toDouble()) / weidht
            return price
        }
        else{
             return 0.0
        }
    }
}

class client(var startCity : city, var finishCity : city,
             var cargo_weight : Double){
    var desired_speed : Double = 0.0// Желаемая скорость доставки груза (в часах)
    var desired_price : Double = 0.0 // Желаемая цена доставки
    constructor(startCity : city, finishCity : city,
                cargo_weight : Double, _desired_speed : Double,
                _desired_price : Double) : this(startCity, finishCity, cargo_weight){

        desired_speed = _desired_speed
        desired_price = _desired_price
    }
}

class agency(var customer : client){
    var profit : Double = 0.0 // доход с одного клиента + к себестоимости 18 процентов с ндс

    // Доход с одного клиента
    fun one_profit(price: Double): Double{
        return profit + price + price * 0.18
    }

    // Метод возращает тип траспорта для ревозки груза
    fun type_of_transport(): String{
        var trnsp = ""
/*        if ((customer.startCity.type == "big" && customer.finishCity.type == "big") ||
            (customer.startCity.type == "middle" && customer.finishCity.type == "middle") ||
            (customer.startCity.type == "big" && customer.finishCity.type == "middle") ||
            (customer.startCity.type == "middle" && customer.finishCity.type == "big")){
            if (customer.startCity.weather < 3 && customer.finishCity.weather < 3 ){
                if (customer.startCity.type == "big" && customer.finishCity.type == "big"
                    && customer.startCity.weather == 1 && customer.finishCity.weather == 1){
                    trnsp = "avia"
                }else trnsp = "auto"
            }else trnsp = "train"
        }
*/
        // Для полета
        if (customer.startCity.type == "big" && customer.finishCity.type == "big"
            && customer.startCity.weather == 1 && customer.finishCity.weather == 1){
            trnsp = "avia"
        }
        // Для поезда
        else if ((customer.startCity.type == "big" && customer.finishCity.type == "big") ||
            (customer.startCity.type == "middle" && customer.finishCity.type == "middle") ||
            (customer.startCity.type == "big" && customer.finishCity.type == "middle") ||
            (customer.startCity.type == "middle" && customer.finishCity.type == "big")){
            trnsp = "train"
        }
        // для автомобиля
        else if (customer.startCity.weather < 3 && customer.finishCity.weather < 3 ){
            trnsp = "auto"
        }
        else{
            trnsp = "0"
        }
        return trnsp
    }
    // Метод возвращает реальное время доставки груза (в часах)
    fun cargo_time(dist: Int): Double{
        var trnsp = type_of_transport()
        var time = 0.0
        if (trnsp == "avia"){
            var avia = Avia(dist)
            time = avia.cargo_time(dist, customer)
        }
        else if (trnsp == "train"){
            var train = Train(dist)
            time = train.cargo_time(dist, customer)
        }
        else if (trnsp == "auto"){
            var auto = Auto(dist)
            time = auto.cargo_time(dist, customer)
        }
        else{
            println("Неопределен тип транспорта, возможно дело в погоде!")
        }
        return time
    }
    //Метод возвращает цену за доставку груза
    fun cargo_price(dist: Int): Double {
        var trnsp = type_of_transport()
        var price = 0.0
        if (trnsp != "0") {
            if (trnsp == "avia") {
                var avia = Avia(dist)
                price = avia.cargo_price(dist, customer)
                if (price == 0.0){
                    //price = avia.cargo_price(dist, customer)
                    var train = Train(dist)
                    price = train.cargo_price(dist, customer)
                    if (price == 0.0){
                        var auto = Auto(dist)
                        price = auto.cargo_price(dist, customer)
                       /* if (price == 0.0)
                            println("Груз невозможно перевезти на траспорте $trnsp, проблема с весов груза и ее опредлением для траспорта!!! ")*/
                    }
                }
            } else if (trnsp == "train") {
                var train = Train(dist)
                price = train.cargo_price(dist, customer)
                if (price == 0.0){
                    var auto = Auto(dist)
                    price = auto.cargo_price(dist, customer)
                    /*if (price == 0.0)
                        println("Груз невозможно перевезти на траспорте $trnsp, проблема с весов груза и ее опредлением для траспорта!!! ")*/
                }

            } else if (trnsp == "auto") {
                var auto = Auto(dist)
                price = auto.cargo_price(dist, customer)
                /*if (price == 0.0)
                    println("Груз невозможно перевезти на траспорте $trnsp, проблема с весов груза и ее опредлением для траспорта!!! ")*/
            }

            if (price == 0.0) {
                //println("Груз невозможно перевезти на всех видах траспорте, проблема с весом груза и ее опредлением для траспорта!!! ")
            }
            return price
        } else return 0.0
    }
}


fun main() {
    // Рандом для погоды
    var weather: Int = (1..3).random()

    // Создаем города
    var Moscow = big_city("big", weather, true, "Moscow")
    weather = (1..3).random()
    var SPB = big_city("big", weather, true, "SPB")
    weather = (1..3).random()
    var Kazan = middle_city("middle", weather, false, "Kazan")
    weather = (1..3).random()
    var Ufa = middle_city("middle", weather, false, "Ufa")

  /*  var from_Moscow = mapOf("SPB" to 634, "Kazan" to 717, "Ufa" to 1164)
    var from_SPB = mapOf("Moscow" to 634, "Kazan" to 1198, "Ufa" to 1631)
    var from_Kazan = mapOf("Moscow" to 717, "SPB" to 1198, "Ufa" to 528)
    var from_Ufa = mapOf("Moscow" to 1164, "SPB" to 1631, "Kazan" to 528)
  */
    //////////////////////////////////////////////////////////////////////////
    var cities = listOf("Moscow", "SPB", "Kazan", "Ufa") // список всех городов
    // Создаем список с городами, каждому из которых ставим в соответствие уникальную вершину
    var _vertex = mutableListOf<Vertex>()
    for (i in 1..cities.size){
        _vertex.add(Vertex(i, cities[i-1]))
    }

    // Создаем список ребер между вершинами
    var _edges = mutableListOf<Edge>()
    for (i in 0..cities.size-1){
        for (j in i+1..cities.size-1){
            if (i != j)
                _edges.add(Edge(_vertex[i], _vertex[j]))
            _vertex[i].addEdge(Edge(_vertex[i], _vertex[j]))
        }
    }

    // Устанавливаем вес ребра(расстояние между городами)
    println("Расстояния между городами:")
    for (i in 0.._edges.size-1){
        _edges[i].dist = (100..1800).random()
        println("${_edges[i]} : ${_edges[i].dist}")
    }
    println()

    var _graph = Graph(_vertex)
    //_graph.show()
    // Находим расстояние между городами по их названию, используя список весов ребер
    //println(_edges)
   // println(_graph.find_distance("Ufa", "SPB", _edges))
    println("Информация по заказу для клиента №1:")
    var customer1 = client(Moscow, SPB, 0.5)
    var transport_agency1 = agency(customer1)
    var dist1 = _graph.find_distance(customer1.startCity.name, customer1.finishCity.name, _edges)
    var weight1 = customer1.cargo_weight
    var time1 = transport_agency1.cargo_time(dist1)

    // Окргуляем цену до тысячных
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.DOWN
    var price1 = df.format(transport_agency1.cargo_price(dist1))

    if (weight1 != 0.0 && price1 != "0") {
        println("Время траспортировки: ${time1} груза массой: ${weight1} кг на транспорте: ${transport_agency1.type_of_transport()}")
        println("Стоимость доставки: ${price1} рублей")
        var profit1 = df.format(transport_agency1.one_profit(transport_agency1.cargo_price(dist1)))
        println("Доход агенства с одного заказчика составляет: ${profit1}")
    }
    else{
        println("Ожидайте изменения погоды в городах! В следствии этой проблемы, в данное время нельзя передать ваш груз.\n" +
                "Также возможно проблема в весе груза (на заказ принимаются грузы весом не менее 1 кг).\n" +
                "За ожидание вам возместится ущерб в размере 10% от стоимости перевозки\n")
    }

    println("Информация по заказу для клиента №2:")
    var customer2 = client(Ufa, Kazan, 345.5)
    var transport_agency2 = agency(customer2)
    var dist2 = _graph.find_distance(customer2.startCity.name, customer2.finishCity.name, _edges)
    var weight2 = customer2.cargo_weight
    var time2 = transport_agency2.cargo_time(dist2)

    // Окргуляем цену до тысячных
    var price2 = df.format(transport_agency2.cargo_price(dist2))

    if (weight2 != 0.0 && price2 != "0") {
        println("Время траспортировки: ${time2} груза массой: ${weight2} кг на транспорте: ${transport_agency2.type_of_transport()}")
        println("Стоимость доставки: ${price2} рублей")
        var profit2 = df.format(transport_agency2.one_profit(transport_agency2.cargo_price(dist2)))
        println("Доход агенства с одного заказчика составляет: ${profit2}")
    }
    else{
        println("Ожидайте изменения погоды в городах! В следствии этой проблемы, в данное время нельзя передать ваш груз.\n" +
                "Также возможно проблема в весе груза (на заказ принимаются грузы весом не менее 1 кг).\n" +
                "За ожидание вам возместится ущерб в размере 10% от стоимости перевозки")
    }
}

class Vertex(val id : Int, var city : String, var visited : Boolean = false){
    var edges = mutableListOf<Edge>()
    fun addEdge (edge: Edge){
        edges.add(edge)
    }
    override fun toString(): String = id.toString() + "-" + city
}
class Edge(var a : Vertex, var b : Vertex){
    var dist : Int = 0
    override fun toString(): String = "${a.city} <-> ${b.city}"
}
class Graph(var vertices: List<Vertex>){
    fun show() {
        vertices.forEach{vertex -> println("$vertex -> ${vertex.edges.joinToString(", ")}")
        }
    }
    fun find_distance(start_city : String, last_city: String, edges: List<Edge>): Int{
        // найдем номера вершин требуемых городов
        var v1: Int = 0
        var v2: Int = 0
        var dist : Int = 0
        for (i in 0..vertices.size-1){
            if(vertices[i].city == start_city)
                v1 = vertices[i].id
            if(vertices[i].city == last_city)
                v2 = vertices[i].id
        }
        for (i in 0..edges.size-1){
            if((edges[i].a.id == v1 && edges[i].b.id == v2) || (edges[i].a.id == v2 && edges[i].b.id == v1))
                dist = edges[i].dist
        }
        return dist
    }
}
