package ru.unlocker.slurm.spark

import scala.util.Try

object Work011 {
  // АТД для записи
  sealed trait Record

  // плохая запись
  case class BadRecord(recStr: String) extends Record

  // хорошая запись
  case class CategoryQuarterlyRecord(quarter: String, category: String, revenue: Float) extends Record


  /**
   * Валидируем запись
   *
   * @param recStr входная строка
   * @return запись
   */
  def validateRecord(recStr: String): Record = {
    val t = for {
      inp <- Try(recStr.split(',')).filter(_.length == 3)
      rev <- Try(inp(2).toFloat)
    } yield {
      CategoryQuarterlyRecord(inp(0), inp(1), rev)
    }
    t.fold(_ => BadRecord(recStr), x => x)
  }

  /**
   * Разбор данных
   *
   * @return списки хороших и плохих записей
   */
  def parseData(): (List[CategoryQuarterlyRecord], List[BadRecord]) = {
    val revenueInfo: String =
      """Q1-2018,Трантор - Терминус,10.33;
        |Q1-2018,Трантор - Хари Селдон,7.85;
        |Q1-2018,Трантор - Анакреон,3.45;
        |Q2-2018,Трантор - Смитония,7.63;
        |Q2-2018,Трантор - Гаэя,5.05;
        |Q2-2018,Трантор - Корелл,-;
        |Q3-2018,Трантор - Хельикон,1.31;
        |Q3-2018,Трантор - Компор,3.95;
        |Q3-2018,Трантор - Синтаксис,1.50;
        |Q4-2018,Трантор - Кальган,5.71;
        |Q4-2018,Трантор - Неверон,6.52;
        |Q4-2018,Трантор - Радоле,4.15
    """.stripMargin

    revenueInfo.split(";\\n")
      .toList
      .map(validateRecord)
      .foldRight((List.empty[CategoryQuarterlyRecord], List.empty[BadRecord])) {
        (it, res) =>
          it match {
            case bad: BadRecord => res.copy(_2 = bad +: res._2)
            case good: CategoryQuarterlyRecord => res.copy(_1 = good +: res._1)
          }
      }
  }

  /**
   * Анализатор
   *
   * @param list исходные данные
   */
  class CompanyPerformance(list: List[CategoryQuarterlyRecord]) {
    def getTotalRevenue: Float = list.map(_.revenue).sum

    def getCategoryRevenue(category: String): Float = list.filter(_.category == category).map(_.revenue).sum
  }

  def main(args: Array[String]): Unit = {
    val (goodrecs, badrecs) = parseData()
    println(s"Bad records = $badrecs")
    println(s"Good records = $goodrecs")
    val companyPerf2018 = new CompanyPerformance(goodrecs)
    val totalRevenue = companyPerf2018.getTotalRevenue
    println(s"totalRevenue = $totalRevenue")
    val totalRevenueInCategory = companyPerf2018.getCategoryRevenue("Exercise_Fitness")
    println(s"totalRevenueInCategory = $totalRevenueInCategory")
  }
}
