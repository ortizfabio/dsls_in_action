package ch6.trade
package driver

import dsl._

object Main {
  import TradeImplicits._

  import api._
  import Accounts._
  import Instruments._
  import Markets._
  import Currencies._
  


  def main(args: Array[String]): Unit = {
    import dsl.TradeImplicits.Tuple2Trade
    val fixTrade = 200 discount_bonds IBM for_client NOMURA on NYSE at 72.ccy(USD)
    val fixedIncomeTrade: Trade = Tuple2Trade(fixTrade)
    println(api.FixedIncomeTradingService.cashValue(fixedIncomeTrade.asInstanceOf[FixedIncomeTrade]))

    val eqTrade = 200 equities IBM_EQ for_client NOMURA on NYSE at 72.ccy(USD)
    val equityTrade = Tuple2Trade(eqTrade)
    println(api.EquityTradingService.cashValue(equityTrade.asInstanceOf[EquityTrade]))

  }

}
