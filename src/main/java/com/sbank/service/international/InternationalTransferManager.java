package com.sbank.service.international;

import com.sbank.controller.request.InternationalTransferRequest;
import com.sbank.domain.Account;
import com.sbank.domain.international.InternationalTransferEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InternationalTransferManager {

    private final List<InternationalTransferEventListener> subscribers = new CopyOnWriteArrayList<>();

    public void subscribe(InternationalTransferEventListener listener){
        subscribers.add(listener);
    }

    public void sendMoney(Account sourceAccount, InternationalTransferRequest request) {
        sendAll(new InternationalTransferEvent(sourceAccount.getAccountNo(), request.getAmount(), request.getTargetAccountIBAN(), request.getTargetAccountSwiftCode()) );
    }

    private void sendAll(InternationalTransferEvent internationalTransferEvent) {
        subscribers.forEach( s -> s.receive(internationalTransferEvent));
    }

}
