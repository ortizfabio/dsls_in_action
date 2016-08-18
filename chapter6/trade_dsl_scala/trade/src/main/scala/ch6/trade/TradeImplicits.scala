// Listing 6.6 TradeImplicits defines the conversion functions
// Listing 6.7 The implicit definitions in TradeImplicits

package ch6.trade
package dsl

import api._
object TradeImplicits {

  type Quantity = Int
  type WithInstrumentQuantity = (Instrument, Quantity)
  type WithAccountInstrumentQuantity = (Account, Instrument, Quantity)
  type WithMktAccountInstrumentQuantity = (Market, Account, Instrument, Quantity)
  type Money = (Int, Currency)

  class InstrumentHelper(qty: Quantity) {
    def discount_bonds(db: DiscountBond) = (db:DiscountBond, qty:Quantity)
    def equities(eq: Equity) = (eq:Equity, qty:Quantity)
  }

  class AccountHelper(wiq: WithInstrumentQuantity) {
    def for_client(ca: ClientAccount) = (ca:ClientAccount, wiq._1:Instrument, wiq._2:Quantity)
  }

  class MarketHelper(waiq: WithAccountInstrumentQuantity) {
    def on(mk: Market) = (mk:Market, waiq._1:Account, waiq._2:Instrument, waiq._3:Quantity)
  }

  class RichInt(v: Int) {
    def ccy(c: Currency) = (v:Int, c:Currency)
  }

  class PriceHelper(wmaiq: WithMktAccountInstrumentQuantity) {
    def at(c: Money) = (c:Money, wmaiq._1:Market, wmaiq._2:Account, wmaiq._3:Instrument, wmaiq._4:Quantity)
  }

  implicit def quantity2InstrumentHelper(qty: Quantity) = new InstrumentHelper(qty)
  implicit def withAccount(wiq: WithInstrumentQuantity) = new AccountHelper(wiq)
  implicit def withMarket(waiq: WithAccountInstrumentQuantity) = new MarketHelper(waiq)
  implicit def withPrice(wmaiq: WithMktAccountInstrumentQuantity) = new PriceHelper(wmaiq)
  implicit def int2RichInt(v: Int) = new RichInt(v)

  import Util._
  implicit def Tuple2Trade(t: (Money, Market, Account, Instrument, Quantity)):Trade =
    t match {
      case ((money, mkt, account, ins: DiscountBond, qty)) =>
        FixedIncomeTradeImpl(
          tradingAccount = account,
          instrument = ins,
          currency = money._2,
          tradeDate = TODAY,
          market = mkt,
          quantity = qty,
          unitPrice = money._1)

      case ((money, mkt, account, ins: Equity, qty)) =>
        EquityTradeImpl(
          tradingAccount = account,
          instrument = ins,
          currency = money._2,
          tradeDate = TODAY,
          market = mkt,
          quantity = qty,
          unitPrice = money._1)
      case _ => throw new IllegalArgumentException("invalid trade type")
    }
}
