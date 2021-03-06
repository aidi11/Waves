package com.wavesplatform.settings

import com.typesafe.config.Config
import com.wavesplatform.metrics.Metrics
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

case class WavesSettings(directory: String,
                         ntpServer: String,
                         dbSettings: DBSettings,
                         extensions: Seq[String],
                         networkSettings: NetworkSettings,
                         walletSettings: WalletSettings,
                         blockchainSettings: BlockchainSettings,
                         minerSettings: MinerSettings,
                         restAPISettings: RestAPISettings,
                         synchronizationSettings: SynchronizationSettings,
                         utxSettings: UtxSettings,
                         featuresSettings: FeaturesSettings,
                         metrics: Metrics.Settings,
                         config: Config)

object WavesSettings extends CustomValueReaders {
  def fromRootConfig(rootConfig: Config): WavesSettings = {
    val waves = rootConfig.getConfig("waves")

    val directory               = waves.as[String]("directory")
    val ntpServer               = waves.as[String]("ntp-server")
    val dbSettings              = waves.as[DBSettings]("db")
    val extensions              = waves.as[Seq[String]]("extensions")
    val networkSettings         = waves.as[NetworkSettings]("network")
    val walletSettings          = waves.as[WalletSettings]("wallet")
    val blockchainSettings      = waves.as[BlockchainSettings]("blockchain")
    val minerSettings           = waves.as[MinerSettings]("miner")
    val restAPISettings         = waves.as[RestAPISettings]("rest-api")
    val synchronizationSettings = waves.as[SynchronizationSettings]("synchronization")
    val utxSettings             = waves.as[UtxSettings]("utx")
    val featuresSettings        = waves.as[FeaturesSettings]("features")
    val metrics                 = rootConfig.as[Metrics.Settings]("metrics") // TODO: Move to waves section

    WavesSettings(
      directory,
      ntpServer,
      dbSettings,
      extensions,
      networkSettings,
      walletSettings,
      blockchainSettings,
      minerSettings,
      restAPISettings,
      synchronizationSettings,
      utxSettings,
      featuresSettings,
      metrics,
      rootConfig
    )
  }
}
