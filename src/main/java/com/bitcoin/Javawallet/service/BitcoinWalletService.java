package com.bitcoin.Javawallet.service;

import java.io.File;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.params.MainNetParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BitcoinWalletService {

    private WalletAppKit kit;

    public BitcoinWalletService(@Value("${bitcoin.network}") String network, @Value("${bitcoin.wallet.file}") String walletFile) {
        NetworkParameters params = null;
        if ("testnet".equals(network)) {
            params = TestNet3Params.get();
        } else {
            params = MainNetParams.get();
        }
        this.kit = new WalletAppKit(params, new File(walletFile), "wallet") {
            @Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, when the wallet file has been loaded
            }
        };
        kit.setAutoSave(true);
    }

    public WalletAppKit getKit() {
        return kit;
    }

    public void start() {
        kit.startAsync();
        kit.awaitRunning();
    }

    public void stop() {
        kit.stopAsync();
        kit.awaitTerminated();
    }
}
