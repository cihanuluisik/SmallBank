package com.sbank.acceptance.account.transaction.international;

import com.sbank.domain.international.InternationalTransferEvent;
import com.sbank.service.international.InternationalTransferEventListener;
import com.sbank.service.international.InternationalTransferManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MockedInternatioalTransferListener implements InternationalTransferEventListener {

    @Autowired
    InternationalTransferManager internationalTransferManager;

    private volatile InternationalTransferEvent lastEvent;

    @PostConstruct
    public void subscribe() {
        internationalTransferManager.subscribe(this);
    }

    @Override
    public void receive(InternationalTransferEvent internationalTransferEvent) {
        this.lastEvent = internationalTransferEvent;
    }

    public InternationalTransferEvent getNextMessage() {
        return this.lastEvent;
    }
}
