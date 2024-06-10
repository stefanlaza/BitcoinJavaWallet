package com.bitcoin.Javawallet.controller;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitcoin.Javawallet.service.BitcoinWalletService;

@Controller
public class BitcoinWalletController {

    @Autowired
    private BitcoinWalletService walletService;

  @GetMapping("/")
public String index(Model model) {
    WalletAppKit kit = walletService.getKit();
    kit.awaitRunning(); // Wait for the wallet initialization to complete
    Wallet wallet = kit.wallet(); // Now it's safe to access the wallet object

    model.addAttribute("balance", wallet.getBalance().toFriendlyString());
    model.addAttribute("transactions", wallet.getTransactionsByTime());
    return "index";
}


    @PostMapping("/send")
    public String send(@RequestParam String address, @RequestParam String amount) {
        try {
            Coin value = Coin.parseCoin(amount);
            Address toAddress = Address.fromString(walletService.getKit().wallet().getNetworkParameters(), address);
            walletService.getKit().wallet().sendCoins(walletService.getKit().peerGroup(), toAddress, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/receive")
    public String receive(Model model) {
        model.addAttribute("label", "Your Label Here");
        return "receive";
    }

    @PostMapping("/receive")
    public String receivePost(@RequestParam String label) {
        // Process received label
        return "redirect:/";
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        // Add logic to fetch transactions
        model.addAttribute("transactions" /* Fetch transactions */);
        return "transactions";
    }
}
