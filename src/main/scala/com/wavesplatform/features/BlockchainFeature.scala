package com.wavesplatform.features

case class BlockchainFeature private (id: Short, description: String)

object BlockchainFeatures {

  val SmallerMinimalGeneratingBalance = BlockchainFeature(1, "Minimum Generating Balance of 1000 WAVES")
  val NG                              = BlockchainFeature(2, "NG Protocol")
  val MassTransfer                    = BlockchainFeature(3, "Mass Transfer Transaction")
  val SmartAccounts                   = BlockchainFeature(4, "Smart Accounts")
  val DataTransaction                 = BlockchainFeature(5, "Data Transaction")
  val BurnAnyTokens                   = BlockchainFeature(6, "Burn Any Tokens")
  val FeeSponsorship                  = BlockchainFeature(7, "Fee Sponsorship")
  val FairPOS                         = BlockchainFeature(8, "Fair POS")

  private val dict = Seq(
    SmallerMinimalGeneratingBalance,
    NG,
    MassTransfer,
    /*SmartAccounts, */
    DataTransaction,
    BurnAnyTokens,
    FeeSponsorship,
    FairPOS
  ).map(f => f.id -> f).toMap

  val implemented: Set[Short] = dict.keySet

  def feature(id: Short) = dict(id)
}
